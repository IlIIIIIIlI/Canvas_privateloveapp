package Client;

import GUI.ClientGUI;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * TODO: this is actually the each client thread will do
 *
 * @author Andy
 * @date 5/17/2022 9:46 PM
 */

public class Client extends Thread {

    private DataInputStream in;

    public Client(DataInputStream in) {
        this.in = in;
    }

    // override the thread running way
    public void run() {
        String input;

        // 200 millis is the secret :)
        try {
            while (!(input = in.readUTF()).equals("END")) {
                JSONObject commandIn = new JSONObject(input);
                if (commandIn.has("Send")) {
                    sleep(200);
                    System.out.println("send");
                    System.out.println(commandIn);
                    JSONObject temp = (JSONObject) commandIn.get("Send");
                    System.out.println(temp);
                    showMessage(temp);
                } else if (commandIn.has("Close")) {
                    sleep(200);
                    JOptionPane.showMessageDialog(null, "You have been kicked out");
                    System.exit(0);
                } else if (commandIn.has("New")) {
                    sleep(200);
                    ClientGUI.paintArea.repaint();
                } else if (commandIn.has("userList")) {
                    sleep(200);
                    ClientGUI.userList.setText("");
                    ClientGUI.userList.setText(String.valueOf(commandIn.get("userList")));
                } else if (commandIn.has("TTT")) {
                    sleep(200);
                    JSONObject content = (JSONObject) commandIn.get("TTT");
                    ClientGUI.eX = (Integer) content.get("endX");
                    ClientGUI.eY = (Integer) content.get("endY");
                    ClientGUI.showText = (String) content.get(("boardText"));
                    ClientGUI.paintGraph.drawString(ClientGUI.showText, ClientGUI.eX, ClientGUI.eY);

                } else {
                    sleep(1);
                    showDraw(commandIn);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Warning: Connection from manager is lost...");
            System.exit(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
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
        ClientGUI.textArea.append(title + user + "  " + text + '\n');
    }


    private void showDraw(JSONObject commandIn) {
//        Graphics2D stroke = (Graphics2D) ClientGUI.paintGraph;
//        stroke.setStroke(new BasicStroke(3.7f));
        JSONObject content;
        if (commandIn.has("R")) {
            content = (JSONObject) commandIn.get("R");
            help(content);
            int x = Math.min(ClientGUI.sX, ClientGUI.eX);
            int y = Math.min(ClientGUI.sY, ClientGUI.eY);
            int w = Math.abs(ClientGUI.eX - ClientGUI.sX);
            int h = Math.abs(ClientGUI.eY - ClientGUI.sY);
            ClientGUI.paintGraph.drawRect(x, y, w, h);

        } else if (commandIn.has("C")) {
            content = (JSONObject) commandIn.get("C");
            help(content);
            int x = Math.min(ClientGUI.sX, ClientGUI.eX);
            int y = Math.min(ClientGUI.sY, ClientGUI.eY);
            int w = Math.abs(ClientGUI.eX - ClientGUI.sX);
            int h = Math.abs(ClientGUI.eY - ClientGUI.sY);
            ClientGUI.paintGraph.drawOval(x, y, w, h);
        } else if (commandIn.has("T")) {
            content = (JSONObject) commandIn.get("T");
            help(content);
            ClientGUI.paintGraph.drawPolygon(new int[]{ClientGUI.sX, ClientGUI.eX, ((Math.max(ClientGUI.eX - ClientGUI.sX, ClientGUI.sX - ClientGUI.eX) / 2) + Math.min(ClientGUI.sX, ClientGUI.eX))}, new int[]{ClientGUI.sY, ClientGUI.eY, (Math.max(ClientGUI.eY - ClientGUI.sY, ClientGUI.sY - ClientGUI.eY) / 2 + Math.max(ClientGUI.sY, ClientGUI.eY))}, 3);

        } else if (commandIn.has("P")) {
            content = (JSONObject) commandIn.get("P");
            help(content);
            ClientGUI.paintGraph.drawLine(ClientGUI.sX, ClientGUI.sY, ClientGUI.eX, ClientGUI.eY);

        } else if (commandIn.has("L")) {
            content = (JSONObject) commandIn.get("L");
            help(content);
            ClientGUI.paintGraph.drawLine(ClientGUI.sX, ClientGUI.sY, ClientGUI.eX, ClientGUI.eY);

        } else if (commandIn.has("E")) {
            content = (JSONObject) commandIn.get("E");
            help(content);
            ClientGUI.paintGraph.setColor(Color.WHITE);
            ClientGUI.paintGraph.drawLine(ClientGUI.sX, ClientGUI.sY, ClientGUI.eX, ClientGUI.eY);
        }

    }

    /**
     * a moudle help to draw on server face
     */
    public void help(JSONObject content) {
        Color c = new Color(Integer.parseInt(String.valueOf(content.get("color"))));
        ClientGUI.paintGraph.setColor(c);
        ClientGUI.sX = (Integer) content.get("startX");
        ClientGUI.eX = (Integer) content.get("endX");
        ClientGUI.sY = (Integer) content.get("startY");
        ClientGUI.eY = (Integer) content.get("endY");
    }

}