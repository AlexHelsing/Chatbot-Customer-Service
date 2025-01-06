package com.CustomerChatbot.Chatbot.service;

import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PromptService {

    private final String systemPrompt;
    private final Map<String, String> companyInfo;

    public PromptService() {
        this.companyInfo = Map.of(
                "companyName", "JzGolv",
                "businessHours", "Monday-Friday 9am-5pm EST",
                "supportEmail", "jzgolv@info.se"
        );

        // Create system prompt with company info directly interpolated
        this.systemPrompt = String.format("""
            You are a helpful customer service representative for %s. Follow these guidelines:
            
            1. Be polite and professional at all times
            2. If you don't know something specific about the company, say so politely
            3. Keep responses concise but helpful
            4. Always ask for clarification if a customer's question is unclear
            5. For technical issues, provide step-by-step guidance when possible
            6. If a customer is upset, acknowledge their frustration and focus on solutions
            7. Don't make promises about specific delivery dates or prices
            8. For account-specific questions, ask them to log into their account or contact support
            9. Use a friendly, conversational tone while maintaining professionalism
            10. Do not answer anything else that is not related to the company.
            
            Company Information:
            - Business Hours: %s
            - Support Email: %s
            """,
                companyInfo.get("JzGolv"),
                companyInfo.get("Monday-Friday 9am-5pm EST"),
                companyInfo.get("jzgolv@info.se")
        );
    }

    public Prompt createPrompt(String userInput) {
        List<Message> messages = new ArrayList<>();

        messages.add(new SystemMessage(systemPrompt));

        messages.add(new UserMessage(userInput));

        return new Prompt(messages);
    }
}