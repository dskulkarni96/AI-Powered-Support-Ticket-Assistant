
package com.example.aisupportassistant.ticket.service;

import com.example.aisupportassistant.ticket.dto.AITicketClassification;
import com.example.aisupportassistant.ticket.dto.CreateTicketRequest;
import com.example.aisupportassistant.ticket.dto.CreateTicketResponse;
import com.example.aisupportassistant.ticket.entity.AffectedUserScope;
import com.example.aisupportassistant.ticket.entity.SupportTicket;
import com.example.aisupportassistant.ticket.exception.TicketNotFoundException;
import com.example.aisupportassistant.ticket.repository.SupportTicketRepository;
import com.example.aisupportassistant.knowledge.service.RagService;
import com.example.aisupportassistant.service.LangChainService;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class TicketService {

    private final SupportTicketRepository repository;
    private final LangChainService langChainService;
    private final RagService ragService;

    public TicketService(SupportTicketRepository repository,LangChainService
    		langChainService,RagService ragService) {
        this.repository = repository;
        this.langChainService = langChainService;
        this.ragService = ragService;
    }

    public CreateTicketResponse createTicket(CreateTicketRequest request) {

        SupportTicket ticket = new SupportTicket();

        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());

        AITicketClassification classification =
                langChainService.classifyTicket(
                        request.getTitle() + "\n" + request.getDescription()
                );

        ticket.setCategory(classification.category());
        ticket.setAiPriority(classification.priority());
        ticket.setPriority(classification.priority());
        
        String customerQuestion = request.getTitle()+ '\n' + request.getDescription();
        String suggestedResponse = ragService.answerQuestion(customerQuestion);
        ticket.setAiSuggestedResponse(suggestedResponse);

        SupportTicket savedTicket = repository.save(ticket);

       return mapToResponse(savedTicket);
    }
    
    public CreateTicketResponse getTicketById(Long id) {

        SupportTicket ticket = repository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(
                        "Ticket not found with ID: " + id));

        return mapToResponse(ticket);
    }
    
    public List<CreateTicketResponse> getAllTickets() {

        return repository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
    
    public CreateTicketResponse updateTicket(Long id, CreateTicketRequest request) {

        SupportTicket ticket = repository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(
                        "Ticket not found with ID: " + id));

        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());

        SupportTicket updatedTicket = repository.save(ticket);

        return mapToResponse(updatedTicket);
    }
    
    public void deleteTicket(Long id) {

        if (!repository.existsById(id)) {
            throw new TicketNotFoundException(
                    "Ticket not found with ID: " + id);
        }

        repository.deleteById(id);
    }
    
    private String calculateFinalPriority(
            String aiPriority,
            AffectedUserScope scope) {
    	
    	String normalizedPriority = aiPriority == null ? "MEDIUM" : aiPriority;

        int aiLevel = switch (normalizedPriority.toUpperCase()) {
            case "LOW" -> 1;
            case "MEDIUM" -> 2;
            case "HIGH" -> 3;
            case "CRITICAL" -> 4;
            default -> 2;
        };

        int impactLevel = switch (scope) {
            case FEW -> 1;
            case MEDIUM -> 2;
            case MOST -> 3;
            case ALL -> 4;
        };

        int finalLevel = Math.max(aiLevel, impactLevel);

        return switch (finalLevel) {
            case 1 -> "LOW";
            case 2 -> "MEDIUM";
            case 3 -> "HIGH";
            case 4 -> "CRITICAL";
            default -> "MEDIUM";
        };
    }
    
    public void assessImpact(Long ticketId, AffectedUserScope scope) {

        SupportTicket ticket = repository.findById(ticketId)
                .orElseThrow(() ->
                        new TicketNotFoundException("Ticket not found: " + ticketId));

        // Handle tickets created before aiPriority was introduced
        if (ticket.getAiPriority() == null) {
            ticket.setAiPriority(ticket.getPriority());
        }

        ticket.setAffectedUserScope(scope);

        String finalPriority = calculateFinalPriority(
                ticket.getAiPriority(),
                scope
        );

        ticket.setPriority(finalPriority);

        repository.save(ticket);
    }

    
    private CreateTicketResponse mapToResponse(SupportTicket ticket) {

        return new CreateTicketResponse(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getCategory(),
                ticket.getAiPriority(),
                ticket.getAffectedUserScope(),
                ticket.getPriority(),
                ticket.getStatus(),
                ticket.getAiSuggestedResponse(),
                ticket.getCreatedAt()
        );
    }
}
