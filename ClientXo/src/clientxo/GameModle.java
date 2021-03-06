/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientxo;

import commontxo.ChatRoom;
import commontxo.ServerCallBack;
import commontxo.ClientCallBack;
import commontxo.GameRoom;
import commontxo.Player;
import commontxo.PlayerList;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Abdo Amin
 */
public class GameModle extends UnicastRemoteObject implements ClientCallBack {

    GameController myController;
    private ServerCallBack server;
    private Player me;

    ArrayList<PlayerList> onlineList;
    GameRoom gameRoom;
    HashMap<String, ChatRoom> chatRooms;//multibale chat rooms

    GameModle(GameController myController) throws RemoteException {
        this.myController = myController;
    }

    public ServerCallBack getServerInstance() {
        if (server == null) {
            try {
                Registry reg = LocateRegistry.getRegistry("127.0.0.1", 1099);
                server = (ServerCallBack) reg.lookup("GameService");
                server.register(this, me.getPlayerUserName());
            } catch (RemoteException | NotBoundException e) {
                System.err.println(e.getMessage());
            }
        }
        return server;
    }

    @Override
    public boolean sendGameNotifigation(String playerUserName) throws RemoteException {
        //TODO Create Dialog retuen true at accpet
        System.out.println("sendGameNotifigation");
        return true;
    }

    @Override
    public void joinGameRoom(String roomName, ClientCallBack creatorClient) throws RemoteException {
        gameRoom = new GameRoom(roomName, new HashMap<String, ClientCallBack>() {
            {
                put(roomName, creatorClient);
            }
        });

        //TODO Start Game 
        System.out.println("joinGameRoom");
        System.out.println(roomName);
    }

    @Override
    public void joinChatRoom(String targetUserName, ClientCallBack targetClient) throws RemoteException {
        chatRooms.put(targetUserName, new ChatRoom("", targetClient));
    }

    @Override
    public void leaveGameRoom() throws RemoteException {
        gameRoom = null;
    }

    @Override
    public void play(String player, int position) throws RemoteException {

    }

    @Override
    public void notifiyOnlineList() throws RemoteException {
        myController.showPlayerList(getServerInstance().initOnlineList());
    }

    @Override
    public void addPlayerToGameRoom(String playerUserName, ClientCallBack player) throws RemoteException {
        gameRoom.addPlayer(playerUserName, player);
    }

    @Override
    public void leftChatRoom(String userNameWhoLeft) throws RemoteException {
        if (!me.getPlayerUserName().equals(userNameWhoLeft)&&chatRooms.containsKey(userNameWhoLeft)) {
            chatRooms.remove(userNameWhoLeft);
        }
    }

    @Override
    public void receiveMessage(String senderUserName, String message) throws RemoteException {
        if (chatRooms.containsKey(senderUserName)) {
            chatRooms.get(senderUserName).setChat(
                    chatRooms.get(senderUserName).getChat()+"\n"
                            +senderUserName + " : " + message);
            myController.displayMessage(chatRooms.get(senderUserName).getChat());
        }
        //append message at my room after send
    }

    @Override
    public void sendMessage(String myUserName, String message) {
        try {
            receiveMessage(myUserName, message);
        } catch (RemoteException ex) {
            Logger.getLogger(GameModle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void leftGameRoom(String userNameWhoLeft) throws RemoteException {
        if (gameRoom.getPlayers().containsKey(userNameWhoLeft)) {
            gameRoom.removePlayer(userNameWhoLeft);
        }
    }
}
