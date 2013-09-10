package model;

import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

public class UpdateTypingStatusWorker extends SwingWorker<Integer, Void>{
    private final UserListModel model;
    private final String user;
    private final Adapter.TypingStatus status;
    
    public UpdateTypingStatusWorker(UserListModel model, String user, Adapter.TypingStatus status) {
        this.model = model;
        this.user = user;
        this.status = status;
    }
    
    @Override
    protected Integer doInBackground() throws Exception {
        for (int i=0; i< model.getSize(); i++) {
            if (((User)model.get(i)).getName().equals(user)) {
                ((User)model.get(i)).setTypingStatus(status);
                return i;
            }
        }
        return null;
    }
    
    @Override
    protected void done() {
        try {
            User changedUser = (User)model.get(get());
            model.setElementAt(changedUser, get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        
    }
    
}
