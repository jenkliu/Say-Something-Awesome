package server;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Class for the Server to process a request from the Client to the Server.
 * 
 */
public class ClientRequest {
    private String message;
    private List<String> args;
    private ClientRequestType type;
    
    public enum ClientRequestType {
        CreateConvo,
        NewConvo,
        AddUser,
        LeaveConvo,
        Message,
        UserList,
        DeleteConvo,
        ChangeUserState,
        Invalid
    }
    
    public ClientRequest(String msg) {
        this.message = msg;
        this.args = setArgs();
        this.type = setType();
    }

    /**
     * Sets the arguments by using a tokenizer on the input message.
     * @return tokens - list of tokens gathered
     */
    private List<String> setArgs() {
        StringTokenizer tokenizer = new StringTokenizer(message, " ");
        ArrayList<String> tokens = new ArrayList<String>();
        tokens.add(tokenizer.nextToken());
        
        if (tokens.get(0).equals("leave")) {        // 0 = "leave"
            tokens.add(tokenizer.nextToken());      // 1 = convoID
            tokens.add(tokenizer.nextToken());      // 2 = userFrom
        }
        else if (tokens.get(0).equals("addUser")) { // 0 = "addUser"
            tokens.add(tokenizer.nextToken());      // 1 = convoID
            tokens.add(tokenizer.nextToken());      // 2 = userTo
            tokens.add(tokenizer.nextToken());      // 2 = userFrom
        }                                      
        else if (tokens.get(0).equals("create")) {  // 0 = "create"
            tokens.add(tokenizer.nextToken());      // 1 = userTo
            tokens.add(tokenizer.nextToken());      // 2 = userFrom
            String text = "";                       // 3 = text
            while (tokenizer.hasMoreTokens()) {
                text += tokenizer.nextToken() + " ";
            }
            tokens.add(text);
        }
        else if (tokens.get(0).equals("msg")) {     // 0 = "msg"
            tokens.add(tokenizer.nextToken());      // 1 = convoID
            tokens.add(tokenizer.nextToken());      // 2 = userFrom
            String text = "";                       // 3 = text
            while (tokenizer.hasMoreTokens()) {
                text += tokenizer.nextToken() + " ";
            }
            tokens.add(text);
        }
        else if (tokens.get(0).equals("userList")) {    // 0 = "userList"
            tokens.add(tokenizer.nextToken());          // 1 = userFrom
        }
        
        else if (tokens.get(0).equals("delete")) {      // 0 = "delete"
            tokens.add(tokenizer.nextToken());          // 1 = convoID
        }
        else if (tokens.get(0).equals("changeUserState")) { // 0 = "changeUserState"
            tokens.add(tokenizer.nextToken());              // 1 = convoID
            tokens.add(tokenizer.nextToken());              // 2 = userFrom
            tokens.add(tokenizer.nextToken());              // 3 = userState
        }
        else if (tokens.get(0).equals("newConvo")) {    // 0 = "newConvo"
            tokens.add(tokenizer.nextToken());          // 1 = convoID
            tokens.add(tokenizer.nextToken());          // 2 = userFrom
            tokens.add(tokenizer.nextToken());          // 3 = userTo
            String text = "";                           // 4 = text
            while (tokenizer.hasMoreTokens()) {
                text += tokenizer.nextToken() + " ";
            }
            tokens.add(text);
        }
        // do nothing, the setType() method will set the request as invalid
        else {
        }
        return tokens;
    }
    
    
    /**
     * Checks the first arg and sets the ClientRequest's type
     * by it.
     * @return ClientRequestType - an ENUM typing of the different messages
     */
    private ClientRequestType setType() {
        if (this.args.get(0).equals("msg")) {
            return ClientRequestType.Message;
        }
        else if (this.args.get(0).equals("leave")) {
            return ClientRequestType.LeaveConvo;
        }
        else if (this.args.get(0).equals("addUser")) {
            return ClientRequestType.AddUser;
        }
        else if (this.args.get(0).equals("create")) {
            return ClientRequestType.CreateConvo;
        }
        else if (this.args.get(0).equals("userList")) {
            return ClientRequestType.UserList;
        }
        else if (this.args.get(0).equals("delete")) {
            return ClientRequestType.DeleteConvo;
        }
        else if (this.args.get(0).equals("changeUserState")) {
            return ClientRequestType.ChangeUserState;
        }
        else if (this.args.get(0).equals("newConvo")) {
            return ClientRequestType.NewConvo;
        }
        // Should never get here
        else {
            return ClientRequestType.Invalid;
        }
    }
    
    public ArrayList<String> getArgs() {
        return new ArrayList<String>(this.args);
    }
    
    public ClientRequestType getType() {
        return this.type;
    }
    
    public String getMsg() {
        return this.message;
    }
    
    public String toString () {
        return this.type.toString();
    }
}
