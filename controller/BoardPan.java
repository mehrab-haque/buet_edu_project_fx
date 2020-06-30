package controller;

import editor.Coin;
import editor.MatchStick;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import utility.Constant;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class BoardPan implements Initializable {
    @FXML private Rectangle rect1;
    @FXML private Rectangle rect2;
    @FXML private Rectangle rect3;
    @FXML private Rectangle rect4;
    @FXML private AnchorPane anchorPane;
    @FXML private Line line1;
    @FXML private Line line2;
    @FXML private Line line3;
    @FXML private Line lineH;
    public Group group1;
    public Group group2;
    public Group group3;
    public Group group4;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        group1=new Group();
        group2=new Group();
        group3=new Group();
        group4=new Group();

        Coin coin=new Coin(62.5,25,18);
        coin.setSkin(7);
        coin.useSkin(true);
        group1.getChildren().addAll(rect1,coin);

        MatchStick matchStick=new MatchStick(90,20,30,30);
        matchStick.setWeight(3.8);
        matchStick.useSkin(true);
        matchStick.setLayoutX(125);
        matchStick.setScaleX(2.5);
        matchStick.setScaleY(2.5);
        group2.getChildren().addAll(rect2,matchStick);

        ImageView imageView3=new ImageView(new Image("resources/text.png"));
        imageView3.setFitHeight(35);
        imageView3.setFitWidth(35);
        imageView3.setLayoutX(295);
        imageView3.setLayoutY(7.5);
        group3.getChildren().addAll(rect3,imageView3);

        ImageView imageView4=new ImageView(new Image("resources/settings.png"));
        imageView4.setFitHeight(40);
        imageView4.setFitWidth(40);
        imageView4.setLayoutX(417.5);
        imageView4.setLayoutY(5);
        group4.getChildren().addAll(rect4,imageView4);

        group1.setOnMouseMoved(event -> {
            rect1.setFill(Color.LIMEGREEN);
        });
        group1.setOnMouseExited(event -> {
            rect1.setFill(Color.WHITE);
        });

        group2.setOnMouseMoved(event -> {
            rect2.setFill(Color.LIMEGREEN);
        });
        group2.setOnMouseExited(event -> {
            rect2.setFill(Color.WHITE);
        });

        group3.setOnMouseMoved(event -> {
            rect3.setFill(Color.LIMEGREEN);
        });
        group3.setOnMouseExited(event -> {
            rect3.setFill(Color.WHITE);
        });

        group4.setOnMouseMoved(event -> {
            rect4.setFill(Color.LIMEGREEN);
        });
        group4.setOnMouseExited(event -> {
            rect4.setFill(Color.WHITE);
        });

        /*Tooltip tooltip1=new Tooltip("Drag to add coin");
        tooltip1.setShowDelay(Duration.ZERO);
        Tooltip.install(group1,tooltip1);

        Tooltip tooltip2=new Tooltip("Drag to add match-stick");
        tooltip2.setShowDelay(Duration.ZERO);
        Tooltip.install(group2,tooltip2);

        Tooltip tooltip3=new Tooltip("Drag to add text");
        tooltip3.setShowDelay(Duration.ZERO);
        Tooltip.install(group3,tooltip3);

        Tooltip tooltip4=new Tooltip("Board settings");
        tooltip4.setShowDelay(Duration.ZERO);
        Tooltip.install(group4,tooltip4);*/

        anchorPane.getChildren().addAll(group1,group2,group3,group4,line1,line2,line3,lineH);
    }
}
