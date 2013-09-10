package gui;

import javax.swing.JPanel;
/**
 * Abstract parent class of HomeTab and ChatRoomTab
 *
 */
public abstract class Tab extends JPanel {
    private static final long serialVersionUID = 1L;
    public boolean isUserSelected = false;
    
    public abstract void setSelectedUser(String user);
    
    public abstract void setNoUserSelected();
}
