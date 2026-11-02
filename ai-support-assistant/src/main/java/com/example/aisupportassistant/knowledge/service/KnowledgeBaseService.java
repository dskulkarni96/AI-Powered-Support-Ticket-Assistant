
package com.example.aisupportassistant.knowledge.service;

import java.util.List;
import java.util.Map;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class KnowledgeBaseService {

    private final VectorStore vectorStore;

    public KnowledgeBaseService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    
    @PostConstruct
    public void loadKnowledgeBase() {

        List<Document> documents = List.of(

            new Document("""
                DOCUMENT: LOGIN ISSUES

                Problem: Invalid credentials error

                Possible causes:
                - Incorrect username or password.
                - Account temporarily locked after multiple failed login attempts.
                - Password recently changed but old credentials are being used.
                - Browser session or cached authentication data is outdated.

                Troubleshooting:
                1. Verify the username and password.
                2. Check whether the account is locked.
                3. Clear browser cache and cookies.
                4. Try accessing the application using another browser.
                5. If the issue persists, escalate to the authentication support team.
                """,
                Map.of("source", "support-knowledge.txt", "category", "LOGIN")),

            new Document("""
                DOCUMENT: PAYMENT ISSUES

                Problem: Payment deducted but order not placed

                Possible causes:
                - Payment gateway timeout.
                - Transaction confirmation delay.
                - Communication failure between payment gateway and application.

                Troubleshooting:
                1. Ask the customer for the transaction reference.
                2. Verify the transaction status with the payment gateway.
                3. Check whether the order was created in the application.
                4. Escalate unresolved payment discrepancies to the payment operations team.
                """,
                Map.of("source", "support-knowledge.txt", "category", "PAYMENT")),

            new Document("""
                DOCUMENT: APPLICATION ERRORS

                Problem: Application crashes or displays unexpected errors

                Possible causes:
                - Application server issues.
                - Unexpected application exceptions.
                - Network connectivity problems.
                - Invalid or unexpected input.

                Troubleshooting:
                1. Capture the exact error message.
                2. Record the time when the issue occurred.
                3. Check application logs for exceptions.
                4. Check application monitoring dashboards.
                5. Escalate persistent errors to the technical support team.
                """,
                Map.of("source", "support-knowledge.txt", "category", "APPLICATION"))
        );

        vectorStore.add(documents);

        System.out.println("Knowledge base loaded. Total documents: "
                + documents.size());
    }
}
