package com.nsind.chatbot.service;

import com.nsind.chatbot.dto.ChatRequest;
import com.nsind.chatbot.dto.ChatResponse;
import com.nsind.chatbot.entity.Chatbot;
import com.nsind.chatbot.entity.ChatMessage;
import com.nsind.chatbot.repository.ChatMessageRepository;
import com.nsind.chatbot.repository.ChatbotRepository;
import com.nsind.common.exception.BadRequestException;
import com.nsind.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ChatbotService
 */
@DisplayName("ChatbotService Tests")
class ChatbotServiceTest {

    @Mock
    private ChatbotRepository chatbotRepository;

    @Mock
    private ChatMessageRepository messageRepository;

    @Mock
    private WebClient webClient;

    @InjectMocks
    private ChatbotService chatbotService;

    private Chatbot testChatbot;
    private String testUserId;
    private String testChatbotId;
    private String testApiKey;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUserId = "user-123";
        testChatbotId = "chatbot-456";
        testApiKey = "api-key-789";

        testChatbot = Chatbot.builder()
                .id(testChatbotId)
                .userId(testUserId)
                .name("Test Chatbot")
                .description("Test Description")
                .apiKey(testApiKey)
                .systemPrompt("You are a helpful assistant")
                .isActive(true)
                .build();
    }

    @Test
    @DisplayName("Should create chatbot successfully")
    void testCreateChatbot_Success() {
        // Given
        when(chatbotRepository.save(any(Chatbot.class))).thenReturn(testChatbot);

        // When
        Chatbot result = chatbotService.createChatbot(testUserId, "Test Chatbot", "Test Description");

        // Then
        assertNotNull(result);
        assertEquals(testChatbotId, result.getId());
        assertEquals(testUserId, result.getUserId());
        assertEquals("Test Chatbot", result.getName());
        assertTrue(result.getIsActive());
        verify(chatbotRepository, times(1)).save(any(Chatbot.class));
    }

    @Test
    @DisplayName("Should create chatbot with null description")
    void testCreateChatbot_WithNullDescription() {
        // Given
        when(chatbotRepository.save(any(Chatbot.class))).thenReturn(testChatbot);

        // When
        Chatbot result = chatbotService.createChatbot(testUserId, "Test Chatbot", null);

        // Then
        assertNotNull(result);
        assertEquals(testChatbotId, result.getId());
        verify(chatbotRepository, times(1)).save(any(Chatbot.class));
    }

    @Test
    @DisplayName("Should get user chatbots successfully")
    void testGetUserChatbots_Success() {
        // Given
        List<Chatbot> chatbots = Arrays.asList(testChatbot);
        when(chatbotRepository.findByUserId(testUserId)).thenReturn(chatbots);

        // When
        List<Chatbot> result = chatbotService.getUserChatbots(testUserId);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testChatbotId, result.get(0).getId());
        verify(chatbotRepository, times(1)).findByUserId(testUserId);
    }

    @Test
    @DisplayName("Should return empty list when user has no chatbots")
    void testGetUserChatbots_EmptyList() {
        // Given
        when(chatbotRepository.findByUserId(testUserId)).thenReturn(new ArrayList<>());

        // When
        List<Chatbot> result = chatbotService.getUserChatbots(testUserId);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(chatbotRepository, times(1)).findByUserId(testUserId);
    }

    @Test
    @DisplayName("Should get chatbot details successfully")
    void testGetChatbot_Success() {
        // Given
        when(chatbotRepository.findById(testChatbotId)).thenReturn(Optional.of(testChatbot));

        // When
        Chatbot result = chatbotService.getChatbot(testChatbotId, testUserId);

        // Then
        assertNotNull(result);
        assertEquals(testChatbotId, result.getId());
        assertEquals(testUserId, result.getUserId());
        verify(chatbotRepository, times(1)).findById(testChatbotId);
    }

    @Test
    @DisplayName("Should throw exception when chatbot not found")
    void testGetChatbot_NotFound() {
        // Given
        when(chatbotRepository.findById(testChatbotId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class,
            () -> chatbotService.getChatbot(testChatbotId, testUserId));
        verify(chatbotRepository, times(1)).findById(testChatbotId);
    }

    @Test
    @DisplayName("Should throw exception when user tries to access other user's chatbot")
    void testGetChatbot_UnauthorizedAccess() {
        // Given
        String anotherUserId = "another-user";
        when(chatbotRepository.findById(testChatbotId)).thenReturn(Optional.of(testChatbot));

        // When & Then
        assertThrows(BadRequestException.class,
            () -> chatbotService.getChatbot(testChatbotId, anotherUserId));
    }

    @Test
    @DisplayName("Should delete chatbot successfully")
    void testDeleteChatbot_Success() {
        // Given
        when(chatbotRepository.findById(testChatbotId)).thenReturn(Optional.of(testChatbot));
        doNothing().when(chatbotRepository).deleteById(testChatbotId);

        // When
        chatbotService.deleteChatbot(testChatbotId, testUserId);

        // Then
        verify(chatbotRepository, times(1)).findById(testChatbotId);
        verify(chatbotRepository, times(1)).deleteById(testChatbotId);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent chatbot")
    void testDeleteChatbot_NotFound() {
        // Given
        when(chatbotRepository.findById(testChatbotId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class,
            () -> chatbotService.deleteChatbot(testChatbotId, testUserId));
        verify(chatbotRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should throw exception when user tries to delete other user's chatbot")
    void testDeleteChatbot_UnauthorizedAccess() {
        // Given
        String anotherUserId = "another-user";
        when(chatbotRepository.findById(testChatbotId)).thenReturn(Optional.of(testChatbot));

        // When & Then
        assertThrows(BadRequestException.class,
            () -> chatbotService.deleteChatbot(testChatbotId, anotherUserId));
        verify(chatbotRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should throw exception when sending message with invalid API key")
    void testChat_InvalidApiKey() {
        // Given
        ChatRequest request = ChatRequest.builder()
                .apiKey("invalid-key")
                .message("Hello")
                .userId(testUserId)
                .chatbotId(testChatbotId)
                .build();

        when(chatbotRepository.findByApiKey("invalid-key")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(BadRequestException.class, () -> chatbotService.chat(request));
    }

    @Test
    @DisplayName("Should throw exception when chatbot is inactive")
    void testChat_InactiveChatbot() {
        // Given
        Chatbot inactiveChatbot = testChatbot.toBuilder().isActive(false).build();
        ChatRequest request = ChatRequest.builder()
                .apiKey(testApiKey)
                .message("Hello")
                .userId(testUserId)
                .chatbotId(testChatbotId)
                .build();

        when(chatbotRepository.findByApiKey(testApiKey)).thenReturn(Optional.of(inactiveChatbot));

        // When & Then
        assertThrows(BadRequestException.class, () -> chatbotService.chat(request));
    }
}

