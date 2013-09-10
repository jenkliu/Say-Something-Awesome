package gui;

import java.awt.Toolkit;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import model.Adapter;
import model.User;
import model.UserListModel;

public class RoomMemberList extends JList{
    private static final long serialVersionUID = 1L;
    private final ListCellRenderer cellRenderer = new RoomMemberCellRenderer(new MemberAdapter());
    private final UserListModel model = new UserListModel();
    
    public RoomMemberList() {
        setCellRenderer(cellRenderer);
        setModel(model);

        Toolkit.getDefaultToolkit().setDynamicLayout(true);
    }
    
    static class MemberAdapter extends Adapter {
        private final Map<URL, ImageIcon> iconCache = new HashMap<URL, ImageIcon>();
        
        private User getMember() { 
            return (User)getValue(); 
        }
        
        public String getName() { 
            return getMember().getName(); 
        }
        
        public TypingStatus getTypingStatus() { 
            return getMember().getTypingStatus(); 
        }
        
        public ImageIcon getMemberIcon() { 
            URL url = getMember().getUserIconURL();
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
    
    public UserListModel getModel(){
        return model;
    }
}
