package com.example.embedding_chat;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class EmbeddingChatService {
    
    @Value("${app.config.class-path-resource}")
    private String resourcePath;
    private final ChatClient client;
    private final VectorStore store;

    public EmbeddingChatService(ChatClient client, VectorStore store) {
        this.client = client;
        this.store = store;
    }

    @PostConstruct
    public void initVectorStore() {
        // 外部データソースのリソースを取得
        Resource resource = new ClassPathResource(resourcePath);

        // PDFをページごとに分割
        List<Document> documents = convertDocuments(resource);

        // ベクトルストアに登録
        store.add(documents);
    }

    private List<Document> convertDocuments(Resource pdfResource) {
        PdfDocumentReaderConfig config = PdfDocumentReaderConfig.builder()
            .withPagesPerDocument(1)
            .build();

        PagePdfDocumentReader reader = new PagePdfDocumentReader(pdfResource, config);
        List<Document> docs = reader.read();

        return docs;
    }

    public String sendPrompt(String message) {
        // ベクトルストアから類似ドキュメントを検索
        List<Document> similarDocs = store.similaritySearch(message);

        // 取得したドキュメントを1つのテキストにまとめる
        String context = buildContext(similarDocs);

        // ChatClientに「コンテキスト」と「質問」を投げて回答を生成
        String response = client.prompt()
            .system("""
                あなたはJavaプログラミング参考書のPDFドキュメントに基づいて回答するアシスタントです。
                回答は日本語で、わかりやすく簡潔に答えてください。
                わからない場合は「わかりません」と答えてください。
            """)
            .user("""
                以下は参照用のドキュメントの内容です。
                -----
                %s
                -----
                このドキュメントに基づいて、次の質問に答えてください。
                質問:%s
            """.formatted(context, message))
            .call()
            .content();

        return response;
    }

    private String buildContext(List<Document> documents) {
        if (documents == null || documents.isEmpty()) {
            return "関連するドキュメントは見つかりませんでした";
        }

        return documents.stream()
            .limit(5)
            .map(doc -> {
                String content = doc.getText();
                if (content == null) {
                    content = "";
                }
                return content;
            })
            .collect(Collectors.joining("\n\n"));
    }
}
