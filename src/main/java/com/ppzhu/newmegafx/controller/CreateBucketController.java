package com.ppzhu.newmegafx.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.IllegalBucketNameException;
import com.ppzhu.newmegafx.client.MegaClient;
import com.ppzhu.newmegafx.entry.MegaManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


public class CreateBucketController implements Initializable {

    public TextField bucketName;
    public ProgressIndicator progressIndicator;
    private MegaClient megaClient ;
    private Stage stage ;
    private MegaManager megaManager = MegaManager.getInstance();

    public void createBucket(ActionEvent actionEvent) {
        progressIndicator.setVisible(true);
        progressIndicator.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
        stage = BucketListController.getCreateStage();
        String text = bucketName.getText();
        if (Character.isDigit(text.charAt(0))){
            Alert alert =  new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Bucket name can't start with a number!");
            alert.showAndWait();
            progressIndicator.setVisible(false);
        }else {
            /*
        将用户的输入转换成小写
         */
            String lowerCase = text.toLowerCase();
            megaClient = megaManager.getMegaClient();

            AmazonS3 client = megaClient.getClient();
            Thread thread = new Thread(){
                @Override
                public void run() {
                    try {
                        Bucket bucket = client.createBucket(lowerCase);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setContentText(bucket.getName()+"create success!");
                                alert.showAndWait();
                                if (stage!=null){
                                    stage.close();
                                }
                                progressIndicator.setVisible(false);
                            }
                        });
                    } catch (AmazonS3Exception e) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setContentText("The bucket already exists!");
                                alert.showAndWait();
                                bucketName.setText("");
                                progressIndicator.setVisible(false);
                            }
                        });
                    } catch (IllegalBucketNameException e){
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setContentText("Illegal characters!");
                                alert.showAndWait();
                                progressIndicator.setVisible(false);
                            }
                        });

                    }
                }

            };
            thread.start();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        progressIndicator.setPrefWidth(25);
        progressIndicator.setPrefHeight(25);
        progressIndicator.setVisible(false);
    }
}
