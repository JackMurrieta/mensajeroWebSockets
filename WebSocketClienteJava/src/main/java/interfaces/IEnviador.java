/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import valdez.alejandro.entidades.Message;

/**
 * Interfaz para manejar el envío de mensajes.
 * @author Juan Pablo Heras
 */
public interface IEnviador {
    
    void enviarMensajeDesdeView(Message mensaje);
    
}
