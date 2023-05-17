package com.ktds.dsquare.common.enums;

/**
 * <h2>Type list</h2>
 * <ul>
 * <li>ANSWER_REGISTRATION</li>
 * <li>COMMENT_REGISTRATION</li>
 * <li>NESTED_COMMENT_REGISTRATION</li>
 * <li>SPECIALITY_QUESTION_REGISTRATION</li>
 * <li>REQUEST_CHOICE</li>
 * </ul>
 */
public enum NotifType {

    ANSWER_REGISTRATION, // 답변 등록 알림
    COMMENT_REGISTRATION, // 댓글 등록 알림
    NESTED_COMMENT_REGISTRATION, // 대댓글 등록 알림
    SPECIALITY_QUESTION_REGISTRATION, // 담당 분야 질문글 등록 알림
    REQUEST_CHOICE // 요청 선정 알림

}
