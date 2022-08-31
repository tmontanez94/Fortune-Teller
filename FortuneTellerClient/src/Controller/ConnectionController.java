 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Windows.FortuneTeller;

/**
 *
 * @author tmontanez
 */
public class ConnectionController {
    
private static ConnectionController controllerTM = null;
    private static FortuneTeller fortuneTellerTM;
    
// A thread in which client keeps receiving messages from server
    private static Thread chatThreadTM = null;

    private ConnectionController() {
        Connection.connect();
    }

    public static ConnectionController getInstance() {
        if (controllerTM == null) {
            controllerTM = new ConnectionController();
        }
        return controllerTM;
    }

    public boolean loginUser(String datTM) {
        return Connection.loginUser(datTM);
    }

    public boolean signupUser(String datTM) {
        return Connection.signupUser(datTM);
    }

    public void launchFortuneTeller(String nameTM) {
        fortuneTellerTM = new FortuneTeller(nameTM);
        fortuneTellerTM.setSocket(Connection.aSocketTM);
        fortuneTellerTM.setInTM(Connection.inTM);
        fortuneTellerTM.setOut(Connection.outTM);
        fortuneTellerTM.setVisible(true);
        // Initiate and start the thread
        chatThreadTM = new Thread(() -> fortuneTellerTM.startChat());
        chatThreadTM.start();
    }
    public void stopChat(){
        if(chatThreadTM!=null)
            chatThreadTM.stop();
    }
    public void disconnect() {
        stopChat();
        Connection.close();
    }

}
