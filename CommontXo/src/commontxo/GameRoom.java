/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commontxo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Abdo Amin
 */
public class GameRoom implements Serializable{

    private String roomName;
    private ArrayList<ClientCallBack> players;

    public GameRoom(String roomName, ArrayList<ClientCallBack> players) {
        this.roomName = roomName;
        this.players = players;
    }

    public String getRoomName() {
        return roomName;
    }

    public ArrayList<ClientCallBack> getPlayers() {
        return players;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void setPlayers(ArrayList<ClientCallBack> players) {
        this.players = players;
    }
    
    public void addPlayer(ClientCallBack player) {
        this.players.add(player);
    }

}
