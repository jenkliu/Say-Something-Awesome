package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import main.Client;

/**
 * Message from the Client to the Server.
 *
 */
public class ClientMessenger {
    private final Client client;
    private final int convoID;
    private final String username;
    private final Socket socket;
    
    public ClientMessenger (Client client, int convoID) {
        this.client = client;
        this.convoID = convoID;
        this.username = this.client.getUsername();
        this.socket = this.client.getSocket();
    }
    
    /**
     * Tell the server to add a user to this conversation. 
     * 
     * @param userToAdd
     */
    public void requestAddUser(String userToAdd) {
        sendRequest("addUser " + convoID + " " + userToAdd + " " + username);
    }
    
    /**
     * Tell the server to send a message in this conversation.
     * 
     * @param message
     */
    public void requestSendMessage(String message) {
        sendRequest("msg " + convoID + " " + username + " " + message);
    }
    
    /**
     *  Tell the server that you left this conversation.
     */
    public void requestLeave() {
        sendRequest("leave " + convoID + " " + username);
        System.out.println(username+" left chat "+convoID);
    }
    
    /**
     * Tell the server that your typing status has changed.
     * 
     * @param newState
     *          new typing status
     */
    public void requestChangeUserState(String newState) {
        sendRequest("changeUserState " + convoID + " " + username + " " + newState);
    }
    
    /**
     * Send the request to the server
     * 
     * @param request
     */
    private void sendRequest(String request) {
        try {
            // should we make a new PrintWriter each time?
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public Client getClient() {
    	return client;
    }
}
