package valdez.alejandro.endpoints;

import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.OnMessage;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;
import java.net.URI;
import valdez.alejandro.whatsapp.WhatsappActions;

/**
 *
 * @author janot
 */
@ClientEndpoint
public class WSEndpoint {
    private URI serverURI;
    private Session sesion;
    private WhatsappActions actions;
    
    public WSEndpoint(){
        serverURI = URI.create("wss://uneffaced-nonregimented-karlie.ngrok-free.dev/chat");
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

    public void setActions(WhatsappActions actions) {
        this.actions = actions;
    }
    
    
    @OnMessage
    public void onMessage(String mensaje) {
        if (actions != null) {
            actions.recibirMensaje(mensaje);
        }
    }
    
}
