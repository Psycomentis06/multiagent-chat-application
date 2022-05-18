import agent.ChatContainer;
import agent.ChatManager;
import gui.ChatWindow;
import jade.wrapper.StaleProxyException;

public class Main {
    public static void main(String[] args) {
        ChatContainer chatContainer = new ChatContainer();
        try {
            chatContainer.createAgent(ChatManager.class.getName() + "1", ChatManager.class.getName(), new Object[] {});
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
        new ChatWindow();
    }
}
