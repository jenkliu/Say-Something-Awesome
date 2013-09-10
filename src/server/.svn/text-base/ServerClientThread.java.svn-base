package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import main.Server;

public class ServerClientThread extends Thread {
    private Server server;
    private Socket socket;
    
    public ServerClientThread(Server s, Socket soc) {
        this.server = s;
        this.socket = soc;
    }
    
    
    /**
     * Entry sequence for ServerClientThreads. Attempts to login the client,
     * queue their requests for handling, and then finally log the user off
     */
    public void run() {
        String input = "";
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            boolean loggedIn = false;
            
            while (!loggedIn) {
                input = in.readLine();
                loggedIn = logIn(input, socket);
            } 
            queueRequests(socket);
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        
        // User Logs off
        finally {
            logOff(input, socket);
        }
    }
    
    /**
     * Handles the logIn process
     * @param input - most recent read from inputstream of client (username)
     * @param socket - socket of the connecting client
     * @return true if login successful, false otherwise
     * @throws IOException
     */
    private boolean logIn(String input, Socket socket) throws IOException {
        boolean conflict = false;
        boolean loggedIn = false;
        
        for (String name: server.getSocketNames().keySet()) {
            if (input.equalsIgnoreCase(name)) {
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                String output = "conflict " + input + " is already taken";
                out.println(output);
                conflict = true;
            }
        }
        if (!conflict) {
            loggedIn = true;
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            String output = "accepted " + input;
            out.println(output);
            System.out.println(server.getCurrentTime() + " --- User Logging In: " + input);
            for (Socket s: server.getSocketNames().values()) {
                synchronized (s) {
                    PrintWriter out1 = new PrintWriter(s.getOutputStream(), true);
                    String output1 = "logOnUser " + input;
                    out1.println(output1);
                }
            }
            server.addSocketName(input, socket);
        }
        return loggedIn;
    }
    
    /**
     * This method is called when a user exits the application, terminating
     * the constant read of inputs and enters this method through the finally
     * block of run. The socket is closed here.
     * @param input - Username of client
     * @param socket - Socket of client
     */
    private void logOff(String input, Socket socket) {
        PrintWriter out;
        
        try {
            String output = "logOffUser " + input;
            server.removeSocketName(input);
            System.out.println(server.getCurrentTime() + " --- User Logging Off: " + input);
            for (ServerConversationThread sct: server.getConvoThreads().values()) {

                if (sct.getConversation().getUsers().contains(input)) {
                    sct.queue.add("leave " + sct.getConversation().getID() + " " + input);
                }
            }
            for (Socket s: server.getSocketNames().values()) {
                synchronized (s) {
                    out = new PrintWriter(s.getOutputStream(), true);
                    out.println(output);
                }
            }
            socket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads in input from clients and determines which queue to put them
     * in.
     * @param s - The socket the client is connected through
     */
    private void queueRequests(Socket s) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            for (String input = in.readLine(); input != null; input = in.readLine()) {
                ClientRequest cm = new ClientRequest(input);
                switch (cm.getType()) {
                
                case AddUser:
                case LeaveConvo:
                case Message:
                case ChangeUserState:
                    int id = Integer.parseInt(cm.getArgs().get(1));
                    ServerConversationThread t = server.getConvoThreads().get(id);
                    t.queue.add(cm.getMsg());
                    break;
                
                case CreateConvo:
                case UserList:
                case Invalid:
                    server.queue.add(input);
                    break;
                }
            }
        }
        catch (IOException e) {
            System.out.println(server.getCurrentTime() + " --- User input interrupt: " + s.toString());
        }
    }
}
