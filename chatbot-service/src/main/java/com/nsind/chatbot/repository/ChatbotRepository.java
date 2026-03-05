package com.nsind.chatbot.repository;

import com.nsind.chatbot.entity.Chatbot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatbotRepository extends JpaRepository<Chatbot, String> {
    List<Chatbot> findByUserId(String userId);
    Optional<Chatbot> findByApiKey(String apiKey);
}

