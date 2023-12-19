package com.ppzhu.newmegafx.controller;/*
 * @Author ppzhu
 * @Date 2023/12/17 19:23
 * @Discription
 */

import com.ppzhu.newmegafx.MegaApplication;
import com.ppzhu.newmegafx.entry.NewMegaManager;
import com.ppzhu.newmegafx.entry.ObjectList;
import com.ppzhu.newmegafx.thread.DownloadCall;
import com.ppzhu.newmegafx.thread.ReflushObjectListCall;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class ObjectListController implements Initializable {
    public Label listName;
    public TableView tableView;
    public TableColumn key;
    public TableColumn size;
    public TableColumn lastModified;
    public ProgressIndicator progressIndicator;
    private NewMegaManager megaManager = NewMegaManager.getInstance();
    private static String bucketName = BucketListController.getBucketName();
    private String downloadPath ="D:\\MegaDownload\\";
    private static Stage uploadStage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        progressIndicator.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);

        this.key.setCellValueFactory(new PropertyValueFactory<>("key"));
        this.size.setCellValueFactory(new PropertyValueFactory<>("size"));
        this.lastModified.setCellValueFactory(new PropertyValueFactory<>("lastModified"));
        listName.setText(bucketName);
        ReflushObjectListCall reflushObjectListCall = new ReflushObjectListCall(megaManager,bucketName,tableView,progressIndicator);
        FutureTask futureTask = new FutureTask(reflushObjectListCall);
        Thread thread  = new Thread(futureTask);
        thread.start();


    }

    public void download() {
        ObjectList objectList = (ObjectList) tableView.getSelectionModel().getSelectedItem();

        if (objectList!=null){
            String keyName = objectList.getKey();
            String subkeyName = keyName;
            if (keyName.contains("/")){
                subkeyName =keyName.substring(keyName.indexOf("/"));
            }
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setInitialDirectory(new File("c:\\"));
            directoryChooser.setTitle("choose the path you want to save");
            File saveFile = directoryChooser.showDialog(null);
            System.out.println(saveFile.getAbsolutePath());
            File file = new File(saveFile.getAbsolutePath()+"\\"+subkeyName);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            DownloadCall downloadCall = new DownloadCall(bucketName,keyName,file);
            FutureTask futureTask = new FutureTask(downloadCall);
            Thread thread = new Thread(futureTask);
            thread.start();

        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("you have not selected anything");
            alert.showAndWait();
        }

    }

    public void upload(ActionEvent actionEvent) {
        Stage stage = new Stage();
        try {
            Pane pane = FXMLLoader.load(MegaApplication.class.getResource("upload.fxml"));
            stage.setResizable(false);
            Scene scene = new Scene(pane);
            uploadStage = stage;
            stage.setScene(scene);
            stage.initOwner(BucketListController.getObjectListStage());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.getIcons().add(new Image(MegaApplication.class.getResource("logo.png").toExternalForm()));
            stage.setTitle("upload to "+bucketName);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public static Stage getUploadStage(){
        return uploadStage;
    }

    public void reflush(ActionEvent actionEvent) {
        ReflushObjectListCall reflushObjectListCall = new ReflushObjectListCall(megaManager,bucketName,tableView,progressIndicator);
        FutureTask futureTask = new FutureTask(reflushObjectListCall);
        Thread thread = new Thread(futureTask);
        thread.start();
    }

    public void preview(ActionEvent actionEvent) {
    }
    public static String getBucketName(){
        return bucketName;
    }
}
