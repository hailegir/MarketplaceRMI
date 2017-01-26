package clientRMI;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

import serverRMI.ServerRemoteInterface;




public class ClientMain {


	public static void main(String[] args) throws RemoteException{

		try{
			//System.setSecurityManager(new RMISecurityManager());
                        System.setProperty("java.security.policy","security.policy");

			ServerRemoteInterface Market = (ServerRemoteInterface) Naming.lookup("rmi://localhost/Marketplace");

					System.out.println("Client connected to Server!");
					
			new loginGUI(Market);
			}
		catch(Exception e){
			e.printStackTrace();	 
		}
	}

}
