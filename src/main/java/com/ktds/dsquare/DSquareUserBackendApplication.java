package com.ktds.dsquare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DSquareUserBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(DSquareUserBackendApplication.class, args);
    }

}
