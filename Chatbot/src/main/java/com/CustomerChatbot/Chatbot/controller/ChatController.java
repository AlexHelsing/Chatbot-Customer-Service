package com.CustomerChatbot.Chatbot.controller;

import com.CustomerChatbot.Chatbot.model.ChatMessage;
import com.CustomerChatbot.Chatbot.service.PromptService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import java.util.Map;

@Controller
public class ChatController {

    private final OllamaChatModel chatModel;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PromptService promptService;

    public ChatController(OllamaChatModel chatModel, ObjectMapper objectMapper, PromptService promptService) {
        this.chatModel = chatModel;
        this.promptService = promptService;
    }

    @MessageMapping("/ai/generate")
    @SendTo("/topic/messages")
    public ChatMessage generate(String message) {
        try {
            ChatMessage incomingMessage = objectMapper.readValue(message, ChatMessage.class);
            String userText = incomingMessage.getText();
            System.out.println("user input text " + userText);

            UserMessage userMessage = new UserMessage(userText);

            Prompt prompt = promptService.createPrompt(userText);

            ChatResponse response = this.chatModel.call(prompt);

            String responseText = response.getResult().getOutput().getContent();

            System.out.println(responseText);

            ChatMessage botReply = new ChatMessage();
            botReply.setText(responseText);
            botReply.setSender("bot");
            botReply.setTimestamp(java.time.Instant.now().toString());

            return botReply;

        } catch (Exception e) {
            e.printStackTrace();
            ChatMessage errorReply = new ChatMessage();
            errorReply.setText("An error occurred while processing your message.");
            errorReply.setSender("bot");
            errorReply.setTimestamp(java.time.Instant.now().toString());
            return errorReply;
        }
    }
}
