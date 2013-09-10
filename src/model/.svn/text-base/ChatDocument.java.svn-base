package model;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class ChatDocument extends DefaultStyledDocument {
    /**
     * Model for the conversation text.
     */
    private static final long serialVersionUID = 1L;

    public ChatDocument() {
        addStylesToDocument();
    }
    
    /**
     * Add a chat message to the document.
     * @param msg 
     * @throws BadLocationException
     */
    public void addChatMsg(ChatMessage msg) throws BadLocationException {
        String user = msg.getSender();
        String message = msg.getMessage();
        
        this.insertString(this.getLength(), user+": ", this.getStyle("bold"));
        this.insertString(this.getLength(), message+"\n", this.getStyle("regular"));
    }
    
    /**
     * Add a chat notification to the document.
     * @param msg
     *      e.g.
     *          "UserA added UserB to this conversation."
     *          "UserA left this conversation."
     *          etc.
     * @throws BadLocationException
     */
    public void addSystemMsg(String msg) throws BadLocationException {
    	this.insertString(this.getLength(), msg+"\n", this.getStyle("regular"));
    }
    
    protected void addStylesToDocument() {
        //Initialize some styles.
        Style def = StyleContext.getDefaultStyleContext().
                        getStyle(StyleContext.DEFAULT_STYLE);
 
        Style regular = this.addStyle("regular", def);
        StyleConstants.setFontFamily(def, "SansSerif");
 
        Style s = this.addStyle("italic", regular);
        StyleConstants.setItalic(s, true);
 
        s = this.addStyle("bold", regular);
        StyleConstants.setBold(s, true);
    }
}
