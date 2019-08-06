package chatroom.chat;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.alibaba.fastjson.JSON;

public class MessageEncoder implements Encoder.Text<Message> {
  @Override
  public String encode(Message message) throws EncodeException {
    return JSON.toJSONString(message);
  }

  @Override
  public void init(EndpointConfig endpointConfig) {
    // Custom initialization logic
  }

  @Override
  public void destroy() {
    // Close resources
  }
}
