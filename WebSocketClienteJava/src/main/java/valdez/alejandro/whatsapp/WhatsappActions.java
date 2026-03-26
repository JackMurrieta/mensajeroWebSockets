package valdez.alejandro.whatsapp;

import com.google.gson.Gson;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import valdez.alejandro.endpoints.WSEndpoint;
import valdez.alejandro.entidades.Message;
import valdez.alejandro.entidades.User;

/**
 *
 * @author janot
 */
public class WhatsappActions {
    private WSEndpoint endPoint;
    private final String CONECTAR_CON_SERVER = "/chat";
    
    public WhatsappActions(WSEndpoint endPoint) {
        this.endPoint = endPoint;
    }
    
    public void enviarMsj(Message message){
        try {
            Session session = endPoint.getSesion();
            String json = convertirAFormatoJSON(message);
            session.getBasicRemote().sendText(json);
        } catch (Exception e) {
            System.err.println("Error al enviar el mensaje grupal: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public List<User> obtenerListaUsuariosConectados(){
        return new ArrayList();
    }
    
    private String convertirAFormatoJSON(Message message){
        Gson gson = new Gson();
        return gson.toJson(message);
    }
}
