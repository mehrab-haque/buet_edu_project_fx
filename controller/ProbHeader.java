package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;

import java.net.URL;
import java.util.ResourceBundle;

public class ProbHeader implements Initializable {
    @FXML private Label label;
    @FXML public Button edit;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        label.setStyle("-fx-border-color: #0090ff; -fx-border-style: solid; -fx-border-width: 2;");
    }
}
