package serverRMI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

import clientRMI.ClientRemoteInterface;

public interface ServerRemoteInterface extends Remote {
	public Boolean register (String s, String pw) throws RemoteException, SQLException;
	public Boolean login(String s, String pw) throws RemoteException, SQLException;
	public void saveProdToMarket(String[] prod) throws RemoteException;
	public ArrayList<String[]> createMyProductList(String userName) throws RemoteException;
	public void saveMyProd(String prod, String userName) throws RemoteException;
	public ArrayList<String[]> createProdList () throws RemoteException;
	public void saveProdToWish (String[] s, String userName) throws RemoteException;
	public ArrayList<String[]> createMyWishList(String userName) throws RemoteException;
	public void deleteWish(String[] s, String userName) throws RemoteException;
	public void deleteProduct(String s) throws RemoteException;
	public int getSum(String userName) throws RemoteException;
	public void discSum(int cost, String userName) throws RemoteException, SQLException;
	public void addSession(ClientRemoteInterface cri) throws RemoteException;
	public Boolean userOnline(String userName) throws RemoteException;
	public void removeSession(ClientRemoteInterface cri) throws RemoteException;
	public void sendToUser(String userName) throws RemoteException;
	public ArrayList<String> getMSG (String userName) throws RemoteException;
	public void saveWishNotify(String userName, String prodName, int prodPrice) throws RemoteException;
	public void incSum(int cost, String userNamesale) throws RemoteException, SQLException;
}
