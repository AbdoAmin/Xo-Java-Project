/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientxo;

import commontxo.ServerCallBack;
import commontxo.ClientCallBack;
import commontxo.GameRoom;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 *
 * @author Abdo Amin
 */
public class GameModle extends UnicastRemoteObject implements ClientCallBack {
    GameController myController;
    private ServerCallBack server;
    
    ArrayList<String/*PlayerInfo*/> onlineList;
    GameRoom gameRoom;
    ArrayList</*ChatRoom(String UserName,String Chat,ArrayList<ClientCallBack>)*/String> chatRooms;//multibale chat rooms
    
 
    
    

    GameModle(GameController myController) throws RemoteException {
        this.myController = myController;
    }

    @Override
    public void receiveMessage(String nameOfRoom,String myMessage) throws RemoteException {
        myController.displayMessage(myMessage);
    }

    public ServerCallBack stablishConnection() {
        if (server == null) {
            try {
                Registry reg = LocateRegistry.getRegistry("127.0.0.1", 1099);
                server = (ServerCallBack) reg.lookup("GameService");
                server.register(this,"Sallam");
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
        }
        return server;
    }

    @Override
    public boolean sendGameNotifigation(String playerUserName) throws RemoteException {
     //Create Dialog
     System.out.println("sendGameNotifigation");
     return true;
    }

    @Override
    public void joinGameRoom(String roomName,ClientCallBack creatorClient) throws RemoteException {
        gameRoom=new GameRoom(roomName,new ArrayList<ClientCallBack>(){{add(creatorClient);}});
             System.out.println("joinGameRoom");
            System.out.println(roomName);
    }

  
    @Override
    public void joinChatRoom(String targetUserName, ClientCallBack targetClient) throws RemoteException {
    }
    

    @Override
    public void leaveGameRoom() throws RemoteException {
        gameRoom=null;
    }

    @Override
    public void leftChatRoom(String userNameWhoLeft) throws RemoteException {
        System.out.println("Test"); 
    }

    @Override
    public void play(String player, int position) throws RemoteException {
        
    }


    @Override
    public void notifiyOnlineList() throws RemoteException {
        
    }

    @Override
    public void addPlayerToGameRoom(ClientCallBack player) throws RemoteException {
        gameRoom.addPlayer(player);
    }

    
}
