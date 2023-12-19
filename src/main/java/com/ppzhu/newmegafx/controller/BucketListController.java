package com.ppzhu.newmegafx.controller;

import com.ppzhu.newmegafx.MegaApplication;
import com.ppzhu.newmegafx.client.NewMegaClient;
import com.ppzhu.newmegafx.entry.MegaBucket;
import com.ppzhu.newmegafx.entry.NewMegaManager;
import com.ppzhu.newmegafx.thread.RefreshListCall;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

public class BucketListController implements Initializable {
    public   TableView tableView;
    private static TableView sbuTableView;
    private static NewMegaClient megaClient;
    private static Stage createStage;
    public ProgressIndicator progressIndicator;
    public TableColumn name;
    public TableColumn createDate;
    private NewMegaManager megaManager = NewMegaManager.getInstance();
    private static String bucketName;
    private static Stage objectListStage;

    public static TableView sbuTableView() {
        return sbuTableView;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        name.setStyle("-fx-alignment: CENTER;");
        createDate.setStyle("-fx-alignment: CENTER;");
        this.name.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.createDate.setCellValueFactory(new PropertyValueFactory<>("creationTime"));
        progressIndicator.setVisible(true);
        progressIndicator.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
        this.megaManager = NewMegaManager.getInstance();
        sbuTableView = tableView;
        ObservableList<MegaBucket> bucketArrayList = FXCollections.observableArrayList();
        Thread newStageThread = new Thread(() -> {
            megaClient =megaManager.getMegaClient();
            megaManager.setMegaClient(megaClient);
            S3Client client = megaClient.getClient();
            ListBucketsResponse listBucketsResponse = client.listBuckets();
            List<software.amazon.awssdk.services.s3.model.Bucket> buckets = listBucketsResponse.buckets();

            int size = buckets.size();
            bucketArrayList.clear();
            Iterator<Bucket> iterator = buckets.iterator();
            while (iterator.hasNext()){
                Bucket next = iterator.next();
                Instant instant = next.creationDate();
                long epochMilli = instant.toEpochMilli();
                Date date = new Date(epochMilli);
                MegaBucket megaBucket = new MegaBucket(
                        next.name(),
                        date
                );
                bucketArrayList.add(megaBucket);
            }

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    tableView.setItems(bucketArrayList);
//                    processBar.setVisible(false);
                }
            });
            progressIndicator.setVisible(false);
        });
        newStageThread.start();


    }


    public void createBucket(ActionEvent actionEvent) {
        Stage stage = new Stage();
        try {
            Pane pane = FXMLLoader.load(MegaApplication.class.getResource("createBucketPage.fxml"));
            stage.setScene(new Scene(pane));
            stage.setTitle("create bucket");
            stage.initOwner(LoginController.bucketsListStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.getIcons().add(new Image(MegaApplication.class.getResource("logo.png").toExternalForm()));
            stage.setResizable(false);
            createStage = stage;
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Stage getCreateStage(){
        return createStage;
    }

    public void refreshList(ActionEvent actionEvent) {
        RefreshListCall listCall = new RefreshListCall(progressIndicator);
        Thread thread = new Thread(listCall);
        thread.start();
    }

    public void openBucket(ActionEvent actionEvent) {
        MegaBucket megaBucket  = (MegaBucket) tableView.getSelectionModel().getSelectedItem();
        bucketName = megaBucket.getName();

        Stage stage = new Stage();
        stage.getIcons().add(new Image(MegaApplication.class.getResource("logo.png").toExternalForm()));
        stage.setTitle(bucketName);
        try {
            Pane pane = FXMLLoader.load(MegaApplication.class.getResource("objectList.fxml"));
            Scene scene = new Scene(pane);
            stage.setScene(scene);
            stage.initOwner(LoginController.bucketsListStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            objectListStage=stage;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        stage.show();
    }
    public static String getBucketName(){
        return bucketName;
    }
    public static Stage getObjectListStage(){
        return objectListStage;
    }
}
