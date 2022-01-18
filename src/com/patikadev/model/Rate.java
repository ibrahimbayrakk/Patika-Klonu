package com.patikadev.model;

import com.patikadev.helper.DBConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Rate {

    private int id;
    private int patika_id;
    private int course_id;
    private int student_id;
    private int rating;

    public Rate(){}

    public Rate(int id, int patika_id, int course_id, int student_id, int rating) {
        this.id = id;
        this.patika_id = patika_id;
        this.course_id = course_id;
        this.student_id = student_id;
        this.rating = rating;
    }

    public static Rate getRateByStudentIDAndCourseID(int studentID, int courseID){
        Rate rate = null;
        String query = "SELECT * FROM rate WHERE student_id=" + studentID + " AND course_id=" + courseID;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            if(rs.next()){
                rate = new Rate();
                rate.setId(rs.getInt("id"));
                rate.setPatika_id(rs.getInt("patika_id"));
                rate.setCourse_id(rs.getInt("course_id"));
                rate.setStudent_id(rs.getInt("student_id"));
                rate.setRating(rs.getInt("rating"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rate;
    }

    public static boolean add(int patikaID, int courseID, int studentID, int rating){
        String query = "INSERT INTO rate (patika_id, course_id, student_id, rating) VALUES (?,?,?,?)";

        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, patikaID);
            pr.setInt(2, courseID);
            pr.setInt(3, studentID);
            pr.setInt(4, rating);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPatika_id() {
        return patika_id;
    }

    public void setPatika_id(int patika_id) {
        this.patika_id = patika_id;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
