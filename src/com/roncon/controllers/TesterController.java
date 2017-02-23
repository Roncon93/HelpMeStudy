package com.roncon.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TesterController implements Initializable
{
    @FXML private Label lblQuestionNum;
    @FXML private Label lblQuestionContent;
    @FXML private Label lblResult;
    @FXML private Label lblAnswer;
    @FXML private RadioButton radFirst;
    @FXML private RadioButton radSecond;
    @FXML private RadioButton radThird;
    @FXML private RadioButton radFourth;
    @FXML private Button btnSubmit;

    private int questionNum = -1;
    private boolean nextQuestion = false;
    private boolean resetTest = false;
    private JSONArray data;
    private RadioButton[] radAnswers;
    private long answerIndex;
    private char answerLetter;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        radAnswers = new RadioButton[]{ radFirst, radSecond, radThird, radFourth };

        Object obj = null;

        try {
            obj = new JSONParser().parse(new FileReader("cot3541_midterm.hms"));
        }

        catch (ParseException e) {
            e.printStackTrace();
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        data = (JSONArray) obj;

        nextQuestion();

        btnSubmit.setOnAction(event ->
        {
            if (getAnswersSelected() == null)
                return;

            else if (resetTest)
            {
                questionNum = 0;
                reset();
            }

            else if (!nextQuestion)
            {
                btnSubmit.setText("Next Question");
                nextQuestion = true;

                boolean correctAnswer = false;

                for (int i = 0; i < radAnswers.length && correctAnswer == false; i++) {
                    if (radAnswers[i].isSelected() && i == answerIndex)
                        correctAnswer = true;
                }

                lblResult.setVisible(true);

                if (!correctAnswer)
                {
                    lblResult.setText("Wrong!");

                    lblAnswer.setVisible(true);
                    lblAnswer.setText("Correct Answer: " + answerLetter);
                } else
                    lblResult.setText("Correct!");

                if (questionNum >= data.size() - 1)
                {
                    btnSubmit.setText("Restart");
                    resetTest = true;
                }

                setAnswersDisable(true);
            }

            else
                nextQuestion();
        });
    }

    private void nextQuestion()
    {
        questionNum++;

        reset();
    }

    private void reset()
    {
        if (questionNum >= data.size())
            btnSubmit.setDisable(true);

        else
        {
            JSONObject questionData = (JSONObject) data.get(questionNum);
            String question = (String) questionData.get("question");
            JSONArray answers = (JSONArray) questionData.get("answers");

            lblQuestionContent.setText(question);

            for (int i = 0; i < radAnswers.length; i++) {
                RadioButton button = radAnswers[i];

                if (i < answers.size()) {
                    char letter = (char) ('a' + i);

                    button.setVisible(true);
                    button.setText(letter + ". " + answers.get(i));
                } else
                    button.setVisible(false);
            }

            answerIndex = (long) questionData.get("correct_answer");
            answerLetter = Character.toUpperCase((char) ('a' + answerIndex));
        }

        lblResult.setVisible(false);
        lblAnswer.setVisible(false);

        lblQuestionNum.setText("Question " + (questionNum + 1));

        btnSubmit.setText("Submit");

        setAnswersSelected(false);
        setAnswersDisable(false);

        nextQuestion = false;
        resetTest = false;
    }

    private void setAnswersDisable(boolean value)
    {
        for (RadioButton button : radAnswers)
            button.setDisable(value);
    }

    private void setAnswersSelected(boolean value)
    {
        for (RadioButton button : radAnswers)
            button.setSelected(value);
    }

    private RadioButton getAnswersSelected()
    {
        for (RadioButton button : radAnswers)
        {
            if (button.isSelected())
                return button;
        }

        return null;
    }
}
