package gui;

import component.TableComponent;
import simulator.Simulator;
import constant.Constant;
import utilities.*;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ManagerInterface {

    public static JFrame jf = new JFrame(Constant.WINDOW_TITLE);

    final int WIDTH = Constant.WINDOW_WIDTH;
    final int HEIGHT = Constant.WINDOW_HEIGHT;
    final Color BG_COLOR = Constant.WINDOW_BACKGROUND_COLOUR;

    public Simulator sim = null;
    private boolean isStart = false;

    public static TableComponent ticketCounterTable = new TableComponent(jf, Constant.TICKET_TABLE_TITLE);
    public static TableComponent museumTable = new TableComponent(jf, Constant.MUSEUM_TABLE_TITLE);
    public static TableComponent southEntranceTable = new TableComponent(jf, Constant.ENTRANCE_TABLE_TITLE);
    public static TableComponent northEntranceTable = new TableComponent(jf, Constant.ENTRANCE_TABLE_TITLE);
    public static TableComponent eastExitTable = new TableComponent(jf, Constant.EXIT_TABLE_TITLE);
    public static TableComponent westExitTable = new TableComponent(jf, Constant.EXIT_TABLE_TITLE);
    public static JTextArea jTextArea = new JTextArea();

    public void init() throws Exception {

        sim = new Simulator();

        // set window attribute
        jf.setBounds((ScreenUtils.getScreenWidth() - WIDTH) / 2, (ScreenUtils.getScreenHeight() - HEIGHT) / 2, WIDTH, HEIGHT);
        jf.setResizable(false);

        //startStimulate button
        JPanel topPanel = new JPanel();
        topPanel.setBackground(BG_COLOR);
        Box btnBox = Box.createHorizontalBox();
        JButton startBtn = new JButton(Constant.BUTTON_START);
        startBtn.addActionListener(e ->{
            try{
                if(!isStart){
                    sim.startSimulate();
                    isStart = true;
                    startBtn.setBackground(Color.lightGray);
                    startBtn.setText(Constant.BUTTON_SIMULATING);
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
        splitPanePanel.setBackground(BG_COLOR);
        JSplitPane sp = new JSplitPane();

        // set continuous layout
        sp.setContinuousLayout(true);
        sp.setDividerLocation(200);
        sp.setDividerSize(7);

        // set left content
        DefaultMutableTreeNode museum = new DefaultMutableTreeNode(Constant.LEFT_PANEL_MUSEUM);
        DefaultMutableTreeNode ticketCounter = new DefaultMutableTreeNode(Constant.LEFT_PANEL_TICKET_COUNTER);
        DefaultMutableTreeNode usersInMuseum = new DefaultMutableTreeNode(Constant.LEFT_PANEL_VISITORS_ENTRANCE_EXIT_LOG);
        DefaultMutableTreeNode usersInSouthEntrance = new DefaultMutableTreeNode(Constant.LEFT_PANEL_SOUTH_ENTRANCE_LOG);
        DefaultMutableTreeNode usersInNorthEntrance = new DefaultMutableTreeNode(Constant.LEFT_PANEL_NORTH_ENTRANCE_LOG);
        DefaultMutableTreeNode usersInEastExit = new DefaultMutableTreeNode(Constant.LEFT_PANEL_EAST_EXIT_LOG);
        DefaultMutableTreeNode usersInWestExit = new DefaultMutableTreeNode(Constant.LEFT_PANEL_WEST_EXIT_LOG);

        museum.add(ticketCounter);
        museum.add(usersInMuseum);
        museum.add(usersInSouthEntrance);
        museum.add(usersInNorthEntrance);
        museum.add(usersInEastExit);
        museum.add(usersInWestExit);

        Color color = new Color(203,220,217);
        JTree tree = new JTree(museum);
        tree.setBackground(BG_COLOR);
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
        sp.setBackground(BG_COLOR);
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
        private ImageIcon ticketCounterIcon = null;
        private ImageIcon documentIcon = null;

        public MyRenderer() {
            rootIcon = new ImageIcon(PathUtils.getRealPath("systemManage.png"));
            ticketCounterIcon = new ImageIcon(PathUtils.getRealPath("userManage.png"));
            documentIcon = new ImageIcon(PathUtils.getRealPath("bookManage.png"));
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

            ImageIcon image = null;
            switch (row) {
                case 0:
                    image = rootIcon;
                    break;
                case 1:
                    image = ticketCounterIcon;
                    break;
                case 2:
                    image = documentIcon;
                    break;
                case 3:
                    image = documentIcon;
                    break;
                case 4:
                    image = documentIcon;
                    break;
                case 5:
                    image = documentIcon;
                    break;
                case 6:
                    image = documentIcon;
                    break;
            }

            this.setIcon(image);
            return this;
        }
    }
}
