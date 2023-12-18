package com.ppzhu.newmegafx.test;/*
 * @Author ppzhu
 * @Date 2023/12/16 16:03
 * @Discription
 */

import com.ppzhu.newmegafx.MegaApplication;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.ProgressBar;

public class ProcessBarApplication extends Application {
    public static void main(String[] args) {
        Application.launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Pane pane  = FXMLLoader.load(MegaApplication.class.getResource("objectList.fxml"));
        stage.setScene(new Scene(pane));
        stage.show();
    }
}
