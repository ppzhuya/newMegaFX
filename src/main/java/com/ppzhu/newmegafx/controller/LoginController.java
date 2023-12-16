package com.ppzhu.newmegafx.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ppzhu.newmegafx.MegaApplication;
import com.ppzhu.newmegafx.entry.Account;
import com.ppzhu.newmegafx.entry.MegaManager;
import com.ppzhu.newmegafx.thread.LoginCall;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class LoginController implements Initializable {

    public CheckBox checkBox;
    public PasswordField passworld;
    public TextField username;
    private static Account account;
    private File file = new File(MegaApplication.class.getClassLoader().getResource("account.json").getFile());


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (file.exists()) {
            try {
                String s = Files.readString(Path.of(file.toURI()));
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(s, JsonObject.class);
                String isSelected = jsonObject.get("isSelected").toString();
                if (isSelected.equals("true")) {
                    checkBox.setSelected(true);
                    String usernamejson = jsonObject.get("username").toString().replaceAll("\"", "");
                    String passwordjson = jsonObject.get("password").toString().replaceAll("\"", "");

                    passworld.setText(passwordjson);
                    username.setText(usernamejson);
                } else {
                    checkBox.setSelected(false);
                }

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void login() {
        System.out.println(checkBox.isSelected());
        if (!checkBox.isSelected()) {
            new Thread(){
                @Override
                public void run() {
                    try {
                        String jsonStr = "{\"isSelected\": false, \"username\": \"\", \"password\": \"\"}";
                        OutputStream outputStream = new FileOutputStream(file);
                        outputStream.write(jsonStr.getBytes());
                        outputStream.flush();
                        outputStream.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }.start();

        }


        LoginCall loginCall = new LoginCall(username.getText(), passworld.getText());
        FutureTask futureTask = new FutureTask(loginCall);
        Thread thread = new Thread(futureTask);
        thread.start();

        try {
            account = (Account) futureTask.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Database service startup failure!");
            alert.showAndWait();
        }

        if (account != null) {
            if (checkBox.isSelected()) {
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            String jsonStr = "{\"isSelected\": true, \"username\": \"" + username.getText() + "\", \"password\": \"" + passworld.getText() + "\"}";
                            OutputStream outputStream = new FileOutputStream(file);
                            outputStream.write(jsonStr.getBytes());
                            outputStream.flush();
                            outputStream.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }.start();

            }

            MegaManager instance = MegaManager.getInstance();
            instance.setAccount(account);
            /*
            新开窗口，进入系统
             */
            Stage stage = new Stage();
            stage.setTitle("bucket list");
            try {
                Pane pane = FXMLLoader.load(MegaApplication.class.getResource("bucketList.fxml"));
                stage.setScene(new Scene(pane));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.setResizable(false);
            stage.getIcons().add(new Image(MegaApplication.class.getResource("logo.png").toExternalForm()));
            stage.show();
            MegaApplication.primarystage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("UserName or PassWorld error!");
            alert.showAndWait();
        }

    }
}
