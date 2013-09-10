package gui;

import java.awt.Toolkit;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import main.Client;
import model.Adapter;
import model.User;
import model.UserListModel;

public class BuddyList extends JList {
    private static final long serialVersionUID = 1L;
    private final ListCellRenderer cellRenderer = new BuddyCellRenderer(new BuddyAdapter());
    private final UserListModel model = new UserListModel();
    private final HomeTab myHomeTab;
    private static ChatTabbedPane myChatPane;
    private String selectedBuddy;
    
    public BuddyList(HomeTab hometab) {
        myHomeTab = hometab;
        setCellRenderer(cellRenderer);
        setModel(model);
        
        Toolkit.getDefaultToolkit().setDynamicLayout(true);
    }
    
    public void setClient(Client client) {
        myChatPane = myHomeTab.getClient().getChatGUI().getChatcard().getChatPane();
        final BuddyList list = this;
        getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                System.out.println("buddy selected valueChanged");
                if (!e.getValueIsAdjusting()) {
                    int index = list.getSelectedIndex();
                    if (index == -1){
                        myChatPane.setNoUserSelected();
                    } else {
                        selectedBuddy = ((User)model.get(index)).getName();
                        myChatPane.setSelectedUser(selectedBuddy);
                    }
                }
            }
        });
    }
    
    public UserListModel getListModel(){
        return model;
    }
    
    static class BuddyAdapter extends Adapter {
        private final Map<URL, ImageIcon> iconCache = new HashMap<URL, ImageIcon>();
        
        private User getBuddy() { 
            return (User)getValue();
        }
        
        public String getName() { 
            return getBuddy().getName(); 
        }
        
        public OnlineStatus getOnlineStatus() { 
            return getBuddy().getOnlineStatus(); 
        }
        
        public String getMessage() { 
            return getBuddy().getMessage(); 
        }
        
        public ImageIcon getBuddyIcon() { 
            URL url = getBuddy().getUserIconURL();
            if (url == null) { 
                return null; 
            }
            ImageIcon icon = iconCache.get(url);
            if (icon != null) {
                return icon;
            }
            icon = new ImageIcon(url);
            iconCache.put(url, icon);
            return icon;
        }
    }
    
    public String getSelectedBuddy() {
        return selectedBuddy;
    }

}