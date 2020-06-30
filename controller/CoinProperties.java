package controller;

import com.sun.tools.jconsole.JConsoleContext;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import utility.Constant;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class CoinProperties implements Initializable{
    @FXML public ColorPicker innerColor;
    @FXML public ColorPicker outerColor;
    @FXML public Button saveBtn;
    @FXML public Button cancelBtn;
    @FXML public CheckBox cantMove;
    @FXML public CheckBox isMust;
    @FXML public CheckBox useSkin;
    @FXML public ComboBox skin;
    @FXML public TextField text;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        skin.setMaxHeight(36);

        for(int i=0;i<Constant.SKINS_COIN_LOCATION.length;i++){
            Image image=new Image(Constant.SKINS_COIN_LOCATION[i]);
            ImageView imageView=new ImageView(image);
            imageView.setFitWidth(36);
            imageView.setFitHeight(36);
            skin.getItems().add(imageView);
        }

        skin.getSelectionModel().select(0);

        skin.setCellFactory(new Callback<ListView<ImageView>, ListCell<ImageView>>() {

            @Override
            public ListCell<ImageView> call(ListView<ImageView> p) {
                return new ListCell<ImageView>() {
                    private final ImageView rectangle;

                    {
                        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                        rectangle = new ImageView();
                    }

                    @Override
                    protected void updateItem(ImageView item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            rectangle.setImage(item.getImage());
                            rectangle.setFitHeight(36);
                            rectangle.setFitWidth(36);
                            setGraphic(rectangle);
                        }
                    }
                };
            }
        });
        addTextLimiter(text,2);

    }
    public static void addTextLimiter(final TextField tf, final int maxLength) {
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (tf.getText().length() > maxLength) {
                    String s = tf.getText().substring(0, maxLength);
                    tf.setText(s);
                }
            }
        });
    }
}
