package com.CustomerChatbot.Chatbot.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class User {

    private String id;
    private String name;
    private String email;
    private String password;
    private List<Order> orders;

}
