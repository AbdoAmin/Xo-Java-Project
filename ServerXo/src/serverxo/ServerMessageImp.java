/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverxo;

import commontxo.ClientCallBack;
import commontxo.GameRoom;
import commontxo.ServerCallBack;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Abdo Amin
 */
public class ServerMessageImp extends UnicastRemoteObject implements ServerCallBack {

    HashMap<String, ClientCallBack> clients = new HashMap<String, ClientCallBack>();

    HashMap<String,ArrayList<ClientCallBack>> gameRooms = new HashMap<String,ArrayList<ClientCallBack>>();

    public ServerMessageImp() throws RemoteException {
    }

//    @Override
//    public void tellOthers(String msg) throws RemoteException {
//        for (ClientCallBack clientRef : clients) {
//            try {
//                clientRef.receiveMessage(msg);
//            } catch (RemoteException ex) {
//                System.out.println("Can’t send msg to client!");
//                ex.printStackTrace();
//            }
//        }
//    }
    @Override
    public void register(ClientCallBack clientRef, String playerUserName) throws RemoteException {
        clients.put(playerUserName, clientRef);
    }

    @Override
    public void unRegister(ClientCallBack clientRef, String playerUserName) throws RemoteException {
        clients.remove(playerUserName, clientRef);
    }

    @Override
    public ArrayList<String> initOnlineList() throws RemoteException {
        //TODO Fillter Online
    }

    @Override
    public boolean sendGameRequest(String myUserName, String oppesiteUserName) throws RemoteException {
        if (clients.get(oppesiteUserName).sendGameNotifigation(myUserName)) {
            ArrayList<ClientCallBack> temp = new ArrayList<ClientCallBack>() {
                {
                    add(clients.get(myUserName));
                    add(clients.get(oppesiteUserName));
                }
            };
            gameRooms.put(myUserName,temp);
            clients.get(myUserName).joinGameRoom(temp);
            clients.get(oppesiteUserName).joinGameRoom(temp);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean notifiyGameResult(String p) throws RemoteException {
    }

    @Override
    public void joinChatRoom(String myUserName, String playerUserName) throws RemoteException {
    }

    @Override
    public void leftChatRoom(ArrayList<ClientCallBack> players) throws RemoteException {
    }

    @Override
    public void signOut(String player) throws RemoteException {
    }

    @Override
    public void signIn(String player) throws RemoteException {
    }

    @Override
    public void signUp(String player) throws RemoteException {
    }

    @Override
    public void spectateGame(String myUserName,String playerUserName) throws RemoteException {
        if(gameRooms.get(playerUserName)!=null){
            gameRooms.get(playerUserName).add(clients.get(myUserName));
            gameRooms.get(playerUserName).forEach(client->{
                try {
                    client.joinGameRoom(gameRooms.get(playerUserName));
                } catch (RemoteException ex) {
                    Logger.getLogger(ServerMessageImp.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
    }

}

