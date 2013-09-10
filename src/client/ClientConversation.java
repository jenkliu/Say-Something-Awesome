package client;

import javax.swing.text.BadLocationException;

import main.Client;
import model.Adapter;
import model.ChatDocument;
import model.ChatMessage;
import model.UpdateListWorker;
import model.UserListModel;
import model.UpdateListWorker.UpdateType;

/**
 * Client-side model for the conversation
 *
 */

public class ClientConversation  {
    private final int id;
    private final Client client;
    private final ClientMessenger cm;
    
    private final UserListModel memberList;
    private final ChatDocument chatDoc;
    
    /**
     * For instantiating a conversation that you have just been added to
     * 
     * @param client
     * @param id
     *          the id of the new conversation
     */
    public ClientConversation(Client client, int id) {
        this.client = client;
        this.id = id;
        
        this.memberList = new UserListModel();
        this.chatDoc = new ChatDocument();
        this.cm = new ClientMessenger(this.client, this.id);
    }
    
    /**
     * For instantiating a new conversation started by user1 and sent to user 2.
     * 
     * @param client
     * @param id
     * @param user1 who started the conversation
     * @param user2 with whom the conversation was started 
     */
    public ClientConversation(Client client, int id, String user1, String user2) {
        this.client = client;
        this.id = id;
        
        this.memberList = new UserListModel();
        this.chatDoc = new ChatDocument();
        addMembers(new String[] {user1, user2});
        
        this.cm = new ClientMessenger(this.client, this.id);
    }
    
    /**
     * Add members to this conversation. 
     * @param members
     *          array of members
     */
    public void addMembers(String[] members) {
        (new UpdateListWorker(memberList, members, UpdateType.ADD)).execute();
        System.out.println("adding members to convo "+ id);
        for (int i = 0; i < members.length; i++) {
            System.out.print(members[i] + " ");
        }
    }
    /**
     * Get the members in this conversation.
     * @return array of members in this conversation
     */
    public Object[] getMembers() {
        return this.memberList.toArray();
    }
    
    /**
     * Get the id of this conversation.
     * @return id of this conversation
     */
    public int getID() {
        return this.id;
    }
    
    /**
     * Change the typing status of a member of this conversation.  
     * @param username
     * @param status
     */
    public void changeMemberStatus(String username, Adapter.TypingStatus status) {
        this.memberList.changeTypingStatus(username, status);
    }
    
    /**
     * Check if this conversation contains this user.
     * @param username we are checking
     * @return true if this conversation contains the user
     */
    public boolean containsUser(String username) {
        return this.memberList.contains(username);
    }
    
    /**
     * Add a chat message to the chat document
     * @param msg
     */
    public void addChatMsg(ChatMessage msg) {
        try {
			this.chatDoc.addChatMsg(msg);
		} catch (BadLocationException e) {
		    System.out.println("Error updating doc: index not found");
		}
    }
    /**
     * Add a notification to the chat document
     * @param msg
     */
    public void addSystemMsg(String msg){
    	try {
			this.chatDoc.addSystemMsg(msg);
		} catch (BadLocationException e) {
			System.out.println("Error updating doc: index not found");
		}
    }
    
    /**
     * Get the list model for this conversation.
     * @return memberList
     */
    public UserListModel getListModel() {
        return memberList;
    }
    
    /**
     * Get the client associated with this conversation. 
     * @return client
     */
    public Client getClient() {
    	return client;
    }
    /**
     * Get the client messenger associated with this conversation.
     * @return ClientMessenger
     */
    public ClientMessenger getClientMessenger() {
        return this.cm;
    }
    
    /**
     * Get the chat document model associated with this conversation.
     * @return ChatDocument
     */
    public ChatDocument getChatDoc() {
        return this.chatDoc;
    }
}
