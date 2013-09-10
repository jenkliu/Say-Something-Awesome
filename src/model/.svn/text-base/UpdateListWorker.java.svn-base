package model;

import java.util.List;

import javax.swing.SwingWorker;

public class UpdateListWorker  extends SwingWorker<Void, String>{
    public enum UpdateType { ADD, REMOVE }
    
    private final UserListModel model;
    private final String[] users;
    private final UpdateType type;
    
    public UpdateListWorker(UserListModel model, String[] users, UpdateType type) {
        this.model = model;
        this.users = users;
        this.type = type;
    }
    
    @Override
    protected Void doInBackground() throws Exception {
        for (String user: users) {
            publish(user);
        }
        return null;
    }
    
    @Override
    protected void process(List<String> chunks) {
        for (String user: chunks) {
            if (type == UpdateType.ADD){
                model.addElement(new User(user, Adapter.OnlineStatus.ONLINE, "I'm online.", "default-buddy.png"));
            } else if (type == UpdateType.REMOVE){
                for (int i = 0; i < model.getSize(); i++){
                    if (((User)model.get(i)).getName().equals(user)){
                        model.remove(i);
                    }
                }
            }
        }
    }
}