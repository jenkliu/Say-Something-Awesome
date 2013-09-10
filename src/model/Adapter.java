package model;

import javax.swing.ImageIcon;

/** 
 * Map from application-specific User object to the properties needed
 * for display.  Typically one would override all of the get
 * methods except getValue().  They all return a placeholder 
 * string/icon/Status by default.
 */
public class Adapter {
    public enum OnlineStatus {ONLINE, OFFLINE, AWAY};
    public enum TypingStatus {IS_TYPING, HAS_ENTERED_TEXT, NO_TEXT};
    
    private Object value;
    
    public Object getValue() { return value; }
    
    public void setValue(Object value) { 
        this.value = value; 
    }
    
    public String getName() { return "Username"; }
    
    public OnlineStatus getOnlineStatus() { return OnlineStatus.ONLINE; }
    
    public TypingStatus getTypingStatus() { return TypingStatus.NO_TEXT; }
    
    public String getMessage() { return "I'm online!"; }
    
    public ImageIcon getUserIcon() { return null; }
    
}
