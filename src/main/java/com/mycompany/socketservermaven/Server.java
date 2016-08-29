
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author HienPK
 */
@ServerEndpoint("/echo") 
public class Server {
    private static final ArrayList<Session> sessions = new ArrayList<>();
    @OnOpen
    public void onOpen(Session session){
        System.out.println(session.getId() + " has opened a connection"); 
        try {
            session.getBasicRemote().sendText("Connection Established");
            session.getBasicRemote().sendText("Welcome client " + session.getId());
            sendToAll("Client " + session.getId() + " joined");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        sessions.add(session);
    }
 
    /**
     * When a user sends a message to the server, this method will intercept the message
     * and allow us to react to it. For now the message is read as a String.
     */
    @OnMessage
    public void onMessage(String message, Session session){
        System.out.println("Message from " + session.getId() + ": " + message);
       
        sendToAll("Client " + session.getId() + ": " + message);
    }
 
    /**
     * The user closes the connection.
     * 
     * Note: you can't send messages to the client from this method
     */
    @OnClose
    public void onClose(Session session){
        System.out.println("Session " +session.getId()+" has ended");
    }
    
    private void sendToAll(String message)
    {
        for(Session session : sessions)
        {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
