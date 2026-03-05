package com.nsind.document.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChunkingService {

    private static final int CHUNK_SIZE = 1024; // 1024 characters per chunk
    private static final int CHUNK_OVERLAP = 200; // Overlap for context

    public List<String> chunkText(String text) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }

        List<String> chunks = new ArrayList<>();
        int textLength = text.length();

        if (textLength <= CHUNK_SIZE) {
            chunks.add(text);
            log.info("Text chunked into 1 chunk");
            return chunks;
        }

        for (int i = 0; i < textLength; i += (CHUNK_SIZE - CHUNK_OVERLAP)) {
            int endIndex = Math.min(i + CHUNK_SIZE, textLength);
            chunks.add(text.substring(i, endIndex));
        }

        log.info("Text chunked into {} chunks", chunks.size());
        return chunks;
    }

    public List<String> chunkTextBySentence(String text, int maxChunkSize) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }

        List<String> chunks = new ArrayList<>();
        String[] sentences = text.split("(?<=[.!?])\\s+");

        StringBuilder currentChunk = new StringBuilder();

        for (String sentence : sentences) {
            if (currentChunk.length() + sentence.length() > maxChunkSize &&
                currentChunk.length() > 0) {
                chunks.add(currentChunk.toString());
                currentChunk = new StringBuilder();
            }

            if (currentChunk.length() > 0) {
                currentChunk.append(" ");
            }
            currentChunk.append(sentence);
        }

        if (currentChunk.length() > 0) {
            chunks.add(currentChunk.toString());
        }

        log.info("Text chunked by sentence into {} chunks", chunks.size());
        return chunks;
    }
}

