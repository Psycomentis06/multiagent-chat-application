package agent;

public class ChatContainerSingleton {
    private static ChatContainer container;

    public static  ChatContainer getContainer() {
        if (container == null) {
            container = new ChatContainer();
        }
        return container;
    }
}
