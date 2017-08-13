package Game.View;

import Game.Controller.GameBoardController;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class WinPane extends Pane{
    private Pane rootPane;
    private Pane pane = this;
    private GameBoardController parent;
    private Button nextLevel, menu;
    private Label label;
    Background background;

    public WinPane(Pane rootPane, GameBoardController parent) {
        this.rootPane = rootPane;
        this.parent = parent;
        initComponents();
        background = new Background(rootPane, 300, 200);
        addComponents();
        eventHandlers();
    }

    private void initComponents() {
        pane.setPrefSize(300, 300);
        pane.setLayoutX(rootPane.getPrefWidth() / 2 - pane.getPrefWidth() / 2);
        pane.setLayoutY(rootPane.getPrefHeight() / 2 - 150);

        nextLevel = new Button("Next level");
        nextLevel.setId("nextLevelButton");
        nextLevel.setPrefSize(125, 50);
        nextLevel.setLayoutX(pane.getPrefWidth() / 2 - nextLevel.getPrefWidth() / 2 + 68);
        nextLevel.setLayoutY(pane.getPrefHeight() / 2 - 25);

        menu = new Button("Menu");
        menu.setPrefSize(125, 50);
        menu.setId("pauseMenuButton");
        menu.setLayoutX(pane.getPrefWidth() / 2 - menu.getPrefWidth() / 2 - 68);
        menu.setLayoutY(pane.getPrefHeight() / 2 - 25);

        label = new Label("You won!");
        label.setId("label");
        label.setLayoutX(pane.getPrefWidth() / 2 - 65);
        label.setLayoutY(pane.getPrefHeight() / 2 - 120);
    }

    private void addComponents() {
        pane.getChildren().addAll(background.getWood(), background.getBeige(), nextLevel, menu, label);
        rootPane.getChildren().add(this);
    }

    private void eventHandlers() {
        menu.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                removeAllComponents();
            }
        });

        nextLevel.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                removeAllComponents();
                parent.nextLevel();
            }
        });
    }

    private void removeAllComponents() {
        rootPane.getChildren().removeAll(this);
    }
}