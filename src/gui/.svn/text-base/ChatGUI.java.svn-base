package gui;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;

import javax.swing.JFrame;

import main.Client;

public class ChatGUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private final Container contentPane = this.getContentPane();
    private final StartCard startcard = new StartCard(this);
    private final ChatCard chatcard = new ChatCard();
    
    private final CardLayout layout = new CardLayout();
    private Client myclient;
    
    
    /**
     * The user interface for the Say Something Awesome chat client.
     * 
     * This class is the main GUI class.
     */
    public ChatGUI (){
        setTitle("Say Something Awesome");
        contentPane.setLayout(layout);
        contentPane.add(startcard, "Start Card");
        contentPane.add(chatcard, "Chat Card");
         
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }
    
    public static void makeNonResizeable(Component component){
        component.setMinimumSize(component.getPreferredSize());
        component.setMaximumSize(component.getPreferredSize());
    }
    
    public CardLayout getLayout() {
        return layout;
    }
    
    public ChatCard getChatcard() {
        return chatcard;
    }
    
    public void setFinalClient(Client client){
        myclient = client;
        chatcard.setClient(client);
        myclient.requestUserList();
    }
}
