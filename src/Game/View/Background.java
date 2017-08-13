package Game.View;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Background{
    Rectangle wood, beige;

    public Background(Pane rootPane, int width, int height){
        wood = new Rectangle(width, height);
        beige = new Rectangle(width - 20, height - 20);
        wood.setArcHeight(20.0);
        wood.setArcWidth(20.0);
        beige.setArcHeight(20.0);
        beige.setArcWidth(20.0);
        beige.setLayoutX(10.0);
        beige.setLayoutY(10.0);
        Image background = new Image("file:../../assets/images/wood_pattern.jpg");
        wood.setFill(new ImagePattern(background));
        beige.setFill(Paint.valueOf("#F6EDAE"));

        rootPane.getChildren().addAll(wood, beige);
    }//Constructor: Creates a new Background (Beige with bood background) and adds to rootPane.

    public Rectangle getWood(){
        return wood;
    }

    public Rectangle getBeige(){
        return beige;
    }
}