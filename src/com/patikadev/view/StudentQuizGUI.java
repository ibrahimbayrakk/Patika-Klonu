package com.patikadev.view;

import com.patikadev.helper.Config;
import com.patikadev.helper.Helper;
import com.patikadev.model.Question;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class StudentQuizGUI extends JFrame {
    private JPanel wrapper;

    private int quizID;
    private ArrayList<JPanel> questionPanels;
    private ArrayList<JLabel> questionIndexLabels;
    private ArrayList<JLabel> questionLabels;
    private ArrayList<JRadioButton[]> answersRadioButtons;

    public StudentQuizGUI(int quizID){
        this.quizID = quizID;
        JPanel main = new JPanel( new GridLayout(0,1,1,1) );
        main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        main.setAlignmentX(JPanel.CENTER_ALIGNMENT);

        prepareQuestions(main);
        prepareQuestionGUI(main);

        setSize(800, 600);
        setLocation(Helper.screenCenter("x", getSize()), Helper.screenCenter("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setVisible(true);
    }

    public void prepareQuestions(JPanel main){
        questionPanels = new ArrayList<>();
        questionIndexLabels = new ArrayList<>();
        questionLabels = new ArrayList<>();
        answersRadioButtons = new ArrayList<>();

        int i = 0;
        for(Question q : Question.getQuestionsByQuizID(quizID)){
            JPanel tempQuestionPanel = new JPanel(new GridLayout(0, 1, 1, 1));
            tempQuestionPanel.setPreferredSize(new Dimension(main.getWidth(), 100));

            JLabel tempQuestionIndexLabel = new JLabel("Soru - " + (++i) + " (Doğru Cevap: " + q.getCorrectAnswerContent() + ")");
            Font f = tempQuestionIndexLabel.getFont();
            tempQuestionIndexLabel.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
            questionIndexLabels.add(tempQuestionIndexLabel);
            tempQuestionPanel.add(tempQuestionIndexLabel);

            JLabel tempQuestionLabel = new JLabel(q.getContent());
            questionLabels.add(tempQuestionLabel);
            tempQuestionPanel.add(tempQuestionLabel);

            JRadioButton answer1RadioButton = new JRadioButton(q.getAnswers()[0].getContent());
            JRadioButton answer2RadioButton = new JRadioButton(q.getAnswers()[1].getContent());
            JRadioButton answer3RadioButton = new JRadioButton(q.getAnswers()[2].getContent());
            JRadioButton answer4RadioButton = new JRadioButton(q.getAnswers()[3].getContent());
            JRadioButton[] tempRadioButtonsList = new JRadioButton[4];
            tempRadioButtonsList[0] = answer1RadioButton;
            tempRadioButtonsList[1] = answer2RadioButton;
            tempRadioButtonsList[2] = answer3RadioButton;
            tempRadioButtonsList[3] = answer4RadioButton;
            answersRadioButtons.add(tempRadioButtonsList);
            tempQuestionPanel.add(answer1RadioButton);
            tempQuestionPanel.add(answer2RadioButton);
            tempQuestionPanel.add(answer3RadioButton);
            tempQuestionPanel.add(answer4RadioButton);

            questionPanels.add(tempQuestionPanel);

            for(int j = 0; j < answersRadioButtons.size(); j++){
                final JRadioButton tempAnswer1RadioButton = answersRadioButtons.get(j)[0];
                final JRadioButton tempAnswer2RadioButton = answersRadioButtons.get(j)[1];
                final JRadioButton tempAnswer3RadioButton = answersRadioButtons.get(j)[2];
                final JRadioButton tempAnswer4RadioButton = answersRadioButtons.get(j)[3];

                tempAnswer1RadioButton.addActionListener(e -> {
                    tempAnswer1RadioButton.setSelected(true);
                    tempAnswer2RadioButton.setSelected(false);
                    tempAnswer3RadioButton.setSelected(false);
                    tempAnswer4RadioButton.setSelected(false);
                });
                tempAnswer2RadioButton.addActionListener(e -> {
                    tempAnswer1RadioButton.setSelected(false);
                    tempAnswer2RadioButton.setSelected(true);
                    tempAnswer3RadioButton.setSelected(false);
                    tempAnswer4RadioButton.setSelected(false);
                });
                tempAnswer3RadioButton.addActionListener(e -> {
                    tempAnswer1RadioButton.setSelected(false);
                    tempAnswer2RadioButton.setSelected(false);
                    tempAnswer3RadioButton.setSelected(true);
                    tempAnswer4RadioButton.setSelected(false);
                });
                tempAnswer4RadioButton.addActionListener(e -> {
                    tempAnswer1RadioButton.setSelected(false);
                    tempAnswer2RadioButton.setSelected(false);
                    tempAnswer3RadioButton.setSelected(false);
                    tempAnswer4RadioButton.setSelected(true);
                });
            }
        }
    }

    public void prepareQuestionGUI(JPanel main){
        for(int i = 0; i < this.questionPanels.size(); i++){
            main.add(questionPanels.get(i));
        }
        JScrollPane scrollPane = new JScrollPane(main,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JButton btn_quizDone = new JButton("Quizi Tamamla");
        btn_quizDone.setSize(new Dimension(100, 40));
        main.add(btn_quizDone);
        add(main);
    }
}
