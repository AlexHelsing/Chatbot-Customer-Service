package com.CustomerChatbot.Chatbot.repository;

import com.CustomerChatbot.Chatbot.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    // JpaRepository provides built-in methods like save, findById, findAll, deleteById, etc.
}
