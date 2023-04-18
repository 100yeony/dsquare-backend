

package com.ktds.dsquare.board.qna.service;

import com.ktds.dsquare.board.qna.domain.Answer;
import com.ktds.dsquare.board.qna.domain.Category;
import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.board.qna.dto.BriefQuestionResponse;
import com.ktds.dsquare.board.qna.dto.CategoryResponse;
import com.ktds.dsquare.board.qna.dto.QuestionRequest;
import com.ktds.dsquare.board.qna.dto.QuestionResponse;
import com.ktds.dsquare.board.qna.repository.AnswerRepository;
import com.ktds.dsquare.board.qna.repository.CategoryRepository;
import com.ktds.dsquare.board.qna.repository.QuestionRepository;
import com.ktds.dsquare.member.Member;
import com.ktds.dsquare.member.MemberRepository;
import com.ktds.dsquare.member.dto.response.MemberInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    private final CategoryResponse categoryResponse;


    //create - 질문글 작성
    @Transactional
    public void createQuestion(QuestionRequest dto) {
        //workYn
        Question question = new Question();
        Member writer = memberRepository.findById(dto.getWriterId()).orElseThrow(() -> new RuntimeException("Writer does not exist"));
        question.setWriter(writer);
        question.setTitle(dto.getTitle());
        question.setContent(dto.getContent());
        question.setViewCnt(0L);
        question.setAtcId(dto.getAtcId());
        question.setDeleteYn(false);

        LocalDateTime now = LocalDateTime.now();
        question.setCreateDate(now);
        question.setLastUpdateDate(now);

        Category category = categoryRepository.findById(dto.getCid()).orElseThrow(() -> new RuntimeException("Category does not exist"));
        question.setCategory(category);
        questionRepository.save(question);
    }

    //read - 질문글 전체 조회
    public List<BriefQuestionResponse> getAllQuestions(Boolean workYn) {
        // deleteYn = false만 필터링 한 후 qid 기준으로 정렬
        Category notWorkCategory = categoryRepository.findByCid(2)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        List<Question> questions;
        List<BriefQuestionResponse> briefQuestions = new ArrayList<>();
        if(workYn) questions = questionRepository.findByDeleteYnAndCategoryNotOrderByCreateDateDesc(false, notWorkCategory);
        else questions = questionRepository.findByDeleteYnAndCategoryOrderByCreateDateDesc(false, notWorkCategory);

        for (Question Q : questions) {
            Category category = Q.getCategory();
            Member member = Q.getWriter();
            List<Answer> answers = answerRepository.findByQuestionAndDeleteYn(Q, false);
            Boolean managerAnswerYn = false;
            for (Answer A : answers) {
                if (category.getManagerId() == A.getWriter().getId()) {
                    managerAnswerYn = true;
                    break;
                }
            }
            briefQuestions.add(BriefQuestionResponse.toDto(Q, MemberInfo.toDto(member), categoryResponse.toDto(category), (long)answers.size(), managerAnswerYn));
        }
        return briefQuestions;
    }

    //read - 질문글 상세 조회
    public QuestionResponse getQuestionDetail(Long qid) {
        Question question = questionRepository.findById(qid)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        question.increaseViewCnt();
        questionRepository.save(question);

        Member member = question.getWriter();
        MemberInfo writer = MemberInfo.toDto(member);
        return QuestionResponse.toDto(question, writer, categoryResponse.toDto(question.getCategory()));
    }

    public Question getQuestionById(Long qid){
        return questionRepository.findById(qid)
                .orElseThrow(() -> new RuntimeException("Question not found"));
    }

    // 질문글 수정
    @Transactional
    public void updateQuestion(Long qid, QuestionRequest request) {
        Question question = questionRepository.findById(qid)
                .orElseThrow(() -> new RuntimeException("Update Question Fail"));

        question.setTitle(request.getTitle());
        question.setContent(request.getContent());
        Category category = categoryRepository.findById(request.getCid()).orElseThrow(()-> new RuntimeException("category not found"));
        question.setCategory(category);
        question.setLastUpdateDate(LocalDateTime.now());
        question.setAtcId(request.getAtcId());
    }

    // 질문글 삭제
    @Transactional
    public void deleteQuestion(Long qid) {
        Question question = questionRepository.findById(qid)
                .orElseThrow(() -> new RuntimeException("Delete Question Fail"));
        List<Answer> answerList = answerRepository.findByQuestionAndDeleteYn(question, false);

        // 답변글이 이미 존재할 때 => HTTP Status로 처리해줘야 함(추후 수정 필요)
        if(!answerList.isEmpty()) throw new RuntimeException("Delete Question Fail - Reply exists");

        question.setDeleteYn(true);
        question.setLastUpdateDate(LocalDateTime.now());
    }


    /* search - Q&A 검색(카테고리, 사용자, 제목+내용)
     * 전제조건 : workYn은 필수, deleteYn=false
     * 1. category만 검색하는 경우 -> key,value x
     * 2. category없이 제목+내용 or 작성자로 검색하는 경우 -> cid X
     * 3. 둘 다 검색하는 경우 -> cid, key, value
     * */
    public List<BriefQuestionResponse> search(Boolean workYn, Integer cid, String key, String value){
        //deleteYn = false인 것만 조회
        Specification<Question> filter = Specification.where(QuestionSpecification.equalNotDeleted(false));
        //업무 구분
        if(workYn){
            //업무 - cid=2를 제외한 나머지
            if(cid == null) {
                filter = filter.and(QuestionSpecification.notEqualNotWork(2));
            }
            //카테고리 검색 - cid 필터링
            if(cid != null){
                filter = filter.and(QuestionSpecification.equalCategory(cid));
            }
        } else{
            //비업무 - cid=2
            filter = filter.and(QuestionSpecification.equalCategory(2));
        }

        //사용자 이름 검색(2글자로도 포함된 사람 검색 & 다른 조건과 모두 AND)
        if (key != null && key.equals("member") && value != null) {
            List<Member> members = memberRepository.findByNameContaining(value);
            try {
                if (members.size() > 0) {
                    List<Member> writerIds = new ArrayList<>();
                    for (Member M : members) {
                        Member m = memberRepository.findById(M.getId())
                                .orElseThrow(() -> new RuntimeException("Member Not Found"));
                        writerIds.add(m);
                    }
                    filter = filter.and(QuestionSpecification.inWriter(writerIds));
                } else {
                    // 매칭되는 멤버가 없으면 빈 리스트 반환
                    return Collections.emptyList();
                }
            } catch (RuntimeException ex) {
                // Exception이 발생한 경우 빈 리스트 반환
                return Collections.emptyList();
            }
        }
        //제목+내용 검색
        if(key!=null && key.equals("titleAndContent") && value != null){
            filter = filter.and(QuestionSpecification.equalTitleAndContentContaining(value));
        }

        List<Question> questionList = questionRepository.findAll(filter, Sort.by(Sort.Direction.DESC, "createDate"));
        List<BriefQuestionResponse> searchResults = new ArrayList<>();

        //BriefQuestionResponse 객체로 만들어줌
        for(Question q: questionList){
            CategoryResponse categoryRes = CategoryResponse.toDto(q.getCategory());
            List<Answer> answers = answerRepository.findByQuestionAndDeleteYn(q, false);
            Boolean managerAnswerYn = false;
            for (Answer A : answers) {
                if (q.getCategory().getManagerId() == A.getWriter().getId()) {
                    managerAnswerYn = true;
                    break;
                }
            }
            searchResults.add(BriefQuestionResponse.toDto(q, MemberInfo.toDto(q.getWriter()),categoryRes ,(long)answers.size(), managerAnswerYn));
        }

        return searchResults;
    }


    public List<BriefQuestionResponse> searchMap(Map<String, String> params){
        //deleteYn = false인 것만 조회
        Specification<Question> filter = Specification.where(QuestionSpecification.equalNotDeleted(false));
        //업무 구분

        if(params.containsKey("workYn") ){
            Boolean isWork = Boolean.valueOf(params.get("workYn"));
            if(isWork){
                filter = filter.and(QuestionSpecification.notEqualNotWork(2));
            }
//            Integer cid = 2;
            //업무 - cid=2를 제외한 나머지
//            if(params.containsKey("cid") == null) {
//                filter = filter.and(QuestionSpecification.notEqualNotWork(2));
//            }
            //카테고리 검색 - cid 필터링
//            if(params.containsKey("cid")){
//                Integer cidValue = Integer.parseInt(params.get("cid"));
//                filter = filter.and(QuestionSpecification.equalCid(cidValue));
//            }
        }


        List<Question> questionList = questionRepository.findAll(filter, Sort.by(Sort.Direction.DESC, "createDate"));
        List<BriefQuestionResponse> searchResults = new ArrayList<>();

        //BriefQuestionResponse 객체로 만들어줌
        for(Question q: questionList){
            CategoryResponse categoryRes = CategoryResponse.toDto(q.getCategory());
            List<Answer> answers = answerRepository.findByQuestionAndDeleteYn(q, false);
            Boolean managerAnswerYn = false;
            for (Answer A : answers) {
                if (q.getCategory().getManagerId() == A.getWriter().getId()) {
                    managerAnswerYn = true;
                    break;
                }
            }
            searchResults.add(BriefQuestionResponse.toDto(q, MemberInfo.toDto(q.getWriter()),categoryRes ,(long)answers.size(), managerAnswerYn));
        }

        return searchResults;
    }



}

