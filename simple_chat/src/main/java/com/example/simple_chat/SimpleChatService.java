package com.example.simple_chat;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SimpleChatService {
    
    @Autowired
    private ChatClient.Builder builder;

    public String sendPrompt(String message) {
        ChatClient client = builder.build();
        ChatClient.CallResponseSpec response = client.prompt().user(message).call();
        return response.content();
    }
}
