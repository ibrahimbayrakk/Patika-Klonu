package com.patikadev.model;

import com.patikadev.helper.DBConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Answer {

    private int id;
    private int question_id;
    private String content;

    public Answer(String content){
        this.content = content;
    }

    public Answer(int id, int question_id, String content) {
        this.id = id;
        this.question_id = question_id;
        this.content = content;
    }

    public static Answer[] getListAsArrayByQuestionID(int questionID){
        ArrayList<Answer> answerList = new ArrayList<>();
        Answer obj;

        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM answer WHERE question_id = " + questionID);
            while(rs.next()){
                int id = rs.getInt("id");
                String content = rs.getString("content");
                obj = new Answer(id, questionID, content);
                answerList.add(obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Answer[] answerArray = new Answer[answerList.size()];
        answerArray = answerList.toArray(answerArray);
        return answerArray;
    }

    public static boolean delete(int id){
        String query = "DELETE FROM answer WHERE id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, id);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public String toString() {
        return this.content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
