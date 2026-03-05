package com.nsind.chatbot.service;

import com.nsind.chatbot.dto.ChatRequest;
import com.nsind.chatbot.dto.ChatResponse;
import com.nsind.chatbot.entity.ChatMessage;
import com.nsind.chatbot.entity.Chatbot;
import com.nsind.chatbot.repository.ChatMessageRepository;
import com.nsind.chatbot.repository.ChatbotRepository;
import com.nsind.common.exception.BadRequestException;
import com.nsind.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatbotService {

    @Value("${openai.api-key}")
    private String openaiApiKey;

    @Value("${openai.api-url:https://api.openai.com/v1/chat/completions}")
    private String chatApiUrl;

    private final ChatbotRepository chatbotRepository;
    private final ChatMessageRepository messageRepository;
    private final WebClient webClient;

    @Transactional
    public Chatbot createChatbot(String userId, String name, String description) {
        String apiKey = UUID.randomUUID().toString();

        Chatbot chatbot = Chatbot.builder()
                .userId(userId)
                .name(name)
                .description(description)
                .apiKey(apiKey)
                .systemPrompt("You are a helpful assistant trained on user documents.")
                .isActive(true)
                .build();

        chatbot = chatbotRepository.save(chatbot);
        log.info("Chatbot created: {} for user: {}", chatbot.getId(), userId);
        return chatbot;
    }

    @Transactional
    public ChatResponse chat(ChatRequest request) {
        Chatbot chatbot = chatbotRepository.findByApiKey(request.getApiKey())
                .orElseThrow(() -> new BadRequestException("Invalid API key"));

        if (!chatbot.getIsActive()) {
            throw new BadRequestException("Chatbot is inactive");
        }

        // Generate response from LLM
        String botResponse = generateResponse(request.getMessage(), chatbot.getSystemPrompt());

        // Save message
        ChatMessage message = ChatMessage.builder()
                .userId(request.getUserId())
                .chatbotId(request.getChatbotId())
                .userMessage(request.getMessage())
                .botResponse(botResponse)
                .build();

        message = messageRepository.save(message);

        return ChatResponse.builder()
                .messageId(message.getId())
                .response(botResponse)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    public List<Chatbot> getUserChatbots(String userId) {
        return chatbotRepository.findByUserId(userId);
    }

    public Chatbot getChatbot(String chatbotId, String userId) {
        Chatbot chatbot = chatbotRepository.findById(chatbotId)
                .orElseThrow(() -> new ResourceNotFoundException("Chatbot not found"));

        if (!chatbot.getUserId().equals(userId)) {
            throw new BadRequestException("Unauthorized access");
        }

        return chatbot;
    }

    @Transactional
    public void deleteChatbot(String chatbotId, String userId) {
        Chatbot chatbot = getChatbot(chatbotId, userId);
        chatbotRepository.deleteById(chatbotId);
        log.info("Chatbot deleted: {}", chatbotId);
    }

    private String generateResponse(String userMessage, String systemPrompt) {
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "gpt-3.5-turbo");

            List<Map<String, String>> messages = new ArrayList<>();
            Map<String, String> systemMsg = new HashMap<>();
            systemMsg.put("role", "system");
            systemMsg.put("content", systemPrompt);
            messages.add(systemMsg);

            Map<String, String> userMsg = new HashMap<>();
            userMsg.put("role", "user");
            userMsg.put("content", userMessage);
            messages.add(userMsg);

            requestBody.put("messages", messages);
            requestBody.put("temperature", 0.7);
            requestBody.put("max_tokens", 500);

            Map<String, Object> response = webClient.post()
                    .uri(chatApiUrl)
                    .header("Authorization", "Bearer " + openaiApiKey)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response != null && response.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    return message.get("content").toString();
                }
            }

            return "I apologize, but I could not generate a response at this time.";
        } catch (Exception e) {
            log.error("Error generating response: {}", e.getMessage(), e);
            return "I apologize, but I encountered an error processing your request.";
        }
    }
}

