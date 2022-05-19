import agent.ChatClientAgent;
import agent.ChatContainer;
import agent.ChatManagerAgent;
import jade.wrapper.ControllerException;

public class Main {
    public static void main(String[] args) {
       ChatContainer chatContainer = new ChatContainer();
        try {
            chatContainer.createAgent(ChatManagerAgent.class.getName(), ChatManagerAgent.class.getName(), new Object[]{});
            chatContainer.createAgent(ChatClientAgent.class.getName(), ChatClientAgent.class.getName(), new Object[]{});
        } catch (ControllerException e) {
            e.printStackTrace();
        }
    }
}
