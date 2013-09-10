package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import server.ClientRequest;
import server.ServerClientThread;
import server.ServerConversation;
import server.ServerConversationThread;
import server.ServerMessage;
import server.ServerMessage.ServerMessageType;

/**
 * Chat server runner.
 */
public class Server {
    private final static int PORT = 4444;
    private int maxConvoID = 0;
    private ConcurrentHashMap<String, Socket> socketNames;
    private ConcurrentHashMap<Integer, ServerConversationThread> convoThreads;
    private ConcurrentHashMap<Integer, ServerConversation> conversationMap;
    private ServerSocket serverSocket;
    public ConcurrentLinkedQueue<String> queue;
    
    
    public Server() throws IOException {
        this.socketNames = new ConcurrentHashMap<String, Socket>();
        this.convoThreads = new ConcurrentHashMap<Integer, ServerConversationThread>();
        this.conversationMap = new ConcurrentHashMap<Integer, ServerConversation>();
        this.serverSocket = new ServerSocket(PORT);
        this.queue = new ConcurrentLinkedQueue<String>();
        
    }

    /**
     * Main entrance sequence to the Server
     * @param args - command line arguments
     * @throws IOException because of the creation of sockets
     */
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.serve();
    }
    
    
    /**
     * The serve method handles the creation of the threads. The first 
     * thread is the main thread, it interacts with the queue, 
     * interprets data from the queue, and sends back messages to 
     * the clients. Also this method handles the creation of new threads 
     * for new clients who connect to the serverSocket. The thread is
     * responsible for physically reading the input from clients
     * and also to send the outputs back to the clients.
     * @throws IOException because of sockets
     */
    public void serve() throws IOException {
        // This creates the mainThread which will constantly be receiving client input and
        // handling it accordingly
        Thread mainThread = runMainThread();
        mainThread.start();
        
        // This is where sockets are matched with incoming client connections and
        // threads are started for each.
        while (true) {
            final Socket socket = serverSocket.accept();
            Thread thread = new ServerClientThread(this, socket);
            thread.start();
        }
    }
    
    /**
     * @return the current socketNames concurrent hashmap
     */
    public ConcurrentHashMap<String, Socket> getSocketNames() {
        return this.socketNames;
    }
    /**
     * @return the current convoThreads concurrent hashmap
     */
    public ConcurrentHashMap<Integer, ServerConversationThread> getConvoThreads() {
        return this.convoThreads;
    }
    /**
     * Adds a socket to the socketNames hashmap
     * @param name - name to add
     * @param socket - socket associated with name
     */
    public void addSocketName(String name, Socket socket) {
        socketNames.put(name, socket);
    }
    /**
     * removes a name from the socketNames hashmap
     * @param name - name to remove from the hashmap
     */
    public void removeSocketName(String name) {
        socketNames.remove(name);
    }
    /**
     * Removes a convo from the convoThreads hashmap
     * @param id - id to remove from the hashmap
     */
    public void removeConvoThread(int id) {
        convoThreads.remove(id);
    }
    /**
     * removes a conversation from conversationMap
     * @param id - id of the conversation to remove
     */
    public void removeConversationMap(int id) {
        conversationMap.remove(id);
    }
    
    
    /**
     * Creates the main server thread. This thread is responsible for 
     * creating all of the conversation threads as new conversations are added.
     * This thread also handles the messages intended for specific people (messages that
     * are conversation specific are handled by their respective conversation).
     * threads.
     */
    public Thread runMainThread() {
        Thread conversationThread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    String request = queue.poll();
                    if (request != null) {
                        interpretRequest(request);
                    }
                }
            }
        });
        return conversationThread;
    }
    
    /**
     * Creates a ClientRequest with the request. Then the ClientRequest is
     * passed to its appropriate handler by switching ClientRequest.type.
     * The ClientRequest will either be a createConvo request or a userList
     * request.
     * @param request - request taken from queue by the main thread.
     */
    private void interpretRequest(String request) {
        try {
            ClientRequest clientRequest = new ClientRequest(request);
            switch (clientRequest.getType()) {
            
            case CreateConvo:
                handleCreateConvo(clientRequest);
                break;
            case UserList:
                handleUserList(clientRequest);
                break;
            case Invalid:
                handleInvalidRequest(clientRequest);
                break;
            }
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * The handler for a createConvo request. Once received the Server checks
     * if the conversation already exists. If not, a new Conversation thread is created
     * and from now on all requests for that conversation are directed to that conversations
     * thread queue.
     * @param msg - ClientRequest from client
     */
    private void handleCreateConvo(ClientRequest msg) {
        ArrayList<String> args = msg.getArgs();
        String userTo = args.get(1);
        String userFrom = args.get(2);
        boolean alreadyExists = false;
        
        for (ServerConversation c: conversationMap.values()) {
            if (c.getUsers().contains(userFrom) && c.getUsers().contains(userTo) && c.getUsers().size() == 2) {
                alreadyExists = true;
            }
        }
        if (!alreadyExists && socketNames.keySet().contains(userTo)) {
            String text = args.get(3);
            int id = maxConvoID++;
            String output = "newConvo " + id + " " + userTo + " " + userFrom + " " + text;
            System.out.println(getCurrentTime() + " --- Action being performed: " + userFrom + " CreateConvo with " + userTo + " with ID = " + id);
            
            ServerConversation convo = new ServerConversation(id, userTo, userFrom);
            conversationMap.put(id, convo);
            
            ServerConversationThread thread = new ServerConversationThread(this, convo);
            thread.start();
            convoThreads.put(id, thread);
            thread.queue.add(output);
        }
        else if (!alreadyExists) {
            System.out.println(getCurrentTime() + " --- Action interrupted - CreateConvo: User " + userTo + " logged off");
        }
        else {
            System.out.println(getCurrentTime() + " --- Action interrupted - CreateConvo: Conversation already exists");
        }
    }
    
    /**
     * Handles the request for the current UserList. This handler compiles that
     * list and sends the serverMessage to the routeMessages handler.
     * @param msg - ClientRequest from client
     */
    private void handleUserList(ClientRequest msg) {
        ArrayList<String> args = msg.getArgs();
        String userFrom = args.get(1);
        String userTo = "";
        String userList = "userList ";
        for (String s: socketNames.keySet()) {
            if (!userFrom.equals(s)) {
                userList += s + " ";
            }
        }
        System.out.println(getCurrentTime() + " --- Action being performed: UserList request to/from " + userFrom);
        ServerConversation convo = null;
        ServerMessageType type = ServerMessageType.ToClient;
        ServerMessage sm = new ServerMessage(convo, userFrom, userTo, userList, type);
        routeMessages(sm);
    }
    
    private void handleInvalidRequest(ClientRequest msg) {
        System.out.println(getCurrentTime() + " --- Invalid Request Received: " + msg.getArgs().get(0));
    }
    
    /**
     * Routes the messages to the right send method, depending on
     * who the messages are destined for.
     * @param sm - the message intended to be sent to the users.
     */
    private void routeMessages(ServerMessage sm) {
        switch (sm.getType()) {
        
        case ToClient:
            sendToClient(sm);
            break;
            
        case ToConversation:
            sendToConvo(sm);
            break;
            
        case ToAll:
            sendToAll(sm);
            break;
        }
    }
       
    /**
     * Sends the message to only one client
     * @param sm - the message being sent
     */
    private void sendToClient(ServerMessage sm) {
        Socket s = socketNames.get(sm.getUserFrom());
        
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
     * Sends the message to only one conversation
     * @param sm - the message being sent
     */
    private void sendToConvo(ServerMessage sm) {
        for (String name: sm.getConvo().getUsers()) {
            Socket s = socketNames.get(name);
            
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
    /**
     * Sends the message to everyone
     * @param sm - the message being sent
     */
    private void sendToAll(ServerMessage sm) {
        for (Socket s: socketNames.values()) {
            
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
    
    public String getCurrentTime() {
        java.util.Date date = new java.util.Date();
        Timestamp t = new Timestamp(date.getTime());
        return t.toString();
    }
    
}

