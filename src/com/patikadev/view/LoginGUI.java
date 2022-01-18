package com.patikadev.view;

import com.patikadev.helper.Config;
import com.patikadev.helper.Helper;
import com.patikadev.model.Educator;
import com.patikadev.model.Operator;
import com.patikadev.model.Student;
import com.patikadev.model.User;

import javax.swing.*;

public class LoginGUI extends JFrame {
    private JPanel wrapper;
    private JPanel wtop;
    private JPanel wbottom;
    private JTextField fld_userUname;
    private JPasswordField fld_userPass;
    private JButton btn_login;
    private JButton kayıtOlButton;

    public LoginGUI(){
        add(wrapper);
        setSize(400, 450);
        setLocation(Helper.screenCenter("x", getSize()), Helper.screenCenter("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);

        btn_login.addActionListener(e -> {
            if(Helper.isFieldEmpty(fld_userUname) || Helper.isFieldEmpty(fld_userPass)){
                Helper.showMessage("fill");
            }else{
                User u = User.getFetch(fld_userUname.getText(), fld_userPass.getText());
                if(u == null){
                    Helper.showMessage("Kullanıcı bulunamadı!");
                }else{
                    switch (u.getType()){
                        case "operator":
                            OperatorGUI opGUI = new OperatorGUI((Operator) u);
                            break;
                        case "educator":
                            EducatorGUI edGUI = new EducatorGUI((Educator) u);
                            break;
                        case "student":
                            StudentGUI stuGUI = new StudentGUI((Student) u);
                            break;
                    }
                    dispose();
                }
            }
        });

        kayıtOlButton.addActionListener(e -> {
            SignUpGUI signUpGUI = new SignUpGUI();
            dispose();
        });
    }

    public static void main(String[] args) {
        Helper.setLayout();
        LoginGUI lg = new LoginGUI();
    }
}
