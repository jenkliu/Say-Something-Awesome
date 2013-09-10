package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.Timer;

import main.Client;
import model.Adapter;
import client.ClientConversation;
import client.ClientMessenger;
import gui.RoomMemberList.MemberAdapter;

public class ChatRoomTab extends Tab {
    /**
     * Tab for a chat conversation
     */
    private static final long serialVersionUID = 1L;
    private final ClientConversation convo;
    private final ClientMessenger cm;
    private final Client client;
    private ChatTabbedPane tabbedPane;
    
    private final JSplitPane chatPanel;
    
    private final JPanel optionsPanel;
    private final JButton addUserBtn;
    private final JLabel selectAUser;
    private final JButton leaveChatBtn;
    
    private final JTextPane convoPane;
    private final JScrollPane convoScrollPane;
    private final JPanel msgPanel;
    private final JTextField msgField;
    private final JButton msgSendBtn;
    
    private final JPanel chatUserPanel;
    private final JScrollPane userScrollPane;
    private final JList userList;
    private String selectedUser;
        
    private Adapter.TypingStatus typingStatus;
    private Timer typingTimer;
    
    public ChatRoomTab (ClientConversation convo){
        
        this.convo = convo;
        cm = convo.getClientMessenger();
        client = convo.getClient();
        tabbedPane = client.getChatGUI().getChatcard().getChatPane();
        
        typingStatus = Adapter.TypingStatus.NO_TEXT;
        typingTimer = getTypingTimer();
        
        convoPane = new JTextPane(convo.getChatDoc());
        convoPane.setName("convoPane");
        convoPane.setEditable(false);
        convoScrollPane = new JScrollPane(convoPane);
        
        msgField = new JTextField();
        msgField.setName("msgField");
        msgField.setFocusable(true);
        msgSendBtn = new JButton("Send");       
        selectAUser = new JLabel("<-- Select a user from your buddy list to add to this chat!");
        addUserBtn = new JButton();
        addUserBtn.setVisible(false);
        leaveChatBtn = new JButton("Leave chat");     

        // options panel (top)
        optionsPanel = new JPanel();
        layoutOptionsPanel();
        addLeaveListener();
        
        // message panel where you type text and enter (bottom)
        msgPanel = new JPanel();
        layoutMsgPanel();
        addMsgListeners();
        
        // central panel (options + chat box + message panel)
        JPanel topOfChatPanel = new JPanel();
        topOfChatPanel.setLayout(new BorderLayout());
        topOfChatPanel.add(optionsPanel, BorderLayout.NORTH);
        topOfChatPanel.add(convoScrollPane, BorderLayout.CENTER);
        chatPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topOfChatPanel, msgPanel);
        chatPanel.setResizeWeight(0.7);
        chatPanel.setDividerLocation(0.8);
        
        // room member typing status panel  (right)      
        chatUserPanel = new JPanel();
        userList = new JList(convo.getListModel());
        userList.setCellRenderer(new RoomMemberCellRenderer(new MemberAdapter()));
        userScrollPane = new JScrollPane(userList);
        layoutUserPanel();
        
        // set up entire layout
        setLayout(new BorderLayout());
        add(chatPanel, BorderLayout.CENTER);
        add(userScrollPane, BorderLayout.EAST);
    }
    
    private void layoutOptionsPanel() {
    	optionsPanel.setLayout(new BorderLayout());  
    	optionsPanel.add(selectAUser, BorderLayout.NORTH);
    	optionsPanel.add(addUserBtn, BorderLayout.WEST);
    	optionsPanel.add(leaveChatBtn, BorderLayout.EAST);
    }

    private void layoutMsgPanel() {        
        msgPanel.setLayout(new BorderLayout());
        msgPanel.add(msgField, BorderLayout.CENTER);
        msgPanel.add(msgSendBtn, BorderLayout.EAST);
    }  
    
    private void layoutUserPanel() {
        chatUserPanel.setLayout(new BorderLayout());
        chatUserPanel.add(new JLabel("In this chat:"), BorderLayout.NORTH);
        chatUserPanel.add(userScrollPane, BorderLayout.CENTER);
    }
    
    private void addLeaveListener() {
    	leaveChatBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doLeaveChat();
            }
        });
    	
    }
    
    private void addMsgListeners() {
        // send message to server when enter key is pressed
        msgField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doSendMessage();
            }
        });
        
        // send message to server when "send" button is pressed
        msgSendBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doSendMessage();
            }
        });
        
        
        msgField.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                // when user types, change status to IS_TYPING if it isn't already
                if (!typingStatus.equals(Adapter.TypingStatus.IS_TYPING) && 
                        msgField.getText().length() != 0) {
                    typingStatus = Adapter.TypingStatus.IS_TYPING;
                    doSendTypingStatus();
                }
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
                // reset typingTimer whenever a key is released
                if (!typingTimer.isRunning()){
                    typingTimer.start();
                }
                else {
                    typingTimer.restart();
                }                
            }
            
            @Override
            public void keyTyped(KeyEvent e) {              
            }

        });
    }
    
    private void doLeaveChat() {
    	convo.getClientMessenger().requestLeave();
    	client.getCurrentConvos().remove(convo.getID());
    	tabbedPane.removeTabAt(tabbedPane.getSelectedIndex());
    }
    
    private void doSendMessage() {
        typingStatus = Adapter.TypingStatus.NO_TEXT;
        String msg = msgField.getText();
        cm.requestSendMessage(msg);
        msgField.setText("");       
    }
    
    private void doSendTypingStatus() {
        cm.requestChangeUserState("isTyping");
    }

    private Timer getTypingTimer() {
        Action updateTypedStatus = new AbstractAction() {

            private static final long serialVersionUID = 1L;
            // do this after 3 seconds have passed with no typing
            public void actionPerformed(ActionEvent e) {
                // if no text in the box, status is NO_TEXT
                if (msgField.getText().length() == 0) {
                    if (!typingStatus.equals(Adapter.TypingStatus.NO_TEXT)) {
                        cm.requestChangeUserState("noText");
                        typingStatus = Adapter.TypingStatus.NO_TEXT;
                    }
                }
                // if there is text in the box, status is HAS_ENTERED_TEXT
                else if (!typingStatus.equals(Adapter.TypingStatus.HAS_ENTERED_TEXT)) {
                    cm.requestChangeUserState("hasEnteredText");
                    typingStatus = Adapter.TypingStatus.HAS_ENTERED_TEXT;
                }
            }
        };
        return new Timer(3000, updateTypedStatus);
    }
    
    /**
     * Set the user that is currently selected in the buddy list
     * 
     * @param username of user currently selected in buddy list
     */
    public void setSelectedUser(String user) {
        this.selectedUser = user;
        isUserSelected = true;
        addUserBtn.setText("Add "+user+" to this chat");
        // remove old action listeners for "Add User" button
        for (ActionListener al : addUserBtn.getActionListeners()) {
            addUserBtn.removeActionListener(al);
        }
        // add new action listener for "Add User" button
        addUserBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // if this user is already in the chat, create a pop up dialog warning
                if (convo.containsUser(selectedUser)) {
                	String error = selectedUser + " is already in this chat!";
                	JOptionPane.showMessageDialog(new JFrame(),
                            error,
                            "Add user error",
                            JOptionPane.WARNING_MESSAGE);
                	return;
                }
            	doAddToChat(selectedUser);
            }
        });
        addUserBtn.setVisible(true);
        selectAUser.setVisible(false);
    }
    
    /**
     * Hide the "Add user button" (for when no user is selected on buddy list)
     */
    public void setNoUserSelected() {
        isUserSelected = false;
        addUserBtn.setVisible(false);
        selectAUser.setVisible(true);
    }
    
    private void doAddToChat(String user) {
        cm.requestAddUser(user);
    }
    
    /**
     * Get the conversation associated with this tab. 
     * @return
     */
    public ClientConversation getConvo() {
        return convo;
    }
}
