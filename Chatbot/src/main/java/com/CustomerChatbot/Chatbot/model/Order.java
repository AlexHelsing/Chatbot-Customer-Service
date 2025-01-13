package com.CustomerChatbot.Chatbot.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Order {

    private String orderId;
    private String customerId;
    private String customerName;
    private String orderStatus;
    private String orderDate;
    private String orderTime;

}
