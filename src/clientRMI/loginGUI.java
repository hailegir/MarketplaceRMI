package clientRMI;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import serverRMI.ServerRemoteInterface;
//import serverRMI.accounts;

public class loginGUI extends JFrame {

    JPanel thePanel = new JPanel();

    ServerRemoteInterface Market;
    JLabel userLabel = new JLabel("Username:", JLabel.CENTER);
    JTextField loginUserField = new JTextField();
    JLabel passLabel = new JLabel("Password:", JLabel.CENTER);
    JPasswordField loginPassField = new JPasswordField();
    JButton logButton = new JButton("Login");

    JLabel userLabel2 = new JLabel("Username:", JLabel.CENTER);
    JTextField regUserField = new JTextField();
    JLabel passLabel2 = new JLabel("Password:", JLabel.CENTER);
    JPasswordField regPassField = new JPasswordField();
    JButton regButton = new JButton("Register");

    public loginGUI(ServerRemoteInterface s) {

        Market = s;
        thePanel.setLayout(new GridLayout(2, 5));
        setSize(600, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        thePanel.add(userLabel);
        thePanel.add(loginUserField);
        thePanel.add(passLabel);
        thePanel.add(loginPassField);
        thePanel.add(logButton);

        thePanel.add(userLabel2);
        thePanel.add(regUserField);
        thePanel.add(passLabel2);
        thePanel.add(regPassField);
        thePanel.add(regButton);

        add(thePanel);
        setVisible(true);

        logButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ee) {
                try {

                    if (Market.login(loginUserField.getText(), loginPassField.getText())) {
                        setVisible(false);
                        Market.addSession(new Client(Market, loginUserField.getText()));
                    } else {
                        JOptionPane.showMessageDialog(null, "No such user or incorrect password");
                    }

                    loginUserField.setText("");
                    loginPassField.setText("");

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }

        });

        regButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ee) {
                try {
                    String name = regUserField.getText();
                    String password = regPassField.getText();
                    if (Market.register(name, password)) {
                        JOptionPane.showMessageDialog(null, "Welcome! You are now registered");
                        regUserField.setText("");
                        regPassField.setText("");
                    } else {
                        JOptionPane.showMessageDialog(null, "Username is already taken");
                        regUserField.setText("");
                        regPassField.setText("");
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }

        });
    }
}
