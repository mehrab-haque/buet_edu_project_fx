package controller;

import editor.MatchStick;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class StickProperties implements Initializable {
    @FXML public ColorPicker fillColor;
    @FXML public Button saveBtn;
    @FXML public Button cancelBtn;
    @FXML public CheckBox cantMove;
    @FXML public CheckBox isMust;
    @FXML public CheckBox useSkin;
    @FXML public HBox hBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MatchStick matchStick=new MatchStick(120,0,0,0);
        matchStick.useSkin(true);
        matchStick.setWeight(10);
        hBox.getChildren().add(matchStick);
    }
}
