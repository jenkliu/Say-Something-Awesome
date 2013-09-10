package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import model.Adapter;

public class RoomMemberCellRenderer extends JPanel implements ListCellRenderer{
    
    private static final long serialVersionUID = 1L;
    
    private Adapter binding;
    
    private final Border noFocusBorder;
    
    private ImageIcon defaultMemberIcon;
    private final Map<ImageIcon, ImageIcon> memberIcons;
    private JLabel statusLabel;
    private JLabel nameLabel;
    private JLabel memberLabel;
    private Dimension memberIconSize = new Dimension(32, 32);

    
    public Component getListCellRendererComponent(JList jList, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        //setComponentOrientation(jList.getComponentOrientation());
        setBorder(noFocusBorder);
        Adapter adapter = getAdapter(value);
        
        switch (adapter.getTypingStatus()) {
            case IS_TYPING: statusLabel.setText("is typing..."); break;
            case HAS_ENTERED_TEXT: statusLabel.setText("has entered text."); break;
            default: statusLabel.setText("");
        }
        statusLabel.setBackground(getBackground()); 
        
        String name = adapter.getName();
        nameLabel.setText(name);
        nameLabel.setForeground(getForeground());
        
        memberLabel.setIcon(scaleMemberIcon(adapter.getUserIcon()));
        memberLabel.setBackground(getBackground()); 
        
        return this;
    }
    
    public Adapter getBinding() {
        return binding;
    }
    

    public Dimension getMemberIconSize() {
        return new Dimension(memberIconSize);
    }
    
    /** Cache a buddyIconSize scaled copy of adapterBuddyIcon
     */
    private ImageIcon scaleMemberIcon(ImageIcon memberIcon) {
        if (memberIcon == null) {
            return defaultMemberIcon;
        }
        else {
            ImageIcon buddyIcon = memberIcons.get(memberIcon);
            if (buddyIcon != null) {
                return buddyIcon;
            } else {
                Dimension maxIconSize = getMemberIconSize();
                int iconWidth = memberIcon.getIconWidth();
                int iconHeight = memberIcon.getIconHeight();
                if ((iconWidth > maxIconSize.width) || (iconHeight > maxIconSize.height)) {
                    double xScale = maxIconSize.getWidth() / (double)iconWidth;
                    double yScale = maxIconSize.getHeight() / (double)iconHeight;
                    double scale = Math.min(xScale, yScale);
                    int scaledWidth = (int)(scale * (double)iconWidth);
                    int scaledHeight = (int)(scale * (double)iconHeight);
                    int flags = Image.SCALE_SMOOTH;
                    Image scaledBuddyImage = memberIcon.getImage().getScaledInstance(scaledWidth, scaledHeight, flags);
                    buddyIcon = new ImageIcon(scaledBuddyImage);
                }
                memberIcons.put(memberIcon, buddyIcon);
                return buddyIcon;
            }
        }
    }
    
    private ImageIcon loadIcon(String name) {
        // TBD deal with load errors: use a default image, log errors
        try {
            return new ImageIcon(ImageIO.read(new File("resources/"+ name)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ImageIcon();
    }
    
    public RoomMemberCellRenderer(Adapter binding) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        this.binding = binding;
        memberIcons = new HashMap<ImageIcon, ImageIcon>();
        
        noFocusBorder = new EmptyBorder(1, 1, 1, 1);
        setBorder(noFocusBorder);
        defaultMemberIcon = loadIcon("default-buddy.png");
        
        statusLabel = new JLabel("is here!");
        nameLabel = new JLabel("Name");
        memberLabel = new JLabel(defaultMemberIcon);
        
        Dimension d = statusLabel.getPreferredSize();
        statusLabel.setPreferredSize(new Dimension(d.width+60,d.height));
        
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        memberLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        add(memberLabel);
        add(nameLabel);
        add(statusLabel);
        
    }

    public RoomMemberCellRenderer() {
        this(new Adapter());
    }
    
    public Adapter getAdapter() { return binding; }
    
    public void setAdapter(Adapter binding) {
        if (binding == null) {
            throw new IllegalArgumentException("null binding");
        }
        this.binding = binding;
    }
    
    public final Adapter getAdapter(Object value) {
        Adapter adapter = getAdapter();
        adapter.setValue(value);
        return adapter;
    }
    

}
