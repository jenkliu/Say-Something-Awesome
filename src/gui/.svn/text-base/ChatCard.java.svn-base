package gui;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import main.Client;

public class ChatCard extends JPanel{
    private static final long serialVersionUID = 1L;
    private final JPanel buddyListPanel = new JPanel();   
    private final HomeTab hometab = new HomeTab();
    private final BuddyList buddyList = new BuddyList(hometab);    
    private final ChatTabbedPane chatPane = new ChatTabbedPane();
    private Client client;
    
    private String[] buddiesOnline = new String[] {};
    
    public ChatCard() { 
        layoutBuddyListPanel();
        layoutChatPanel();
        
        setLayout(new BorderLayout());
        JSplitPane splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buddyListPanel, chatPane);
        add(splitpane);
    }
    
    private void layoutBuddyListPanel() {
        buddyListPanel.setLayout(new BoxLayout(buddyListPanel, BoxLayout.Y_AXIS));
        buddyListPanel.add(new JLabel("Buddy List"));
        
        JScrollPane scrollPane = new JScrollPane(buddyList);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        buddyListPanel.add(scrollPane);
    }
    
    private void layoutChatPanel (){
        chatPane.addTab("Home", hometab);
    }

    public String[] getBuddiesOnline() {
        return buddiesOnline;
    }

    public BuddyList getBuddyList(){
        return buddyList;
    }
    
    public void setClient(Client client){
        this.client = client;
        hometab.setClient(this.client);
        buddyList.setClient(this.client);
    }
    
    public ChatTabbedPane getChatPane() {
        return chatPane;
    }
    

}
