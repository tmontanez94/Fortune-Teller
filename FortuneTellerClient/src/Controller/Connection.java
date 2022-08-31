/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author tmontanez
 */
public class Connection {
    
public static Socket aSocketTM;
    public static DataOutputStream outTM;
    public static DataInputStream inTM;

    public static void connect(){
        String hostNameTM = "localhost";//"127.0.0.1";
        int portNumberTM = 8081;//must match the server's port number. use a number greater than 1023
        System.out.println("Creating a client socket to connect on port " + portNumberTM + " to host " + hostNameTM);
        try {
            aSocketTM = new Socket(hostNameTM, portNumberTM);
            outTM = new DataOutputStream(aSocketTM.getOutputStream());
            inTM = new DataInputStream(new BufferedInputStream(aSocketTM.getInputStream()));

        } catch (UnknownHostException e) {
            System.out.println("Can't Find Host " + hostNameTM);
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Couldn't get I/O for the connection to "
                    + hostNameTM);
            System.out.println(e);
        }
    }

    public static boolean loginUser(String dat){
        boolean authenticated = false;
        try {
            outTM.writeUTF("login");
            outTM.writeUTF(dat);
            authenticated = inTM.readBoolean();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return authenticated;
    }

    public static boolean signupUser(String dat){
        boolean signedIn = false;
        try {
            outTM.writeUTF("signup");
            outTM.writeUTF(dat);
            signedIn = inTM.readBoolean();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return signedIn;
    }

    public static void  close(){
        try {
            outTM.writeUTF("exit");
            outTM.close();
            aSocketTM.close();
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
