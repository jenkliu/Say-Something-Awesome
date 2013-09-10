package main;

import gui.ChatGUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import client.ClientConversation;
import client.ServerRequestHandler;


/**
 * GUI chat client runner.
 */
public class Client {

    private String username;
    private HashMap<Integer, ClientConversation> currentConvos;
    private ChatGUI chatGUI;
    private Socket socket;
    private ServerRequestHandler requestHandler;
    
    public Client(Socket sock, String username, ChatGUI gui) throws UnknownHostException, IOException {
        this.username = username;
        this.currentConvos = new HashMap<Integer, ClientConversation>();
        this.chatGUI = gui;
        this.socket = sock;
        this.requestHandler = new ServerRequestHandler(this);
        
        serverConnect(socket);
    }
    
    // Create a receiving thread to listen for requests from the server
    private void serverConnect(final Socket socket) throws IOException {      
        Thread receiveThread = new Thread(new Runnable() {
            public void run() {
                try {
                   requestHandler.handleRequest(socket); 
                } catch (IOException e) {
                    // better way to handle exceptions here?
                    e.printStackTrace();
                }
            }
        });
        receiveThread.start();        
    }
   
    /**
     * Creates the final Client that is connected to the server.
     * 
     * @param host, port, username, mygui
     * @throws UnknownHostException
     *      if the client cannot connect to the server.
     * @throws IOException
     */
    public static Client connect(String host, int port, String username, ChatGUI mygui) throws UnknownHostException, IOException{
        Socket sock = new Socket(host, port);
        
        if (!requestUsernameAvailability(sock, username)) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "This username has already been taken. Please choose another.",
                    "Username unavilable",
                    JOptionPane.WARNING_MESSAGE);
            return null;
        }
        
        return new Client(sock, username, mygui);
        
    }
    
    public String getUsername() {
        return username;
    }

    public HashMap<Integer, ClientConversation> getCurrentConvos() {
        return currentConvos;
    }

    public ChatGUI getChatGUI() {
        return chatGUI;
    }

    public Socket getSocket() {
        return socket;
    }
    
    /**
     * Send a request to the server to create a conversation with userTo with message
     * @param userTo
     * @param message
     */
    public void requestCreateChat(String userTo, String message) {
        sendRequest("create " + userTo + " " + username + " " + message);
    }
    
    /**
     * Send a request to the server for a lis of all users currently online.
     */
    public void requestUserList() {
        sendRequest("userList " + username);
    }
    
    private void sendRequest(String request) {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Creates a socket to connect to the server and queries whether the selected username is taken
     * 
     * @param host
     *          the IP address of the server to connect to
     * @param port
     *          the port number
     * @param username
     *          the requested username
     * @return
     *          true if username is accepted, false if it is already taken
     * @throws UnknownHostException, IOException if invalid host or port is entered (caught by GUI)
     */
    public static boolean checkUsernameAvailability(String host, int port, String username) throws UnknownHostException, IOException  {
        Socket tempSock = new Socket(host, port);
        if (requestUsernameAvailability(tempSock, username)) {
            return true;
        }
        return false;      
    }
    
    // Send username request to server
    private static boolean requestUsernameAvailability(Socket socket, String username) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        
        out.println(username);
        
        String response = in.readLine();
        if (response != null) {
            String[] tokens = response.split(" ");
            if (tokens[0].equals("accepted")) {
                return true;
            } // username is taken               
            else if (tokens[0].equals("conflict")) {
                return false;
            }
        }   
        throw new RuntimeException("Error: connection failed");
    }
    
    /**
     * Start a GUI chat client.
     */
    public static void main(String[] args) {
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, just use default
        }
        ChatGUI gui = new ChatGUI();
    }
    
    /**
     * Get the current time (for console-printing purposes)
     * @return String of the date and time
     */
    public String getCurrentTime() {
        java.util.Date date = new java.util.Date();
        Timestamp t = new Timestamp(date.getTime());
        return t.toString();
    }
    
}
