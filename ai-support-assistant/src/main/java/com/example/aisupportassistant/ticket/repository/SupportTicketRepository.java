
package com.example.aisupportassistant.ticket.repository;

import com.example.aisupportassistant.ticket.entity.SupportTicket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupportTicketRepository
        extends JpaRepository<SupportTicket, Long> {
}
