package Game.Controller;

import Game.View.MultiPlayerPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.Random;

public class MPController extends GameController {
    private StackPane rootPane;
    private int score;
    private String name;
    private GameBoardController leftGameBoardController;
    private GameBoardController rightGameBoardController;
    private String randomSeed;
    private MultiPlayerPane mpPane;

    public MPController(StackPane rootPane){
        super(rootPane, 2);
        this.rootPane = rootPane;
        gameBoardControllers = new GameBoardController[2];
        invokeView();
    }

    private void invokeView() {
      mpPane = new MultiPlayerPane(this, rootPane);
    }


    public void invokeGameBoardController(Pane mpPane) {
        randomSeed = generateRandomLevel();
        gameBoardControllers[0] = new GameBoardController(mpPane,rootPane, this, 15, 8, 'l', null);
        gameBoardControllers[1] = new GameBoardController(mpPane,rootPane, this, 15, 8, 'r', null);
    }


    public StackPane getStackPane(){
        return rootPane;
    }

    @Override
    public String generateRandomLevel() {
        StringBuilder seed = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 30; i++) {
            seed.append( random.nextInt(7 - 1) + 1);
        }
        return seed.toString();
    }

    @Override
    public String getRandomSeed() {
        return randomSeed;
    }

    public void setRemainingBalls(int player, int remainingBalls){
        mpPane.setRemainingBalls(player,remainingBalls);
    }
}
