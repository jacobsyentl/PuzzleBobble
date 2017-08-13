package Game.View;

import Game.Controller.MenuController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

public class MenuPane extends Pane{
    private StackPane rootPane;
    private Pane pane = this;
    private Button singleplayer, multiplayer,highscores, credits, exit;
    private MenuController parent;
    private ImageView imgFrame;
    private boolean playMusic = false;
    Background background;

    public MenuPane(StackPane rootPane, MenuController parent) {
        this.rootPane = rootPane;
        this.parent = parent;
        initComponents();
        background = new Background(rootPane, 350, 690);
        addComponents();
        addEventHandlers();
    }

    private void initComponents() {
        if(playMusic) {
            String url = "/res/theme.mp3";
            URL resource = getClass().getResource(url);
            final Media media = new Media(resource.toString());
            final MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setVolume(0.2);
            mediaPlayer.play();
        }

        pane.setMaxSize(320, 640);
        pane.setPrefSize(320, 640);
        Image logo = new Image("file:../../assets/images/logo.png");
        imgFrame = new ImageView();
        imgFrame.setImage(logo);
        imgFrame.setLayoutX(pane.getMaxWidth() / 2 - 150);
        imgFrame.setLayoutY(pane.getMaxHeight() / 2 - 300);

        singleplayer = new Button("Singleplayer");
        singleplayer.setId("mainMenuButton");
        singleplayer.setPrefSize(250, 50);
        singleplayer.setLayoutX(pane.getMaxWidth() / 2 - 125);
        singleplayer.setLayoutY(pane.getMaxHeight() / 2 - 100);

        multiplayer = new Button("Multiplayer");
        multiplayer.setId("mainMenuButton");
        multiplayer.setPrefSize(250, 50);
        multiplayer.setLayoutX(pane.getMaxWidth() / 2 - 125);
        multiplayer.setLayoutY(pane.getMaxHeight() / 2 - 25);

        highscores = new Button("Highscores");
        highscores.setId("mainMenuButton");
        highscores.setPrefSize(250, 50);
        highscores.setLayoutX(pane.getMaxWidth() / 2 - 125);
        highscores.setLayoutY(pane.getMaxHeight() / 2 + 50);

        credits = new Button("Credits");
        credits.setId("mainMenuButton");
        credits.setPrefSize(250, 50);
        credits.setLayoutX(pane.getMaxWidth() / 2 - 125);
        credits.setLayoutY(pane.getMaxHeight() / 2 + 125);

        exit = new Button("Exit");
        exit.setId("exitButton");
        exit.setPrefSize(250, 50);
        exit.setLayoutX(pane.getMaxWidth() / 2 - 125);
        exit.setLayoutY(pane.getMaxHeight() / 2 + 225);
    }

    private void addComponents() {
        pane.getChildren().addAll(singleplayer, multiplayer, highscores, credits, exit, imgFrame);
        rootPane.getChildren().add(pane);
    }

    private void addEventHandlers() {
        singleplayer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                removeAllComponents();
                parent.invokeSettingsController();
            }
        });

        multiplayer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                removeAllComponents();
                parent.invokeMPController();
            }
        });

        highscores.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                removeAllComponents();
                parent.invokeHighscoresController();
            }
        });

        credits.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                removeAllComponents();
                parent.invokeCreditsController();
            }
        });

        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.exit(1);
            }
        });
    }

    private void removeAllComponents(){
        rootPane.getChildren().removeAll(pane, background.getBeige(), background.getWood());
    }
}