package com.ktds.dsquare.common.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.ktds.dsquare.board.comment.Comment;
import com.ktds.dsquare.board.comment.CommentRepository;
import com.ktds.dsquare.board.qna.domain.Answer;
import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.board.qna.repository.AnswerRepository;
import com.ktds.dsquare.board.qna.repository.QuestionRepository;
import com.ktds.dsquare.common.enums.NotifType;
import com.ktds.dsquare.common.exception.PostNotFoundException;
import com.ktds.dsquare.common.notification.RegistrationToken;
import com.ktds.dsquare.common.notification.repository.RegistrationTokenRepository;
import com.ktds.dsquare.member.Member;
import com.ktds.dsquare.member.MemberRepository;
import com.ktds.dsquare.member.dto.response.BriefMemberInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationSendService {

    private final FirebaseMessaging fcm;

    private final RegistrationTokenRepository rtRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendNotification(NotifType type, Object information) throws FirebaseMessagingException {
        if (ObjectUtils.isEmpty(type))
            return;

        log.info("Send notification [{}]", type);
        Member receiver = new Member();
        List<RegistrationToken> registrationTokens = Collections.emptyList();
        switch (type) {
            case ANSWER_REGISTRATION:
                long questionId = (long)information;
                Question question = questionRepository.findById(questionId).orElseThrow(() -> new PostNotFoundException("No such question."));

                receiver = question.getWriter();
                log.info("[Answer registered] Notification will be sent to {}.", receiver.getEmail());
                registrationTokens = rtRepository.findByOwner(receiver);
                break;
            case COMMENT_REGISTRATION:
                long postId = (long)information;
                // Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("No such post."));

                if (questionRepository.existsById(postId)) {
                    Question q = questionRepository.findById(postId).orElseThrow(RuntimeException::new);

                    receiver = q.getWriter();
                    registrationTokens = rtRepository.findByOwner(receiver);
                }
                else if (answerRepository.existsById(postId)) {
                    Answer a = answerRepository.findById(postId).orElseThrow(RuntimeException::new);

                    receiver = a.getWriter();
                    registrationTokens = rtRepository.findByOwner(receiver);
                }
                log.info("[Comment registered] Notification will be sent to {}.", receiver.getEmail());
                break;
            case NESTED_COMMENT_REGISTRATION:
                log.info("[Nested comment registered] ");
            case SPECIALITY_QUESTION_REGISTRATION:
                log.info("[Specialty question registered] ");
            case REQUEST_CHOICE:
                log.info("[Request chosen] ");
            default:
                throw new IllegalArgumentException("Unknown notification type.");
        }

        log.info("Message receivers are as follows-\n{}", registrationTokens.stream().map(RegistrationToken::getValue).collect(Collectors.toList()));
        sendNotification(type, registrationTokens);
    }
    private void sendNotification(NotifType type, List<RegistrationToken> registrationTokens) throws FirebaseMessagingException {
        Map<String, String> data;
        switch (type) {
            case ANSWER_REGISTRATION:
                data = Map.of("title", "답변 등록 알림", "body", "회원님의 질문에 새로운 답변이 등록되었습니다.");
                break;
            case COMMENT_REGISTRATION:
                data = Map.of("title", "댓글 등록 알림", "body", "회원님의 게시글에 새로운 댓글이 등록되었습니다.");
                break;
            case NESTED_COMMENT_REGISTRATION:
            case SPECIALITY_QUESTION_REGISTRATION:
            case REQUEST_CHOICE:
            default:
                log.warn("Not supported notification. Type: [{}]", type);
                return;
        }

        List<String> receivers = registrationTokens.stream()
                .map(RegistrationToken::getValue)
                .collect(Collectors.toList());
        MulticastMessage multicastMessage = makeMulticastMessage(data, receivers);
        fcm.sendMulticast(multicastMessage);
    }
    private MulticastMessage makeMulticastMessage(Map<String, String> data, List<String> registrationTokens) {
        return MulticastMessage.builder()
                .putAllData(data)
                .addAllTokens(registrationTokens)
                .build();
    }


    public String notifyTopic(String topic) throws FirebaseMessagingException {
        Message message = makeTopicMessage(topic);
        return fcm.send(message);
    }

//    @Transactional
    public String send(String to) throws FirebaseMessagingException {
        Message message = makeMessage(to);
        return fcm.send(message);
    }
    public Message makeTopicMessage(String topic) {
        return Message.builder()
                .setTopic(topic)
                .putData("title", "Topic Notification")
                .putData("body", "New notification :: " + topic)
                .build();
    }
    public Message makeMessage(String to) {
        return Message.builder()
                .setToken(to)
                .putData("title", "Test Notification Title")
                .putData("body", "Test Notification Body")
                .build();
    }

}
