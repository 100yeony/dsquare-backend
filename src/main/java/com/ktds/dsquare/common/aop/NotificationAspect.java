package com.ktds.dsquare.common.aop;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.ktds.dsquare.board.Post;
import com.ktds.dsquare.board.PostSelectService;
import com.ktds.dsquare.board.comment.dto.CommentRegisterResponse;
import com.ktds.dsquare.board.qna.domain.Answer;
import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.board.qna.dto.AnswerRegisterResponse;
import com.ktds.dsquare.board.qna.dto.CategoryResponse;
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
    private void sendNotification(Object obj, NotifType type) {
        try {
            long[] receiverList = extractReceiverList(obj, type);
            if (ObjectUtils.isEmpty(receiverList)) {
                log.info("There's no one to receive notification.");
                return;
            }

            notificationSendService.sendNotification(receiverList, type);
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
                receiverList = collectReceiverKey((AnswerRegisterResponse)obj);
                break;
            case COMMENT_REGISTRATION:
                receiverList = collectReceiverKey((CommentRegisterResponse)obj);
                break;
            case NESTED_COMMENT_REGISTRATION:
            case SPECIALITY_QUESTION_REGISTRATION:
                receiverList = collectReceiverKey((CategoryResponse)obj);
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
    private long[] collectReceiverKey(CategoryResponse source) {
        long[] receiverList = null;
        // ...
        receiverList = new long[] { source.getManagerId() };
        return receiverList;
    }

}
