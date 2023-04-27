

package com.ktds.dsquare.board.qna.service;

import com.ktds.dsquare.board.comment.CommentRepository;
import com.ktds.dsquare.board.enums.BoardType;
import com.ktds.dsquare.board.like.LikeService;
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
import com.ktds.dsquare.board.tag.QuestionTag;
import com.ktds.dsquare.board.tag.Tag;
import com.ktds.dsquare.board.tag.repository.QuestionTagRepository;
import com.ktds.dsquare.board.tag.repository.TagRepository;
import com.ktds.dsquare.member.Member;
import com.ktds.dsquare.member.MemberRepository;
import com.ktds.dsquare.member.dto.response.MemberInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
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
    private final LikeService likeService;
    private final CommentRepository commentRepository;

    //create - 질문글 작성
    @Transactional
    public void createQuestion(QuestionRequest dto) {
        Member writer = memberRepository.findById(dto.getWriterId()).orElseThrow(() -> new EntityNotFoundException("Writer does not exist"));
        Category category = categoryRepository.findById(dto.getCid()).orElseThrow(() -> new EntityNotFoundException("Category does not exist"));
        Question question = Question.toEntity(dto, writer, category);

        questionRepository.save(question);
        insertNewTags(dto.getTags(), question);
    }

    //read - 질문글 전체 조회 & 검색
    /* Q&A 검색(카테고리, 사용자, 제목+내용)
     * 전제조건 : workYn은 필수 & deleteYn=false
     * 1. category만 검색하는 경우 -> key,value x
     * 2. category없이 제목+내용 or 작성자로 검색하는 경우 -> cid X
     * 3. 둘 다 검색하는 경우 -> cid, key, value
     * */
    public List<BriefQuestionResponse> getQuestions(Boolean workYn, Integer cid, String key, String value){
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
            boolean managerAnswerYn = false;
            for (Answer A : answers) {
                if (q.getCategory().getManagerId()==A.getWriter().getId()) {
                    managerAnswerYn = true;
                    break;
                }
            }

            Long likeCnt = likeService.findLikeCnt(BoardType.QUESTION, q.getQid());
            Boolean likeYn = likeService.findLikeYn(BoardType.QUESTION, q.getQid(), q.getWriter());
            Long commentCnt = commentRepository.countByBoardTypeAndPostId(BoardType.QUESTION, q.getQid());
            searchResults.add(BriefQuestionResponse.toDto(q, MemberInfo.toDto(q.getWriter()),categoryRes ,(long)answers.size(), managerAnswerYn, likeCnt, likeYn, commentCnt));
        }

        return searchResults;
    }


    //read - 질문글 상세 조회
    public QuestionResponse getQuestionDetail(Member user, Long qid) {
        Question question = questionRepository.findByDeleteYnAndQid(false, qid);
        question.increaseViewCnt();
        questionRepository.save(question);

        MemberInfo writer = MemberInfo.toDto(user);
        CategoryResponse categoryRes = CategoryResponse.toDto(question.getCategory());

        Long likeCnt = likeService.findLikeCnt(BoardType.QUESTION, qid);
        Boolean likeYn = likeService.findLikeYn(BoardType.QUESTION, qid, user);
        Long commentCnt = commentRepository.countByBoardTypeAndPostId(BoardType.QUESTION, qid);
        return QuestionResponse.toDto(question, writer, categoryRes, likeCnt, likeYn, commentCnt);
    }

    // 질문글 수정
    @Transactional
    public void updateQuestion(Long qid, QuestionRequest request) {
        Question question = questionRepository.findByDeleteYnAndQid(false, qid);
        if(question==null){
            throw new EntityNotFoundException("Question not found. qid is " + qid);
        }
        Category category = categoryRepository.findById(request.getCid())
                .orElseThrow(()-> new EntityNotFoundException("category not found. category is " + request.getCid()));
        question.updateQuestion(request.getTitle(), request.getContent(), category, request.getAtcId());

        // 태그 수정
        List<QuestionTag> oldQTs = question.getQuestionTags();
        List<Tag> oldTags = new ArrayList<>();
        for(QuestionTag oldQT : oldQTs) {
            oldTags.add(oldQT.getTag());
        }
        List<String> newTags = request.getTags();

        for(Tag oldTag : oldTags) {
            String oldTagName = oldTag.getName();
            if(newTags.contains(oldTagName))
                newTags.remove(oldTagName);
            else
                deleteQuestionTagRelation(question, oldTag);
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
        question.deleteQuestion();
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

    // 태그-질문 간 연관관계 삭제
    @Transactional
    public void deleteQuestionTagRelation(Question question, Tag tag) {
        questionTagRepository.deleteByQuestionAndTag(question, tag);
    }

}

