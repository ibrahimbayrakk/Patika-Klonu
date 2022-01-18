package com.patikadev.model;

import com.patikadev.helper.DBConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Question {

    private int id;
    private String content;
    private int correct_answer_id;
    private Answer[] answers;

    public Question(int id, String content, int correct_answer_id, Answer[] answers){
        this.id = id;
        this.content = content;
        this.correct_answer_id = correct_answer_id;
        this.answers = answers;
    }

    public static ArrayList<Question> getQuestionsByQuizID(int quizID){
        ArrayList<Question> questionList = new ArrayList<>();
        Question obj;

        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM question WHERE quiz_id = " + quizID);
            while (rs.next()){
                int id = rs.getInt("id");
                String content = rs.getString("content");
                int correct_answer_id = rs.getInt("correct_answer_id");

                // Get answers for current question
                Answer[] answersForCurrentQuestion = Answer.getListAsArrayByQuestionID(id);

                obj = new Question(id, content, correct_answer_id, answersForCurrentQuestion);
                questionList.add(obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return questionList;
    }

    public static boolean add(int quizID, String content, int correct_answer_idx, Answer[] answers){
        String queryQuestion = "INSERT INTO question (quiz_id, content, correct_answer_id) VALUES (?,?,?)";
        String queryAnswer = "INSERT INTO answer (question_id, content) VALUES (?,?)";
        String queryUpdateQuestionCorrectAnswerId = "UPDATE question SET correct_answer_id = ? WHERE id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(queryQuestion, Statement.RETURN_GENERATED_KEYS);
            pr.setInt(1, quizID);
            pr.setString(2, content);
            pr.setInt(3, -1); // Here set it to -1 then update it after all answers are inserted.

            int res = pr.executeUpdate();
            if(res == -1) return false;

            ResultSet rs = pr.getGeneratedKeys();
            rs.next();
            int currentQuestionID = rs.getInt(1);
            int currentCorrectAnswerID = -1;

            for(int i = 0; i < answers.length; i++){
                pr = DBConnector.getInstance().prepareStatement(queryAnswer, Statement.RETURN_GENERATED_KEYS);
                pr.setInt(1, currentQuestionID);
                pr.setString(2, answers[i].getContent());
                res = pr.executeUpdate();

                if(i == correct_answer_idx){
                    rs = pr.getGeneratedKeys();
                    rs.next();
                    currentCorrectAnswerID = rs.getInt(1);
                }

                if(res == -1) return false;
            }

            // Update correct_answer_id
            pr = DBConnector.getInstance().prepareStatement(queryUpdateQuestionCorrectAnswerId);
            pr.setInt(1,currentCorrectAnswerID);
            pr.setInt(2,currentQuestionID);
            System.out.println(pr.toString());
            res = pr.executeUpdate();

            rs.close();
            pr.close();

            return res != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean delete(int id){
        String query = "DELETE FROM question WHERE id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, id);
            int res = pr.executeUpdate();
            if(res != -1){
                Answer[] answerList = Answer.getListAsArrayByQuestionID(id);
                for(int i = 0; i < answerList.length; i++){
                    if(answerList[i].getQuestion_id() == id){
                        Answer.delete(answerList[i].getId());
                    }
                }
            }
            return res != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public String getCorrectAnswerContent(){
        String content = null;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM answer WHERE id = " + this.correct_answer_id);
            if(rs.next()){
                content = rs.getString("content");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCorrect_answer_id() {
        return correct_answer_id;
    }

    public void setCorrect_answer_id(int correct_answer_id) {
        this.correct_answer_id = correct_answer_id;
    }

    public Answer[] getAnswers() {
        return answers;
    }

    public void setAnswers(Answer[] answers) {
        this.answers = answers;
    }
}
