package server;


/**
 * Message from the Server to the Clients that are part of a ConversationThread.
 */
public class ServerMessage {
    private ServerConversation convo;
    private String userTo;
    private String userFrom;
    private String msg;
    private ServerMessageType type;
    
    
    public enum ServerMessageType {
        ToClient,
        ToConversation,
        ToAll
    }
    
    public ServerMessage(ServerConversation c, String userfrom, String userto, String m, ServerMessageType type) {
        this.convo = c;
        this.userFrom = userfrom;
        this.userTo = userto;
        this.msg = m;
        this.type = type;
    }
    
    public ServerConversation getConvo() {
        return this.convo;
    }
    
    public String getUserTo() {
        return this.userTo;
    }
    
    public String getUserFrom() {
        return this.userFrom;
    }
    
    public String getMsg() {
        return this.msg;
    }
    
    public ServerMessageType getType() {
        return this.type;
    }
}
