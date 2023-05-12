package com.ktds.dsquare.member;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktds.dsquare.auth.dto.request.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = { "jasypt.encryptor.password=${jasypt_password}"}
)
public class MemberControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void insertMembers() throws Exception {
        for (int i = 0; i < 10; ++i) {
            String email = "tester" + i + "@gmail.com";
            String pw = passwordEncoder.encode("1234");
            String nickname = "TESTER" + i;
            String name = "TESTER" + i;
            String contact = "01012341234";
            
            Member member = Member.builder()
                    .email(email)
                    .pw(pw)
                    .nickname(nickname)
                    .name(name)
                    .contact(contact)
                    .lastPwChangeDate(LocalDateTime.now())
                    .role(Set.of(Role.USER))
                    .build();
            memberRepository.save(member);
        }
    }

    public String makeRequestUri(String uri) {
        if (!StringUtils.hasText(uri))
            throw new IllegalArgumentException();
        if (!uri.startsWith("/"))
            uri = "/" + uri;

        return "http://localhost:" + port + uri;
    }

    @Test
    @DisplayName("로그인 - 성공")
    public void login_And_Succeed() throws Exception {
        // given
        final String url = makeRequestUri("/login");

        final String email = "tester0@gmail.com";
        final String pw = "1234";
        final LoginRequest loginRequest = LoginRequest.builder().email(email).pw(pw).build();

        // when
        ResultActions result = mvc.perform(
                post(url, Map.of("email", email, "pw", pw))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequest))
        );

        // then
        result.andExpect(status().isOk());
    }

}
