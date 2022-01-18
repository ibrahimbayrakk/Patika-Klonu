package com.patikadev.model;

import com.patikadev.helper.DBConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Course {

    private int id;
    private int userID;
    private int patikaID;
    private String name;
    private String lang;

    private Patika patika;
    private User educator;

    public Course(){
    }

    public Course(int id, int userID, int patikaID, String name, String lang) {
        this.id = id;
        this.userID = userID;
        this.patikaID = patikaID;
        this.name = name;
        this.lang = lang;
        this.patika = Patika.getFetch(patikaID);
        this.educator = User.getFetch(userID);
    }

    public static ArrayList<Course> getList(){
        ArrayList<Course> courseList = new ArrayList<>();

        Course obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM course");
            while(rs.next()){
                int id = rs.getInt("id");
                int user_id = rs.getInt("user_id");
                int patika_id = rs.getInt("patika_id");
                String name = rs.getString("name");
                String lang = rs.getString("lang");
                obj = new Course(id, user_id, patika_id, name, lang);
                courseList.add(obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courseList;
    }
    public static Course getByID(int id){
        Course c = null;
        String query = "SELECT * FROM course WHERE id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, id);
            ResultSet rs = pr.executeQuery();
            if(rs.next()){
                c = new Course();
                c.setId(rs.getInt("id"));
                c.setUserID(rs.getInt("user_id"));
                c.setPatikaID(rs.getInt("patika_id"));
                c.setName(rs.getString("name"));
                c.setLang(rs.getString("lang"));
                c.setPatika(Patika.getFetch(c.getPatikaID()));
                c.setEducator(User.getFetch(c.getUserID()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }

    public static ArrayList<Course> getListByUser(int userID){
        ArrayList<Course> courseList = new ArrayList<>();

        Course obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM course WHERE user_id = " + userID);
            while(rs.next()){
                int id = rs.getInt("id");
                int user_id = rs.getInt("user_id");
                int patika_id = rs.getInt("patika_id");
                String name = rs.getString("name");
                String lang = rs.getString("lang");
                obj = new Course(id, user_id, patika_id, name, lang);
                courseList.add(obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courseList;
    }

    public static ArrayList<Course> getListByPatika(int patikaID){
        ArrayList<Course> courseList = new ArrayList<>();

        Course obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM course WHERE patika_id = " + patikaID);
            while(rs.next()){
                int id = rs.getInt("id");
                int user_id = rs.getInt("user_id");
                int patika_id = rs.getInt("patika_id");
                String name = rs.getString("name");
                String lang = rs.getString("lang");
                obj = new Course(id, user_id, patika_id, name, lang);
                courseList.add(obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courseList;
    }

    public static ArrayList<Course> getListByEnrollment(int studentID){
        ArrayList<Course> courseList = new ArrayList<>();

        Course obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM course WHERE id IN (SELECT course_id FROM enrollment WHERE student_id=" + studentID + ")");
            while(rs.next()){
                int id = rs.getInt("id");
                int user_id = rs.getInt("user_id");
                int patika_id = rs.getInt("patika_id");
                String name = rs.getString("name");
                String lang = rs.getString("lang");
                obj = new Course(id, user_id, patika_id, name, lang);
                courseList.add(obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courseList;
    }

    public static boolean add(int user_id, int patika_id, String name, String lang){
        String query = "INSERT INTO course (user_id, patika_id, name, lang) VALUES (?,?,?,?)";

        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, user_id);
            pr.setInt(2, patika_id);
            pr.setString(3, name);
            pr.setString(4, lang);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean delete(int id){
        String query = "DELETE FROM course WHERE id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, id);
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

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getPatikaID() {
        return patikaID;
    }

    public void setPatikaID(int patikaID) {
        this.patikaID = patikaID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Patika getPatika() {
        return patika;
    }

    public void setPatika(Patika patika) {
        this.patika = patika;
    }

    public User getEducator() {
        return educator;
    }

    public void setEducator(User educator) {
        this.educator = educator;
    }
}
