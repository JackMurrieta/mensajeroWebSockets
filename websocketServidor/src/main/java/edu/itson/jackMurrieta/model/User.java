package edu.itson.jackMurrieta.model;

import jakarta.websocket.Session;

public class User {
    public String username;
    public Session session;

    public User( String username,Session session) {
        this.session = session;
        this.username = username;
    }
}
