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

        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                ACLMessage aclMessage = new ACLMessage(ACLMessage.SUBSCRIBE);
                aclMessage.addReceiver(new AID(ChatManagerAgent.class.getName(), AID.ISLOCALNAME));
                aclMessage.setContent(getName() + " joined the chat");
                send(aclMessage);
            }
        });

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {

            }
        });
    }

    public void sendMessage(String msg) {
        System.out.println("Works");
        ACLMessage aclMessage = new ACLMessage(ACLMessage.SUBSCRIBE);
        aclMessage.addReceiver(new AID(ChatManagerAgent.class.getName(), AID.ISLOCALNAME));
        aclMessage.setContent(msg);
        send(aclMessage);
    }

    @Override
    protected void onGuiEvent(GuiEvent guiEvent) {
        System.out.println("Gui Event passed");
    }
}
