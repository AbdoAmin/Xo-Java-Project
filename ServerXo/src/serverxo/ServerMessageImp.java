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

    HashMap<String, ClientCallBack> clients = new HashMap<>();

    HashMap<String, String> clientMapGameRoom = new HashMap<>();

    HashMap<String, GameRoom> gameRooms = new HashMap<>();//for fast acces

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
    public boolean notifiyGameResult(String roomName,String WinnerUserName) throws RemoteException {
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
                if (room.equals(roomName)) {
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
    public void leftChatRoom(String myUserName, String playerUserName) throws RemoteException {
    }

    @Override
    public void signOut(String player) throws RemoteException {
    }

    @Override
    public boolean signIn(String player) throws RemoteException {
        return true;
    }

    @Override
    public boolean signUp(String player) throws RemoteException {
        return true;
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
                    System.err.println(ex.getMessage());
                }

            });
        }
    }

    @Override
    public void leaveServer(String gameRoom, String myUserName) throws RemoteException {
        if (clients.containsKey(myUserName)) {
            ArrayList<String> temp = gameRooms.get(gameRoom).getPlayers().keySet();
            if (temp.indexOf(myUserName) > 1) {
                gameRooms.get(gameRoom).getPlayers().remove(myUserName);
                clients.forEach((e, client) -> {
                    try {
                        client.leftChatRoom(myUserName);
                        client.leftGameRoom(myUserName);
                    } catch (RemoteException ex) {
                        Logger.getLogger(ServerMessageImp.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
                updateList();
            } else {
                notifiyGameResult(gameRoom,gameRooms.get(gameRoom).getPlayers().get(temp.get(0)));
            }
            clients.remove(myUserName);

        }
    }


}
