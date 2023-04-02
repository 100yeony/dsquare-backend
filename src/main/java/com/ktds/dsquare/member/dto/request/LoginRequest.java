package com.ktds.dsquare.member.dto.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@NoArgsConstructor @AllArgsConstructor
@Builder
@Getter
public class LoginRequest {

    private String email;
    private String pw;

    public static LoginRequest convert(HttpServletRequest request) throws IOException {
        return new ObjectMapper()
                .readValue(request.getInputStream(), LoginRequest.class);
    }
}
