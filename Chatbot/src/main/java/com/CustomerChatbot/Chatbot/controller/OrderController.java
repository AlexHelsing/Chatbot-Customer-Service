package com.CustomerChatbot.Chatbot.controller;

import com.CustomerChatbot.Chatbot.model.Order;
import com.CustomerChatbot.Chatbot.model.User;
import com.CustomerChatbot.Chatbot.repository.OrderRepository;
import com.CustomerChatbot.Chatbot.repository.UserRepository;
import com.CustomerChatbot.Chatbot.service.EmbeddingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final EmbeddingService embeddingService;

    @Autowired
    public OrderController(OrderRepository orderRepository, UserRepository userRepository, EmbeddingService embeddingService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.embeddingService = embeddingService;
    }

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody Order order) {
        try {
            UUID userId = order.getCustomerId();
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

            order.setUser(user);

            String inputText = order.getCustomerName() + " " + order.getOrderStatus();
            float[] embedding = embeddingService.generateEmbedding(inputText);
            order.setEmbedding(embedding);

            orderRepository.save(order);

            return ResponseEntity.ok("Order created successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating order: " + e.getMessage());
        }
    }



}
