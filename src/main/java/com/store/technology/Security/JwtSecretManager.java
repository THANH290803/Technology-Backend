package com.store.technology.Security;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class JwtSecretManager {
    private final AtomicReference<String> currentSecret = new AtomicReference<>(generateSecret());

    // Sinh secret ngẫu nhiên >=100 bytes, encode Base64
    private static String generateSecret() {
        byte[] bytes = new byte[100];
        new SecureRandom().nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

    public String getCurrentSecret() {
        return currentSecret.get();
    }

    // Rotate mỗi giờ (3600000 ms)
    @Scheduled(fixedRate = 3600000)
    public void rotateSecret() {
        currentSecret.set(generateSecret());
        System.out.println("JWT_SECRET rotated: " + currentSecret.get());
    }
}
