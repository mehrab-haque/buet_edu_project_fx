package editor;

import controller.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import utility.Constant;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static utility.Constant.*;

public class ProblemSet {

    private JSONObject data;
    private Group layout;
    private BoardView problemBoard;
    private List<BoardView> solutionBoardList;
    public ProbForm probForm;

    public ProblemSet() throws IOException {
        data=new JSONObject();
        load();
    }
    public ProblemSet(JSONObject data) throws IOException {
        this.data=data;
        load();
    }

    public JSONObject getData(){
        JSONObject outputData=new JSONObject();
        outputData.put("prob_schema",new JSONObject(problemBoard.getData().toString()));
        if(solutionBoardList.size()>0){
            JSONArray solutionsArray=new JSONArray();
            for(int i=0;i<solutionBoardList.size();i++)
                solutionsArray.put(new JSONObject(solutionBoardList.get(i).getData().toString()));
            outputData.put("sol_schema",solutionsArray);
        }
        if(!probForm.title.getText().isEmpty())outputData.put("title",probForm.title.getText());
        if(!probForm.series.getText().isEmpty())outputData.put("series",probForm.series.getText());
        if(!probForm.description.getText().isEmpty())outputData.put("description",probForm.description.getText());
        if(!probForm.statement.getText().isEmpty())outputData.put("statement",probForm.statement.getText());
        if(!probForm.time.getText().isEmpty())outputData.put("time",Integer.parseInt(probForm.time.getText()));
        if(!probForm.restrictions.getText().isEmpty())outputData.put("restrictions",probForm.restrictions.getText());
        if(!probForm.explanation.getText().isEmpty())outputData.put("explanation",probForm.explanation.getText());
        if(probForm.category.getSelectionModel().getSelectedIndex()!=-1)outputData.put("category",probForm.category.getSelectionModel().getSelectedIndex());
        if(probForm.difficulty.getSelectionModel().getSelectedIndex()!=-1)outputData.put("difficulty",probForm.difficulty.getSelectionModel().getSelectedIndex());
        if(probForm.keyVBox.getChildren().size()>0){
            JSONArray keywordsArray=new JSONArray();
            for(int i=0;i<probForm.keyVBox.getChildren().size();i++){
                Parent keywordLayout=(Parent)probForm.keyVBox.getChildrenUnmodifiable().get(i);
                HBox hBox=(HBox)keywordLayout.getChildrenUnmodifiable().get(1);
                HBox hBox1=(HBox)hBox.getChildrenUnmodifiable().get(0);
                Label label=(Label)hBox1.getChildrenUnmodifiable().get(0);
                keywordsArray.put(label.getText());
            }
            outputData.put("keywords",keywordsArray);
        }
        if(probForm.qImgVBox.getChildren().size()>0){
            JSONArray imagesArray=new JSONArray();
            for(int i=0;i<probForm.qImgVBox.getChildren().size();i++){
                Parent imageLayout=(Parent)probForm.qImgVBox.getChildrenUnmodifiable().get(i);
                ImageView imageView=(ImageView)imageLayout.getChildrenUnmodifiable().get(0);
                String path=imageView.getAccessibleText();
                File file=new File(path);
                if(file.exists())imagesArray.put(path);
            }
            if(imagesArray.length()>0)outputData.put("des_images",imagesArray);
        }
        if(probForm.aImgVBox.getChildren().size()>0){
            JSONArray imagesArray=new JSONArray();
            for(int i=0;i<probForm.aImgVBox.getChildren().size();i++){
                Parent imageLayout=(Parent)probForm.aImgVBox.getChildrenUnmodifiable().get(i);
                ImageView imageView=(ImageView)imageLayout.getChildrenUnmodifiable().get(0);
                String path=imageView.getAccessibleText();
                File file=new File(path);
                if(file.exists())imagesArray.put(path);
            }
            if(imagesArray.length()>0)outputData.put("ans_images",imagesArray);
        }
        outputData.put("ans_type",probForm.ansType.getSelectionModel().getSelectedIndex());
        if(probForm.ansType.getSelectionModel().getSelectedIndex()==1 && !probForm.ansTxt.getText().isEmpty())
            outputData.put("answer",probForm.ansTxt.getText());
        if(probForm.ansType.getSelectionModel().getSelectedIndex()==2 && probForm.optionsVBox.getChildren().size()>0) {
            JSONArray optionsArray=new JSONArray();
            for(int i=0;i<probForm.optionsVBox.getChildren().size();i++){
                Parent optionLayout=(Parent)probForm.optionsVBox.getChildrenUnmodifiable().get(i);
                HBox hBox=(HBox)optionLayout.getChildrenUnmodifiable().get(1);
                HBox hBox1=(HBox)hBox.getChildrenUnmodifiable().get(0);
                Label label=(Label)hBox1.getChildrenUnmodifiable().get(0);
                optionsArray.put(label.getText());
            }
            outputData.put("options",optionsArray);
        }
        if(probForm.ansType.getSelectionModel().getSelectedIndex()==2 && probForm.ansOption.getSelectionModel().getSelectedIndex()!=-1)
            outputData.put("answer",probForm.ansOption.getSelectionModel().getSelectedIndex());
        return outputData;
    }

    public Group getLayout(){
        return layout;
    }

    private void load() throws IOException {
        layout=new Group();
        Group boardGroup=new Group();
        Group solutionGroup=new Group();
        if(data.has("prob_schema"))
            problemBoard=new BoardView(data.getJSONObject("prob_schema"), Constant.MODE_PROBLEM_SETTING);
        else
            problemBoard=new BoardView(Constant.MODE_PROBLEM_SETTING);
        Rectangle dividerRect=new Rectangle(10,600);
        dividerRect.setFill(Constant.COLOR_SKYBLUE);
        dividerRect.setLayoutX(500);
        dividerRect.setLayoutY(-50);
        FXMLLoader solutionLoader = new FXMLLoader(getClass().getResource("/fxml/solutionButton.fxml"));
        Parent solutionLayout = solutionLoader.load();
        SolutionButton solutionButton = solutionLoader.getController();
        solutionLayout.setLayoutX(510);
        solutionLayout.setLayoutY(-50);
        solutionGroup.getChildren().add(solutionLayout);
        boardGroup.getChildren().addAll(problemBoard.getLayout(),dividerRect,solutionGroup);
        FXMLLoader headerLoader = new FXMLLoader(getClass().getResource("/fxml/solutionHeader.fxml"));
        Parent headerLayout = headerLoader.load();
        SolutionHeader solutionHeader = headerLoader.getController();
        headerLayout.setLayoutX(510);
        headerLayout.setVisible(false);
        solutionBoardList=new ArrayList<>();
        if(data.has("sol_schema")) {
            for (int i = 0; i < data.getJSONArray("sol_schema").length(); i++) {
                BoardView boardView=new BoardView(data.getJSONArray("sol_schema").getJSONObject(i), Constant.MODE_PROBLEM_SOLVING);
                boardView.getLayout().setLayoutX(510);
                solutionBoardList.add(boardView);
            }
            BoardView boardView=solutionBoardList.get(0);
            solutionGroup.getChildren().add(boardView.getLayout());
            solutionHeader.dropdown.getItems().clear();
            for(int i=1;i<=solutionBoardList.size();i++){
                solutionHeader.dropdown.getItems().add("Solution - "+i);
            }
            solutionHeader.dropdown.getSelectionModel().select(0);
            if(solutionBoardList.size()==1)solutionHeader.delete.setDisable(true);
            headerLayout.setVisible(true);

            JSONObject probData = new JSONObject(problemBoard.getData().toString());
            BoardView probBoard=new BoardView(probData,Constant.MODE_PROBLEM_IMAGE);
            boardGroup.getChildren().set(0,probBoard.getLayout());
        }
        solutionButton.btn.setOnMouseClicked(event -> {
            try {
                JSONObject probData = new JSONObject(problemBoard.getData().toString());
                BoardView boardView = new BoardView(probData, Constant.MODE_PROBLEM_SOLVING);
                boardView.getLayout().setLayoutX(510);
                solutionGroup.getChildren().add(boardView.getLayout());
                BoardView probBoard=new BoardView(probData,Constant.MODE_PROBLEM_IMAGE);
                boardGroup.getChildren().set(0,probBoard.getLayout());
                solutionBoardList.add(boardView);

                solutionHeader.dropdown.getItems().clear();
                solutionHeader.dropdown.getItems().add("Solution - 1");
                solutionHeader.dropdown.getSelectionModel().select(0);

                solutionHeader.delete.setDisable(true);

                headerLayout.setVisible(true);
            }catch (Exception e){

            }
        });

        solutionHeader.add.setOnMouseClicked(event->{
            try {
                JSONObject probData = new JSONObject(problemBoard.getData().toString());
                BoardView boardView = new BoardView(probData, Constant.MODE_PROBLEM_SOLVING);
                boardView.getLayout().setLayoutX(510);
                solutionGroup.getChildren().set(solutionGroup.getChildren().size()-1,boardView.getLayout());

                solutionBoardList.add(boardView);

                solutionHeader.dropdown.getItems().clear();
                for(int i=1;i<=solutionBoardList.size();i++){
                    solutionHeader.dropdown.getItems().add("Solution - "+i);
                }
                solutionHeader.dropdown.getSelectionModel().select(solutionBoardList.size()-1);

                if(solutionBoardList.size()>1)solutionHeader.delete.setDisable(false);
            }catch (Exception e){

            }
        });

        solutionHeader.delete.setOnMouseClicked(event -> {
            int index=solutionHeader.dropdown.getSelectionModel().getSelectedIndex();
            solutionBoardList.remove(index);
            solutionGroup.getChildren().set(solutionGroup.getChildren().size()-1,solutionBoardList.get(solutionBoardList.size()-1).getLayout());
            solutionHeader.dropdown.getItems().clear();
            for(int i=1;i<=solutionBoardList.size();i++){
                solutionHeader.dropdown.getItems().add("Solution - "+i);
            }
            solutionHeader.dropdown.getSelectionModel().select(solutionBoardList.size()-1);

            if(solutionBoardList.size()==1)solutionHeader.delete.setDisable(true);
        });

        solutionHeader.dropdown.setOnAction(event->{
            solutionGroup.getChildren().set(solutionGroup.getChildren().size()-1,solutionBoardList.get(solutionHeader.dropdown.getSelectionModel().getSelectedIndex()).getLayout());
        });


        boardGroup.setLayoutY(50);

        FXMLLoader hLoader = new FXMLLoader(getClass().getResource("/fxml/problemHeader.fxml"));
        Parent hLayout = hLoader.load();
        ProbHeader probHeader = hLoader.getController();

        probHeader.edit.setOnMouseClicked(event -> {
            solutionGroup.getChildren().remove(solutionGroup.getChildren().size()-1);
            solutionHeader.dropdown.getItems().clear();
            solutionBoardList.clear();
            boardGroup.getChildren().set(0,problemBoard.getLayout());
            headerLayout.setVisible(false);
        });

        FXMLLoader formLoader = new FXMLLoader(getClass().getResource("/fxml/probForm.fxml"));
        Parent formLayout = formLoader.load();
        formLayout.setLayoutX(1020);
        formLayout.setLayoutY(50);
        probForm = formLoader.getController();

        if(data.has("title"))probForm.title.setText(data.getString("title"));
        if(data.has("series"))probForm.series.setText(data.getString("series"));
        if(data.has("description"))probForm.description.setText(data.getString("description"));
        if(data.has("statement"))probForm.statement.setText(data.getString("statement"));
        if(data.has("restrictions"))probForm.restrictions.setText(data.getString("restrictions"));
        if(data.has("explanation"))probForm.explanation.setText(data.getString("explanation"));
        if(data.has("time"))probForm.time.setText(String.valueOf(data.getInt("time")));
        if(data.has("difficulty"))probForm.difficulty.getSelectionModel().select(data.getInt("difficulty"));
        if(data.has("category"))probForm.category.getSelectionModel().select(data.getInt("category"));
        if(data.has("ans_type"))probForm.ansType.getSelectionModel().select(data.getInt("ans_type"));
        if(data.has("ans_type") && data.getInt("ans_type")==1) {
            probForm.txtVBox.setVisible(true);
            probForm.mcqVBox.setVisible(false);
            probForm.txtVBox.setManaged(true);
            probForm.mcqVBox.setManaged(false);
            if(data.has("answer"))probForm.ansTxt.setText(data.getString("answer"));
        }
        if(data.has("ans_type") && data.getInt("ans_type")==2){
            probForm.txtVBox.setVisible(false);
            probForm.mcqVBox.setVisible(true);
            probForm.txtVBox.setManaged(false);
            probForm.mcqVBox.setManaged(true);
            if(data.has("options")){
                JSONArray optionsArray=data.getJSONArray("options");
                for(int i=0;i<optionsArray.length();i++){
                    try{
                        FXMLLoader keywordLoader = new FXMLLoader(getClass().getResource("/fxml/keyword.fxml"));
                        Parent keywordLayout = keywordLoader.load();
                        Keyword keyword=keywordLoader.getController();
                        keywordLayout.setOnMouseMoved(event2 -> {
                            keyword.rect.setVisible(true);
                            keyword.remove.setVisible(true);
                        });
                        keywordLayout.setOnMouseMoved(event2 -> {
                            keyword.rect.setVisible(true);
                            keyword.remove.setVisible(true);
                        });
                        keywordLayout.setOnMouseExited(event2 -> {
                            keyword.rect.setVisible(false);
                            keyword.remove.setVisible(false);
                        });
                        keyword.text.setText(optionsArray.getString(i));
                        probForm.optionsVBox.getChildren().add(keywordLayout);
                        probForm.ansOption.getItems().add("Option - "+(probForm.ansOption.getItems().size()+1));
                        keyword.remove.setOnMouseClicked(event2 -> {
                            probForm.optionsVBox.getChildren().remove(keywordLayout);
                            probForm.ansOption.getItems().remove(probForm.ansOption.getItems().size()-1);
                        });
                    }catch (Exception e){

                    }
                }
            }
            if(data.has("answer"))
                probForm.ansOption.getSelectionModel().select(data.getInt("answer"));
        }
        if(data.has("keywords")){
            JSONArray keywordsArray=data.getJSONArray("keywords");
            for(int i=0;i<keywordsArray.length();i++){
                try{
                    FXMLLoader keywordLoader = new FXMLLoader(getClass().getResource("/fxml/keyword.fxml"));
                    Parent keywordLayout = keywordLoader.load();
                    Keyword keyword=keywordLoader.getController();
                    keywordLayout.setOnMouseMoved(event2 -> {
                        keyword.rect.setVisible(true);
                        keyword.remove.setVisible(true);
                    });
                    keywordLayout.setOnMouseEntered(event2 -> {
                        keyword.rect.setVisible(true);
                        keyword.remove.setVisible(true);
                    });
                    keywordLayout.setOnMouseExited(event2 -> {
                        keyword.rect.setVisible(false);
                        keyword.remove.setVisible(false);
                    });
                    keyword.text.setText(keywordsArray.getString(i));
                    probForm.keyVBox.getChildren().add(keywordLayout);
                    keyword.remove.setOnMouseClicked(event2 -> {
                        probForm.keyVBox.getChildren().remove(keywordLayout);
                    });
                }catch (Exception e){

                }
            }
        }
        if(data.has("des_images")){
            JSONArray imagesArray=data.getJSONArray("des_images");
            for(int i=0;i<imagesArray.length();i++){
                String path=imagesArray.getString(i);
                File file=new File(path);
                if(file.exists()){
                    try {
                        FXMLLoader imageLoader = new FXMLLoader(getClass().getResource("/fxml/removableImageView.fxml"));
                        Parent imageLayout = imageLoader.load();
                        RemovableImageView removableImageView = imageLoader.getController();
                        imageLayout.setOnMouseMoved(event1 -> {
                            removableImageView.rect.setHeight(removableImageView.image.getFitHeight());
                            removableImageView.rect.setVisible(true);
                            removableImageView.remove.setVisible(true);
                        });
                        imageLayout.setOnMouseEntered(event1 -> {
                            removableImageView.rect.setHeight(removableImageView.image.getFitHeight());
                            removableImageView.rect.setVisible(true);
                            removableImageView.remove.setVisible(true);
                        });
                        imageLayout.setOnMouseExited(event1 -> {
                            removableImageView.rect.setVisible(false);
                            removableImageView.remove.setVisible(false);
                        });
                        Image image = new Image(file.toURI().toString());
                        removableImageView.image.setImage(image);
                        removableImageView.image.setAccessibleText(path);
                        removableImageView.image.setFitWidth(280);
                        removableImageView.image.setFitHeight(280 * image.getHeight() / image.getWidth());
                        probForm.qImgVBox.getChildren().add(imageLayout);
                        removableImageView.remove.setOnMouseClicked(event1 -> {
                            probForm.qImgVBox.getChildren().remove(imageLayout);
                        });
                    }catch (Exception e){

                    }
                }
            }
        }

        if(data.has("ans_images")){
            JSONArray imagesArray=data.getJSONArray("ans_images");
            for(int i=0;i<imagesArray.length();i++){
                String path=imagesArray.getString(i);
                File file=new File(path);
                if(file.exists()){
                    try {
                        FXMLLoader imageLoader = new FXMLLoader(getClass().getResource("/fxml/removableImageView.fxml"));
                        Parent imageLayout = imageLoader.load();
                        RemovableImageView removableImageView = imageLoader.getController();
                        imageLayout.setOnMouseMoved(event1 -> {
                            removableImageView.rect.setHeight(removableImageView.image.getFitHeight());
                            removableImageView.rect.setVisible(true);
                            removableImageView.remove.setVisible(true);
                        });
                        imageLayout.setOnMouseEntered(event1 -> {
                            removableImageView.rect.setHeight(removableImageView.image.getFitHeight());
                            removableImageView.rect.setVisible(true);
                            removableImageView.remove.setVisible(true);
                        });
                        imageLayout.setOnMouseExited(event1 -> {
                            removableImageView.rect.setVisible(false);
                            removableImageView.remove.setVisible(false);
                        });
                        Image image = new Image(file.toURI().toString());
                        removableImageView.image.setImage(image);
                        removableImageView.image.setAccessibleText(path);
                        removableImageView.image.setFitWidth(280);
                        removableImageView.image.setFitHeight(280 * image.getHeight() / image.getWidth());
                        probForm.aImgVBox.getChildren().add(imageLayout);
                        removableImageView.remove.setOnMouseClicked(event1 -> {
                            probForm.aImgVBox.getChildren().remove(imageLayout);
                        });
                    }catch (Exception e){

                    }
                }
            }
        }

        AnchorPane anchorPane=new AnchorPane();
        anchorPane.setPrefWidth(310);
        anchorPane.setPrefHeight(550);
        anchorPane.setStyle("-fx-background-color: #0090ff44");
        anchorPane.setLayoutX(1020);
        anchorPane.setLayoutY(50);
        anchorPane.setVisible(false);

        probForm.keywordBtn.setOnMouseClicked(event -> {
            try {
                FXMLLoader inputLoader = new FXMLLoader(getClass().getResource("/fxml/input.fxml"));
                Parent inputLayout = inputLoader.load();
                inputLayout.setLayoutX(30);
                inputLayout.setLayoutY(217.5);
                Input input=inputLoader.getController();
                anchorPane.setVisible(true);
                anchorPane.getChildren().clear();
                anchorPane.getChildren().add(inputLayout);

                input.cancel.setOnMouseClicked(event1 -> {
                    anchorPane.setVisible(false);
                });

                input.submit.setOnMouseClicked(event1 -> {
                    if(!input.txt.getText().isEmpty()){
                        try{
                            FXMLLoader keywordLoader = new FXMLLoader(getClass().getResource("/fxml/keyword.fxml"));
                            Parent keywordLayout = keywordLoader.load();
                            Keyword keyword=keywordLoader.getController();
                            keywordLayout.setOnMouseMoved(event2 -> {
                                keyword.rect.setVisible(true);
                                keyword.remove.setVisible(true);
                            });
                            keywordLayout.setOnMouseMoved(event2 -> {
                                keyword.rect.setVisible(true);
                                keyword.remove.setVisible(true);
                            });
                            keywordLayout.setOnMouseExited(event2 -> {
                                keyword.rect.setVisible(false);
                                keyword.remove.setVisible(false);
                            });
                            keyword.text.setText(input.txt.getText());
                            probForm.keyVBox.getChildren().add(keywordLayout);
                            keyword.remove.setOnMouseClicked(event2 -> {
                                probForm.keyVBox.getChildren().remove(keywordLayout);
                            });
                            anchorPane.setVisible(false);
                        }catch (Exception e){

                        }
                    }
                });
            }catch (Exception e){

            }
        });

        probForm.imgQ.setOnMouseClicked(event -> {
            FileChooser fileChooser=new FileChooser();
            FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
            FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
            fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
            File file = fileChooser.showOpenDialog(null);
            String path = file.getAbsolutePath();
            if(file!=null){
                try {
                    FXMLLoader imageLoader = new FXMLLoader(getClass().getResource("/fxml/removableImageView.fxml"));
                    Parent imageLayout = imageLoader.load();
                    RemovableImageView removableImageView = imageLoader.getController();
                    imageLayout.setOnMouseMoved(event1 -> {
                        removableImageView.rect.setHeight(removableImageView.image.getFitHeight());
                        removableImageView.rect.setVisible(true);
                        removableImageView.remove.setVisible(true);
                    });
                    imageLayout.setOnMouseEntered(event1 -> {
                        removableImageView.rect.setHeight(removableImageView.image.getFitHeight());
                        removableImageView.rect.setVisible(true);
                        removableImageView.remove.setVisible(true);
                    });
                    imageLayout.setOnMouseExited(event1 -> {
                        removableImageView.rect.setVisible(false);
                        removableImageView.remove.setVisible(false);
                    });
                    Image image = new Image(file.toURI().toString());
                    removableImageView.image.setImage(image);
                    removableImageView.image.setAccessibleText(path);
                    removableImageView.image.setFitWidth(280);
                    removableImageView.image.setFitHeight(280 * image.getHeight() / image.getWidth());
                    probForm.qImgVBox.getChildren().add(imageLayout);
                    removableImageView.remove.setOnMouseClicked(event1 -> {
                        probForm.qImgVBox.getChildren().remove(imageLayout);
                    });
                }catch (Exception e){

                }
            }
        });

        probForm.imgA.setOnMouseClicked(event -> {
            FileChooser fileChooser=new FileChooser();
            FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
            FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
            fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
            File file = fileChooser.showOpenDialog(null);
            String path = file.getAbsolutePath();
            if(file!=null){
                try {
                    FXMLLoader imageLoader = new FXMLLoader(getClass().getResource("/fxml/removableImageView.fxml"));
                    Parent imageLayout = imageLoader.load();
                    RemovableImageView removableImageView = imageLoader.getController();
                    imageLayout.setOnMouseMoved(event1 -> {
                        removableImageView.rect.setHeight(removableImageView.image.getFitHeight());
                        removableImageView.rect.setVisible(true);
                        removableImageView.remove.setVisible(true);
                    });
                    imageLayout.setOnMouseEntered(event1 -> {
                        removableImageView.rect.setHeight(removableImageView.image.getFitHeight());
                        removableImageView.rect.setVisible(true);
                        removableImageView.remove.setVisible(true);
                    });
                    imageLayout.setOnMouseExited(event1 -> {
                        removableImageView.rect.setVisible(false);
                        removableImageView.remove.setVisible(false);
                    });
                    Image image = new Image(file.toURI().toString());
                    removableImageView.image.setImage(image);
                    removableImageView.image.setAccessibleText(path);
                    removableImageView.image.setFitWidth(280);
                    removableImageView.image.setFitHeight(280 * image.getHeight() / image.getWidth());
                    probForm.aImgVBox.getChildren().add(imageLayout);
                    removableImageView.remove.setOnMouseClicked(event1 -> {
                        probForm.aImgVBox.getChildren().remove(imageLayout);
                    });
                }catch (Exception e){

                }
            }
        });

        probForm.addOptionBtn.setOnMouseClicked(event -> {
            try {
                FXMLLoader inputLoader = new FXMLLoader(getClass().getResource("/fxml/input.fxml"));
                Parent inputLayout = inputLoader.load();
                inputLayout.setLayoutX(30);
                inputLayout.setLayoutY(217.5);
                Input input=inputLoader.getController();
                input.txt.setPromptText("Option...");
                anchorPane.setVisible(true);
                anchorPane.getChildren().clear();
                anchorPane.getChildren().add(inputLayout);

                input.cancel.setOnMouseClicked(event1 -> {
                    anchorPane.setVisible(false);
                });

                input.submit.setOnMouseClicked(event1 -> {
                    if(!input.txt.getText().isEmpty()){
                        try{
                            FXMLLoader keywordLoader = new FXMLLoader(getClass().getResource("/fxml/keyword.fxml"));
                            Parent keywordLayout = keywordLoader.load();
                            Keyword keyword=keywordLoader.getController();
                            keywordLayout.setOnMouseMoved(event2 -> {
                                keyword.rect.setVisible(true);
                                keyword.remove.setVisible(true);
                            });
                            keywordLayout.setOnMouseMoved(event2 -> {
                                keyword.rect.setVisible(true);
                                keyword.remove.setVisible(true);
                            });
                            keywordLayout.setOnMouseExited(event2 -> {
                                keyword.rect.setVisible(false);
                                keyword.remove.setVisible(false);
                            });
                            keyword.text.setText(input.txt.getText());
                            probForm.optionsVBox.getChildren().add(keywordLayout);
                            probForm.ansOption.getItems().add("Option - "+(probForm.ansOption.getItems().size()+1));
                            keyword.remove.setOnMouseClicked(event2 -> {
                                probForm.optionsVBox.getChildren().remove(keywordLayout);
                                probForm.ansOption.getItems().remove(probForm.ansOption.getItems().size()-1);
                            });
                            anchorPane.setVisible(false);
                        }catch (Exception e){

                        }
                    }
                });
            }catch (Exception e){

            }
        });

        layout.getChildren().addAll(hLayout,boardGroup,headerLayout,formLayout,anchorPane);
    }
}
