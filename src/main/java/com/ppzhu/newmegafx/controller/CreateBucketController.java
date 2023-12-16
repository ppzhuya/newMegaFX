package com.ppzhu.newmegafx.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.IllegalBucketNameException;
import com.ppzhu.newmegafx.client.MegaClient;
import com.ppzhu.newmegafx.entry.MegaManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class CreateBucketController {

    public TextField bucketName;
    private MegaClient megaClient ;
    private Stage stage ;
    private MegaManager megaManager = MegaManager.getInstance();

    public void createBucket(ActionEvent actionEvent) {
        stage = BucketListController.getCreateStage();
        String text = bucketName.getText();
        if (Character.isDigit(text.charAt(0))){
            Alert alert =  new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Bucket name can't start with a number!");
            alert.showAndWait();
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
                            }
                        });
                    } catch (IllegalBucketNameException e){
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setContentText("Illegal characters!");
                                alert.showAndWait();
                            }
                        });

                    }
                }

            };
            thread.start();
        }
    }
}
