package Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.AlreadyBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.util.*;

import GUI.ServerGUI;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * TODO: this class is the main process in server side
 *
 * @author Andy
 * @date 5/21/2022 1:55 AM
 */
@SuppressWarnings("preview")
public class CreateWhiteBoard {

    private static int port = 2022;
    private static String userName;
    public static ArrayList<DataOutputStream> outputPool = new ArrayList<>();
    public static Map<Integer, Socket> threadPool = new Hashtable<>();
    private static DataOutputStream out;
    private static DataInputStream in;
    public static Map<Integer, String> validatedUsers = new Hashtable<>();
    public static ArrayList<JSONObject> pastCanvas = new ArrayList<>();
    public static ArrayList<JSONObject> fileLoader = new ArrayList<>();
    static int threadNum = 0;
    // server side close will cause socket exception problem
    public static HashSet<Integer> closedSockets = new HashSet<>();

    public static void main(String[] args) {

        // check number of args
        if (args.length != 3) {
            JOptionPane.showMessageDialog(null, "Check IP.");
            System.exit(0);
        }

        try {
            String address = args[0];
            System.out.println(address);
//            if (!address.equalsIgnoreCase("localhost") || !address.equalsIgnoreCase("127.0.0.1")) {
//                JOptionPane.showMessageDialog(null, "Check IP.");
//                System.exit(0);
//            }
            port = Integer.parseInt(args[1]);
            userName = args[2];
//        String address = "localhost";
//        port = 1200;
//        userName = "chenoi";
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Wrong type of arguments");
            System.exit(0);
        }

        try {
            // Instantiating the implementation class
            RemoteControl remoteControl = new RemoteControl();
            // registry port should separate from normal port
            int regPort = port + 1;
            // create registry so we don't need to open this in cmd
            Registry registry = LocateRegistry.createRegistry(regPort);
            // Binding the remote object (stub) in the registry
            registry.bind("WHITEBOARD", remoteControl);
            System.err.println("Server IS SET");
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(null, "RMI problem");
            System.exit(0);
        } catch (AlreadyBoundException AE) {
            JOptionPane.showMessageDialog(null, "RMI Port number has been used");
            System.exit(0);
        }

        // start the GUI
        ServerGUI sg = new ServerGUI();
        sg.ServerFunction();

        ServerGUI.colourButton.addMouseListener(new MouseAdapter() {
            JDialog dialog = null;
            JColorChooser cc = null;

            @Override
            public void mouseClicked(MouseEvent e) {
                if (dialog == null) {
                    cc = new JColorChooser(ServerGUI.selectedColor);
                    dialog = JColorChooser.createDialog(
                            null,
                            "Enter a colour you like...",
                            true,
                            cc,
                            null,
                            null);
                }
                cc.setColor(ServerGUI.selectedColor);
                dialog.setVisible(true);
                dialog.dispose();
                Color newColor = cc.getColor();
                if (newColor != null) {
                    ServerGUI.selectedColor = newColor;
                }
            }
        });

        // core
        ServerGUI.paintArea.addMouseListener(new MouseListener() {
            // have to have this
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            // define if press the mouse
            @Override
            public void mousePressed(MouseEvent me) {
                // update start for drag
                ServerGUI.sX = me.getX();
                ServerGUI.sY = me.getY();
//                printBoard(ServerGUI.paintGraph);
            }

            @Override
            public void mouseReleased(MouseEvent me) {
//                ServerGUI.paintArea.repaint();

                // update end for drag
                ServerGUI.eX = me.getX();
                ServerGUI.eY = me.getY();


//            int r = (int) Math.sqrt(w * w + h * h);
                System.out.println(ServerGUI.selectedButton);
                switch (ServerGUI.selectedButton) {
                    case "R":
                        ServerGUI.paintGraph.setColor(ServerGUI.selectedColor);
                        int x = Math.min(ServerGUI.sX, ServerGUI.eX);
                        int y = Math.min(ServerGUI.sY, ServerGUI.eY);
                        int w = Math.abs(ServerGUI.eX - ServerGUI.sX);
                        int h = Math.abs(ServerGUI.eY - ServerGUI.sY);
                        ServerGUI.paintGraph.drawRect(x, y, w, h);
                        commandOutput("R");
                        break;
                    case "T":
                        ServerGUI.paintGraph.setColor(ServerGUI.selectedColor);
                        ServerGUI.paintGraph.drawPolygon(new int[]{ServerGUI.sX, ServerGUI.eX, ((Math.max(ServerGUI.eX - ServerGUI.sX, ServerGUI.sX - ServerGUI.eX) / 2) + Math.min(ServerGUI.sX, ServerGUI.eX))}, new int[]{ServerGUI.sY, ServerGUI.eY, (Math.max(ServerGUI.eY - ServerGUI.sY, ServerGUI.sY - ServerGUI.eY) / 2 + Math.max(ServerGUI.sY, ServerGUI.eY))}, 3);
                        commandOutput("T");
                        break;
                    case "C":
                        ServerGUI.paintGraph.setColor(ServerGUI.selectedColor);
                        x = Math.min(ServerGUI.sX, ServerGUI.eX);
                        y = Math.min(ServerGUI.sY, ServerGUI.eY);
                        w = Math.abs(ServerGUI.eX - ServerGUI.sX);
                        h = Math.abs(ServerGUI.eY - ServerGUI.sY);
                        ServerGUI.paintGraph.drawOval(x, y, w, h);
                        commandOutput("C");
                        break;
                    case "L":
                        ServerGUI.paintGraph.setColor(ServerGUI.selectedColor);
                        ServerGUI.paintGraph.drawLine(ServerGUI.sX, ServerGUI.sY, ServerGUI.eX, ServerGUI.eY);
                        commandOutput("L");
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        ServerGUI.paintArea.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent me) {
//                if (ServerGUI.selectedButton.equals("P") || ServerGUI.selectedButton.equals("E")) {
                switch (ServerGUI.selectedButton) {
                    case "P":
                        ServerGUI.paintGraph.setColor(ServerGUI.selectedColor);
                        ServerGUI.eX = ServerGUI.sX;
                        ServerGUI.eY = ServerGUI.sY;
                        ServerGUI.sX = me.getX();
                        ServerGUI.sY = me.getY();
                        ServerGUI.paintGraph.drawLine(ServerGUI.sX, ServerGUI.sY, ServerGUI.eX, ServerGUI.eY);
                        commandOutput("P");
                    case "E":
                        ServerGUI.paintGraph.setColor(Color.WHITE);
                        ServerGUI.eX = ServerGUI.sX;
                        ServerGUI.eY = ServerGUI.sY;
                        ServerGUI.sX = me.getX();
                        ServerGUI.sY = me.getY();
                        ServerGUI.paintGraph.drawLine(ServerGUI.sX, ServerGUI.sY, ServerGUI.eX, ServerGUI.eY);
                        commandOutput("E");

                }
            }

            @Override
            public void mouseMoved(MouseEvent me) {
            }
        });

        // lets listen every button, and return their name!
        for (int i = 0; i < ServerGUI.buttonList.length; i++) {
            ServerGUI.buttonList[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ServerGUI.selectedButton = e.getActionCommand();
                }
            });
        }

        // this is the message button
        ServerGUI.sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = ServerGUI.typeText.getText().replaceAll("\n", "").trim();
                if (!text.equals("")) {
                    commandOutput("Send");
                    ServerGUI.typeText.setText("");
                }
                ServerGUI.textArea.append("Manager: " + text + '\n');
            }
        });


        ServerGUI.menuClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Server has been closed!");
                System.exit(0);
            }
        });


        ServerGUI.kickButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(CreateWhiteBoard.validatedUsers);
                String bye = ServerGUI.kickName.getText().trim();
                if (bye.equals("") || !validatedUsers.containsKey(Integer.parseInt(bye))) {
                    JOptionPane.showMessageDialog(null, "Check your input Kick ID...");
                } else {
                    if (!validatedUsers.containsKey(Integer.parseInt(bye))) {
                        JOptionPane.showMessageDialog(null, "no user found");
                    } else {// kick!
                        try {
                            // get a extra stream
                            DataOutputStream output = outputPool.get(Integer.parseInt(bye));
                            JSONObject func = new JSONObject();
                            func.put("Close", "Close");
                            output.writeUTF(String.valueOf(func));
                            output.flush();
//                            threadPool.get(Integer.parseInt(bye)).shutdownInput();
//                            threadPool.get(Integer.parseInt(bye)).shutdownOutput();
//                            threadPool.get(Integer.parseInt(bye)).close();
                            outputPool.remove(Integer.parseInt(bye));
                            threadPool.remove(Integer.parseInt(bye));
                            validatedUsers.remove(Integer.parseInt(bye));
                            JOptionPane.showMessageDialog(null, "Success kicking out!");

                            // RESET ALL USER LIST
                            ServerGUI.userList.setText("");
                            for (Integer id : CreateWhiteBoard.validatedUsers.keySet()) {
                                ServerGUI.userList.append(id + "-" + CreateWhiteBoard.validatedUsers.get(id) + "\n");
                            }

                            for (int i = 0; i < outputPool.size(); i++) {
                                JSONObject funac = new JSONObject();
                                funac.put("userList", ServerGUI.userList.getText());
                                outputPool.get(i).writeUTF(String.valueOf(funac));
                                outputPool.get(i).flush();
                            }

                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }
            }
        });

        /**
         * THE NEW refresh menu BUTTON
         */
        ServerGUI.menuNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    pastCanvas.clear();
                    ServerGUI.paintArea.repaint();
                    for (int i = 0; i < outputPool.size(); i++) {
//                        if (closedSockets.contains(i)) continue;
                        JSONObject func = new JSONObject();
                        func.put("New", "New");
                        outputPool.get(i).writeUTF(String.valueOf(func));
                        outputPool.get(i).flush();
                    }
                } catch (IOException ee) {
                    System.out.println("menuNew problem");
                }
            }
        });

        /**
         * The SAVEAS menu BUTTON
         */
        ServerGUI.menuSaveAs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fileName = JOptionPane.showInputDialog("the name of canvas you save now is?: ,with json as suffix");
                if (fileName != null) {
                    try {
                        FileWriter file = new FileWriter(fileName);
                        String temp = String.valueOf(pastCanvas);
                        temp = temp.substring(1, temp.length() - 1);
                        temp.replaceAll("\\n", "");
                        temp.replaceAll("\\t", "");
                        file.write(temp);
                        file.flush();
                        JOptionPane.showMessageDialog(null, "Success saving " + fileName);
                    } catch (IOException ee) {
                        JOptionPane.showMessageDialog(null, "your file isn't save in the name of :" + fileName);
                    }
                }

            }
        });


        /**
         * The SAVE menu BUTTON
         */
        ServerGUI.menuSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    FileWriter file = new FileWriter("MEME.json");
                    String temp = String.valueOf(pastCanvas);
                    temp = temp.substring(1, temp.length() - 1);
                    temp.replaceAll("\\n", "");
                    temp.replaceAll("\\t", "");
                    file.write(temp);
                    file.flush();
                    JOptionPane.showMessageDialog(null, "saved!");
                } catch (IOException ee) {
                    JOptionPane.showMessageDialog(null, "not saved");
                }
            }
        });


        /**
         * The OPEN menu BUTTON
         */
        ServerGUI.menuOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filename = JOptionPane.showInputDialog("file name is? :");
                try {
                    Scanner s = new Scanner(new File(filename));
//                    String temp;
                    while (s.hasNext()) {
//                        if((temp=s.next()).equalsIgnoreCase("[")){
//                            continue;
//                        }
                        JSONObject jsonObject = new JSONObject(s.next());
                        fileLoader.add(jsonObject);
                    }
                    s.close();
                } catch (JSONException je) {
                    System.out.println("some words cannot be interpreted.");
                    ;
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                pastCanvas.clear();
//                ServerGUI.paintArea.repaint();
                pastCanvas = fileLoader;
                for (int i = 0; i < pastCanvas.size(); i++) {
                    JSONObject content;
                    JSONObject temp = pastCanvas.get(i);
                    if (temp.has("R")) {
                        content = (JSONObject) temp.get("R");
                        Color c = new Color(Integer.parseInt(String.valueOf(content.get("color"))));
                        ServerGUI.paintGraph.setColor(c);
                        ServerGUI.sX = (Integer) content.get("startX");
                        ServerGUI.eX = (Integer) content.get("endX");
                        ServerGUI.sY = (Integer) content.get("startY");
                        ServerGUI.eY = (Integer) content.get("endY");
                        int x = Math.min(ServerGUI.sX, ServerGUI.eX);
                        int y = Math.min(ServerGUI.sY, ServerGUI.eY);
                        int w = Math.abs(ServerGUI.eX - ServerGUI.sX);
                        int h = Math.abs(ServerGUI.eY - ServerGUI.sY);
                        ServerGUI.paintGraph.drawRect(x, y, w, h);

                    } else if (temp.has("C")) {
                        content = (JSONObject) temp.get("C");
                        Color c = new Color(Integer.parseInt(String.valueOf(content.get("color"))));
                        ServerGUI.paintGraph.setColor(c);
                        ServerGUI.sX = (Integer) content.get("startX");
                        ServerGUI.eX = (Integer) content.get("endX");
                        ServerGUI.sY = (Integer) content.get("startY");
                        ServerGUI.eY = (Integer) content.get("endY");
                        int x = Math.min(ServerGUI.sX, ServerGUI.eX);
                        int y = Math.min(ServerGUI.sY, ServerGUI.eY);
                        int w = Math.abs(ServerGUI.eX - ServerGUI.sX);
                        int h = Math.abs(ServerGUI.eY - ServerGUI.sY);
                        ServerGUI.paintGraph.drawOval(x, y, w, h);
                    } else if (temp.has("T")) {
                        content = (JSONObject) temp.get("T");
                        Color c = new Color(Integer.parseInt(String.valueOf(content.get("color"))));
                        ServerGUI.paintGraph.setColor(c);
                        ServerGUI.sX = (Integer) content.get("startX");
                        ServerGUI.eX = (Integer) content.get("endX");
                        ServerGUI.sY = (Integer) content.get("startY");
                        ServerGUI.eY = (Integer) content.get("endY");
                        ServerGUI.paintGraph.drawPolygon(new int[]{ServerGUI.sX, ServerGUI.eX, ((Math.max(ServerGUI.eX - ServerGUI.sX, ServerGUI.sX - ServerGUI.eX) / 2) + Math.min(ServerGUI.sX, ServerGUI.eX))}, new int[]{ServerGUI.sY, ServerGUI.eY, (Math.max(ServerGUI.eY - ServerGUI.sY, ServerGUI.sY - ServerGUI.eY) / 2 + Math.max(ServerGUI.sY, ServerGUI.eY))}, 3);

                    } else if (temp.has("P")) {
                        content = (JSONObject) temp.get("P");
                        Color c = new Color(Integer.parseInt(String.valueOf(content.get("color"))));
                        ServerGUI.paintGraph.setColor(c);
                        ServerGUI.sX = (Integer) content.get("startX");
                        ServerGUI.eX = (Integer) content.get("endX");
                        ServerGUI.sY = (Integer) content.get("startY");
                        ServerGUI.eY = (Integer) content.get("endY");
                        ServerGUI.paintGraph.drawLine(ServerGUI.sX, ServerGUI.sY, ServerGUI.eX, ServerGUI.eY);

                    } else if (temp.has("L")) {
                        content = (JSONObject) temp.get("L");
                        Color c = new Color(Integer.parseInt(String.valueOf(content.get("color"))));
                        ServerGUI.paintGraph.setColor(c);
                        ServerGUI.sX = (Integer) content.get("startX");
                        ServerGUI.eX = (Integer) content.get("endX");
                        ServerGUI.sY = (Integer) content.get("startY");
                        ServerGUI.eY = (Integer) content.get("endY");
                        ServerGUI.paintGraph.drawLine(ServerGUI.sX, ServerGUI.sY, ServerGUI.eX, ServerGUI.eY);

                    } else if (temp.has("E")) {
                        content = (JSONObject) temp.get("E");
                        Color c = new Color(Integer.parseInt(String.valueOf(content.get("color"))));
                        ServerGUI.paintGraph.setColor(c);
                        ServerGUI.sX = (Integer) content.get("startX");
                        ServerGUI.eX = (Integer) content.get("endX");
                        ServerGUI.sY = (Integer) content.get("startY");
                        ServerGUI.eY = (Integer) content.get("endY");
                        ServerGUI.paintGraph.setColor(Color.WHITE);
                        ServerGUI.paintGraph.drawLine(ServerGUI.sX, ServerGUI.sY, ServerGUI.eX, ServerGUI.eY);
                    } else if (temp.has("TTT")) {
                        content = (JSONObject) temp.get("TTT");
                        ServerGUI.eX = (Integer) content.get("endX");
                        ServerGUI.eY = (Integer) content.get("endY");
                        ServerGUI.tt = (String) content.get(("boardText"));
                        ServerGUI.paintGraph.drawString(ServerGUI.tt, ServerGUI.eX, ServerGUI.eY);
                    }
                }
            }
        });


        /**
         * socket process
         */
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            JOptionPane.showMessageDialog(null, userName + " is the manager!");
            ServerGUI.yourName.setText("Manager:" + userName);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                // make sure the unique threads
                threadNum += 1;
                threadPool.put(threadNum, clientSocket);
//                threadNum += 1;
                // for better kick, so that we can iterate a array
                int clientNum = threadPool.size() - 1;
                // todo: username is wrong
//                for (Integer id : validatedUsers.keySet()) {
                System.out.println(validatedUsers);
//                ServerGUI.userList.append(threadPool.size() + "-" + validatedUsers.get(threadPool.size()) + "\n");
////                }
                ServerGUI.userList.setText("");
                for (Integer id : validatedUsers.keySet()) {
                    ServerGUI.userList.append(id + " : " + validatedUsers.get(id) + "\n");
                }


                // changes to the server user lists
//                ServerGUI.showUserList(idNameMap);
                // the incoming client
                in = new DataInputStream(clientSocket.getInputStream());
                // add a out thread for the incoming client
                out = new DataOutputStream(clientSocket.getOutputStream());
                // output my canvas info
                outputPool.add(out);
                try {
                    System.out.println("the client num");
                    System.out.println(clientNum);
                    Propagation thread = new Propagation(clientNum, in, outputPool);
                    thread.start();
                } catch (Exception ee) {
                    System.out.println("GOTTA PROBLEM IN OUT");
                }
                System.out.println(CreateWhiteBoard.validatedUsers);

            }


        } catch (
                BindException e) {
            JOptionPane.showMessageDialog(null, "Cannot bind the port");
            System.exit(0);
        } catch (
                IOException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }

    }


    /**
     * for every function in the white board, we store in json and PROPAGATE IT.
     * MAIN PROPAGATE STATION!
     *
     * @param command
     */
    public static void commandOutput(String command) {
        JSONObject cmd = new JSONObject();
        JSONObject func = new JSONObject();
        // record user always
        func.put("user", userName);
        if (command.equalsIgnoreCase("Send")) {
            func.put("text", ServerGUI.typeText.getText().replaceAll("\n", ""));
        } else if (command.equalsIgnoreCase("TTT")) {
            func.put("endX", ServerGUI.eX);
            func.put("endY", ServerGUI.eY);
            func.put("boardText", ServerGUI.tt);
        } else {
            // record color and positions
            String colorS = Integer.toString(ServerGUI.selectedColor.getRGB());
            func.put("color", colorS);
            func.put("startX", ServerGUI.sX);
            func.put("endX", ServerGUI.eX);
            func.put("startY", ServerGUI.sY);
            func.put("endY", ServerGUI.eY);
        }
        // put into the json
        cmd.put(command, func);

        // save server side canvas
        pastCanvas.add(cmd);

        // send to clients of server side canvas
        for (int i = 0; i < outputPool.size(); i++) {
//            if (closedSockets.contains(i)) continue;
            try {
                System.out.println(outputPool);
                outputPool.get(i).writeUTF(String.valueOf(cmd));
                outputPool.get(i).flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String readFileAsString(String file) throws Exception {
        return new String(Files.readAllBytes(Paths.get(file)));
    }
}
