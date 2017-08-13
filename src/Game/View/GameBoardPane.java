package Game.View;

import Game.Controller.GameBoardController;
import Game.Database.Bubble;
import Game.Logic.*;
import com.sun.javafx.geom.Point2D;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Hashtable;

public class GameBoardPane extends Pane{
    private Pane rootPane;
    private GameBoardController parent;
    private RotateTransition bubbleTraject;
    private Line pointer;
    private Button btn;
    private int rows;
    private Pane pane = this;
    private double ballDiameter = 40;
    private double ballRadius = ballDiameter /2;
    private double ballShift = Math.sqrt(3)/2;
    private int shooterOffset;
    private Bubble currentBubble, nextBubble;
    private Bubble[][] visualBubbles;
    private Button btnPause;
    private char position;
    private boolean checked = false;
    private Point2D boardStartCoord;
    Label playerName;
    Label playerScore;

    protected Line border;

    public GameBoardPane(Pane rootPane, GameBoardController parent, int shooterOffset, char position){
        this.boardStartCoord= new Point2D(15,60);
        this.rootPane =rootPane;
        this.parent = parent;
        this.shooterOffset = shooterOffset;
        this.rows = parent.getRows();
        this.visualBubbles = parent.getBoard().getBoard();
        this.position = position;
        this.setFocusTraversable(true);

        initComponents();
        if(position == 'r'){
            pane.setLayoutX(350);
        }else if(position == 's'){
            pane.setLayoutX(-15);
        }
        new Background(this, 350, 690);

        pane.setLayoutY(-27);

        loadLevel();
        parent.generateFirstBubbles();

        showBubbles();
        drawBubbles();
        setInitialValues();
        addComponents();

        if(position == 's'){
            addEventHandlers();
        }
    }

    private void setInitialValues() {
        this.playerName = new Label();
        this.playerScore = new Label();
        this.playerScore.setTextAlignment(TextAlignment.RIGHT);

        if (position == 's') {
            this.playerName.setText(parent.getPlayerName());
        }
            this.playerScore.setText(Integer.toString(parent.getScore()));
        playerName.setLayoutX(25);
        playerName.setLayoutY(10);
        playerScore.setLayoutX(270);
        playerScore.setLayoutY(10);
        playerName.setId("gamePaneText");
        playerScore.setId("gamePaneText");
    }

    private void addEventHandlers() {
        btnPause.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!parent.getGamePaused()){
                    parent.setGamePaused(true);
                    parent.invokePausedPane();
                }
            }
        });
    }

    private void loadLevel() {
        parent.loadLevel();
        fillRowsWithBalls();
    }

    private void initComponents() {
        double pointerLength =100;
        bubbleTraject = new RotateTransition();

        double rowHeight = Math.sqrt(3)/2*ballRadius;

        pane.setPrefSize(8 * ballDiameter + boardStartCoord.x*2, ((15 * 2 + shooterOffset) * rowHeight+100) + boardStartCoord.y);
        pane.setMaxSize(8 * ballDiameter, ((15 * 2 + shooterOffset) * rowHeight));
        pane.setId("container");

        pointer = new Line(0.0,0.0,0.0,0.0);
        pointer.setStroke(Color.valueOf("#090A3A"));
        pointer.setEndX(pointerLength);
        pointer.setLayoutX((ballDiameter * 8 - pointerLength) / 2  + boardStartCoord.x);
        pointer.setLayoutY(rowHeight * (rows * 2 + shooterOffset) + boardStartCoord.y);
        pointer.setRotate(90.0);
        pointer.setStrokeWidth(4.0);

        border = new Line(0, 50, 320, 50);
        border.setLayoutX(15);

        border.getStrokeDashArray().addAll(2.0);

        btnPause = new Button("II");
        btnPause.setId("btnPause");
        btnPause.setLayoutX(555);
        btnPause.setLayoutY(0);
        btnPause.setPrefSize(40, 40);
    }

    private void addComponents() {
        pane.getChildren().addAll(pointer, btnPause, playerName, playerScore, border);
        rootPane.getChildren().add(this);
    }

    public void fillRowsWithBalls() {
        for (int i = 0; i < visualBubbles.length; i++) {
            double xShift = 0;
            if(i%2!=0) {
                xShift = ballRadius;
            }
            for (int j = 0; j < visualBubbles[i].length; j++) {
                Bubble bubble =visualBubbles[i][j];
                if(bubble != null) {
                    bubble.setCenterX(ballRadius + (ballDiameter * j) + xShift + boardStartCoord.x );
                    bubble.setCenterY(ballRadius + (ballDiameter * i) * ballShift + boardStartCoord.y);
                    bubble.setRadius(ballRadius);
                    pane.getChildren().add(bubble);
                }
            }
        }
    }

    public void rotate(int rotateDirection){
        bubbleTraject.setDuration(Duration.seconds(1));
        bubbleTraject.setInterpolator(Interpolator.LINEAR);
        bubbleTraject.setNode(pointer);
        bubbleTraject.setToAngle(rotateDirection);
        bubbleTraject.setCycleCount(1);
        bubbleTraject.setAutoReverse(true);
        bubbleTraject.play();
    }

    public void stopRotation(){
        bubbleTraject.stop();
    }

    public double getAngleInRadians() {
        return (pointer.getRotate() - 90) / 180 * Math.PI;
    }

    private void shootBubble(double angle) {
        Hashtable<String, Object> interactionBubbles = parent.shoot(angle);
        if (interactionBubbles != null) {
            if (!((ArrayList) interactionBubbles.get("projectile")).isEmpty()) {
                bubbleProjectileAnimation(interactionBubbles);
            }
        }
    }

    public void bubbleProjectileAnimation(Hashtable<String,Object> interactionBubbles){
        ArrayList<VisualCoord> coords = (ArrayList)interactionBubbles.get("projectile");
        Path path = new Path();
        path.getElements().add(new MoveTo(currentBubble.getCenterX(), currentBubble.getCenterY()));
        for (VisualCoord coord : coords) {
            path.getElements().add(new LineTo(coord.getX()*ballRadius+boardStartCoord.x, ballRadius+coord.getY()*ballRadius*ballShift + boardStartCoord.y));
        }
        Bubble projectile = currentBubble;
        projectile.setCenterX(coords.get(coords.size() - 1).getX() * ballRadius + boardStartCoord.x);
        projectile.setCenterY(coords.get(coords.size() - 1).getY()*ballRadius*ballShift+ballRadius + boardStartCoord.y);
        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.seconds(1));
        pathTransition.setPath(path);
        pathTransition.setNode(projectile);
        pathTransition.play();

        pathTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!((ArrayList) interactionBubbles.get("bubblesInCluster")).isEmpty()){
                    fallingBubblesAnimation(interactionBubbles);
                }
            }
        });
    }

    private void fallingBubblesAnimation(Hashtable<String, Object> interactionBubbles) {
        ArrayList<Bubble> bubblesInCluster = (ArrayList)interactionBubbles.get("bubblesInCluster");
        ArrayList<Bubble> bubblesUnderCluster = (ArrayList)interactionBubbles.get("bubblesUnderCluster");
        for (int i = 0; i < bubblesInCluster.size(); i++) {
            bubbleFallingAnimation(bubblesInCluster.get(i));
        }

        if (bubblesUnderCluster != null) {
            for (int i = 0; i < bubblesUnderCluster.size(); i++) {
                bubbleFallingAnimation(bubblesUnderCluster.get(i));
                //parent.setIsAnimationFinished(true);
            }
        }
        checked = false;
    }

    private void bubbleFallingAnimation(Bubble bubble) {
        Path path = new Path();
        path.getElements().add(new MoveTo(bubble.getCenterX(), bubble.getCenterY()));

        path.getElements().add(new LineTo(bubble.getCenterX(), pane.getHeight() + 500));

        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.seconds(2));
            pathTransition.setPath(path);
            pathTransition.setNode(bubble);
            pathTransition.play();

            pathTransition.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if(!checked){
                        checked = true;
                        parent.checkEmpty(position);

                    }
                    pane.getChildren().remove(bubble);
                }
            });
    }

    public void showBubbles(){
        currentBubble = parent.getCurrentBubble();
        nextBubble = parent.getNextBubble();
        currentBubble.setCenterX(currentBubble.getDataCoord().getX()*ballDiameter+ballDiameter+boardStartCoord.x);
        currentBubble.setCenterY(currentBubble.getDataCoord().getY()*ballDiameter*ballShift+ballDiameter+boardStartCoord.y);
        currentBubble.setRadius(ballRadius);
        nextBubble.setCenterX(nextBubble.getDataCoord().getX()*ballDiameter+ballDiameter+boardStartCoord.x);
        nextBubble.setCenterY(nextBubble.getDataCoord().getY()*ballDiameter*ballShift+ballDiameter+boardStartCoord.y);
        nextBubble.setRadius(ballRadius);
        pane.getChildren().add(nextBubble);
    }

    public void drawBubbles(){
        pane.getChildren().add(currentBubble);
    }

    public void setVisualBubbles(Bubble[][] board) {
        this.visualBubbles = board;
    }

    private void makeBackground() {
        Rectangle wood, beige, mountain;

        wood = new Rectangle(350,690);
        beige = new Rectangle(320,670);
        mountain = new Rectangle(250,120);

        mountain.setLayoutX(70);
        mountain.setLayoutY(535);
        wood.setArcHeight(20.0);
        wood.setArcWidth(20.0);
        beige.setArcHeight(20.0);
        beige.setArcWidth(20.0);
        beige.setLayoutX(15.0);
        beige.setLayoutY(10.0);
        Image background = new Image("file:../../assets/images/wood_pattern.jpg");
        Image mountainImage = new Image("file:../../assets/images/hill.png");
        wood.setFill(new ImagePattern(background));
        mountain.setFill(new ImagePattern(mountainImage));

        beige.setFill(Paint.valueOf("#F6EDAE"));
        rootPane.getChildren().addAll(wood, beige);
    }

    public void setScore(int score) {
        this.playerScore.setText(Integer.toString(score));

    }

    public void removeAllComponents(){
        rootPane.getChildren().removeAll(this);
    }
}