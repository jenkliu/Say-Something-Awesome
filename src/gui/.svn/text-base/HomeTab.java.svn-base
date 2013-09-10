package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import main.Client;

/**
 * Home tab of the chat client.
 *
 */
public class HomeTab extends Tab {
    private static final long serialVersionUID = 1L;
    
    private JLabel startChatLabel = new JLabel();
    private final JTextField startChatArea = new JTextField(35);
    private final JButton startChatBtn = new JButton("SEND");
    private String selectedUser;
    private Client client;
    
    public HomeTab() {
        //startChatArea.setPreferredSize(new Dimension(300,100));
        this.setLayout(new GridBagLayout());
        
        GridBagConstraints c1 = new GridBagConstraints();
        c1.gridx = 0;
        c1.gridy = 0;
        c1.gridwidth = 3;
        c1.anchor = GridBagConstraints.BASELINE_LEADING;
        this.add(startChatLabel, c1);
        
        GridBagConstraints c3 = new GridBagConstraints();
        c3.gridx = 0;
        c3.gridy = 1;
        c3.gridwidth = 3;
        c3.anchor = GridBagConstraints.BASELINE;
        this.add(startChatArea, c3);
        
        GridBagConstraints c4 = new GridBagConstraints();
        c4.gridx = 3;
        c4.gridy = 2;
        c4.anchor = GridBagConstraints.BASELINE_TRAILING;
        this.add(startChatBtn, c4);           
        
    }
    
    /**
     * Sets the client for the tab (to be called after connection with server
     * has been established)
     * @param client
     */
    public void setClient(Client client) {
        this.client = client; 
        // Send a createConvo request to the server when "SEND" button is pressed
        startChatBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doCreateConvo();
            }
        });
        
        // Send a createConvo request to the server when enter is pressed from textfield
        startChatArea.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doCreateConvo();
            }
        });
        setNoUserSelected();
    }
    
    /**
     * Set to user that is currently selected in buddy list
     * 
     * @param user that is currently selected in buddy list
     */
    public void setSelectedUser(String user) {
        System.out.println(user+" is selected");
        // show start btn if user wasn't previously selected
        if (!isUserSelected) {
            isUserSelected = true;
            startChatBtn.setVisible(true);
            startChatArea.setVisible(true);
        }
        selectedUser = user;
        
        startChatLabel.setText("Welcome "+client.getUsername()+"! Say something awesome to "+selectedUser+":");           
    }
    
    /**
     * Remove "Remove text field when no user is selected on buddy list"
     */
    public void setNoUserSelected() {
        isUserSelected = false;
        startChatBtn.setVisible(false);
        startChatArea.setVisible(false);
        startChatLabel.setText("Welcome "+client.getUsername()+"! Select a user to chat with!");
        
    }
    
    /**
     * Sends request to the server to create a new conversation.
     */
    public void doCreateConvo() {
        String msg = startChatArea.getText();
        client.requestCreateChat(selectedUser, msg);
        startChatArea.setText("");
    }
    
    /** 
     * Get the client for this tab.
     * @return client
     */
    public Client getClient() {
        if (client != null) {
            return this.client;
        }
        throw new RuntimeException("Error: Client has not yet been instantiated");
    }
}
