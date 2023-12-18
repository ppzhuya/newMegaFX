package com.ppzhu.newmegafx.controller;/*
 * @Author ppzhu
 * @Date 2023/12/18 13:40
 * @Discription
 */

import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;

public class UploadController {

    public ProgressBar progressBar;
    public Rectangle uploadArea;

    public void upload(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("c:\\"));
        fileChooser.setTitle("upload");
        File file = fileChooser.showOpenDialog(null);
        System.out.println(file.getAbsolutePath());
    }
}
