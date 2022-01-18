package com.patikadev.view;

import com.patikadev.helper.Config;
import com.patikadev.helper.DBConnector;
import com.patikadev.helper.Helper;
import com.patikadev.model.Course;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StudentCoursesGUI extends JFrame{
    private JPanel wrapper;
    private JTable tbl_studentCourses;
    private DefaultTableModel mdl_studentCourseList;
    private Object[] row_studentCourseList;

    private StudentGUI studentGUI;
    private int studentID;
    private int patikaID;

    public StudentCoursesGUI(StudentGUI studentGUI, int studentID, int patikaID){
        this.studentGUI = studentGUI;
        this.studentID = studentID;
        this.patikaID = patikaID;

        add(wrapper);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                studentGUI.setVisible(true);
            }
        });
        setSize(600, 400);
        setLocation(Helper.screenCenter("x", getSize()), Helper.screenCenter("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setVisible(true);

        mdl_studentCourseList = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        Object[] col_studentCourseList = {"Kurs ID", "Kurs Adı", "Programlama Dili"};
        mdl_studentCourseList.setColumnIdentifiers(col_studentCourseList);
        row_studentCourseList = new Object[col_studentCourseList.length];
        loadStudentCourseModel();

        tbl_studentCourses.setModel(mdl_studentCourseList);
        tbl_studentCourses.getTableHeader().setReorderingAllowed(false);
        tbl_studentCourses.addMouseListener(new MouseAdapter() {
            private int anonStudentID;

            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getClickCount() == 2 && tbl_studentCourses.getSelectedRow() != -1){
                    if(Helper.confirm(tbl_studentCourses.getValueAt(tbl_studentCourses.getSelectedRow(), 1) +
                            " adlı kursa kayıt olmak istediğinize emin misiniz?")){
                        int currentCourseID = Integer.parseInt(tbl_studentCourses.getValueAt(tbl_studentCourses.getSelectedRow(), 0).toString());
                        if(isStudentAlreadyRegisteredToCourse(anonStudentID, currentCourseID)){
                            Helper.showMessage("Zaten bu derse kayıtlısınız!");
                        }else{
                            // Register student to the patika
                            if(registerStudentToCourse(currentCourseID)){
                                Helper.showMessage("done");
                                studentGUI.loadStudentRegisteredCourses();
                            }else{
                                Helper.showMessage("error");
                            }
                        }
                    }
                }
            }

            private MouseAdapter init(int studentID){
                anonStudentID = studentID;
                return this;
            }
        }.init(this.studentID));
    }

    public void loadStudentCourseModel(){
        DefaultTableModel clearModel = (DefaultTableModel) tbl_studentCourses.getModel();
        clearModel.setRowCount(0);

        int i;
        for(Course c : Course.getListByPatika(this.patikaID)){
            i = 0;
            row_studentCourseList[i++] = c.getId();
            row_studentCourseList[i++] = c.getName();
            row_studentCourseList[i++] = c.getLang();
            mdl_studentCourseList.addRow(row_studentCourseList);
        }
    }

    public boolean isStudentAlreadyRegisteredToCourse(int studentID, int courseID){
        String query = "SELECT * FROM enrollment WHERE student_id = " + studentID + " AND course_id = " + courseID;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            if(rs.next()){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean registerStudentToCourse(int course_id){
        String query = "INSERT INTO enrollment (student_id, course_id) VALUES (?,?)";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, this.studentID);
            pr.setInt(2, course_id);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
