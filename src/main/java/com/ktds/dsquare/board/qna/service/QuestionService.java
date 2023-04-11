package com.ktds.dsquare.board.qna.service;

import com.ktds.dsquare.board.qna.domain.Answer;
import com.ktds.dsquare.board.qna.domain.Category;
import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.board.qna.dto.QuestionDto;
import com.ktds.dsquare.board.qna.repository.AnswerRepository;
import com.ktds.dsquare.board.qna.repository.CategoryRepository;
import com.ktds.dsquare.board.qna.repository.QuestionRepository;
import com.ktds.dsquare.member.Member;
import com.ktds.dsquare.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private MemberRepository memberRepository;


    //create - 질문글 작성
    public void createQuestion(QuestionDto dto) {
        Question question = new Question();
//        question.setQid(dto.getQid());
        question.setWriterId(dto.getWriterId());
//        question.setNickname(dto.getNickname());
        //writerId로 member table 조회해서 nickname 가져오기??
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
    public List<Question> getAllQuestions() {
        // deleteYn = false만 필터링 한 후 qid 기준으로 정렬
        return questionRepository.findByDeleteYnOrderByCreateDateDesc(false);
    }

    //read - 질문글 상세 조회
    public Optional<Question> getQuestionDetail(Long qid) {
        return questionRepository.findById(qid);
    }

    // 질문글 조회수 증가
    public void increaseViewCnt(Long qid) {
        Question question = questionRepository.findById(qid)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        question.setViewCnt(question.getViewCnt()+1);
        questionRepository.save(question);
    }

    // 질문글 수정
    public void updateQuestion(Long qid, QuestionDto request) {
        Question question = questionRepository.findById(qid).orElseThrow(() -> new RuntimeException("Update Question Fail"));

        question.setTitle(request.getTitle());
        question.setContent(request.getContent());
        question.setLastUpdateDate(LocalDateTime.now());
        question.setAtcId(request.getAtcId());

        questionRepository.save(question);
    }

    // 질문글 삭제
    public void deleteQuestion(Long qid) {
        Question question = questionRepository.findById(qid).orElseThrow(() -> new RuntimeException("Delete Question Fail"));
        List<Answer> answerList = answerRepository.findByQidAndDeleteYn(question, false);
        if(answerList.isEmpty()) {
            question.setDeleteYn(true);
            question.setLastUpdateDate(LocalDateTime.now());
            questionRepository.save(question);
        } else {
            // 답변글이 이미 존재할 때 => HTTP Status로 처리해줘야 함(추후 수정 필요)
            throw new RuntimeException("Delete Question Fail - Reply exists");
        }
    }


    //search - Q&A 통합 검색(사용자, 제목+내용)
    public List<Question> searchByWriterId(Long writerId) {
        return questionRepository.findByWriterId(writerId);
    }

    public List<Question> searchByCid(Integer cid){
        Optional<Category> optionalCategory = categoryRepository.findByCid(cid);
        if(optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            return questionRepository.findByCid(category);
        }else{
            throw new RuntimeException("Category Not Found");
        }
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
