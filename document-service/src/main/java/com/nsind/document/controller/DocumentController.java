package com.nsind.document.controller;

import com.nsind.document.dto.DocumentResponse;
import com.nsind.document.service.DocumentService;
import com.nsind.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
@Tag(name = "Documents", description = "Document upload and processing")
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping("/upload")
    @Operation(summary = "Upload a document")
    public ResponseEntity<ApiResponse<DocumentResponse>> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestHeader("X-User-Id") String userId) throws Exception {
        DocumentResponse response = documentService.uploadDocument(file, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Document uploaded successfully"));
    }

    @PostMapping("/process/{documentId}")
    @Operation(summary = "Process uploaded document")
    public ResponseEntity<ApiResponse<Void>> processDocument(
            @PathVariable String documentId,
            @RequestHeader("X-User-Id") String userId) throws Exception {
        documentService.processDocument(documentId);
        return ResponseEntity.ok(ApiResponse.success(null, "Document processing started"));
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get user documents")
    public ResponseEntity<ApiResponse<List<DocumentResponse>>> getUserDocuments(
            @PathVariable String userId) {
        List<DocumentResponse> documents = documentService.getUserDocuments(userId);
        return ResponseEntity.ok(ApiResponse.success(documents, "Documents retrieved"));
    }

    @GetMapping("/{documentId}/details")
    @Operation(summary = "Get document details")
    public ResponseEntity<ApiResponse<DocumentResponse>> getDocument(
            @PathVariable String documentId,
            @RequestHeader("X-User-Id") String userId) {
        DocumentResponse document = documentService.getDocument(documentId, userId);
        return ResponseEntity.ok(ApiResponse.success(document, "Document retrieved"));
    }

    @DeleteMapping("/{documentId}")
    @Operation(summary = "Delete document")
    public ResponseEntity<ApiResponse<Void>> deleteDocument(
            @PathVariable String documentId,
            @RequestHeader("X-User-Id") String userId) throws Exception {
        documentService.deleteDocument(documentId, userId);
        return ResponseEntity.ok(ApiResponse.success(null, "Document deleted"));
    }
}

