//package gui;
//
//import component.BookManageComponent;
//import constant.Constant;
//import simulator.Simulator;
//
//import javax.swing.*;
//
//public class Admin extends JFrame{
//    public static JButton startStimulateButton;
//    public static JPanel stimulate;
//    public static JTextArea output;
//    public static JTree tree;
//    public static JTree tableTree;
//    public static JPanel outputPanel;
//    public static JSplitPane splitPane;
//    public static JPanel adminPanel;
//
//    Admin(){
//        super("Museum FSKTM");
//        this.setContentPane(this.adminPanel);
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        this.pack();
//    }
//
//    public Simulator sim = null;
//    private boolean isStart = false;
//
//    public static BookManageComponent ticketCounterTable = new BookManageComponent(adminPanel, Constant.TICKET_TABLE_TITLE);
//    public static BookManageComponent museumTable = new BookManageComponent(adminPanel, Constant.MUSEUM_TABLE_TITLE);
//    public static BookManageComponent southEntranceTable = new BookManageComponent(adminPanel, Constant.ENTRANCE_TABLE_TITLE);
//    public static BookManageComponent northEntranceTable = new BookManageComponent(adminPanel, Constant.ENTRANCE_TABLE_TITLE);
//    public static BookManageComponent eastExitTable = new BookManageComponent(adminPanel, Constant.EXIT_TABLE_TITLE);
//    public static BookManageComponent westExitTable = new BookManageComponent(adminPanel, Constant.EXIT_TABLE_TITLE);
//
//    public static void main(String[] args) {
//        Admin admin = new Admin();
//        admin.setVisible(true);
//    }
//}
