package chatroom.chat;

import java.net.URI;

import javax.websocket.server.ServerContainer;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.java_websocket.server.WebSocketServer;

public class EmbeddedJettyServer {

    private final int port;
    private Server server;

    public EmbeddedJettyServer(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8080);
        server.addConnector(connector);

        // Setup the basic application "context" for this application at "/"
        // This is also known as the handler tree (in jetty speak)
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        try {
            // Initialize javax.websocket layer
            ServerContainer wscontainer = WebSocketServerContainerInitializer.configureContext(context);

            // Add WebSocket endpoint to javax.websocket layer
            wscontainer.addEndpoint(WebSocketServer.class);
            System.out.println("Begin start");
            server.start();
            server.dump(System.err);
            server.join();


        } catch (Throwable t) {
            t.printStackTrace(System.err);
        }
    }

    public void stop() throws Exception {
        server.stop();
    }

    public URI getWebsocketUri(Class<WebSocketServer> class1) {
        return  server.getURI();
    }
}
