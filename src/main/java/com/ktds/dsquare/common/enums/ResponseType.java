package com.ktds.dsquare.common.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import org.springframework.http.HttpStatus;

@JsonFormat(shape = Shape.OBJECT)
public enum ResponseType {

    /*** Successful Response ***/
    _200_OK(HttpStatus.OK, "200000", "OK."),

    /*** Client Error Response ***/
    _400_BAD_REQUEST(HttpStatus.BAD_REQUEST, "400000", "Bad request."),
    _400_LACK_OF_DATA(HttpStatus.BAD_REQUEST, "400001", "There is no content."),
    _400_INVAlID_BOARD_TYPE(HttpStatus.BAD_REQUEST, "400002", "Invalid board type."),
    _400_DELETE_QUESTION_FAILED(HttpStatus.BAD_REQUEST, "400003", "Cannot Delete Question. The Question has Answers."),
    _401_FAILED_LOGIN(HttpStatus.UNAUTHORIZED, "401001", "Login failed."),
    _401_EXPIRED_CREDENTIALS(HttpStatus.UNAUTHORIZED, "401002", "Password is expired."),
    _403_FORBIDDEN(HttpStatus.FORBIDDEN, "403000", "Forbidden."),
    _403_LACK_OF_AUTHORITY(HttpStatus.FORBIDDEN, "403001", "You don't have an authority to access."),
    _404_NOT_FOUND(HttpStatus.NOT_FOUND, "404000", "Entity Not Found."),
    _404_POST_NOT_FOUND(HttpStatus.NOT_FOUND, "404001", "Post Not Found."),
    _404_USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "404002", "User Not Found."),
    _409_CONFLICT(HttpStatus.CONFLICT, "409000", "Conflict."),
    _409_LIKE_STATUS_CONFLICT(HttpStatus.CONFLICT, "409001", "Like already exist."),
    _409_DISLIKE_STATUS_CONFLICT(HttpStatus.CONFLICT, "409002", "There is no Like to delete."),

    /*** Server Error Response ***/
    _500_INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500000", "Internal server error."),

    //
    _999_JUNK_RESPONSE(HttpStatus.PROCESSING, "102999", "Junk response.")
    ;

    public final HttpStatus status;
    public final String code;
    public final String message;

    ResponseType(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

}
