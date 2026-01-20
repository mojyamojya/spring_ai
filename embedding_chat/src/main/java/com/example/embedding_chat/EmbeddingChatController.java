package com.example.embedding_chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/embedding/chat")
public class EmbeddingChatController {
    
    @Autowired
    private EmbeddingChatService service;

    @PostMapping
    public String index(@RequestBody String message) {
        return service.sendPrompt(message);
    }
}
