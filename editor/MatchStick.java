package editor;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import utility.Constant;

import java.awt.*;


public class MatchStick extends Group {
    private Line line;
    private Line stick;
    private Circle match;
    private Circle head;
    private Circle tail;
    private boolean isSelected;
    private int indHeadX;
    private int indHeadY;
    private int indTailX;
    private int indTailY;
    private Color fillColor;
    private Color stickColor;
    private Color headColor;
    private Group matchGroup;
    private boolean isMust;
    private boolean cantMove;

    public boolean isMust() {
        return isMust;
    }

    public void setMust(boolean must) {
        isMust = must;
    }

    public boolean isCantMove() {
        return cantMove;
    }

    public void setCantMove(boolean cantMove) {
        this.cantMove = cantMove;
    }

    private boolean isSkin;
    private double matchDeductDiv=0.14;
    public MatchStick(double startX, double startY, double endX, double endY){
        isSkin=false;
        matchGroup=new Group();
        line=new Line(startX,startY,endX,endY);
        line.setStrokeWidth(10);
        head=new Circle(10);
        tail=new Circle(10);
        head.setCenterX(startX);
        head.setCenterY(startY);
        tail.setCenterX(endX);
        tail.setCenterY(endY);
        getChildren().addAll(line,head,tail);
        isSelected=false;
        head.setFill(Color.LIMEGREEN);
        tail.setFill(Color.LIMEGREEN);
        head.setVisible(false);
        tail.setVisible(false);
        line.setStroke(Constant.COLOR_SKYBLUE);
        fillColor=Constant.COLOR_SKYBLUE;
        headColor=Constant.COLOR_MATCHSTICK_HEAD;
        stickColor=Constant.COLOR_MATCHSTICK_LINE;

        stick=new Line(line.getStartX(),line.getStartY(),line.getEndX(),line.getEndY());
        stick.setStrokeWidth(line.getStrokeWidth());
        stick.setStroke(stickColor);
        match=new Circle(stick.getStrokeWidth()/1.5);
        match.setCenterX(stick.getStartX());
        match.setCenterY(stick.getStartY());
        match.setFill(headColor);
        matchGroup.getChildren().addAll(stick,match);
        double length1=Math.sqrt((line.getStartX()-line.getEndX())*(line.getStartX()-line.getEndX())+((line.getStartY()-line.getEndY()))*((line.getStartY()-line.getEndY())));
        double length2=length1-2*100* Math.sqrt(2)*matchDeductDiv;
        double scale=length2/length1;
        matchGroup.setScaleX(scale);
        matchGroup.setScaleY(scale);
        getChildren().add(matchGroup);
        matchGroup.setVisible(false);
        line.setStrokeLineCap(StrokeLineCap.ROUND);
        line.setStrokeLineJoin(StrokeLineJoin.ROUND);
        stick.setStrokeLineCap(StrokeLineCap.ROUND);
        stick.setStrokeLineJoin(StrokeLineJoin.ROUND);
        //System.out.println(matchGroup.getScaleX());
    }
    public MatchStick(double midX,double midY, double length){
        isSkin=false;
        matchGroup=new Group();
        double startX=0;
        double startY=0;
        double endX=length*Math.sqrt(2);
        double endY=length*Math.sqrt(2);
        line=new Line(startX,startY,endX,endY);
        line.setStrokeWidth(10);
        head=new Circle(10);
        tail=new Circle(10);
        head.setCenterX(startX);
        head.setCenterY(startY);
        tail.setCenterX(endX);
        tail.setCenterY(endY);
        getChildren().addAll(line,head,tail);
        isSelected=false;
        head.setFill(Color.LIMEGREEN);
        tail.setFill(Color.LIMEGREEN);
        head.setVisible(false);
        tail.setVisible(false);
        line.setStroke(Constant.COLOR_SKYBLUE);
        fillColor=Constant.COLOR_SKYBLUE;
        headColor=Constant.COLOR_MATCHSTICK_HEAD;
        stickColor=Constant.COLOR_MATCHSTICK_LINE;


        stick=new Line(line.getStartX(),line.getStartY(),line.getEndX(),line.getEndY());
        stick.setStrokeWidth(line.getStrokeWidth());
        stick.setStroke(stickColor);
        match=new Circle(stick.getStrokeWidth()/1.3);
        match.setCenterX(stick.getStartX());
        match.setCenterY(stick.getStartY());
        match.setFill(headColor);
        matchGroup.getChildren().addAll(stick,match);
        double length1=Math.sqrt((line.getStartX()-line.getEndX())*(line.getStartX()-line.getEndX())+((line.getStartY()-line.getEndY()))*((line.getStartY()-line.getEndY())));
        double length2=length1-2*100* Math.sqrt(2)*matchDeductDiv;
        double scale=length2/length1;
        matchGroup.setScaleX(scale);
        matchGroup.setScaleY(scale);
        getChildren().add(matchGroup);
        matchGroup.setVisible(false);

        line.setStrokeLineCap(StrokeLineCap.ROUND);
        line.setStrokeLineJoin(StrokeLineJoin.ROUND);
        stick.setStrokeLineCap(StrokeLineCap.ROUND);
        stick.setStrokeLineJoin(StrokeLineJoin.ROUND);


        moveTo(new Point2D(midX,midY));
        //System.out.println(matchGroup.getScaleX());
    }

    public int getIndHeadX() {
        return indHeadX;
    }

    public void setIndHeadX(int indHeadX) {
        this.indHeadX = indHeadX;
    }

    public int getIndHeadY() {
        return indHeadY;
    }

    public void setIndHeadY(int indHeadY) {
        this.indHeadY = indHeadY;
    }

    public int getIndTailX() {
        return indTailX;
    }

    public void setIndTailX(int indTailX) {
        this.indTailX = indTailX;
    }

    public int getIndTailY() {
        return indTailY;
    }

    public void setIndTailY(int indTailY) {
        this.indTailY = indTailY;
    }

    public void setHead(double x, double y){
        line.setStartX(x);
        line.setStartY(y);
        head.setCenterX(x);
        head.setCenterY(y);

        match.setCenterX(x);
        match.setCenterY(y);
        stick.setStartX(x);
        stick.setStartY(y);

        double length1=Math.sqrt((line.getStartX()-line.getEndX())*(line.getStartX()-line.getEndX())+((line.getStartY()-line.getEndY()))*((line.getStartY()-line.getEndY())));
        double length2=length1-2*100* Math.sqrt(2)*matchDeductDiv;
        double scale=length2/length1;
        matchGroup.setScaleX(scale);
        matchGroup.setScaleY(scale);
    }
    public void setTail(double x, double y){
        line.setEndX(x);
        line.setEndY(y);
        tail.setCenterY(y);
        tail.setCenterX(x);

        stick.setEndX(x);
        stick.setEndY(y);

        double length1=Math.sqrt((line.getStartX()-line.getEndX())*(line.getStartX()-line.getEndX())+((line.getStartY()-line.getEndY()))*((line.getStartY()-line.getEndY())));
        double length2=length1-2*100* Math.sqrt(2)*matchDeductDiv;
        double scale=length2/length1;
        matchGroup.setScaleX(scale);
        matchGroup.setScaleY(scale);
    }
    public Circle getHead(){
        return head;
    }
    public Circle getTail(){
        return tail;
    }
    public Line getLine(){
        return line;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
        if(isSelected){
            line.setStroke(Color.LIMEGREEN);
            head.setVisible(true);
            tail.setVisible(true);
            line.setVisible(true);
            if(isSkin)matchGroup.setVisible(false);
        }else{
            line.setStroke(fillColor);
            head.setVisible(false);
            tail.setVisible(false);
            if(isSkin){
                matchGroup.setVisible(true);
                line.setVisible(false);
            }
        }
    }

    public Group getMatchGroup(){
        return matchGroup;
    }

    public void moveTo(Point2D point){
        Point2D mid=new Point2D((line.getStartX()+line.getEndX())/2.0,(line.getStartY()+line.getEndY())/2.0);
        line.setStartX(line.getStartX()+point.getX()-mid.getX());
        line.setStartY(line.getStartY()+point.getY()-mid.getY());
        line.setEndX(line.getEndX()+point.getX()-mid.getX());
        line.setEndY(line.getEndY()+point.getY()-mid.getY());
        head.setCenterX(head.getCenterX()+point.getX()-mid.getX());
        head.setCenterY(head.getCenterY()+point.getY()-mid.getY());
        tail.setCenterX(tail.getCenterX()+point.getX()-mid.getX());
        tail.setCenterY(tail.getCenterY()+point.getY()-mid.getY());

        match.setCenterX(head.getCenterX()+point.getX()-mid.getX());
        match.setCenterY(head.getCenterY()+point.getY()-mid.getY());
        stick.setStartX(line.getStartX()+point.getX()-mid.getX());
        stick.setStartY(line.getStartY()+point.getY()-mid.getY());
        stick.setEndX(line.getEndX()+point.getX()-mid.getX());
        stick.setEndY(line.getEndY()+point.getY()-mid.getY());

        double length1=Math.sqrt((line.getStartX()-line.getEndX())*(line.getStartX()-line.getEndX())+((line.getStartY()-line.getEndY()))*((line.getStartY()-line.getEndY())));
        double length2=length1-2*100* Math.sqrt(2)*matchDeductDiv;
        double scale=length2/length1;
        matchGroup.setScaleX(scale);
        matchGroup.setScaleY(scale);
    }

    public boolean isSkin() {
        return isSkin;
    }

    public void setWeight(double weight){
        line.setStrokeWidth(weight);
        head.setRadius(weight);
        tail.setRadius(weight);

        stick.setStrokeWidth(weight/matchGroup.getScaleX());
        match.setRadius(weight/(1.3*matchGroup.getScaleX()));
    }
    public Color getFillColor(){
        return fillColor;
    }
    public void setFillColor(Color color){
        line.setStroke(color);
        fillColor=color;
    }

    public Color getStickColor() {
        return stickColor;
    }

    public void setStickColor(Color stickColor) {
        this.stickColor = stickColor;
        stick.setStroke(stickColor);
    }

    public Color getHeadColor() {
        return headColor;
    }

    public void setHeadColor(Color headColor) {
        this.headColor = headColor;
        match.setFill(headColor);
    }

    public void useSkin(boolean b){
        isSkin=b;
        if(isSkin){
            matchGroup.setVisible(true);
            line.setVisible(false);
        }
        else{
            matchGroup.setVisible(false);
            line.setVisible(true);
        }
    }
}

