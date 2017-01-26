package clientRMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientRemoteInterface extends Remote {
	public void receiveMSG() throws RemoteException;
	public String getUser() throws RemoteException;
	
}
