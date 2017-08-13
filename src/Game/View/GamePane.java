package Game.View;

import Game.Controller.GameController;
import Game.Controller.SPController;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class GamePane extends Pane {
    protected StackPane rootPane;
    protected Pane pane;
    protected GameController parent;
    protected Label[] playerNames, playerScores;

    public GamePane( StackPane rootPane,SPController parent){
        this.rootPane = rootPane;
        this.parent = parent;
        this.pane = this;

        this.playerNames = new Label[parent.getNumberOfPlayers()];
        this.playerScores = new Label[parent.getNumberOfPlayers()];

        this.setFocusTraversable(true);
        initComponents();
        makeBackground();
        eventHandlers();
        addComponents();
        parent.invokeGameBoardController(pane);

    }

    protected void eventHandlers() {

        pane.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode keyCode = event.getCode();

                if (keyCode == KeyCode.LEFT) {
                    parent.leftKeyPressed(0);
                }
                if (keyCode == KeyCode.RIGHT) {
                    parent.rightKeyPressed(0);
                }

                event.consume();
            }
        });
        pane.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode keyCode = event.getCode();

                if (keyCode == KeyCode.LEFT || keyCode == KeyCode.RIGHT ) {
                    parent.leftRightKeyReleased(0);
                }
                if (keyCode == KeyCode.UP) {
                    parent.upKeyReleased(0);
                }

                event.consume();
            }
        });
    }

    private void makeBackground() {

    }

    protected void initComponents() {
    }

    private void addComponents() {
        rootPane.getChildren().add(pane);
    }

    public void setScore(int player, int score) {
        this.playerScores[player].setText(Integer.toString(score));
    }

    public void removeAllComponents() {
        System.out.println("test");
    }
}