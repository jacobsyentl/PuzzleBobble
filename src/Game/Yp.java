package Game;

import Game.Controller.MenuController;
import Game.View.MainScene;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Yp extends Application {
    StackPane rootPane;
    @Override
    public void start(Stage primaryStage) throws Exception{

        primaryStage.setResizable(false);

        rootPane = new MainScene();
        Scene scene = new Scene(rootPane);

        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        scene.getStylesheets().add("https://fonts.googleapis.com/css?family=Fredoka%20One");

        primaryStage.setTitle("Space Bubble");
        primaryStage.getIcons().add(new Image("file:../../assets/images/avatars/Avatar-4.png"));
        primaryStage.setScene(scene);
        new MenuController(rootPane);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}