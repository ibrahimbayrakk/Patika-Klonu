package com.patikadev.view;

import com.patikadev.helper.Config;
import com.patikadev.helper.Helper;
import com.patikadev.model.Question;
import com.patikadev.model.Quiz;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;

public class EducatorQuizGUI extends JFrame {
    private JPanel wrapper;
    private JScrollPane scrl_quiz;
    private JLabel fld_quizName;
    private JButton btn_addQuestion;
    private JButton btn_deleteQuestion;
    private JTable tbl_questions;
    private DefaultTableModel mdl_questionList;
    private Object[] row_questionList;

    private int quiz_id;
    private EducatorGUI educatorGUI;

    public EducatorQuizGUI(EducatorGUI educatorGUI, int quiz_id){
        this.quiz_id = quiz_id;
        this.educatorGUI = educatorGUI;

        add(wrapper);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                educatorGUI.setLocation(Helper.screenCenter("x", educatorGUI.getSize()), Helper.screenCenter("y", educatorGUI.getSize()));
                educatorGUI.setEnabled(true);
                educatorGUI.setVisible(true);
            }
        });
        setSize(800, 600);
        setLocation(Helper.screenCenter("x", getSize()), Helper.screenCenter("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setVisible(true);

        Quiz currentQuiz = Quiz.getQuizByID(quiz_id);
        String quizName = currentQuiz.getName();
        fld_quizName.setText(quizName);

        mdl_questionList = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        Object[] col_questionList = {"ID", "Content", "Answers", "Correct Answer"};
        mdl_questionList.setColumnIdentifiers(col_questionList);
        row_questionList = new Object[col_questionList.length];
        loadQuestionModel();

        tbl_questions.setModel(mdl_questionList);
        tbl_questions.getTableHeader().setReorderingAllowed(false);
        tbl_questions.getColumnModel().getColumn(0).setMaxWidth(75);

        btn_addQuestion.addActionListener(new ActionListener() {
            private int anonQuizID;
            private EducatorQuizGUI anonEducatorQuizGUI;
            @Override
            public void actionPerformed(ActionEvent e) {
                AddQuestionGUI addQuestionGUI = new AddQuestionGUI(anonEducatorQuizGUI, anonQuizID);
            }

            private ActionListener init(int quiz_id, EducatorQuizGUI educatorQuizGUI){
                anonQuizID = quiz_id;
                anonEducatorQuizGUI = educatorQuizGUI;
                return this;
            }
        }.init(quiz_id, this));

        btn_deleteQuestion.addActionListener(e -> {
            if(tbl_questions.getSelectedRow() != -1){
                if(Helper.confirm("sure")){
                    if(Question.delete(Integer.parseInt(tbl_questions.getValueAt(tbl_questions.getSelectedRow(), 0).toString()))){
                        Helper.showMessage("done");
                        loadQuestionModel();
                    }else{
                        Helper.showMessage("error");
                    }
                }
            }
        });
    }

    public void loadQuestionModel(){
        DefaultTableModel clearModel = (DefaultTableModel) tbl_questions.getModel();
        clearModel.setRowCount(0);

        int i;
        for(Question q : Question.getQuestionsByQuizID(this.quiz_id)){
            i = 0;
            row_questionList[i++] = q.getId();
            row_questionList[i++] = q.getContent();
            row_questionList[i++] = Arrays.toString(q.getAnswers());
            row_questionList[i++] = q.getCorrectAnswerContent();
            mdl_questionList.addRow(row_questionList);
        }
    }

    public int getQuiz_id() {
        return quiz_id;
    }
}
