package valdez.alejandro.main;

import valdez.alejandro.endpoints.WSEndpoint;
import valdez.alejandro.entidades.Message;
import valdez.alejandro.enums.MessageType;
import valdez.alejandro.whatsapp.WhatsappActions;
import views.ChatLobby;


/**
 *
 * @author janot
 */
public class WebSocketClienteJava {

    public static void main(String[] args) {
        WSEndpoint wsEndPoint = new WSEndpoint();
         
//        Message message = new Message("theBoss", "pedro", "ME PELAS LA VERGA", MessageType.PRIVATE);
        WhatsappActions whaa = new WhatsappActions(wsEndPoint);
        
        
        wsEndPoint.setActions(whaa);
        
        ChatLobby chatUI = new ChatLobby();
        
        
        chatUI.setEnviador(whaa); //Setteamos el enviador para que pueda
        whaa.addObserver(chatUI);
        
        chatUI.iniciarSesion();
        chatUI.setVisible(true);
//        whaa.enviarMsj(message);
        
        
    }
}
