package commontxo;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ServerCallBack extends Remote {
    
    //init 
    public ArrayList<PlayerList> initOnlineList() throws RemoteException;//if room!=null player:Busy
    
    void register(ClientCallBack clientRef,String playerUserName) throws RemoteException;

    //function inside signOut related to server not at this interfce //deprecated :D
    void unRegister(ClientCallBack clientRef,String playerUserName) throws RemoteException;

    //game
    boolean sendGameRequest(String myUserName,String oppesiteUserName) throws RemoteException;
    
    public void spectateGame(String myUserName,String playerUserName) throws RemoteException;
        
    boolean notifiyGameResult(String roomName,String WinnerUserName) throws RemoteException;
    
    
    // Chat 
    void joinChatRoom(String myUserName,String playerUserName) throws RemoteException;
    
    public void leftChatRoom(String myUserName,String playerUserName) throws RemoteException;
   
    public void leaveServer(String gameRoom,String myUserName) throws RemoteException;
    //control
    
    //Call RealTime change //any change
    public void signOut(String/*Player*/ player) throws RemoteException; 
    
    public boolean signIn(String/*Player*/ player) throws RemoteException;
    
    public boolean signUp(String/*Player*/ player) throws RemoteException;
    
}
