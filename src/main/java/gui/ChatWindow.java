package gui;

import agent.ChatClientAgent;
import agent.ChatContainerSingleton;
import agent.EventType;
import com.formdev.flatlaf.FlatLightLaf;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.wrapper.StaleProxyException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class ChatWindow extends JFrame {

    // Reference to ChatWindow class
    private ChatWindow chatWindowRef;

    // Messages Panel
    private JPanel messagesPanel;

    private int messagePosition;

    // Grid Panel constrains
    private GridBagConstraints messagesPanelBagConstraints;

    // Connected Agents list
    private Vector<String> connectedAgents;

    private JPanel leftPanel;

    // Connected Agents title bar label
    private JLabel connectedAgentsTitleBarLabel;

    private JList<String> joinedAgents;

    private JTextField chatInput;

    private JPanel mainPanel;

    private ChatClientAgent chatClientAgent;

    public ChatWindow(ChatClientAgent chatClientAgent) {
        chatWindowRef = this;
        this.chatClientAgent = chatClientAgent;

        mainPanel = new JPanel();
        leftPanel= new JPanel();
        messagesPanel = new JPanel();
        messagePosition = 0;
        connectedAgents = new Vector<>();
        joinedAgents = new JList<>();
        chatInput = new JTextField();
        messagesPanelBagConstraints = new GridBagConstraints();
        connectedAgentsTitleBarLabel = new JLabel("Connected Agents (" + connectedAgents.size() + ")");
        setup();
    }

    public void setup() {
        setTitle(chatClientAgent.getName());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setJMenuBar(createJMenuBar());
        add(createChatContent());
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public JMenuBar createJMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu newAgentMenu = new JMenu("New Agent");
        newAgentMenu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {

                try {
                    String agentName = JOptionPane.showInputDialog(chatWindowRef, "Agent name: ", "ClientAgent#" + Math.round(Math.random() * 9999));
                    ChatContainerSingleton.getContainer().createAgent(agentName, ChatClientAgent.class.getName(), new Object[]{});
                } catch (StaleProxyException ex) {
                    JOptionPane.showMessageDialog(chatWindowRef, ex.getMessage());
                }
            }

            @Override
            public void menuDeselected(MenuEvent e) {

            }

            @Override
            public void menuCanceled(MenuEvent e) {

            }
        });
        JMenu disconnectAgent = new JMenu("Disconnect");
        disconnectAgent.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                GuiEvent event = new GuiEvent(this, EventType.DISCONNECT);
                chatClientAgent.postGuiEvent(event);
                dispatchEvent(new WindowEvent(chatWindowRef, WindowEvent.WINDOW_CLOSING));
                chatClientAgent.doDelete();
            }

            @Override
            public void menuDeselected(MenuEvent e) {

            }

            @Override
            public void menuCanceled(MenuEvent e) {

            }
        });

        JMenu exit = new JMenu("Exit");
        exit.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                System.exit(0);
            }

            @Override
            public void menuDeselected(MenuEvent e) {

            }

            @Override
            public void menuCanceled(MenuEvent e) {

            }
        });

        menuBar.add(newAgentMenu);
        menuBar.add(disconnectAgent);
        menuBar.add(exit);
        return menuBar;
    }

    public JPanel createChatContent() {
        mainPanel = new JPanel(new BorderLayout());
        JPanel centerPanel = new JPanel(new BorderLayout());
        JPanel southPanel = new JPanel(new BorderLayout());
        leftPanel = new JPanel(new BorderLayout());

        // Center
        messagesPanel.setLayout(new GridBagLayout());
        messagesPanelBagConstraints = new GridBagConstraints();
        messagesPanelBagConstraints.fill = GridBagConstraints.BOTH;
        messagesPanelBagConstraints.ipady = 40;      //make this component tall
        messagesPanelBagConstraints.weightx = 1;
        messagesPanelBagConstraints.weighty = 1;
        messagesPanelBagConstraints.insets = new Insets(0, 20, 0, 20);


        JScrollPane messagesScrollPane = new JScrollPane(messagesPanel);
        messagesScrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                if (e.getAdjustable().getValue() == 0)
                    e.getAdjustable().setValue(e.getAdjustable().getMaximum());
            }
        });

        centerPanel.add(messagesScrollPane, BorderLayout.CENTER);

        // Left

        JScrollPane joinedAgentsScrollPane = new JScrollPane(joinedAgents);
        connectedAgentsJList();

        connectedAgentsTitleBarLabel.setText("Connected Agents (" + connectedAgents.size() + ")");
        connectedAgentsTitleBarLabel.setFont(connectedAgentsTitleBarLabel.getFont().deriveFont(20f));
        connectedAgentsTitleBarLabel.setBorder(new EmptyBorder(0, 5, 0, 5));
        leftPanel.add(connectedAgentsTitleBarLabel, BorderLayout.NORTH);
        leftPanel.add(joinedAgentsScrollPane, BorderLayout.CENTER);

        // South
        JButton button = new JButton("Send");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GuiEvent event = new GuiEvent(this, EventType.SEND_MSG);
                chatClientAgent.postGuiEvent(event);
                chatInput.setText("");
            }
        });


        southPanel.add(chatInput, BorderLayout.CENTER);
        southPanel.add(button, BorderLayout.EAST);


        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(southPanel, BorderLayout.SOUTH);
        mainPanel.add(leftPanel, BorderLayout.WEST);

        return mainPanel;
    }

    public void updateConnectedAgentsTitle() {
        connectedAgentsTitleBarLabel.setText("Connected Agents (" + connectedAgents.size() + ")");
    }

    public void connectedAgentsJList() {
        JList<String> joinedAgents = new JList<>(connectedAgents);
        joinedAgents.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                String chatInputContent = chatInput.getText();
                String toAgent = "";
                if (chatInputContent != null && chatInputContent.startsWith("@")) {
                    toAgent = chatInputContent.trim().split(" ")[0];
                    chatInputContent = chatInputContent.replace(toAgent, "@" + joinedAgents.getSelectedValue());
                } else {
                    chatInputContent = "@" + joinedAgents.getSelectedValue() + chatInputContent;
                }
                chatInput.setText(chatInputContent);
            }
        });
        JScrollPane joinedAgentsScrollPane = new JScrollPane(joinedAgents);
        leftPanel.add(joinedAgentsScrollPane, 0);
    }

    public void setConnectedAgents(Vector<String> connectedAgents) {
        this.connectedAgents = connectedAgents;
        joinedAgents.setListData(connectedAgents);
        updateConnectedAgentsTitle();
        connectedAgentsJList();
        revalidate();
    }

    public void removeDisconnectedAgent(String name) {
        connectedAgents.removeElement(name);
    }

    public void addMessage(ACLMessage msg) {
        JLabel jLabel = new JLabel(msg.getContent());
        jLabel.setOpaque(true);
        jLabel.setHorizontalTextPosition(JLabel.LEFT);
        JPanel labelPane = new JPanel(new BorderLayout());

        if (chatClientAgent.getName().equals(msg.getSender().getName())) {
            jLabel.setHorizontalAlignment(JLabel.RIGHT);
            //jLabel.setBackground(new Color(71, 71, 135));
            labelPane.add(jLabel, BorderLayout.EAST);
        } else {
            jLabel.setHorizontalAlignment(JLabel.LEFT);
            //jLabel.setBackground(new Color(44, 44, 84));
            labelPane.add(jLabel, BorderLayout.WEST);
        }
        messagesPanelBagConstraints.gridy = messagePosition;
        messagePosition++;
        messagesPanel.add(jLabel, messagesPanelBagConstraints);
        revalidate();
    }

    public void agentConnectedMessage(String msg) {
        JLabel jLabel = new JLabel(msg);
        jLabel.setOpaque(true);
        jLabel.setHorizontalTextPosition(JLabel.CENTER);
        jLabel.setForeground(new Color(127, 140, 141));
        JPanel labelPane = new JPanel(new BorderLayout());
        labelPane.add(jLabel, BorderLayout.CENTER);
        messagesPanelBagConstraints.gridy = messagePosition;
        messagePosition++;
        messagesPanel.add(jLabel, messagesPanelBagConstraints);
        mainPanel.revalidate(); // Refresh GUI for latest updates
    }
}
