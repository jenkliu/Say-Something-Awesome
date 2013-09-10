package server;

import static org.junit.Assert.*;
import org.junit.Test;

public class ClientRequestTest {

    @Test
    public void createConvoRequestTest() {
        String arg = "create Ranna Derek What's up g?";
        ClientRequest test = new ClientRequest(arg);
        assertEquals(test.getArgs().get(1), "Ranna");
        assertEquals(test.getArgs().get(2), "Derek");
        assertEquals(test.getArgs().get(3), "What's up g? ");
        assertEquals(test.getType(), ClientRequest.ClientRequestType.CreateConvo);
    }
    @Test
    public void leaveRequestTest() {
        String arg = "leave 27 BobbyJoe27";
        ClientRequest test = new ClientRequest(arg);
        assertEquals(test.getArgs().get(1), "27");
        assertEquals(test.getArgs().get(2), "BobbyJoe27");
        assertEquals(test.getType(), ClientRequest.ClientRequestType.LeaveConvo);
    }
    @Test
    public void addUserRequestTest() {
        String arg = "addUser 109 Derek Jen";
        ClientRequest test = new ClientRequest(arg);
        assertEquals(test.getArgs().get(1), "109");
        assertEquals(test.getArgs().get(2), "Derek");
        assertEquals(test.getArgs().get(3), "Jen");
        assertEquals(test.getType(), ClientRequest.ClientRequestType.AddUser);
    }
    @Test
    public void messageRequestTest() {
        String arg = "msg 47 Jen Man, that test sucked";
        ClientRequest test = new ClientRequest(arg);
        assertEquals(test.getArgs().get(1), "47");
        assertEquals(test.getArgs().get(2), "Jen");
        assertEquals(test.getArgs().get(3), "Man, that test sucked ");
        assertEquals(test.getType(), ClientRequest.ClientRequestType.Message);
    }
    
    @Test
    public void userListRequestTest() {
        String arg = "userList Ranna";
        ClientRequest test = new ClientRequest(arg);
        assertEquals(test.getArgs().get(1), "Ranna");
        assertEquals(test.getType(), ClientRequest.ClientRequestType.UserList);
    }
}
