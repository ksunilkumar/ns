package com.nsind.chatbot.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String chatbotId;

    @Column(nullable = false)
    private String userMessage;

    @Lob
    @Column(nullable = false)
    private String botResponse;

    @Lob
    private String context;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}

