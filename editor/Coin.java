package editor;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;
import utility.Constant;

import java.io.File;

public class Coin extends Group {
    private Circle outerCircle;
    private Circle innerCircle;
    private Label label;
    private ImageView skinImage;
    private Color innerColor;
    private Color outerColor;
    private int skin;
    private int indX;
    private int indY;
    private boolean cantMove=false;
    private boolean isMust=false;
    private boolean useSkin=false;

    public int getSkin() {
        return skin;
    }

    public boolean isCantMove() {
        return cantMove;
    }

    public void setCantMove(boolean cantMove) {
        this.cantMove = cantMove;
    }

    public boolean isMust() {
        return isMust;
    }

    public void setMust(boolean must) {
        isMust = must;
    }

    public boolean isUseSkin() {
        return useSkin;
    }

    public void setUseSkin(boolean useSkin) {
        this.useSkin = useSkin;
    }

    public Coin(double centerX, double centerY, double radius){
        outerCircle=new Circle(radius);
        innerCircle=new Circle(radius/2);
        skinImage=new ImageView();
        skinImage.setFitHeight(radius*2);
        skinImage.setFitWidth(radius*2);
        skinImage.setVisible(false);
        skinImage.setLayoutX(centerX-radius);
        skinImage.setLayoutY(centerY-radius);
        skin=0;
        outerCircle.setFill(Constant.COLOR_SKYBLUE);
        innerCircle.setFill(Constant.COLOR_SKYBLUE_DARK);
        outerColor=Constant.COLOR_SKYBLUE;
        innerColor=Constant.COLOR_SKYBLUE_DARK;
        innerCircle.setCenterX(centerX);
        outerCircle.setCenterX(centerX);
        innerCircle.setCenterY(centerY);
        outerCircle.setCenterY(centerY);
        label=new Label();
        label.setPrefWidth(radius*1.5);
        label.setPrefHeight(radius*1.5);
        label.setStyle("-fx-background-color: #00000088;-fx-font-size: 3em;-fx-text-alignment:center;-fx-alignment:center;-fx-text-fill:#ffffff;-fx-background-radius: "+radius+";");
        label.setLayoutX(centerX-radius*0.75);
        label.setLayoutY(centerY-radius*0.75);
        label.setVisible(false);
        getChildren().addAll(outerCircle,innerCircle,skinImage,label);
    }

    public int getIndX() {
        return indX;
    }

    public void setIndX(int indX) {
        this.indX = indX;
    }

    public int getIndY() {
        return indY;
    }

    public void setIndY(int indY) {
        this.indY = indY;
    }

    public void setInnerColor(Color color){
        innerCircle.setFill(color);
        innerColor=color;
    }

    public void setOuterColor(Color color){
        outerCircle.setFill(color);
        outerColor=color;
    }

    public Color getInnerColor(){
        return innerColor;
    }

    public Color getOuterColor(){
        return outerColor;
    }

    public void setSkin(int index){
        skin=index;
    }

    public void setCenterX(double centerX){
        skinImage.setLayoutX(centerX-outerCircle.getRadius());
        innerCircle.setCenterX(centerX);
        outerCircle.setCenterX(centerX);
        label.setLayoutX(centerX-outerCircle.getRadius()*0.75);
    }

    public void setCenterY(double centerY){
        skinImage.setLayoutY(centerY-outerCircle.getRadius());
        innerCircle.setCenterY(centerY);
        outerCircle.setCenterY(centerY);
        label.setLayoutY(centerY-outerCircle.getRadius()*0.75);
    }

    public void setSelected(boolean b){
        if(b){
            if(useSkin){
                skinImage.setVisible(false);
                outerCircle.setVisible(true);
            }
            innerCircle.setVisible(false);
            outerCircle.setFill(Color.LIMEGREEN);
            label.setVisible(false);
        }else{
            if(!label.getText().isEmpty())label.setVisible(true);
            if(useSkin){
                skinImage.setVisible(true);
                outerCircle.setVisible(false);
                innerCircle.setVisible(false);
            }else{
                skinImage.setVisible(false);
                outerCircle.setVisible(true);
                innerCircle.setVisible(true);
                outerCircle.setFill(outerColor);
            }
        }
    }

    public void useSkin(boolean b){
        if(b){
            useSkin=true;
            innerCircle.setVisible(false);
            outerCircle.setVisible(false);
            Image image=new Image(Constant.SKINS_COIN_LOCATION[skin]);
            skinImage.setImage(image);
            skinImage.setVisible(true);
        }else{
            useSkin=false;
            skinImage.setVisible(false);
            innerCircle.setVisible(true);
            outerCircle.setVisible(true);
        }
    }

    public void setText(String s){
        if(!s.isEmpty()){
            label.setText(s);
            label.setVisible(true);
        }
        else label.setVisible(false);
    }
    public String getText(){
        return label.getText();
    }

}
