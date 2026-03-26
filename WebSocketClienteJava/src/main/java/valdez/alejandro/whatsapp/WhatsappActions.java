package valdez.alejandro.whatsapp;

import java.util.logging.Level;
import java.util.logging.Logger;
import valdez.alejandro.endpoints.WSEndpoint;

/**
 *
 * @author janot
 */
public class WhatsappActions {
    private WSEndpoint endPoint;
    
    public WhatsappActions(WSEndpoint endPoint) {
        this.endPoint = endPoint;
    }
    
    public void enviarMsj(String msj){
        try {
            endPoint.getSesion().getBasicRemote().sendText(msj);
        } catch (Exception e) {
            Logger.getLogger(WhatsappActions.class.getName()).log(Level.SEVERE,null, e);
        }
    }
}
