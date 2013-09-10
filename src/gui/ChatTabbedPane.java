package gui;

import java.awt.Component;

import javax.swing.JTabbedPane;

public class ChatTabbedPane extends JTabbedPane{
    private static final long serialVersionUID = 1L;
    
    
    public ChatTabbedPane(){
        setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
        
    }
    public void addChatTab(String title, Component component){
        super.addTab(title, component);
    }
    
    public void setSelectedUser(String user) {
        for (int i = 0; i < this.getTabCount(); i++) {
            Tab thisTab = (Tab) this.getComponentAt(i);
            thisTab.setSelectedUser(user);
        }
    }
    
    public void setNoUserSelected() {
        for (int i = 0; i < this.getTabCount(); i++) {
            Tab thisTab = (Tab) this.getComponentAt(i);
            thisTab.setNoUserSelected();
        }
    }
    
}
