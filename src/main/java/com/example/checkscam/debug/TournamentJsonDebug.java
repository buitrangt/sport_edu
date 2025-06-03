package com.example.checkscam.debug;

import com.example.checkscam.dto.request.TournamentRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class TournamentJsonDebug {
    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        // Sample JSON that frontend sends
        String frontendJson = "{\"name\":\"Test Tournament\",\"description\":\"Test Description\",\"sportType\":\"FOOTBALL\",\"maxTeams\":16,\"startDate\":\"2025-06-02T10:00:00.000Z\",\"endDate\":\"2025-06-03T18:00:00.000Z\",\"registrationDeadline\":\"2025-06-01T23:59:00.000Z\",\"location\":\"Test Location\",\"rules\":\"\",\"prizeInfo\":\"\",\"contactInfo\":\"test@example.com\"}";
        
        try {
            System.out.println("Testing JSON parsing...");
            System.out.println("Input JSON: " + frontendJson);
            
            TournamentRequestDTO request = objectMapper.readValue(frontendJson, TournamentRequestDTO.class);
            
            System.out.println("✅ SUCCESS! Parsed tournament:");
            System.out.println("Name: " + request.getName());
            System.out.println("Description: " + request.getDescription());
            System.out.println("Start Date: " + request.getStartDate());
            System.out.println("End Date: " + request.getEndDate());
            System.out.println("Registration Deadline: " + request.getRegistrationDeadline());
            System.out.println("Location: " + request.getLocation());
            System.out.println("Contact Info: " + request.getContactInfo());
            
        } catch (Exception e) {
            System.err.println("❌ FAILED to parse JSON:");
            e.printStackTrace();
        }
    }
}
