package serverRMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import clientRMI.ClientRemoteInterface;

public class ServerImpl extends UnicastRemoteObject implements ServerRemoteInterface {

    ArrayList<ClientRemoteInterface> clientArr = new ArrayList<ClientRemoteInterface>();
    accounts currentacc;
    Connection conn;
    Statement stat = null;
    private ArrayList<accounts> accArr = new ArrayList<accounts>();
    //private ArrayList<String[]> prodArr = new ArrayList<String[]>();
    ArrayList<String[]> wishArr;
    private static int counter = 1;

    public ServerImpl(Statement s, Connection c) throws RemoteException {
        conn = c;
        stat = s;
    }

        @Override
	public Boolean register (String s, String pw) throws RemoteException{
			
		
		try {
			stat = conn.createStatement(
			        ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = stat.executeQuery("select people.name, people.password from people");
			while (rs.next()){
				if (rs.getString("name").equalsIgnoreCase(s)){
					return false;
				}
			}

				PreparedStatement prep = conn.prepareStatement("insert into people values (?, ?, ?)");
			    
			    prep.setString(1, s);
			    prep.setString(2, pw);
			    prep.setInt(3, 2000);
			    prep.addBatch();   
			    prep.executeBatch();
			    return true;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;

	}
//    public Boolean register(String s, String pw) throws RemoteException, SQLException {
//        
//        
//        stat = conn.createStatement(
//                ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
//
//        PreparedStatement prep = conn.prepareStatement("select * from people where people.name=" + s);
//
//        prep.setString(1, s);
//        prep.setString(2, pw);
//        prep.addBatch();
//        ResultSet rs = prep.executeQuery();
//
//        Boolean log = rs.next();
//        rs.close();
//        prep.close();
//        return log;
//
//        try {
//            stat = conn.createStatement(
//                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
//            ResultSet rs = stat.executeQuery("select * from people where people.name=" + s);
//            if (rs.next()) {
//                return false;
//            }
//
//            PreparedStatement prep = conn.prepareStatement("insert into people values (?, ?, ?);");
//
//            prep.setString(1, s);
//            prep.setString(2, pw);
//            prep.setInt(3, 2000);
//            prep.addBatch();
//            prep.executeBatch();
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//
//    }

    @Override
    public Boolean login(String s, String pw) throws RemoteException, SQLException {

        stat = conn.createStatement(
                ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);

        PreparedStatement prep = conn.prepareStatement("select people.name, people.password from people where people.name=? and people.password=?");

        prep.setString(1, s);
        prep.setString(2, pw);
        prep.addBatch();
        ResultSet rs = prep.executeQuery();

        Boolean log = rs.next();
        rs.close();
        prep.close();
        return log;

    }

    public void saveProdToMarket(String[] prod) throws RemoteException {

        //ResultSet rs = stat.executeQuery("select people.id, people.name, people.password from people;");
        try {

            stat = conn.createStatement(
                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);

            PreparedStatement prep = conn.prepareStatement(
                    "insert into marketProducts values (?, ?, ?, ?)");

            prep.setString(1, prod[2]);
            prep.setString(2, prod[0]);
            prep.setInt(3, Integer.parseInt(prod[1]));
            prep.setInt(4, Integer.parseInt(prod[3]));
            prep.addBatch();
            prep.executeBatch();

            //Start a method that looks through all the wishes!! =)))))) 
            prep = conn.prepareStatement(
                    "select wishes.name, wishes.wishName, wishes.wishPrice from wishes where wishName=?");
            prep.setString(1, prod[0]);
            prep.addBatch();
            ResultSet rs = prep.executeQuery();

            String userName;
            int price;

            while (rs.next()) {
                userName = rs.getString("name");
                price = rs.getInt("wishPrice");
                if (Integer.parseInt(prod[1]) <= price) {
                    saveWishNotify(userName, prod[0], Integer.parseInt(prod[1]));
                    if (userOnline(userName)) {
                        sendToUser(userName);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ArrayList<String[]> createMyProductList(String userName) throws RemoteException {
        try {

            stat = conn.createStatement(
                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);

            PreparedStatement prep = conn.prepareStatement("select items.item, items.name from items where name=?");
            prep.setString(1, userName);
            prep.addBatch();
            ResultSet rs = prep.executeQuery();

            String[] xxx = new String[1];
            ArrayList<String[]> prodArr = new ArrayList<String[]>();

            while (rs.next()) {
                xxx = new String[1];
                xxx[0] = rs.getString("item");
                prodArr.add(xxx);
            }
            rs.close();
            return prodArr;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveMyProd(String prod, String userName) throws RemoteException {

        try {

            PreparedStatement prep = conn.prepareStatement(
                    "insert into items values (?, ?)");

            /// when saved may be array of prod [] is required for prod and price
            prep.setString(1, userName);
            prep.setString(2, prod);
            prep.addBatch();
            prep.executeBatch();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String[]> createProdList() throws RemoteException {

        try {

            stat = conn.createStatement(
                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);

            //name VARCHAR(32), productName VARCHAR(32), productPrice integer, productID integer
            ResultSet rs = stat.executeQuery("select marketProducts.name, marketProducts.productName, marketProducts.productPrice, marketProducts.productID from marketProducts");
            String[] xxx = new String[4];
            ArrayList<String[]> prodArr = new ArrayList<String[]>();

            while (rs.next()) {
                xxx = new String[4];
                xxx[0] = rs.getString("productName");
                xxx[1] = Integer.toString(rs.getInt("productPrice"));
                xxx[2] = rs.getString("name");
                xxx[3] = Integer.toString(rs.getInt("productID"));
                prodArr.add(xxx);
            }

            rs.close();
            return prodArr;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveProdToWish(String[] s, String userName) throws RemoteException {

        try {

            PreparedStatement prep = conn.prepareStatement(
                    "insert into wishes values (?, ?, ?)");
            prep.setString(1, userName);
            prep.setString(2, s[0]);
            prep.setInt(3, Integer.parseInt(s[1]));
            prep.addBatch();
            prep.executeBatch();
            prep.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String[]> createMyWishList(String userName) throws RemoteException {

        try {

            stat = conn.createStatement(
                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);

            PreparedStatement prep = conn.prepareStatement("select wishes.name, wishes.wishName, wishes.wishPrice from wishes where name=?");
//			ResultSet rs = stat.executeQuery("select wishes.name, wishes.wishName, wishes.wishPrice from wishes where name=?;");
            prep.setString(1, userName);
            prep.addBatch();
            ResultSet rs = prep.executeQuery();

            String[] xxx = new String[3];
            ArrayList<String[]> wishArr = new ArrayList<String[]>();

            while (rs.next()) {
                xxx = new String[3];
                xxx[0] = rs.getString("name");
                xxx[1] = rs.getString("wishName");
                xxx[2] = rs.getString("wishPrice");
                wishArr.add(xxx);
            }
            rs.close();
            return wishArr;

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return null;
    }

    public void deleteWish(String[] s, String userName) throws RemoteException {

        try {
            PreparedStatement prep = conn.prepareStatement("DELETE FROM wishes WHERE wishName='" + s[0] + "' AND name='" + userName + "'");
            //prep.setString(1, s[0]);
            //prep.setString(2, userName);
            //prep.addBatch();   
            prep.executeBatch();
            prep.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteProduct(String s) throws RemoteException {

        try {
            //ResultSet rs = stat.executeQuery("select marketProducts.productName from marketProducts where productName='"+s+"';");
            PreparedStatement prep = conn.prepareStatement("DELETE FROM marketProducts WHERE productName=?");
            prep.setString(1, s);
            prep.addBatch();
            prep.executeBatch();
            prep.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public int getSum(String userName) throws RemoteException {
        //

        //ResultSet rs;
        try {
            stat = conn.createStatement(
                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stat.executeQuery(
                    "select people.money from people where people.name=\"" + userName + "\"");

            //rs = stat.executeQuery("select people.money from people where people.name=\""+userName +"\";");	
            rs.first();
            return rs.getInt("money");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;/// why 0???
    }

    public void discSum(int cost, String userName) throws RemoteException, SQLException {
        ResultSet rs;

        try {
            /*
			stat = conn.createStatement(
			        ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
			      ResultSet rs = stat.executeQuery("select people.money from people where people.name=\""+userName +"\";");
			
			//rs = stat.executeQuery("select people.money from people where people.name=\""+userName +"\";");	
			//rs.first(); 
			    
			int balance= (rs.getInt("money"));
			System.out.println("amount of money is" + (rs.getInt("money")));
			balance -= cost;
			//rs.updateInt("money", balance);
	        //rs.updateRow();
			//ResultSet rs1 = stat.executeUpdate("update people.money from people where people.name=\""+userName +"\" , AND people.money='" + balance + "';");
	        System.out.println("amount of money is" + (rs.getInt("money")));
			  
             */

            stat = conn.createStatement();
            stat.executeQuery("select people.money from people where people.name=\"" + userName + "\"");
            rs = stat.getResultSet();
            rs.next();
            int balance = rs.getInt(1);
            balance -= cost;

            //currentacc.setSum(balance);
            //rs = stat.executeQuery("select people.money from people where people.name=\""+userName +"\";");
            //rs.next()
            //int balance= (prep.getInt("money"));
            //balance -= cost;
            PreparedStatement prep = conn.prepareStatement(
                    "update people set people.money= '" + balance + "' where people.name= '" + userName + "' ");
            prep.executeUpdate();

            prep.close();

            System.out.println("amount of money is for  " + userName + "   is   " + Integer.parseInt(rs.getString("money")));

            // reduce money from userName and add money to userNamesale
        } catch (SQLException e) {
            e.printStackTrace();
        }//finally {
        //     if (stat != null) { stat.close(); }
        //}

        //currentacc.setSum(currentacc.getSum()-cost); /// the purpose of current account unknown...discSum(String userName, int cost) looks good
        //System.out.println("you now have this much money on your account: " +currentacc.getSum());
    }

    public void incSum(int cost, String userNamesale) throws RemoteException, SQLException {
        ResultSet rs;
        try {
            stat = conn.createStatement();
            stat.executeQuery("select people.money from people where people.name=\"" + userNamesale + "\"");
            rs = stat.getResultSet();
            rs.next();
            int balance = rs.getInt(1);
            balance += cost;

            PreparedStatement prep1 = conn.prepareStatement(
                    "update people set people.money= '" + balance + "' where people.name= '" + userNamesale + "' ");

            prep1.executeUpdate();

            prep1.close();

            System.out.println("amount of money is for  " + userNamesale + "   is   " + Integer.parseInt(rs.getString("money")));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void addSession(ClientRemoteInterface cri) throws RemoteException {
        clientArr.add(cri);
    }

    public void removeSession(ClientRemoteInterface cri) throws RemoteException {
        if (clientArr.contains(cri)) {
            clientArr.remove(cri);
        }
    }

    public Boolean userOnline(String userName) throws RemoteException {
        try {
            for (ClientRemoteInterface x : clientArr) {
                if (x.getUser().equals(userName)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /// More clarification needed
    public Boolean lookThroughWishes(String[] s) throws RemoteException {
        return null;

    }

    public void sendToUser(String userName) throws RemoteException {
        if (userOnline(userName)) {
            for (ClientRemoteInterface x : clientArr) {
                if (x.getUser().equals(userName)) {
                    x.receiveMSG();
                }
            }
        }

    }

    public ArrayList<String> getMSG(String userName) throws RemoteException {
        try {

            stat = conn.createStatement(
                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);

            PreparedStatement prep = conn.prepareStatement(
                    "select userMSG.msg from userMSG where name=?");
            prep.setString(1, userName);
            prep.addBatch();
            ResultSet rs = prep.executeQuery();
            ArrayList<String> msgArr = new ArrayList<String>();
            int counter = 0;

            while (rs.next()) {
                msgArr.add(rs.getString("msg"));
            }

            return msgArr;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void saveWishNotify(String userName, String prodName, int prodPrice) throws RemoteException {
        String s = "There is now a prodcut (" + prodName + ") for the sum of " + prodPrice + "SEK.";

        try {
            PreparedStatement prep = conn.prepareStatement(
                    "insert into userMSG values (?, ?, ?)");

            prep.setString(1, userName);
            prep.setString(2, s);
            prep.setString(3, prodName);
            prep.addBatch();
            prep.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
