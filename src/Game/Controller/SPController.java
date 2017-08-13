package Game.Controller;

import Game.Database.Player;
import Game.View.SinglePlayerPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class SPController extends GameController {
    private int currentLevel = 1;
    private Player player;
    private SinglePlayerPane spPane;

    public SPController(StackPane rootPane, Player player){
        super(rootPane, 1);
        this.gameBoardControllers = new GameBoardController[1];
        this.player = player;
        invokeView();
    }

    private void invokeView() {
        spPane = new SinglePlayerPane(rootPane,this);
    }

    public void invokeGameBoardController(Pane pane){
       gameBoardControllers[0] = new GameBoardController(pane,rootPane,this, 15, 8, 's', player);
    }


    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public void setRemainingBalls(int player, int remainingBalls){
        spPane.setRemainingBalls(player,remainingBalls);
    }

}
