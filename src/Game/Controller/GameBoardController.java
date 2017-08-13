package Game.Controller;

import Game.Database.Bubble;
import Game.Database.BubbleDB;
import Game.Database.Player;
import Game.Logic.*;
import Game.View.GameBoardPane;
import Game.View.PausedPane;
import Game.View.WinPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.*;

public class GameBoardController {
    private StackPane rootPane;
    private boolean gameStatus;
    private int score, shooterOffset, currentLevel = 1;
    private Board board;
    private BubbleDB bubbleDB = BubbleDB.getInstance();
    private Seed seed = new Seed();
    private String levelSeed;
    private Bubble currentBubble, nextBubble;
    private BubbleFactory bubbleFactory;
    private int rows, columns;
    private GameController parent;
    private GameBoardPane gameBoardPane;
    private Pane root;
    private List<Integer> typeBubbles;
    private String currentSeed = "";
    private boolean isAnimationFinished = false;
    private char position;
    private Player player;
    HashMap<String, Integer> lesserChoiceCounter;
    private int colorblindCounter;
    private int remainingBubbles;
    int playerNumber;

    public GameBoardController(){
        this(null,null,null, 15, 8,'s', null);
    }

    public GameBoardController(Pane root,StackPane rootPane, GameController parent, int rows, int columns, char pos, Player player) {
        this.rootPane=rootPane;
        this.rows = rows;
        this.columns = columns;
        this.player = player;
        shooterOffset = 4;
        board = new Board(this.rows, this.columns, shooterOffset, this);
        score = 0;
        this.root = root;
        this.parent = parent;
        bubbleFactory = this.parent.bubbleFactory;
        this.position = pos;
        this.colorblindCounter = 0;
        this.lesserChoiceCounter = new HashMap<String, Integer>();
        this.remainingBubbles = 30;

        BubbleDB bubbleDB = BubbleDB.getInstance();
        levelSeed = bubbleDB.getLevels().get(currentLevel - 1).getLevelSeed();
        invokeView(root, pos);
    }

    private void invokeView(Pane rootPane,char position) {
        if(rootPane!=null){
            this.gameBoardPane = new GameBoardPane(rootPane,this,shooterOffset,position);
        }
    }

    public void invokePausedPane(){
        new PausedPane(root, this);
    }

    public void loadLevel(){
        if(position == 's') {
            String levelSeed = bubbleDB.getLevelFromNumber(currentLevel);
            board.loadLevel(levelSeed);
            typeBubbles = seed.analyseSeed(levelSeed);

        }
        else{
            String levelseed = parent.getRandomSeed();
            board.loadLevel(levelseed);
            typeBubbles = seed.analyseSeed(levelseed);

        }
    }

    public void start(){
        gameStatus = true;
    }

    public void stop(){
        gameStatus = false;
    }

    public boolean isStarted(){
        return gameStatus;
    }

    public boolean isStopped(){
        if(gameStatus) return false;
        else return true;
    }

    public int getRows() {
        return rows;
    }

    public int getScore() { return score; }

    public Board getBoard(){return board;}

    public void checkEmpty(char position){
        if (board.isBoardEmpty()){
            parent.setGamePaused(true);
            if(position == 's'){
                new WinPane(root, this);
                writeScore();
            }
        }
    }

    public Hashtable<String, Object> shoot(double thetaFromPointerInRadians) {

        int currentRow = 0;

        LogicCoord currentPositionOfProjectile = new LogicCoord(0,-shooterOffset);
        LogicCoord lastPositionOfProjectileConfirmedToFitInBoard = currentPositionOfProjectile;

        currentPositionOfProjectile = board.getLogicCoordOfCollisionWithBottomOfLogicRow(thetaFromPointerInRadians, currentRow, currentPositionOfProjectile.convertToAspectLogicCoord(shooterOffset));

        Hashtable<String, Object> result = new Hashtable<>();
        ArrayList<VisualCoord> queueProjectile = new ArrayList<>();

        do {
            while (board.containsXOf(currentPositionOfProjectile) && currentRow < board.getNumberOfRows()) {
                lastPositionOfProjectileConfirmedToFitInBoard = currentPositionOfProjectile;

                ArrayList<LogicCoord> potentialCoordsOfCollision = board.getBubblesWithinRangeOfProjectile(currentPositionOfProjectile, thetaFromPointerInRadians);

                potentialCoordsOfCollision = board.filterLogicCoordsByBallsOnBoard(potentialCoordsOfCollision);

                ArrayList<LogicCoord> collisions = board.getCollisions(potentialCoordsOfCollision, currentPositionOfProjectile, thetaFromPointerInRadians);

                if (collisions.size() > 0){
                    result = board.getCollisionResult(collisions, thetaFromPointerInRadians, queueProjectile, currentPositionOfProjectile);
                    if (result != null) {
                        if (board.isBoardEmpty()) {
                            setGamePaused(true);
                        }
                        int score = 0;
                        int multiplier =0;
                        if (result.containsKey("bubblesInCluster")) {
                            ArrayList<Bubble> bubblesInCluster = (ArrayList) result.get("bubblesInCluster");
                            for (Bubble bubble : bubblesInCluster) {
                                score += bubble.getBubbleScore();
                                multiplier++;
                            }
                        }

                        if (result.containsKey("bubblesInClusterToDrop")) {

                            ArrayList<Bubble> bubblesInClusterToDrop = (ArrayList) result.get("bubblesInClusterToDrop");
                            for (Bubble bubble : bubblesInClusterToDrop) {
                                score += bubble.getBubbleScore();
                                multiplier++;
                            }
                        }
                        incrementScore(score * multiplier);

                        return result;
                    } else {
                        return null;
                    }
                }

                currentRow++;
                currentPositionOfProjectile = board.getLogicCoordOfCollisionWithBottomOfLogicRow(thetaFromPointerInRadians, currentRow, currentPositionOfProjectile.convertToAspectLogicCoord(shooterOffset));
            }

            currentPositionOfProjectile = board.getCoordOfCollisionWithEdge(thetaFromPointerInRadians, lastPositionOfProjectileConfirmedToFitInBoard);

            queueProjectile.add(currentPositionOfProjectile.convertToVisualCoord(board.getNumberOfRows(), board.getNumberOfColumns()));

            thetaFromPointerInRadians *= -1;

        } while (currentPositionOfProjectile.getY() < rows*2);

        queueProjectile.remove(queueProjectile.size()-1);

        LogicCoord collisionWithTop = board.getLogicCoordOfCollisionWithBottomOfLogicRow(thetaFromPointerInRadians*-1,rows,lastPositionOfProjectileConfirmedToFitInBoard.convertToAspectLogicCoord(shooterOffset));
        LogicCoord snapPosition = board.getSnapPositionOf(collisionWithTop ,thetaFromPointerInRadians);

        board.setBubbleAt(snapPosition, currentBubble);
        Hashtable<Bubble, Integer> tree = board.hitResultTree(board.getBubbleAt(snapPosition));


        ArrayList<Bubble> bubblesInCluster = board.filterHashtableToCluster(tree);
        if (bubblesInCluster.size() >= 3 ||  board.clusterContainsBubbleWithName(bubblesInCluster, "Rainbow") || board.clusterContainsBubbleWithName(bubblesInCluster, "Bomb") || board.clusterContainsBubbleWithName(bubblesInCluster, "More chances")  || board.clusterContainsBubbleWithName(bubblesInCluster, "Color Blind") || board.clusterContainsBubbleWithName(bubblesInCluster, "Less Choice")) {
            board.removeClusterFromBoard(bubblesInCluster);
        } else bubblesInCluster = new ArrayList<>();
        ArrayList<Bubble> bubblesUnderClusterToDrop = board.getBallsUnderClusterToDrop(bubblesInCluster);
        board.removeClusterFromBoard(bubblesUnderClusterToDrop);

        queueProjectile.add(board.getSnapPositionOf(board.getLogicCoordOfCollisionWithBottomOfLogicRow(thetaFromPointerInRadians * -1, rows, lastPositionOfProjectileConfirmedToFitInBoard.convertToAspectLogicCoord(shooterOffset)), thetaFromPointerInRadians).convertToVisualCoord(board.getNumberOfRows(), board.getNumberOfColumns()));
        result.put("projectile", queueProjectile);
        result.put("bubblesInCluster", bubblesInCluster);
        result.put("bubblesUnderCluster", bubblesUnderClusterToDrop);

        if (board.isBoardEmpty()) {
            setGamePaused(true);
        }

        return result;
    }

    private void incrementScore (int increment) {
        setScore(this.getScore()+increment);
        gameBoardPane.setScore(this.getScore());
    }

    public int randomBubble(){
        int length = typeBubbles.size();
        Random rand = new Random();
        int randomBubble = typeBubbles.get(rand.nextInt(length));
        return randomBubble;
    }

    public void nextMove(){
        typeBubbles = null;
        remainingBubbles--;


        Bubble[][] gameBoard = board.getBoard();
        currentSeed = "";

        for(int i = 0; i < gameBoard.length; i++){
            for(int j = 0; j < gameBoard[i].length; j++){
                if(gameBoard[i][j] != null){
                    if (!lesserChoiceCounter.containsKey(gameBoard[i][j].getBubbleName())) {
                        currentSeed += gameBoard[i][j].getBubbleGameId();
                    }
                }
            }
        }
        typeBubbles = seed.analyseSeed(currentSeed);
    }

    public Bubble generateBubble(){

        Bubble bubble = bubbleFactory.createBubble(randomBubble());

        return bubble;
    }

    public void generateFirstBubbles(){

        DataCoord positionCurrentBubble = new LogicCoord(0,-shooterOffset).convertToDataCoord(rows, columns);

        DataCoord positionNextBubble = new LogicCoord(-6,-shooterOffset).convertToDataCoord(rows, columns);

        currentBubble = generateBubble();
        currentBubble.setDataCoord(positionCurrentBubble);

        nextBubble = generateBubble();
        nextBubble.setDataCoord(positionNextBubble);
    }

    public void nextBubbleIsCurrentBubble(){
        currentBubble = nextBubble;
        DataCoord positionCurrentBubble = new LogicCoord(0,-shooterOffset).convertToDataCoord(rows, columns);
        currentBubble.setDataCoord(positionCurrentBubble);

        nextBubble = generateBubble();
        DataCoord positionNextBubble = new LogicCoord(-6,-shooterOffset).convertToDataCoord(rows, columns);
        nextBubble.setDataCoord(positionNextBubble);
    }

    public Bubble getCurrentBubble() {
        return currentBubble;
    }

    public Bubble getNextBubble() {
        return nextBubble;
    }

    public void setGamePaused(boolean gamePaused) {
        parent.setGamePaused(gamePaused);
    }

    public boolean getGamePaused(){
        return parent.isGamePaused();
    }

    public int getCurrentLevel(){
        return currentLevel;
    }

    public void nextLevel(){
        board.clearBoard();
        currentLevel += 1;
        String levelSeed = bubbleDB.getLevelFromNumber(currentLevel);
        board.loadLevel(levelSeed);
        typeBubbles = seed.analyseSeed(levelSeed);

        board.clearBoard();

        loadLevel();
        gameBoardPane.setVisualBubbles(board.getBoard());
        gameBoardPane.fillRowsWithBalls();
        generateFirstBubbles();
        gameBoardPane.showBubbles();
        gameBoardPane.drawBubbles();
        parent.setGamePaused(false);
        score = 0;
    }

    public void resumeGame(){
        parent.setGamePaused(false);
    }

    public boolean getIsAnimationFinished(){
        return isAnimationFinished;
    }

    public void setIsAnimationFinished(boolean isAnimationFinished){
        this.isAnimationFinished = isAnimationFinished;
    }

    public void leftKeyPressed() {
        if (!parent.isGamePaused()) {
            gameBoardPane.rotate(20);
        }
    }

    public void leftRightKeyReleased() {
        if (!parent.isGamePaused()) {
            gameBoardPane.stopRotation();
        }
    }

    private void reduceAllLesserChoiceCounts() {
        Set<String> treeKeys = lesserChoiceCounter.keySet();
        Iterator<String> iterator = treeKeys.iterator();
        String key;

        while (iterator.hasNext()) {
            key = iterator.next();

            if (lesserChoiceCounter.get(key) != 0) {
                lesserChoiceCounter.put(key, lesserChoiceCounter.get(key)-1);
            }
            if (lesserChoiceCounter.get(key) == 0) {
                iterator.remove();
            }
        }
    }

    public void upKeyReleased() {
        if (!parent.isGamePaused()) {

            if (0 < colorblindCounter ) {
                colorblindCounter--;
            }

            board.makeAllBallsGray(false);
            reduceAllLesserChoiceCounts();

            double angle = gameBoardPane.getAngleInRadians();
            Hashtable<String, Object> interactionBubbles = shoot(angle);
            if (interactionBubbles != null) {
                if (!((ArrayList) interactionBubbles.get("projectile")).isEmpty()) {
                    gameBoardPane.bubbleProjectileAnimation(interactionBubbles);
                }
            } else {
                System.out.println("Game over");
            }


            nextBubbleIsCurrentBubble();
            nextMove();
            gameBoardPane.showBubbles();
        }
    }

    public void rightKeyPressed() {
        if (!parent.isGamePaused()) {
            gameBoardPane.rotate(160);
        }
    }

    public String getPlayerName() {
        return player.getPlayerName();

    }

    public void setScore(int score) {
        this.score = score;
    }

    private void writeScore(){
        int scoreFromDB = bubbleDB.checkIfPlayerHasLevelScore(player.getPlayerId(), currentLevel);
        if(scoreFromDB >= 0){
            if(score > scoreFromDB){
                bubbleDB.updateHighscore(player.getPlayerId(), currentLevel, score);
            }
        }else{
            bubbleDB.writeScore(player.getPlayerId(), currentLevel, score);
        }
    }

    public void setLesserChoiceCounter(String bubbleName) {
        lesserChoiceCounter.put(bubbleName, 5);
    }

    public void setColorblindCounter(int count) {
        this.colorblindCounter = count;
        board.makeAllBallsGray(true);
    }

    public void incrementRemainingBubbles(int i) {
        this.remainingBubbles += 5;
    }

    public BubbleFactory getBubbleFactory() {
        return bubbleFactory;
    }

    public void backToMenu() {
        new MenuController(rootPane);
//        parent.removeEventHandlers();
        gameBoardPane.removeAllComponents();
    }


}