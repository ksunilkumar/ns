package com.nsind.embedding.service;

import com.nsind.embedding.dto.EmbeddingResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.*;

@Service
@Slf4j
public class OpenAIEmbeddingService {

    @Value("${openai.api-key}")
    private String apiKey;

    @Value("${openai.api-url:https://api.openai.com/v1/embeddings}")
    private String apiUrl;

    @Value("${openai.model:text-embedding-ada-002}")
    private String model;

    private final WebClient webClient;

    public OpenAIEmbeddingService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public EmbeddingResponse generateEmbedding(String text, String chunkId) {
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);
            requestBody.put("input", text);

            Map<String, Object> response = webClient.post()
                    .uri(apiUrl)
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response != null && response.containsKey("data")) {
                List<Map<String, Object>> data = (List<Map<String, Object>>) response.get("data");
                if (!data.isEmpty()) {
                    List<Double> embedding = (List<Double>) data.get(0).get("embedding");

                    return EmbeddingResponse.builder()
                            .chunkId(chunkId)
                            .embedding(embedding)
                            .dimension(embedding.size())
                            .build();
                }
            }

            log.warn("No embedding data returned for chunk: {}", chunkId);
            return null;
        } catch (Exception e) {
            log.error("Error generating embedding: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate embedding", e);
        }
    }

    public List<EmbeddingResponse> generateBatchEmbeddings(List<String> texts, String documentId) {
        List<EmbeddingResponse> responses = new ArrayList<>();

        for (int i = 0; i < texts.size(); i++) {
            String chunkId = documentId + "_chunk_" + i;
            try {
                EmbeddingResponse response = generateEmbedding(texts.get(i), chunkId);
                if (response != null) {
                    responses.add(response);
                }
            } catch (Exception e) {
                log.error("Error generating embedding for chunk {}: {}", chunkId, e.getMessage());
            }
        }

        log.info("Generated {} embeddings for document: {}", responses.size(), documentId);
        return responses;
    }
}

