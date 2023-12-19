package com.ppzhu.newmegafx.controller;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.IllegalBucketNameException;
import com.ppzhu.newmegafx.client.NewMegaClient;
import com.ppzhu.newmegafx.entry.NewMegaManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.CreateBucketResponse;

import java.net.URL;
import java.util.ResourceBundle;


public class CreateBucketController implements Initializable {

    public TextField bucketName;
    public ProgressIndicator progressIndicator;
    private NewMegaClient megaClient ;
    private Stage stage ;
    private NewMegaManager megaManager = NewMegaManager.getInstance();

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

            S3Client client = megaClient.getClient();
            Thread thread = new Thread(){
                @Override
                public void run() {
                    try {
                        CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
                                .bucket(lowerCase).build();
                        CreateBucketResponse bucket = client.createBucket(createBucketRequest);

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setContentText(lowerCase+"create success!");
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
