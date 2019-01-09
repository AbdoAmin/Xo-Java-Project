/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientxo;

import commontxo.ClientCallBack;
import java.rmi.RemoteException;
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
            myModle.stablishConnection().unRegister(myModle, "Abdo");
        } catch (RemoteException ex) {
            Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendMessage(String sentMessage) throws RemoteException {
//        try {
//            myModle.stablishConnection().tellOthers(sentMessage);
//        } catch (RemoteException ex) {
//            Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
//        }

//test and delete it
        for (ClientCallBack clientRef : myModle.gameRoom.getPlayers()) {
            clientRef.leftChatRoom("");
        }

        }
    
    
    

    public static void main(String args[]) {
        GameController myGame = new GameController();
        myGame.startGUI();
        myGame.myModle.stablishConnection();
        try {
            myGame.myModle.stablishConnection().sendGameRequest("Abdo", "Sallam");
        } catch (RemoteException ex) {
            Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
