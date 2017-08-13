package Game.Controller;

import Game.Logic.BubbleFactory;
import Game.View.GamePane;
import javafx.scene.layout.StackPane;

public abstract class GameController {
    protected StackPane rootPane;
    BubbleFactory bubbleFactory;
    boolean isWon;
    protected int[] score;
    protected GameBoardController[] gameBoardControllers;
    protected GamePane pane;
    protected int numberOfPlayers;
    private boolean gamePaused = false;

    public GameController(StackPane rootPane, int numberOfPlayers){
        this.rootPane = rootPane;
        bubbleFactory = new BubbleFactory();
        isWon = false;
        this.numberOfPlayers = numberOfPlayers;
        gameBoardControllers = new GameBoardController[this.numberOfPlayers];
    }

    public boolean isGamePaused() {
        return gamePaused;
    }

    public void setGameWon(boolean isWon) {
        this.isWon = isWon;
    }

    public void setGamePaused(boolean gamePaused) {
        this.gamePaused = gamePaused;
    }

    public String getPlayerName(int player) {
        return gameBoardControllers[player].getPlayerName();
    }

    public String generateRandomLevel() {
        return null;
    }

    public String getRandomSeed(){
        return null;
    }

    public void updateScore(int player, int score) {
        pane.setScore(player, score);
    }

    public void leftKeyPressed(int player) {
        gameBoardControllers[player].leftKeyPressed();
    }

    public void rightKeyPressed(int player) {
        gameBoardControllers[player].rightKeyPressed();

    }

    public void leftRightKeyReleased(int player) {
        gameBoardControllers[player].leftRightKeyReleased();
    }

    public void upKeyReleased(int player) {
        gameBoardControllers[player].upKeyReleased();
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public int getPlayerScore(int player) {
        return gameBoardControllers[player].getScore();
    }

    public void removeEventHandlers() {
        pane.removeAllComponents();
    }
}