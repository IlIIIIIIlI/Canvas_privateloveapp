package GUI;


import Client.JoinWhiteBoard;
import org.json.JSONObject;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.IOException;


/**
 * TODO: Client GUI, as the words meaning
 *
 * @author Andy
 * @date 5/21/2022 1:45 AM
 */


public class ClientGUI {

    public static JFrame frame;
    // the default colour
    public static Color selectedColor = Color.BLACK;
    // define the coordinates of point
    public static Integer sX = null;
    public static Integer sY = null;
    public static Integer eX = null;
    public static Integer eY = null;
    public static String showText = null;
    public static JButton[] buttonList = new JButton[7];
    public static String selectedButton = "P";
    // paintgraph is inside the paint area
    public static JPanel paintArea;
    public static Graphics paintGraph;
    static JButton colourButton;
    static JTextArea typeText;
    static JButton sendButton;
    public static JLabel yourName = new JLabel("YourName");
    public static DataOutputStream out;
    public static JTextArea textArea;
    public static JTextArea userList;
    private static JTextField textField;
    static JButton textButton;

    /**
     * @wbp.parser.entryPoint
     */
    public static void baseGUI() {
        // set my basis title
        frame = new JFrame();
        frame.setResizable(false);
        frame.setTitle("Andy's Distributed White Board");
        frame.setBounds(100, 100, 759, 528);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 238, 111);
        frame.getContentPane().add(panel);
        panel.setLayout(null);

        JPanel functionPanel = new JPanel();
        functionPanel.setForeground(Color.decode("#fca311"));
        functionPanel.setBounds(0, 0, 251, 123);
        panel.add(functionPanel);
        functionPanel.setLayout(null);

        JLabel functionLabel = new JLabel("Function");
        functionLabel.setBounds(89, 7, 60, 21);
        functionLabel.setFont(new Font("Times New Roman", Font.PLAIN, 17));
        functionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        functionPanel.add(functionLabel);

        /**
         * pens
         */
        ImageIcon imageIcon = new ImageIcon(new ImageIcon("C:\\\\Users\\\\19723\\\\Dropbox\\\\Unimelb\\\\DS_A2\\\\sub\\\\src\\\\main\\\\resources\\\\icons\\\\Pen-2-icon.png").getImage().getScaledInstance(23, 23, Image.SCALE_DEFAULT));
        JButton penButton = new JButton("P");
        penButton.setBounds(16, 36, 55, 31);
        penButton.setToolTipText("Free draw");
//        penButton.addMouseListener(toolSelector(penButton, functionShape.free));
//        penButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//            }
//        });
        penButton.setFont(new Font("Times New Roman", Font.PLAIN, 11));
        penButton.setBackground(Color.WHITE);
        penButton.setForeground(Color.decode("#14213d"));
        penButton.setIcon(imageIcon);
        functionPanel.add(penButton);


        /**
         * lines
         */
        ImageIcon lineIcon = new ImageIcon(new ImageIcon("C:\\\\Users\\\\19723\\\\Dropbox\\\\Unimelb\\\\DS_A2\\\\sub\\\\src\\\\main\\\\resources\\\\icons\\\\Editing-Line-icon.png").getImage().getScaledInstance(23, 23, Image.SCALE_DEFAULT));
        JButton lineButton = new JButton("L");
        lineButton.setBackground(Color.WHITE);
        lineButton.setForeground(Color.decode("#14213d"));
        lineButton.setBounds(92, 36, 55, 31);
        lineButton.setToolTipText("Line");
        lineButton.setIcon(lineIcon);
//        lineButton.addMouseListener(toolSelector(lineButton, functionShape.line));
        functionPanel.add(lineButton);

        /**
         * circles
         */
        ImageIcon circleIcon = new ImageIcon(new ImageIcon("C:\\\\Users\\\\19723\\\\Dropbox\\\\Unimelb\\\\DS_A2\\\\sub\\\\src\\\\main\\\\resources\\\\icons\\\\Arrow-Circle-icon.png").getImage().getScaledInstance(23, 23, Image.SCALE_DEFAULT));
        JButton circleButton = new JButton("C");
        circleButton.setBackground(Color.WHITE);
        circleButton.setForeground(Color.decode("#14213d"));
//        circleButton.addMouseListener(toolSelector(circleButton, functionShape.circle));

        circleButton.setBounds(168, 36, 55, 31);
        circleButton.setToolTipText("Circle");
        circleButton.setIcon(circleIcon);
        functionPanel.add(circleButton);


        /**
         * erasers
         */
        JButton eraserButton = new JButton("E");
        eraserButton.setBounds(16, 79, 55, 31);
        eraserButton.setToolTipText("Eraser");
        eraserButton.setBackground(Color.WHITE);
        eraserButton.setForeground(Color.WHITE);
//        eraserButton.addMouseListener(toolSelector(eraserButton, eraser));

        ImageIcon eraserIcon = new ImageIcon(new ImageIcon("C:\\\\Users\\\\19723\\\\Dropbox\\\\Unimelb\\\\DS_A2\\\\sub\\\\src\\\\main\\\\resources\\\\icons\\\\Eraser-2-icon.png").getImage().getScaledInstance(23, 23, Image.SCALE_DEFAULT));
        eraserButton.setIcon(eraserIcon);
        functionPanel.add(eraserButton);


        /**
         * triangles
         */
        ImageIcon triangleIcon = new ImageIcon(new ImageIcon("C:\\\\Users\\\\19723\\\\Dropbox\\\\Unimelb\\\\DS_A2\\\\sub\\\\src\\\\main\\\\resources\\\\icons\\\\Editing-Triangle-Stroked-icon.png").getImage().getScaledInstance(23, 23, Image.SCALE_DEFAULT));
        JButton triangleButton = new JButton("T");
        triangleButton.setBackground(Color.WHITE);
        triangleButton.setForeground(Color.decode("#14213d"));
//        triangleButton.addMouseListener(toolSelector(triangleButton, triangle));
        triangleButton.setBounds(92, 79, 55, 31);
        triangleButton.setToolTipText("Triangle");
        triangleButton.setIcon(triangleIcon);
        functionPanel.add(triangleButton);

        /**
         * rectangulars
         */
        ImageIcon rectangleIcon = new ImageIcon(new ImageIcon("C:\\\\Users\\\\19723\\\\Dropbox\\\\Unimelb\\\\DS_A2\\\\sub\\\\src\\\\main\\\\resources\\\\icons\\\\Editing-Rectangle-icon.png").getImage().getScaledInstance(23, 23, Image.SCALE_DEFAULT));
        JButton rectangleButton = new JButton("R");
        rectangleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
//        rectangleButton.addMouseListener(toolSelector(rectangleButton, rectangle));
        rectangleButton.setBounds(168, 79, 55, 31);
        rectangleButton.setToolTipText("Rectangular");
        rectangleButton.setIcon(rectangleIcon);
        rectangleButton.setBackground(Color.WHITE);
        rectangleButton.setForeground(Color.decode("#14213d"));
        functionPanel.add(rectangleButton);

        JPanel textPanel = new JPanel();
        textPanel.setForeground(Color.decode("#fca311"));
        textPanel.setBounds(374, 0, 359, 111);
        frame.getContentPane().add(textPanel);
        textPanel.setLayout(null);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(10, 33, 339, 73);
        textPanel.add(scrollPane);

        textArea = new JTextArea();
        textArea.setFont(new Font("Times New Roman", Font.PLAIN, 13));
        scrollPane.setViewportView(textArea);

        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane_1.setBounds(10, 0, 339, 22);
        textPanel.add(scrollPane_1);

        typeText = new JTextArea();
        typeText.setToolTipText("Please Typing...");
        typeText.setFont(new Font("Times New Roman", Font.PLAIN, 13));
        scrollPane_1.setViewportView(typeText);

        sendButton = new JButton("Send");
        sendButton.setBackground(Color.decode("#fca311"));
        sendButton.setForeground(Color.decode("#14213d"));
        sendButton.setFont(new Font("Times New Roman", Font.PLAIN, 13));
        scrollPane_1.setRowHeaderView(sendButton);

        // save all buttons
        buttonList[0] = penButton;      //p - free hand
        buttonList[1] = lineButton;     //l - straight line
        buttonList[2] = circleButton;   //c - circles
        buttonList[3] = eraserButton;   //e - erasers
        buttonList[4] = triangleButton; //t - triangles
        buttonList[5] = rectangleButton;//r - rectangles
        buttonList[6] = sendButton;     //Send - message
        System.out.println(buttonList);

        // except for the colour button
        colourButton = new JButton("Colour");
        colourButton.setBackground(Color.WHITE);
        colourButton.setForeground(Color.decode("#14213d"));
        colourButton.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        colourButton.setBounds(263, 75, 89, 23);
        frame.getContentPane().add(colourButton);

        JLabel userNameLabel = new JLabel("User Name:");
        userNameLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        userNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        userNameLabel.setBounds(248, 11, 116, 28);
        frame.getContentPane().add(userNameLabel);

        // the major paint place
        paintArea = new JPanel();
        paintArea.setBackground(Color.WHITE);
        paintArea.setBounds(10, 116, 639, 362);
        frame.getContentPane().add(paintArea);

        JScrollPane userListPanel = new JScrollPane();
        userListPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        userListPanel.setBounds(659, 123, 74, 269);
        frame.getContentPane().add(userListPanel);

        userList = new JTextArea();
        userList.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        userListPanel.setViewportView(userList);

        JLabel userListLabel = new JLabel("User List");
        userListLabel.setBackground(Color.decode("#fca311"));
        userListLabel.setHorizontalAlignment(SwingConstants.CENTER);
        userListLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        userListPanel.setColumnHeaderView(userListLabel);

        yourName.setHorizontalAlignment(SwingConstants.CENTER);
        yourName.setFont(new Font("Times New Roman", Font.BOLD, 15));
        yourName.setBounds(267, 50, 74, 14);
        frame.getContentPane().add(yourName);

        textField = new JTextField();
        textField.setBounds(659, 405, 74, 39);
        frame.getContentPane().add(textField);
        textField.setColumns(10);

        textButton = new JButton("GoBoard!");
        textButton.setHorizontalAlignment(SwingConstants.LEFT);
        textButton.setBackground(Color.WHITE);
        textButton.setForeground(Color.decode("#14213d"));
        textButton.setFont(new Font("Times New Roman", Font.PLAIN, 11));
        textButton.setBounds(659, 455, 74, 23);
        frame.getContentPane().add(textButton);


        frame.setVisible(true);
        paintGraph = paintArea.getGraphics();

        // what can a colour button do
        textButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showText = textField.getText().replaceAll("\n", "").trim();
                if (!showText.equals("")) {
                    commandOutput("TTT", out);
                    ClientGUI.paintGraph.drawString(showText, eX, eY);
                }
//                    ClientGUI.textArea.append("User:" + JoinWhiteBoard.getUserName()+ " " + text + '\n');
            }
        });


        // what can a colour button do
        colourButton.addMouseListener(new MouseAdapter() {
            JDialog dialog = null;
            JColorChooser cc = null;

            @Override
            public void mouseClicked(MouseEvent e) {
                if (dialog == null) {
                    cc = new JColorChooser(selectedColor);
                    dialog = JColorChooser.createDialog(
                            null,
                            "Enter a colour you like...",
                            true,
                            cc,
                            null,
                            null);
                }
                cc.setColor(selectedColor);
                dialog.setVisible(true);
                dialog.dispose();
                Color newColor = cc.getColor();
                if (newColor != null) {
                    selectedColor = newColor;
                }
            }
        });

        // core
        paintArea.addMouseListener(new MouseListener() {
            // have to have this
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            // define if press the mouse
            @Override
            public void mousePressed(MouseEvent me) {
                // update start for drag
                sX = me.getX();
                sY = me.getY();
//                printBoard(paintGraph);
            }

            @Override
            public void mouseReleased(MouseEvent me) {
//                paintArea.repaint();

                // update end for drag
                eX = me.getX();
                eY = me.getY();


//            int r = (int) Math.sqrt(w * w + h * h);
                System.out.println(selectedButton);
                switch (selectedButton) {
                    case "R":
                        paintGraph.setColor(selectedColor);
                        int x = Math.min(sX, eX);
                        int y = Math.min(sY, eY);
                        int w = Math.abs(eX - sX);
                        int h = Math.abs(eY - sY);
                        paintGraph.drawRect(x, y, w, h);
                        commandOutput("R", out);
                        break;
                    case "T":
                        paintGraph.setColor(selectedColor);
                        paintGraph.drawPolygon(new int[]{sX, eX, ((Math.max(eX - sX, sX - eX) / 2) + Math.min(sX, eX))}, new int[]{sY, eY, (Math.max(eY - sY, sY - eY) / 2 + Math.max(sY, eY))}, 3);
                        commandOutput("T", out);
                        break;
                    case "C":
                        paintGraph.setColor(selectedColor);
                        x = Math.min(sX, eX);
                        y = Math.min(sY, eY);
                        w = Math.abs(eX - sX);
                        h = Math.abs(eY - sY);
                        paintGraph.drawOval(x, y, w, h);
                        commandOutput("C", out);
                        break;
                    case "L":
                        paintGraph.setColor(selectedColor);
                        paintGraph.drawLine(sX, sY, eX, eY);
                        commandOutput("L", out);
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

        paintArea.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent me) {
                // set line stroke
//                if (selectedButton.equals("P") || selectedButton.equals("E")) {
                switch (selectedButton) {
                    case "P":
                        paintGraph.setColor(selectedColor);
                        eX = sX;
                        eY = sY;
                        sX = me.getX();
                        sY = me.getY();
                        paintGraph.drawLine(sX, sY, eX, eY);
                        commandOutput("P", out);
                    case "E":
                        paintGraph.setColor(Color.WHITE);
                        eX = sX;
                        eY = sY;
                        sX = me.getX();
                        sY = me.getY();
                        paintGraph.drawLine(sX, sY, eX, eY);
                        commandOutput("E", out);

                }
            }

            @Override
            public void mouseMoved(MouseEvent me) {
            }
        });

        // lets listen every button, and return their name!
        for (int i = 0; i < buttonList.length; i++) {
            buttonList[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedButton = e.getActionCommand();
                }
            });
        }

        // this is the message button
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = typeText.getText().replaceAll("\n", "").trim();
                if (!text.equals("")) {
                    commandOutput("Send", out);
                    typeText.setText("");
                }
//                    ClientGUI.textArea.append("User:" + JoinWhiteBoard.getUserName()+ " " + text + '\n');
            }
        });
    }

    /**
     * for every function in the white board, we store in json and sent it.
     *
     * @param command
     */
    public static synchronized void commandOutput(String command, DataOutputStream out) {
        JSONObject cmd = new JSONObject();
        JSONObject func = new JSONObject();
        // record user always
        func.put("user", JoinWhiteBoard.getUserName());
        if (command.equalsIgnoreCase("Send")) {
            func.put("text", typeText.getText().replaceAll("\n", ""));
        } else if (command.equalsIgnoreCase("TTT")) {
            func.put("endX", eX);
            func.put("endY", eY);
            func.put("boardText", showText);
        } else {
            // record color and positions
            String colorS = Integer.toString(selectedColor.getRGB());
            func.put("color", colorS);
            func.put("startX", sX);
            func.put("endX", eX);
            func.put("startY", sY);
            func.put("endY", eY);
        }
        // put into the json
        cmd.put(command, func);
        try {
            out.writeUTF(cmd + "\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        System.out.println(cmd.get(command));
        /**
         * {"text":"AAAAAAAAA","user":"Andy"}
         * {"text":"AAAAAAAAAAAAAAAAAA","user":"Andy"}\
         *
         * {"endY":289,"endX":303,"color":"java.awt.Color[r=102,g=255,b=102]","startY":289,"startX":304,"user":"Andy"}
         * {"endY":289,"endX":304,"color":"java.awt.Color[r=102,g=255,b=102]","startY":289,"startX":304,"user":"Andy"}
         */
    }

}

