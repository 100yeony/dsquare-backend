package com.ktds.dsquare.auth;

import com.ktds.dsquare.auth.dto.request.LoginRequest;
import com.ktds.dsquare.auth.dto.request.TokenRefreshRequest;
import com.ktds.dsquare.auth.dto.response.LoginResponse;
import com.ktds.dsquare.auth.jwt.JwtService;
import com.ktds.dsquare.common.ErrorResponse;
import com.ktds.dsquare.common.exception.AccessTokenStillValidException;
import com.ktds.dsquare.common.exception.RefreshTokenMismatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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
            return new ResponseEntity<>(jwtService.refreshAccessToken(request), HttpStatus.OK);
        } catch (RefreshTokenMismatchException e) {
            return new ResponseEntity<>(
                    ErrorResponse.builder()
                            .code("400001")
                            .message("It is not your token!")
                            .build(),
                    HttpStatus.BAD_REQUEST
            );
        } catch (AccessTokenStillValidException e) {
            return new ResponseEntity<> (
                    ErrorResponse.builder()
                            .code("400002")
                            .message("Refresh token has been hijacked.")
                            .build(),
                    HttpStatus.BAD_REQUEST
            );
        } catch (CannotAcquireLockException e) {
            return new ResponseEntity<>("Simultaneous request detected.", HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
