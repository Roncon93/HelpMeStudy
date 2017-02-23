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

/**
 * Handles all user interaction for tester.fxml.
 */
public class TesterController implements Initializable
{
    // Constants
    private static final String FILENAME = "Your Test File Here";

    // UI Components
    @FXML private Label lblQuestionNum;
    @FXML private Label lblQuestionContent;
    @FXML private Label lblResult;
    @FXML private Label lblAnswer;
    @FXML private RadioButton radFirst;
    @FXML private RadioButton radSecond;
    @FXML private RadioButton radThird;
    @FXML private RadioButton radFourth;
    @FXML private Button btnSubmit;

    private int questionNum = -1;           // Current question number
    private boolean nextQuestion = false;   // Goes to the next question on the test
    private boolean resetTest = false;      // Restarts the test to the first question
    private JSONArray testData;             // Stores the testData of all the questions
    private RadioButton[] radAnswers;       // Stores the list of radio buttons
    private long answerIndex;               // Index of the current answer
    private char answerLetter;              // Letter of the current answer

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        radAnswers = new RadioButton[]{ radFirst, radSecond, radThird, radFourth };

        Object jsonObject = null;

        try {
            jsonObject = new JSONParser().parse(new FileReader(FILENAME));
        }

        catch (ParseException e) {
            e.printStackTrace();
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        testData = (JSONArray) jsonObject;

        nextQuestion();

        btnSubmit.setOnAction(event -> {
            submit();
        });
    }

    /**
     * Checks if the currently selected answer is correct and updates the UI accordingly.
     */
    private void submit()
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

            for (int i = 0; i < radAnswers.length && correctAnswer == false; i++)
            {
                if (radAnswers[i].isSelected() && i == answerIndex)
                    correctAnswer = true;
            }

            lblResult.setVisible(true);

            if (!correctAnswer)
            {
                lblResult.setText("Wrong!");

                lblAnswer.setVisible(true);
                lblAnswer.setText("Correct Answer: " + answerLetter);
            }

            else
                lblResult.setText("Correct!");

            if (questionNum >= testData.size() - 1)
            {
                btnSubmit.setText("Restart");
                resetTest = true;
            }

            setAnswersDisable(true);
        }

        else
            nextQuestion();
    }

    /**
     * Displays the next question on the test.
     */
    private void nextQuestion()
    {
        questionNum++;

        reset();
    }

    /**
     * Loads the current question and resets the UI states.
     */
    private void reset()
    {
        if (questionNum >= testData.size())
            btnSubmit.setDisable(true);

        else
        {
            JSONObject questionData = (JSONObject) testData.get(questionNum);
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

    /**
     * Sets the disable state for all radio buttons in the list.
     * @param value The value of the disable state.
     */
    private void setAnswersDisable(boolean value)
    {
        for (RadioButton button : radAnswers)
            button.setDisable(value);
    }

    /**
     * Sets the selected state for all radio buttons in the list.
     * @param value The value of the selected state.
     */
    private void setAnswersSelected(boolean value)
    {
        for (RadioButton button : radAnswers)
            button.setSelected(value);
    }

    /**
     * Gets the currently selected radio button in the list.
     * @return The currently selected radio button or null of none are selected.
     */
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
