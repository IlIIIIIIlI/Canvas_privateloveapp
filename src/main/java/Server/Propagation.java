package Server;

import GUI.ServerGUI;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UTFDataFormatException;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * TODO: This class is for propagation to clients.
 *
 * @author Andy
 * @date 5/26/2022 8:13 PM
 */
public class Propagation extends Thread {

    private int socketAllocation;
    private ArrayList<DataOutputStream> outputPool = new ArrayList<>();
    private DataInputStream in;


    public Propagation(int clientNum, DataInputStream In, ArrayList<DataOutputStream> outputPool) {
        this.socketAllocation = clientNum;
        this.in = In;
        this.outputPool = outputPool;
    }

    // @override thread function
    public void run() {
        String input;

        try {
            // we output old canvas to the new thread
            pastCanvas();
            // propagate the user list, for new registers.
            for (int i = 0; i < outputPool.size(); i++) {
                JSONObject func = new JSONObject();
                func.put("userList", ServerGUI.userList.getText());
                outputPool.get(i).writeUTF(String.valueOf(func));
                outputPool.get(i).flush();
            }

            // send commands series
            while (!(input = in.readUTF()).equals("END")) {
//                System.out.println(in);
//                System.out.println("start");
//                System.out.println(input);
                JSONObject commandIn = new JSONObject(input);
                if (commandIn.has("Send")) {
                    JSONObject temp = (JSONObject) commandIn.get("Send");
                    sendText(temp);
                } else {
                    sendDraw(commandIn);
                }
                CreateWhiteBoard.closedSockets.add(socketAllocation);
                CreateWhiteBoard.pastCanvas.add(commandIn);
            }

        } catch (UTFDataFormatException utf) {
            System.out.println("small error");
        } catch (SocketException se) {
            System.out.println("confirm a user is disconnected");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException je) {
            System.out.println("Json file wrong");
        }
    }

    /**
     * send the message
     */
    public synchronized void sendText(JSONObject commandIn) {
        showMessage(commandIn);
        try {
            for (int i = 0; i < outputPool.size(); i++) {
//                if (CreateWhiteBoard.closedSockets.contains(i)) {
//
//                } else {
                JSONObject func = new JSONObject();
                // re-install the ke, as send is opened by outside function
                func.put("Send", commandIn);
                outputPool.get(i).writeUTF(String.valueOf(func));
                outputPool.get(i).flush();
//                }
            }
        } catch (IOException e) {
            System.out.println("sendText is wrong");
        }
    }

    /**
     * past canvas
     */
    public synchronized void pastCanvas() {
        System.out.println("this socket");
        System.out.println(socketAllocation);
        try {
            for (int i = 0; i < CreateWhiteBoard.pastCanvas.size(); i++) {
                DataOutputStream output = outputPool.get(socketAllocation);
                output.writeUTF(CreateWhiteBoard.pastCanvas.get(i) + "\n");
                output.flush();
            }
        } catch (IOException e) {
            System.out.println("sendDraw is wrong");
        }


    }


    /**
     * propagate the received drawings to every client socket
     */
    public synchronized void sendDraw(JSONObject commandIn) {
        // first show on myself board
        showDraw(commandIn);
        try {
            // propagate to every client
            for (int i = 0; i < outputPool.size(); i++) {
                // don't send to the closed clients and the sender
//                if (i != socketAllocation || CreateWhiteBoard.closedSockets.contains(i)) {
//                } else {
                outputPool.get(i).writeUTF(String.valueOf(commandIn));
                outputPool.get(i).flush();
//                }
            }
        } catch (
                IOException e) {
            System.out.println("sendDraw is wrong");
        }

    }

    /**
     * show the message in the server side
     */
    public void showMessage(JSONObject commandIn) {
        String title = "User" + ":";
        String text = (String) commandIn.get("text");
        String user = (String) commandIn.get("user");
        System.out.println(user);
        System.out.println(text);
        ServerGUI.textArea.append(title + user + "  " + text + '\n');
    }


    /**
     * show the drawings in the server paint
     */
    private void showDraw(JSONObject commandIn) {
//        Graphics2D stroke = (Graphics2D) ServerGUI.paintGraph;
//        stroke.setStroke(new BasicStroke(3.7f));
        JSONObject content;
        if (commandIn.has("R")) {
            content = (JSONObject) commandIn.get("R");
            help(content);
            int x = Math.min(ServerGUI.sX, ServerGUI.eX);
            int y = Math.min(ServerGUI.sY, ServerGUI.eY);
            int w = Math.abs(ServerGUI.eX - ServerGUI.sX);
            int h = Math.abs(ServerGUI.eY - ServerGUI.sY);
            ServerGUI.paintGraph.drawRect(x, y, w, h);

        } else if (commandIn.has("C")) {
            content = (JSONObject) commandIn.get("C");
            help(content);
            int x = Math.min(ServerGUI.sX, ServerGUI.eX);
            int y = Math.min(ServerGUI.sY, ServerGUI.eY);
            int w = Math.abs(ServerGUI.eX - ServerGUI.sX);
            int h = Math.abs(ServerGUI.eY - ServerGUI.sY);
            ServerGUI.paintGraph.drawOval(x, y, w, h);
        } else if (commandIn.has("T")) {
            content = (JSONObject) commandIn.get("T");
            help(content);
            ServerGUI.paintGraph.drawPolygon(new int[]{ServerGUI.sX, ServerGUI.eX, ((Math.max(ServerGUI.eX - ServerGUI.sX, ServerGUI.sX - ServerGUI.eX) / 2) + Math.min(ServerGUI.sX, ServerGUI.eX))}, new int[]{ServerGUI.sY, ServerGUI.eY, (Math.max(ServerGUI.eY - ServerGUI.sY, ServerGUI.sY - ServerGUI.eY) / 2 + Math.max(ServerGUI.sY, ServerGUI.eY))}, 3);

        } else if (commandIn.has("P")) {
            content = (JSONObject) commandIn.get("P");
            help(content);
            ServerGUI.paintGraph.drawLine(ServerGUI.sX, ServerGUI.sY, ServerGUI.eX, ServerGUI.eY);

        } else if (commandIn.has("L")) {
            content = (JSONObject) commandIn.get("L");
            help(content);
            ServerGUI.paintGraph.drawLine(ServerGUI.sX, ServerGUI.sY, ServerGUI.eX, ServerGUI.eY);

        } else if (commandIn.has("E")) {
            content = (JSONObject) commandIn.get("E");
            help(content);
            ServerGUI.paintGraph.setColor(Color.WHITE);
            ServerGUI.paintGraph.drawLine(ServerGUI.sX, ServerGUI.sY, ServerGUI.eX, ServerGUI.eY);
        } else if (commandIn.has("TTT")) {
            content = (JSONObject) commandIn.get("TTT");
            ServerGUI.eX = (Integer) content.get("endX");
            ServerGUI.eY = (Integer) content.get("endY");
            ServerGUI.tt = (String) content.get(("boardText"));
            ServerGUI.paintGraph.drawString(ServerGUI.tt, ServerGUI.eX, ServerGUI.eY);
        }

    }

    /**
     * a moudle help to draw on server face
     */
    public void help(JSONObject content) {
        Color c = new Color(Integer.parseInt(String.valueOf(content.get("color"))));
        ServerGUI.paintGraph.setColor(c);
        ServerGUI.sX = (Integer) content.get("startX");
        ServerGUI.eX = (Integer) content.get("endX");
        ServerGUI.sY = (Integer) content.get("startY");
        ServerGUI.eY = (Integer) content.get("endY");
    }


}
