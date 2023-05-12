package com.ktds.dsquare.util;

import com.ktds.dsquare.auth.dto.request.LoginRequest;
import com.ktds.dsquare.auth.dto.request.TokenRefreshRequest;
import com.ktds.dsquare.auth.dto.response.LoginResponse;
import com.ktds.dsquare.member.Member;
import com.ktds.dsquare.member.MemberRepository;
import com.ktds.dsquare.member.Role;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = { "jasypt.encryptor.password=${jasypt_password}"})
public class JwtUtilTest {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MockMvc mvc;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;
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

    private void waitForTask(final AtomicInteger monitor) {
        int limit = 5000;
        int interval = 100;
        while (monitor.get() > 0 && limit > 0) {
            limit -= interval;
            sleep(interval);
        }
    }
    private void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            log.error("Sleep failed. {}", e.getMessage());
        }
    }

    public String makeRequestUri(String uri) {
        if (!StringUtils.hasText(uri))
            throw new IllegalArgumentException();
        if (!uri.startsWith("/"))
            uri = "/" + uri;

        return "http://localhost:" + port + uri;
    }

    public LoginResponse login() throws Exception {
        assertThat(memberRepository.findAll().size())
                .isGreaterThan(0);

        final String url = makeRequestUri("/login");
        final String email = "tester0@gmail.com";
        final String pw = "1234";
        final LoginRequest loginRequest = LoginRequest.builder().email(email).pw(pw).build();

        return restTemplate.postForObject(url, loginRequest, LoginResponse.class);
    }

    @Test @DisplayName("JWT 토큰 발급 동시 요청 - 하나의 요청만 처리된다")
    public void generateTokenSimultaneously_And_GeneratedDifferently() throws Exception {
        // given
        LoginResponse tokens = login();

        AtomicInteger nClient = new AtomicInteger(4);
        Set<HttpStatus> statusSet = new HashSet<>();
        Thread[] clients = generateTokenRefreshClients(nClient, tokens, statusSet);

        // when
        Thread.sleep(1000);
        for (Thread client : clients) {
            client.start();
        }
        waitForTask(nClient);

        // then
        log.info("Result: {}", statusSet);
        assertThat(statusSet.size()).isGreaterThan(1);
        assertThat(statusSet.contains(HttpStatus.CREATED)).isTrue();
    }
    private Thread[] generateTokenRefreshClients(final AtomicInteger nClient, final LoginResponse authToken, final Set<HttpStatus> statusSet) {
        assert Objects.nonNull(nClient) && Objects.nonNull(authToken) && Objects.nonNull(statusSet);

        Thread[] clients = new Thread[nClient.get()];
        for (int i = 0; i < clients.length; ++i) {
            clients[i] = new Thread(tokenRefreshTask(nClient, String.valueOf(i), authToken, statusSet));
        }
        return clients;
    }
    private Runnable tokenRefreshTask(AtomicInteger monitor, final String name, final LoginResponse authToken, final Set<HttpStatus> statusSet) {
        return () -> {
            log.info("[Task # {}] executed at {}", name, System.currentTimeMillis());
            try {
                ResponseEntity<LoginResponse> response = refreshToken(authToken);
                statusSet.add(response.getStatusCode());
            } catch (RestClientException e) {
                statusSet.add(HttpStatus.CONFLICT);
            } finally {
                monitor.decrementAndGet();
            }
        };
    }
    private ResponseEntity<LoginResponse> refreshToken(final LoginResponse authToken) throws RestClientException {
        final String url = makeRequestUri("/auth/refresh");
        final TokenRefreshRequest request = TokenRefreshRequest.builder()
                .refreshToken(authToken.getRefreshToken())
                .build();
        return restTemplate.postForEntity(url, request, LoginResponse.class);
    }

}
