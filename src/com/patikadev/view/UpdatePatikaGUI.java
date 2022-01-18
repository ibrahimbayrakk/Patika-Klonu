package com.patikadev.view;

import com.patikadev.helper.Config;
import com.patikadev.helper.Helper;
import com.patikadev.model.Patika;

import javax.swing.*;

public class UpdatePatikaGUI extends JFrame {
    private JPanel wrapper;
    private JTextField fld_patikaName;
    private JButton btn_updateButton;
    private Patika patika;

    public UpdatePatikaGUI(Patika patika){
        this.patika = patika;
        add(wrapper);
        setSize(300, 150);
        setLocation(Helper.screenCenter("x", getSize()), Helper.screenCenter("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setVisible(true);

        fld_patikaName.setText(patika.getName());
        btn_updateButton.addActionListener(e -> {
            if(Helper.isFieldEmpty(fld_patikaName)){
                Helper.showMessage("fill");
            }else {
                if(Patika.update(patika.getId(), fld_patikaName.getText())){
                    Helper.showMessage("done");
                }
                dispose();
            }
        });
    }
}
