package model;

import java.net.URL;

public class User {
    private final String name;
    private Adapter.OnlineStatus onlineStatus;
    private Adapter.TypingStatus typingStatus = Adapter.TypingStatus.NO_TEXT;
    private String message;
    private final URL userIconURL;
    
    public User(String name, Adapter.OnlineStatus status, String message, String buddyIcon) {
        this.name = name;
        this.setOnlineStatus(status);
        this.message = message;
        this.userIconURL = (buddyIcon == null) ? null : 
                this.getClass().getResource("/resources/sample-buddy-icons/" + buddyIcon);
    }
    
    public User(String name) {
        this.name = name;
        this.setOnlineStatus(Adapter.OnlineStatus.ONLINE);
        this.message = "I'm online";
        this.userIconURL = this.getClass().getResource("/resources/sample-buddy-icons/" + "default-buddy.png");
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equalsIgnoreCase(other.name))
            return false;
        return true;
    }
    
    public String getName(){
        return name;
    }

    public Adapter.OnlineStatus getOnlineStatus() {
        return onlineStatus;
    }
    
    public void setOnlineStatus(Adapter.OnlineStatus onlineStatus) {
        this.onlineStatus = onlineStatus;
    }
    
    public Adapter.TypingStatus getTypingStatus() {
        return typingStatus;
    }

    public void setTypingStatus(Adapter.TypingStatus typingStatus) {
        this.typingStatus = typingStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public URL getUserIconURL() {
        return userIconURL;
    }
}
