package com.javatechie.service;


import dev.langchain4j.service.spring.AiService;

@AiService
public interface JavaTechieAIAgent {

    String chat(String message);
}