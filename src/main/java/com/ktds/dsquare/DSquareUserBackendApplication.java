package com.ktds.dsquare;

import com.ktds.dsquare.config.properties.YamlLoadFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

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
