package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

public class SolutionHeader implements Initializable {
    @FXML public Button add;
    @FXML public Button delete;
    @FXML public ComboBox<String> dropdown;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
