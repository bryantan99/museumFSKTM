package gui;

import Simulator.Simulator;
import component.BookManageComponent;
import constant.Constant;
import museum.Museum;
import museum.Ticket;
import museum.TicketCounter;
import utilities.*;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManagerInterface {


    public static JFrame jf = new JFrame("The information of Museum");

    final int WIDTH = 1200;
    final int HEIGHT = 800;

    public static BookManageComponent ticketCounterTable = new BookManageComponent(jf, Constant.TICKET_TABLE_TITLE);
    public static BookManageComponent museumTable = new BookManageComponent(jf, Constant.MUSEUM_TABLE_TITLE);
    public static BookManageComponent southEntranceTable = new BookManageComponent(jf, Constant.ENTRANCE_TABLE_TITLE);
    public static BookManageComponent northEntranceTable = new BookManageComponent(jf, Constant.ENTRANCE_TABLE_TITLE);
    public static BookManageComponent eastExitTable = new BookManageComponent(jf, Constant.EXIT_TABLE_TITLE);
    public static BookManageComponent westExitTable = new BookManageComponent(jf, Constant.EXIT_TABLE_TITLE);

    public void init() throws Exception {
        // set window attribute
        jf.setBounds((ScreenUtils.getScreenWidth() - WIDTH) / 2, (ScreenUtils.getScreenHeight() - HEIGHT) / 2, WIDTH, HEIGHT);
        jf.setResizable(false);

        // set the menu bar
        JMenuBar jmb = new JMenuBar();
        JMenu jMenu = new JMenu("Seting");
        JMenuItem m1 = new JMenuItem("Switch Account");
        JMenuItem m2 = new JMenuItem("Exit");
        m1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new LoginInterface().init();
                    jf.dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        m2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        jMenu.add(m1);
        jMenu.add(m2);
        jmb.add(jMenu);

        jf.setJMenuBar(jmb);

        // set the split pane
        JSplitPane sp = new JSplitPane();

        // set continuour layout
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
        MyRenderer myRenderer = new MyRenderer();
        myRenderer.setBackgroundNonSelectionColor(color);
        myRenderer.setBackgroundSelectionColor(new Color(140,140,140));
        tree.setCellRenderer(myRenderer);

        tree.setBackground(color);
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

        /* output log  --> textArea (public static --> entranceTurnstile museum exitTurnstile)
            list -> append
            textArea -> scroll


         */

        jf.add(sp);
        jf.setVisible(true);

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
            //使用默认绘制
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

    public BookManageComponent getMuseumTable() {
        return museumTable;
    }

    public BookManageComponent getSouthEntranceTable() {
        return southEntranceTable;
    }

    public BookManageComponent getNorthEntranceTable() {
        return northEntranceTable;
    }

    public BookManageComponent getEastExitTable() {
        return eastExitTable;
    }

    public BookManageComponent getWestExitTable() {
        return westExitTable;
    }
}