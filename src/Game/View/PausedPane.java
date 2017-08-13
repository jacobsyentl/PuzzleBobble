package Game.View;

import Game.Controller.GameBoardController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class PausedPane extends Pane{

    private Pane rootPane;
    private Pane pane = this;
    private GameBoardController parent;
    private Button resumeGame, menu;
    private Label label;
    Background background;

    public PausedPane(Pane rootPane, GameBoardController parent) {
        this.parent = parent;
        this.rootPane = rootPane;
        initComponents();
        background = new Background(rootPane, 300, 200);
        addComponents();
        addEventHandlers();
    }

    private void initComponents() {
        pane.setPrefSize(300, 300);
        pane.setLayoutX(rootPane.getPrefWidth() / 2 - pane.getPrefWidth() / 2);
        pane.setLayoutY(rootPane.getPrefHeight() / 2 - 150);

        resumeGame = new Button("Resume");
        resumeGame.setId("pauseMenuButton");
        resumeGame.setPrefSize(125, 50);
        resumeGame.setLayoutX(pane.getPrefWidth() / 2 - resumeGame.getPrefWidth() / 2 + 68);
        resumeGame.setLayoutY(pane.getPrefHeight() / 2 - 25);

        menu = new Button("Menu");
        menu.setPrefSize(125, 50);
        menu.setId("pauseMenuButton");
        menu.setLayoutX(pane.getPrefWidth() / 2 - menu.getPrefWidth() / 2 - 68);
        menu.setLayoutY(pane.getPrefHeight() / 2 - 25);

        label = new Label("Game Paused");
        label.setId("label");
        label.setLayoutX(pane.getPrefWidth() / 2 - 95);
        label.setLayoutY(pane.getPrefHeight() / 2 - 120);
    }

    private void addComponents() {
        pane.getChildren().addAll(background.getWood(), background.getBeige(), resumeGame, menu, label);

        rootPane.getChildren().add(pane);
    }

    private void addEventHandlers() {
        resumeGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                parent.resumeGame();
                removeAllComponents();
            }
        });

        menu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                parent.backToMenu();
                removeAllComponents();
            }
        });
    }

    private void removeAllComponents() {
        rootPane.getChildren().removeAll(pane);
    }
}