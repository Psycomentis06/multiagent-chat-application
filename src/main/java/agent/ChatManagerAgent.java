package agent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import utils.JoinedLeftMessage;

import javax.swing.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ChatManagerAgent extends Agent {
    private static Set<String> registeredAgents;
    ObjectMapper objectMapper;

    public ChatManagerAgent() {
        registeredAgents = new HashSet<>();
        objectMapper = new ObjectMapper();
    }

    @Override
    protected void setup() {
        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                // Receive message then send it to it's destination
                ACLMessage aclMessage = receive();

                if (aclMessage != null) {
                    var msgType = aclMessage.getPerformative();
                    switch (msgType) {
                        case ChatACLMessageType.JOINED -> {
                            String agentName = aclMessage.getSender().getName();
                            joinRoom(agentName);
                            try {
                                JoinedLeftMessage joinedLeftMessage = new JoinedLeftMessage(registeredAgents, agentName, true);
                                String msg = objectMapper.writeValueAsString(joinedLeftMessage);
                                defuseMessage(msg, ChatACLMessageType.JOINED);
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                            }
                        }
                        case ChatACLMessageType.LEFT -> {
                            String agentName = aclMessage.getSender().getName();
                            leaveRoom(agentName);
                            try {
                                JoinedLeftMessage joinedLeftMessage = new JoinedLeftMessage(registeredAgents, agentName, false);
                                String msg = objectMapper.writeValueAsString(joinedLeftMessage);
                                defuseMessage(msg, ChatACLMessageType.JOINED);
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                            }
                        }
                        case ChatACLMessageType.MESSAGE -> {
                            String receiverName = "";
                            String msg = aclMessage.getContent();
                            System.out.println("Manager: " + msg);
                            if (msg.startsWith("@")) {
                                receiverName = msg
                                        .split(" ")[0]
                                        .replace('@', ' ')
                                        .trim();
                                sendMessage(receiverName, msg, ChatACLMessageType.MESSAGE);
                            } else {
                                defuseMessage(msg, ChatACLMessageType.MESSAGE);
                            }
                        }
                        default -> {
                        }
                    }
                }
            }
        });
    }

    public void sendMessage(String to, String msg, int msgType) {
        ACLMessage aclMessage = new ACLMessage(msgType);
        if (registeredAgents.contains(to)) {
            aclMessage.addReceiver(new AID(to, AID.ISLOCALNAME));
        }
    }

    public void defuseMessage(String msg, int msgType) {
        for (String agent :
                registeredAgents) {
            ACLMessage message = new ACLMessage(msgType);
            message.addReceiver(new AID(agent, AID.ISGUID));
            message.setContent(msg);
            send(message);
        }
    }

    public void joinRoom(String agentName) {
        registeredAgents.add(agentName);
    }

    public void leaveRoom(String agentName) {
        registeredAgents.remove(agentName);
    }
}
