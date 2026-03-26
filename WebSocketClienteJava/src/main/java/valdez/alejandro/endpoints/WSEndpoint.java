package valdez.alejandro.endpoints;

import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;
import java.net.URI;

/**
 *
 * @author janot
 */
@ClientEndpoint
public class WSEndpoint {
    private URI serverURI;
    private Session sesion;
    
    public WSEndpoint(){
        serverURI = URI.create("ws://localhost:8080/WebsocketTomee9/chat");
        try {
            WebSocketContainer container =  ContainerProvider.getWebSocketContainer();
            sesion = container.connectToServer(this,serverURI);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Session getSesion() {
        return sesion;
    }
    
}
