package edu.itson.jackMurrieta.model;

public class Message {
    public String from;
    public String to; // null = broadcast
    public String content;
    public MessageType type;

    public Message() {
    }

    public Message(String from, String content, MessageType type) {
        this.from = from;
        this.content = content;
        this.type = type;
    }
}
