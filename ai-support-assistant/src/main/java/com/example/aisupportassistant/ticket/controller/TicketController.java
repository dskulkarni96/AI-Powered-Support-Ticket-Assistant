
package com.example.aisupportassistant.ticket.controller;

import com.example.aisupportassistant.ticket.dto.CreateTicketRequest;
import com.example.aisupportassistant.ticket.dto.CreateTicketResponse;
import com.example.aisupportassistant.ticket.dto.ImpactAssessmentRequest;
import com.example.aisupportassistant.ticket.service.TicketService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateTicketResponse createTicket(
            @Valid @RequestBody CreateTicketRequest request) {

        return ticketService.createTicket(request);
    }
    
    @GetMapping("/{id}")
    public CreateTicketResponse getTicketById(@PathVariable Long id) {
        return ticketService.getTicketById(id);
    }

    @GetMapping
    public List<CreateTicketResponse> getAllTickets() {
        return ticketService.getAllTickets();
    }
    
    @PutMapping("/{id}")
    public CreateTicketResponse updateTicket(
            @PathVariable Long id,
            @Valid @RequestBody CreateTicketRequest request) {

        return ticketService.updateTicket(id, request);
    }
    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
    }
    
    @PutMapping("/{id}/impact")
    public ResponseEntity<String> assessImpact(
            @PathVariable Long id,
            @Valid @RequestBody ImpactAssessmentRequest request) {

        ticketService.assessImpact(id, request.affectedUserScope());

        return ResponseEntity.ok("Ticket impact assessed successfully");
    }
    
    
}
