/*
 * To change this license header, choose License Headers inTM Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template inTM the editor.
 */
package Windows;

import Controller.ConnectionController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;

import com.sun.speech.freetts.*;

/**
 *
 * @author tmontanez
 */
public class FortuneTeller extends JFrame {
    Socket socketTM;
    DataOutputStream outTM;
    DataInputStream inTM;

    JTextPane convAreaTM;
    JTextField msgFieldTM;
    String senderTM;
    JButton sendBtnTM;
    JButton speakBtnTM;
    JButton clearBtnTM;
    private static String VOICENAMETM = "kevin16";

    public FortuneTeller(String senderTM) {
        this.senderTM = senderTM;
        setTitle("Tomas' Excellent Fortune Teller");
        setupGUI();

        // disconnects when close button is clicked
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ConnectionController.getInstance().disconnect();
            }
        });
    }

    
    //  method for Text to Speech
     
    private void speak(String message) {
        Voice voice;
        // Set property as Kevin Dictionary
        System.setProperty(
                "freetts.voices",
                "com.sun.speech.freetts.en.us"
                        + ".cmu_us_kal.KevinVoiceDirectory");

        VoiceManager vm = VoiceManager.getInstance();
        voice = vm.getVoice(VOICENAMETM);

        voice.allocate();
        try {
            //sets the rate (words per minute i.e. 190) of the speech
            voice.setRate(120);
            //sets the baseline pitch (150) inTM hertz
            //sets the volume (10) of the voice
            voice.setVolume(10);
            voice.speak("\n"+message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSocket(Socket socketTM) {
        this.socketTM = socketTM;
    }

    public void setOut(DataOutputStream outTM) {
        this.outTM = outTM;
    }

    public void setInTM(DataInputStream inTM) {
        this.inTM = inTM;
    }

    public void sendMessage(String messageTM) {
        appendToPane(senderTM + ": " + messageTM + "\n\n", Color.gray);
        try {
            outTM.writeUTF(messageTM);
        } catch (IOException e) {
            e.printStackTrace();
        }
        msgFieldTM.setText(null);
    }

    public void receiveMessage(String messageTM) {
        appendToPane("Fortune Teller: " + messageTM + "\n\n", Color.BLUE);
        if(messageTM.contains("exiting")){
            appendToPane("( Fortune Teller left the chat )\n\n",Color.RED);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            appendToPane("( chat ended )\n\n",Color.RED);
            sendBtnTM.setEnabled(false);
        }
    }

    private void appendToPane(String msg, Color c) {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = convAreaTM.getDocument().getLength();
        convAreaTM.setCaretPosition(len);
        convAreaTM.setCharacterAttributes(aset, false);
        convAreaTM.replaceSelection(msg);
    }

    private void setupGUI() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBounds(0,0,200,300);

        convAreaTM = new JTextPane();
        convAreaTM.setBorder(new EmptyBorder(new Insets(10, 20, 10, 20)));
        convAreaTM.setMargin(new Insets(5, 5, 5, 5));
        convAreaTM.setFocusable(false);

        JScrollPane scrollpane = new JScrollPane(convAreaTM);
        panel.add(scrollpane, BorderLayout.CENTER);
        scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        msgFieldTM = new JTextField();
        sendBtnTM = new JButton("Send");
        clearBtnTM = new JButton("Clear");
        speakBtnTM = new JButton("Speak");

        msgFieldTM.setToolTipText("Write a message below for the server: ");
        msgFieldTM.requestFocus();
        sendBtnTM.addActionListener(e -> sendMessage(msgFieldTM.getText()));
        clearBtnTM.addActionListener(e -> msgFieldTM.setText(null));
        speakBtnTM.addActionListener(e -> speak(convAreaTM.getText()));

        setLayout(null);
        speakBtnTM.setBounds(360,10,70,30);
        panel.setBounds(10, 50, 420, 450);
        convAreaTM.setBounds(0,0,420,450);
        msgFieldTM.setBounds(10, 520, 260, 30);
        clearBtnTM.setBounds(280, 520, 70, 30);
        sendBtnTM.setBounds(360, 520, 70, 30);
        Container container = getContentPane();
        container.add(panel);
        container.add(msgFieldTM);
        container.add(speakBtnTM);
        container.add(sendBtnTM);
        container.add(clearBtnTM);

        setSize(460, 600);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width / 2 - getWidth() / 2, screenSize.height / 2 - getHeight() / 2);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void startChat() {
        try {
            while (socketTM.isConnected()) {
                receiveMessage(inTM.readUTF());
            }
        } catch (IOException e) {
            ConnectionController.getInstance().disconnect();
        }
    }

}
