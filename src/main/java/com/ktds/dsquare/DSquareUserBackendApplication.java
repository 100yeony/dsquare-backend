package com.ktds.dsquare;

import com.ktds.dsquare.config.properties.YamlLoadFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;



@SpringBootApplication
@PropertySources({
        @PropertySource(value = "classpath:config/jasypt.yml", factory = YamlLoadFactory.class),
        @PropertySource(value = "classpath:config/jwt.yml", factory = YamlLoadFactory.class),
        @PropertySource(value = "classpath:config/mail.yml", factory = YamlLoadFactory.class),
})
public class DSquareUserBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(DSquareUserBackendApplication.class, args);
    }

}
