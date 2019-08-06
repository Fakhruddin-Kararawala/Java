package chatroom;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@SpringBootApplication
@RestController
public class WebSocketChatApplication {

  public static void main(String[] args) {
    SpringApplication.run(WebSocketChatApplication.class, args);
  }

  /**
   * Login Page
   */
  @GetMapping("/")
  public ModelAndView login() {
    return new ModelAndView("/login");
  }

  /**
   * Chatroom Page
   */
  @GetMapping("/index")
  public ModelAndView index(String username, HttpServletRequest request) {
    ModelAndView mav = new ModelAndView("/chat");
    mav.addObject("username", username);
    mav.addObject("webSocketUrl", "ws://localhost:8080/chat");
    return mav;
  }
}
