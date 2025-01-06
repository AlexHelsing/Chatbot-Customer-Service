package com.CustomerChatbot.Chatbot;

import org.springframework.boot.SpringApplication;

public class TestChatbotApplication {

	public static void main(String[] args) {
		SpringApplication.from(ChatbotApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
