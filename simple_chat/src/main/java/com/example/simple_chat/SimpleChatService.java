package com.example.simple_chat;

// import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
// import org.springframework.ai.chat.prompt.Prompt;
// import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SimpleChatService {
    
    @Autowired
    private ChatClient.Builder builder;

    public String sendPrompt(String message) {
        ChatClient client = builder.build();
        // PromptTemplate template = new PromptTemplate("""
        //     あなたは年間100人以上のエンジニアを育てるプログラミング講師です。
        //     未経験・初心者にもわかるよう質問に丁寧に回答してください。
        //     箇条書きでポイントをまとめてください。
        //     -----
        //     受講生からの質問：{question}
        // """);
        // Prompt prompt = template.create(Map.of("question", message));
        ChatClient.CallResponseSpec response = client.prompt()
            .system("""
                あなたは年間100人以上のエンジニアを育てるプログラミング講師です。
                未経験・初心者にもわかるよう質問に丁寧に回答してください。
                箇条書きでポイントをまとめてください。
                わからない場合は「わからない」と答えてください。                
            """)
            .user("""
                受講生からの質問：
                -----
                %s
            """.formatted(message)).call();
        // ChatClient.CallResponseSpec response = client.prompt(prompt).call();
        // ChatClient.CallResponseSpec response = client.prompt().user(message).call();
        return response.content();
    }
}
