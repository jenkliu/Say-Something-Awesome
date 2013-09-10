package model;

import static org.junit.Assert.*;

import javax.swing.text.BadLocationException;

import org.junit.Test;

public class ChatDocumentTest {
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false;
    }
    
    @Test
    public void addMessageTest() throws BadLocationException {
        ChatDocument cd = new ChatDocument();
        cd.addChatMsg(new ChatMessage("Jen", "hey there!"));
        String text = cd.getText(0, cd.getLength());
        assertEquals(text, "Jen: hey there!\n");
        
        cd.addChatMsg(new ChatMessage("Derek", "yo"));
        text = cd.getText(0, cd.getLength());
        assertEquals(text, "Jen: hey there!\nDerek: yo\n");        
    }
}
