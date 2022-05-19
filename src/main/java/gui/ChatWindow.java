package gui;

import agent.ChatClientAgent;
import agent.EventType;
import com.formdev.flatlaf.FlatLightLaf;
import jade.gui.GuiEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatWindow extends JFrame {

    ChatClientAgent chatClientAgent;
    public ChatWindow(ChatClientAgent chatClientAgent) {
        this.chatClientAgent = chatClientAgent;
        FlatLightLaf.setup();
        //FlatDarculaLaf.setup();
        setup();
    }

    public void setup() {
        setTitle("Agents chat");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setJMenuBar(createJMenuBar());
        add(createChatContent());
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public JMenuBar createJMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu agentMenu = new JMenu("Agent", false);

        JMenuItem newAgentMenuItem = new JMenuItem("New");
        agentMenu.add(newAgentMenuItem);
        JMenuItem disconnectAgentMenuItem = new JMenuItem("Disconnect");
        agentMenu.add(disconnectAgentMenuItem);
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        agentMenu.add(exitMenuItem);

        menuBar.add(agentMenu);
        return menuBar;
    }

    public JPanel createChatContent() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel northPanel = new JPanel(new BorderLayout());
        JPanel southPanel = new JPanel(new BorderLayout());

        // North
        JPanel messagesPanel = new JPanel();
        messagesPanel.setLayout(new GridBagLayout());
        GridBagConstraints messagesPanelBagConstraints = new GridBagConstraints();
        messagesPanelBagConstraints.fill = GridBagConstraints.BOTH;
        messagesPanelBagConstraints.ipady = 40;      //make this component tall
        messagesPanelBagConstraints.weightx = 1;
        messagesPanelBagConstraints.weighty = 1;
        messagesPanelBagConstraints.insets = new Insets(0, 20, 0, 20);

        for (int i = 0; i < 30; i++) {
            JLabel jLabel = new JLabel(
                    """
                    <html>
                        <p>
                            #######<br>
                            Label N°%d<br>
                            #######
                         </p>
                    </html>
                    """.formatted(i));
            jLabel.setOpaque(true);
            jLabel.setHorizontalTextPosition(JLabel.LEFT);
            JPanel labelPane = new JPanel(new BorderLayout());

            if (i % 2 == 0) {
                jLabel.setHorizontalAlignment(JLabel.RIGHT);
                //jLabel.setBackground(new Color(71, 71, 135));
                labelPane.add(jLabel, BorderLayout.EAST);
            } else {
                jLabel.setHorizontalAlignment(JLabel.LEFT);
                //jLabel.setBackground(new Color(44, 44, 84));
                labelPane.add(jLabel, BorderLayout.WEST);
            }
            messagesPanelBagConstraints.gridy = (i + 1);
            messagesPanel.add(jLabel, messagesPanelBagConstraints);

        }
        JScrollPane messagesScrollPane = new JScrollPane(messagesPanel);
        messagesScrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                if (e.getAdjustable().getValue() == 0)
                    e.getAdjustable().setValue(e.getAdjustable().getMaximum());
            }
        });

        northPanel.add(messagesScrollPane, BorderLayout.CENTER);


        // South
        JButton button = new JButton("Send");
        JTextField chatInput = new JTextField();
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


        mainPanel.add(northPanel, BorderLayout.CENTER);
        mainPanel.add(southPanel, BorderLayout.SOUTH);

        return mainPanel;
    }
}
