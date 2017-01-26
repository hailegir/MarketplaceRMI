package clientRMI;

import java.rmi.RemoteException;

import serverRMI.ServerRemoteInterface;

//import java.lang.Runtime;

public class AddShutdownHookSample {
	ServerRemoteInterface Market;
	ClientRemoteInterface cri;

	 public void attachShutDownHook(ServerRemoteInterface SRI, ClientRemoteInterface CRI){
	Market = SRI;
	cri = CRI;
	  Runtime.getRuntime().addShutdownHook(new Thread() {
	   @Override
	   public void run() {
	    System.out.println("Inside Add Shutdown Hook");
	    try {
			Market.removeSession(cri);
		} catch (RemoteException e) {			
			e.printStackTrace();
		}
	   }
	  });
	  System.out.println("Shut Down Hook Attached.");
	 }
}

	 
	 