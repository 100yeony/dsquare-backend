package com.ktds.dsquare.auth;

import com.ktds.dsquare.auth.dto.request.TokenRefreshRequest;
import com.ktds.dsquare.auth.jwt.JwtService;
import com.ktds.dsquare.common.ErrorResponse;
import com.ktds.dsquare.common.exception.AccessTokenStillValidException;
import com.ktds.dsquare.common.exception.RefreshTokenMismatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;

    @PostMapping("/refresh")
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
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
