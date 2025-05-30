package com.example.checkscam.service;

import com.example.checkscam.dto.team.*;
import java.util.List;

public interface TeamService {
    
    /**
     * Get all teams by tournament ID
     */
    List<TeamResponseDTO> getTeamsByTournament(Long tournamentId);
    
    /**
     * Get team by ID with detailed information
     */
    TeamResponseDTO getTeamById(Long teamId);
    
    /**
     * Register a team for tournament
     */
    TeamRegistrationResponseDTO registerTeamForTournament(Long tournamentId, TeamRegistrationRequestDTO request);
    
    /**
     * Update team information
     */
    TeamUpdateResponseDTO updateTeam(Long teamId, TeamUpdateRequestDTO request);
    
    /**
     * Delete team
     */
    void deleteTeam(Long teamId);
    
    /**
     * Approve team registration (Admin only)
     */
    TeamResponseDTO approveTeam(Long teamId);
    
    /**
     * Reject team registration (Admin only)
     */
    TeamResponseDTO rejectTeam(Long teamId);
    
    /**
     * Update team registration status (Admin only)
     */
    TeamResponseDTO updateTeamRegistrationStatus(Long teamId, String status);
}
