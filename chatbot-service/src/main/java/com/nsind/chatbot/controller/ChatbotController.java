package com.nsind.chatbot.controller;

import com.nsind.chatbot.dto.ChatRequest;
import com.nsind.chatbot.dto.ChatResponse;
import com.nsind.chatbot.entity.Chatbot;
import com.nsind.chatbot.service.ChatbotService;
import com.nsind.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/chatbot")
@RequiredArgsConstructor
@Tag(name = "Chatbot", description = "Chatbot management and interaction")
public class ChatbotController {

    private final ChatbotService chatbotService;

    @PostMapping("/create")
    @Operation(summary = "Create new chatbot")
    public ResponseEntity<ApiResponse<Chatbot>> createChatbot(
            @RequestHeader("X-User-Id") String userId,
            @RequestParam String name,
            @RequestParam(required = false) String description) {
        Chatbot chatbot = chatbotService.createChatbot(userId, name, description);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(chatbot, "Chatbot created successfully"));
    }

    @PostMapping("/chat")
    @Operation(summary = "Send message to chatbot")
    public ResponseEntity<ApiResponse<ChatResponse>> chat(@RequestBody ChatRequest request) {
        ChatResponse response = chatbotService.chat(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Response generated"));
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get user chatbots")
    public ResponseEntity<ApiResponse<List<Chatbot>>> getUserChatbots(
            @PathVariable String userId) {
        List<Chatbot> chatbots = chatbotService.getUserChatbots(userId);
        return ResponseEntity.ok(ApiResponse.success(chatbots, "Chatbots retrieved"));
    }

    @GetMapping("/{chatbotId}/details")
    @Operation(summary = "Get chatbot details")
    public ResponseEntity<ApiResponse<Chatbot>> getChatbot(
            @PathVariable String chatbotId,
            @RequestHeader("X-User-Id") String userId) {
        Chatbot chatbot = chatbotService.getChatbot(chatbotId, userId);
        return ResponseEntity.ok(ApiResponse.success(chatbot, "Chatbot details retrieved"));
    }

    @DeleteMapping("/{chatbotId}")
    @Operation(summary = "Delete chatbot")
    public ResponseEntity<ApiResponse<Void>> deleteChatbot(
            @PathVariable String chatbotId,
            @RequestHeader("X-User-Id") String userId) {
        chatbotService.deleteChatbot(chatbotId, userId);
        return ResponseEntity.ok(ApiResponse.success(null, "Chatbot deleted"));
    }
}

