package com.ppzhu.newmegafx;

import com.ppzhu.newmegafx.entry.MegaManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class MegaApplication extends Application {
    private MegaManager megaManager = MegaManager.getInstance();
    public static Stage primarystage;
    @Override
    public void start(Stage stage) throws IOException {
        primarystage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(MegaApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        stage.getIcons().add(new Image(getClass().getResource("logo.png").toExternalForm()));
    }
    public static void main(String[] args) {
        launch();
    }
}