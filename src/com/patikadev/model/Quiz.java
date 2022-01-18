package com.patikadev.model;

import com.patikadev.helper.DBConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Quiz {

    private int id;
    private int course_id;
    private String name;

    public Quiz(int id, int course_id, String name){
        this.id = id;
        this.course_id = course_id;
        this.name = name;
    }

    public static ArrayList<Quiz> getList(){
        ArrayList<Quiz> quizList = new ArrayList<>();

        Quiz obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM quiz");
            while(rs.next()){
                int id = rs.getInt("id");
                int course_id = rs.getInt("course_id");
                String name = rs.getString("name");
                obj = new Quiz(id, course_id, name);
                quizList.add(obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quizList;
    }

    public static ArrayList<Quiz> getListByCourseID(int courseID){
        ArrayList<Quiz> quizList = new ArrayList<>();

        Quiz obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM quiz WHERE course_id = " + courseID);
            while(rs.next()){
                int id = rs.getInt("id");
                int course_id = rs.getInt("course_id");
                String name = rs.getString("name");
                obj = new Quiz(id, course_id, name);
                quizList.add(obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quizList;
    }

    public static Quiz getQuizByID(int quizID){
        Quiz obj = null;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM quiz WHERE id = " + quizID);
            if(rs.next()){
                int id = rs.getInt("id");
                int course_id = rs.getInt("course_id");
                String name = rs.getString("name");
                obj = new Quiz(id, course_id, name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static ArrayList<Quiz> getListByEducatorID(int educatorID){
        ArrayList<Quiz> quizList = new ArrayList<>();

        Quiz obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            String query = "SELECT * FROM quiz WHERE course_id IN (SELECT course_id FROM course WHERE user_id = " + educatorID + ")";
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){
                int id = rs.getInt("id");
                int course_id = rs.getInt("course_id");
                String name = rs.getString("name");
                obj = new Quiz(id, course_id, name);
                quizList.add(obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quizList;
    }

    public static boolean add(String name, int course_id){
        String query = "INSERT INTO quiz (course_id, name) VALUES (?,?)";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, course_id);
            pr.setString(2, name);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean delete(int id){
        String query = "DELETE FROM quiz WHERE id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, id);
            int res = pr.executeUpdate();
            if(res != -1){
                String deleteAnswersQuery = "DELETE FROM answer WHERE question_id = ?";
                pr = DBConnector.getInstance().prepareStatement(deleteAnswersQuery);
                pr.setInt(1, id);
                res = pr.executeUpdate();
            }
            return res != -1;
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

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
