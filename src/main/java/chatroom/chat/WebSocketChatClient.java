package chatroom.chat;


import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CountDownLatch;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;


@ClientEndpoint
public class WebSocketChatClient {

	CountDownLatch latch = new CountDownLatch(1);
	private Session session;

	@OnOpen
	public void onOpen(Session session) {
		System.out.println("Connected to server");
		this.session = session;
		latch.countDown();
	}

	@OnMessage
	public void onText(String message, Session session) {
		System.out.println("Message received from server:" + message);
	}

	@OnClose
	public void onClose(CloseReason reason, Session session) {
		System.out.println("Closing a WebSocket due to " + reason.getReasonPhrase());
	}

	public CountDownLatch getLatch() {
		return latch;
	}

	public void sendMessage(String str) throws EncodeException {
		try {
			session.getBasicRemote().sendObject(str);
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		try {

			String dest = "ws://localhost:8080/chat";
			WebSocketChatClient socket = new WebSocketChatClient();
			WebSocketContainer container = ContainerProvider.getWebSocketContainer();
			container.connectToServer(socket, new URI(dest));

			socket.getLatch().await();
			String s = "{\"message\" : \"Hello\"}";
			for (int i = 0; i < 10; i++) {
				socket.sendMessage(s);
			}
			Thread.sleep(10000l);

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
