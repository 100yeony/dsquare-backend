package com.ktds.dsquare.board.qna.service;

import com.ktds.dsquare.board.comment.CommentRepository;
import com.ktds.dsquare.board.comment.CommentService;
import com.ktds.dsquare.board.enums.BoardType;
import com.ktds.dsquare.board.like.LikeRepository;
import com.ktds.dsquare.board.paging.PagingService;
import com.ktds.dsquare.board.qna.domain.Answer;
import com.ktds.dsquare.board.qna.domain.Category;
import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.board.qna.dto.request.QuestionRegisterRequest;
import com.ktds.dsquare.board.qna.dto.response.BriefQuestionResponse;
import com.ktds.dsquare.board.qna.dto.response.CategoryResponse;
import com.ktds.dsquare.board.qna.dto.request.QuestionRequest;
import com.ktds.dsquare.board.qna.dto.response.QuestionRegisterResponse;
import com.ktds.dsquare.board.qna.dto.response.QuestionResponse;
import com.ktds.dsquare.board.qna.dto.*;
import com.ktds.dsquare.board.qna.repository.AnswerRepository;
import com.ktds.dsquare.board.qna.repository.CategoryRepository;
import com.ktds.dsquare.board.qna.repository.QuestionRepository;
import com.ktds.dsquare.board.tag.PostTag;
import com.ktds.dsquare.board.tag.QuestionTag;
import com.ktds.dsquare.board.tag.Tag;
import com.ktds.dsquare.board.tag.TagService;
import com.ktds.dsquare.common.annotation.Notify;
import com.ktds.dsquare.common.enums.NotifType;
import com.ktds.dsquare.common.exception.DeleteQuestionException;
import com.ktds.dsquare.common.exception.PostNotFoundException;
import com.ktds.dsquare.common.exception.UserNotFoundException;
import com.ktds.dsquare.common.file.Attachment;
import com.ktds.dsquare.common.file.AttachmentService;
import com.ktds.dsquare.common.file.dto.AttachmentDto;
import com.ktds.dsquare.member.Member;
import com.ktds.dsquare.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionService {

    /*** Repository ***/
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    /*** Service ***/
    private final CommentService commentService;
    private final AttachmentService attachmentService;
    private final TagService tagService;
    private final PagingService pagingService;


    //create - 질문글 작성
    @Transactional
    @Notify(value = NotifType.SPECIALITY_QUESTION_REGISTRATION, type = QuestionRegisterResponse.class)
    public QuestionRegisterResponse createQuestion(
            QuestionRegisterRequest request,
            MultipartFile attachment,
            Member writer
    ) throws RuntimeException {
        Category category = categoryRepository.findById(request.getCid())
                .orElseThrow(() -> new EntityNotFoundException("Category does not exist"));
        Question question = Question.createQuestion(request, category, writer);
        question = questionRepository.save(question);

        // Tagging
        List<PostTag> tagRelations = tagService.registerTags(request.getTags(), question);
        // Handle attachment
        Attachment savedAttachment = saveAttachment(attachment, question);

        return QuestionRegisterResponse.toDto(question, tagRelations, savedAttachment);
    }
    @Transactional
    public Attachment saveAttachment(MultipartFile attachment, Question question) throws RuntimeException {
        return attachmentService.saveAttachment(question.getWriter(), attachment, question);
    }

    //read - 질문글 전체 조회 & 검색
    /* Q&A 검색(카테고리, 사용자, 제목+내용)
     * 전제조건 : workYn은 필수 & deleteYn=false
     * 1. category만 검색하는 경우 -> key,value x
     * 2. category없이 제목+내용 or 작성자로 검색하는 경우 -> cid X
     * 3. 둘 다 검색하는 경우 -> cid, key, value
     * */
    public List<BriefQuestionResponse> getQuestions(Boolean workYn, Member user, Integer cid, String key, String value, String order, Pageable pageable){
        Pageable page = pagingService.orderPage(order, pageable);
        //deleteYn = false인 것만 조회
        Specification<Question> filter = Specification.where(QuestionSpecification.equalNotDeleted(false));
        //업무 구분

        try{
            if(workYn){
                //업무 - cid=2를 제외한 나머지
                if(cid == null) {
                    filter = filter.and(QuestionSpecification.notEqualNotWork(2));
                }
                //카테고리 검색 - cid 필터링
                if(cid != null){
                    filter = filter.and(QuestionSpecification.equalCategory(cid));
                }
            } else if(workYn == false){
                //비업무 - cid=2
                filter = filter.and(QuestionSpecification.equalCategory(2));

            } else if(workYn == null){
                //대시보드 - 최신글 조회(업무&비업무)
                filter = filter.and(QuestionSpecification.equalNotDeleted(false));
                Page<Question> questionList = questionRepository.findAll(filter, page);
                List<BriefQuestionResponse> searchResults = new ArrayList<>();

                //BriefQuestionResponse 객체로 만들어줌
                for(Question q: questionList){
                    searchResults.add(makeBriefQuestionRes(q, user));
                }
                return searchResults;
            }
        }
        catch (RuntimeException e){
            log.debug("error:", e);
        }

        //사용자 이름 검색(2글자로도 포함된 사람 검색 & 다른 조건과 모두 AND)
        if (key != null && key.equals("member") && value != null) {
            List<Member> members = memberRepository.findByNameContaining(value);
            try {
                if (members.size() > 0) {
                    List<Member> writerIds = new ArrayList<>();
                    for (Member M : members) {
                        Member m = memberRepository.findById(M.getId())
                                .orElseThrow(() -> new UserNotFoundException("Member Not Found"));
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

        Page<Question> questionList = questionRepository.findAll(filter, page);
        List<BriefQuestionResponse> searchResults = new ArrayList<>();

        //BriefQuestionResponse 객체로 만들어줌
        for(Question q: questionList){
            searchResults.add(makeBriefQuestionRes(q, user));
        }
        return searchResults;
    }

    public BriefQuestionResponse makeBriefQuestionRes(Question q, Member user){
        CategoryResponse categoryRes = CategoryResponse.toDto(q.getCategory());
        List<Answer> answers = answerRepository.findByQuestionAndDeleteYn(q, false);
        boolean managerAnswerYn = false;
        for (Answer A : answers) {
            if (q.getCategory().getManagerId()==A.getWriter().getId()) {
                managerAnswerYn = true;
                break;
            }
        }
        Boolean likeYn = findLikeYn(BoardType.QUESTION, q.getId(), user);
        Long commentCnt = commentRepository.countByBoardTypeAndPostId(BoardType.QUESTION, q.getId());
        return BriefQuestionResponse.toDto(q,categoryRes ,(long)answers.size(), managerAnswerYn, q.getLikeCnt(), likeYn, commentCnt);
    }


    //read - 질문글 상세 조회
    public QuestionResponse getQuestionDetail(Member user, Long qid) {
        Question question = questionRepository.findByDeleteYnAndId(false, qid)
                .orElseThrow(() -> new PostNotFoundException("Question not found. Question ID: " + qid));
        question.increaseViewCnt();
        questionRepository.save(question);

        CategoryResponse categoryRes = CategoryResponse.toDto(question.getCategory());

        Boolean likeYn = findLikeYn(BoardType.QUESTION, qid, user);
        Long commentCnt = commentRepository.countByBoardTypeAndPostId(BoardType.QUESTION, qid);
        return QuestionResponse.toDto(question, categoryRes, question.getLikeCnt(), likeYn, commentCnt);
    }

    // 질문글 수정
    @Transactional
    public void updateQuestion(Long qid, QuestionRequest request, MultipartFile newAttachment) {
        Question question = questionRepository.findByDeleteYnAndId(false, qid)
                .orElseThrow(() -> new PostNotFoundException("Question not found. Question ID: " + qid));
        Category category = categoryRepository.findById(request.getCid())
                .orElseThrow(()-> new EntityNotFoundException("category not found. category is " + request.getCid()));

        question.updateQuestion(request.getTitle(), request.getContent(), category);
        updateQuestionAttachment(request.getAttachment(), newAttachment, question);

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
                tagService.deleteTagRelation(question, oldTag);
        }
        tagService.insertNewTags(newTags, question);
    }
    @Transactional
    public void updateQuestionAttachment(
            AttachmentDto attachment,
            MultipartFile newAttachment,
            Question question
    ) {
        // 1. Handle changes in existing attachment
        attachmentService.updateAttachment(attachment, question.getAttachment());
        // 2. Handle attachment newly getting in
        saveAttachment(newAttachment, question);
    }

    // 질문글 삭제
    @Transactional
    public void deleteQuestion(Long qid) {
        Question question = questionRepository.findById(qid)
                .orElseThrow(() -> new PostNotFoundException("Delete Question Fail"));
        List<Answer> answerList = answerRepository.findByQuestionAndDeleteYn(question, false);

        if(!answerList.isEmpty()) throw new DeleteQuestionException("Delete Question Fail - Reply exists");
        deleteAttachment(question.getAttachment());
        question.deleteQuestion();
        commentService.deleteCommentCascade(BoardType.QUESTION, qid);
    }
    private void deleteAttachment(Attachment attachment) {
        if (ObjectUtils.isEmpty(attachment))
            return;

        deleteAttachment(List.of(attachment));
    }
    private void deleteAttachment(List<Attachment> attachment) {
        attachmentService.deleteAttachmentByPostDeletion(attachment);
    }

    public void like(Long id) {
        Question question = questionRepository.findByDeleteYnAndId(false, id)
                .orElseThrow(() -> new PostNotFoundException("Question not found. Question ID: " + id));
        question.like();
    }


    public void cancleLike(Long id){
        Question question = questionRepository.findByDeleteYnAndId(false, id)
                .orElseThrow(() -> new PostNotFoundException("Question not found. Question ID: " + id));
        question.cancleLike();
    }

    public Boolean findLikeYn(BoardType boardType, Long postId, Member user){
        return likeRepository.existsByBoardTypeAndPostIdAndMember(boardType, postId, user);
    }

}

