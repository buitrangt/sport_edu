package com.example.checkscam.service.impl;

import com.example.checkscam.service.FileUploadService;
import com.example.checkscam.exception.FileUploadValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class FileUploadServiceImpl implements FileUploadService {
    
    @Value("${app.upload.tournament-images:/uploads/tournaments}")
    private String tournamentImagePath;
    
    @Value("${app.upload.max-file-size:5242880}") // 5MB default
    private long maxFileSize;
    
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif");
    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList(
        "image/jpeg", "image/jpg", "image/png", "image/gif"
    );

    @Override
    public String uploadTournamentImage(MultipartFile file) {
        validateFile(file);
        
        try {
            // Create upload directory if not exists
            Path uploadPath = Paths.get(tournamentImagePath);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String extension = getFileExtension(originalFilename);
            String filename = UUID.randomUUID().toString() + "." + extension;
            
            // Save file
            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            log.info("Successfully uploaded tournament image: {}", filename);
            return filename;
            
        } catch (IOException e) {
            log.error("Failed to upload file", e);
            throw new FileUploadValidationException("file.upload.failed", 
                HttpStatus.INTERNAL_SERVER_ERROR, 
                e.getMessage());
        }
    }

    @Override
    public void deleteFile(String fileName) {
        try {
            if (fileName != null && !fileName.isEmpty()) {
                Path filePath = Paths.get(tournamentImagePath).resolve(fileName);
                Files.deleteIfExists(filePath);
                log.info("Successfully deleted file: {}", fileName);
            }
        } catch (IOException e) {
            log.error("Failed to delete file: {}", fileName, e);
        }
    }

    @Override
    public boolean isValidImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }
        
        String contentType = file.getContentType();
        String filename = file.getOriginalFilename();
        
        return contentType != null && 
               ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase()) &&
               filename != null && 
               hasValidExtension(filename) &&
               file.getSize() <= maxFileSize;
    }
    
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FileUploadValidationException("file.required", 
                HttpStatus.BAD_REQUEST);
        }
        
        if (file.getSize() > maxFileSize) {
            throw new FileUploadValidationException("file.size.exceeded", 
                HttpStatus.BAD_REQUEST, 
                maxFileSize);
        }
        
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new FileUploadValidationException("file.type.invalid", 
                HttpStatus.BAD_REQUEST, 
                String.join(", ", ALLOWED_CONTENT_TYPES));
        }
        
        String filename = file.getOriginalFilename();
        if (filename == null || !hasValidExtension(filename)) {
            throw new FileUploadValidationException("file.extension.invalid", 
                HttpStatus.BAD_REQUEST, 
                String.join(", ", ALLOWED_EXTENSIONS));
        }
    }
    
    private boolean hasValidExtension(String filename) {
        String extension = getFileExtension(filename);
        return ALLOWED_EXTENSIONS.contains(extension.toLowerCase());
    }
    
    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf('.') == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.') + 1);
    }
}
