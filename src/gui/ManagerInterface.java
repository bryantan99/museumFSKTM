package gui;

import simulator.Simulator;
import component.BookManageComponent;
import constant.Constant;
import utilities.*;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ManagerInterface {


    public static JFrame jf = new JFrame("The information of Museum");

    final int WIDTH = 1200;
    final int HEIGHT = 800;
    final Color bgColor = new Color(203,220,217);

    public Simulator sim = null;
    private boolean isStart = false;

    public static BookManageComponent ticketCounterTable = new BookManageComponent(jf, Constant.TICKET_TABLE_TITLE);
    public static BookManageComponent museumTable = new BookManageComponent(jf, Constant.MUSEUM_TABLE_TITLE);
    public static BookManageComponent southEntranceTable = new BookManageComponent(jf, Constant.ENTRANCE_TABLE_TITLE);
    public static BookManageComponent northEntranceTable = new BookManageComponent(jf, Constant.ENTRANCE_TABLE_TITLE);
    public static BookManageComponent eastExitTable = new BookManageComponent(jf, Constant.EXIT_TABLE_TITLE);
    public static BookManageComponent westExitTable = new BookManageComponent(jf, Constant.EXIT_TABLE_TITLE);
    public static JTextArea jTextArea = new JTextArea();

    public void init() throws Exception {

        sim = new Simulator();

        // set window attribute
        jf.setBounds((ScreenUtils.getScreenWidth() - WIDTH) / 2, (ScreenUtils.getScreenHeight() - HEIGHT) / 2, WIDTH, HEIGHT);
        jf.setResizable(false);

        // set the menu bar
        JMenuBar jmb = new JMenuBar();
        JMenu jMenu = new JMenu("Setting");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        jMenu.add(exitItem);
        jmb.add(jMenu);
        jf.setJMenuBar(jmb);

        //startStimulate button
        JPanel topPanel = new JPanel();
        topPanel.setBackground(bgColor);
        Box btnBox = Box.createHorizontalBox();
        JButton startBtn = new JButton("Start Stimulate");
        startBtn.addActionListener(e ->{
            try{
                if(!isStart){
                    sim.startSimulate();
                    isStart = true;
                    startBtn.setBackground(Color.lightGray);
                    startBtn.setText("Stimulating...");
                    startBtn.setEnabled(false);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        btnBox.add(startBtn,Component.CENTER_ALIGNMENT);
        topPanel.add(startBtn,Component.CENTER_ALIGNMENT);
        jf.add(topPanel,BorderLayout.NORTH);

        // set the split pane
        JPanel splitPanePanel = new JPanel();
        splitPanePanel.setLayout(new BorderLayout());
        splitPanePanel.setBackground(bgColor);
        JSplitPane sp = new JSplitPane();

        // set continuous layout
        sp.setContinuousLayout(true);
        sp.setDividerLocation(200);
        sp.setDividerSize(7);

        // set left content
        DefaultMutableTreeNode museum = new DefaultMutableTreeNode("Museum");
        DefaultMutableTreeNode ticketCounter = new DefaultMutableTreeNode("Ticket Counter");
        DefaultMutableTreeNode usersInMuseum = new DefaultMutableTreeNode("Customers In Museum");
        DefaultMutableTreeNode usersInSouthEntrance = new DefaultMutableTreeNode("Customers In South Entrance");
        DefaultMutableTreeNode usersInNorthEntrance = new DefaultMutableTreeNode("Customers In North Entrance");
        DefaultMutableTreeNode usersInEastExit = new DefaultMutableTreeNode("Customers In East Exit");
        DefaultMutableTreeNode usersInWestExit = new DefaultMutableTreeNode("Customers In West Exit");

        museum.add(ticketCounter);
        museum.add(usersInMuseum);
        museum.add(usersInSouthEntrance);
        museum.add(usersInNorthEntrance);
        museum.add(usersInEastExit);
        museum.add(usersInWestExit);

        Color color = new Color(203,220,217);
        JTree tree = new JTree(museum);
        tree.setBackground(bgColor);
        MyRenderer myRenderer = new MyRenderer();

        myRenderer.setBackgroundNonSelectionColor(color);
        myRenderer.setBackgroundSelectionColor(new Color(140,140,140));

        tree.setCellRenderer(myRenderer);
        tree.setSelectionRow(1);

        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                Object lastPathComponent = e.getNewLeadSelectionPath().getLastPathComponent();
                if (usersInMuseum.equals(lastPathComponent)){
                    sp.setRightComponent(museumTable);
                    sp.setDividerLocation(200);
                }else if (usersInSouthEntrance.equals(lastPathComponent)){
                    sp.setRightComponent(southEntranceTable);
                    sp.setDividerLocation(200);
                }else if (usersInNorthEntrance.equals(lastPathComponent)){
                    sp.setRightComponent(northEntranceTable);
                    sp.setDividerLocation(200);
                }else if (usersInEastExit.equals(lastPathComponent)){
                    sp.setRightComponent(eastExitTable);
                    sp.setDividerLocation(200);
                }else if (ticketCounter.equals(lastPathComponent)){
                    sp.setRightComponent(ticketCounterTable);
                    sp.setDividerLocation(200);
                }else if (usersInWestExit.equals(lastPathComponent)){
                    sp.setRightComponent(westExitTable);
                    sp.setDividerLocation(200);
                }else{
                    sp.setRightComponent(museumTable);
                    sp.setDividerLocation(200);
                }
            }
        });

        sp.setRightComponent(ticketCounterTable);
        sp.setLeftComponent(tree);
        sp.setBackground(bgColor);
        splitPanePanel.add(sp,BorderLayout.NORTH);

        Box outputBox = Box.createHorizontalBox();
        JScrollPane jScrollPane = new JScrollPane(jTextArea);
        jTextArea.setFont(DesignUtils.getFont());
        outputBox.add(jScrollPane);
        splitPanePanel.add(outputBox);
        jf.add(splitPanePanel);
        jf.setVisible(true);

        jf.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                jf.dispose();
                System.exit(0); //calling the method is a must
            }
        });

    }

    // rendered
    private class MyRenderer extends DefaultTreeCellRenderer {
        private ImageIcon rootIcon = null;
        private ImageIcon userManageIcon = null;
        private ImageIcon bookManageIcon = null;
        private ImageIcon borrowManageIcon = null;
        private ImageIcon statisticsManageIcon = null;

        public MyRenderer() {
            rootIcon = new ImageIcon(PathUtils.getRealPath("systemManage.png"));
            userManageIcon = new ImageIcon(PathUtils.getRealPath("userManage.png"));
            bookManageIcon = new ImageIcon(PathUtils.getRealPath("bookManage.png"));
            borrowManageIcon = new ImageIcon(PathUtils.getRealPath("borrowManage.png"));
            statisticsManageIcon = new ImageIcon(PathUtils.getRealPath("statisticsManage.png"));
        }

        //
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

            ImageIcon image = null;
            switch (row) {
                case 0:
                    image = rootIcon;
                    break;
                case 1:
                    image = userManageIcon;
                    break;
                case 2:
                    image = bookManageIcon;
                    break;
                case 3:
                    image = borrowManageIcon;
                    break;
                case 4:
                    image = statisticsManageIcon;
                    break;
                case 5:
                    image = userManageIcon;
                    break;
                case 6:
                    image = bookManageIcon;
                    break;
            }

            this.setIcon(image);
            return this;
        }
    }
}
