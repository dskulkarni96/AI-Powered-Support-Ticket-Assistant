
package com.example.aisupportassistant.ticket.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "support_tickets")
public class SupportTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(length=255)
    private String title;

    @Column(length = 2000)
    private String description;

    private String category;

    private String priority;

    private String status;

    @Column(columnDefinition = "TEXT")
    private String aiSuggestedResponse;

    private LocalDateTime createdAt;
    
    @Column(name = "ai_priority")
    private String aiPriority;

    @Enumerated(EnumType.STRING)
    @Column(name = "affected_user_scope")
    private AffectedUserScope affectedUserScope;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();

        if (this.status == null) {
            this.status = "OPEN";
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAiSuggestedResponse() {
        return aiSuggestedResponse;
    }

    public void setAiSuggestedResponse(String aiSuggestedResponse) {
        this.aiSuggestedResponse = aiSuggestedResponse;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

	public String getAiPriority() {
		return aiPriority;
	}

	public void setAiPriority(String aiPriority) {
		this.aiPriority = aiPriority;
	}

	public AffectedUserScope getAffectedUserScope() {
		return affectedUserScope;
	}

	public void setAffectedUserScope(AffectedUserScope affectedUserScope) {
		this.affectedUserScope = affectedUserScope;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
    
    
}
