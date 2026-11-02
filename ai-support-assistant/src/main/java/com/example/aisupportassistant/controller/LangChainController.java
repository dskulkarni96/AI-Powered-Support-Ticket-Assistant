package com.example.aisupportassistant.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.aisupportassistant.service.LangChainService;
import com.example.aisupportassistant.ticket.dto.AITicketClassification;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/ai")
public class LangChainController {
	
	private final LangChainService langChainService;

	public LangChainController(LangChainService langChainService) {
		super();
		this.langChainService = langChainService;
	}
	
	@PostMapping("/ask")
	public String ask(@RequestBody String question) {
		return langChainService.ask(question);
	}
	
	@GetMapping("/test")
	public AITicketClassification testAI(@RequestParam String question) {
	    return langChainService.classifyTicket(question);
	}

}
