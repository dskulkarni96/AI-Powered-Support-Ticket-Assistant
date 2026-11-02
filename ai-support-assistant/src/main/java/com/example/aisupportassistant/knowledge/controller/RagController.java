package com.example.aisupportassistant.knowledge.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.aisupportassistant.knowledge.service.RagService;

@RestController
@RequestMapping("/api/knowledge")
public class RagController {

    private final RagService ragService;

    public RagController(RagService ragService) {
        this.ragService = ragService;
    }

    @GetMapping("/ask")
    public String askQuestion(@RequestParam String question) {
        return ragService.answerQuestion(question);
    }
}