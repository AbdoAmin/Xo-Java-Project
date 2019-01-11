/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientxo;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Abdo Amin
 */
public class GameController {

    MyGui myGUI;
    GameModle myModle;

    GameController() {
        myGUI = new MyGui(this);
        try {
            myModle = new GameModle(this);
        } catch (RemoteException ex) {
            Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void startGUI() {
        myGUI.setSize(600, 400);
        myGUI.setResizable(false);
        myGUI.setVisible(true);
    }

    public void displayMessage(String myMessage) {
        myGUI.displayMessage(myMessage);
    }

    public void unRegister() {
        try {
            myModle.getServerInstance().unRegister(myModle, "Abdo");
        } catch (RemoteException ex) {
            Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendMessage(String sentMessage) throws RemoteException {
//        try {
//            myModle.getServerInstance().tellOthers(sentMessage);
//        } catch (RemoteException ex) {
//            Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
//        }

    }

    public static void main(String args[]) {
        GameController myGame = new GameController();
        myGame.startGUI();
        myGame.myModle.getServerInstance();
        try {
            myGame.myModle.getServerInstance().sendGameRequest("Abdo", "Sallam");
        } catch (RemoteException ex) {
            Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //current player surrender or leave spectate.
    public void withdraw(String myUserName) throws RemoteException {
        //remove Mysilfe ...
        ArrayList<String> temp = new ArrayList<>(myModle.gameRoom.getPlayers().keySet());
        if (temp.indexOf(myUserName) > 1) {
            myModle.gameRoom.getPlayers().forEach((e, client) -> {
                try {
                    client.leftGameRoom(myUserName);
                } catch (RemoteException ex) {
                    Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            myModle.leaveGameRoom();
            myModle.getServerInstance().removeClientMapGameRoom(myUserName);
            myModle.getServerInstance().removePlayerFromGameRoom(myUserName, myModle.gameRoom.getRoomName());
        } else {
            temp.remove(myUserName);
            myModle.getServerInstance().notifiyGameResult(myModle.gameRoom.getRoomName(), temp.get(0));
        }
    }

}
