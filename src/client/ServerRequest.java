package client;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Class used for client-side classification and tokenization of incoming ServerMessages
 * 
 */
public class ServerRequest {
    private String message;
    private List<String> args;
    private ServerRequestType type;
    
    public enum ServerRequestType {
        UserList, // List of all users currently online
        LogOnUser, // A user has logged on
        LogOffUser, // A user has logged off
        AddUser, // A user has been added to a conversation
        RemoveUser, // A user has left a conversation
        Message, // A message has been sent in an existing conversation
        ChangeUserState, // The typing status of a user has changed
        NewConvo, // A new conversation has been created with you in it
        MemberList // List of all members of a conversation
    }
    
    public ServerRequest(String msg) {
        this.message = msg;
        this.args = setArgs();
        this.type = setType();
    }
    
    private List<String> setArgs() {
        StringTokenizer tokenizer = new StringTokenizer(message, " ");
        ArrayList<String> tokens = new ArrayList<String>();
        tokens.add(tokenizer.nextToken());
        
        if (tokens.get(0).equals("msg")) {
            tokens.add(tokenizer.nextToken());      // 1 = convoID
            tokens.add(tokenizer.nextToken());      // 2 = userFrom
            String text = "";
            while(tokenizer.hasMoreTokens()) {
                text += tokenizer.nextToken();
                if (tokenizer.hasMoreTokens()) {
                	text += " ";
                }
            }
            tokens.add(text);                       // 3 = text
        }
        
        else if (tokens.get(0).equals("newConvo")) {
            tokens.add(tokenizer.nextToken());      // 1 = convoID
            tokens.add(tokenizer.nextToken());      // 2 = userFrom
            tokens.add(tokenizer.nextToken());      // 3 = userTo
            String text = "";
            while(tokenizer.hasMoreTokens()) {
                text += tokenizer.nextToken();
                if (tokenizer.hasMoreTokens()) {
                    text += " ";
                }
            }
            tokens.add(text);                       // 4 = text
        }
        
        else if (tokens.get(0).equals("memberList")) {
            tokens.add(tokenizer.nextToken());      // 1 = convoID
            while(tokenizer.hasMoreTokens()) {
                tokens.add(tokenizer.nextToken());  // list of usernames in convo
            }
        }
        
        else if (tokens.get(0).equals("userList")) {
            while(tokenizer.hasMoreTokens()) {
                tokens.add(tokenizer.nextToken());  // list of usernames online
            }
        }
        
        else if (tokens.get(0).equals("logOnUser")) {
            tokens.add(tokenizer.nextToken());      // 1 = username
        }
        
        else if (tokens.get(0).equals("logOffUser")) {
            tokens.add(tokenizer.nextToken());      // 1 = username
        }
        
        else if (tokens.get(0).equals("addUser")) {
            tokens.add(tokenizer.nextToken());      // 1 = convoID
            tokens.add(tokenizer.nextToken());      // 2 = userTo
            tokens.add(tokenizer.nextToken());      // 3 = userFrom
        }
        
        else if (tokens.get(0).equals("removeUser")) {
            tokens.add(tokenizer.nextToken());      // 1 = convoID
            tokens.add(tokenizer.nextToken());      // 2 = username
        }
        
        else if (tokens.get(0).equals("conflict")) {
            tokens.add(tokenizer.nextToken());      // 1 = username
        }
        
        else if (tokens.get(0).equals("accepted")) {
            tokens.add(tokenizer.nextToken());      // 1 = username
        }
        
        else if (tokens.get(0).equals("changeUserState")) {
            tokens.add(tokenizer.nextToken());      // 1 = convoID
            tokens.add(tokenizer.nextToken());      // 2 = username
            tokens.add(tokenizer.nextToken());      // 3 = user state
        }
        
        // should never get here
        else {
            return null;
        }
        return tokens;
    }
    
    private ServerRequestType setType() {
        if (this.args.get(0).equals("msg")) {
            return ServerRequestType.Message;
        }
        else if (this.args.get(0).equals("userList")) { 
            return ServerRequestType.UserList;
        }
        else if (this.args.get(0).equals("newConvo")) {
            return ServerRequestType.NewConvo;
        }
        else if (this.args.get(0).equals("memberList")) {
            return ServerRequestType.MemberList;
        }
        else if (this.args.get(0).equals("logOnUser")) {
            return ServerRequestType.LogOnUser;
        }
        else if (this.args.get(0).equals("logOnUser")) {
            return ServerRequestType.LogOnUser;
        }
        else if (this.args.get(0).equals("logOffUser")) {
            return ServerRequestType.LogOffUser;
        }
        else if (this.args.get(0).equals("addUser")) {
            return ServerRequestType.AddUser;
        }
        else if (this.args.get(0).equals("removeUser")) {
            return ServerRequestType.RemoveUser;
        }
        else if (this.args.get(0).equals("changeUserState")) {
            return ServerRequestType.ChangeUserState;
        }
        // should not get here
        else {
            return null;
        }
    }
    
    /**
     * Get the tokenized form of the request.
     * @return ArrayList of tokens
     */
    public ArrayList<String> getArgs() {
        return new ArrayList<String>(this.args);
    }
    
    /**
     * Get the type of ServerRequest
     * @return ServerRequestType
     */
    public ServerRequestType getType() {
        return this.type;
    }
    
    /**
     * get the UserList for a ServerRequest of ServerRequestType UserList
     * requires that this is of ServerRequestType UserList
     * @return
     *      String[] of usernames that are currently online
     * @throws RuntimeException if this is not of ServerRequestType UserList
     */
    public String[] getUserList() {
        if (this.type.equals(ServerRequestType.UserList)) {
            ArrayList<String> args = this.getArgs();
            String[] userArray = new String[args.size()-1];
            for (int i=0; i < args.size()-1; i++) {
                userArray[i] = args.get(i+1);
                
            }
            return userArray;
        }
        throw new RuntimeException("Error: input is not of type UserList");
    }
    
    /**
     * get the MemberList for a ServerRequest of ServerRequestType MemberList
     * requires that this is of ServerRequestType MemberList
     * @return
     *      String[] of usernames
     * @throws RuntimeException if this is not of ServerRequestType UserList
     */
    public String[] getMemberList() {
        if (this.type.equals(ServerRequestType.MemberList)) {
            ArrayList<String> args = this.getArgs();
            String[] memberArray = new String[args.size()-2];
            for (int i=0; i < args.size()-2; i++) {
                memberArray[i] = args.get(i+2);
                
            }
            return memberArray;
        }
        throw new RuntimeException("Error: input is not of type MemberList");
    }
    
}
