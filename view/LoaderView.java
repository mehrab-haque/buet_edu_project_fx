package view;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.File;

public class LoaderView {
    private Group group;
    public LoaderView(int width, int height){
        group=new Group();
        File file = new File("src/resources/loader.gif");
        Image image = new Image(file.toURI().toString());
        Rectangle rectangle=new Rectangle(width,height);
        rectangle.setFill(Color.WHITE);
        ImageView imageView=new ImageView();
        imageView.setImage(image);
        imageView.setFitHeight(80);
        imageView.setFitWidth(80);
        imageView.translateXProperty().set(width/2-40);
        imageView.translateYProperty().set(height/2-40);
        group.getChildren().add(rectangle);
        group.getChildren().add(imageView);
    }
    public Group getLayout(){
        return group;
    }
}
