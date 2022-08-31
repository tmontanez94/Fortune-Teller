/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Windows;

import Controller.ConnectionController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *
 * @author tmontanez
 */
public class Login extends JFrame implements ActionListener {

    private JPanel contentPaneTM;
    private JTextField textFieldTM;
    private JPasswordField passwordFieldTM;
    private JButton loginBtnTM;
    private JButton resetBtnTM;
    private JButton signupBtnTM;

    ConnectionController controllerTM;

    
    //  Create the frame.
     
    public Login() {
        controllerTM = ConnectionController.getInstance();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 305, 340);
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

        JLabel lblNewLabelTM = new JLabel("Login");
        lblNewLabelTM.setFont(new Font("Arial", Font.BOLD, 20));
        lblNewLabelTM.setBounds(getWidth()/2-30, 16, 70, 40);
        contentPaneTM.add(lblNewLabelTM);

        JLabel lblNewLabel_1TM = new JLabel("Username");
        lblNewLabel_1TM.setBounds(10, 67, 62, 19);
        contentPaneTM.add(lblNewLabel_1TM);

        textFieldTM = new JTextField();
        textFieldTM.setBounds(10, 85, 264, 29);
        contentPaneTM.add(textFieldTM);
        textFieldTM.setColumns(10);

        JLabel lblNewLabel_2 = new JLabel("Password");
        lblNewLabel_2.setBounds(10, 125, 70, 19);
        contentPaneTM.add(lblNewLabel_2);

        passwordFieldTM = new JPasswordField();
        passwordFieldTM.setBounds(10, 144, 264, 29);
        contentPaneTM.add(passwordFieldTM);

        loginBtnTM = new JButton("Login");
        loginBtnTM.addActionListener(this);
        loginBtnTM.setBounds(10, 199, 110, 23);
        contentPaneTM.add(loginBtnTM);

        resetBtnTM = new JButton("Reset");
        resetBtnTM.addActionListener(this);
        resetBtnTM.setBounds(168, 199, 110, 23);
        contentPaneTM.add(resetBtnTM);

        signupBtnTM = new JButton("Sign up");
        signupBtnTM.setBackground(null);
        signupBtnTM.addActionListener(this);
        signupBtnTM.setBounds(90, 230, 110, 23);
        contentPaneTM.add(signupBtnTM);


        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width / 2 - getWidth() / 2, screenSize.height / 2 - getHeight() / 2);

    }

    // resets the ui
     
    private void reset(){
        textFieldTM.setText(null);
        passwordFieldTM.setText(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (resetBtnTM.equals(source)) {
            reset();
        } else if (signupBtnTM.equals(source)) {
            dispose();
            new Signup().setVisible(true);
        } else if (loginBtnTM.equals(source)) {
            String name = textFieldTM.getText();
            String pass = passwordFieldTM.getText();
            String dat = name+","+pass;
            if(controllerTM.loginUser(dat)){
                dispose();
                controllerTM.launchFortuneTeller(name);
            }
        }
    }

    public static void main(String[] args) {
        new Login().setVisible(true);
    }
}
