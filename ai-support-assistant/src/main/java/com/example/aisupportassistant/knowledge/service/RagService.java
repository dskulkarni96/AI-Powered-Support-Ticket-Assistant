
package com.example.aisupportassistant.knowledge.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import com.example.aisupportassistant.service.LangChainService;

@Service
public class RagService {

    private final VectorStore vectorStore;
    private final LangChainService langChainService;
    private final Logger logger = LoggerFactory.getLogger(RagService.class);

    public RagService(
            VectorStore vectorStore,
            LangChainService langChainService) {

        this.vectorStore = vectorStore;
        this.langChainService = langChainService;
    }

    public String answerQuestion(String question) {

        List<Document> documents = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(question)
                        .topK(1)
                        .build()
        );
        if(documents.isEmpty()) {
        	 return "I couldn't find relevant information in the support knowledge base. Please escalate this issue to the support team.";
        }

        String context = documents.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n\n"));
        
        logger.info("Question received: {}", question);
        logger.debug("Retrieved context: {}", context);

        return langChainService.generateAnswerFromKnowledge(
                question, context
        );
    }
}
