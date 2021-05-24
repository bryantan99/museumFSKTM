package gui;

import simulator.Simulator;
import component.BackGroundPanel;
import database.museum_user;
import utilities.PathUtils;
import utilities.ScreenUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class LoginInterface {
    private int WIDTH = 500;
    private int HEIGHT = 300;
    private boolean isStart = false;

    JFrame jf = new JFrame("Museum Admin Login Page");

    public Simulator sim = null;

    public void init() throws Exception {
        sim = new Simulator();
        // set the window
        jf.setBounds((ScreenUtils.getScreenWidth() - WIDTH) / 2, (ScreenUtils.getScreenHeight() - HEIGHT) / 2, WIDTH, HEIGHT);
        jf.setResizable(false);

        // set content of window
        BackGroundPanel bgPanel = new BackGroundPanel(ImageIO.read(new File(PathUtils.getRealPath("library.jpg"))));
        bgPanel.setBounds(0,0,WIDTH,HEIGHT);

        // components about login
        Box vBox = Box.createVerticalBox();

        // username
        Box uBox = Box.createHorizontalBox();
        JLabel uLabel = new JLabel("Username: ");
        JTextField uField = new JTextField(15);

        uBox.add(uLabel);
        uBox.add(Box.createHorizontalStrut(20));
        uBox.add(uField);

        // password
        Box pBox = Box.createHorizontalBox();
        JLabel pLabel = new JLabel("Password: ");
        JTextField pField = new JTextField(15);

        pBox.add(pLabel);
        pBox.add(Box.createHorizontalStrut(22));
        pBox.add(pField);

        // Button
        Box btnBox = Box.createHorizontalBox();
        JButton loginBtn = new JButton("Sign in");

        loginBtn.addActionListener(e -> {
            String username = uField.getText().trim();
            String password = pField.getText().trim();

            if(museum_user.users.containsKey(username) && password.equals(museum_user.users.get(username))){
                JOptionPane.showMessageDialog(jf, "Login successfully!");
                try {
                    new ManagerInterface().init();
                    if(!isStart){
                        //sim.startSimulate();
                        isStart = true;
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }else{
                JOptionPane.showMessageDialog(jf, "Error!");
            }
        });


        btnBox.add(loginBtn);

        vBox.add(Box.createVerticalStrut(50));
        vBox.add(uBox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(pBox);
        vBox.add(Box.createVerticalStrut(40));
        vBox.add(btnBox);

        bgPanel.add(vBox);

        jf.add(bgPanel);
        jf.setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        new ManagerInterface().init();
    }
}
