/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.*;

/**
 *
 * @author tmontanez
 */
public class Dashboard extends JFrame {
    JLabel connStatusLblTM;
    JTextPane responseLblTM;
    JLabel posCountLblTM;
    JLabel negCountLblTM;
    JLabel clientNameLblTM;

    public static final String CONNECTED = "Connected";
    public static final String NOT_CONNECTED = "Not Connected";

    public static final Color BACKGROUND_COLOR = new Color(39, 41, 43);
    public static final Color CONNECTED_COLOR = new Color(17, 106, 209);
    public static final Color NOT_CONNECTED_COLOR = new Color(186, 61, 22);
    public static final Color POSITIVE_COLOR = new Color(51, 153, 31);
    public static final Color NEGATIVE_COLOR = new Color(186, 61, 22);
    public static final Color RESPONSE_COLOR = new Color(237, 103, 40);
    public static final String FONT_FAMILY = "Lucida Console";

    public Dashboard() {
        initGUI();
    }

    public void setConnectionStatus(String statusTM) {
        connStatusLblTM.setText(statusTM);
        if(statusTM.equals(CONNECTED)) {
            connStatusLblTM.setForeground(CONNECTED_COLOR);
        } else {
            connStatusLblTM.setForeground(NOT_CONNECTED_COLOR);
            resetState();
        }
        revalidate();
        repaint();
    }
    public void setPositiveWordCount(int countTM) {
        this.posCountLblTM.setText(String.valueOf(countTM));
        revalidate();
        repaint();
    }

    public void setNegativeWordCount(int countTM) {
        this.negCountLblTM.setText(String.valueOf(countTM));
        revalidate();
        repaint();
    }

    public void resetState(){
        setNegativeWordCount(0);
        setPositiveWordCount(0);
        setClientResponse("---");
        setClientNameLbl("---");
    }

    public void setClientNameLbl(String name){
        this.clientNameLblTM.setText(name);
        revalidate();
        repaint();
    }

    private void initGUI() {
        JLabel lbl1TM = new JLabel("Connection Status");
        connStatusLblTM = new JLabel("Not Connected");

        JLabel lbl2TM = new JLabel("Client's Response");
        responseLblTM = new JTextPane();

        JLabel lbl3TM = new JLabel("Positive Word Count");
        posCountLblTM = new JLabel("0");

        JLabel lbl4TM = new JLabel("Negative Word Count");
        negCountLblTM = new JLabel("0");
        
        JLabel lbl5TM = new JLabel("Client's Name");
        clientNameLblTM = new JLabel("---");

        setSize(600, 600);
        setLayout(null);
        Container container = getContentPane();
        container.setBackground(BACKGROUND_COLOR);

        formatHeadingLabel(lbl1TM);
        lbl1TM.setBounds(20, 20, 200, 40);

        connStatusLblTM.setFont(new Font(FONT_FAMILY, Font.BOLD, 33));
        connStatusLblTM.setBounds(20, 30, 350, 100);

        formatHeadingLabel(lbl2TM);
        lbl2TM.setBounds(20, 150, 200, 40);

        responseLblTM.setBackground(BACKGROUND_COLOR);
        responseLblTM.setFont(new Font(FONT_FAMILY, Font.PLAIN, 15));
        responseLblTM.setBorder(new EmptyBorder(new Insets(10, 0, 10, 20)));
        responseLblTM.setFocusable(false);
        responseLblTM.setBounds(20, 190, 450, 100);
        setClientResponse("---");

        formatHeadingLabel(lbl3TM);
        lbl3TM.setBounds(20, 300, 200, 40);

        posCountLblTM.setFont(new Font(FONT_FAMILY, Font.BOLD, 35));
        posCountLblTM.setForeground(POSITIVE_COLOR);
        posCountLblTM.setBounds(20, 300, 350, 100);

        formatHeadingLabel(lbl4TM);
        lbl4TM.setBounds(20, 400, 200, 40);

        negCountLblTM.setFont(new Font(FONT_FAMILY, Font.BOLD, 35));
        negCountLblTM.setForeground(NEGATIVE_COLOR);
        negCountLblTM.setBounds(20, 400, 350, 100);

        formatHeadingLabel(lbl5TM);
        lbl5TM.setBounds(350, 30, 200, 40);

        clientNameLblTM.setFont(new Font(FONT_FAMILY, Font.BOLD, 29));
        clientNameLblTM.setForeground(Color.white);
        clientNameLblTM.setBounds(350, 30, 350, 100);

        container.add(lbl1TM);
        container.add(connStatusLblTM);
        container.add(lbl2TM);
        container.add(responseLblTM);
        container.add(lbl3TM);
        container.add(posCountLblTM);
        container.add(lbl4TM);
        container.add(negCountLblTM);
        container.add(lbl5TM);
        container.add(clientNameLblTM);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width / 2 - getWidth() / 2, screenSize.height / 2 - getHeight() / 2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void formatHeadingLabel(JLabel lbl3TM) {
        lbl3TM.setFont(new Font(FONT_FAMILY, Font.BOLD, 15));
        lbl3TM.setForeground(Color.gray);
    }

    public void setClientResponse(String msgTM) {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, RESPONSE_COLOR);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        responseLblTM.setCharacterAttributes(aset, false);
        responseLblTM.setText(msgTM);
    }
}
