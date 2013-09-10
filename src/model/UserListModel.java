package model;

import javax.swing.DefaultListModel;

public class UserListModel extends DefaultListModel {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * Check if the model contains this user.
     * 
     * @param username
     *          of user we are looking for
     * @return
     *      true if model contains user
     */
    public boolean contains(String username) {
        for (int i=0; i<this.getSize(); i++) {
            if (((User)get(i)).getName().equals(username)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Change the specified user's status in the model.
     *        
     * @param username
     *          of user whose status is being changed
     * @param newStatus
     *          user's new typing status.
     *          
     */
    public void changeTypingStatus(String username, Adapter.TypingStatus status) {
        (new UpdateTypingStatusWorker(this, username, status)).execute();
    }
    
    /**
     * Change the specified user's online status in the model.
     *        
     * @param username
     *          of user whose status is being changed
     * @param newStatus
     *          user's new online status.
     *          
     */
    public void changeOnlineStatus(String username, Adapter.OnlineStatus status) {
        for (int i=0; i<this.getSize(); i++) {
            if (((User)get(i)).getName().equals(username)) {
                User member = (User)get(i);
                member.setOnlineStatus(status);
            }
        }
    }
}
