package Game.View;

import Game.Controller.MPController;
import Game.Database.Player;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;

import java.util.HashMap;
import java.util.Map;

public class MultiPlayerPane extends Pane{
    private MPController  parent;
    private StackPane rootPane;
    private Pane pane;
    private Line hBorder;
    private Line vBorder;

    public MultiPlayerPane(MPController parent, StackPane rootPane) {
        this.parent = parent;
        this.rootPane = rootPane;
        pane = this;
        initComponents();
        addComponents();
        eventHandlers();
        parent.invokeGameBoardController(this);
    }

    private void initComponents() {
        pane.setMaxSize(640, 640);
        pane.setPrefSize(640, 640);
        hBorder = new Line(0, 20, 640, 20);
        vBorder = new Line(320,20,320,660 );

        hBorder.getStrokeDashArray().addAll(2.0);
        vBorder.getStrokeDashArray().addAll(2.0);
    }

    private void addComponents() {
        pane.getChildren().addAll(hBorder,vBorder);
        rootPane.getChildren().add(pane);
    }

    private void eventHandlers() {
        pane.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode keyCode = event.getCode();

                if (keyCode == KeyCode.Q) {
                    parent.leftKeyPressed(0);
                }
                if (keyCode == KeyCode.D) {
                    parent.rightKeyPressed(0);
                }

                if (keyCode == KeyCode.LEFT) {
                    parent.leftKeyPressed(1);
                }
                if (keyCode == KeyCode.RIGHT) {
                    parent.rightKeyPressed(1);
                }

                event.consume();
            }
        });
        pane.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode keyCode = event.getCode();

                if (keyCode == KeyCode.Q || keyCode == KeyCode.D ) {
                    parent.leftRightKeyReleased(0);
                }
                if (keyCode == KeyCode.Z) {
                    parent.upKeyReleased(0);
                }
                if (keyCode == KeyCode.LEFT || keyCode == KeyCode.RIGHT ) {
                    parent.leftRightKeyReleased(1);
                }
                if (keyCode == KeyCode.UP) {
                    parent.upKeyReleased(1);
                }

                event.consume();
            }
        });
    }

    public void displayActivePowerBubbles(HashMap<String,Integer> powerBubbles, Player player){
        for (Map.Entry<String,Integer> entry: powerBubbles.entrySet()){
            String key = entry.getKey();
            int value = entry.getValue();

            Label label = new Label(key+": "+value);
            label.setLayoutX(10);
            this.getChildren().add(label);
        }
    }

    public void setRemainingBalls(int player,int remainingBalls) {
        Label label =new Label(player+": "+remainingBalls);
        label.setLayoutX(50);
        this.getChildren().add(label);
    }
}