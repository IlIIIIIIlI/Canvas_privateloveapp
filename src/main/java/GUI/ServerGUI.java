package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * TODO: Server GUI, as the words meaning
 *
 * @author Andy
 * @date 5/21/2022 1:45 AM
 */
public class ServerGUI {
    public static String tt;
    public static JFrame frame;
    public static JTextField kickName;
    // the default colour
    public static Color selectedColor = Color.BLACK;
    // define the coordinates of point
    public static Integer sX = null;
    public static Integer sY = null;
    public static Integer eX = null;
    public static Integer eY = null;
    public static JButton[] buttonList = new JButton[7];
    public BufferedImage board;
    public static String selectedButton = "P";
    // paintgraph is inside the paint area
    public static JPanel paintArea;
    public static Graphics paintGraph;
    public static JButton colourButton;
    public static JTextArea typeText;
    public static JButton sendButton;
    public static JButton kickButton;
    public static JLabel yourName = new JLabel("Manager");
    public static JTextArea textArea;
    public static JTextArea userList;
    public static JMenuItem menuNew;
    public static JMenuItem menuOpen;
    public static JMenuItem menuSave;
    public static JMenuItem menuSaveAs;
    public static JMenuItem menuClose;

    /**
     * @wbp.parser.entryPoint
     */
    public void ServerFunction() {
        frame = new JFrame();
        frame.setResizable(false);
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
        scrollPane.setBounds(10, 48, 361, 58);
        textPanel.add(scrollPane);

        textArea = new JTextArea();
        textArea.setFont(new Font("Times New Roman", Font.PLAIN, 13));
        scrollPane.setViewportView(textArea);

        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane_1.setBounds(10, 0, 361, 37);
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
        colourButton.setBounds(267, 88, 89, 23);
        frame.getContentPane().add(colourButton);

        JLabel userNameLabel = new JLabel("User Name:");
        userNameLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        userNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        userNameLabel.setBounds(248, 24, 116, 28);
        frame.getContentPane().add(userNameLabel);

        // the major paint place
        paintArea = new JPanel();
        paintArea.setBackground(Color.WHITE);
        paintArea.setBounds(10, 116, 639, 362);
        frame.getContentPane().add(paintArea);

        JScrollPane userListPanel = new JScrollPane();
        userListPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        userListPanel.setBounds(659, 123, 74, 258);
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
        yourName.setFont(new Font("Times New Roman", Font.BOLD, 11));
        yourName.setBounds(237, 63, 137, 14);
        frame.getContentPane().add(yourName);

        frame.setTitle("Manager");

        JPanel kickPanel = new JPanel();
        kickPanel.setBounds(659, 385, 74, 69);
        frame.getContentPane().add(kickPanel);

        // define a kick function for server, extends the client gui
        kickName = new JTextField();
        kickName.setFont(new Font("Times New Roman", Font.PLAIN, 13));
        kickName.setHorizontalAlignment(SwingConstants.CENTER);
        kickPanel.add(kickName);
        kickName.setColumns(7);

        kickButton = new JButton("Kick!");
        kickPanel.add(kickButton);
        kickButton.setBackground(Color.decode("#fca311"));
        kickButton.setForeground(Color.decode("#14213d"));
        kickButton.setFont(new Font("Times New Roman", Font.PLAIN, 16));

        // define a menu bar for server
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.WHITE);
        menuBar.setForeground(Color.WHITE);
        frame.setJMenuBar(menuBar);

        JMenu mnNewMenu = new JMenu("File");
        mnNewMenu.setFont(new Font("Times New Roman", Font.BOLD, 15));
        mnNewMenu.setHorizontalAlignment(SwingConstants.CENTER);
        menuBar.add(mnNewMenu);

        menuNew = new JMenuItem("New");
        menuNew.setHorizontalAlignment(SwingConstants.LEFT);
        menuNew.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        mnNewMenu.add(menuNew);

        menuOpen = new JMenuItem("Open");
        menuOpen.setHorizontalAlignment(SwingConstants.LEFT);
        menuOpen.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        mnNewMenu.add(menuOpen);

        menuSave = new JMenuItem("Save");
        menuSave.setHorizontalAlignment(SwingConstants.LEFT);
        menuSave.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        mnNewMenu.add(menuSave);

        menuSaveAs = new JMenuItem("SaveAs");
        menuSaveAs.setHorizontalAlignment(SwingConstants.LEFT);
        menuSaveAs.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        mnNewMenu.add(menuSaveAs);

        menuClose = new JMenuItem("Close");
        menuClose.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        menuClose.setHorizontalAlignment(SwingConstants.LEFT);
        mnNewMenu.add(menuClose);

        // make sure the graph is printed after set visible
        frame.setVisible(true);
        paintGraph = paintArea.getGraphics();
    }
}
