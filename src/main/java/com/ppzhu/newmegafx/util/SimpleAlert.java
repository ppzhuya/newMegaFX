package com.ppzhu.newmegafx.util;/*
 * @Author ppzhu
 * @Date 2023/12/17 23:37
 * @Discription
 */

import javafx.scene.control.Alert;

public class SimpleAlert {
    private String content;
    private Alert.AlertType type;

    public SimpleAlert(String content,Alert.AlertType type) {
        this.content = content;
        this.type = type;
    }

    public  void alert()
    {
        Alert alert = new Alert(type);
        alert.setContentText(content);
        alert.showAndWait();
    }}
