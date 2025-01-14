package com.CustomerChatbot.Chatbot.controller;

import com.CustomerChatbot.Chatbot.model.User;
import com.CustomerChatbot.Chatbot.repository.UserRepository;
import com.CustomerChatbot.Chatbot.service.EmbeddingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final EmbeddingService embeddingService;

    @Autowired
    public UserController(UserRepository userRepository, EmbeddingService embeddingService) {
        this.userRepository = userRepository;
        this.embeddingService = embeddingService;
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody User user) {
        try {

            String inputText = user.getName() + " " + user.getEmail();
            float[] embedding = embeddingService.generateEmbedding(inputText);
            user.setEmbedding(embedding);

            userRepository.save(user);

            return ResponseEntity.ok("User created successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating user: " + e.getMessage());
        }
    }
}
