package com.example.checkscam.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    String uploadTournamentImage(MultipartFile file);
    void deleteFile(String fileName);
    boolean isValidImageFile(MultipartFile file);
}
