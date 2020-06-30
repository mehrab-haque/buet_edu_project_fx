package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;

public class BoardProperties {
    @FXML public ColorPicker bgColor;
    @FXML public ColorPicker graphColor;
    @FXML public ColorPicker indicatorColor;
    @FXML public CheckBox isIndicator;
    @FXML public Slider opSlider;
    @FXML public Button saveBtn;
    @FXML public Button cancelBtn;
    @FXML public Button defaultCoin;
    @FXML public Button defaultMatchStick;
}
