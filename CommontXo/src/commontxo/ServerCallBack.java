package commontxo;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ServerCallBack extends Remote {
    
    public ArrayList<String> initOnlineList() throws RemoteException;

    boolean sendGameRequest(String myUserName,String oppesiteUserName) throws RemoteException;
        
    boolean notifiyGameResult(String/*Player  winnerPlayer*/ p) throws RemoteException;
    
    void register(ClientCallBack clientRef,String playerUserName) throws RemoteException;

    //function inside signOut related to server not at this interfce
    void unRegister(ClientCallBack clientRef,String playerUserName) throws RemoteException;
    
    public void spectateGame(String myUserName,String playerUserName) throws RemoteException;
    
    
    void joinChatRoom(String myUserName,String playerUserName) throws RemoteException;
    
    public void leftChatRoom(ArrayList<ClientCallBack> players) throws RemoteException;
   
    //control
    
    //Call RealTime change //any change
    public void signOut(String/*Player*/ player) throws RemoteException; 
    
    public void signIn(String/*Player*/ player) throws RemoteException;
    
    public void signUp(String/*Player*/ player) throws RemoteException;
    
}
