/*
 * To change this license header, choose License Headers inTM Project Properties.
 * To change this template fileTM, choose Tools | Templates
 * and open the template inTM the editor.
 */
package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.*;

/**
 *
 * @author tmontanez
 */
public class Server {
    ArrayList<User> usersTM = new ArrayList<>();
    HashSet<String> negativeWordsTM = new HashSet<>();
    HashSet<String> positiveWordsTM = new HashSet<>();
    ArrayList<String> positiveFortunesTM = new ArrayList<>();
    ArrayList<String> negativeFortunesTM = new ArrayList<>();
    String loggedInUserTM = "";
    ServerSocket serverSocketTM;
    Socket clientSocketTM;
    DataOutputStream outTM;
    DataInputStream inTM;
    Dashboard dashboardTM;
    String methodTM;
    private static final int WAIT_TIMEOUT = 5; // seconds
    private static final int SOCKET_TIMEOUT = 10; // seconds

    private void saveUsers() {
        try (FileWriter writerTM = new FileWriter("users.csv");
             BufferedWriter bufferedWriter = new BufferedWriter(writerTM)) {
            for (User userTM : usersTM) {
                bufferedWriter.write(userTM.toString() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readData() {
        File fileTM = new File("users.csv");
        if (fileTM.exists()) {
            try (Scanner reader = new Scanner(fileTM)) {
                while (reader.hasNextLine()) {
                    String dataTM = reader.nextLine();
                    String[] fields = dataTM.split(",");
                    User user = new User(fields[0], fields[1]);
                    usersTM.add(user);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        fileTM = new File("words.csv");
        if (fileTM.exists()) {
            try (Scanner reader = new Scanner(fileTM)) {
                reader.nextLine();
                while (reader.hasNextLine()) {
                    String data = reader.nextLine();
                    String[] fields = data.split(",");
                    negativeWordsTM.add(fields[0]);
                    positiveWordsTM.add(fields[1]);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        fileTM = new File("fortunes.csv");
        if (fileTM.exists()) {
            try (Scanner reader = new Scanner(fileTM)) {
                reader.nextLine();
                while (reader.hasNextLine()) {
                    String data = reader.nextLine();
                    String[] fields = data.split(",");
                    positiveFortunesTM.add(fields[0]);
                    negativeFortunesTM.add(fields[1]);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void run() {
        System.out.println("Initiating Server...");
        dashboardTM = new Dashboard();
        int portNumber = 8081;
        try {
            serverSocketTM = new ServerSocket(portNumber);
            System.out.println("Creating a server socket that listens on port " + portNumber);
            // This section of the code allows the server to listen infinitely for the client to connect
            readData();
            dashboardTM.setVisible(true);
            while (true) {
                dashboardTM.setConnectionStatus(Dashboard.NOT_CONNECTED);
                clientSocketTM = serverSocketTM.accept();
                dashboardTM.setConnectionStatus(Dashboard.CONNECTED);
                dashboardTM.setVisible(true);
                outTM = new DataOutputStream(clientSocketTM.getOutputStream());
                inTM = new DataInputStream(new BufferedInputStream(clientSocketTM.getInputStream()));
                String[] fields;
                while (true) {
                    methodTM = inTM.readUTF();
                    String msg;
                    switch (methodTM) {
                        case "login":
                            msg = inTM.readUTF();
                            fields = msg.split(",");
                            User user = new User(fields[0], fields[1]);
                            boolean loggedIn = authenticateUser(user);
                            outTM.writeBoolean(loggedIn);
                            if (loggedIn) {
                                loggedInUserTM = user.getNameTM();
                                System.out.println("'" + loggedInUserTM + "' just logged in...");
                                dashboardTM.setClientNameLbl(loggedInUserTM);
                                startFortuneTelling();
                            }
                            break;
                        case "signup":
                            msg = inTM.readUTF();
                            fields = msg.split(",");
                            outTM.writeBoolean(signupUser(new User(fields[0], fields[1])));
                            break;
                        default:
                            break;
                    }
                    if (methodTM.equals("exit")) {
                        System.out.println("'" + loggedInUserTM + "' just exited...");
                        break;
                    }
                }
                inTM.close();
                outTM.close();
                clientSocketTM.close();
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Exception on port " + portNumber + ".");
            System.out.println(e.getMessage());
        }
    }

    private void startFortuneTelling() throws IOException, InterruptedException {
        outTM.writeUTF("Hello, " + loggedInUserTM + ". Are you having a good day?");
        String msg = inTM.readUTF();
        if(msg.equals("exit")) {
            methodTM = "exit";
            return;
        }
        while (true) {
            int positives = countPositiveWords(msg);
            int negatives = countNegativeWords(msg);
            dashboardTM.setClientResponse(msg);
            dashboardTM.setPositiveWordCount(positives);
            dashboardTM.setNegativeWordCount(negatives);
            outTM.writeUTF("Ok, give me a minute to consult my crystal ball.");
            Thread.sleep(WAIT_TIMEOUT * 1000);
            String response = getFortune(positives, negatives);
            outTM.writeUTF("I see " + response + " in your future.");
            clientSocketTM.setSoTimeout(SOCKET_TIMEOUT * 1000);
            try {
                msg = inTM.readUTF();
                if(msg.equals("exit")) {
                    methodTM = "exit";
                    return;
                }
            } catch (SocketTimeoutException ex) {
                clientSocketTM.setSoTimeout(0);
                outTM.writeUTF("Do you want me to try again? Yes or No?");
                String ch = inTM.readUTF();
                if (ch.equalsIgnoreCase("No")) {
                    outTM.writeUTF("exiting...");
                    System.out.println("'" + loggedInUserTM + "' just exited...");
                    dashboardTM.setConnectionStatus(Dashboard.NOT_CONNECTED);
                    return;
                } else {
                    outTM.writeUTF("waiting...");
                    msg = inTM.readUTF();
                    if(msg.equals("exit")) {
                        methodTM = "exit";
                        return;
                    }
                }
            }
        }
    }

    private String getFortune(int positives, int negatives) {
        String response;
        Random random = new Random();
        if (positives > negatives) {
            response = negativeFortunesTM.get(random.nextInt(negativeFortunesTM.size()));
        } else if (positives < negatives) {
            response = positiveFortunesTM.get(random.nextInt(positiveFortunesTM.size()));
        } else {
            int ch = random.nextInt(2);
            if (ch == 0) {
                response = negativeFortunesTM.get(random.nextInt(negativeFortunesTM.size()));
            } else {
                response = positiveFortunesTM.get(random.nextInt(positiveFortunesTM.size()));
            }
        }
        return response;
    }

    private int countNegativeWords(String msg) {
        String[] words = msg.split("[^a-zA-Z]+");
        int count = 0;
        for (String word : words) {
            if (negativeWordsTM.contains(word.toLowerCase(Locale.ROOT)))
                count++;
        }
        return count;
    }

    private int countPositiveWords(String msg) {
        String[] words = msg.split("[^a-zA-Z]+");
        int count = 0;
        for (String word : words) {
            if (positiveWordsTM.contains(word.toLowerCase(Locale.ROOT)))
                count++;
        }
        return count;
    }

    private boolean signupUser(User user) {
        if (authenticateUser(user))
            return false;

        usersTM.add(user);
        saveUsers();
        System.out.println("inserted");
        return true;
    }

    private boolean authenticateUser(User user) {
        for (User u : usersTM) {
            if (u.getNameTM().equals(user.getNameTM()) && u.getPasswordTM().equals(user.getPasswordTM()))
                return true;
        }
        return false;
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }
}
