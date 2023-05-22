package com.ktds.dsquare.common.aop;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.ktds.dsquare.board.Post;
import com.ktds.dsquare.board.PostSelectService;
import com.ktds.dsquare.board.comment.dto.CommentRegisterResponse;
import com.ktds.dsquare.board.comment.dto.NestedCommentRegisterResponse;
import com.ktds.dsquare.board.qna.domain.Answer;
import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.board.qna.dto.AnswerRegisterResponse;
import com.ktds.dsquare.board.qna.dto.QuestionRegisterResponse;
import com.ktds.dsquare.board.qna.service.AnswerSelectService;
import com.ktds.dsquare.board.qna.service.QuestionSelectService;
import com.ktds.dsquare.common.annotation.Notify;
import com.ktds.dsquare.common.enums.NotifType;
import com.ktds.dsquare.common.notification.service.NotificationSendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static com.ktds.dsquare.board.enums.BoardType.Constant.*;

@Slf4j
@RequiredArgsConstructor
@Component
@Aspect
public class NotificationAspect {

    private final NotificationSendService notificationSendService;

    private final PostSelectService postSelectService;
    private final QuestionSelectService questionSelectService;
    private final AnswerSelectService answerSelectService;


    /**
     * Pointcut for methods with annotation @Notify
     */
    @Pointcut("@annotation(notify)")
    public void notifyAnnotationPointcut(Notify notify) {}


    @AfterReturning(value = "notifyAnnotationPointcut(notify)", returning = "rtnVal") // TODO argNames ?
    public void notifyAnnotationAdvice(final JoinPoint joinPoint, final Object rtnVal, Notify notify) throws Exception {
        if (rtnVal.getClass() != notify.type())
            throw new IllegalArgumentException("Class mismatch.");
        // TODO or try to cast rtnVal.getClass().cast()

        for (NotifType value : notify.value()) {
            sendNotification(notify.type().cast(rtnVal), value);
        }
    }
    private void sendNotification(Object information, NotifType type) {
        try {
            long[] receiverList = extractReceiverList(information, type);
            if (ObjectUtils.isEmpty(receiverList)) {
                log.info("There's no one to receive notification.");
                return;
            }

            Map<String, String> data = makeNotificationData(information, type);
            notificationSendService.sendNotification(receiverList, type, data);
        } catch (ClassCastException e) {
            log.error("Impossible class casting!", e);
            throw new IllegalArgumentException();
        } catch (FirebaseMessagingException e) {
            log.error("Couldn't send notification.", e);
        }
    }

    private long[] extractReceiverList(Object obj, NotifType type) {
        long[] receiverList = null;
        switch (type) {
            case ANSWER_REGISTRATION:
                receiverList = collectReceiverKey((AnswerRegisterResponse)obj); // TODO is casting necessary if object class is identified automatically?
                break;
            case COMMENT_REGISTRATION:
                receiverList = collectReceiverKey((CommentRegisterResponse)obj);
                break;
            case NESTED_COMMENT_REGISTRATION:
                receiverList = collectReceiverKey((NestedCommentRegisterResponse)obj);
                break;
            case SPECIALITY_QUESTION_REGISTRATION:
                receiverList = collectReceiverKey((QuestionRegisterResponse)obj);
                break;
            case REQUEST_CHOICE:
            default:
                log.error("Not supported notification type.");
        }
        return receiverList;
    }
    private long[] collectReceiverKey(AnswerRegisterResponse source) {
        Question question = questionSelectService.selectWithId(source.getQid());
        return new long[] { question.getWriter().getId() };
    }
    private long[] collectReceiverKey(CommentRegisterResponse source) {
        long[] receiverList = null;

        Post post = postSelectService.selectWithId(source.getPostId());
        switch (post.getDtype()) {
            case QUESTION:
                Question question = questionSelectService.selectWithId(post.getId());
                receiverList = new long[] { question.getWriter().getId() };
                break;
            case ANSWER:
                Answer answer = answerSelectService.selectWithId(post.getId());
                receiverList = new long[] { answer.getWriter().getId() };
                break;
            case TALK:
            case CARROT:
            case CARD:
            default:
                log.error("Unknown post type.");
        }
        return receiverList;
    }
    private long[] collectReceiverKey(QuestionRegisterResponse source) {
        long[] receiverList = null;
        // ...
        receiverList = new long[] { source.getCategory().getManagerId() };
        return receiverList;
    }
    private long[] collectReceiverKey(NestedCommentRegisterResponse source) {
        return new long[] { source.getOriginWriter().getId() };
    }

    private Map<String, String> makeNotificationData(Object information, NotifType type) {
        /*
        TODO refactor
        Map<String, String> data
            = createNotificationDataBase(type); // must include "title", "body", and "type"(Notification Type)
            // and post ID & thumbnail ?...
         */
        Map<String, String> data = null;
        switch (type) {
            case ANSWER_REGISTRATION:
                data = extractData((AnswerRegisterResponse)information);
                break;
            case COMMENT_REGISTRATION:
                data = extractData((CommentRegisterResponse)information);
                break;
            case SPECIALITY_QUESTION_REGISTRATION:
                data = extractData((QuestionRegisterResponse)information);
                break;
            case NESTED_COMMENT_REGISTRATION:
                /*
                TODO refactor
                data.putAll(extractData(...));
                 */
                data = extractData((NestedCommentRegisterResponse)information);
                break;
            case REQUEST_CHOICE:
            default:
                log.warn("Not supported notification. Type: [{}]", type);
        }
        return data;
    }
    private Map<String, String> extractData(AnswerRegisterResponse information) {
        Map<String, String> data = new HashMap<>();
        data.put("board", "QNA"); // BoardType.QUESTION or BoardType.ANSWER is incongruent
        data.put("title", "답변 등록 알림");
        data.put("body", "회원님의 질문에 새로운 답변이 등록되었습니다.");

        data.put("questionId", String.valueOf(information.getQid()));
        data.put("writerId", String.valueOf(information.getWriterInfo().getId()));
        data.put("thumbnail",
                StringUtils.hasText(information.getWriterInfo().getProfileImage())
                ? information.getWriterInfo().getProfileImage() : ""
        ); // TODO seems to update DB also
        return data;
    }
    private Map<String, String> extractData(CommentRegisterResponse information) {
        Map<String, String> data = new HashMap<>();
        data.put("board", information.getBoardType().toString());
        data.put("title", "댓글 등록 알림");
        data.put("body", "회원님의 게시글에 새로운 댓글이 등록되었습니다.");

        data.put("postId", String.valueOf(information.getPostId()));
        data.put("writerId", String.valueOf(information.getWriterInfo().getId()));
        data.put("thumbnail",
                StringUtils.hasText(information.getWriterInfo().getProfileImage())
                        ? information.getWriterInfo().getProfileImage() : ""
        ); // TODO seems to update DB also
        return data;
    }
    private Map<String, String> extractData(QuestionRegisterResponse information) {
        Map<String, String> data = new HashMap<>();
        data.put("board", "QNA");
        data.put("title", "담당 분야 질문 등록 알림");
        data.put("body", "담당 분야에 새로운 질문이 등록되었습니다.");

        data.put("postId", String.valueOf(information.getId()));
        data.put("writerId", String.valueOf(information.getWriterInfo().getId()));
        data.put("thumbnail",
                StringUtils.hasText(information.getWriterInfo().getProfileImage())
                        ? information.getWriterInfo().getProfileImage() : ""
        ); // TODO seems to update DB also
        return data;
    }
    private Map<String, String> extractData(NestedCommentRegisterResponse information) {
        Map<String, String> data = new HashMap<>(); // TODO refactor
        data.put("board", information.getBoardType().toString());
        data.put("title", "댓글 등록 알림");
        data.put("body", "회원님의 댓글을 언급한 새로운 댓글이 작성되었습니다.");

        data.put("postId", String.valueOf(information.getPostId()));
        data.put("origCommentId", "empty"/* String.valueOf(information.<...>) */);
        data.put("writerId", String.valueOf(information.getWriter().getId()));
        data.put("thumbnail",
                StringUtils.hasText(information.getWriter().getProfileImage())
                    ? information.getWriter().getProfileImage() : ""
        );
        return data;
    }

}
