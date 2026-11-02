package com.example.aisupportassistant.ticket.dto;

import java.time.LocalDateTime;

import com.example.aisupportassistant.ticket.entity.AffectedUserScope;

public record CreateTicketResponse(
        Long id,
        String title,
        String description,
        String category,
        String aiPriority,
        AffectedUserScope affectedUserScope,
        String priority,
        String status,
        String aiSuggestedResponse,
        LocalDateTime createdAt
) {}