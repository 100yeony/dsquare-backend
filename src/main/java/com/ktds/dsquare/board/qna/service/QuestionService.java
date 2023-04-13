package com.ktds.dsquare.board.qna.service;

import com.ktds.dsquare.board.qna.domain.Answer;
import com.ktds.dsquare.board.qna.domain.Category;
import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.board.qna.dto.BriefQuestionResponse;
import com.ktds.dsquare.board.qna.dto.QuestionRequest;
import com.ktds.dsquare.board.qna.dto.QuestionResponse;
import com.ktds.dsquare.board.qna.repository.AnswerRepository;
import com.ktds.dsquare.board.qna.repository.CategoryRepository;
import com.ktds.dsquare.board.qna.repository.QuestionRepository;
import com.ktds.dsquare.member.Member;
import com.ktds.dsquare.member.MemberRepository;
import com.ktds.dsquare.member.dto.response.MemberInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;


    //create - 질문글 작성
    @Transactional
    public void createQuestion(QuestionRequest dto) {
        //workYn
        Question question = new Question();
        question.setWriterId(dto.getWriterId());
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
            Long writerId = Q.getWriterId();

            Category category = Q.getCid();
            Member member = memberRepository.findById(writerId)
                    .orElseThrow(() -> new RuntimeException("Member not found"));
            List<Answer> answers = answerRepository.findByQuestionAndDeleteYn(Q, false);
            Boolean managerAnswerYn = false;
            for (Answer A : answers) {
               if (category.getManagerId() == A.getWriterId()) {
                   managerAnswerYn = true;
                   break;
               }
            }
            briefQuestions.add(BriefQuestionResponse.toDto(Q, MemberInfo.toDto(member), category, (long)answers.size(), managerAnswerYn));
        }
        return briefQuestions;
    }

    //read - 질문글 상세 조회
    public QuestionResponse getQuestionDetail(Long qid) {
        Question question = questionRepository.findById(qid)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        question.increaseViewCnt();

        Long writerId = question.getWriterId();

        Member member = memberRepository.findById(writerId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        MemberInfo writer = MemberInfo.toDto(member);


        return QuestionResponse.toDto(question, writer, question.getCid());
    }

    public Question getQuestionById(Long qid){
        return questionRepository.findById(qid)
                .orElseThrow(() -> new RuntimeException("Question not found"));
    }

    // 질문글 수정
    @Transactional
    public void updateQuestion(Long qid, QuestionRequest request) {
        Question question = questionRepository.findById(qid).orElseThrow(() -> new RuntimeException("Update Question Fail"));

        question.setTitle(request.getTitle());
        question.setContent(request.getContent());
        question.setLastUpdateDate(LocalDateTime.now());
        question.setAtcId(request.getAtcId());
    }

    // 질문글 삭제
    @Transactional
    public void deleteQuestion(Long qid) {
        Question question = questionRepository.findById(qid).orElseThrow(() -> new RuntimeException("Delete Question Fail"));
        List<Answer> answerList = answerRepository.findByQuestionAndDeleteYn(question, false);
        if(answerList.isEmpty()) {
            question.setDeleteYn(true);
            question.setLastUpdateDate(LocalDateTime.now());
        } else {
            // 답변글이 이미 존재할 때 => HTTP Status로 처리해줘야 함(추후 수정 필요)
            throw new RuntimeException("Delete Question Fail - Reply exists");
        }
    }


    //search - Q&A 검색(사용자, 제목+내용)
    public List<Question> searchByWriterId(Long writerId) {
        return questionRepository.findByWriterId(writerId);
    }

    public List<Question> searchByCid(Integer cid){

        Category category = categoryRepository.findByCid(cid)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return questionRepository.findByCid(category);

    }
    public List<Question> searchByName(String value){
        //검색 기능에서 input으로 name을 받으면 question 테이블엔 name이 없으니까 id로 member 테이블에서 이름을 찾아야 함.
        //member 테이블에서 이름 조회하여 pk 찾기 -> question 테이블에서 writerID로 질문글 찾기
        Member member = memberRepository.findByName(value);
        Long mid = member.getId();
        return questionRepository.findByWriterId(mid);
    }

    public List<Question> searchByTitleOrContent(String keyword) {
        return questionRepository.findByTitleContainingOrContentContaining(keyword, keyword);
    }

}
