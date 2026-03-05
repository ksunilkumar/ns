package com.nsind.document.service;

import com.nsind.document.dto.DocumentResponse;
import com.nsind.document.entity.Document;
import com.nsind.document.entity.DocumentChunk;
import com.nsind.document.repository.DocumentChunkRepository;
import com.nsind.document.repository.DocumentRepository;
import com.nsind.common.exception.BadRequestException;
import com.nsind.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentService {

    @Value("${file.upload.dir:uploads}")
    private String uploadDir;

    private final DocumentRepository documentRepository;
    private final DocumentChunkRepository chunkRepository;
    private final FileExtractorService extractorService;
    private final ChunkingService chunkingService;

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final String[] ALLOWED_EXTENSIONS = {".pdf", ".txt", ".docx", ".xlsx", ".xls", ".csv"};

    @Transactional
    public DocumentResponse uploadDocument(MultipartFile file, String userId) throws IOException {
        validateFile(file);

        String originalFileName = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFileName);
        String storedFileName = UUID.randomUUID() + fileExtension;

        // Create user directory
        Path userDir = Paths.get(uploadDir, userId);
        Files.createDirectories(userDir);

        Path filePath = userDir.resolve(storedFileName);
        Files.write(filePath, file.getBytes());

        Document document = Document.builder()
                .userId(userId)
                .fileName(originalFileName)
                .filePath(filePath.toString())
                .fileType(fileExtension)
                .fileSize(file.getSize())
                .status(Document.DocumentStatus.UPLOADED)
                .build();

        document = documentRepository.save(document);
        log.info("Document uploaded: {} for user: {}", originalFileName, userId);

        return mapToResponse(document);
    }

    @Transactional
    public void processDocument(String documentId) throws Exception {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        document.setStatus(Document.DocumentStatus.PROCESSING);
        document.setProcessingStartedAt(LocalDateTime.now());
        documentRepository.save(document);

        try {
            // Extract text
            File file = new File(document.getFilePath());
            String extractedText = extractorService.extractText(file, document.getFileType());
            document.setExtractedText(extractedText);

            // Chunk text
            List<String> chunks = chunkingService.chunkText(extractedText);

            // Save chunks
            for (int i = 0; i < chunks.size(); i++) {
                DocumentChunk chunk = DocumentChunk.builder()
                        .documentId(documentId)
                        .chunkIndex(i)
                        .chunkText(chunks.get(i))
                        .build();
                chunkRepository.save(chunk);
            }

            document.setChunkCount(chunks.size());
            document.setStatus(Document.DocumentStatus.COMPLETED);
            document.setProcessingCompletedAt(LocalDateTime.now());
            documentRepository.save(document);

            log.info("Document processed: {} with {} chunks", documentId, chunks.size());
        } catch (Exception e) {
            document.setStatus(Document.DocumentStatus.FAILED);
            documentRepository.save(document);
            log.error("Error processing document: {}", documentId, e);
            throw e;
        }
    }

    public List<DocumentResponse> getUserDocuments(String userId) {
        return documentRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public DocumentResponse getDocument(String documentId, String userId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        if (!document.getUserId().equals(userId)) {
            throw new BadRequestException("Unauthorized access to document");
        }

        return mapToResponse(document);
    }

    @Transactional
    public void deleteDocument(String documentId, String userId) throws IOException {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        if (!document.getUserId().equals(userId)) {
            throw new BadRequestException("Unauthorized access to document");
        }

        // Delete file
        Files.deleteIfExists(Paths.get(document.getFilePath()));

        // Delete chunks
        chunkRepository.deleteByDocumentId(documentId);

        // Delete document
        documentRepository.deleteById(documentId);

        log.info("Document deleted: {}", documentId);
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestException("File is empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BadRequestException("File size exceeds maximum limit of 10MB");
        }

        String fileName = file.getOriginalFilename();
        String extension = getFileExtension(fileName);

        boolean isAllowed = false;
        for (String allowed : ALLOWED_EXTENSIONS) {
            if (allowed.equalsIgnoreCase(extension)) {
                isAllowed = true;
                break;
            }
        }

        if (!isAllowed) {
            throw new BadRequestException("File type not allowed. Allowed types: PDF, TXT, DOCX, XLSX, XLS, CSV");
        }
    }

    private String getFileExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        return lastDot > 0 ? fileName.substring(lastDot).toLowerCase() : "";
    }

    private DocumentResponse mapToResponse(Document document) {
        return DocumentResponse.builder()
                .id(document.getId())
                .fileName(document.getFileName())
                .fileType(document.getFileType())
                .fileSize(document.getFileSize())
                .status(document.getStatus().toString())
                .chunkCount(document.getChunkCount())
                .createdAt(document.getCreatedAt().getTime())
                .updatedAt(document.getUpdatedAt().getTime())
                .build();
    }
}

