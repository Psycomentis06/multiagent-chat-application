package agent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gui.ChatWindow;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import utils.JoinedLeftMessage;
import utils.MessageContent;

import java.util.Vector;
import java.util.stream.Collectors;

public class ChatClientAgent extends GuiAgent {

    transient protected ChatWindow chatWindow;

    ObjectMapper objectMapper;

    public ChatClientAgent() {
        objectMapper = new ObjectMapper();
    }

    @Override
    protected void setup() {
        chatWindow = new ChatWindow(this);

        addBehaviour(new OneShotBehaviour(this) {
            @Override
            public void action() {
                sendMessage(getName() + " joined the chat", ChatACLMessageType.JOINED);
            }
        });

        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                ACLMessage aclMessage = receive();
                if (aclMessage != null) {
                    switch (aclMessage.getPerformative()) {
                        case ChatACLMessageType.JOINED, ChatACLMessageType.LEFT -> {
                            try {
                                JoinedLeftMessage joinedLeftMessage = objectMapper.readValue(aclMessage.getContent(), JoinedLeftMessage.class);
                                chatWindow.agentConnectedMessage(joinedLeftMessage.getMessage());
                                //chatWindow.setConnectedAgents(new Vector<>(joinedLeftMessage.getAgents()));
                                chatWindow.setConnectedAgents(
                                        joinedLeftMessage
                                                .getAgents()
                                                .stream()
                                                .map(joinedLeftMessage::senderNameOnly)
                                                .collect(Collectors.toCollection(Vector::new))
                                );
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                            }
                        }
                        case ChatACLMessageType.MESSAGE -> {
                            try {
                                MessageContent messageContent = objectMapper.readValue(aclMessage.getContent(), MessageContent.class);
                                chatWindow.addMessage(messageContent);
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
    }

    public void sendMessage(String msg, int msgType) {
        ACLMessage aclMessage = new ACLMessage(msgType);
        aclMessage.addReceiver(new AID(ChatManagerAgent.class.getName(), AID.ISLOCALNAME));
        aclMessage.setContent(msg);
        send(aclMessage);
    }

    @Override
    protected void onGuiEvent(GuiEvent guiEvent) {
        switch (guiEvent.getType()) {
            case EventType.DISCONNECT -> {
                sendMessage("Agent " + getName() + " Left chat", ChatACLMessageType.LEFT);
            }
            case EventType.SEND_MSG -> {
                sendMessage((String) guiEvent.getParameter(0), ChatACLMessageType.MESSAGE);
            }
        }
    }
}
