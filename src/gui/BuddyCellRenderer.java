package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.GrayFilter;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import model.Adapter;


/**
 * This is the ListCellRenderer class for each Buddy in the BuddyList.
 */
public class BuddyCellRenderer extends JPanel implements ListCellRenderer {
    private static final long serialVersionUID = 1L;
    private Adapter binding;
    private final Border noFocusBorder;
    private Color evenRowColor;
    private Color oddRowColor;
    private Color messageColor;
    private Font nameLabelFont;
    private Icon blankStatusIcon;
    private Icon redStatusIcon;
    private Icon greenStatusIcon;
    private ImageIcon defaultBuddyIcon;
    private ImageIcon defaultOfflineBuddyIcon;
    private final Map<ImageIcon, ImageIcon> onlineBuddyIcons;
    private final Map<ImageIcon, ImageIcon> offlineBuddyIcons;
    private JLabel statusLabel;
    private JLabel nameLabel;
    private JLabel messageLabel;
    private JLabel buddyLabel;
    private Dimension buddyIconSize = new Dimension(32, 32);
    private final Insets zeroInsets = new Insets(0, 0, 0, 0);
    
    public Component getListCellRendererComponent(JList jList, Object value, int index, boolean isSelected, boolean hasFocus) {
        setComponentOrientation(jList.getComponentOrientation());

    	if (isSelected) {
    	    setBackground(jList.getSelectionBackground());
    	    setForeground(jList.getSelectionForeground());
    	} else {
    	    setBackground(((index & 0x1) == 1) ? evenRowColor : oddRowColor);
    	    setForeground(jList.getForeground());
    	}
    
        Border border = null;
        if (hasFocus) {
            if (isSelected) {
                border = UIManager.getBorder("List.focusSelectedCellHighlightBorder");
            }
            if (border == null) {
                border = UIManager.getBorder("List.focusCellHighlightBorder");
            }
        } else {
            border = noFocusBorder;
        }
    	setBorder(border);
    
    	Adapter adapter = getAdapter(value);
    	
    	switch (adapter.getOnlineStatus()) {
    	    case ONLINE: statusLabel.setIcon(greenStatusIcon); break;
    	    case AWAY: statusLabel.setIcon(redStatusIcon); break;
    	    default: statusLabel.setIcon(blankStatusIcon);
    	}
    	statusLabel.setBackground(getBackground());  // see BlankIcon.paintIcon()
    
    	String name = adapter.getName();
    	nameLabel.setText((name == null) ? "" : name);
    	nameLabel.setForeground(getForeground());
    	nameLabel.setEnabled(adapter.getOnlineStatus() != Adapter.OnlineStatus.OFFLINE);
    	
    	/* If there's no message, or we're offline, then hide
    	 * the message JLabel and (vertically) center the name JLabel
    	 */
    	String msg = adapter.getMessage();
    	msg = (msg != null) ? msg.trim() : "";
    	if ((msg.length() == 0) || (adapter.getOnlineStatus() == Adapter.OnlineStatus.OFFLINE)) {
    	    messageLabel.setVisible(false);
    	    GridBagLayout l = (GridBagLayout)getLayout();
    	    GridBagConstraints c = l.getConstraints(nameLabel);
    	    c.gridheight = 2;
    	    l.setConstraints(nameLabel, c);
    	}
    	else {
    	    GridBagLayout l = (GridBagLayout)getLayout();
    	    GridBagConstraints c =l.getConstraints(nameLabel);
    	    c.gridheight = 1;
    	    l.setConstraints(nameLabel, c);
    	    messageLabel.setVisible(true);
    	    messageLabel.setText(msg);
    	}
    	
    	Icon buddyIcon = (adapter.getOnlineStatus() == Adapter.OnlineStatus.OFFLINE) ?
    	    offlineBuddyIcon(adapter.getUserIcon()) :
    	    onlineBuddyIcon(adapter.getUserIcon()) ;
    	buddyLabel.setIcon(buddyIcon);
    	buddyLabel.setBackground(getBackground());  // see BlankIcon.paintIcon()
    	
    	return this;
    }
	
    public Dimension getBuddyIconSize() {
        return new Dimension(buddyIconSize);
    }
    
//    public void setBuddyIconSize(Dimension size) {
//    	if ((size == null) || (size.width < 0) || (size.height < 0)) {
//    	    throw new IllegalArgumentException("invalid size");
//    	}
//    	this.buddyIconSize = new Dimension(size);
//    }

    /** Cache a buddyIconSize scaled copy of adapterBuddyIcon
     */
    private ImageIcon onlineBuddyIcon(ImageIcon adapterBuddyIcon) {
    	if (adapterBuddyIcon == null) {
    	    return defaultBuddyIcon;
    	}
    	else {
    	    ImageIcon buddyIcon = onlineBuddyIcons.get(adapterBuddyIcon);
    	    if (buddyIcon != null) {
    	        return buddyIcon;
    	    } else {
    	        Dimension maxIconSize = getBuddyIconSize();
        		int iconWidth = adapterBuddyIcon.getIconWidth();
        		int iconHeight = adapterBuddyIcon.getIconHeight();
        		if ((iconWidth > maxIconSize.width) || (iconHeight > maxIconSize.height)) {
        		    double xScale = maxIconSize.getWidth() / (double)iconWidth;
        		    double yScale = maxIconSize.getHeight() / (double)iconHeight;
        		    double scale = Math.min(xScale, yScale);
        		    int scaledWidth = (int)(scale * (double)iconWidth);
        		    int scaledHeight = (int)(scale * (double)iconHeight);
        		    int flags = Image.SCALE_SMOOTH;
        		    Image scaledBuddyImage = adapterBuddyIcon.getImage().getScaledInstance(scaledWidth, scaledHeight, flags);
        		    buddyIcon = new ImageIcon(scaledBuddyImage);
        		}
        		onlineBuddyIcons.put(adapterBuddyIcon, buddyIcon);
        		return buddyIcon;
    	    }
    	}
    }

    private ImageIcon createOfflineBuddyIcon(ImageIcon icon) {
        return new ImageIcon(GrayFilter.createDisabledImage(icon.getImage()));
    }

    /* Cache a gray filtered copy of the onlineBuddyIcon
     */
    ImageIcon offlineBuddyIcon(ImageIcon adapterBuddyIcon) {
    	if (adapterBuddyIcon == null) {
    	    return defaultOfflineBuddyIcon;
    	}
    	else {
    	    ImageIcon buddyIcon = offlineBuddyIcons.get(adapterBuddyIcon);
    	    if (buddyIcon != null) {
    	        return buddyIcon;
    	    } else {
        		ImageIcon onlineBuddyIcon = onlineBuddyIcon(adapterBuddyIcon);
        		buddyIcon = createOfflineBuddyIcon(onlineBuddyIcon);
        		offlineBuddyIcons.put(adapterBuddyIcon, buddyIcon);
        		return buddyIcon;
    	    }
    	}
    }

    private void initGridBagConstraints(GridBagConstraints c) {
    	c.anchor = GridBagConstraints.CENTER;
    	c.fill = GridBagConstraints.NONE;
    	c.gridwidth = 1;
    	c.gridheight = 1;
    	c.gridx = GridBagConstraints.RELATIVE;
    	c.gridy = GridBagConstraints.RELATIVE;
    	c.insets = zeroInsets;
    	c.ipadx = 0;
    	c.ipady = 0;
    	c.weightx = 0.0;
    	c.weighty = 0.0;
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

    public BuddyCellRenderer(Adapter binding) {
    	super(new GridBagLayout());
    	this.binding = binding;
    	onlineBuddyIcons = new HashMap<ImageIcon, ImageIcon>();
    	offlineBuddyIcons = new HashMap<ImageIcon, ImageIcon>();
    
    	noFocusBorder = new EmptyBorder(1, 1, 1, 1);
    	setBorder(noFocusBorder);
    	evenRowColor = Color.white;
    	oddRowColor = new Color(237, 243, 255);
    	messageColor = new Color(170, 170, 170);
    	redStatusIcon = loadIcon("red-led.png");
    	greenStatusIcon = loadIcon("green-led.png");
    	blankStatusIcon = new BlankIcon(greenStatusIcon);
    	defaultBuddyIcon = loadIcon("default-buddy.png");
    	defaultOfflineBuddyIcon = createOfflineBuddyIcon(defaultBuddyIcon);
    	
    	statusLabel = new JLabel(greenStatusIcon);
    	nameLabel = new JLabel("Name");
    	messageLabel = new JLabel("one line message");
    	buddyLabel = new JLabel(defaultBuddyIcon);
    	
    	nameLabelFont = nameLabel.getFont().deriveFont(12.0f).deriveFont(Font.BOLD);
    	nameLabel.setFont(nameLabelFont);
    	messageLabel.setForeground(messageColor);
    
    	GridBagConstraints c = new GridBagConstraints();
    	GridBagLayout l = (GridBagLayout)getLayout();
    	
    	initGridBagConstraints(c);
    	c.gridheight = 2;
    	c.insets = new Insets(0, 6, 0, 7); // top, left, bottom, right;
    	add(statusLabel, c);
    
    	initGridBagConstraints(c);
    	c.fill = GridBagConstraints.HORIZONTAL;
    	c.weightx = 1.0;
    	c.ipadx = 10;
    	c.insets = new Insets(4, 0, 0, 0); // top, left, bottom, right;
    	add(nameLabel, c);
    
    	initGridBagConstraints(c);
    	c.fill = GridBagConstraints.HORIZONTAL;
    	c.weightx = 1.0;
    	c.gridx = 1;
    	c.gridy = 1;
    	c.ipadx = 15;
    	c.insets = new Insets(0, 0, 4, 0); // top, left, bottom, right;
    	add(messageLabel, c);
    
    	initGridBagConstraints(c);
    	c.gridheight = 2;
    	c.insets = new Insets(0, 0, 0, 10); // top, left, bottom, right;
    	add(buddyLabel, c);
    }

    public BuddyCellRenderer() {
        this(new Adapter());
    }

    private static class BlankIcon implements Icon {
    	private final int width, height;
    	
    	public BlankIcon(Icon icon) {
    	    this.width = icon.getIconWidth();
    	    this.height = icon.getIconHeight();
    	}
    	
    	public int getIconWidth() { return width; }
    	
    	public int getIconHeight() { return height; }
    	
    	public void paintIcon(Component c, Graphics g, int x, int y) {
    	    g.setColor(c.getBackground());
    	    g.fillRect(x, y, getIconWidth(), getIconHeight());
    	}
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
