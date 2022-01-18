package com.patikadev.model;

import com.patikadev.helper.DBConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Comment {

    private int id;
    private int patika_id;
    private int course_id;
    private int student_id;
    private String comment;

    public Comment(){}

    public Comment(int id, int patika_id, int course_id, int student_id, String comment) {
        this.id = id;
        this.patika_id = patika_id;
        this.course_id = course_id;
        this.student_id = student_id;
        this.comment = comment;
    }

    public static ArrayList<Comment> getList(){
        ArrayList<Comment> commentList = new ArrayList<>();
        String query = "SELECT * FROM comment";

        Comment obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){
                obj = new Comment();
                obj.setId(rs.getInt("id"));
                obj.setPatika_id(rs.getInt("patika_id"));
                obj.setCourse_id(rs.getInt("course_id"));
                obj.setStudent_id(rs.getInt("student_id"));
                obj.setComment(rs.getString("comment"));
                commentList.add(obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commentList;
    }

    public static boolean add(int patikaID, int courseID, int studentID, String comment){
        String query = "INSERT INTO comment (patika_id,course_id,student_id,comment) VALUES (?,?,?,?)";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, patikaID);
            pr.setInt(2, courseID);
            pr.setInt(3, studentID);
            pr.setString(4, comment);
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
