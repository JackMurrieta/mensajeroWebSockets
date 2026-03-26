package valdez.alejandro.entidades;

import valdez.alejandro.enums.MessageType;

/**
 *
 * @author janot
 */
public class Message {
    private String from;
    private String to; // null = broadcast
    private String content;
    private MessageType type;

    public Message(String from, String to, String content, MessageType type) {
        this.from = from;
        this.to = to;
        this.content = content;
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getContent() {
        return content;
    }

    public MessageType getType() {
        return type;
    }
    
}
