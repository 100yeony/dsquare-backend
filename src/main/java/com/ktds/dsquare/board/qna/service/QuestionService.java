package com.ktds.dsquare.board.qna.service;

import com.ktds.dsquare.board.comment.CommentRepository;
import com.ktds.dsquare.board.comment.CommentService;
import com.ktds.dsquare.board.enums.BoardType;
import com.ktds.dsquare.board.like.LikeRepository;
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
import com.ktds.dsquare.board.tag.TagService;
import com.ktds.dsquare.common.Paging.PagingService;
import com.ktds.dsquare.common.file.Attachment;
import com.ktds.dsquare.common.file.AttachmentService;
import com.ktds.dsquare.common.file.dto.AttachmentDto;
import com.ktds.dsquare.member.Member;
import com.ktds.dsquare.member.MemberRepository;
import com.ktds.dsquare.member.dto.response.MemberInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
public class QuestionService {

    /*** Repository ***/
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    private final LikeRepository likeRepository;

    /*** Service ***/
    private final CommentRepository commentRepository;
    private final CommentService commentService;
    private final AttachmentService attachmentService;
    private final TagService tagService;
    private final PagingService pagingService;

    //create - 질문글 작성
    @Transactional
    public void createQuestion(QuestionRequest dto, MultipartFile attachment, Member writer) throws RuntimeException {
        Category category = categoryRepository.findById(dto.getCid()).orElseThrow(() -> new EntityNotFoundException("Category does not exist"));
        Question question = Question.toEntity(dto, writer, category);

        Attachment savedAttachment = saveAttachment(writer, attachment, question);
        question.registerAttachment(savedAttachment);

        questionRepository.save(question);
        tagService.insertNewTags(dto.getTags(), question);
    }
    private Attachment saveAttachment(Member user, MultipartFile attachment, Question question) throws RuntimeException {
        return attachmentService.saveAttachment(user, attachment, question);
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
        Boolean likeYn = findLikeYn(BoardType.QUESTION, q.getQid(), user);
        Long commentCnt = commentRepository.countByBoardTypeAndPostId(BoardType.QUESTION, q.getQid());
        return BriefQuestionResponse.toDto(q, MemberInfo.toDto(q.getWriter()),categoryRes ,(long)answers.size(), managerAnswerYn, q.getLikeCnt(), likeYn, commentCnt);
    }


    //read - 질문글 상세 조회
    public QuestionResponse getQuestionDetail(Member user, Long qid) {
        Question question = questionRepository.findByDeleteYnAndQid(false, qid);
        question.increaseViewCnt();
        questionRepository.save(question);

        Member member = question.getWriter();
        MemberInfo writer = MemberInfo.toDto(member);
        CategoryResponse categoryRes = CategoryResponse.toDto(question.getCategory());

        Boolean likeYn = findLikeYn(BoardType.QUESTION, qid, user);
        Long commentCnt = commentRepository.countByBoardTypeAndPostId(BoardType.QUESTION, qid);
        return QuestionResponse.toDto(question, writer, categoryRes, question.getLikeCnt(), likeYn, commentCnt);
    }

    // 질문글 수정
    @Transactional
    public void updateQuestion(Long qid, QuestionRequest request, MultipartFile newAttachment) {
        Question question = questionRepository.findByDeleteYnAndQid(false, qid);
        if(question==null){
            throw new EntityNotFoundException("Question not found. qid is " + qid);
        }
        Category category = categoryRepository.findById(request.getCid())
                .orElseThrow(()-> new EntityNotFoundException("category not found. category is " + request.getCid()));
        Attachment savedAttachment = updateQuestionAttachment(request.getAttachment(), newAttachment, question);
        question.updateQuestion(request.getTitle(), request.getContent(), category);

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
    private Attachment updateQuestionAttachment(
            AttachmentDto attachment,
            MultipartFile newAttachment,
            Question question
    ) {
        // 1. Handle changes in existing attachment
        attachmentService.updateAttachment(attachment, question.getAttachment());
        // 2. Handle attachment newly getting in
        return attachmentService.saveAttachment(question.getWriter(), newAttachment, question);
    }

    // 질문글 삭제
    @Transactional
    public void deleteQuestion(Long qid) {
        Question question = questionRepository.findById(qid)
                .orElseThrow(() -> new EntityNotFoundException("Delete Question Fail"));
        List<Answer> answerList = answerRepository.findByQuestionAndDeleteYn(question, false);

        // 답변글이 이미 존재할 때 => HTTP Status로 처리해줘야 함(추후 수정 필요)
        if(!answerList.isEmpty()) throw new EntityNotFoundException("Delete Question Fail - Reply exists");
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
        Question question = questionRepository.findById(id)
                        .orElseThrow(()-> new EntityNotFoundException("question not found"));
        question.like();
    }


    public void cancleLike(Long id){
        Question question = questionRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("question not found"));
        question.cancleLike();
    }

    public Boolean findLikeYn(BoardType boardType, Long postId, Member user){
        return likeRepository.existsByBoardTypeAndPostIdAndMember(boardType, postId, user);
    }

}

