package gui;

import component.BackGroundPanel;
import database.museum_user;
import utilities.PathUtils;
import utilities.ScreenUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class RegisterInterface {
    JFrame jf = new JFrame("Register page");

    final int WIDTH = 500;
    final int HEIGHT = 400;


    //组装视图
    public void init() throws Exception {
        //设置窗口的属性
        jf.setBounds((ScreenUtils.getScreenWidth()-WIDTH)/2,(ScreenUtils.getScreenHeight()-HEIGHT)/2,WIDTH,HEIGHT);
        jf.setResizable(false);

        BackGroundPanel bgPanel = new BackGroundPanel(ImageIO.read(new File(PathUtils.getRealPath("regist.jpg"))));
        bgPanel.setBounds(0,0,WIDTH,HEIGHT);


        Box vBox = Box.createVerticalBox();

        //组装用户名
        Box uBox = Box.createHorizontalBox();
        JLabel uLabel = new JLabel("Username: ");
        JTextField uField = new JTextField(15);

        uBox.add(uLabel);
        uBox.add(Box.createHorizontalStrut(20));
        uBox.add(uField);

        //组装密码
        Box pBox = Box.createHorizontalBox();
        JLabel pLabel = new JLabel("Password: ");
        JTextField pField = new JTextField(15);

        pBox.add(pLabel);
        pBox.add(Box.createHorizontalStrut(20));
        pBox.add(pField);

//        //组装手机号
//        Box tBox = Box.createHorizontalBox();
//        JLabel tLabel = new JLabel("Phone Num:");
//        JTextField tField = new JTextField(15);
//
//        tBox.add(tLabel);
//        tBox.add(Box.createHorizontalStrut(20));
//        tBox.add(tField);

        Box btnBox = Box.createHorizontalBox();
        JButton registBtn = new JButton("Register");
        JButton backBtn = new JButton("Back to login page");

        registBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(uField.getText()!=null && pField.getText()!=null){
                    museum_user.users.put(uField.getText(),pField.getText());
                    JOptionPane.showMessageDialog(jf, "Register successfully! Login to Manager Panel.");
                    try {
                        new ManagerInterface().init();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }else if(uField.getText() == null || pField.getText() == null){
                    JOptionPane.showMessageDialog(jf, "Please fill in username and password!");
                }
            }
        });

        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //返回到登录页面即可
                try {
                    new LoginInterface().init();
                    jf.dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });


        btnBox.add(registBtn);
        btnBox.add(Box.createHorizontalStrut(100));
        btnBox.add(backBtn);

        vBox.add(Box.createVerticalStrut(50));
        vBox.add(uBox);
        vBox.add(Box.createVerticalStrut(40));
        vBox.add(pBox);
        vBox.add(Box.createVerticalStrut(20));
        vBox.add(Box.createVerticalStrut(20));
//        vBox.add(tBox);
        vBox.add(Box.createVerticalStrut(30));
        vBox.add(btnBox);

        bgPanel.add(vBox);

        jf.add(bgPanel);

        jf.setVisible(true);
    }

}
