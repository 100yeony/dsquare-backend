package com.ktds.dsquare.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FcmConfig {

    @Bean
    FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
        return FirebaseMessaging.getInstance(firebaseApp);
    }

    @Bean
    FirebaseApp firebaseApp(GoogleCredentials credentials) {
        return FirebaseApp.initializeApp(
                FirebaseOptions.builder()
                        .setCredentials(credentials)
                        .build()
        );
    }

    @Bean
    GoogleCredentials googleCredentials(
            @Value("${firebase.messaging.credentials}") String credentials
    ) throws IOException {
        try (InputStream is = new ByteArrayInputStream(credentials.getBytes())) {
            return GoogleCredentials.fromStream(is);
        }
    }

}
