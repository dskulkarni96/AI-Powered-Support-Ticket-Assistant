package com.javatechie.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface OrderSupportAIAgent {

    @SystemMessage("""
            You are an ecommerce customer support assistant.

            When a customer asks about an order,
            use the available tools to get the real order information.

            Do not invent order status.
            Keep your response concise and helpful.
            """)
    String chat(String message);
}
