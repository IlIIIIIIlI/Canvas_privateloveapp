package Client;

import GUI.ClientGUI;
import Remote.IRemoteConnect;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.ConnectException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * TODO: the main class for client.
 *
 * @author Andy
 * @date 5/20/2022 1:56 AM
 */
public class JoinWhiteBoard {

    private static Socket clientSocket;
    private static DataOutputStream out;
    private static DataInputStream in;
    private static String address;
    private static int port;
    private static String userName;

    public static String getUserName() {
        return userName;
    }

    public static void main(String[] args) {
        // verifying the input parameters
        if (args.length != 3){
            JOptionPane.showMessageDialog(null,"Error: Wrong number of arguments");
        }
        try {
            address = args[0];
            port = Integer.parseInt(args[1]);
            userName = args[2];
//            address = "localhost";
//            port = 1200;
//            userName = "zhimei";
            ClientGUI.yourName.setText(userName);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Wrong type of arguments");
        }

        try {
            // Getting the registry
            int regPort = port + 1;
            Registry registry = LocateRegistry.getRegistry(address, regPort);
            // Looking up the registry for the remote object
            IRemoteConnect stub = (IRemoteConnect) registry.lookup("WHITEBOARD");
            // Calling the remote method using the obtained object
//            System.out.println("Waitting for the approval of server/manager...");
//            Boolean res = remoteCall.requestConnect(username);
            if (!stub.connectionWall(userName)) {
                JOptionPane.showMessageDialog(null, "Your asking is rejected.");
                System.exit(0);
            }
        } catch (ConnectException ce) {
            JOptionPane.showMessageDialog(null, "No server started");
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }


        try {
            clientSocket = new Socket(address, port);
        } catch (UnknownHostException e) {
            JOptionPane.showMessageDialog(null, "Invalid hostname for server");
            System.exit(0);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "try other serverAddress or port.");
            System.exit(0);
        }

        // start a client GUI
        ClientGUI.baseGUI();
        try {
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
            ClientGUI.out = out;
            // actually this is sysmetry to server side.
            // define a thread
            Client client = new Client(in);
            client.start();
        } catch (IOException e) {
            System.exit(0);
        }
    }
}
