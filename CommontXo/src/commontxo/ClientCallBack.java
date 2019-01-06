/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commontxo;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author Abdo Amin
 */
public interface ClientCallBack extends Remote {
    //control
    public boolean sendGameNotifigation(String playerUserName)throws RemoteException ;
    
    public void joinGameRoom(ArrayList<ClientCallBack> players) throws RemoteException;
    
    public void leftGameRoom() throws RemoteException;
    
    public void joinChatRoom(/*ChatRoom(name,allMessage,->this list)*/ArrayList<ClientCallBack> players) throws RemoteException;
    
    public void leftChatRoom(String userNameWhoLeft) throws RemoteException;
    
    //control game
    public void play(String/*<-Player*/ player,int position) throws RemoteException;
    
    //control chat
    public void receiveMessage(String nameOfRoom,String myMessage)throws RemoteException ;//filter yourseelf and send to yourself as friend room
    
    //realTime Response // just a bouns feature
    void notifiyOnlineList(String msg) throws RemoteException;
}
