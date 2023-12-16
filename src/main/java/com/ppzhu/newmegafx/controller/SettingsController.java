package com.ppzhu.newmegafx.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ppzhu.newmegafx.MegaApplication;
import com.ppzhu.newmegafx.entry.MegaManager;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    public CheckBox checkBox;
    public TextField ipTextFiled;
    public TextField portTextField;
    private MegaManager megaManager = MegaManager.getInstance();

    public void apply(MouseEvent mouseEvent) {

        String ip = ipTextFiled.getText();
        String portStr = portTextField.getText();
        System.out.println(ip);
        System.out.println(portStr);
        if (ip.equals(null) || portStr.equals(null)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("ip and port can't be null!");
            alert.showAndWait();
        }else {
            char[] charArray = portStr.toCharArray();
            boolean check = true;
            for (char a :
                    charArray) {
                if (!Character.isDigit(a)) {
                    check = false;
                    break;
                }
            }
            if (check == false){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("port is illegal!");
                alert.showAndWait();
            }else {
                Proxy proxy = new Proxy(Proxy.Type.HTTP,new InetSocketAddress(ip,Integer.parseInt(portStr)));

            }


        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        File file = new File(MegaApplication.class.getClassLoader().getResource("setting.json").getFile());
        try {
            String settingJson = Files.readString(Path.of(file.toURI()));
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(settingJson, JsonObject.class);
            String isSelected = jsonObject.get("isSelected").toString();
            if (isSelected.equals("true")){
                checkBox.setSelected(true);
            }else {
                checkBox.setSelected(false);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
