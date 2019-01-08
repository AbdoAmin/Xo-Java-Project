/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverxo;

import commontxo.ClientCallBack;
import commontxo.GameRoom;
import commontxo.PlayerList;
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

    HashMap<String, String> clientMapGameRoom = new HashMap<String, String>();

    HashMap<String, GameRoom> gameRooms = new HashMap<String, GameRoom>();//for fast acces

    public void updateList() {
        clients.forEach((e, client) -> {
            try {
                client.notifiyOnlineList();
            } catch (RemoteException ex) {
                Logger.getLogger(ServerMessageImp.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

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
    public ArrayList<PlayerList> initOnlineList() throws RemoteException {
        //TODO Fillter Online
        new PlayerList("A", clientMapGameRoom.get("A"));
        return null;
    }

    @Override
    public boolean sendGameRequest(String myUserName, String oppesiteUserName) throws RemoteException {
        if (clients.containsKey(oppesiteUserName) && clients.containsKey(myUserName)) {
            if (clients.get(oppesiteUserName).sendGameNotifigation(myUserName)) {
                ArrayList<ClientCallBack> temp = new ArrayList<ClientCallBack>() {
                    {
                        add(clients.get(myUserName));
                        add(clients.get(oppesiteUserName));
                    }
                };
                GameRoom newGameRoom = new GameRoom(myUserName, temp);
                gameRooms.put(myUserName, newGameRoom);
                clientMapGameRoom.put(myUserName, newGameRoom.getRoomName());
                clientMapGameRoom.put(oppesiteUserName, newGameRoom.getRoomName());

                //Init GameRoom at Client Side
                clients.get(myUserName).joinGameRoom(myUserName, clients.get(myUserName));
                clients.get(oppesiteUserName).joinGameRoom(myUserName, clients.get(myUserName));

                //pass CleintInterFace
                clients.get(myUserName).addPlayerToGameRoom(clients.get(oppesiteUserName));
                clients.get(oppesiteUserName).addPlayerToGameRoom(clients.get(oppesiteUserName));
                return true;
            }
            System.out.println("sendGameRequest");
        }
        return false;
    }

    @Override
    public boolean notifiyGameResult(String roomName) throws RemoteException {
        //TODO Ediiit Player score
        if (gameRooms.containsKey(roomName)) {
            gameRooms.get(roomName).getPlayers().forEach(client -> {
                try {
                    client.leaveGameRoom();
                } catch (RemoteException ex) {
                    Logger.getLogger(ServerMessageImp.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            clientMapGameRoom.forEach((name, room)
                    -> {
                if (room == roomName) {
                    room = null;
                }
            });
            gameRooms.remove(roomName);
            updateList();
            return true;
        }
        return false;
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
    public void spectateGame(String myUserName, String roomName) throws RemoteException {
        if (gameRooms.containsKey(roomName)) {
            clients.get(myUserName).joinGameRoom(roomName, gameRooms.get(roomName).getPlayers().get(0));
            for (int i = 1; i < gameRooms.get(roomName).getPlayers().size(); i++) {
                clients.get(myUserName).addPlayerToGameRoom(gameRooms.get(roomName).getPlayers().get(i));
            }
            gameRooms.get(roomName).addPlayer(clients.get(myUserName));
            clientMapGameRoom.put(myUserName, roomName);
            gameRooms.get(roomName).getPlayers().forEach(client -> {
                try {
                    client.addPlayerToGameRoom(clients.get(myUserName));
                } catch (RemoteException ex) {
                    Logger.getLogger(ServerMessageImp.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
    }

}
