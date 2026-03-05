package com.nsind.embedding.controller;

import com.nsind.embedding.dto.EmbeddingRequest;
import com.nsind.embedding.dto.EmbeddingResponse;
import com.nsind.embedding.service.OpenAIEmbeddingService;
import com.nsind.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/embeddings")
@RequiredArgsConstructor
@Tag(name = "Embeddings", description = "Vector embedding generation")
public class EmbeddingController {

    private final OpenAIEmbeddingService embeddingService;

    @PostMapping("/generate")
    @Operation(summary = "Generate embedding for text")
    public ResponseEntity<ApiResponse<EmbeddingResponse>> generateEmbedding(
            @RequestBody EmbeddingRequest request) {
        EmbeddingResponse response = embeddingService.generateEmbedding(request.getText(), request.getChunkId());
        return ResponseEntity.ok(ApiResponse.success(response, "Embedding generated"));
    }

    @PostMapping("/batch")
    @Operation(summary = "Generate embeddings for multiple texts")
    public ResponseEntity<ApiResponse<List<EmbeddingResponse>>> generateBatchEmbeddings(
            @RequestBody EmbeddingRequest request) {
        List<EmbeddingResponse> responses = embeddingService.generateBatchEmbeddings(
                request.getTexts(), request.getChunkId());
        return ResponseEntity.ok(ApiResponse.success(responses, "Batch embeddings generated"));
    }
}

