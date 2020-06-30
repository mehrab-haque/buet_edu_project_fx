package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

public class Keyword implements Initializable {
    @FXML public Label text;
    @FXML public Button remove;
    @FXML public Rectangle rect;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        remove.setVisible(false);
        rect.setVisible(false);
    }
}
