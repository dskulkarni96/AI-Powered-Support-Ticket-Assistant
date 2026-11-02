package com.example.aisupportassistant.ticket.dto;

import com.example.aisupportassistant.ticket.entity.AffectedUserScope;

import jakarta.validation.constraints.NotNull;

public record ImpactAssessmentRequest(
        @NotNull AffectedUserScope affectedUserScope
) {
}