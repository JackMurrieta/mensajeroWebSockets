package edu.itson.jackMurrieta.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.itson.jackMurrieta.model.Message;
import edu.itson.jackMurrieta.model.MessageType;
import edu.itson.jackMurrieta.model.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.websocket.Session;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


//logica servidor
@ApplicationScoped
public class ChatService {

    private Map<String, User> users = new ConcurrentHashMap<>();
    private Map<Session, String> sessionUserMap = new ConcurrentHashMap<>();

    private ObjectMapper mapper = new ObjectMapper();

    //Agregar usuario
    public void addUser(String username, String password, Session session) {

        if (users.containsKey(username)) {
            sendError(session, "El usuario ya existe");
            return;
        }

        User user = new User(username, session);
        users.put(username, user);
        sessionUserMap.put(session, username);

        broadcastSystemMessage(username + " se ha conectado");
        broadcastUserList();
    }

    //Remover usuario
    public void removeUser(Session session) {
        String username = sessionUserMap.get(session);

        if (username != null) {
            users.remove(username);
            sessionUserMap.remove(session);

            broadcastSystemMessage(username + " se ha desconectado");
            broadcastUserList();
        }
    }

    //Broadcast general
    public void broadcast(String message) {
        users.values().forEach(user ->
                user.session.getAsyncRemote().sendText(message)
        );
    }

    //Mensaje privado
    public void sendPrivate(String from, String to, String content) {
        User recipient = users.get(to);

        if (recipient != null && recipient.session.isOpen()) {
            try {
                // Creamos un objeto de respuesta tipo PRIVATE
                Message privateMsg = new Message();
                privateMsg.type = MessageType.PRIVATE;
                privateMsg.from = from; // Quién lo envió originalmente
                privateMsg.content = content;

                String json = mapper.writeValueAsString(privateMsg);
                recipient.session.getAsyncRemote().sendText(json);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Opcional: Avisar al emisor que el destinatario no está conectado
        }
    }

    //Lista de usuarios
    public void broadcastUserList() {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("type", "USER_LIST");
            response.put("users", users.keySet());

            String json = mapper.writeValueAsString(response);

            users.values().forEach(user ->
                    user.session.getAsyncRemote().sendText(json)
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Obtener username desde sesión
    public String getUsername(Session session) {
        return sessionUserMap.get(session);
    }

    //Mensaje sistema
    private void broadcastSystemMessage(String message) {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("type", "SYSTEM");
            response.put("content", message);

            String json = mapper.writeValueAsString(response);

            broadcast(json);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Error
    private void sendError(Session session, String error) {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("type", "ERROR");
            response.put("content", error);

            session.getAsyncRemote().sendText(
                    mapper.writeValueAsString(response)
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}