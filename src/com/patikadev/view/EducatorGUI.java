package com.patikadev.view;

import com.patikadev.helper.Config;
import com.patikadev.helper.DBConnector;
import com.patikadev.helper.Helper;
import com.patikadev.helper.Item;
import com.patikadev.model.Content;
import com.patikadev.model.Course;
import com.patikadev.model.Educator;
import com.patikadev.model.Quiz;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EducatorGUI extends JFrame {
    private JPanel wrapper;
    private JTabbedPane tabbedPane1;
    private JPanel pnl_courses;
    private JScrollPane scrl_courseList;
    private JTable tbl_courseList;
    private JPanel pnl_content;
    private JScrollPane scrl_content;
    private JTable tbl_contentList;
    private JTable tbl_quizList;
    private JLabel lbl_welcomeEducator;
    private JButton btn_addContent;
    private JButton btn_addQuiz;
    private JTextField fld_updateTitle;
    private JTextArea txtArea_upadateDescription;
    private JTextField fld_updateYoutube;
    private JButton btn_updateContent;
    private JButton btn_clearSelection;
    private JComboBox cmb_quiz;
    private JTextField fld_updateQuiz;
    private JButton btn_deleteContent;
    private JTextField fld_searchKurs;
    private JTextField fld_searchTitle;
    private JButton btn_search;
    private JButton btn_deleteQuiz;
    private JTextField fld_addQuizName;
    private JComboBox cmb_addQuizCourse;
    private DefaultTableModel mdl_courseList;
    private Object[] row_courseList;
    private DefaultTableModel mdl_contentList;
    private Object[] row_contentList;
    private DefaultTableModel mdl_quizList;
    private Object[] row_quizList;

    private final Educator educator;
    private boolean updateEnabled;

    public EducatorGUI(Educator educator){
        this.educator = educator;
        this.updateEnabled = false;
        toggleEnabledUpdate();

        add(wrapper);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                LoginGUI loginGUI = new LoginGUI();
            }
        });
        setSize(1000, 650);
        setLocation(Helper.screenCenter("x", getSize()), Helper.screenCenter("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setVisible(true);

        lbl_welcomeEducator.setText("Eğitmen:  " + educator.getName());

        mdl_courseList = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        Object[] col_courseList = {"ID", "Kullanıcı ID", "Patika ID", "Ad", "Programlama Dili"};
        mdl_courseList.setColumnIdentifiers(col_courseList);
        row_courseList = new Object[col_courseList.length];
        loadCourseModel();

        tbl_courseList.setModel(mdl_courseList);
        tbl_courseList.getTableHeader().setReorderingAllowed(false);

        mdl_contentList = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        Object[] col_contentList = {"ID", "Kurs ID", "Başlık"};
        mdl_contentList.setColumnIdentifiers(col_contentList);
        row_contentList = new Object[col_contentList.length];
        loadContentModel();

        tbl_contentList.setModel(mdl_contentList);
        tbl_contentList.getTableHeader().setReorderingAllowed(false);

        tbl_contentList.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point point = e.getPoint();
                int selectedRow = tbl_contentList.rowAtPoint(point);
                tbl_contentList.setRowSelectionInterval(selectedRow, selectedRow);
            }
        });

        tbl_contentList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(tbl_contentList.getSelectedRow() == -1) return;

                int id = Integer.parseInt(tbl_contentList.getValueAt(tbl_contentList.getSelectedRow(), 0).toString());
                String title;
                String description;
                String youtube;
                int quiz_id;
                String query = "SELECT title,description,youtube,quiz_id FROM content WHERE id = " + id;

                try {
                    Statement st = DBConnector.getInstance().createStatement();
                    ResultSet rs = st.executeQuery(query);
                    if(rs.next()){
                        title = rs.getString("title");
                        description = rs.getString("description");
                        youtube = rs.getString("youtube");
                        quiz_id = rs.getInt("quiz_id");

                        fld_updateTitle.setText(title);
                        fld_updateYoutube.setText(youtube);
                        txtArea_upadateDescription.setText(description);
                        fld_updateQuiz.setText(String.valueOf(quiz_id));

                        if(cmb_quiz.getItemCount() == 0){
                            cmb_quiz.addItem(new Item(0, ""));
                            for(Quiz obj : Quiz.getListByEducatorID(educator.getId())){
                                cmb_quiz.addItem(new Item(obj.getId(), obj.getName()));
                            }
                        }

                        if(!updateEnabled) toggleEnabledUpdate();
                        updateEnabled = true;
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        cmb_quiz.addActionListener(e -> {
            if(cmb_quiz.getSelectedItem() != null){
                if(((Item)cmb_quiz.getSelectedItem()).getKey() != 0){
                    fld_updateQuiz.setText(
                            fld_updateQuiz.getText().split(" ")[0] + " YeniDeğer=>" + String.valueOf(((Item) cmb_quiz.getSelectedItem()).getKey())
                    );
                }else{
                    fld_updateQuiz.setText(fld_updateQuiz.getText().split(" ")[0]);
                }

            }
        });

        btn_addContent.addActionListener(e -> {
            AddConentGUI addConentGUI = new AddConentGUI(this, this.educator);
            this.setVisible(false);
        });


        mdl_quizList = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        Object[] col_quizList = {"ID", "Kurs ID", "Quiz Adı"};
        mdl_quizList.setColumnIdentifiers(col_quizList);
        row_quizList = new Object[col_quizList.length];
        loadQuizModel();

        tbl_quizList.setModel(mdl_quizList);
        tbl_quizList.getTableHeader().setReorderingAllowed(false);
        tbl_quizList.addMouseListener(new MouseAdapter() {
            private EducatorGUI anonEducatorGUI;

            @Override
            public void mousePressed(MouseEvent e) {
                JTable table = (JTable) e.getSource();
                Point point = e.getPoint();
                if(e.getClickCount() == 2 && table.getSelectedRow() != -1){
                    int selectedQuizID = Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString());
                    anonEducatorGUI.setEnabled(false);
                    anonEducatorGUI.setVisible(false);
                    EducatorQuizGUI educatorQuizGUI = new EducatorQuizGUI(anonEducatorGUI, selectedQuizID);
                }
            }

            private MouseAdapter init(EducatorGUI educatorGUI){
                anonEducatorGUI = educatorGUI;
                return this;
            }
        }.init(this));

        for(Course c : Course.getListByUser(educator.getId())){
            cmb_addQuizCourse.addItem(new Item(c.getId(), c.getName()));
        }

        btn_clearSelection.addActionListener(e -> {
            if(updateEnabled)
                clearUpdateArea();
        });

        btn_updateContent.addActionListener(e -> {
            if(!updateEnabled) return;

            int id = Integer.parseInt(tbl_contentList.getValueAt(tbl_contentList.getSelectedRow(), 0).toString());
            int course_id = Integer.parseInt(tbl_contentList.getValueAt(tbl_contentList.getSelectedRow(), 1).toString());
            int quiz_id = ((Item)cmb_quiz.getSelectedItem()).getKey();
            if(quiz_id == 0) quiz_id = Integer.parseInt(fld_updateQuiz.getText().split(" ")[0]);

            if(Content.update(id,course_id,fld_updateTitle.getText(),txtArea_upadateDescription.getText(),fld_updateYoutube.getText(),quiz_id)){
                Helper.showMessage("done");
                loadContentModel();
                clearUpdateArea();
            }else{
                Helper.showMessage("error");
            }
        });

        btn_deleteContent.addActionListener(e -> {
            if(!updateEnabled) {
                Helper.showMessage("Lütfen silmek için listeden bir içerik seçiniz!");
                return;
            }

            if(Helper.confirm("sure")){
                int selectedID = Integer.parseInt(tbl_contentList.getValueAt(tbl_contentList.getSelectedRow(), 0).toString());
                if(Content.delete(selectedID)){
                    Quiz q = Quiz.getQuizByID(Integer.parseInt(fld_updateQuiz.getText()));
                    if(q != null) Quiz.delete(q.getId());

                    Helper.showMessage("done");
                    loadContentModel();
                    loadQuizModel();
                    clearUpdateArea();
                }else{
                    Helper.showMessage("error");
                }
            }
        });

        btn_search.addActionListener(e -> {
            String course = fld_searchKurs.getText();
            String contentTitle = fld_searchTitle.getText();
            loadContentModel(course, contentTitle);
        });

        btn_deleteQuiz.addActionListener(e -> {
            if(tbl_quizList.getSelectedRow() != -1){
                if(Helper.confirm("sure")){
                    int quiz_id = Integer.parseInt(tbl_quizList.getValueAt(tbl_quizList.getSelectedRow(), 0).toString());
                    if(Quiz.delete(quiz_id)){
                        Helper.showMessage("done");
                        loadQuizModel();
                    }else{
                        Helper.showMessage("error");
                    }
                }
            }
        });

        btn_addQuiz.addActionListener(e -> {
            if(Helper.isFieldEmpty(fld_addQuizName) || cmb_addQuizCourse.getSelectedItem() == null){
                Helper.showMessage("fill");
            }else{
                if(Quiz.add(fld_addQuizName.getText(), ((Item)cmb_addQuizCourse.getSelectedItem()).getKey())){
                    Helper.showMessage("done");
                    loadQuizModel();
                    fld_addQuizName.setText(null);
                }else{
                    Helper.showMessage("error");
                }
            }
        });
    }

    public void toggleEnabledUpdate(){
        fld_updateTitle.setEnabled(!fld_updateTitle.isEnabled());
        fld_updateYoutube.setEnabled(!fld_updateYoutube.isEnabled());
        txtArea_upadateDescription.setEnabled(!txtArea_upadateDescription.isEnabled());
        cmb_quiz.setEnabled(!cmb_quiz.isEnabled());
    }

    public void clearUpdateArea(){
        this.updateEnabled = !this.updateEnabled;

        fld_updateTitle.setText(null);
        fld_updateYoutube.setText(null);
        txtArea_upadateDescription.setText(null);
        fld_updateQuiz.setText(null);
        cmb_quiz.removeAllItems();
        toggleEnabledUpdate();
        tbl_contentList.clearSelection();
    }

    public void loadCourseModel(){
        DefaultTableModel clearModel = (DefaultTableModel) tbl_courseList.getModel();
        clearModel.setRowCount(0);

        int i;
        for(Course obj : Course.getListByUser(this.educator.getId())){
            i = 0;
            row_courseList[i++] = obj.getId();
            row_courseList[i++] = obj.getUserID();
            row_courseList[i++] = obj.getPatikaID();
            row_courseList[i++] = obj.getName();
            row_courseList[i++] = obj.getLang();
            mdl_courseList.addRow(row_courseList);
        }
    }

    public void loadContentModel(){
        DefaultTableModel clearModel = (DefaultTableModel) tbl_contentList.getModel();
        clearModel.setRowCount(0);

        int i;
        for(Content obj : Content.getListByUserForEducator(this.educator.getId())){
            i = 0;
            row_contentList[i++] = obj.getId();
            row_contentList[i++] = obj.getCourse_id();
            row_contentList[i++] = obj.getTitle();
            mdl_contentList.addRow(row_contentList);
        }
    }

    public void loadContentModel(String course, String contentTitle){
        DefaultTableModel clearModel = (DefaultTableModel) tbl_contentList.getModel();
        clearModel.setRowCount(0);

        int i;
        for(Content obj : Content.getListByUserForEducatorFilter(this.educator.getId(), course, contentTitle)){
            i = 0;
            row_contentList[i++] = obj.getId();
            row_contentList[i++] = obj.getCourse_id();
            row_contentList[i++] = obj.getTitle();
            mdl_contentList.addRow(row_contentList);
        }
    }

    public void loadQuizModel(){
        DefaultTableModel clearModel = (DefaultTableModel) tbl_quizList.getModel();
        clearModel.setRowCount(0);

        int i;
        for(Quiz obj : Quiz.getListByEducatorID(this.educator.getId())){
            i = 0;
            row_quizList[i++] = obj.getId();
            row_quizList[i++] = obj.getCourse_id();
            row_quizList[i++] = obj.getName();
            mdl_quizList.addRow(row_quizList);
        }
    }
}
