package chatroom.chat;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

import chatroom.chat.Message.MessageType;

/**
 * WebSocket Server
 *
 * @see ServerEndpoint WebSocket Client
 * @see Session   WebSocket Session
 */

@Component
@ServerEndpoint(
    value="/chat", 
    decoders = MessageDecoder.class, 
    encoders = MessageEncoder.class)
public class WebSocketChatServer {

	CountDownLatch latch = new CountDownLatch(1);

	private Session session;
    
	/**
     * All chat sessions.
     */
    private static Map<String, Session> onlineSessions = new ConcurrentHashMap<>();

    private static void sendMessageToAll(Message message) {
        onlineSessions.values().forEach(s->{
          try {
            s.getBasicRemote().sendObject(message);
          } catch (IOException | EncodeException e) {
            e.printStackTrace();
          }
        });
    }

    /**
     * Open connection, 1) add session, 2) add user.
     */
    @OnOpen
    public void onOpen(Session session) {
     System.out.println("Inside onOpen method");
      onlineSessions.put(session.getId(), session);
      Message message = new Message();
      message.setOnlineCount(onlineSessions.size());
      sendMessageToAll(message);
    }

    /**
     * Send message, 1) get username and session, 2) send message to all.
     */
    @OnMessage
    public void onMessage(String str, Session session) {
     System.out.println("Inside onMessage method "+str);
      Message message = new Message();
      message.setType(MessageType.SPEAK);
      message.setOnlineCount(onlineSessions.size());
      sendMessageToAll(message);
    }

    /**
     * Close connection, 1) remove session, 2) update user.
     */
    @OnClose
    public void onClose(Session session) {
      onlineSessions.remove(session.getId());
      Message message = new Message();
      message.setOnlineCount(onlineSessions.size());
      sendMessageToAll(message);
    }

    /**
     * Print exception.
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
    
    public CountDownLatch getLatch() {
        return latch;
    }
 
    public void sendMessage(String str) {
        try {
            session.getBasicRemote().sendText(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
