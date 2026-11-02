package com.example.aisupportassistant.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.langchain4j.model.ollama.OllamaChatModel;

@Configuration
public class LangChainConfig {
	
	@Bean
   public OllamaChatModel langChain4jOllamaModel() {
	   return  OllamaChatModel.builder().baseUrl("http://localhost:11434").modelName("llama3.2:1b")
               .temperature(0.1)
               .build();
   }
}
