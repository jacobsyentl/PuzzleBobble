package Game.View;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class MainScene extends StackPane {
    public MainScene() {
        styleMainGamePane();
    }

    private void styleMainGamePane() {
        stylePane();
        makeLandscape();
    }

    private void stylePane(){
        this.setPrefSize(875.0,720.0);
        this.setMinSize(875.0,720.0);
        this.setMaxSize(875.0,720.0);
        this.setId("mainPane");
    }

    private void makeLandscape() {
        Rectangle rect = new Rectangle(900,250);
        this.setAlignment(rect, Pos.BOTTOM_CENTER);
        Image background = new Image("file:../../assets/images/Green_landscape.png");
        rect.setFill(new ImagePattern(background));
        this.getChildren().add(rect);
    }
}