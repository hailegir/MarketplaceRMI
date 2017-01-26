package serverRMI;



import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;




public class accounts implements Remote, Serializable {
	private String name;
	private String pass;
	private int money;
	private ArrayList<String[]> myProdArr = new ArrayList<String[]>();
	private ArrayList<String[]> myWishArr = new ArrayList<String[]>();

	
	public accounts(String n, String p){
		name =n;
		pass =p;
		money = 2000;
	}
	
	public String getName(){
		return name;
	}
	
	public int getSum(){
		return money;
	}
	
	public void setSum(int i){
		money = i;
	}
	public String getPass(){
		return pass;
	}
	
	public void saveMyProd(String[] s){
		myProdArr.add(s);
	}
	
	public ArrayList<String[]> getMyProdList(){
		return myProdArr;
	}
	
	public ArrayList<String[]> getMyWishList(){
		return myWishArr;
	}
	
	public void saveMyWishList (String[] s){
		myWishArr.add(s);
	}
	
	public void deleteWish(String[] s) throws RemoteException{
		int counter = 0;
		for (String[] x : myWishArr){
			if(x[0].equals(s)){
				myWishArr.remove(counter);
			}
			counter++;
		}
	}
	

	
}
