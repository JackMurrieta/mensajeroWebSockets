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
 * @author juanpheras
 */
public interface ISuscriptor {
    
    void onMensajeRecibido(Message mensaje);
    void onListaUsuariosActualizada(List<User> usuarios);
    void onError(String error);
    
}
