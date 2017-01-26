package clientRMI;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import serverRMI.ServerRemoteInterface;


public class addWishGUI extends JFrame {
	
	JPanel thePanel = new JPanel();
	GenericTable wishTable;
	JLabel productLabel = new JLabel("Product:", JLabel.CENTER);
	JTextField productLabelField = new JTextField();
	JLabel priceLabel = new JLabel("Your intended price:", JLabel.CENTER);
	JTextField priceLabelField = new JTextField();
	JButton addButton = new JButton("Add wish");
	ServerRemoteInterface Market;
	String userName = null;

	addWishGUI(GenericTable mm, ServerRemoteInterface m, String name){
		userName=name;
		Market = m;
		wishTable = mm;
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

							String[] product1 = {productLabelField.getText(), priceLabelField.getText()};
							wishTable.add(product1);
							Market.saveProdToWish(product1, userName);
							setVisible(false);
						}
					 catch (Exception ex){ex.printStackTrace();}

					}
				
			});
	}
}