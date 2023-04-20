

package com.ktds.dsquare.board.qna.service;

import com.ktds.dsquare.board.qna.domain.*;
import com.ktds.dsquare.board.qna.dto.BriefQuestionResponse;
import com.ktds.dsquare.board.qna.dto.CategoryResponse;
import com.ktds.dsquare.board.qna.dto.QuestionRequest;
import com.ktds.dsquare.board.qna.dto.QuestionResponse;
import com.ktds.dsquare.board.qna.repository.*;
import com.ktds.dsquare.member.Member;
import com.ktds.dsquare.member.MemberRepository;
import com.ktds.dsquare.member.dto.response.MemberInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    private final TagRepository tagRepository;
    private final QuestionTagRepository questionTagRepository;

    //create - 질문글 작성
    @Transactional
    public void createQuestion(QuestionRequest dto) {
        //workYn
        Question question = new Question();
        Member writer = memberRepository.findById(dto.getWriterId()).orElseThrow(() -> new EntityNotFoundException("Writer does not exist"));
        question.setWriter(writer);
        question.setTitle(dto.getTitle());
        question.setContent(dto.getContent());
        question.setViewCnt(0L);
        question.setAtcId(dto.getAtcId());
        question.setDeleteYn(false);

        LocalDateTime now = LocalDateTime.now();
        question.setCreateDate(now);
        question.setLastUpdateDate(now);

        Category category = categoryRepository.findById(dto.getCid()).orElseThrow(() -> new EntityNotFoundException("Category does not exist"));
        question.setCategory(category);
        questionRepository.save(question);

        insertNewTags(dto.getTags(), question);
    }

    //read - 질문글 전체 조회
    public List<BriefQuestionResponse> getAllQuestions(Boolean workYn) {
        // deleteYn = false만 필터링 한 후 qid 기준으로 정렬
        Category notWorkCategory = categoryRepository.findByCid(2)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        List<Question> questions;
        List<BriefQuestionResponse> briefQuestions = new ArrayList<>();
        if(workYn) questions = questionRepository.findByDeleteYnAndCategoryNotOrderByCreateDateDesc(false, notWorkCategory);
        else questions = questionRepository.findByDeleteYnAndCategoryOrderByCreateDateDesc(false, notWorkCategory);

        for (Question Q : questions) {
            Category category = Q.getCategory();
            Member member = Q.getWriter();
            List<Answer> answers = answerRepository.findByQuestionAndDeleteYn(Q, false);
            boolean managerAnswerYn = false;
            for (Answer A : answers) {
                if (category.getManagerId().equals(A.getWriter().getId())) {
                    managerAnswerYn = true;
                    break;
                }
            }
            briefQuestions.add(BriefQuestionResponse.toDto(Q, MemberInfo.toDto(member), CategoryResponse.toDto(category), (long)answers.size(), managerAnswerYn));
        }
        return briefQuestions;
    }

    //read - 질문글 상세 조회
    public QuestionResponse getQuestionDetail(Long qid) {
        Question question = questionRepository.findById(qid)
                .orElseThrow(() -> new EntityNotFoundException("Question not found"));
        question.increaseViewCnt();
        questionRepository.save(question);

        Member member = question.getWriter();
        MemberInfo writer = MemberInfo.toDto(member);
        return QuestionResponse.toDto(question, writer, CategoryResponse.toDto(question.getCategory()));
    }

    // 질문글 수정
    @Transactional
    public void updateQuestion(Long qid, QuestionRequest request) {
        Question question = questionRepository.findById(qid)
                .orElseThrow(() -> new EntityNotFoundException("Update Question Fail"));

        question.setTitle(request.getTitle());
        question.setContent(request.getContent());
        Category category = categoryRepository.findById(request.getCid()).orElseThrow(()-> new RuntimeException("category not found"));
        question.setCategory(category);
        question.setLastUpdateDate(LocalDateTime.now());
        question.setAtcId(request.getAtcId());

        // 태그 수정
        List<QuestionTag> oldQTs = question.getQuestionTags();
        List<Tag> oldTags = new ArrayList<>();
        for(QuestionTag oldQT : oldQTs) {
            oldTags.add(oldQT.getTag());
        }
        List<String> newTags = request.getTags();

        for(Tag oldTag : oldTags) {
            String oldTagName = oldTag.getName();
            if(!newTags.contains(oldTagName))
                deleteTag(question, oldTag);
            else
                newTags.remove(oldTagName);
        }
        insertNewTags(newTags, question);
    }

    // 질문글 삭제
    @Transactional
    public void deleteQuestion(Long qid) {
        Question question = questionRepository.findById(qid)
                .orElseThrow(() -> new EntityNotFoundException("Delete Question Fail"));
        List<Answer> answerList = answerRepository.findByQuestionAndDeleteYn(question, false);

        // 답변글이 이미 존재할 때 => HTTP Status로 처리해줘야 함(추후 수정 필요)
        if(!answerList.isEmpty()) throw new EntityNotFoundException("Delete Question Fail - Reply exists");

        question.setDeleteYn(true);
        question.setLastUpdateDate(LocalDateTime.now());

        // 태그 삭제
        List<QuestionTag> QTs = question.getQuestionTags();
        for (QuestionTag QT : QTs) {
            deleteTag(question, QT.getTag());
        }
    }


    /* search - Q&A 검색(카테고리, 사용자, 제목+내용)
     * 전제조건 : workYn은 필수, deleteYn=false
     * 1. category만 검색하는 경우 -> key,value x
     * 2. category없이 제목+내용 or 작성자로 검색하는 경우 -> cid X
     * 3. 둘 다 검색하는 경우 -> cid, key, value
     * */
    public List<BriefQuestionResponse> searchQnA(Boolean workYn, Integer cid, String key, String value){
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
                                .orElseThrow(() -> new EntityNotFoundException("Member Not Found"));
                        writerIds.add(m);
                    }
                    filter = filter.and(QuestionSpecification.inWriter(writerIds));
                } else {
                    // 매칭되는 멤버가 없으면 빈 리스트 반환
                    return Collections.emptyList();
                }
            } catch (EntityNotFoundException ex) {
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
            boolean managerAnswerYn = false;
            for (Answer A : answers) {
                if (q.getCategory().getManagerId().equals(A.getWriter().getId())) {
                    managerAnswerYn = true;
                    break;
                }
            }
            searchResults.add(BriefQuestionResponse.toDto(q, MemberInfo.toDto(q.getWriter()),categoryRes ,(long)answers.size(), managerAnswerYn));
        }
        return searchResults;
    }


    // 새 태그(키워드) 등록
    @Transactional
    public void insertNewTags(List<String> newTags, Question question) {
        for (String name : newTags) {
            Tag tag = tagRepository.findByName(name);
            if(tag == null) {
                tag = Tag.toEntity(name);
                tagRepository.save(tag);
            }
            QuestionTag qt = QuestionTag.toEntity(question, tag);
            questionTagRepository.save(qt);
        }
    }
    // 태그 삭제
    @Transactional
    public void deleteTag(Question question, Tag tag) {
        deleteQuestionTag(question, tag);
        if(questionTagRepository.findByTag(tag) == null) tagRepository.delete(tag);
    }
    // 태그 삭제 전 연관관계 삭제
    @Transactional
    public void deleteQuestionTag(Question question, Tag tag) {
        questionTagRepository.deleteByQuestionAndTag(question, tag);
    }

}

