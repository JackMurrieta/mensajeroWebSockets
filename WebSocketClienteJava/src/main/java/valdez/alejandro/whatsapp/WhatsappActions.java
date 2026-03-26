package valdez.alejandro.whatsapp;

import com.google.gson.Gson;
import interfaces.IPublicador;
import interfaces.ISuscriptor;
import jakarta.websocket.OnMessage;
import jakarta.websocket.Session;
import java.util.ArrayList;
import java.util.List;
import valdez.alejandro.endpoints.WSEndpoint;
import valdez.alejandro.entidades.Message;
import valdez.alejandro.entidades.User;

/**
 * @author janot
 */
public class WhatsappActions implements IPublicador{
private WSEndpoint endPoint;
    private Gson gson = new Gson();
    private List<User> usuariosConectados = new ArrayList<>();
    private List<ISuscriptor> observers = new ArrayList<>();

    public WhatsappActions(WSEndpoint endPoint) {
        this.endPoint = endPoint;
    }

    public void addObserver(ISuscriptor observer) {
        this.observers.add(observer);
    }

    @OnMessage
    public void recibirMensaje(String mensajeJson) {
        try {
            Message msg = gson.fromJson(mensajeJson, Message.class);

            switch (msg.getType()) {
                case CONNECT:
                    // Creamos el usuario y lo metemos a la lista local
                    User nuevoUsuario = new User(msg.getFrom(), null); 
                    usuariosConectados.add(nuevoUsuario);
                    notificarListaActualizada();
                    notificarMensaje(msg); // Opcional: avisar que "X se conectó"
                    break;

                case DISCONNECT:
                    // Buscamos y sacamos al usuario que se fue
                    usuariosConectados.removeIf(u -> u.getUsername().equals(msg.getFrom()));
                    notificarListaActualizada();
                    notificarMensaje(msg);
                    break;

                case USER_LIST:
                    // Si el server manda la lista completa (ej. en el content como JSON)
                    // Aquí podrías parsear msg.getContent() si viene como array de nombres
                    System.out.println("Lista completa recibida: " + msg.getContent());
                    break;

                case BROADCAST:
                    
                case PRIVATE:
                    // Estos solo se pintan en el chat
                    notificarMensaje(msg);
                    break;
            }

        } catch (Exception e) {
            notificarError("Error al procesar mensaje: " + e.getMessage());
        }
    }

    public void enviarMsj(Message message) {
        try {
            Session session = endPoint.getSesion();
            if (session != null && session.isOpen()) {
                session.getBasicRemote().sendText(gson.toJson(message));
            }
        } catch (Exception e) {
            notificarError("No se pudo enviar el mensaje.");
        }
    }

    @Override
    public void notificarMensaje(Message m) {
        for (ISuscriptor o : observers) {
            o.onMensajeRecibido(m);
        }
    }

    @Override
    public void notificarListaActualizada() {
        for (ISuscriptor o : observers) {
            o.onListaUsuariosActualizada(new ArrayList<>(usuariosConectados));
        }
    }

    @Override
    public void notificarError(String err) {
        for (ISuscriptor o : observers) {
            o.onError(err);
        }
    }
    @Override
    public List<User> obtenerListaUsuariosConectados() {
        return new ArrayList<>(usuariosConectados);
    }
}