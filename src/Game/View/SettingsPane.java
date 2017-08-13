package Game.View;

import Game.Controller.SettingsController;
import Game.Database.BubbleDB;
import Game.Database.Player;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class SettingsPane extends Pane {
    private StackPane rootPane;
    private Pane pane = this;
    private Button btnNext, btnBack;
    private SettingsController parent;
    private RadioButton rbDifficulty1 = new RadioButton("EASY");
    private RadioButton rbDifficulty2 = new RadioButton("MEDIUM");
    private RadioButton rbDifficulty3 = new RadioButton("HARD");
    private final ToggleGroup radioGroup = new ToggleGroup();
    private Label lblTitle;
    private TextField txtPlayerName;
    private Circle avatar1Circle, avatar2Circle, avatar3Circle, avatar4Circle;
    private String playerName, selectedAvatar;
    BubbleDB bubbleDB = BubbleDB.getInstance();
    Background background;

    public SettingsPane(StackPane rootPane, SettingsController parent){
        this.rootPane = rootPane;
        this.parent = parent;
        initComponents();
        background = new Background(rootPane, 350, 690);
        addComponents();
        addEventHandlers();
    }

    private void initComponents() {
        pane.setMaxSize(330, 640);
        pane.setPrefSize(330, 640);

        lblTitle = new Label("Settings");
        lblTitle.setLayoutX(pane.getMaxWidth() / 2 - 50);
        lblTitle.setLayoutY(10);
        lblTitle.setId("label");

        rbDifficulty1.setToggleGroup(radioGroup);
        rbDifficulty1.setSelected(true);
        rbDifficulty1.setId("radio");
        rbDifficulty1.setLayoutX(pane.getMaxWidth() / 2 - 60);
        rbDifficulty1.setLayoutY(pane.getMaxHeight() / 2 - 255);

        rbDifficulty2.setToggleGroup(radioGroup);
        rbDifficulty2.setId("radio");
        rbDifficulty2.setLayoutX(pane.getMaxWidth() / 2 - 60);
        rbDifficulty2.setLayoutY(pane.getMaxHeight() / 2 - 215);

        rbDifficulty3.setToggleGroup(radioGroup);
        rbDifficulty3.setId("radio");
        rbDifficulty3.setLayoutX(pane.getMaxWidth() / 2 - 60);
        rbDifficulty3.setLayoutY(pane.getMaxHeight() / 2 - 175);

        Image avatar1Image = new Image("file:../../assets/images/avatars/1.png");
        avatar1Circle= new Circle(50);
        avatar1Circle.setFill(new ImagePattern(avatar1Image));
        avatar1Circle.setLayoutX(pane.getMaxWidth() / 2 - 75);
        avatar1Circle.setLayoutY(pane.getMaxHeight() / 2 - 50);

        Image avatar2Image = new Image("file:../../assets/images/avatars/2.png");
        avatar2Circle= new Circle(50);
        avatar2Circle.setFill(new ImagePattern(avatar2Image));
        avatar2Circle.setLayoutX(pane.getMaxWidth() / 2 + 75);
        avatar2Circle.setLayoutY(pane.getMaxHeight() / 2 - 50);

        Image avatar3Image = new Image("file:../../assets/images/avatars/3.png");
        avatar3Circle= new Circle(50);
        avatar3Circle.setFill(new ImagePattern(avatar3Image));
        avatar3Circle.setLayoutX(pane.getMaxWidth() / 2 - 75);
        avatar3Circle.setLayoutY(pane.getMaxHeight() / 2 + 75);

        Image avatar4Image = new Image("file:../../assets/images/avatars/4.png");
        avatar4Circle= new Circle(50);
        avatar4Circle.setFill(new ImagePattern(avatar4Image));
        avatar4Circle.setLayoutX(pane.getMaxWidth() / 2 + 75);
        avatar4Circle.setLayoutY(pane.getMaxHeight() / 2 + 75);

        txtPlayerName = new TextField();
        txtPlayerName.setPromptText("Player Name");
        txtPlayerName.setAlignment(Pos.CENTER);
        txtPlayerName.setPrefSize(200, 50);
        txtPlayerName.setLayoutX(pane.getMaxWidth() / 2 - txtPlayerName.getPrefWidth() / 2);
        txtPlayerName.setLayoutY(pane.getMaxHeight() / 2 + 175);


        Image backIcon  = new Image("file:../../assets/images/buttons/back.png");
        ImageView backIconView = new ImageView(backIcon);
        btnBack = new Button();
        btnBack.setGraphic(backIconView);
        btnBack.setId("mainMenuButton");
        btnBack.setPrefSize(80, 50);
        btnBack.setLayoutX(pane.getMaxWidth() / 2 - 100);
        btnBack.setLayoutY(pane.getMaxHeight() / 2 + 260);

        Image forwardIcon  = new Image("file:../../assets/images/buttons/forward.png");
        ImageView forwardIconView = new ImageView(forwardIcon);
        btnNext = new Button();
        btnNext.setGraphic(forwardIconView);
        btnNext.setId("mainMenuButton");
        btnNext.setPrefSize(80, 50);
        btnNext.setLayoutX(pane.getMaxWidth() / 2 + 20);
        btnNext.setLayoutY(pane.getMaxHeight() / 2 + 260);
        btnNext.setDisable(true);

    }

    private void checkIfNameAndAvatarSelected(){
        if(txtPlayerName.getLength() == 0 || selectedAvatar == null){
            btnNext.setDisable(true);
        }
        else {
            btnNext.setDisable(false);
        }
    }

    private void addComponents() {
        pane.getChildren().addAll(btnNext, btnBack, rbDifficulty1, rbDifficulty2, rbDifficulty3, lblTitle, txtPlayerName, avatar1Circle, avatar2Circle, avatar3Circle, avatar4Circle);
        rootPane.getChildren().add(pane);
    }

    private void addEventHandlers() {
        avatar1Circle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                selectedAvatar = "file:../../assets/images/avatars/1.png";
                avatar2Circle.setStroke(null);
                avatar3Circle.setStroke(null);
                avatar4Circle.setStroke(null);
                avatar1Circle.setStroke(Color.BLACK);
                avatar1Circle.setStrokeWidth(2.0);
                checkIfNameAndAvatarSelected();
            }
        });

        avatar2Circle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                selectedAvatar = "file:../../assets/images/avatars/2.png";
                avatar1Circle.setStroke(null);
                avatar3Circle.setStroke(null);
                avatar4Circle.setStroke(null);
                avatar2Circle.setStroke(Color.BLACK);
                avatar2Circle.setStrokeWidth(2.0);
                checkIfNameAndAvatarSelected();
            }
        });

        avatar3Circle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                selectedAvatar = "file:../../assets/images/avatars/3.png";
                avatar1Circle.setStroke(null);
                avatar2Circle.setStroke(null);
                avatar4Circle.setStroke(null);
                avatar3Circle.setStroke(Color.BLACK);
                avatar3Circle.setStrokeWidth(2.0);
                checkIfNameAndAvatarSelected();
            }
        });

        avatar4Circle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                selectedAvatar = "file:../../assets/images/avatars/4.png";
                avatar1Circle.setStroke(null);
                avatar2Circle.setStroke(null);
                avatar3Circle.setStroke(null);
                avatar4Circle.setStroke(Color.BLACK);
                avatar4Circle.setStrokeWidth(2.0);
                checkIfNameAndAvatarSelected();
            }
        });

        btnNext.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                playerName = txtPlayerName.getText();
                Player player = bubbleDB.checkPlayer(playerName);
                if(player == null){
                    bubbleDB.createPlayer(selectedAvatar, playerName);
                    player = bubbleDB.checkPlayer(playerName);
                }
                removeAllComponents();
                parent.invokeSPController(player);
            }
        });

        btnBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                removeAllComponents();
                parent.invokeMenuController();
            }
        });

        txtPlayerName.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                checkIfNameAndAvatarSelected();
            }
        });
    }

    private void removeAllComponents() {
        rootPane.getChildren().removeAll(pane, background.getBeige(), background.getWood());
    }
}