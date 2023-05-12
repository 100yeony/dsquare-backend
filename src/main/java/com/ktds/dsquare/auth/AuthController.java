package com.ktds.dsquare.auth;

import com.ktds.dsquare.auth.dto.request.LoginRequest;
import com.ktds.dsquare.auth.dto.request.TokenRefreshRequest;
import com.ktds.dsquare.auth.dto.response.LoginResponse;
import com.ktds.dsquare.auth.jwt.JwtService;
import com.ktds.dsquare.common.enums.ResponseType;
import com.ktds.dsquare.common.exception.AccessTokenStillValidException;
import com.ktds.dsquare.common.exception.RefreshTokenExpiredException;
import com.ktds.dsquare.common.exception.RefreshTokenMismatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.ktds.dsquare.util.ResponseUtil.makeResponse;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;

    /**
     * API for Swagger
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return new ResponseEntity<>(LoginResponse.toDto(Map.of()), HttpStatus.OK);
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<?> refresh(@RequestBody TokenRefreshRequest request) {
        try {
            return new ResponseEntity<>(jwtService.refreshAccessToken(request), HttpStatus.CREATED);
        } catch (RefreshTokenMismatchException e) {
            return makeResponse(ResponseType._400_TOKEN_MISMATCH);
        } catch (AccessTokenStillValidException e) {
            return makeResponse(ResponseType._400_TOKEN_STILL_VALID);
        } catch (RefreshTokenExpiredException e) {
            return makeResponse(ResponseType._401_EXPIRED_TOKEN);
        } catch (CannotAcquireLockException e) {
            return makeResponse(ResponseType._409_CONFLICT);
        }
    }

}
