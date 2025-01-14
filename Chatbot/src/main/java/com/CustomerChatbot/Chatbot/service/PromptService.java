package com.CustomerChatbot.Chatbot.service;

import com.CustomerChatbot.Chatbot.model.Order;
import com.CustomerChatbot.Chatbot.repository.OrderRepository;
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
    private final EmbeddingService embeddingService;
    private final OrderRepository orderRepository;

    public PromptService(OrderRepository orderRepository, EmbeddingService embeddingService) {
        this.orderRepository = orderRepository;
        this.embeddingService = embeddingService;

        this.companyInfo = Map.of(
                "companyName", "JzGolv",
                "businessHours", "Monday-Friday 9am-5pm EST",
                "supportEmail", "jzgolv@info.se"
        );

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
                companyInfo.get("companyName"),
                companyInfo.get("businessHours"),
                companyInfo.get("supportEmail")
        );
    }

    public Prompt createPrompt(String userInput) {
        List<Message> messages = new ArrayList<>();

        messages.add(new SystemMessage(systemPrompt));

        messages.add(new UserMessage(userInput));

        return new Prompt(messages);
    }

    public float[] generateUserEmbedding(String userInput) {
        try {
            return embeddingService.generateEmbedding(userInput);
        } catch (Exception e) {
            throw new RuntimeException("Error generating embedding for user input: " + e.getMessage(), e);
        }
    }

    public void addOrder(String orderDetails, String customerName, String customerId, String orderStatus) {
        try {
            float[] embedding = embeddingService.generateEmbedding(orderDetails);

            Order order = new Order();
            order.setCustomerName(customerName);
            order.setOrderStatus(orderStatus);
            order.setEmbedding(embedding);

            orderRepository.save(order);
            System.out.println("Order saved successfully!");
        } catch (Exception e) {
            throw new RuntimeException("Failed to add order: " + e.getMessage(), e);
        }
    }

    private String generateOrderId() {
        return java.util.UUID.randomUUID().toString();
    }
}
