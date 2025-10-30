package com.store.technology.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class TelegramService {
    private static final String BOT_TOKEN = "7696077044:AAELvKNUECYUxG6vXGH0p2zvLZH8MhXpxBw";
    private static final String CHAT_ID = "6999594920";

    public void sendOrderMessage(String message) {
        try {
            String url = String.format(
                    "https://api.telegram.org/bot%s/sendMessage",
                    BOT_TOKEN
            );

            RestTemplate restTemplate = new RestTemplate();
            Map<String, Object> params = Map.of(
                    "chat_id", CHAT_ID,
                    "text", message,
                    "parse_mode", "HTML"
            );

            ResponseEntity<String> response = restTemplate.postForEntity(url, params, String.class);
            System.out.println("Telegram response: " + response.getBody());

        } catch (Exception e) {
            System.err.println("❌ Lỗi gửi Telegram: " + e.getMessage());
        }
    }
}
