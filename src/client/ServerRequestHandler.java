package client;

import gui.BuddyList;
import gui.ChatGUI;
import gui.ChatRoomTab;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import main.Client;
import model.Adapter;
import model.ChatMessage;
import model.UpdateListWorker;
import model.UpdateListWorker.UpdateType;

/**
 * 
 * A ServerRequestHandler is instanntiated for every Client to process requests from the server.
 *
 */

public class ServerRequestHandler {
    private final Client client;
    private final ChatGUI chatGUI;
    private final HashMap<Integer, ClientConversation> currentConvos;
    
    public ServerRequestHandler(Client client) {
        this.client = client;
        this.chatGUI = client.getChatGUI();
        this.currentConvos = client.getCurrentConvos();
    }
    
    /**
     * Handle incoming requests from the server on this socket.
     * @param socket that server is connected to
     * @throws IOException
     */
    public void handleRequest(Socket socket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        for (String line = in.readLine(); line != null; line = in.readLine()) {
            interpretRequest(line);                
        }      
    }
    
    // Parse the incoming request from the server
    private void interpretRequest(String request) {
        System.out.println("request from server is: "+request);
        ServerRequest serverRequest = new ServerRequest(request);
        
        switch(serverRequest.getType()) {
        
        case UserList:
            handleUserList(serverRequest);
            break;
        case LogOnUser:
            handleLogOnUser(serverRequest);
            break;
        case LogOffUser:
            handleLogOffUser(serverRequest);
            break;
        case AddUser:
            handleAddUser(serverRequest);
            break;
        case RemoveUser:
            handleRemoveUser(serverRequest);
            break;
        case Message:
            handleMessage(serverRequest);
            break;
        case ChangeUserState:
            handleChangeUserState(serverRequest);
            break;
        case NewConvo:
            handleNewConvo(serverRequest);
            break;
        case MemberList:
            handleMemberList(serverRequest);
            break;
        }
    }
    
    // Get the users currently online and populate buddy list
    private void handleUserList(ServerRequest msg) {
        String[] users = msg.getUserList();
        (new UpdateListWorker(this.chatGUI.getChatcard().getBuddyList().getListModel(), users, UpdateType.ADD)).execute();
        
        System.out.println(client.getCurrentTime() + " --- Action being performed: " + "I received userList");
    }
    
    // A user logs on - add to buddy list
    private void handleLogOnUser(ServerRequest msg) {
        ArrayList<String> args = msg.getArgs();
        String newUser = args.get(1);
        (new UpdateListWorker(this.chatGUI.getChatcard().getBuddyList().getListModel(), new String[] {newUser}, UpdateType.ADD)).execute();
        System.out.println(client.getCurrentTime() + " --- Action being performed: " + newUser + " logged on");
    }
    
    // A user logs off - remove from buddy list
    private void handleLogOffUser(ServerRequest msg) {
        ArrayList<String> args = msg.getArgs();
        String user = args.get(1);
        (new UpdateListWorker(this.chatGUI.getChatcard().getBuddyList().getListModel(), new String[] {user}, UpdateType.REMOVE)).execute();
        
        System.out.println(client.getCurrentTime() + " --- Action being performed: " + user + " logged off and was removed from the following conversations: "); 
        Iterator<ClientConversation> convoIterator = currentConvos.values().iterator();
        while (convoIterator.hasNext()) {
            ClientConversation nextConvo = convoIterator.next();
            if (nextConvo.containsUser(user)) {
                (new UpdateListWorker(nextConvo.getListModel(), new String[] {user}, UpdateType.REMOVE)).execute();
                nextConvo.addSystemMsg(user + " logged off.");
                System.out.print(nextConvo.getID() + " ");
            }
        }
        
    }
    
    // When you or another person is added to an existing conversation
    private void handleAddUser(ServerRequest msg) {
        ArrayList<String> args = msg.getArgs();
        int id = Integer.parseInt(args.get(1));
        String joiningUser = args.get(2);
        String userFrom = args.get(3);
        
        // Someone is added to a conversation that you're currently in
        if (currentConvos.containsKey(id)) {
            ClientConversation convo = currentConvos.get(id);
            convo.addMembers(new String[] {joiningUser});
            currentConvos.get(id).addSystemMsg(userFrom + " added " + joiningUser + " to this conversation.");
            System.out.println(client.getCurrentTime() + " --- Action being performed: " + userFrom + "added " + joiningUser + " to convo " + id); 
        }
        // You are added to an existing conversation
        else if (joiningUser.equals(client.getUsername())) {
        	initNewConvo(client, id);
        	currentConvos.get(id).addSystemMsg(userFrom + " added you to this conversation.");
        	System.out.println(client.getCurrentTime() + " --- Action being performed: " + userFrom + " added me to convo " + id);        	
        }
    }
    
    // When someone leaves a conversation that you're in
    private void handleRemoveUser(ServerRequest msg) {
        ArrayList<String> args = msg.getArgs();
        int id = Integer.parseInt(args.get(1));
        String leavingUser = args.get(2);
        
        if (currentConvos.containsKey(id)) {
            (new UpdateListWorker(currentConvos.get(id).getListModel(), new String[] {leavingUser}, UpdateType.REMOVE)).execute();
            currentConvos.get(id).addSystemMsg(leavingUser + " left this conversation.");
            //currentConvos.get(id).removeUser(leavingUser);
            System.out.println(client.getCurrentTime() + " --- Action being performed: " + leavingUser + " removed from convo " + id);
        }
    }
    
    // Add a message to a conversation
    private void handleMessage(ServerRequest msg) {
        ArrayList<String> args = msg.getArgs();
        int id = Integer.parseInt(args.get(1));

        String userFrom = args.get(2);
        String text = args.get(3);
        
        ClientConversation convo;
        if (currentConvos.containsKey(id)) {
            convo = currentConvos.get(id);
            ChatMessage chatMsg = new ChatMessage(userFrom, text);
            // change sender's status to NO_TEXT
            convo.getListModel().changeTypingStatus(userFrom, Adapter.TypingStatus.NO_TEXT);
            convo.addChatMsg(chatMsg);
            System.out.println(client.getCurrentTime() + " --- Action being performed: " + userFrom + " sent message in convo " + id);
        }          
        
        
    }
    
    // When someone starts a new conversation with you.
    private void handleNewConvo(ServerRequest msg) {
        ArrayList<String> args = msg.getArgs();
        int id = Integer.parseInt(args.get(1));
        String userTo = args.get(2);
        String userFrom = args.get(3);
        String text = args.get(4);
        
        ClientConversation convo = initNewConvo(client, id);
        convo.addMembers(new String[] {userFrom, userTo});
        System.out.println(client.getCurrentTime() + " --- Action being performed: " + userFrom + " started convo with " + userTo);
        
        ChatMessage chatMsg = new ChatMessage(userFrom, text);
        convo.addChatMsg(chatMsg);
    }
    
    // Set the member list of an existing conversation that you were just added to.
    private void handleMemberList(ServerRequest msg) {
        ArrayList<String> args = msg.getArgs();
        int id = Integer.parseInt(args.get(1));
        String[] members = msg.getMemberList();
        ClientConversation convo = currentConvos.get(id);
        convo.addMembers(members);
        System.out.println(client.getCurrentTime() + " --- Action being performed: " + "I received member list for convo " + id + " with users ");
        for (int i=0; i<members.length; i++) {
            System.out.print(members[i] + " ");
        }
    }
    
    // Add a new conversation 
    private ClientConversation initNewConvo(Client client, int id) {
        ClientConversation convo = new ClientConversation(client, id);
        currentConvos.put(id, convo);
        ChatRoomTab newTab = new ChatRoomTab(convo);
        chatGUI.getChatcard().getChatPane().addChatTab("Convo "+Integer.toString(id), newTab);
        
        // if there is a buddy currently selected in the buddy list, initialize tab with add user button
        BuddyList bl = chatGUI.getChatcard().getBuddyList();
        if (!bl.isSelectionEmpty()) {
            newTab.setSelectedUser(bl.getSelectedBuddy());
        }
        return convo;
    }
    
    // Set a user's typing status
    private void handleChangeUserState(ServerRequest msg) {
        ArrayList<String> args = msg.getArgs();
        int id = Integer.parseInt(args.get(1));
        String user = args.get(2);
        String newState = args.get(3);
        Adapter.TypingStatus status;
        
        if (newState.equals("isTyping")){
            status = Adapter.TypingStatus.IS_TYPING;
        } else if (newState.equals("hasEnteredText")){
            status = Adapter.TypingStatus.HAS_ENTERED_TEXT;
        } else {
            status = Adapter.TypingStatus.NO_TEXT;
        }
        
        currentConvos.get(id).getListModel().changeTypingStatus(user, status);
        System.out.println(client.getCurrentTime() + " --- Action being performed: " + user + " changed typing status to " + status);
    }

}
