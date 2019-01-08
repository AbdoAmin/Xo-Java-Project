/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverxo;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author Abdo Amin
 */
public class ChatServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new ChatServer();
    }

    public ChatServer() {
        try {
            ServerMessageImp obj = new ServerMessageImp();
            Registry reg = LocateRegistry.createRegistry(1099);
            reg.rebind("GameService", obj);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
