package clientRMI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;


import serverRMI.ServerRemoteInterface;


public class Client extends UnicastRemoteObject implements ClientRemoteInterface{

	ServerRemoteInterface Market;
	Client thisClient;
	
	String[] MARKETCOLUMNS = {"Product", "Price", "Owner"};
	GenericTable marketTable = new GenericTable(MARKETCOLUMNS);
	String[] MARKETCOLUMNS2 = {"Product", "Price"};
	GenericTable wishTable = new GenericTable(MARKETCOLUMNS2);
	String[] MARKETCOLUMNS3 = {"Product"};
	GenericTable myProductTable = new GenericTable(MARKETCOLUMNS3);
	
	JFrame mainFrame = new JFrame();
	JPanel topPanel = new JPanel();
	JPanel marketPanel = new JPanel();
	JPanel centerPanel = new JPanel();
	JPanel wishPanel = new JPanel();
	JPanel myProductPanel = new JPanel();
	JPanel buyandaddPanel = new JPanel();
	JPanel addandRemovePanel = new JPanel();
	JButton buyButton = new JButton("Buy");
	JButton removeWishButton = new JButton("Remove");
	JButton addWishButton = new JButton("Add Wish");
	JButton addProductButton = new JButton("Add Product");
	JLabel welcomeMSG;
	JLabel welcomeMSG2;
	ArrayList<String[]> prodArr = new ArrayList<String[]>();
	ArrayList<String[]> myProdArr = new ArrayList<String[]>();
	ArrayList<String> msgArr = new ArrayList<String>();
	String userName = null;
	
	
	public Client(ServerRemoteInterface m, String myName) throws RemoteException {
		Market = m;
		userName=myName;
		System.out.println("So far so good");
		thisClient = this;
		  AddShutdownHookSample sample = new AddShutdownHookSample();
		  sample.attachShutDownHook(Market, thisClient);
		  
		 String[] strArr = null;
		
		mainFrame.setLayout(new BorderLayout());
		welcomeMSG = new JLabel("Welcome to the marketplace "+userName, JLabel.CENTER);
		/// needs to be modified
		welcomeMSG2 = new JLabel("Your account balance is "+ Integer.toString(Market.getSum(userName)) +"kr.", JLabel.CENTER);
			
		mainFrame.setSize(1000,600);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		centerPanel.setLayout(new GridLayout(1,3));
		

		
		mainFrame.add(topPanel, BorderLayout.NORTH);
		mainFrame.add(centerPanel, BorderLayout.CENTER);
		
		marketPanel.setLayout(new BorderLayout());
		marketPanel.add(new JLabel("Market", JLabel.CENTER), BorderLayout.NORTH);
		marketPanel.add(new JScrollPane(marketTable), BorderLayout.CENTER);
		marketPanel.add(buyandaddPanel, BorderLayout.SOUTH);
		
		
		
		wishPanel.setLayout(new BorderLayout());
		wishPanel.add(new JLabel("Wishlist", JLabel.CENTER), BorderLayout.NORTH);
		wishPanel.add(new JScrollPane(wishTable), BorderLayout.CENTER);
		wishPanel.add(addandRemovePanel, BorderLayout.SOUTH);
		
		myProductPanel.setLayout(new BorderLayout());
		myProductPanel.add(new JLabel("Your Products", JLabel.CENTER), BorderLayout.NORTH);
		myProductPanel.add(new JScrollPane(myProductTable), BorderLayout.CENTER);
		
		buyandaddPanel.add(buyButton);
		buyandaddPanel.add(addProductButton);
		
		addandRemovePanel.add(addWishButton);
		addandRemovePanel.add(removeWishButton);
		
		topPanel.setLayout(new GridLayout(1,2));
		topPanel.add(welcomeMSG);
		topPanel.add(welcomeMSG2);

		
		centerPanel.add(marketPanel);
		centerPanel.add(wishPanel);
		centerPanel.add(myProductPanel);
		
		mainFrame.setVisible(true);
		
//		//Uppdatera alla produkter i produktlistan från Servern
		
		prodArr = Market.createProdList();		
		if (prodArr!=null){
			for (String[] x : prodArr){
				marketTable.add(x);
			}
		}
		
		
		//Lägg till alla produkter som hör till den här användaren till myProductTable
		prodArr = Market.createMyProductList(userName);
		if (prodArr!=null){
		for (String[] x : prodArr){			
			myProductTable.add(x);
			}
		}

		//Add all products that is on my wishlist
		prodArr = Market.createMyWishList(userName);
		for (String[] x : prodArr){			
			wishTable.add(x);
			}
		
		
		//Get all msgs from server
		msgArr = Market.getMSG(userName);
			if(msgArr!=null){
				for(String x : msgArr){
					JOptionPane.showMessageDialog(null, x);
				}
			}
			

		

		//This button removes a wish
		removeWishButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ee){
				try{
					
//					if(Market.userOnline("rille")){
//						JOptionPane.showMessageDialog(null, "rille is online");
//					}
//					else
//						JOptionPane.showMessageDialog(null, "rille is OFFFline");
					
					int row = wishTable.getSelectedRow();
					String[] name = new String [1];
					name[0] = (String)wishTable.getValueAt(row, 0);
					Market.deleteWish(name, userName);
					wishTable.remove(wishTable.getSelectedItem());
					
					
					
					
				}
			 catch (Exception ex){ex.printStackTrace();}

			}
		
	});
		
		//This button is adding a product
		addProductButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ee){
						try{
							
							new addProdGUI(marketTable, Market, userName);
						}
					 catch (Exception ex){ex.printStackTrace();}

					}
				
			});
		
		addWishButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ee){
				try{
					
					new addWishGUI(wishTable, Market, userName);
//					Market.removeSession(thisClient);
					
				}
			 catch (Exception ex){ex.printStackTrace();}
				
				

			}
		
	});
		
		//This button removes a product from productlist and puts in your product list, 
		//subtracts the amount from your account. Adds the money to the owner.
		buyButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ee){
				try{

					
					int row = marketTable.getSelectedRow();
					String[] product = new String [3];
					product [0] =(String) marketTable.getValueAt(row,0);
					product [1] =(String) marketTable.getValueAt(row,1);
					product [2] =(String) marketTable.getValueAt(row,2);
					
					
					// This is the productID so that many product with the same name can be added:
					// product [3] =(String) marketTable.getValueAt(row,3);
					
					
					
					if(row == -1){
						JOptionPane.showMessageDialog(null, "Please mark the product you wish to buy");					
					}
					
					if (Market.getSum(userName)>=Integer.parseInt(product [1])){
						
							//We need to remove the money Market.discSum(price);
							JOptionPane.showMessageDialog(null, "Nice!  " + userName + "   You bought a   " +product [0] +"   for the modest sum of   " +product [1] +"  kr.");
							
							//JOptionPane.showMessageDialog(null, "amount of money  " + userName + "  has is   " + Integer.toString(Market.getSum(userName)) + "  kr." );
							//JOptionPane.showMessageDialog(null, "amount of money  " + product [2] + "  has is   " + Integer.toString(Market.getSum(product [2])) + "  kr." );
							///// Additional: Reduce money in account of the user
							Market.discSum(Integer.parseInt(product [1]), userName);// product [2] is the owner of prod
							Market.incSum(Integer.parseInt(product [1]), product [2]);
							///// saving the amount of money in the database is required
							//JOptionPane.showMessageDialog(null, "amount of money  " + userName + "  has is   " + Integer.toString(Market.getSum(userName)) + "  kr." );
							//JOptionPane.showMessageDialog(null, "amount of money  " + product [2] + "  has is   " + Integer.toString(Market.getSum(product [2])) + "  kr." );
						myProductTable.add(product);
						Market.saveMyProd(product[0],userName);//// why only product[1]
						Market.deleteProduct(product [0]);
						marketTable.remove(marketTable.getSelectedItem());
						// delete product! Market.deleteProduct(name[0]);
						//welcomeMSG2.setText("Your account balance is "+ Integer.toString(Market.getSum(userName)) +"kr.");
						welcomeMSG2.setText("Your account balance is "+ Integer.toString(Market.getSum(userName)) +"kr.");// Modified
						//Market.saveMyProd(product);
					}
					
					else
						JOptionPane.showMessageDialog(null, "You do not have enough money on your account.");
						
				}
			 catch (Exception ex){ex.printStackTrace();}

			}
		
	});
		
	}
	

	
	public void receiveMSG() throws RemoteException{
		msgArr = Market.getMSG(userName);
		if(msgArr!=null){
			for(final String x : msgArr){
				SwingUtilities.invokeLater(new Runnable() {
					public void run()  {
						JOptionPane.showMessageDialog(null, x);
					}
				});
			}
		}	
	}
	
	public String getUser(){
		return userName;
	}
		

		
}
	
	


