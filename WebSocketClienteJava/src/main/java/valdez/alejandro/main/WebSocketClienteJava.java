package valdez.alejandro.main;

import valdez.alejandro.endpoints.WSEndpoint;
import valdez.alejandro.whatsapp.WhatsappActions;
import views.ChatLobby;

/**
 *
 * @author janot
 */
public class WebSocketClienteJava {

    public static void main(String[] args) {
        WSEndpoint wsEndPoint = new WSEndpoint();
        
        WhatsappActions whaa = new WhatsappActions(wsEndPoint);
        
        
    }
}
