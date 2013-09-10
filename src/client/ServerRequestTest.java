package client;

import static org.junit.Assert.*;

import org.junit.Test;

public class ServerRequestTest {
    
    @Test
    public void testMsgRequest() {
        String req = "msg 42 jen you're awesome";
        ServerRequest sr = new ServerRequest(req);
        assertEquals(sr.getArgs().get(1), "42");
        assertEquals(sr.getArgs().get(2), "jen");
        assertEquals(sr.getArgs().get(3), "you're awesome");
        assertEquals(ServerRequest.ServerRequestType.Message, sr.getType());
    }
    
    @Test
    public void testUserListRequest() {
        String req = "userList jen derek ranna";
        ServerRequest sr = new ServerRequest(req);
        assertEquals(sr.getArgs().get(1), "jen");
        assertEquals(sr.getArgs().get(2), "derek");
        assertEquals(sr.getArgs().get(3), "ranna");
        assertEquals(ServerRequest.ServerRequestType.UserList, sr.getType());
    }
    
    @Test
    public void testGetUserList() {
        String req = "userList jen derek ranna";
        ServerRequest sr = new ServerRequest(req);
        String[] users= sr.getUserList();
        assertEquals(3, users.length);
        assertEquals(users[0], "jen");
        assertEquals(users[1], "derek");
        assertEquals(users[2], "ranna");
    }
    
    @Test
    public void testLogOnUser() {
        String req = "logOnUser joe";
        ServerRequest sr = new ServerRequest(req);
        assertEquals(sr.getArgs().get(1), "joe");
        assertEquals(ServerRequest.ServerRequestType.LogOnUser, sr.getType());
    }
    
    @Test
    public void testLogOffUser() {
        String req = "logOffUser ranna";
        ServerRequest sr = new ServerRequest(req);
        assertEquals(ServerRequest.ServerRequestType.LogOffUser, sr.getType());
    }
    
    @Test
    public void testAddUser() {
        String req = "addUser 3 jen derek";
        ServerRequest sr = new ServerRequest(req);
        assertEquals(sr.getArgs().get(1), "3");
        assertEquals(sr.getArgs().get(2), "jen");
        assertEquals(sr.getArgs().get(3), "derek");
        assertEquals(ServerRequest.ServerRequestType.AddUser, sr.getType());
    }
    
    @Test
    public void testRemoveUser() {
        String req = "removeUser 8 jen";
        ServerRequest sr = new ServerRequest(req);
        assertEquals(sr.getArgs().get(1), "8");
        assertEquals(sr.getArgs().get(2), "jen");
        assertEquals(ServerRequest.ServerRequestType.RemoveUser, sr.getType());
    }
    
    @Test
    public void testConflict() {
        String req = "conflict bill";
        ServerRequest sr = new ServerRequest(req);
        assertEquals(sr.getArgs().get(1), "bill");
    }
    
    @Test
    public void testAccepted() {
        String req = "accepted bill";
        ServerRequest sr = new ServerRequest(req);
        assertEquals(sr.getArgs().get(1), "bill");
    }
    
    @Test
    public void testChangeUserState() {
        String req = "changeUserState 2 Jen isTyping";
        ServerRequest sr = new ServerRequest(req);
        assertEquals(sr.getArgs().get(1), "2");
        assertEquals(sr.getArgs().get(2), "Jen");
        assertEquals(sr.getArgs().get(3), "isTyping");
        assertEquals(ServerRequest.ServerRequestType.ChangeUserState, sr.getType());
    }
    
    @Test
    public void testNewConvo() {
        String req = "newConvo 4 Jen Ranna";
        ServerRequest sr = new ServerRequest(req);
        assertEquals(sr.getArgs().get(1), "4");
        assertEquals(sr.getArgs().get(2), "Jen");
        assertEquals(sr.getArgs().get(3), "Ranna");
        assertEquals(ServerRequest.ServerRequestType.NewConvo, sr.getType());
    }
    
    @Test
    public void testGetMemberList() {
        String req = "memberList 4 jen derek ranna";
        ServerRequest sr = new ServerRequest(req);
        String[] members= sr.getMemberList();
        assertEquals(3, members.length);
        assertEquals(members[0], "jen");
        assertEquals(members[1], "derek");
        assertEquals(members[2], "ranna");
    }
    
    @Test
    public void testMemberListRequest() {
        String req = "memberList 4 jen derek ranna";
        ServerRequest sr = new ServerRequest(req);
        assertEquals(sr.getArgs().get(1), "4");
        assertEquals(sr.getArgs().get(2), "jen");
        assertEquals(sr.getArgs().get(3), "derek");
        assertEquals(sr.getArgs().get(4), "ranna");
        assertEquals(ServerRequest.ServerRequestType.MemberList, sr.getType());
    }
}
