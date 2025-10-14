package com.store.technology.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // ✅ Cho phép các domain được truy cập API
        config.setAllowedOrigins(List.of(
                "http://localhost:3000", // FE local
                "https://your-frontend-domain.vercel.app", // FE deploy
                "https://technology-frontend.onrender.com" // ví dụ FE trên Render
        ));

        // ✅ Cho phép các method HTTP
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        // ✅ Cho phép gửi các header như Authorization
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        // ✅ Cho phép gửi token cookie nếu cần
        config.setAllowCredentials(true);

        // ✅ Áp dụng cho tất cả endpoint
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
