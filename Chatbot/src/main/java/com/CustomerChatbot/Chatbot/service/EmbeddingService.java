package com.CustomerChatbot.Chatbot.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

@Service
public class EmbeddingService {

    private static final String EMBEDDING_API_URL = "http://localhost:8001/generate_embedding";

    public float[] generateEmbedding(String text) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            String payload = "{\"text\": \"" + text + "\"}";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            HttpEntity<String> request = new HttpEntity<>(payload, headers);

            ResponseEntity<EmbeddingResponse> response = restTemplate.postForEntity(
                    EMBEDDING_API_URL, request, EmbeddingResponse.class);

            if (response.getBody() != null) {
                return response.getBody().getEmbedding();
            } else {
                throw new RuntimeException("Failed to generate embedding");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while calling embedding API: " + e.getMessage(), e);
        }
    }

    @Setter
    @Getter
    public static class EmbeddingResponse {
        private float[] embedding;

    }
}
