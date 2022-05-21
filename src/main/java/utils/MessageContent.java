package utils;

import java.time.Instant;
import java.util.Date;
import java.util.Set;

public class MessageContent {
    private String message;
    private Date date;
    private String sender;

    public MessageContent() {}

    public MessageContent(String message, String sender) {
        this.message = message;
        this.sender = sender;
        date = new Date();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
