package com.ppzhu.newmegafx.controller;
/*
 * @Author ppzhu
 * @Date 2023/12/18 13:40
 * @Discription
 */

import com.amazonaws.services.s3.AmazonS3;
import com.ppzhu.newmegafx.client.MegaClient;
import com.ppzhu.newmegafx.entry.MegaManager;
import com.ppzhu.newmegafx.thread.UploadCall;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class UploadController implements Initializable {

    public ProgressBar progressBar;
    public Rectangle uploadArea;
    public TextField customizeKey;
    public Label label;
    private String bucketName;
    private MegaManager megaManager = MegaManager.getInstance();

    public void upload(MouseEvent mouseEvent) {
        Stage uploadStage = ObjectListController.getUploadStage();
        String title = uploadStage.getTitle();
        bucketName = title.replace("upload to ", "");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("c:\\"));
        fileChooser.setTitle("upload");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            System.out.println(file.getAbsolutePath());
            String key = file.getName();
            /*
            file upload
            File:C:\appverifUI.dll
             */

            System.out.println("key:" + key);
            System.out.println("ckey:" + customizeKey.getText());

            MegaClient megaClient = megaManager.getMegaClient();
            AmazonS3 client = megaClient.getClient();
            System.out.println(bucketName);
            try {
                if (customizeKey.getText() == null) {
                    UploadCall uploadCall = new UploadCall(bucketName, key, megaManager, file);
                    FutureTask futureTask = new FutureTask(uploadCall);
                    Thread thread = new Thread(futureTask);
                    thread.start();
                    futureTask.get();
                } else {
                    UploadCall uploadCall = new UploadCall(bucketName, customizeKey.getText(), megaManager, file);
                    FutureTask futureTask = new FutureTask(uploadCall);
                    Thread thread = new Thread(futureTask);
                    thread.start();
                    futureTask.get();
                }
                client.shutdown();
            } catch (ExecutionException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            } catch (InterruptedException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }catch (RuntimeException e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("upload success");
            alert.showAndWait();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /*
        get bucketName
         */
        label.setTooltip(new Tooltip("If you don't customize the file name, the default file name will be used, and if the default file name conflicts, the upload will fail!"));
    }
}
