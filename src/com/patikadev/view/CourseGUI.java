package com.patikadev.view;

import com.patikadev.helper.Config;
import com.patikadev.helper.Helper;
import com.patikadev.model.Comment;
import com.patikadev.model.Content;
import com.patikadev.model.Course;
import com.patikadev.model.Rate;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CourseGUI extends JFrame {
    private JPanel wrapper;
    private JPanel pnl_comment;
    private JList lst_comment;
    private JPanel pnl_courseContent;
    private JLabel lbl_patikaName;
    private JLabel lbl_courseName;
    private JLabel lbl_courseLang;
    private JTextPane pn_content;
    private JLabel lbl_youtubeLink;
    private JButton btn_doQuiz;
    private JPanel pnl_rate;
    private JRadioButton rdbtn_1;
    private JRadioButton rdbtn_2;
    private JRadioButton rdbtn_3;
    private JRadioButton rdbtn_4;
    private JRadioButton rdbtn_5;
    private JButton btn_rate;
    private JTextField fld_comment;
    private JButton btn_comment;
    private DefaultListModel<String> mdl_commentList;

    private int rating;
    private int quizID;
    private int courseID;
    private int contentID;
    private int patikaID;
    private boolean alreadyRated;
    private StudentGUI studentGUI;
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    public CourseGUI(StudentGUI studentGUI, int courseID, int contentID, int patikaID){
        this.rating = -1;
        this.courseID = courseID;
        this.contentID = contentID;
        this.studentGUI = studentGUI;
        this.patikaID = patikaID;

        this.mdl_commentList = new DefaultListModel<>();
        updateComments();

        add(wrapper);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                studentGUI.setVisible(true);
            }
        });
        setSize(800, 600);
        setLocation(Helper.screenCenter("x", getSize()), Helper.screenCenter("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setVisible(true);

        loadCourseAndContentData();
        if(!checkIsAlreadyRated()){
            btn_comment.setEnabled(true);
        }

        btn_doQuiz.addActionListener(e -> {
            StudentQuizGUI studentQuizGUI = new StudentQuizGUI(this.quizID);
        });

        btn_comment.setEnabled(false);

        rdbtn_1.addActionListener(e -> {
            if(alreadyRated) return;
            this.rating = 1;
            rdbtn_1.setSelected(true);
            rdbtn_2.setSelected(false);
            rdbtn_3.setSelected(false);
            rdbtn_4.setSelected(false);
            rdbtn_5.setSelected(false);
            btn_rate.setEnabled(true);
        });

        rdbtn_2.addActionListener(e -> {
            if(alreadyRated) return;
            this.rating = 2;
            rdbtn_1.setSelected(true);
            rdbtn_2.setSelected(true);
            rdbtn_3.setSelected(false);
            rdbtn_4.setSelected(false);
            rdbtn_5.setSelected(false);
            btn_rate.setEnabled(true);
        });

        rdbtn_3.addActionListener(e -> {
            if(alreadyRated) return;
            this.rating = 3;
            rdbtn_1.setSelected(true);
            rdbtn_2.setSelected(true);
            rdbtn_3.setSelected(true);
            rdbtn_4.setSelected(false);
            rdbtn_5.setSelected(false);
            btn_rate.setEnabled(true);
        });

        rdbtn_4.addActionListener(e -> {
            if(alreadyRated) return;
            this.rating = 4;
            rdbtn_1.setSelected(true);
            rdbtn_2.setSelected(true);
            rdbtn_3.setSelected(true);
            rdbtn_4.setSelected(true);
            rdbtn_5.setSelected(false);
            btn_rate.setEnabled(true);
        });

        rdbtn_5.addActionListener(e -> {
            if(alreadyRated) return;
            this.rating = 5;
            rdbtn_1.setSelected(true);
            rdbtn_2.setSelected(true);
            rdbtn_3.setSelected(true);
            rdbtn_4.setSelected(true);
            rdbtn_5.setSelected(true);
            btn_rate.setEnabled(true);
        });

        btn_rate.addActionListener(e -> {
            if(alreadyRated) return;
            if(rdbtn_1.isSelected() || rdbtn_2.isSelected() || rdbtn_3.isSelected() || rdbtn_4.isSelected() || rdbtn_5.isSelected()){
                if(Rate.add(this.patikaID,this.courseID,this.studentGUI.getStudent().getId(),this.rating)){
                    Helper.showMessage("done");
                    checkIsAlreadyRated();
                }else{
                    Helper.showMessage("error");
                }
            }else{
                Helper.showMessage("Lütfen bir puan seçiniz!");
            }
        });

        btn_comment.addActionListener(e -> {
            if(Helper.isFieldEmpty(fld_comment)){
                Helper.showMessage("fill");
            }else{
                LocalDateTime now = LocalDateTime.now();
                String comment = "(" + dtf.format(now) + ") " + this.studentGUI.getStudent().getName() + ": " + fld_comment.getText();
                if(Comment.add(this.patikaID, this.courseID, this.studentGUI.getStudent().getId(), comment)){
                    updateComments();
                    fld_comment.setText(null);
                }else{
                    Helper.showMessage("error");
                }
            }
        });

        // Restrict comment to max 255 characters.
        fld_comment.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(fld_comment.getText().length() > 255){
                    e.consume();
                }
            }
        });

        fld_comment.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkIsEmpty();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkIsEmpty();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                checkIsEmpty();
            }

            public void checkIsEmpty(){
                if(Helper.isFieldEmpty(fld_comment)){
                    btn_comment.setEnabled(false);
                }else{
                    btn_comment.setEnabled(true);
                }
            }
        });
    }

    public void loadCourseAndContentData(){
        Course currentCourse = Course.getByID(this.courseID);
        Content currentContent = Content.getFetch(this.contentID);
        lbl_patikaName.setText("Patika: " + currentCourse.getPatika().getName());
        lbl_courseName.setText("Kurs: " + currentCourse.getName());
        lbl_courseLang.setText("Programlama Dili: " + currentCourse.getLang());
        lbl_youtubeLink.setText("Link: " + currentContent.getYoutube());
        pn_content.setText(currentContent.getDescription());

        this.quizID = currentContent.getQuiz_id();
    }

    public boolean checkIsAlreadyRated(){
        Rate r = Rate.getRateByStudentIDAndCourseID(studentGUI.getStudent().getId(), courseID);
        if(r != null){
            this.alreadyRated = true;
            btn_rate.setEnabled(false);
            switch (r.getRating()){
                case 1:
                    rdbtn_1.setSelected(true);
                    rdbtn_2.setSelected(false);
                    rdbtn_3.setSelected(false);
                    rdbtn_4.setSelected(false);
                    rdbtn_5.setSelected(false);
                    break;
                case 2:
                    rdbtn_1.setSelected(true);
                    rdbtn_2.setSelected(true);
                    rdbtn_3.setSelected(false);
                    rdbtn_4.setSelected(false);
                    rdbtn_5.setSelected(false);
                    break;
                case 3:
                    rdbtn_1.setSelected(true);
                    rdbtn_2.setSelected(true);
                    rdbtn_3.setSelected(true);
                    rdbtn_4.setSelected(false);
                    rdbtn_5.setSelected(false);
                    break;
                case 4:
                    rdbtn_1.setSelected(true);
                    rdbtn_2.setSelected(true);
                    rdbtn_3.setSelected(true);
                    rdbtn_4.setSelected(true);
                    rdbtn_5.setSelected(false);
                    break;
                case 5:
                    rdbtn_1.setSelected(true);
                    rdbtn_2.setSelected(true);
                    rdbtn_3.setSelected(true);
                    rdbtn_4.setSelected(true);
                    rdbtn_5.setSelected(true);
                    break;
                default:
                    Helper.showMessage("Bilinmeyen değerlendirme puanı: " + r.getRating() + " !");
            }
            rdbtn_1.setEnabled(false);
            rdbtn_2.setEnabled(false);
            rdbtn_3.setEnabled(false);
            rdbtn_4.setEnabled(false);
            rdbtn_5.setEnabled(false);
            return true;
        }
        return false;
    }

    public void updateComments(){
        mdl_commentList.clear();
        for(Comment c : Comment.getList()){
            mdl_commentList.add(0, c.getComment());
        }
        lst_comment.setModel(mdl_commentList);
        lst_comment.setCellRenderer(new LongListCellRenderer());
    }
}

class LongListCellRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        String pre = "<html><body style='width: 300px;'>";
        JLabel l = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        String s = value==null ? "Null" : value.toString();
        l.setText(pre + s);
        return l;
    }
}