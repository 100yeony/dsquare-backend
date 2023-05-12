package com.ktds.dsquare.common.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import org.springframework.http.HttpStatus;

@JsonFormat(shape = Shape.OBJECT)
public enum ResponseType {

    /*** Successful Response ***/
    _200_OK(HttpStatus.OK, "200000", "OK."),

    /*** Client Error Response ***/
    // --- 400 Bad Request ---
    _400_BAD_REQUEST(HttpStatus.BAD_REQUEST, "400000", "Bad request."),
    _400_TOKEN_MISMATCH(HttpStatus.BAD_REQUEST, "400001", "It is not your token!"),
    _400_TOKEN_STILL_VALID(HttpStatus.BAD_REQUEST, "400002", "Token has been hijacked!"),

    // --- 401 Unauthorized ---
    _401_FAILED_LOGIN(HttpStatus.UNAUTHORIZED, "401001", "Login failed."),
    _401_EXPIRED_CREDENTIALS(HttpStatus.UNAUTHORIZED, "401002", "Password is expired."),
    _401_EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "401003", "Authentication/Authorization token is expired."),

    // --- 403 Forbidden ---
    _403_FORBIDDEN(HttpStatus.FORBIDDEN, "403000", "Forbidden."),
    _403_LACK_OF_AUTHORITY(HttpStatus.FORBIDDEN, "403001", "You don't have an authority to access."),

    // --- 409 Conflict ---
    _409_CONFLICT(HttpStatus.CONFLICT, "409000", "Conflict."),

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
