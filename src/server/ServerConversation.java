package server;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ServerConversation {
    private final int id;
    private final Set<String> users;
    
    public ServerConversation(int i, String user1, String user2) {
        Set<String> tmp = new HashSet<String>();
        this.users = Collections.synchronizedSet(tmp);
        this.id = i;
        this.users.add(user1);
        this.users.add(user2);
        
    }
    
    public Set<String> getUsers() {
        return this.users;
    }
    
    public int getID() {
        return this.id;
    }
    
    public synchronized void addUser(String user) {
        this.users.add(user);
    }
    
    public synchronized void removeUser(String user) {
        this.users.remove(user);
    }
}
