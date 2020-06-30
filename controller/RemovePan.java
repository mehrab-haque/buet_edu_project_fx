package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class RemovePan implements Initializable{

    @FXML public ImageView icon;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image image=new Image("resources/remove.png");
        icon.setImage(image);
    }
}
