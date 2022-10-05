//package GUI;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.*;
//import java.io.DataOutputStream;
//import java.io.IOException;
//
//import org.json.JSONObject;
//import GUI.ClientGUI;
//
//import static GUI.ClientGUI.selectedButton;
//
///**
// * TODO: Write a comment describing your class here.
// *
// * @author Andy
// * @date 5/21/2022 4:04 AM
// */
//public class Function {
//    private static String userName;
//    public static DataOutputStream out;
//
//    public Function(String userName) {
//        Function.userName = userName;
//        ClientGUI.baseGUI();
//    }
//
//    public static void enrichGUI() {
//        ClientGUI.frame.setVisible(true);
//        ClientGUI.paintGraph = ClientGUI.paintArea.getGraphics();
//
//        // what can a colour button do
//        ClientGUI.colourButton.addMouseListener(new MouseAdapter() {
//            JDialog dialog = null;
//            JColorChooser cc = null;
//
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                if (dialog == null) {
//                    cc = new JColorChooser(ClientGUI.selectedColor);
//                    dialog = JColorChooser.createDialog(
//                            null,
//                            "Enter a colour you like...",
//                            true,
//                            cc,
//                            null,
//                            null);
//                }
//                cc.setColor(ClientGUI.selectedColor);
//                dialog.setVisible(true);
//                dialog.dispose();
//                Color newColor = cc.getColor();
//                if (newColor != null) {
//                    ClientGUI.selectedColor = newColor;
//                }
//            }
//        });
//
//        // core
//        ClientGUI.paintArea.addMouseListener(new MouseListener() {
//            // have to have this
//            @Override
//            public void mouseClicked(MouseEvent e) {
//            }
//
//            // define if press the mouse
//            @Override
//            public void mousePressed(MouseEvent me) {
//                // update start for drag
//                ClientGUI.sX = me.getX();
//                ClientGUI.sY = me.getY();
////                printBoard(paintGraph);
//            }
//
//            @Override
//            public void mouseReleased(MouseEvent me) {
////                paintArea.repaint();
//
//                // update end for drag
//                ClientGUI.eX = me.getX();
//                ClientGUI.eY = me.getY();
//
//
////            int r = (int) Math.sqrt(w * w + h * h);
//                System.out.println(selectedButton);
//                switch (selectedButton) {
//                    case "R":
//                        ClientGUI.paintGraph.setColor(ClientGUI.selectedColor);
//                        int x = Math.min(ClientGUI.sX, ClientGUI.eX);
//                        int y = Math.min(ClientGUI.sY, ClientGUI.eY);
//                        int w = Math.abs(ClientGUI.eX - ClientGUI.sX);
//                        int h = Math.abs(ClientGUI.eY - ClientGUI.sY);
//                        ClientGUI.paintGraph.drawRect(x, y, w, h);
//                        commandOutput("R", out);
//                        break;
//                    case "T":
//                        ClientGUI.paintGraph.setColor(ClientGUI.selectedColor);
//                        ClientGUI.paintGraph.drawPolygon(new int[]{ClientGUI.sX, ClientGUI.eX, ((Math.max(ClientGUI.eX - ClientGUI.sX, ClientGUI.sX - ClientGUI.eX) / 2) + Math.min(ClientGUI.sX, ClientGUI.eX))}, new int[]{ClientGUI.sY, ClientGUI.eY, (Math.max(ClientGUI.eY - ClientGUI.sY, ClientGUI.sY - ClientGUI.eY) / 2 + Math.max(ClientGUI.sY, ClientGUI.eY))}, 3);
//                        commandOutput("T", out);
//                        break;
//                    case "C":
//                        ClientGUI.paintGraph.setColor(ClientGUI.selectedColor);
//                        x = Math.min(ClientGUI.sX, ClientGUI.eX);
//                        y = Math.min(ClientGUI.sY, ClientGUI.eY);
//                        w = Math.abs(ClientGUI.eX - ClientGUI.sX);
//                        h = Math.abs(ClientGUI.eY - ClientGUI.sY);
//                        ClientGUI.paintGraph.drawOval(x, y, w, h);
//                        commandOutput("C", out);
//                        break;
//                    case "L":
//                        ClientGUI.paintGraph.setColor(ClientGUI.selectedColor);
//                        ClientGUI.paintGraph.drawLine(ClientGUI.sX, ClientGUI.sY, ClientGUI.eX, ClientGUI.eY);
//                        commandOutput("L", out);
//                        break;
//                    default:
//                        break;
//                }
//            }
//
//            @Override
//            public void mouseEntered(MouseEvent e) {
//            }
//
//            @Override
//            public void mouseExited(MouseEvent e) {
//            }
//        });
//
//        ClientGUI.paintArea.addMouseMotionListener(new MouseMotionAdapter() {
//            @Override
//            public void mouseDragged(MouseEvent me) {
//                // set line stroke
//                Graphics2D stroke = (Graphics2D) ClientGUI.paintGraph;
//                stroke.setStroke(new BasicStroke(3.7f));
//
////                if (ClientGUI.selectedButton.equals("P") || ClientGUI.selectedButton.equals("E")) {
//                switch (ClientGUI.selectedButton) {
//                    case "P":
//                        ClientGUI.paintGraph.setColor(ClientGUI.selectedColor);
//                        ClientGUI.eX = ClientGUI.sX;
//                        ClientGUI.eY = ClientGUI.sY;
//                        ClientGUI.sX = me.getX();
//                        ClientGUI.sY = me.getY();
//                        ClientGUI.paintGraph.drawLine(ClientGUI.sX, ClientGUI.sY, ClientGUI.eX, ClientGUI.eY);
//                        commandOutput("P", out);
//                    case "E":
//                        ClientGUI.paintGraph.setColor(Color.WHITE);
//                        ClientGUI.eX = ClientGUI.sX;
//                        ClientGUI.eY = ClientGUI.sY;
//                        ClientGUI.sX = me.getX();
//                        ClientGUI.sY = me.getY();
//                        ClientGUI.paintGraph.drawLine(ClientGUI.sX, ClientGUI.sY, ClientGUI.eX, ClientGUI.eY);
//                        commandOutput("E", out);
//
//                }
//            }
//
//            @Override
//            public void mouseMoved(MouseEvent me) {
//            }
//        });
//
//        // lets listen every button, and return their name!
//        for (int i = 0; i < ClientGUI.buttonList.length; i++) {
//            ClientGUI.buttonList[i].addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    selectedButton = e.getActionCommand();
//                }
//            });
//        }
//
//        // this is the message button
//        ClientGUI.sendButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String text = ClientGUI.typeText.getText().replaceAll("\n", "").trim();
//                if (!text.equals("")) {
//                    commandOutput("Send", out);
//                    ClientGUI.typeText.setText("");
//                }
//            }
//        });
//
//    }
//
//
//    /**
//     * for every function in the white board, we store in json and sent it.
//     *
//     * @param command
//     */
//    public static void commandOutput(String command, DataOutputStream out) {
//        JSONObject cmd = new JSONObject();
//        JSONObject func = new JSONObject();
//        // record user always
//        func.put("user", userName);
//        if (command.equalsIgnoreCase("Send")) {
//            func.put("text", ClientGUI.typeText.getText().replaceAll("\n", ""));
//        } else {
//            // record color and positions
//            String colorS = Integer.toString(ClientGUI.selectedColor.getRGB());
//            func.put("color", colorS);
//            func.put("startX", ClientGUI.sX);
//            func.put("endX", ClientGUI.eX);
//            func.put("startY", ClientGUI.sY);
//            func.put("endY", ClientGUI.eY);
//        }
//        // put into the json
//        cmd.put(command, func);
//        try {
//            out.writeUTF(String.valueOf(cmd));
//            out.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
////        System.out.println(cmd.get(command));
//        /**
//         * {"text":"AAAAAAAAA","user":"Andy"}
//         * {"text":"AAAAAAAAAAAAAAAAAA","user":"Andy"}\
//         *
//         * {"endY":289,"endX":303,"color":"java.awt.Color[r=102,g=255,b=102]","startY":289,"startX":304,"user":"Andy"}
//         * {"endY":289,"endX":304,"color":"java.awt.Color[r=102,g=255,b=102]","startY":289,"startX":304,"user":"Andy"}
//         */
//    }
//}
//
//
//
