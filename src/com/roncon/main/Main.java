package com.roncon.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Program starting point. Loads the first scene into view.
 */
public class Main extends Application
{
    // Constants
    private static final String SCENE_FILE = "tester";
    private static final int SCENE_WIDTH   = 600;
    private static final int SCENE_HEIGHT  = 400;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("../scenes/" + SCENE_FILE + ".fxml"));
        primaryStage.setTitle("HelpMeStudy");
        primaryStage.setScene(new Scene(root, SCENE_WIDTH, SCENE_HEIGHT));
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
