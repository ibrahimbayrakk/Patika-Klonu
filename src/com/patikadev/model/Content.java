package com.patikadev.model;

import com.patikadev.helper.DBConnector;
import com.patikadev.helper.Helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Content {

    private int id;
    private int course_id;
    private int user_id;
    private String title;
    private String description;
    private String youtube;
    private int quiz_id;

    public Content(){}

    public Content(int id, int course_id, String title){
        this.id = id;
        this.course_id = course_id;
        this.title = title;
    }

    public Content(int id, int course_id, String title, String description, String youtube, int quiz_id) {
        this.id = id;
        this.course_id = course_id;
        this.title = title;
        this.description = description;
        this.youtube = youtube;
        this.quiz_id = quiz_id;
    }

    public static Content getFetch(int id){
        Content obj = null;
        String query = "SELECT * FROM content WHERE id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, id);
            ResultSet rs = pr.executeQuery();
            if(rs.next()){
                obj = new Content();
                obj.setId(rs.getInt("id"));
                obj.setCourse_id(rs.getInt("course_id"));
                obj.setUser_id(rs.getInt("user_id"));
                obj.setTitle(rs.getString("title"));
                obj.setDescription(rs.getString("description"));
                obj.setYoutube(rs.getString("youtube"));
                obj.setQuiz_id(rs.getInt("quiz_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static ArrayList<Content> getListByUserForEducator(int id){
        ArrayList<Content> contentList = new ArrayList<>();

        Content obj;
        Statement st = null;
        try {
            st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM content WHERE user_id = " + id);
            while(rs.next()){
                int content_id = rs.getInt("id");
                int course_id = rs.getInt("course_id");
                String title = rs.getString("title");
                obj = new Content(content_id, course_id, title);
                contentList.add(obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contentList;
    }

    public static ArrayList<Content> getListByUserForEducatorFilter(int id, String course, String contentTitle){
        ArrayList<Content> contentList = new ArrayList<>();

        Content obj;
        Statement st = null;
        try {
            st = DBConnector.getInstance().createStatement();
            String query = "SELECT * FROM content WHERE user_id = " + id +
                    " AND title LIKE '%" + contentTitle + "%' AND quiz_id IN (" +
                    "SELECT id FROM quiz WHERE course_id = (" +
                    "SELECT id FROM course WHERE name LIKE '%" + course + "%'))";
            ResultSet rs = st.executeQuery(query);


            while(rs.next()){
                int content_id = rs.getInt("id");
                int course_id = rs.getInt("course_id");
                String title = rs.getString("title");
                obj = new Content(content_id, course_id, title);
                contentList.add(obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contentList;
    }

    public static boolean add(int course_id, int user_id, String title, String description, String youtube, int quiz_id){
        String query = "INSERT INTO content (course_id, user_id, title, description, youtube, quiz_id) VALUES (?,?,?,?,?,?)";

        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, course_id);
            pr.setInt(2, user_id);
            pr.setString(3, title);
            pr.setString(4, description);
            pr.setString(5, youtube);
            pr.setInt(6, quiz_id);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean update(int id, int course_id, String title, String description, String youtube, int quiz_id){
        String query = "UPDATE content SET course_id=?,title=?,description=?,youtube=?,quiz_id=? WHERE id = ?";

        Content findContent = Content.getFetch(id);
        if(findContent != null && findContent.getId() != id){
            Helper.showMessage("Content bulunamadı!");
            return false;
        }

        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, course_id);
            pr.setString(2, title);
            pr.setString(3, description);
            pr.setString(4, youtube);
            pr.setInt(5, quiz_id);
            pr.setInt(6, id);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean delete(int id){
        String query = "DELETE FROM content WHERE id = ?";
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

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public int getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(int quiz_id) {
        this.quiz_id = quiz_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
