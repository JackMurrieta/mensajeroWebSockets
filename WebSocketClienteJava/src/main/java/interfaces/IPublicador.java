/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import java.util.List;
import valdez.alejandro.entidades.Message;
import valdez.alejandro.entidades.User;

/**
 *
 * @author Juan Pablo Heras 
 */
public interface IPublicador {
        
    void notificarMensaje(Message m);
    
    void notificarListaActualizada();
    
    void notificarError(String err);
    
    List<User> obtenerListaUsuariosConectados();
    
}
