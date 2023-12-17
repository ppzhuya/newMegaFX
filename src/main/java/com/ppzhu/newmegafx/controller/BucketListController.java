package com.ppzhu.newmegafx.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.ppzhu.newmegafx.MegaApplication;
import com.ppzhu.newmegafx.client.MegaClient;
import com.ppzhu.newmegafx.entry.MegaBucket;
import com.ppzhu.newmegafx.entry.MegaManager;
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

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class BucketListController implements Initializable {
    public   TableView tableView;
    private static TableView sbuTableView;
    private static MegaClient megaClient;
    private static Stage createStage;
    public ProgressIndicator progressIndicator;
    public TableColumn name;
    public TableColumn createDate;
    private MegaManager megaManager;

    public static TableView sbuTableView() {
        return sbuTableView;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.name.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.createDate.setCellValueFactory(new PropertyValueFactory<>("creationTime"));
        progressIndicator.setVisible(true);
        progressIndicator.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
        this.megaManager = MegaManager.getInstance();
        sbuTableView = tableView;
        ObservableList<MegaBucket> bucketArrayList = FXCollections.observableArrayList();
        Thread newStageThread = new Thread(() -> {
            megaClient =megaManager.getMegaClient();
            megaManager.setMegaClient(megaClient);
            AmazonS3 client = megaClient.getClient();
            List<Bucket> buckets = client.listBuckets();
            int size = buckets.size();
            System.out.println(size);
            bucketArrayList.clear();
            Iterator<Bucket> iterator = buckets.iterator();
            while (iterator.hasNext()) {
                Bucket next = iterator.next();
                System.out.println(next.getName());
                MegaBucket megaBucket = new MegaBucket(
                        next.getName(),
                        next.getCreationDate()
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

    }
}
