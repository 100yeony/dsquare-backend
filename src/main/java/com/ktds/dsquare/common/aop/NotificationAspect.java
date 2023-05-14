package com.ktds.dsquare.common.aop;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.ktds.dsquare.board.comment.dto.CommentRegisterResponse;
import com.ktds.dsquare.board.qna.dto.AnswerRegisterResponse;
import com.ktds.dsquare.common.annotation.Notify;
import com.ktds.dsquare.common.enums.NotifType;
import com.ktds.dsquare.common.notification.service.NotificationSendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Slf4j
@RequiredArgsConstructor
@Component
@Aspect
public class NotificationAspect {

    private final NotificationSendService notificationSendService;

    /**
     * Pointcut for methods with annotation @Notify
     */
    @Pointcut("@annotation(com.ktds.dsquare.common.annotation.Notify)")
    public void notifyAnnotationPointcut() {}


    private <T extends Annotation> T getAnnotation(JoinPoint joinPoint, Class<T> annotation) {
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        return method.getAnnotation(annotation);
    }

    @AfterReturning(value = "notifyAnnotationPointcut()", returning = "rtnVal")
    public void notifyAnnotationAdvice(final JoinPoint joinPoint, final Object rtnVal) throws Exception {
        Notify notify = getAnnotation(joinPoint, Notify.class);
        for (NotifType value : notify.value()) {
            try {
                Object information = processReturnValue(value, rtnVal);
                notificationSendService.sendNotification(value, information);
            } catch (FirebaseMessagingException e) {
                log.error("Couldn't send notification.", e);
            }
        }
    }
    private Object processReturnValue(NotifType type, Object rtnVal) {
        Object processed = null;
        try {
            switch (type) {
                case ANSWER_REGISTRATION:
                    log.info("Answer registered.");
                    AnswerRegisterResponse answerRegisterResult = (AnswerRegisterResponse)rtnVal;
                    processed = answerRegisterResult.getQid();
                    break;
                case COMMENT_REGISTRATION:
                    log.info("Comment registered.");
                    CommentRegisterResponse commentRegisterResponse = (CommentRegisterResponse)rtnVal;
                    processed = commentRegisterResponse.getPostId();
                    break;
                case NESTED_COMMENT_REGISTRATION:
                    log.info("Nested comment registered.");
                    break;
                case SPECIALITY_QUESTION_REGISTRATION:
                    log.info("Specialty question registered.");
                    break;
                case REQUEST_CHOICE:
                    log.info("Request chosen.");
                    break;
                default:
                    throw new IllegalArgumentException("Unknown notification type.");
            }
        } catch (ClassCastException e) {
            log.error("Class casting error!", e);
        }
        return processed;
    }

}
