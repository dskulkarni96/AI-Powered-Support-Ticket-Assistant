package com.example.aisupportassistant.service;

import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.example.aisupportassistant.ticket.dto.AITicketClassification;

import dev.langchain4j.model.chat.ChatModel;

@Service
public class LangChainService {

	private final ChatModel chatModel;

	public LangChainService(ChatModel chatModel) {
		super();
		this.chatModel = chatModel;
	}
	
	public String ask(String question) {
		return chatModel.chat(question);
	}
	
    

	public AITicketClassification classifyTicket(String question) {

	    String prompt = """
	            Classify this support ticket.

	            Return exactly two lines:
	            category=ONE_ALLOWED_CATEGORY
	            priority=ONE_ALLOWED_PRIORITY

	            Allowed categories and their meanings:
Classification rules:

1. If the issue mentions login, password, invalid
   credentials, authentication failure or account
   lockout, ALWAYS classify as AUTHENTICATION.

2. If the issue involves payment, refunds,
   billing or failed transactions, classify as PAYMENT.

3. If the issue involves application crashes,
   bugs, performance or technical errors,
   classify as TECHNICAL.

4. If the issue involves profile changes or
   account settings unrelated to login,
   classify as ACCOUNT.

5. Use OTHER only when none of the above apply.

IMPORTANT:
Login and password issues must NEVER be classified
as TECHNICAL or OTHER.
	            Allowed priorities: LOW, MEDIUM, HIGH, CRITICAL

	            Do not explain your answer.
	            Do not use markdown.

	            Ticket:
	            """ + question;

	    String response = chatModel.chat(prompt);

	    String category = null;
	    String priority = null;

	    for (String line : response.split("\\R")) {

	        line = line.trim();

	        if (line.toLowerCase().startsWith("category=")
	                || line.toLowerCase().startsWith("category:")) {

	            category = line.substring(
	                    Math.max(line.indexOf('='),
	                             line.indexOf(':')) + 1).trim();
	        }

	        if (line.toLowerCase().startsWith("priority=")
	                || line.toLowerCase().startsWith("priority:")) {

	            priority = line.substring(
	                    Math.max(line.indexOf('='),
	                             line.indexOf(':')) + 1).trim();
	        }
	    }
	    
	    category = category==null ? "OTHER" : category.toUpperCase(Locale.ROOT);
	    priority = priority== null ? "MEDIUM" : priority.toUpperCase(Locale.ROOT);
	    
	    // Validate category
	    if (!List.of("AUTHENTICATION", "PAYMENT",
	            "TECHNICAL", "ACCOUNT", "OTHER").contains(category)) {
	        category = "OTHER";
	    }

	    // Validate priority
	    if (!List.of("LOW", "MEDIUM", "HIGH", "CRITICAL")
	            .contains(priority)) {
	        priority = "MEDIUM";
	    }

	    // Guardrail for obvious payment-related tickets
	    String lowerQuestion = question.toLowerCase();

	    if (lowerQuestion.matches("(?s).*\\b(payment|transaction|billing|refund|deducted|charged)\\b.*")) {
	        category = "PAYMENT";
	    }

	    return new AITicketClassification(category, priority);
	}
	
	

public String generateSuggestedResponse(String title, String description) {

    String prompt = """
            You are a professional customer support assistant.

            Generate a helpful, polite and concise response
            to the customer's support issue.

            Acknowledge the problem and suggest practical
            troubleshooting steps where appropriate.

            Do not claim that the issue has been resolved.
            Do not invent company policies or facts.

            Ticket title: %s

            Ticket description: %s
            """.formatted(title, description);

    return chatModel.chat(prompt);
}


public String generateAnswerFromKnowledge(String question, String context) {

    String prompt = """
            You are a customer support assistant.

            Answer the customer's question using ONLY the
            information in the support documentation.

            STRICT RULES:
            1. Do not invent troubleshooting steps.
            2. Do not promise refunds, order placement,
               resolution, or actions not mentioned in the documentation.
            3. Include the relevant troubleshooting steps
               provided in the documentation.
            4. If information is missing, clearly state that.
            5. Address the customer directly and politely.
            6. Present ALL relevant troubleshooting steps from
   the documentation as a numbered list.
7. Do not stop after asking the customer for one detail.

            SUPPORT DOCUMENTATION:
            %s

            CUSTOMER QUESTION:
            %s

            CUSTOMER RESPONSE:
            """.formatted(context, question);

    return chatModel.chat(prompt);
}


}

    

