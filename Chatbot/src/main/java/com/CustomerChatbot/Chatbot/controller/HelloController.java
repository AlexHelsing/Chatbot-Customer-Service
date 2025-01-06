package com.CustomerChatbot.Chatbot.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {

    @RequestMapping("HelloController")
    public String hello(){
        return "Hello Controller";
    }




}
