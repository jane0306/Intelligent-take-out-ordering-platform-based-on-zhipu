package com.example.delivery.data;

public class Message {
    private String sender; // 发送者（“你”或“AI”）
    private String content; // 消息内容

    public Message(String sender, String content) {
        this.sender = sender;
        this.content = content;
    }

    // Getter
    public String getSender() { return sender; }
    public String getContent() { return content; }
}