package Game.View;

import Game.Controller.HighscoresController;
import Game.Database.HighscorePerLevel;
import Game.Database.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;

public class HighscoresPane extends Pane {
    private StackPane rootPane;
    private Pane pane = this;
    private HighscoresController parent;
    private TableView tvSP, tvMP;
    private String selectedItem;
    private TableColumn playerSP, scoreSP, playerMP, scoreMP;
    private Button btnBack;
    private ChoiceBox levelChoice;
    Label lblSP, lblMP;
    ObservableList<HighscorePerLevel> highscoreData;
    ObservableList<Player> mpData;
    Background background;

    public HighscoresPane(StackPane rootPane, HighscoresController parent){
        this.rootPane = rootPane;
        this.parent = parent;
        initComponents();
        background = new Background(rootPane, 700, 690);
        addComponents();
        addEventHandlers();
    }//Constructor: Creates a Pane and adds it to the StackPanel

    private void initComponents() {
        //Singeplayer
        pane.setMaxSize(330, 640);
        pane.setPrefSize(330, 640);

        lblSP = new Label("Singleplayer Highscores");
        lblSP.setLayoutX(-150);
        lblSP.setLayoutY(13);
        lblSP.setId("labelHighScores");

        Image backIcon  = new Image("file:../../assets/images/buttons/back.png");
        ImageView backIconView = new ImageView(backIcon);
        btnBack = new Button();
        btnBack.setGraphic(backIconView);
        btnBack.setId("mainMenuButton");
        btnBack.setPrefSize(80, 50);
        btnBack.setLayoutX(pane.getMaxWidth() / 2 - 35);
        btnBack.setLayoutY(pane.getMaxHeight() / 2 + 250);

        levelChoice = new ChoiceBox();
        levelChoice.setId("selectBox");
        levelChoice.setPrefSize(250, 50);
        levelChoice.setLayoutX(10 - levelChoice.getPrefWidth() / 2);
        levelChoice.setLayoutY(pane.getMaxHeight() / 2 - 250);

        ArrayList<String> levels;
        levels = parent.getLevels();

        ObservableList<String> levelsObservableList = FXCollections.observableArrayList(levels);
        levelChoice.setItems(levelsObservableList);
        levelChoice.getSelectionModel().selectFirst();
        selectedItem = levelChoice.getSelectionModel().getSelectedItem().toString();

        tvSP = new TableView();
        tvSP.setEditable(false);
        tvSP.setPrefSize(250, 400);
        tvSP.setLayoutX(10 - tvSP.getPrefWidth() / 2);
        tvSP.setLayoutY(pane.getMaxHeight() / 2 - 185);
        tvSP.setId("tableView");
        tvSP.setPlaceholder(new Label("No highscores found for selected level."));

        playerSP = new TableColumn("Player");
        scoreSP = new TableColumn("Score");

        loadLevelData();
        tvSP.getColumns().addAll(playerSP, scoreSP);

        playerSP.setPrefWidth(tvSP.getPrefWidth() / 2 - 5 + 35);
        playerSP.setResizable(false);
        scoreSP.setPrefWidth(tvSP.getPrefWidth() / 2 - 5 - 35);
        scoreSP.setResizable(false);


        //Multiplayer
        lblMP = new Label("Mutliplayer Highscores");
        lblMP.setLayoutX(pane.getPrefWidth() / 2 + 25);
        lblMP.setLayoutY(13);
        lblMP.setId("labelHighScores");

        tvMP = new TableView();
        tvMP.setEditable(false);
        tvMP.setPrefSize(250, 400);
        tvMP.setLayoutX(pane.getPrefWidth() - tvSP.getPrefWidth() / 2);
        tvMP.setLayoutY(pane.getMaxHeight() / 2 - 185);
        tvMP.setId("tableView");
        tvMP.setPlaceholder(new Label("No players found with more than 0 wins."));

        playerMP = new TableColumn("Player");
        scoreMP = new TableColumn("Wins");

        playerMP.setPrefWidth(tvSP.getPrefWidth() / 2 - 5 + 35);
        playerMP.setResizable(false);
        scoreMP.setPrefWidth(tvSP.getPrefWidth() / 2 - 5 - 35);
        scoreMP.setResizable(false);

        loadMPData();
        tvMP.getColumns().addAll(playerMP, scoreMP);
    }//Initializes the objects that are going to be put on the pane

    private void addComponents() {
        pane.getChildren().addAll(btnBack, levelChoice, tvSP, lblSP, lblMP, tvMP);
        rootPane.getChildren().add(pane);
    }//Adds this pane to the StackPanel

    private void addEventHandlers() {
        btnBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                removeAllComponents();
                parent.invokeMenuController();
            }
        });

        levelChoice.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selectedItem = levelChoice.getSelectionModel().getSelectedItem().toString();
                loadLevelData();
            }
        });
    }//Adds the event handlers for the button and the option box

    private void removeAllComponents(){
        rootPane.getChildren().removeAll(pane, background.getBeige(), background.getWood());
    }//Removes this panel from the StackPanel

    private void loadLevelData(){
        highscoreData = parent.getLevelData(Integer.parseInt(selectedItem.substring(6)));
        playerSP.setCellValueFactory(
                new PropertyValueFactory<HighscorePerLevel, String>("playerName")
        );


        scoreSP.setCellValueFactory(
                new PropertyValueFactory<HighscorePerLevel, Integer>("highscore")
        );

        tvSP.setItems(highscoreData);
    }//Loads the data from the database

    private void loadMPData(){
        mpData = parent.getPlayers();
        playerMP.setCellValueFactory(
                new PropertyValueFactory<Player, String>("playerName")
        );

        scoreMP.setCellValueFactory(
                new PropertyValueFactory<Player, Integer>("mpHighScoreCount")
        );

        tvMP.setItems(mpData);
    }
}