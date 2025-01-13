package com.CustomerChatbot.Chatbot.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChatMessage {
    // Getters and Setters
    private String text;
    private String timestamp;
    private String sender;

}
