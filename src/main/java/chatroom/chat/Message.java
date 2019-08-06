package chatroom.chat;

/**
 * WebSocket message model
 */
public class Message {
  public enum MessageType {
    ENTER, SPEAK, LEAVE;
  }

  private MessageType type;
  private String msg;
  private String username;
  private int onlineCount;

  public int getOnlineCount() {
    return onlineCount;
  }

  public void setOnlineCount(int onlineCount) {
    this.onlineCount = onlineCount;
  }

  public MessageType getType() {
    return type;
  }

  public void setType(MessageType type) {
    this.type = type;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

}
