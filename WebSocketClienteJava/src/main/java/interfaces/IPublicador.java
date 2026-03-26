/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

/**
 *
 * @author Juan Pablo Heras 
 */
public interface IPublicador {
        
    /**
     * Método que permite suscribirse a un objeto que quiere observar a este Publicador.
     * @param suscriptor Objeto que implementa la interfaz ISuscriptor, representa a un
     * observador de este Publicador.
     */
    public abstract void suscribirse(ISuscriptor suscriptor);
    
    /**
     * Método que permite a un objeto suscriptor desuscribirse para ya no observar a este Publicador.
     * @param suscriptor Objeto que implementa la interfaz ISuscriptor, representa a un
     * observador de este Publicador.
     */
    public abstract void desuscribirse(ISuscriptor suscriptor);
    
    /**
     * Método que notifica a todos en la lista de suscriptores para que llamen a su metodo actualizar.
     */
    public abstract void notificar();
    
    
}
