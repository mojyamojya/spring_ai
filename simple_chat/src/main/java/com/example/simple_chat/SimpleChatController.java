package com.example.simple_chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai/chat")
public class SimpleChatController {
    
    @Autowired
    private SimpleChatService service;

    @PostMapping
    public String index(@RequestBody String message) {
        return service.sendPrompt(message);
    }
}
