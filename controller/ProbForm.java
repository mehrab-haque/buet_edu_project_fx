package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import utility.Constant;
import java.net.URL;
import java.util.ResourceBundle;

public class ProbForm implements Initializable {
    @FXML public TextField title;
    @FXML public TextField series;
    @FXML public TextField statement;
    @FXML public ComboBox<String> category;
    @FXML public ComboBox<String> difficulty;
    @FXML public ComboBox<String> ansType;
    @FXML public Button keywordBtn;
    @FXML public Button imgQ;
    @FXML public Button imgA;
    @FXML public VBox keyVBox;
    @FXML public VBox qImgVBox;
    @FXML public VBox aImgVBox;
    @FXML public ScrollPane scroll;
    @FXML public TextArea description;
    @FXML public VBox mcqVBox;
    @FXML public VBox optionsVBox;
    @FXML public Button addOptionBtn;
    @FXML public VBox txtVBox;
    @FXML public TextField ansTxt;
    @FXML public TextField time;
    @FXML public TextArea restrictions;
    @FXML public TextArea explanation;
    @FXML public ComboBox<String> ansOption;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        description.setStyle("-fx-padding: 0");
        restrictions.setStyle("-fx-padding: 0");
        explanation.setStyle("-fx-padding: 0");
        for(int i=0;i<Constant.CATEGORIES.length;i++)
            category.getItems().add(Constant.CATEGORIES[i]);
        for(int i=1;i<=10;i++)
            difficulty.getItems().add(i+"");
        ansType.getItems().addAll("Figure Board","Text","MCQ");
        ansType.getSelectionModel().select(0);
        txtVBox.setVisible(false);
        mcqVBox.setVisible(false);
        txtVBox.setManaged(false);
        mcqVBox.setManaged(false);
        ansType.setOnAction(event -> {
            if(ansType.getSelectionModel().getSelectedIndex()==0){
                txtVBox.setVisible(false);
                mcqVBox.setVisible(false);
                txtVBox.setManaged(false);
                mcqVBox.setManaged(false);
            }else if(ansType.getSelectionModel().getSelectedIndex()==1){
                txtVBox.setVisible(true);
                mcqVBox.setVisible(false);
                txtVBox.setManaged(true);
                mcqVBox.setManaged(false);
            }else{
                txtVBox.setVisible(false);
                mcqVBox.setVisible(true);
                txtVBox.setManaged(false);
                mcqVBox.setManaged(true);
            }
        });
        time.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    time.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }
}
