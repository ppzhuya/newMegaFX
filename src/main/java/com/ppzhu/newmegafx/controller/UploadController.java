package com.ppzhu.newmegafx.controller;
/*
 * @Author ppzhu
 * @Date 2023/12/18 13:40
 * @Discription
 */

import com.ppzhu.newmegafx.client.NewMegaClient;
import com.ppzhu.newmegafx.entry.NewMegaManager;
import com.ppzhu.newmegafx.thread.UploadCall;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class UploadController implements Initializable {

    public ProgressBar progressBar;
    public Rectangle uploadArea;
    public TextField customizeKey;
    private String bucketName;
    private NewMegaManager megaManager = NewMegaManager.getInstance();

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


            NewMegaClient megaClient = megaManager.getMegaClient();

            S3Client client = megaClient.getClient();
            System.out.println(bucketName);
            try {
                UploadCall uploadCall = new UploadCall(bucketName, key, megaManager, file, uploadStage);
                FutureTask futureTask = new FutureTask(uploadCall);
                Thread thread = new Thread(futureTask);
                thread.start();
                futureTask.get();

                client.close();
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
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
        uploadArea.setOnDragOver(dragEvent -> {
            dragEvent.acceptTransferModes(TransferMode.ANY);
        });
        uploadArea.setOnDragEntered(dragEvent -> {
            Dragboard dragboard = dragEvent.getDragboard();
            if (dragboard.hasFiles()) {
                // 设置鼠标样式
                uploadArea.setCursor(Cursor.HAND);
            }
        });

        uploadArea.setOnDragDropped(dragEvent -> {
            Dragboard dragboard = dragEvent.getDragboard();
            if (dragboard.hasFiles()) {
                List<File> files = dragboard.getFiles();
                for (File file :
                        files) {
                    System.out.println(file.getName());
                    UploadCall uploadCall = new UploadCall(ObjectListController.getBucketName(), file.getName(), megaManager, file,ObjectListController.getUploadStage());
                    FutureTask futureTask = new FutureTask(uploadCall);
                    Thread thread = new Thread(futureTask);
                    thread.start();
                }

            }
        });

    }
}
