package com.nsind.document.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.Text;
import org.springframework.stereotype.Service;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
@Slf4j
public class FileExtractorService {

    public String extractText(File file, String fileType) throws Exception {
        return switch (fileType.toLowerCase()) {
            case ".pdf" -> extractPdf(file);
            case ".txt" -> extractTxt(file);
            case ".docx" -> extractDocx(file);
            case ".xlsx", ".xls" -> extractExcel(file);
            case ".csv" -> extractCsv(file);
            default -> throw new IllegalArgumentException("Unsupported file type: " + fileType);
        };
    }

    private String extractPdf(File file) throws Exception {
        StringBuilder text = new StringBuilder();
        try (PDDocument document = PDDocument.load(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            text.append(stripper.getText(document));
        }
        log.info("Extracted text from PDF: {} characters", text.length());
        return text.toString();
    }

    private String extractTxt(File file) throws Exception {
        String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        log.info("Extracted text from TXT: {} characters", content.length());
        return content;
    }

    private String extractDocx(File file) throws Exception {
        StringBuilder text = new StringBuilder();
        try {
            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(file);
            org.docx4j.wml.Document wmlDocument = wordMLPackage.getMainDocumentPart().getDocument();

            List<Object> allElements = wmlDocument.getBody().getContent();
            for (Object element : allElements) {
                text.append(extractTextFromElement(element));
            }
        } catch (Exception e) {
            log.error("Error extracting DOCX: {}", e.getMessage());
            // Fallback to simple text extraction
            text.append(Files.readString(file.toPath()));
        }
        log.info("Extracted text from DOCX: {} characters", text.length());
        return text.toString();
    }

    private String extractExcel(File file) throws Exception {
        StringBuilder text = new StringBuilder();
        try (XSSFWorkbook workbook = new XSSFWorkbook(file)) {
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                text.append("Sheet: ").append(sheet.getSheetName()).append("\n");

                for (Row row : sheet) {
                    for (Cell cell : row) {
                        text.append(cell.toString()).append(" ");
                    }
                    text.append("\n");
                }
            }
        }
        log.info("Extracted text from Excel: {} characters", text.length());
        return text.toString();
    }

    private String extractCsv(File file) throws Exception {
        StringBuilder text = new StringBuilder();
        try (Reader reader = new FileReader(file);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT)) {

            for (CSVRecord record : csvParser) {
                for (String value : record) {
                    text.append(value).append(" ");
                }
                text.append("\n");
            }
        }
        log.info("Extracted text from CSV: {} characters", text.length());
        return text.toString();
    }

    private String extractTextFromElement(Object element) {
        StringBuilder text = new StringBuilder();

        if (element instanceof jakarta.xml.bind.JAXBElement) {
            jakarta.xml.bind.JAXBElement<?> jaxbElement = (jakarta.xml.bind.JAXBElement<?>) element;
            Object value = jaxbElement.getValue();

            if (value instanceof org.docx4j.wml.P) {
                org.docx4j.wml.P paragraph = (org.docx4j.wml.P) value;
                for (Object child : paragraph.getContent()) {
                    if (child instanceof jakarta.xml.bind.JAXBElement) {
                        Object childValue = ((jakarta.xml.bind.JAXBElement<?>) child).getValue();
                        if (childValue instanceof org.docx4j.wml.R) {
                            org.docx4j.wml.R run = (org.docx4j.wml.R) childValue;
                            for (Object runChild : run.getContent()) {
                                if (runChild instanceof jakarta.xml.bind.JAXBElement) {
                                    Object runValue = ((jakarta.xml.bind.JAXBElement<?>) runChild).getValue();
                                    if (runValue instanceof Text) {
                                        text.append(((Text) runValue).getValue());
                                    }
                                }
                            }
                        }
                    }
                }
                text.append("\n");
            }
        }

        return text.toString();
    }
}

