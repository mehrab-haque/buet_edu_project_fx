package editor;



import controller.*;
import javafx.css.Match;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import org.json.*;
import utility.Constant;

import java.io.IOException;

import static utility.Constant.*;

public class BoardView{
    private boolean dragStarted=false;
    private JSONObject data;
    private Group layout;
    private Group avatarGroup;
    private Group elementsGroup;
    private Parent removePanLayout;
    private double divSize;
    private double zoom;
    private AnchorPane anchorPane;
    private Point3D dragStartPoint;
    private Rectangle indicatorRect;

    private int mode;

    public BoardView(int mode) throws IOException {
        data=new JSONObject();
        this.mode=mode;
        load();
    }

    public BoardView(JSONObject data,int mode) throws IOException {
        this.data=data;
        this.mode=mode;
        load();
    }


    public JSONObject getData(){
        if(!data.getJSONObject("defaultCoin").has("isMust"))data.getJSONObject("defaultCoin").put("isMust",true);
        if(!data.getJSONObject("defaultMatchStick").has("isMust"))data.getJSONObject("defaultMatchStick").put("isMust",true);
        if(data.getJSONObject("defaultCoin").has("cantMove"))data.getJSONObject("defaultCoin").remove("cantMove");
        if(data.getJSONObject("defaultMatchStick").has("cantMove"))data.getJSONObject("defaultMatchStick").remove("cantMove");
        if(elementsGroup.getChildren().size()>0){
            JSONArray elements=new JSONArray();
            for(int i=0;i<elementsGroup.getChildren().size();i++){
                Node element=elementsGroup.getChildren().get(i);
                JSONObject object=new JSONObject();
                if(element.getTypeSelector().equals("Coin")){
                    Coin coin=(Coin)element;
                    object.put("type","coin");
                    object.put("innerColor",coin.getInnerColor().toString());
                    object.put("outerColor",coin.getOuterColor().toString());
                    if(coin.isMust())object.put("isMust",true);
                    if(coin.isCantMove())object.put("cantMove",true);
                    if(coin.isUseSkin())object.put("useSkin",true);
                    object.put("skin",coin.getSkin());
                    object.put("indX",coin.getIndX());
                    object.put("indY",coin.getIndY());
                    if(!coin.getText().isEmpty())object.put("text",coin.getText());
                }else if(element.getTypeSelector().equals("MatchStick")){
                    MatchStick matchStick=(MatchStick)element;
                    object.put("type","matchStick");
                    object.put("fillColor",matchStick.getFillColor().toString());
                    if(matchStick.isMust())object.put("isMust",true);
                    if(matchStick.isCantMove())object.put("cantMove",true);
                    if(matchStick.isSkin())object.put("useSkin",true);
                    object.put("indHeadX",matchStick.getIndHeadX());
                    object.put("indHeadY",matchStick.getIndHeadY());
                    object.put("indTailX",matchStick.getIndTailX());
                    object.put("indTailY",matchStick.getIndTailY());
                }
                elements.put(object);
            }
            data.put("elements",elements);
        }
        return data;
    }

    private void load() throws IOException {
        //Variables
        double graphDim=500,nDiv=50,graphBgDim=5000,panHeight=50,adjustX=-2252,adjustY=-2252,zoomWeight=100,lineOpacity=0.25;
        zoom=800;
        divSize=graphBgDim/nDiv;
        Color defaultBgColor= Color.WHITE,graphLineColor= Color.LIME,defaultIndicatorColor=Color.LIMEGREEN;
        elementsGroup=new Group();
        avatarGroup=new Group();
        anchorPane=new AnchorPane();
        anchorPane.setPrefWidth(graphDim);
        anchorPane.setPrefHeight(graphDim+panHeight);
        anchorPane.setVisible(false);
        anchorPane.setStyle("-fx-background-color: #0090ff44;");
        JSONObject defaultCoin=new JSONObject();
        JSONObject defaultMatchStick=new JSONObject();
        boolean isIndicator=true;
        

        defaultCoin.put("innerColor",Constant.COLOR_SKYBLUE_DARK.toString());
        defaultCoin.put("outerColor",Constant.COLOR_SKYBLUE.toString());
        defaultCoin.put("skin",7);
        defaultCoin.put("useSkin",true);
        defaultCoin.put("isMust",true);
        if(data.has("defaultCoin"))defaultCoin=data.getJSONObject("defaultCoin");
        else data.put("defaultCoin",defaultCoin);

        defaultMatchStick.put("fillColor",Constant.COLOR_SKYBLUE.toString());
        defaultMatchStick.put("isMust",true);
        defaultMatchStick.put("useSkin",true);
        if(data.has("defaultMatchStick"))defaultMatchStick=data.getJSONObject("defaultMatchStick");
        else data.put("defaultMatchStick",defaultMatchStick);

        //Graph
        layout=new Group();
        Group graphGroup=new Group();
        Group bgGroup=new Group();
        SubScene graphScene=new SubScene(graphGroup,graphDim,graphDim);
        Rectangle graphBgRect=new Rectangle(graphBgDim,graphBgDim);
        indicatorRect=new Rectangle();
        if(data.has("bgColor"))graphBgRect.setFill(Color.valueOf(data.getString("bgColor")));
        else{
            data.put("bgColor",defaultBgColor.toString());
            graphBgRect.setFill(defaultBgColor);
        }
        if(data.has("lineColor"))graphLineColor=Color.valueOf(data.getString("lineColor"));
        else data.put("lineColor",graphLineColor.toString());
        Camera camera=new PerspectiveCamera();
        graphScene.setCamera(camera);
        if(data.has("zoom"))zoom=data.getDouble("zoom");
        else data.put("zoom",zoom);
        if(data.has("transX"))adjustX=data.getDouble("transX");
        else data.put("transX",adjustX);
        if(data.has("transY"))adjustY=data.getDouble("transY");
        else data.put("transY",adjustY);
        if(data.has("lineOpacity"))lineOpacity=data.getDouble("lineOpacity");
        else data.put("lineOpacity",lineOpacity);
        if(data.has("isIndicator"))isIndicator=true;
        else if(!data.has("indicatorColor")){
            data.put("isIndicator",true);
        }else
            isIndicator=false;
        if(data.has("indicatorColor"))defaultIndicatorColor=Color.valueOf(data.getString("indicatorColor"));
        else data.put("indicatorColor",defaultIndicatorColor.toString());
        graphGroup.setTranslateX(adjustX);
        graphGroup.setTranslateY(adjustY);
        graphGroup.setTranslateZ(zoom);
        Group graphLineGroup=new Group();
        for(int i=0;i<=nDiv;i++){
            Line lineH=new Line(0,i*divSize,graphBgDim,i*divSize);
            Line lineV=new Line(i*divSize,0,i*divSize,graphBgDim);
            lineH.setOpacity(lineOpacity);
            lineV.setOpacity(lineOpacity);
            lineH.setStroke(graphLineColor);
            lineV.setStroke(graphLineColor);
            graphLineGroup.getChildren().addAll(lineH,lineV);
        }
        setLineWidth(graphLineGroup);
        graphGroup.setOnScroll(scrollEvent -> {
            if(scrollEvent.getDeltaY()<0 && graphGroup.getTranslateZ()<8200) {
                graphGroup.setTranslateZ(graphGroup.getTranslateZ() + zoomWeight);
                data.put("zoom",graphGroup.getTranslateZ());
                zoom=graphGroup.getTranslateZ();
                setLineWidth(graphLineGroup);
                for(int j=0;j<elementsGroup.getChildren().size();j++){
                    Node element=elementsGroup.getChildren().get(j);
                    if(element.getTypeSelector().equals("MatchStick"))
                        setMatchStickWidth((MatchStick)element);
                }
            }
            else if(scrollEvent.getDeltaY()>0  && graphGroup.getTranslateZ()>0) {
                graphGroup.setTranslateZ(graphGroup.getTranslateZ() - zoomWeight);
                data.put("zoom",graphGroup.getTranslateZ());
                zoom=graphGroup.getTranslateZ();
                setLineWidth(graphLineGroup);
                for(int j=0;j<elementsGroup.getChildren().size();j++){
                    Node element=elementsGroup.getChildren().get(j);
                    if(element.getTypeSelector().equals("MatchStick"))
                        setMatchStickWidth((MatchStick)element);
                }
            }
        });

        dragStartPoint=new Point3D(0,0,0);
        bgGroup.setOnMousePressed(event -> {
            if(event.getButton()==MouseButton.PRIMARY) {
                dragStartPoint = (event.getPickResult().getIntersectedPoint());
            }
        });
        bgGroup.setOnMouseDragged(event -> {
            if(event.getButton()==MouseButton.PRIMARY) {
                Point3D dragCurrentPoint = event.getPickResult().getIntersectedPoint();
                if (dragStartPoint.distance(dragCurrentPoint) <= 500) {
                    graphGroup.setTranslateX(graphGroup.getTranslateX() + dragCurrentPoint.getX() - dragStartPoint.getX());
                    graphGroup.setTranslateY(graphGroup.getTranslateY() + dragCurrentPoint.getY() - dragStartPoint.getY());
                }
                ////System.out.println(dragStartPoint+" "+dragCurrentPoint);
                data.put("transX", graphGroup.getTranslateX());
                data.put("transY", graphGroup.getTranslateY());
            }
        });

        if(mode==MODE_PROBLEM_SETTING) {
            indicatorRect.setOnMousePressed(event -> {
                for (int i = 0; i < elementsGroup.getChildren().size(); i++) {
                    Node element=elementsGroup.getChildren().get(i);
                    if(element.getTypeSelector().equals("coin")){
                        Coin coin=(Coin)element;
                        if(!coin.isMust())
                            coin.setVisible(false);
                    }else if(element.getTypeSelector().equals("MatchStick")){
                        MatchStick matchStick=(MatchStick)element;
                        if(!matchStick.isMust())
                            matchStick.setVisible(false);
                    }
                }
            });
            indicatorRect.setOnMouseReleased(event -> {
                for (int i = 0; i < elementsGroup.getChildren().size(); i++)
                    elementsGroup.getChildren().get(i).setVisible(true);
            });
        }

        indicatorRect.setStrokeLineJoin(StrokeLineJoin.ROUND);

        Rectangle borderRect=new Rectangle(graphBgDim,graphBgDim);
        borderRect.setStroke(Color.LIMEGREEN);
        borderRect.setStrokeWidth(50);
        borderRect.setFill(Color.valueOf("#00000000"));

        //Pan
        FXMLLoader panLoader = new FXMLLoader(getClass().getResource("/fxml/boardPan.fxml"));
        Parent panLayout = panLoader.load();
        BoardPan boardPan = panLoader.getController();
        FXMLLoader removePanLoader = new FXMLLoader(getClass().getResource("/fxml/removePan.fxml"));
        removePanLayout = removePanLoader.load();
        RemovePan removePan = removePanLoader.getController();
        Group panGroup=new Group();
        panGroup.getChildren().addAll(panLayout,removePanLayout);
        if(mode==MODE_PROBLEM_IMAGE)panGroup.setVisible(false);
        SubScene panScene=new SubScene(panGroup,500,50);
        panScene.setLayoutY(500);
        removePanLayout.setVisible(false);

        Coin coin1=new Coin(62.5,25,18);
        coin1.setInnerColor(Color.valueOf(data.getJSONObject("defaultCoin").getString("innerColor")));
        coin1.setOuterColor(Color.valueOf(data.getJSONObject("defaultCoin").getString("outerColor")));
        if(data.getJSONObject("defaultCoin").has("skin"))coin1.setSkin(data.getJSONObject("defaultCoin").getInt("skin"));
        if(data.getJSONObject("defaultCoin").has("useSkin"))coin1.useSkin(true);
        boardPan.group1.getChildren().set(1,coin1);

        MatchStick matchStick1=new MatchStick(90,20,30,30);
        matchStick1.setFillColor(Color.valueOf(data.getJSONObject("defaultMatchStick").getString("fillColor")));
        matchStick1.setLayoutX(125);
        if(data.getJSONObject("defaultMatchStick").has("useSkin")){
            matchStick1.setWeight(3.8);
            matchStick1.useSkin(true);
            matchStick1.setScaleX(2.5);
            matchStick1.setScaleY(2.5);
        }
        boardPan.group2.getChildren().set(1,matchStick1);

        if(data.has("elements")){
            JSONArray elements=data.getJSONArray("elements");
            for(int i=0;i<elements.length();i++){
                JSONObject element=elements.getJSONObject(i);
                if(element.getString("type").equals("coin")){
                    Coin coin=new Coin(element.getInt("indX")*divSize,element.getInt("indY")*divSize,divSize/2);
                    elementsGroup.getChildren().add(coin);
                    coin.setInnerColor(Color.valueOf(element.getString("innerColor")));
                    coin.setOuterColor(Color.valueOf(element.getString("outerColor")));
                    coin.setSkin(element.getInt("skin"));
                    if(element.has("isMust"))coin.setMust(true);
                    if(element.has("cantMove"))coin.setCantMove(true);
                    if(element.has("useSkin"))coin.useSkin(true);
                    if(element.has("text"))coin.setText(element.getString("text"));
                    coin.setIndX(element.getInt("indX"));
                    coin.setIndY(element.getInt("indY"));

                    coin.setOnMousePressed(event1 -> {
                        if (mode==MODE_PROBLEM_SETTING && event1.getButton() == MouseButton.SECONDARY) {
                            try {
                                anchorPane.setVisible(true);
                                FXMLLoader propLoader = new FXMLLoader(getClass().getResource("/fxml/coinProperties.fxml"));
                                Parent propLayout = propLoader.load();
                                CoinProperties coinProperties = propLoader.getController();

                                anchorPane.getChildren().clear();
                                anchorPane.getChildren().add(propLayout);
                                propLayout.setLayoutX(125);
                                propLayout.setLayoutY(141.5);

                                coinProperties.innerColor.setValue(coin.getInnerColor());
                                coinProperties.outerColor.setValue(coin.getOuterColor());
                                if (coin.isCantMove()) coinProperties.cantMove.setSelected(true);
                                if (coin.isUseSkin()) coinProperties.useSkin.setSelected(true);
                                if (coin.isMust()) coinProperties.isMust.setSelected(true);
                                coinProperties.skin.getSelectionModel().select(coin.getSkin());
                                coinProperties.text.setText(coin.getText());

                                coinProperties.saveBtn.setOnMouseClicked(event2 -> {
                                    if(event2.getButton()==MouseButton.PRIMARY) {
                                        Color innerColor = coinProperties.innerColor.getValue();
                                        Color outerColor = coinProperties.outerColor.getValue();
                                        coin.setInnerColor(innerColor);
                                        coin.setOuterColor(outerColor);
                                        if (coinProperties.cantMove.isSelected())
                                            coin.setCantMove(true);
                                        else
                                            coin.setCantMove(false);
                                        if (coinProperties.useSkin.isSelected()) {
                                            coin.setSkin(coinProperties.skin.getSelectionModel().getSelectedIndex());
                                            coin.useSkin(true);
                                        } else
                                            coin.useSkin(false);
                                        if (coinProperties.isMust.isSelected())
                                            coin.setMust(true);
                                        else coin.setMust(false);
                                        if (!coinProperties.text.getText().isEmpty()) {
                                            coin.setText(coinProperties.text.getText());
                                        } else {
                                            coin.setText("");
                                        }
                                        anchorPane.setVisible(false);
                                        drawIndicator();
                                    }
                                    ////System.out.println(data);
                                });

                                coinProperties.cancelBtn.setOnMouseClicked(event2 -> {
                                    if(event2.getButton()==MouseButton.PRIMARY) {
                                        anchorPane.setVisible(false);
                                    }
                                });
                            } catch (Exception e) {

                            }
                        }
                        if((mode==MODE_PROBLEM_SETTING || (mode==MODE_PROBLEM_SOLVING && !coin.isCantMove())) && event1.getButton()==MouseButton.PRIMARY) {
                            indicatorRect.setVisible(false);
                            removePanLayout.setVisible(true);
                            coin.setSelected(true);
                        }
                    });

                    coin.setOnMouseDragged(event1 -> {
                        if((mode==MODE_PROBLEM_SETTING || (mode==MODE_PROBLEM_SOLVING && !coin.isCantMove())) && event1.getButton()==MouseButton.PRIMARY) {
                            indicatorRect.setVisible(false);
                            removePanLayout.setVisible(true);
                            coin.setSelected(true);
                            coin.setCenterX(event1.getPickResult().getIntersectedPoint().getX());
                            coin.setCenterY(event1.getPickResult().getIntersectedPoint().getY());
                        }
                    });

                    coin.setOnMouseReleased(event1 -> {
                        if((mode==MODE_PROBLEM_SETTING || (mode==MODE_PROBLEM_SOLVING && !coin.isCantMove())) && event1.getButton()==MouseButton.PRIMARY) {
                            Point2D point1 = new Point2D(event1.getPickResult().getIntersectedPoint().getX(), event1.getPickResult().getIntersectedPoint().getY());
                            if (!removePanLayout.contains(point1)) {
                                int indX1 = (int) (point1.getX() / divSize);
                                int indY1 = (int) (point1.getY() / divSize);
                                if (point1.getX() % divSize > divSize / 2) indX1++;
                                if (point1.getY() % divSize > divSize / 2) indY1++;
                                if (indX1 >= 1 && indX1 <= 49 && indY1 >= 1 && indY1 <= 49) {
                                    coin.setIndX(indX1);
                                    coin.setIndY(indY1);
                                    coin.setCenterX(indX1 * divSize);
                                    coin.setCenterY(indY1 * divSize);
                                    coin.setSelected(false);
                                }
                            } else
                                elementsGroup.getChildren().remove(coin);
                            removePanLayout.setVisible(false);
                            drawIndicator();
                        }
                    });
                }else if(element.getString("type").contentEquals("matchStick")){
                    MatchStick elementStick=new MatchStick(element.getInt("indHeadX")*divSize,element.getInt("indHeadY")*divSize,element.getInt("indTailX")*divSize,element.getInt("indTailY")*divSize);
                    elementsGroup.getChildren().add(elementStick);
                    setMatchStickWidth(elementStick);
                    elementStick.setFillColor(Color.valueOf(element.getString("fillColor")));
                    if(element.has("isMust"))elementStick.setMust(true);
                    if(element.has("cantMove"))elementStick.setCantMove(true);
                    if(element.has("useSkin"))elementStick.useSkin(true);
                    elementStick.setIndHeadX(element.getInt("indHeadX"));
                    elementStick.setIndHeadY(element.getInt("indHeadY"));
                    elementStick.setIndTailX(element.getInt("indTailX"));
                    elementStick.setIndTailY(element.getInt("indTailY"));

                    elementStick.setOnMouseClicked(event1 -> {
                        if((mode==MODE_PROBLEM_SETTING || (mode==MODE_PROBLEM_SOLVING && !elementStick.isCantMove())) && event1.getButton()==MouseButton.PRIMARY) {
                            if (!dragStarted) {
                                if (elementStick.isSelected()) elementStick.setSelected(false);
                                else elementStick.setSelected(true);
                            }
                        }
                        if(mode==MODE_PROBLEM_SETTING && event1.getButton()==MouseButton.SECONDARY){
                            try{
                                anchorPane.setVisible(true);
                                FXMLLoader propLoader = new FXMLLoader(getClass().getResource("/fxml/stickProperties.fxml"));
                                Parent propLayout = propLoader.load();
                                StickProperties stickProperties = propLoader.getController();

                                anchorPane.getChildren().clear();
                                anchorPane.getChildren().add(propLayout);
                                propLayout.setLayoutX(125);
                                propLayout.setLayoutY(180);

                                stickProperties.fillColor.setValue(elementStick.getFillColor());
                                if(elementStick.isCantMove())stickProperties.cantMove.setSelected(true);
                                if(elementStick.isSkin())stickProperties.useSkin.setSelected(true);
                                if(elementStick.isMust())stickProperties.isMust.setSelected(true);

                                if(mode==MODE_PROBLEM_SOLVING)stickProperties.cantMove.setDisable(true);

                                stickProperties.saveBtn.setOnMouseClicked(event2 -> {
                                    Color fillColor=stickProperties.fillColor.getValue();
                                    elementStick.setFillColor(fillColor);
                                    if(stickProperties.cantMove.isSelected())
                                        elementStick.setCantMove(true);
                                    else
                                        elementStick.setCantMove(false);
                                    if(stickProperties.useSkin.isSelected()) {
                                        elementStick.useSkin(true);
                                    }
                                    else{
                                        elementStick.useSkin(false);
                                    }
                                    if(stickProperties.isMust.isSelected())
                                        elementStick.setMust(true);
                                    else
                                        elementStick.setMust(false);
                                    anchorPane.setVisible(false);
                                    drawIndicator();
                                    ////System.out.println(data);
                                });

                                stickProperties.cancelBtn.setOnMouseClicked(event2 -> {
                                    anchorPane.setVisible(false);
                                });
                            }catch (Exception e){

                            }
                        }
                    });

                    elementStick.getLine().setOnMouseDragged(event1 -> {
                        if((mode==MODE_PROBLEM_SETTING || (mode==MODE_PROBLEM_SOLVING && !elementStick.isCantMove())) && event1.getButton()==MouseButton.PRIMARY) {
                            indicatorRect.setVisible(false);
                            removePanLayout.setVisible(true);
                            dragStarted = true;
                            elementStick.setSelected(true);
                            Point2D point1 = new Point2D(event1.getPickResult().getIntersectedPoint().getX(), event1.getPickResult().getIntersectedPoint().getY());
                            elementStick.moveTo(point1);
                        }
                    });
                    elementStick.getMatchGroup().setOnMouseDragged(event1 -> {
                        if((mode==MODE_PROBLEM_SETTING || (mode==MODE_PROBLEM_SOLVING && !elementStick.isCantMove())) && event1.getButton()==MouseButton.PRIMARY) {
                            indicatorRect.setVisible(false);
                            dragStarted = true;
                            elementStick.setSelected(true);
                            Point2D point1 = new Point2D(event1.getPickResult().getIntersectedPoint().getX(), event1.getPickResult().getIntersectedPoint().getY());
                            elementStick.moveTo(point1);
                            removePanLayout.setVisible(true);
                        }
                    });

                    elementStick.setOnMouseReleased(event1 -> {
                        if((mode==MODE_PROBLEM_SETTING || (mode==MODE_PROBLEM_SOLVING && !elementStick.isCantMove())) && event1.getButton()==MouseButton.PRIMARY) {
                            if (dragStarted) {
                                Point2D point2D = new Point2D(event1.getPickResult().getIntersectedPoint().getX(), event1.getPickResult().getIntersectedPoint().getY());
                                if (!removePanLayout.contains(point2D)) {
                                    int indHeadX1 = (int) (elementStick.getHead().getCenterX() / divSize);
                                    int indHeadY1 = (int) (elementStick.getHead().getCenterY() / divSize);
                                    if (elementStick.getHead().getCenterX() % divSize > divSize / 2) indHeadX1++;
                                    if (elementStick.getHead().getCenterY() % divSize > divSize / 2) indHeadY1++;
                                    int indTailX1 = (int) (elementStick.getTail().getCenterX() / divSize);
                                    int indTailY1 = (int) (elementStick.getTail().getCenterY() / divSize);
                                    if (elementStick.getTail().getCenterX() % divSize > divSize / 2) indTailX1++;
                                    if (elementStick.getTail().getCenterY() % divSize > divSize / 2) indTailY1++;
                                    if (indHeadX1 >= 1 && indHeadX1 <= 49 && indHeadY1 >= 1 && indHeadY1 <= 49 && indTailX1 >= 1 && indTailX1 <= 49 && indTailY1 >= 1 && indTailY1 <= 49) {
                                        elementStick.setIndHeadX(indHeadX1);
                                        elementStick.setIndHeadY(indHeadY1);
                                        elementStick.setIndTailX(indTailX1);
                                        elementStick.setIndTailY(indTailY1);
                                        elementStick.setHead(indHeadX1 * divSize, indHeadY1 * divSize);
                                        elementStick.setTail(indTailX1 * divSize, indTailY1 * divSize);
                                    } else {
                                        elementsGroup.getChildren().remove(elementStick);
                                    }
                                    dragStarted = false;
                                } else {
                                    elementsGroup.getChildren().remove(elementStick);
                                }
                            }
                            removePanLayout.setVisible(false);
                            drawIndicator();
                        }
                    });
                    elementStick.getHead().setOnMouseDragged(event1 -> {
                        if((mode==MODE_PROBLEM_SETTING || (mode==MODE_PROBLEM_SOLVING && !elementStick.isCantMove())) && event1.getButton()==MouseButton.PRIMARY) {
                            indicatorRect.setVisible(false);
                            removePanLayout.setVisible(true);
                            elementStick.setHead(event1.getPickResult().getIntersectedPoint().getX(), event1.getPickResult().getIntersectedPoint().getY());
                        }
                    });
                    elementStick.getHead().setOnMouseReleased(event1 -> {
                        if((mode==MODE_PROBLEM_SETTING || (mode==MODE_PROBLEM_SOLVING && !elementStick.isCantMove())) && event1.getButton()==MouseButton.PRIMARY) {
                            Point2D point2D = new Point2D(event1.getPickResult().getIntersectedPoint().getX(), event1.getPickResult().getIntersectedPoint().getY());
                            if (!removePanLayout.contains(point2D)) {
                                int indHeadX1 = (int) (elementStick.getHead().getCenterX() / divSize);
                                int indHeadY1 = (int) (elementStick.getHead().getCenterY() / divSize);
                                if (elementStick.getHead().getCenterX() % divSize > divSize / 2) indHeadX1++;
                                if (elementStick.getHead().getCenterY() % divSize > divSize / 2) indHeadY1++;
                                if (indHeadX1 >= 1 && indHeadX1 <= 49 && indHeadY1 >= 1 && indHeadY1 <= 49) {
                                    elementStick.setIndHeadX(indHeadX1);
                                    elementStick.setIndHeadY(indHeadY1);
                                    elementStick.setHead(indHeadX1 * divSize, indHeadY1 * divSize);
                                } else
                                    elementsGroup.getChildren().remove(elementStick);
                            } else
                                elementsGroup.getChildren().remove(elementStick);
                            removePanLayout.setVisible(false);
                            drawIndicator();
                        }
                    });
                    elementStick.getTail().setOnMouseDragged(event1 -> {
                        if((mode==MODE_PROBLEM_SETTING || (mode==MODE_PROBLEM_SOLVING && !elementStick.isCantMove())) && event1.getButton()==MouseButton.PRIMARY) {
                            indicatorRect.setVisible(false);
                            removePanLayout.setVisible(true);
                            elementStick.setTail(event1.getPickResult().getIntersectedPoint().getX(), event1.getPickResult().getIntersectedPoint().getY());
                        }
                    });
                    elementStick.getTail().setOnMouseReleased(event1 -> {
                        if((mode==MODE_PROBLEM_SETTING || (mode==MODE_PROBLEM_SOLVING && !elementStick.isCantMove())) && event1.getButton()==MouseButton.PRIMARY) {
                            Point2D point2D = new Point2D(event1.getPickResult().getIntersectedPoint().getX(), event1.getPickResult().getIntersectedPoint().getY());
                            if (!removePanLayout.contains(point2D)) {
                                int indTailX1 = (int) (elementStick.getTail().getCenterX() / divSize);
                                int indTailY1 = (int) (elementStick.getTail().getCenterY() / divSize);
                                if (elementStick.getTail().getCenterX() % divSize > divSize / 2) indTailX1++;
                                if (elementStick.getTail().getCenterY() % divSize > divSize / 2) indTailY1++;
                                if (indTailX1 >= 1 && indTailX1 <= 49 && indTailY1 >= 1 && indTailY1 <= 49) {
                                    elementStick.setIndTailX(indTailX1);
                                    elementStick.setIndTailY(indTailY1);
                                    elementStick.setTail(indTailX1 * divSize, indTailY1 * divSize);
                                } else
                                    elementsGroup.getChildren().remove(elementStick);
                            } else
                                elementsGroup.getChildren().remove(elementStick);
                            removePanLayout.setVisible(false);
                            drawIndicator();
                        }
                    });
                }
            }
            drawIndicator();
        }

        boardPan.group1.setOnMousePressed(event -> {
            if(event.getButton()==MouseButton.PRIMARY) {
                removePanLayout.setVisible(true);
                Circle circle = new Circle(event.getPickResult().getIntersectedPoint().getX(), event.getPickResult().getIntersectedPoint().getY(), divSize / 2);
                circle.setFill(Color.LIMEGREEN);
                avatarGroup.getChildren().clear();
                avatarGroup.getChildren().add(circle);
            }
        });
        boardPan.group1.setOnMouseDragged(event -> {
            if(event.getButton()==MouseButton.PRIMARY) {
                indicatorRect.setVisible(false);
                Circle circle = (Circle) avatarGroup.getChildren().get(0);
                circle.setCenterX(event.getPickResult().getIntersectedPoint().getX());
                circle.setCenterY(event.getPickResult().getIntersectedPoint().getY());
            }
        });
        boardPan.group1.setOnMouseReleased(event -> {
            if(event.getButton()==MouseButton.PRIMARY) {
                Point2D point = new Point2D(event.getPickResult().getIntersectedPoint().getX(), event.getPickResult().getIntersectedPoint().getY());
                if (!removePanLayout.contains(point)) {
                    int indX = (int) (point.getX() / divSize);
                    int indY = (int) (point.getY() / divSize);
                    if (point.getX() % divSize > divSize / 2) indX++;
                    if (point.getY() % divSize > divSize / 2) indY++;
                    if (indX >= 1 && indX <= 49 && indY >= 1 && indY <= 49) {
                        Coin coin = new Coin(indX * divSize, indY * divSize, divSize / 2);
                        coin.setInnerColor(Color.valueOf(data.getJSONObject("defaultCoin").getString("innerColor")));
                        coin.setOuterColor(Color.valueOf(data.getJSONObject("defaultCoin").getString("outerColor")));
                        if (data.getJSONObject("defaultCoin").has("cantMove")) coin.setCantMove(true);
                        if (data.getJSONObject("defaultCoin").has("useSkin")) {
                            coin.setSkin(data.getJSONObject("defaultCoin").getInt("skin"));
                            coin.useSkin(true);
                        }
                        if (data.getJSONObject("defaultCoin").has("isMust")) coin.setMust(true);
                        coin.setIndX(indX);
                        coin.setIndY(indY);
                        elementsGroup.getChildren().add(coin);

                        coin.setOnMousePressed(event1 -> {
                            if (mode == Constant.MODE_PROBLEM_SETTING && event1.getButton() == MouseButton.SECONDARY) {
                                try {
                                    anchorPane.setVisible(true);
                                    FXMLLoader propLoader = new FXMLLoader(getClass().getResource("/fxml/coinProperties.fxml"));
                                    Parent propLayout = propLoader.load();
                                    CoinProperties coinProperties = propLoader.getController();

                                    anchorPane.getChildren().clear();
                                    anchorPane.getChildren().add(propLayout);
                                    propLayout.setLayoutX(125);
                                    propLayout.setLayoutY(141.5);

                                    coinProperties.innerColor.setValue(coin.getInnerColor());
                                    coinProperties.outerColor.setValue(coin.getOuterColor());
                                    if (coin.isCantMove()) coinProperties.cantMove.setSelected(true);
                                    if (coin.isUseSkin()) coinProperties.useSkin.setSelected(true);
                                    if (coin.isMust()) coinProperties.isMust.setSelected(true);
                                    coinProperties.skin.getSelectionModel().select(coin.getSkin());
                                    coinProperties.text.setText(coin.getText());

                                    coinProperties.saveBtn.setOnMouseClicked(event2 -> {
                                        if(event2.getButton()==MouseButton.PRIMARY) {
                                            Color innerColor = coinProperties.innerColor.getValue();
                                            Color outerColor = coinProperties.outerColor.getValue();
                                            coin.setInnerColor(innerColor);
                                            coin.setOuterColor(outerColor);
                                            if (coinProperties.cantMove.isSelected())
                                                coin.setCantMove(true);
                                            else
                                                coin.setCantMove(false);
                                            if (coinProperties.useSkin.isSelected()) {
                                                coin.setSkin(coinProperties.skin.getSelectionModel().getSelectedIndex());
                                                coin.useSkin(true);
                                            } else
                                                coin.useSkin(false);
                                            if (coinProperties.isMust.isSelected())
                                                coin.setMust(true);
                                            else coin.setMust(false);
                                            if (!coinProperties.text.getText().isEmpty()) {
                                                coin.setText(coinProperties.text.getText());
                                            } else {
                                                coin.setText("");
                                            }
                                            anchorPane.setVisible(false);
                                            drawIndicator();
                                        }
                                        ////System.out.println(data);
                                    });

                                    coinProperties.cancelBtn.setOnMouseClicked(event2 -> {
                                        if(event2.getButton()==MouseButton.PRIMARY) {
                                            anchorPane.setVisible(false);
                                        }
                                    });
                                } catch (Exception e) {

                                }
                            }
                            if((mode==MODE_PROBLEM_SETTING || (mode==MODE_PROBLEM_SOLVING && !coin.isCantMove())) && event1.getButton()==MouseButton.PRIMARY) {
                                indicatorRect.setVisible(false);
                                removePanLayout.setVisible(true);
                                coin.setSelected(true);
                            }
                        });

                        coin.setOnMouseDragged(event1 -> {
                            if((mode==MODE_PROBLEM_SETTING || (mode==MODE_PROBLEM_SOLVING && !coin.isCantMove())) && event1.getButton()==MouseButton.PRIMARY) {
                                indicatorRect.setVisible(false);
                                removePanLayout.setVisible(true);
                                coin.setSelected(true);
                                coin.setCenterX(event1.getPickResult().getIntersectedPoint().getX());
                                coin.setCenterY(event1.getPickResult().getIntersectedPoint().getY());
                            }
                        });

                        coin.setOnMouseReleased(event1 -> {
                            if((mode==MODE_PROBLEM_SETTING || (mode==MODE_PROBLEM_SOLVING && !coin.isCantMove())) && event1.getButton()==MouseButton.PRIMARY) {
                                Point2D point1 = new Point2D(event1.getPickResult().getIntersectedPoint().getX(), event1.getPickResult().getIntersectedPoint().getY());
                                if (!removePanLayout.contains(point1) && graphGroup.contains(point1)) {
                                    int indX1 = (int) (point1.getX() / divSize);
                                    int indY1 = (int) (point1.getY() / divSize);
                                    if (point1.getX() % divSize > divSize / 2) indX1++;
                                    if (point1.getY() % divSize > divSize / 2) indY1++;
                                    if (indX1 >= 1 && indX1 <= 49 && indY1 >= 1 && indY1 <= 49) {
                                        coin.setIndX(indX1);
                                        coin.setIndY(indY1);
                                        coin.setCenterX(indX1 * divSize);
                                        coin.setCenterY(indY1 * divSize);
                                        coin.setSelected(false);
                                    }
                                } else
                                    elementsGroup.getChildren().remove(coin);
                                removePanLayout.setVisible(false);
                                drawIndicator();
                            }
                        });
                    }
                }
                drawIndicator();
                removePanLayout.setVisible(false);
                avatarGroup.getChildren().clear();
            }
        });

        boardPan.group2.setOnMousePressed(event -> {
            if(event.getButton()==MouseButton.PRIMARY) {
                removePanLayout.setVisible(true);
                Point2D point = new Point2D(event.getPickResult().getIntersectedPoint().getX(), event.getPickResult().getIntersectedPoint().getY());
                double length = 80;
                MatchStick matchStick = new MatchStick(point.getX(), point.getY(), length);
                matchStick.setSelected(true);
                avatarGroup.getChildren().add(matchStick);
                setMatchStickWidth(matchStick);
            }
        });

        boardPan.group2.setOnMouseDragged(event -> {
            if(event.getButton()==MouseButton.PRIMARY) {
                indicatorRect.setVisible(false);
                Point2D point = new Point2D(event.getPickResult().getIntersectedPoint().getX(), event.getPickResult().getIntersectedPoint().getY());
                MatchStick matchStick = (MatchStick) avatarGroup.getChildren().get(0);
                matchStick.moveTo(point);
            }
        });

        boardPan.group2.setOnMouseReleased(event -> {
            if(event.getButton()==MouseButton.PRIMARY) {
                Point2D point = new Point2D(event.getPickResult().getIntersectedPoint().getX(), event.getPickResult().getIntersectedPoint().getY());
                MatchStick matchStick = (MatchStick) avatarGroup.getChildren().get(0);
                if (!removePanLayout.contains(point)) {
                    int indHeadX = (int) (matchStick.getHead().getCenterX() / divSize);
                    int indHeadY = (int) (matchStick.getHead().getCenterY() / divSize);
                    if (matchStick.getHead().getCenterX() % divSize > divSize / 2) indHeadX++;
                    if (matchStick.getHead().getCenterY() % divSize > divSize / 2) indHeadY++;
                    int indTailX = (int) (matchStick.getTail().getCenterX() / divSize);
                    int indTailY = (int) (matchStick.getTail().getCenterY() / divSize);
                    if (matchStick.getTail().getCenterX() % divSize > divSize / 2) indTailX++;
                    if (matchStick.getTail().getCenterY() % divSize > divSize / 2) indTailY++;
                    if (indHeadX >= 1 && indHeadX <= 49 && indHeadY >= 1 && indHeadY <= 49 && indTailX >= 1 && indTailX <= 49 && indTailY >= 1 && indTailY <= 49) {
                        MatchStick elementStick = new MatchStick(indHeadX * divSize, indHeadY * divSize, indTailX * divSize, indTailY * divSize);
                        if (data.getJSONObject("defaultMatchStick").has("fillColor"))
                            elementStick.setFillColor(Color.valueOf(data.getJSONObject("defaultMatchStick").getString("fillColor")));
                        if (data.getJSONObject("defaultMatchStick").has("cantMove")) elementStick.setCantMove(true);
                        if (data.getJSONObject("defaultMatchStick").has("useSkin")) elementStick.useSkin(true);
                        if (data.getJSONObject("defaultMatchStick").has("isMust")) elementStick.setMust(true);
                        elementStick.setIndHeadX(indHeadX);
                        elementStick.setIndHeadY(indHeadY);
                        elementStick.setIndTailX(indTailX);
                        elementStick.setIndTailY(indTailY);
                        setMatchStickWidth(elementStick);
                        elementStick.setSelected(false);
                        elementsGroup.getChildren().add(elementStick);

                        elementStick.setOnMouseClicked(event1 -> {
                            if((mode==MODE_PROBLEM_SETTING || (mode==MODE_PROBLEM_SOLVING && !elementStick.isCantMove())) && event1.getButton()==MouseButton.PRIMARY) {
                                if (!dragStarted) {
                                    if (elementStick.isSelected()) elementStick.setSelected(false);
                                    else elementStick.setSelected(true);
                                }
                            }
                            if(mode==MODE_PROBLEM_SETTING && event1.getButton()==MouseButton.SECONDARY){
                                try{
                                    anchorPane.setVisible(true);
                                    FXMLLoader propLoader = new FXMLLoader(getClass().getResource("/fxml/stickProperties.fxml"));
                                    Parent propLayout = propLoader.load();
                                    StickProperties stickProperties = propLoader.getController();

                                    anchorPane.getChildren().clear();
                                    anchorPane.getChildren().add(propLayout);
                                    propLayout.setLayoutX(125);
                                    propLayout.setLayoutY(180);

                                    stickProperties.fillColor.setValue(elementStick.getFillColor());
                                    if(elementStick.isCantMove())stickProperties.cantMove.setSelected(true);
                                    if(elementStick.isSkin())stickProperties.useSkin.setSelected(true);
                                    if(elementStick.isMust())stickProperties.isMust.setSelected(true);

                                    if(mode==MODE_PROBLEM_SOLVING)stickProperties.cantMove.setDisable(true);

                                    stickProperties.saveBtn.setOnMouseClicked(event2 -> {
                                        Color fillColor=stickProperties.fillColor.getValue();
                                        elementStick.setFillColor(fillColor);
                                        if(stickProperties.cantMove.isSelected())
                                            elementStick.setCantMove(true);
                                        else
                                            elementStick.setCantMove(false);
                                        if(stickProperties.useSkin.isSelected()) {
                                            elementStick.useSkin(true);
                                        }
                                        else{
                                            elementStick.useSkin(false);
                                        }
                                        if(stickProperties.isMust.isSelected())
                                            elementStick.setMust(true);
                                        else
                                            elementStick.setMust(false);
                                        anchorPane.setVisible(false);
                                        drawIndicator();
                                        ////System.out.println(data);
                                    });

                                    stickProperties.cancelBtn.setOnMouseClicked(event2 -> {
                                        anchorPane.setVisible(false);
                                    });
                                }catch (Exception e){

                                }
                            }
                        });

                        elementStick.getLine().setOnMouseDragged(event1 -> {
                            if((mode==MODE_PROBLEM_SETTING || (mode==MODE_PROBLEM_SOLVING && !elementStick.isCantMove())) && event1.getButton()==MouseButton.PRIMARY) {
                                indicatorRect.setVisible(false);
                                removePanLayout.setVisible(true);
                                dragStarted = true;
                                elementStick.setSelected(true);
                                Point2D point1 = new Point2D(event1.getPickResult().getIntersectedPoint().getX(), event1.getPickResult().getIntersectedPoint().getY());
                                elementStick.moveTo(point1);
                            }
                        });
                        elementStick.getMatchGroup().setOnMouseDragged(event1 -> {
                            if((mode==MODE_PROBLEM_SETTING || (mode==MODE_PROBLEM_SOLVING && !elementStick.isCantMove())) && event1.getButton()==MouseButton.PRIMARY) {
                                indicatorRect.setVisible(false);
                                dragStarted = true;
                                elementStick.setSelected(true);
                                Point2D point1 = new Point2D(event1.getPickResult().getIntersectedPoint().getX(), event1.getPickResult().getIntersectedPoint().getY());
                                elementStick.moveTo(point1);
                                removePanLayout.setVisible(true);
                            }
                        });

                        elementStick.setOnMouseReleased(event1 -> {
                            if((mode==MODE_PROBLEM_SETTING || (mode==MODE_PROBLEM_SOLVING && !elementStick.isCantMove())) && event1.getButton()==MouseButton.PRIMARY) {
                                if (dragStarted) {
                                    Point2D point2D = new Point2D(event1.getPickResult().getIntersectedPoint().getX(), event1.getPickResult().getIntersectedPoint().getY());
                                    if (!removePanLayout.contains(point2D)) {
                                        int indHeadX1 = (int) (elementStick.getHead().getCenterX() / divSize);
                                        int indHeadY1 = (int) (elementStick.getHead().getCenterY() / divSize);
                                        if (elementStick.getHead().getCenterX() % divSize > divSize / 2) indHeadX1++;
                                        if (elementStick.getHead().getCenterY() % divSize > divSize / 2) indHeadY1++;
                                        int indTailX1 = (int) (elementStick.getTail().getCenterX() / divSize);
                                        int indTailY1 = (int) (elementStick.getTail().getCenterY() / divSize);
                                        if (elementStick.getTail().getCenterX() % divSize > divSize / 2) indTailX1++;
                                        if (elementStick.getTail().getCenterY() % divSize > divSize / 2) indTailY1++;
                                        if (indHeadX1 >= 1 && indHeadX1 <= 49 && indHeadY1 >= 1 && indHeadY1 <= 49 && indTailX1 >= 1 && indTailX1 <= 49 && indTailY1 >= 1 && indTailY1 <= 49) {
                                            elementStick.setIndHeadX(indHeadX1);
                                            elementStick.setIndHeadY(indHeadY1);
                                            elementStick.setIndTailX(indTailX1);
                                            elementStick.setIndTailY(indTailY1);
                                            elementStick.setHead(indHeadX1 * divSize, indHeadY1 * divSize);
                                            elementStick.setTail(indTailX1 * divSize, indTailY1 * divSize);
                                        } else {
                                            elementsGroup.getChildren().remove(elementStick);
                                        }
                                        dragStarted = false;
                                    } else {
                                        elementsGroup.getChildren().remove(elementStick);
                                    }
                                }
                                removePanLayout.setVisible(false);
                                drawIndicator();
                            }
                        });
                        elementStick.getHead().setOnMouseDragged(event1 -> {
                            if((mode==MODE_PROBLEM_SETTING || (mode==MODE_PROBLEM_SOLVING && !elementStick.isCantMove())) && event1.getButton()==MouseButton.PRIMARY) {
                                indicatorRect.setVisible(false);
                                removePanLayout.setVisible(true);
                                elementStick.setHead(event1.getPickResult().getIntersectedPoint().getX(), event1.getPickResult().getIntersectedPoint().getY());
                            }
                        });
                        elementStick.getHead().setOnMouseReleased(event1 -> {
                            if((mode==MODE_PROBLEM_SETTING || (mode==MODE_PROBLEM_SOLVING && !elementStick.isCantMove())) && event1.getButton()==MouseButton.PRIMARY) {
                                Point2D point2D = new Point2D(event1.getPickResult().getIntersectedPoint().getX(), event1.getPickResult().getIntersectedPoint().getY());
                                if (!removePanLayout.contains(point2D)) {
                                    int indHeadX1 = (int) (elementStick.getHead().getCenterX() / divSize);
                                    int indHeadY1 = (int) (elementStick.getHead().getCenterY() / divSize);
                                    if (elementStick.getHead().getCenterX() % divSize > divSize / 2) indHeadX1++;
                                    if (elementStick.getHead().getCenterY() % divSize > divSize / 2) indHeadY1++;
                                    if (indHeadX1 >= 1 && indHeadX1 <= 49 && indHeadY1 >= 1 && indHeadY1 <= 49) {
                                        elementStick.setIndHeadX(indHeadX1);
                                        elementStick.setIndHeadY(indHeadY1);
                                        elementStick.setHead(indHeadX1 * divSize, indHeadY1 * divSize);
                                    } else
                                        elementsGroup.getChildren().remove(elementStick);
                                } else
                                    elementsGroup.getChildren().remove(elementStick);
                                removePanLayout.setVisible(false);
                                drawIndicator();
                            }
                        });
                        elementStick.getTail().setOnMouseDragged(event1 -> {
                            if((mode==MODE_PROBLEM_SETTING || (mode==MODE_PROBLEM_SOLVING && !elementStick.isCantMove())) && event1.getButton()==MouseButton.PRIMARY) {
                                indicatorRect.setVisible(false);
                                removePanLayout.setVisible(true);
                                elementStick.setTail(event1.getPickResult().getIntersectedPoint().getX(), event1.getPickResult().getIntersectedPoint().getY());
                            }
                        });
                        elementStick.getTail().setOnMouseReleased(event1 -> {
                            if((mode==MODE_PROBLEM_SETTING || (mode==MODE_PROBLEM_SOLVING && !elementStick.isCantMove())) && event1.getButton()==MouseButton.PRIMARY) {
                                Point2D point2D = new Point2D(event1.getPickResult().getIntersectedPoint().getX(), event1.getPickResult().getIntersectedPoint().getY());
                                if (!removePanLayout.contains(point2D)) {
                                    int indTailX1 = (int) (elementStick.getTail().getCenterX() / divSize);
                                    int indTailY1 = (int) (elementStick.getTail().getCenterY() / divSize);
                                    if (elementStick.getTail().getCenterX() % divSize > divSize / 2) indTailX1++;
                                    if (elementStick.getTail().getCenterY() % divSize > divSize / 2) indTailY1++;
                                    if (indTailX1 >= 1 && indTailX1 <= 49 && indTailY1 >= 1 && indTailY1 <= 49) {
                                        elementStick.setIndTailX(indTailX1);
                                        elementStick.setIndTailY(indTailY1);
                                        elementStick.setTail(indTailX1 * divSize, indTailY1 * divSize);
                                    } else
                                        elementsGroup.getChildren().remove(elementStick);
                                } else
                                    elementsGroup.getChildren().remove(elementStick);
                                removePanLayout.setVisible(false);
                                drawIndicator();
                            }
                        });
                    }
                }
                removePanLayout.setVisible(false);
                avatarGroup.getChildren().clear();
                drawIndicator();
            }
        });

        boardPan.group4.setOnMouseClicked(event -> {
            try{
                anchorPane.setVisible(true);
                FXMLLoader propLoader = new FXMLLoader(getClass().getResource("/fxml/boardProperties.fxml"));
                Parent propLayout = propLoader.load();
                BoardProperties boardProperties = propLoader.getController();
                anchorPane.getChildren().clear();
                anchorPane.getChildren().add(propLayout);
                propLayout.setTranslateX(125);
                propLayout.setTranslateY(92);

                if(mode!=MODE_PROBLEM_SETTING){
                    boardProperties.defaultCoin.setDisable(true);
                    boardProperties.defaultMatchStick.setDisable(true);
                }

                boardProperties.bgColor.setValue(Color.valueOf(data.getString("bgColor")));
                boardProperties.graphColor.setValue(Color.valueOf(data.getString("lineColor")));
                boardProperties.opSlider.setValue(data.getDouble("lineOpacity")*100.0);
                boardProperties.indicatorColor.setValue(Color.valueOf(data.getString("indicatorColor")));
                if(data.has("isIndicator"))boardProperties.isIndicator.setSelected(true);
                else boardProperties.isIndicator.setSelected(false);

                boardProperties.saveBtn.setOnMouseClicked(event1 -> {
                    Color bgColor=boardProperties.bgColor.getValue();
                    Color graphColor=boardProperties.graphColor.getValue();
                    Color indicatorColor=boardProperties.indicatorColor.getValue();
                    data.put("bgColor",bgColor.toString());
                    data.put("indicatorColor",indicatorColor.toString());
                    data.put("lineColor",graphColor.toString());
                    data.put("lineOpacity",boardProperties.opSlider.getValue()/100.0);
                    if(boardProperties.isIndicator.isSelected())data.put("isIndicator",true);
                    else if(data.has("isIndicator"))data.remove("isIndicator");
                    graphBgRect.setFill(bgColor);
                    for(int i=0;i<graphLineGroup.getChildren().size();i++){
                        Line line=(Line)graphLineGroup.getChildren().get(i);
                        line.setStroke(graphColor);
                        line.setOpacity(boardProperties.opSlider.getValue()/100.0);
                    }

                    drawIndicator();
                    anchorPane.setVisible(false);
                    //dialog.close();
                });

                boardProperties.cancelBtn.setOnMouseClicked(event1 -> {
                    //dialog.close();
                    anchorPane.setVisible(false);
                });

                boardProperties.defaultCoin.setOnMouseClicked(event1 -> {
                    try{
                        anchorPane.setVisible(true);
                        FXMLLoader prop1Loader = new FXMLLoader(getClass().getResource("/fxml/coinProperties.fxml"));
                        Parent prop1Layout = prop1Loader.load();
                        CoinProperties coinProperties = prop1Loader.getController();
                        anchorPane.getChildren().clear();
                        anchorPane.getChildren().add(prop1Layout);
                        prop1Layout.setTranslateX(125);
                        prop1Layout.setTranslateY(141.5);


                        coinProperties.innerColor.setValue(Color.valueOf(data.getJSONObject("defaultCoin").getString("innerColor")));
                        coinProperties.outerColor.setValue(Color.valueOf(data.getJSONObject("defaultCoin").getString("outerColor")));
                        if(data.getJSONObject("defaultCoin").has("cantMove"))coinProperties.cantMove.setSelected(true);
                        if(data.getJSONObject("defaultCoin").has("useSkin"))coinProperties.useSkin.setSelected(true);
                        if(data.getJSONObject("defaultCoin").has("isMust"))coinProperties.isMust.setSelected(true);
                        if(data.getJSONObject("defaultCoin").has("skin"))coinProperties.skin.getSelectionModel().select(data.getJSONObject("defaultCoin").getInt("skin"));

                        coinProperties.text.setDisable(true);

                        coinProperties.saveBtn.setOnMouseClicked(event2 -> {
                            Color innerColor=coinProperties.innerColor.getValue();
                            Color outerColor=coinProperties.outerColor.getValue();
                            data.getJSONObject("defaultCoin").put("innerColor",innerColor.toString());
                            data.getJSONObject("defaultCoin").put("outerColor",outerColor.toString());
                            data.getJSONObject("defaultCoin").put("skin",coinProperties.skin.getSelectionModel().getSelectedIndex());
                            if(coinProperties.cantMove.isSelected())
                                data.getJSONObject("defaultCoin").put("cantMove",true);
                            else if(data.getJSONObject("defaultCoin").has("cantMove"))data.getJSONObject("defaultCoin").remove("cantMove");
                            if(coinProperties.useSkin.isSelected()) {
                                data.getJSONObject("defaultCoin").put("useSkin",true);
                            }
                            else if(data.getJSONObject("defaultCoin").has("useSkin"))data.getJSONObject("defaultCoin").remove("useSkin");
                            if(coinProperties.isMust.isSelected()) {
                                data.getJSONObject("defaultCoin").put("isMust",true);
                            }
                            else if(data.getJSONObject("defaultCoin").has("isMust"))data.getJSONObject("defaultCoin").remove("isMust");
                            anchorPane.setVisible(false);

                            Coin coin=new Coin(62.5,25,18);
                            coin.setInnerColor(Color.valueOf(data.getJSONObject("defaultCoin").getString("innerColor")));
                            coin.setOuterColor(Color.valueOf(data.getJSONObject("defaultCoin").getString("outerColor")));
                            if(data.getJSONObject("defaultCoin").has("skin"))coin.setSkin(data.getJSONObject("defaultCoin").getInt("skin"));
                            if(data.getJSONObject("defaultCoin").has("useSkin"))coin.useSkin(true);
                            boardPan.group1.getChildren().set(1,coin);
                            ////System.out.println(data);
                        });

                        coinProperties.cancelBtn.setOnMouseClicked(event2 -> {
                            anchorPane.setVisible(false);
                        });
                    }catch (Exception e){

                    }
                });

                boardProperties.defaultMatchStick.setOnMouseClicked(event1 -> {
                    try{
                        anchorPane.setVisible(true);
                        FXMLLoader prop1Loader = new FXMLLoader(getClass().getResource("/fxml/stickProperties.fxml"));
                        Parent prop1Layout = prop1Loader.load();
                        StickProperties stickProperties = prop1Loader.getController();

                        anchorPane.getChildren().clear();
                        anchorPane.getChildren().add(prop1Layout);
                        prop1Layout.setTranslateX(125);
                        prop1Layout.setTranslateY(180);

                        stickProperties.fillColor.setValue(Color.valueOf(data.getJSONObject("defaultMatchStick").getString("fillColor")));
                        if(data.getJSONObject("defaultMatchStick").has("cantMove"))stickProperties.cantMove.setSelected(true);
                        if(data.getJSONObject("defaultMatchStick").has("useSkin"))stickProperties.useSkin.setSelected(true);
                        if(data.getJSONObject("defaultMatchStick").has("isMust"))stickProperties.isMust.setSelected(true);

                        stickProperties.saveBtn.setOnMouseClicked(event2 -> {
                            Color fillColor=stickProperties.fillColor.getValue();
                            data.getJSONObject("defaultMatchStick").put("fillColor",fillColor.toString());
                            if(stickProperties.cantMove.isSelected())
                                data.getJSONObject("defaultMatchStick").put("cantMove",true);
                            else if(data.getJSONObject("defaultMatchStick").has("cantMove"))data.getJSONObject("defaultMatchStick").remove("cantMove");
                            if(stickProperties.useSkin.isSelected()) {
                                data.getJSONObject("defaultMatchStick").put("useSkin",true);
                            }
                            else if(data.getJSONObject("defaultMatchStick").has("useSkin"))data.getJSONObject("defaultMatchStick").remove("useSkin");
                            if(stickProperties.isMust.isSelected()) {
                                data.getJSONObject("defaultMatchStick").put("isMust",true);
                            }
                            else if(data.getJSONObject("defaultMatchStick").has("isMust"))data.getJSONObject("defaultMatchStick").remove("isMust");
                            anchorPane.setVisible(false);
                            ////System.out.println(data);

                            MatchStick matchStick=new MatchStick(90,20,30,30);
                            matchStick.setFillColor(Color.valueOf(data.getJSONObject("defaultMatchStick").getString("fillColor")));
                            matchStick.setLayoutX(125);
                            if(data.getJSONObject("defaultMatchStick").has("useSkin")){
                                matchStick.setWeight(3.8);
                                matchStick.useSkin(true);
                                matchStick.setScaleX(2.5);
                                matchStick.setScaleY(2.5);
                            }
                            boardPan.group2.getChildren().set(1,matchStick);
                        });

                        stickProperties.cancelBtn.setOnMouseClicked(event2 -> {
                            anchorPane.setVisible(false);
                        });
                    }catch (Exception e){

                    }
                });
            }catch (Exception e){

            }
        });

        //loading pre-defined elements
        //updateElements(new Point3D(0,0,0));

        //Grouping
        bgGroup.getChildren().addAll(graphBgRect,graphLineGroup,borderRect,indicatorRect);
        graphGroup.getChildren().addAll(bgGroup,elementsGroup,avatarGroup);
        Rectangle bgRect=new Rectangle(500,500);
        bgRect.setFill(Color.valueOf("#ffffff"));
        layout.getChildren().addAll(bgRect,graphScene,panScene,anchorPane);
    }

    private void drawIndicator() {
        if(data.has("isIndicator")){
            indicatorRect.setVisible(true);
            indicatorRect.setFill(Color.valueOf("#00000000"));
            indicatorRect.setStrokeWidth(getWeight()/2);
            indicatorRect.setStroke(Color.valueOf(data.getString("indicatorColor")));
            if(elementsGroup.getChildren().size()>0){
                int minX=100,minY=100,maxX=-100,maxY=-100;
                for(int i=0;i<elementsGroup.getChildren().size();i++){
                    Node element=elementsGroup.getChildren().get(i);
                    if(element.getTypeSelector().equals("Coin")){
                        Coin coin=(Coin)element;
                        if(coin.isMust()) {
                            if (coin.getIndX() < minX) minX = coin.getIndX();
                            if (coin.getIndX() > maxX) maxX = coin.getIndX();
                            if (coin.getIndY() < minY) minY = coin.getIndY();
                            if (coin.getIndY() > maxY) maxY = coin.getIndY();
                        }
                    }else if(element.getTypeSelector().equals("MatchStick")){
                        MatchStick matchStick=(MatchStick)element;
                        if(matchStick.isMust()) {
                            if (matchStick.getIndHeadX() < minX) minX = matchStick.getIndHeadX();
                            if (matchStick.getIndHeadX() > maxX) maxX = matchStick.getIndHeadX();
                            if (matchStick.getIndTailX() < minX) minX = matchStick.getIndTailX();
                            if (matchStick.getIndTailX() > maxX) maxX = matchStick.getIndTailX();
                            if (matchStick.getIndHeadY() < minY) minY = matchStick.getIndHeadY();
                            if (matchStick.getIndHeadY() > maxY) maxY = matchStick.getIndHeadY();
                            if (matchStick.getIndTailY() < minY) minY = matchStick.getIndTailY();
                            if (matchStick.getIndTailY() > maxY) maxY = matchStick.getIndTailY();
                        }
                    }
                }
                if(minX!=100 && minY!=100 && maxX!=-100 && maxY!=-100) {
                    indicatorRect.setWidth((maxX - minX) * divSize + divSize * 1.5);
                    indicatorRect.setHeight((maxY - minY) * divSize + divSize * 1.5);
                    indicatorRect.setLayoutX(minX * divSize - divSize * 0.75);
                    indicatorRect.setLayoutY(minY * divSize - divSize * 0.75);
                }else
                    indicatorRect.setVisible(false);
            }else indicatorRect.setVisible(false);
        }else{
            indicatorRect.setVisible(false);
        }
    }

    private void setLineWidth(Group lineGroup){
        double minWidth=2,maxWidth=7,maxZoom=5000;
        for(int i=0;i<lineGroup.getChildren().size();i++){
            Line line=(Line)lineGroup.getChildren().get(i);
            line.setStrokeWidth(minWidth+(maxWidth-minWidth)*zoom/maxZoom);
        }
    }

    private void setMatchStickWidth(MatchStick matchStick){
        double minWidth=14,maxWidth=50,maxZoom=5000;
        matchStick.setWeight(minWidth+(maxWidth-minWidth)*zoom/maxZoom);
    }

    private double getWeight(){
        double minWidth=14,maxWidth=50,maxZoom=5000;
        return minWidth+(maxWidth-minWidth)*zoom/maxZoom;
    }

    public Group getLayout(){
        return layout;
    }
}
