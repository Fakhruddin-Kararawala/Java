package chatroom.chat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.net.URISyntaxException;

import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import javax.websocket.server.ServerEndpoint;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
/**
 * WebSocket Server
 *
 * @see ServerEndpoint WebSocket Client
 * @see Session   WebSocket Session
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class WebSocketChatServerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	URI uri;

    @Before
    public void before() throws URISyntaxException {
        uri = new URI("ws://localhost:8080/chat");
    }
    
    @Test
    public void testLogin() throws Exception{
  	  mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk());
    }
    
    @Test
    public void testIndex() throws Exception{
  	  mockMvc.perform(get("/index"))
                .andDo(print())
                .andExpect(status().isOk());
    }  

    @Test
    public void testGet() throws Exception {
		WebSocketChatClient socket = new WebSocketChatClient();
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		container.connectToServer(socket, uri);

		socket.getLatch().await();
		String s = "{\"message\" : \"Hello\"}";
		for (int i = 0; i < 10; i++) {
			socket.sendMessage(s);
		}
    }
}
