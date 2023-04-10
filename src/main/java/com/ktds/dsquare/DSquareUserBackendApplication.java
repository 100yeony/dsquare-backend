package com.ktds.dsquare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableJpaAuditing
@SpringBootApplication
@PropertySources({
        @PropertySource(value = "classpath:config/jwt.yml", factory = YamlLoadFactory.class)
})
public class DSquareUserBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(DSquareUserBackendApplication.class, args);
    }

}
