package com.patikadev.view;

import com.patikadev.helper.Config;
import com.patikadev.helper.DBConnector;
import com.patikadev.helper.Helper;
import com.patikadev.model.Course;
import com.patikadev.model.Patika;
import com.patikadev.model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StudentGUI extends JFrame {
    private JPanel wrapper;
    private JTabbedPane tabbedPane1;
    private JTable tbl_studentPatikalar;
    private JTable tbl_studentRegisteredCourses;
    private JButton btn_showCourses;
    private DefaultTableModel mdl_studentPatikalarList;
    private Object[] row_studentPatikalarList;
    private DefaultTableModel mdl_studentRegisteredCoursesList;
    private Object[] row_studentRegisteredCoursesList;

    private Student student;

    public StudentGUI(Student student){
        this.student = student;

        add(wrapper);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                LoginGUI loginGUI = new LoginGUI();
            }
        });
        setSize(1000, 600);
        setLocation(Helper.screenCenter("x", getSize()), Helper.screenCenter("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setVisible(true);

        mdl_studentPatikalarList = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        Object[] col_studentPatikalarList = {"Patika ID", "Patika Name"};
        mdl_studentPatikalarList.setColumnIdentifiers(col_studentPatikalarList);
        row_studentPatikalarList = new Object[col_studentPatikalarList.length];
        loadStudentPatikalarModel();

        tbl_studentPatikalar.setModel(mdl_studentPatikalarList);
        tbl_studentPatikalar.getTableHeader().setReorderingAllowed(false);

        mdl_studentRegisteredCoursesList = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        Object[] col_studentRegisteredCoursesList = {"KursID", "İçerikID", "Patika Adı", "Kurs Adı", "Programlama Dili"};
        mdl_studentRegisteredCoursesList.setColumnIdentifiers(col_studentRegisteredCoursesList);
        row_studentRegisteredCoursesList = new Object[col_studentRegisteredCoursesList.length];
        loadStudentRegisteredCourses();

        tbl_studentRegisteredCourses.setModel(mdl_studentRegisteredCoursesList);
        tbl_studentRegisteredCourses.getTableHeader().setReorderingAllowed(false);
        tbl_studentRegisteredCourses.addMouseListener(new MouseAdapter() {
            private StudentGUI anonStudentGUI;

            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2 && tbl_studentRegisteredCourses.getSelectedRow() != -1){
                    int selectedCourseID = Integer.parseInt(tbl_studentRegisteredCourses.getValueAt(tbl_studentRegisteredCourses.getSelectedRow(), 0).toString());
                    int selectedContentID = Integer.parseInt(tbl_studentRegisteredCourses.getValueAt(tbl_studentRegisteredCourses.getSelectedRow(), 1).toString());
                    String query = "SELECT id FROM patika WHERE name='" + tbl_studentRegisteredCourses.getValueAt(tbl_studentRegisteredCourses.getSelectedRow(), 2) + "'";
                    try {
                        Statement st = DBConnector.getInstance().createStatement();
                        ResultSet rs = st.executeQuery(query);
                        if(rs.next()){
                            int selectedPatikaID = rs.getInt("id");
                            CourseGUI studentGUI = new CourseGUI(anonStudentGUI, selectedCourseID, selectedContentID, selectedPatikaID);
                            anonStudentGUI.setVisible(false);
                        }else{
                            Helper.showMessage("error");
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }


                }
            }

            private MouseAdapter init(StudentGUI studentGUI){
                anonStudentGUI = studentGUI;
                return this;
            }
        }.init(this));

        btn_showCourses.addActionListener(new ActionListener() {
            private int anonStudentID;
            private StudentGUI anonStudentGUI;

            @Override
            public void actionPerformed(ActionEvent e) {
                if(tbl_studentPatikalar.getSelectedRow() == -1) {
                    Helper.showMessage("Lütfen listeden bir Patika seçiniz!");
                    return;
                }

                anonStudentGUI.setVisible(false);
                StudentCoursesGUI studentCoursesGUI = new StudentCoursesGUI(anonStudentGUI, anonStudentID, Integer.parseInt(tbl_studentPatikalar.getValueAt(tbl_studentPatikalar.getSelectedRow(), 0).toString()));
            }

            private ActionListener init(int studentID, StudentGUI studentGUI){
                anonStudentID = studentID;
                anonStudentGUI = studentGUI;
                return this;
            }
        }.init(this.student.getId(), this));
    }

    public void loadStudentPatikalarModel(){
        DefaultTableModel clearModel = (DefaultTableModel) tbl_studentPatikalar.getModel();
        clearModel.setRowCount(0);

        int i;
        for(Patika p : Patika.getList()){
            i = 0;
            row_studentPatikalarList[i++] = p.getId();
            row_studentPatikalarList[i++] = p.getName();
            mdl_studentPatikalarList.addRow(row_studentPatikalarList);
        }
    }

    public void loadStudentRegisteredCourses(){
        DefaultTableModel clearModel = (DefaultTableModel) tbl_studentRegisteredCourses.getModel();
        clearModel.setRowCount(0);

        int i;
        for(Course c : Course.getListByEnrollment(this.student.getId())){
            i = 0;
            String query = "SELECT * FROM patika WHERE id = (SELECT patika_id FROM course WHERE id = " + c.getId() + ")";
            Statement st = null;
            try {
                st = DBConnector.getInstance().createStatement();
                ResultSet rs = st.executeQuery(query);
                String patikaName = null;
                if(rs.next()){
                    patikaName = rs.getString("name");
                }else{
                    Helper.showMessage("error");
                    return;
                }
                int contentID;
                String contentQuery = "SELECT * FROM content WHERE course_id = " + c.getId();
                rs = st.executeQuery(contentQuery);
                if(rs.next()){
                    contentID = rs.getInt("id");
                }else{
                    Helper.showMessage("error");
                    return;
                }
                row_studentRegisteredCoursesList[i++] = c.getId();
                row_studentRegisteredCoursesList[i++] = contentID;
                row_studentRegisteredCoursesList[i++] = patikaName;
                row_studentRegisteredCoursesList[i++] = c.getName();
                row_studentRegisteredCoursesList[i++] = c.getLang();
                mdl_studentRegisteredCoursesList.addRow(row_studentRegisteredCoursesList);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Student getStudent() {
        return student;
    }
}
