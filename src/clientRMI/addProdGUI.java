package clientRMI;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import serverRMI.ServerRemoteInterface;


public class addProdGUI extends JFrame {
	
	JPanel thePanel = new JPanel();
	GenericTable marketTable;
	JLabel productLabel = new JLabel("Product:", JLabel.CENTER);
	JTextField productLabelField = new JTextField();
	JLabel priceLabel = new JLabel("Price:", JLabel.CENTER);
	JTextField priceLabelField = new JTextField();
	JButton addButton = new JButton("Add the product");
	ServerRemoteInterface Market;
	String userName;
	private static int counter = 0;

	addProdGUI(GenericTable mm, ServerRemoteInterface m, String theUser){
		Market = m;
		userName = theUser;
		marketTable = mm;
		thePanel.setLayout(new GridLayout(1,5));
		setSize(600,70);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		thePanel.add(productLabel);
		thePanel.add(productLabelField);
		thePanel.add(priceLabel);
		thePanel.add(priceLabelField);
		thePanel.add(addButton);
		
		add(thePanel);
		setVisible(true);
		
		//This button is adding a product
		addButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ee){
						try{
							String[] product1 = {productLabelField.getText(), priceLabelField.getText(), userName, Integer.toString(counter)};
							marketTable.add(product1);
							Market.saveProdToMarket(product1);
							counter++;
							setVisible(false);
						}
					 catch (Exception ex){ex.printStackTrace();}

					}
				
			});
	}
}
