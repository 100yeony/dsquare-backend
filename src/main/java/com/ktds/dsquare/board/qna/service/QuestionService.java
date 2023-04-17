

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
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@RestController
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
        question.setCid(category);
        questionRepository.save(question);
    }

    //read - 질문글 전체 조회
    public List<BriefQuestionResponse> getAllQuestions(Boolean workYn) {
        // deleteYn = false만 필터링 한 후 qid 기준으로 정렬
        Category notWorkCategory = categoryRepository.findByCid(2)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        List<Question> questions;
        List<BriefQuestionResponse> briefQuestions = new ArrayList<>();
        if(workYn) questions = questionRepository.findByDeleteYnAndCidNotOrderByCreateDateDesc(false, notWorkCategory);
        else questions = questionRepository.findByDeleteYnAndCidOrderByCreateDateDesc(false, notWorkCategory);

        for (Question Q : questions) {
            Category category = Q.getCid();
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

        Member member = question.getWriter();
        MemberInfo writer = MemberInfo.toDto(member);
        return QuestionResponse.toDto(question, writer, categoryResponse.toDto(question.getCid()));
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
            filter = filter.and(QuestionSpecification.notEqualNotWork(2));
        } else{
            //비업무 - cid=2
            filter = filter.and(QuestionSpecification.equalNotWork(2));
        }
        //카테고리 검색
        if(cid != null){
            filter = filter.and(QuestionSpecification.equalCid(cid));
        }
        //사용자 이름 검색
        if(key!=null && key.equals("member") && value != null){
            Member member = memberRepository.findByName(value);
            Long mid = member.getId();
            filter = filter.and(QuestionSpecification.equalWriterId(mid));
        }
        //제목+내용 검색
        if(key!=null && key.equals("titleAndContent") && value != null){
            filter = filter.and(QuestionSpecification.equalTitleAndContentContaining(value));
        }

        List<Question> questionList = questionRepository.findAll(filter, Sort.by(Sort.Direction.DESC, "createDate"));
        List<BriefQuestionResponse> searchResults = new ArrayList<>();

        //BriefQuestionResponse 객체로 만들어줌
        for(Question q: questionList){
            Member member = q.getWriter();
            CategoryResponse categoryRes = CategoryResponse.toDto(q.getCid());
            List<Answer> answers = answerRepository.findByQuestionAndDeleteYn(q, false);
            Boolean managerAnswerYn = false;
            for (Answer A : answers) {
                if (q.getCid().getManagerId() == A.getWriter().getId()) {
                    managerAnswerYn = true;
                    break;
                }
            }
            searchResults.add(BriefQuestionResponse.toDto(q, MemberInfo.toDto(member),categoryRes ,(long)answers.size(), managerAnswerYn));
        }

        return searchResults;
    }



}

