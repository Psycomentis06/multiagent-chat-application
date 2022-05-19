package agent;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class ChatManagerAgent extends Agent {
    private static Vector<ACLMessage> chatMessages;
    private static Set<String> registeredAgents;

    public ChatManagerAgent() {
        chatMessages = new Vector<>(5);
        registeredAgents = new HashSet<>();
    }

    @Override
    protected void setup() {
        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                // Receive message then send it to it's destination
                ACLMessage aclMessage = receive();
                if (aclMessage != null) {
                    String receiverName = "";
                    String msg = aclMessage.getContent();
                    System.out.println("Manager: " + msg);
                    if (msg.startsWith("@")) {
                        receiverName = msg
                                .split(" ")[0]
                                .replace('@', ' ')
                                .trim();
                        sendMessage(receiverName, msg);
                    } else {
                        defuseMessage(msg);
                    }
                }
            }
        });
    }

    public void sendMessage(String to, String msg) {
        ACLMessage aclMessage = new ACLMessage(ACLMessage.INFORM);
        if (registeredAgents.contains(to)) {
            aclMessage.addReceiver(new AID(to, AID.ISLOCALNAME));
        }
    }

    public void defuseMessage(String msg) {
        for (String agent :
                registeredAgents) {
            ACLMessage message = new ACLMessage(ACLMessage.INFORM);
            message.addReceiver(new AID(agent, AID.ISLOCALNAME));
            message.setContent(msg);
            send(message);
        }
    }
}
