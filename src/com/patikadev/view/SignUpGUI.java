package com.patikadev.view;

import com.patikadev.helper.Config;
import com.patikadev.helper.Helper;
import com.patikadev.model.User;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SignUpGUI extends JFrame {
    private JPanel wrapper;
    private JTextField fld_signUpUname;
    private JButton btn_signUp;
    private JTextField fld_signUpName;
    private JPasswordField fld_signUpPass;

    public SignUpGUI(){
        add(wrapper);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                LoginGUI loginGUI = new LoginGUI();
            }
        });
        setSize(400, 450);
        setLocation(Helper.screenCenter("x", getSize()), Helper.screenCenter("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);

        btn_signUp.addActionListener(e -> {
            if(Helper.isFieldEmpty(fld_signUpName) || Helper.isFieldEmpty(fld_signUpUname) || Helper.isFieldEmpty(fld_signUpPass)){
                Helper.showMessage("fill");
            }else{
                if(User.add(fld_signUpName.getText(), fld_signUpUname.getText(), fld_signUpPass.getText(), "student")){
                    Helper.showMessage("done");
                    dispose();
                }
            }
        });
    }

}
