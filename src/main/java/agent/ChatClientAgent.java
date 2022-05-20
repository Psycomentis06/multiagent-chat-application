package agent;

import gui.ChatWindow;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;

public class ChatClientAgent extends GuiAgent {

    transient protected ChatWindow chatWindow;

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
                        case ChatACLMessageType.JOINED -> {
                            chatWindow.agentConnectedMessage(aclMessage);
                        }
                        case ChatACLMessageType.LEFT -> {
                            chatWindow.agentConnectedMessage(aclMessage);
                        }
                        case ChatACLMessageType.MESSAGE -> {

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
        System.out.println("Gui Event passed");
    }
}
