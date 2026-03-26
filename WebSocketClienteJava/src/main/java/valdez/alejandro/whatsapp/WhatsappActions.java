package valdez.alejandro.whatsapp;

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
    
    public WhatsappActions(WSEndpoint endPoint) {
        this.endPoint = endPoint;
    }
    
    public void enviarMsjGrupal(Message message){

    }
    
    public void enviarMsjParaUsuarioEspecifico(User user, Message message){
        
    }
    
    public List<User> obtenerListaUsuariosConectados(){
        return new ArrayList();
    }
}
