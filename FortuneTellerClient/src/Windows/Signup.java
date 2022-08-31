/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Windows;

import Controller.ConnectionController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *
 * @author tmontanez
 */
public class Signup extends JFrame
{
    private JPanel contentPaneTM;
    private JTextField userTxtFieldTM;
    private JPasswordField passwordFieldTM;
    private JPasswordField passwordField_1TM;
    private JButton registerBtnTM;

    ConnectionController controllerTM;
    /**
     * Create the frame.
     */
    public Signup() {
        controllerTM = ConnectionController.getInstance();
        setBackground(Color.WHITE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 395, 531);
        setupForSignUp();
    }

    /**
     * setup ui for signup
     */
    private void setupForSignUp(){
        contentPaneTM = new JPanel();
        setContentPane(contentPaneTM);
        contentPaneTM.setLayout(null);

        // disconnects when close button is clicked
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ConnectionController.getInstance().disconnect();
            }
        });

        JLabel lblNewLabel = new JLabel("Username");
        lblNewLabel.setBounds(37, 111, 100, 14);
        contentPaneTM.add(lblNewLabel);

        userTxtFieldTM = new JTextField();
        userTxtFieldTM.setBounds(37, 136, 312, 30);
        contentPaneTM.add(userTxtFieldTM);
        userTxtFieldTM.setColumns(10);

        passwordFieldTM = new JPasswordField();
        passwordFieldTM.setBounds(37, 202, 312, 30);
        contentPaneTM.add(passwordFieldTM);

        passwordField_1TM = new JPasswordField();
        passwordField_1TM.setBounds(37, 268, 312, 30);
        contentPaneTM.add(passwordField_1TM);

        JLabel lblNewLabel_1TM = new JLabel("Password");
        lblNewLabel_1TM.setBounds(37, 177, 100, 14);
        contentPaneTM.add(lblNewLabel_1TM);

        JLabel lblNewLabel_3TM = new JLabel("Confirm Password");
        lblNewLabel_3TM.setBounds(37, 243, 120, 14);
        contentPaneTM.add(lblNewLabel_3TM);

        registerBtnTM = new JButton("Signup");
        registerBtnTM.setBounds(37, 388, 312, 30);
        registerBtnTM.addActionListener(e -> {
            String usernameTM = userTxtFieldTM.getText();
            String pass1TM = passwordFieldTM.getText();
            String pass2TM = passwordField_1TM.getText();
            if(!pass1TM.equals(pass2TM)){
                JOptionPane.showMessageDialog(this, "Passwords doesn't match");
                return;
            }
            if(!usernameTM.isEmpty()) {
                String datTM = usernameTM+","+pass1TM;
                controllerTM.signupUser(datTM);
                dispose();
                new Login().setVisible(true);
            }
        });
        contentPaneTM.add(registerBtnTM);

        JLabel titleTM = new JLabel("Signup");
        titleTM.setFont(new Font("Arial", Font.BOLD, 20));
        titleTM.setBounds(getWidth()/2-30, 16, 70, 40);
        contentPaneTM.add(titleTM);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width / 2 - getWidth() / 2, screenSize.height / 2 - getHeight() / 2);
    }


}
