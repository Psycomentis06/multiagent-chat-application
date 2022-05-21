package utils;

import java.util.Date;
import java.util.Set;

public class JoinedLeftMessage {
    private String message;
    private Date date;
    private String sender;
    private Set<String> agents;

    public JoinedLeftMessage() {}

    public JoinedLeftMessage(Set<String> agents, String sender, boolean joined) {
        if (joined) message = senderNameOnly(sender) + " Joined the chat";
        else message = senderNameOnly(sender) + " Left the chat";
        this.sender = sender;
        this.agents = agents;
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

    public String senderNameOnly(String sender) {
        return sender.split("@")[0];
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Set<String> getAgents() {
        return agents;
    }

    public void setAgents(Set<String> agents) {
        this.agents = agents;
    }
}
