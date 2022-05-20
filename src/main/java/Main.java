import agent.ChatClientAgent;
import agent.ChatContainer;
import agent.ChatContainerSingleton;
import agent.ChatManagerAgent;
import jade.wrapper.ControllerException;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
       ChatContainer chatContainer = ChatContainerSingleton.getContainer();
        try {
            chatContainer.createAgent(ChatManagerAgent.class.getName(), ChatManagerAgent.class.getName(), new Object[]{});
            String agentName = JOptionPane.showInputDialog(null, "Agent name: ", "ClientAgent#" + Math.round(Math.random() * 9999));
            ChatContainerSingleton.getContainer().createAgent(agentName, ChatClientAgent.class.getName(), new Object[]{});
        } catch (ControllerException e) {
            e.printStackTrace();
        }
    }
}
