package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import server.ServerMessage.ServerMessageType;


import main.Server;

/**
 * Server Conversation Thread.
 */
public class ServerConversationThread extends Thread {
    private Server server;
    public ConcurrentLinkedQueue<String> queue;
    private ServerConversation conversation;
    private boolean active;
    
    public ServerConversationThread(Server s, ServerConversation c) {
        this.server = s;
        this.queue = new ConcurrentLinkedQueue<String>();
        this.conversation = c;
        this.active = true;
    }
    
    public ServerConversation getConversation() {
        return this.conversation;
    }
    /**
     * This helper method takes input from the queue and redirects it to be
     * interpreted.
     */
    public void run() {
        while (active) {
            String request = queue.poll();
            if (request != null) {
                interpretRequest(request);
            }
        }
    }
    
    /**
     * Creates a ClientRequest with the request. Then the ClientRequest is
     * passed to its appropriate handler by switching ClientRequest.type
     * @param request - request taken from queue by the main thread.
     */
    private void interpretRequest(String request) {
        try {
            ClientRequest clientRequest = new ClientRequest(request);
            switch (clientRequest.getType()) {
            
            case AddUser:
                handleAddUser(clientRequest);
                break;
            case LeaveConvo:
                handleLeaveConvo(clientRequest);
                break;
            case Message:    
                handleMessage(clientRequest);
                break;
            case ChangeUserState:
                handleChangeUserState(clientRequest);
                break;
            case NewConvo:
                handleNewConvo(clientRequest);
                break;
            }
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    
    /**
     * The handler for adding users to Conversations. This method generates
     * the appropriate output, creates a ServerMessage and calls the
     * routeMessages method.
     * @param msg - ClientRequest from client
     */
    private void handleAddUser(ClientRequest msg) {
        ArrayList<String> args = msg.getArgs();
        int id = Integer.parseInt(args.get(1));
        String userTo = args.get(2);
        String userFrom = args.get(3);
        String output = "addUser " + id + " " + userTo + " " + userFrom;
        if (!(conversation.getUsers().contains(userTo)) && server.getSocketNames().keySet().contains(userTo)) {
            System.out.println(server.getCurrentTime() + " --- Action being performed: AddUser " + userTo + " to conversation " + id + " from " + userFrom);
            ServerMessageType type = ServerMessage.ServerMessageType.ToConversation;
            ServerMessage sm = new ServerMessage(conversation, userFrom, userTo, output, type);
            conversation.addUser(userTo);
            routeMessages(sm);
            
            //now tell the user who was added who is in the conversation
            System.out.println(server.getCurrentTime() + " --- Action being performed: Sending MemberList to " + userTo);
            String output1 = "memberList " + id + " ";
            for (String user: conversation.getUsers()) {
                output1 += user + " ";
            }
            ServerMessageType type1 = ServerMessage.ServerMessageType.ToClient;
            ServerMessage sm1 = new ServerMessage(conversation, userFrom, userTo, output1, type1);
            routeMessages(sm1);
        }
        else if (!server.getSocketNames().keySet().contains(userTo)) {
            System.out.println(server.getCurrentTime() + " --- Action interrupted: AddUser " + userTo + " to conversation " + id + " from " + userFrom);
            System.out.println("The user requested is offline");
        }
        else {
            System.out.println(server.getCurrentTime() + " --- Action interrupted: AddUser " + userTo + " to conversation " + id + " from " + userFrom);
            System.out.println("The user is already in the conversation");
        }
    }
    /**
     * The handler for leaving Conversations. This method generates
     * the appropriate output, creates a ServerMessage and calls the
     * routeMessages method.
     * @param msg - ClientRequest from client
     */
    private void handleLeaveConvo(ClientRequest msg) {
        ArrayList<String> args = msg.getArgs();
        int id = Integer.parseInt(args.get(1));
        String userFrom = args.get(2);
        String userTo = "";
        System.out.println(server.getCurrentTime() + " --- Action being performed: LeaveConvo " + id + " from user " + userFrom);
        // This conversation is over
        if (conversation.getUsers().contains(userFrom) && conversation.getUsers().size() == 1) {
            server.removeConversationMap(id);
            server.removeConvoThread(id);
            active = false;
            System.out.println("Conversation " + id + " deleted");
            
        }
        else {
            String output = "removeUser " + id + " " + userFrom;
            ServerMessageType type = ServerMessageType.ToConversation;
            ServerMessage sm = new ServerMessage(conversation, userFrom, userTo, output, type);
            conversation.removeUser(userFrom);
            routeMessages(sm);
        }
    }
    
    /**
     * The handler for sending messages to Conversations. This method generates
     * the appropriate output, creates a ServerMessage and calls the
     * routeMessages method.
     * @param msg - ClientRequest from client
     */
    private void handleMessage(ClientRequest msg) {
        ArrayList<String> args = msg.getArgs();
        int id = Integer.parseInt(args.get(1));
        String userFrom = args.get(2);
        String text = args.get(3);
        String userTo = "";
        
        System.out.println(server.getCurrentTime() + " --- Action being performed: Message in conversation " + id + " from " + userFrom);
        
        String output = "msg " + id + " " + userFrom + " " + text;
        ServerMessageType type = ServerMessageType.ToConversation;
        ServerMessage sm = new ServerMessage(conversation, userFrom, userTo, output, type);
        routeMessages(sm);
    }
    
    
    /**
     * The handler for changing states of the users. This outputs a serverMessage
     * to be sent by routeMessages with the new userState included.
     * @param msg
     */
    private void handleChangeUserState(ClientRequest msg) {
        ArrayList<String> args = msg.getArgs();
        int id = Integer.parseInt(args.get(1));
        String userFrom = args.get(2);
        String userState = args.get(3);
        String userTo = "";
        
        System.out.println(server.getCurrentTime() + " --- Action being performed: ChangeUserState of " + userFrom + " to " + userState + " in conversation " + id);

        
        String output = "changeUserState " + id + " " + userFrom + " " + userState;
        ServerMessageType type = ServerMessageType.ToConversation;
        ServerMessage sm = new ServerMessage(conversation, userFrom, userTo, output, type);
        routeMessages(sm);
    }
    
    private void handleNewConvo(ClientRequest msg) {
        ArrayList<String> args = msg.getArgs();
        int id = Integer.parseInt(args.get(1));
        String userTo = args.get(2);
        String userFrom = args.get(3);
        String text = args.get(4);
        
        System.out.println(server.getCurrentTime() + " --- Action being performed: NewConvo " + id + " from" + userTo);
        
        String output = "newConvo " + id + " " + userTo + " " + userFrom + " " + text;
        ServerMessageType type = ServerMessageType.ToConversation;
        ServerMessage sm = new ServerMessage(conversation, userFrom, userTo, output, type);
        routeMessages(sm);
        


    }
    


    /**
     * route the messages to the appropriate send method.
     * right now there is only one send, but this method is staying
     * to increase the ease of adding other messages and events.
     * @param sm
     */
    private void routeMessages(ServerMessage sm) {
        switch (sm.getType()) {
        
        case ToClient:
            sendToClient(sm);
            break;
        case ToConversation:
            sendToConvo(sm);
            break;

        }
    }
    
    /**
     * Sends the message to only one client
     * @param sm - the message being sent
     */
    private void sendToClient(ServerMessage sm) {
        Socket s = server.getSocketNames().get(sm.getUserTo());
        
        synchronized (s) {
            try {
                PrintWriter out = new PrintWriter(s.getOutputStream(), true);
                out.println(sm.getMsg());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Sends the message to the conversation and all users in it.
     * @param sm - the message to be sent.
     */
    private void sendToConvo(ServerMessage sm) {
        for (String name: sm.getConvo().getUsers()) {
            Socket s = server.getSocketNames().get(name);
            
            synchronized (s) {
                try {
                    PrintWriter out = new PrintWriter(s.getOutputStream(), true);
                    out.println(sm.getMsg());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
