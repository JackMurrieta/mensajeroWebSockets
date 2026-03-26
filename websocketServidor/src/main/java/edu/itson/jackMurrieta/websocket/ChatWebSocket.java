package edu.itson.jackMurrieta.websocket;

import edu.itson.jackMurrieta.model.Message;
import edu.itson.jackMurrieta.service.ChatService;
import jakarta.inject.Inject;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

import com.fasterxml.jackson.databind.ObjectMapper;

@ServerEndpoint("/chat")
public class ChatWebSocket {

    @Inject
    ChatService chatService;

    private static ObjectMapper mapper = new ObjectMapper();

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Nueva conexión: " + session.getId());
    }

    @OnMessage
    public void onMessage(String json, Session session) {
        try {
            Message msg = mapper.readValue(json, Message.class);

            // Obtenemos el nombre real vinculado a esta sesión
            String sender = chatService.getUsername(session);

            switch (msg.type) {
                case CONNECT:
                    // En el primer mensaje, sí recibimos el nombre para registrarlo
                    chatService.addUser(msg.from, "1234", session);
                    break;

                case BROADCAST:
                    // Enviamos el mensaje a todos, inyectando el nombre del emisor real
                    if (sender != null) {
                        chatService.broadcast(sender + ": " + msg.content);
                    }
                    break;

                case PRIVATE:
                    // Enviamos privado: (De quién, Para quién, Qué dice)
                    if (sender != null && msg.to != null) {
                        chatService.sendPrivate(sender, msg.to, msg.content);
                    }
                    break;

                case USER_LIST:
                    chatService.broadcastUserList();
                    break;

                case DISCONNECT:
                    chatService.removeUser(session);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session) {
        chatService.removeUser(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }
}