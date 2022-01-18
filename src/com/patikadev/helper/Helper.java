package com.patikadev.helper;

import javax.swing.*;
import java.awt.*;

public class Helper {

    public static void setLayout() {
        for(UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()){
            if(info.getName().equals("Nimbus")) {
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    public static int screenCenter(String position, Dimension size) {
        int point = 0;
        switch (position){
            case "x":
                point = (Toolkit.getDefaultToolkit().getScreenSize().width - size.width) / 2;
                break;
            case "y":
                point = (Toolkit.getDefaultToolkit().getScreenSize().height - size.height) / 2;
                break;
            default:
                point = 0;
        }

        return point;
    }

    public static boolean isFieldEmpty(JTextField field){
        return field.getText().trim().isEmpty();
    }

    public static boolean isFieldEmpty(JTextArea area){
        return area.getText().trim().isEmpty();
    }

    public static void showMessage(String s){
        optionPaneTR();
        String msg;
        String title;
        switch (s){
            case "fill":
                msg = "Lütfen tüm alanları doldurunuz!";
                title = "Hata!";
                break;
            case "done":
                msg = "İşlem başarılı!";
                title = "Sonuç";
                break;
            case "error":
                msg = "Bir hata oluştu!";
                title = "Hata!";
                break;
            default:
                msg = s;
                title = "Mesaj";
        }
        JOptionPane.showMessageDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void optionPaneTR(){
        UIManager.put("OptionPane.okButtonText", "Tamam");
        UIManager.put("OptionPane.yesButtonText", "Evet");
        UIManager.put("OptionPane.noButtonText", "Hayır");
    }

    public static boolean confirm(String str){
        optionPaneTR();
        String msg;

        switch (str){
            case "sure":
                msg = "Bu işlemi gerçekleştirmek istediğinize emin misiniz?";
                break;
            default:
                msg = str;
        }

        return JOptionPane.showConfirmDialog(null, msg, "Son Kararınız mı?", JOptionPane.YES_NO_OPTION) == 0;
    }
}
