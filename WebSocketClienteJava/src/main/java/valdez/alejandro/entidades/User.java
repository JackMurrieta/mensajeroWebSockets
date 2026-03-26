package valdez.alejandro.entidades;

import jakarta.websocket.Session;

/**
 *
 * @author janot
 */
public class User {
    public String username;
    public Session session;
    
     public User( String username,Session session) {
        this.session = session;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public Session getSession() {
        return session;
    }

}
