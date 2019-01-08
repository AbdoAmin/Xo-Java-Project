/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commontxo;

/**
 *
 * @author Abdo Amin
 */
public class PlayerList {
    private String name;
    private String roomName;

    public PlayerList(String name, String roomName) {
        this.name = name;
        this.roomName = roomName;
    }

    public String getName() {
        return name;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
    
}
