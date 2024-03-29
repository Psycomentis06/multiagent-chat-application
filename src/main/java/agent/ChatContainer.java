package agent;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;

public class ChatContainer {
    AgentContainer container;
    public ChatContainer() {
        Runtime rt = Runtime.instance();
        Properties p = new ExtendedProperties();
        //p.setProperty(Profile.AGENTS, "true");
        //p.setProperty(Profile.GUI, "true");
        ProfileImpl pc = new ProfileImpl(p);
        container = rt.createMainContainer(pc);
        try {
            container.start();
        } catch (ControllerException e) {
            e.printStackTrace();
        }
    }

    public void createAgent(String nickname, String className, Object[] args) throws StaleProxyException {
        AgentController a = container.createNewAgent(nickname, className, args);
        a.start();
    }

    public AgentContainer getContainer() {
        return container;
    }
}
