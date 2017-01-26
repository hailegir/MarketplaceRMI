package serverRMI;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerMain {

    static Connection conn;
    static Statement stat;

    public static void main(String[] args) {

        try {
            //Database stuff
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/mysql", "root", "123456");
        } catch (ClassNotFoundException | SQLException e) {
            Logger.getLogger(ServerMain.class.getName()).log(Level.SEVERE, null, e);
        }
        try {
            System.out.println("Connection established to jdbc:derby://localhost:1527/mysql");
            stat = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
           
            System.out.println("tableExist(conn, \"people\"): " + tableExists(conn, "people"));
            if (!tableExists(conn, "people")) {
                stat.executeUpdate("create table people (name VARCHAR(32), password VARCHAR(32), money Integer)");
                System.out.println("people created");
            }

            //Create items table if it doesn't exist
            if (!tableExists(conn, "items")) {
                stat.executeUpdate("create table items (name VARCHAR(32), item VARCHAR(32))");
                System.out.println("items created");
            }

            if (!tableExists(conn, "wishes")) {
                stat.executeUpdate("create table wishes (name VARCHAR(32), wishName VARCHAR(32), wishPrice Integer)");
                System.out.println("wishes created");
            }

            //Create marketProducts table if it doesn't exist
            if (!tableExists(conn, "marketProducts")) {
                stat.executeUpdate("create table marketProducts (name VARCHAR(32), productName VARCHAR(32), productPrice Integer, productID Integer)");
                System.out.println("marketProducts created");
            }

            //Create wishes table if it doesn't exist
            if (!tableExists(conn, "userMSG")) {
                stat.executeUpdate("create table userMSG (name VARCHAR(32), msg VARCHAR(50), wishName VARCHAR(32))");
                System.out.println("userMSG created");
            }

            /*
            //Drop tables
            stat.executeUpdate("drop table people");
            stat.executeUpdate("drop table items");
            stat.executeUpdate("drop table wishes");
            stat.executeUpdate("drop table marketProducts");
            stat.executeUpdate("drop table userMSG");
             */
        } catch (SQLException ex) {
            Logger.getLogger(ServerMain.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {

//            ServerImpl sImp = new ServerImpl(stat, conn);
//            ServerMain stub = (ServerMain) UnicastRemoteObject.exportObject(sImp, 0);
            System.setProperty("java.security.policy", "security.policy");

            LocateRegistry.createRegistry(1099);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("Marketplace", new ServerImpl(stat, conn));
            //RMI naming stuff
            //java.rmi.Naming.bind("Marketplace", new ServerImpl(stat, conn));
            System.out.println("Marketplace Server" + " is ready.");
        } catch (RemoteException ex) {
            Logger.getLogger(ServerMain.class.getName()).log(Level.SEVERE, "Remote Exception", ex);
        }

    }

    private static boolean tableExists ( Connection con, String table ) {
    int numRows = 0;
    try {
      DatabaseMetaData dbmd = con.getMetaData();
      // Note the args to getTables are case-sensitive!
      ResultSet rs = dbmd.getTables( null, null, table.toUpperCase(), null);
      while( rs.next() ) ++numRows;
    } catch ( SQLException e ) {
        String theError = e.getSQLState();
        System.out.println("Can't query DB metadata: " + theError );
        System.exit(1);
    }
    return numRows > 0;
  }

}
