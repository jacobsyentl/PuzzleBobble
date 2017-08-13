import Game.Controller.GameBoardController;
import Game.Database.Bubble;
import Game.Logic.*;
import javafx.geometry.Point2D;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Hashtable;

import static org.junit.Assert.*;


public class GameTests { /*
    private GameBoardController gameBoardController;

    @Before
    public void intializers() {
        gameBoardController = new GameBoardController();
    }

    @Test
    public void testGameIsStarted() {
        gameBoardController.start();
        Boolean isStarted = gameBoardController.isStarted();

        assertEquals(true, isStarted);
    }

    @Test
    public void testGameIsStopped() {
        gameBoardController.stop();
        Boolean isStopped = gameBoardController.isStopped();

        assertEquals(true, isStopped);
    }


    @Test
    public void testMakeBoard() {
        int rows = 15;
        int columns = 8;
        Boolean rowNumberIsEven;
        int[][] expected = new int[rows][];

        for (int i = 0; i < rows; i++) {
            rowNumberIsEven = (i % 2 == 0);
            if (rowNumberIsEven) {
                expected[i] = new int[columns];
            } else {
                expected[i] = new int[columns - 1];
            }
        }

        Board board = new Board(rows, columns, 10, new GameBoardController());
        Bubble[][] actual = board.getBoard();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testConvertSeedToIntArray() {
        Seed seed = new Seed();
        BubbleFactory bubbleFactory = new BubbleFactory();
        Bubble bubbleOne = bubbleFactory.createBubble(1);
        Bubble bubbleTwo = bubbleFactory.createBubble(2);
        Bubble[] result = seed.convertToBubbleArray("111111112222222111111112222222");
        Bubble[] expected = {bubbleOne, bubbleOne, bubbleOne, bubbleOne, bubbleOne, bubbleOne, bubbleOne, bubbleOne, bubbleTwo, bubbleTwo, bubbleTwo, bubbleTwo, bubbleTwo, bubbleTwo, bubbleTwo, bubbleOne, bubbleOne, bubbleOne, bubbleOne, bubbleOne, bubbleOne, bubbleOne, bubbleOne, bubbleTwo, bubbleTwo, bubbleTwo, bubbleTwo, bubbleTwo, bubbleTwo, bubbleTwo};

        assertArrayEquals(expected, result);
    }
    @Test
    public void testFillBoard() {
        int rows = 15;
        int columns = 8;

        BubbleFactory bubbleFactory = new BubbleFactory();
        Bubble bubbleOne = bubbleFactory.createBubble(1);
        Bubble bubbleTwo = bubbleFactory.createBubble(2);
        Bubble bubbleThree = bubbleFactory.createBubble(3);
        Bubble bubbleFour = bubbleFactory.createBubble(4);
        Bubble bubbleFive = bubbleFactory.createBubble(5);

        Board expectedBoard = new Board(rows, columns, 10, new GameBoardController());
        Bubble[][] expected = expectedBoard.getBoard();

        for (int j = 0; j < 4; j++) {
            expected[0][j] = bubbleThree;
        }
        for (int j = 4; j < 8; j++) {
            expected[0][j] = bubbleTwo;
        }
        for (int j = 0; j < expected[1].length; j++) {
            expected[1][j] = null;
        }
        for (int j = 0; j < expected[2].length; j++) {
            expected[2][j] = bubbleOne;
        }
        for (int j = 0; j < 4; j++) {
            expected[3][j] = bubbleTwo;
        }
        expected[1][3] = bubbleThree;
        expected[4][0] = bubbleFive;

        Board board = new Board(rows, columns, 10, new GameBoardController());
        board.loadLevel("3333222200030001111111122220005");
        Bubble actual[][] = board.getBoard();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testCollisionPoint() {
        Board board = new Board(15, 8, 10, new GameBoardController());
        Point2D result = board.getCollisionPoint(1, 4, 6, 1);
        assertEquals(new Point2D(3, 4), result);
    }

    @Test
    public void testLogicCoordToVisualCoord(){
        int rows = 15;
        int columns = 8;
        Board board = new Board(rows, columns, 10, new GameBoardController());

        Point2D logicCoord1 = new Point2D(-3,3);
        Point2D visualCoordLogicAxis1 = board.convertLogicCoordLogicAxisToVisualCoordLogicAxis(logicCoord1);
        Point2D excpectedVisualCoord1 = new Point2D(5,26);
        Point2D visualCoordVisualAxis1 = board.switchSignOfVerticalAxis(visualCoordLogicAxis1);

        Point2D logicCoord2 = new Point2D(-8,29);
        Point2D visualCoordLogicAxis2 = board.convertLogicCoordLogicAxisToVisualCoordLogicAxis(logicCoord2);
        Point2D excpectedVisualCoord2 = new Point2D(0,0);
        Point2D visualCoordVisualAxis2 = board.switchSignOfVerticalAxis(visualCoordLogicAxis2);

        assertEquals(excpectedVisualCoord1.getX(),visualCoordVisualAxis1.getX(),0);
        assertEquals(excpectedVisualCoord1.getY(),visualCoordVisualAxis1.getY(),0);

        assertEquals(excpectedVisualCoord2.getX(),visualCoordVisualAxis2.getX(),0);
        assertEquals(excpectedVisualCoord2.getY(),visualCoordVisualAxis2.getY(),0);
    }

    @Test
    public void testDistanceBetweenTwoCenters() {
        // FOR LUCAS

        Board board = new Board(15, 8, 10, new GameBoardController());
        Point2D center1 = new Point2D(0, 0);
        Point2D center2 = new Point2D(5, 12);
        double result = board.getDistanceBetweenCenters(center1, center2);
        assertEquals(13, result, 0.01);
    }

    @Test
    public void testGetCoordOfCollisionWithEdge() {
        // FOR LUCAS
        Board board = new Board(15, 8, 10, new GameBoardController());
        int theta = 45;
        LogicCoord collisionCoord = board.getCoordOfCollisionWithEdge(theta,new LogicCoord(0,0));

        assertEquals(new LogicCoord(8,8), collisionCoord);

        theta = -45;
        collisionCoord = board.getCoordOfCollisionWithEdge(theta, new LogicCoord(0,0));

        assertEquals(new Point2D(-8,8), collisionCoord);
    }

    @Test
    public void testGetScore() {
        assertEquals(0, gameBoardController.getScore());
    }

    @Test
    public void testLogicCoord() {

        LogicCoord logicCoord = new LogicCoord(0,0);

        assertEquals(logicCoord.getX(), 0, 0);
        assertEquals (logicCoord.getY(), 0, 0);

        logicCoord = new LogicCoord(1,4);

        assertEquals(logicCoord.getX(),1,0);
        assertEquals(logicCoord.getY(),4,0);
    }

    @Test
    public void testVisualCoord() {

        VisualCoord visualCoord = new VisualCoord(0,0);

        assertEquals (visualCoord.getX(), 0, 0);
        assertEquals (visualCoord.getY(), 0, 0);

        visualCoord = new VisualCoord(1,4);

        assertEquals(visualCoord.getX(), 1,0);
        assertEquals(visualCoord.getY(), 4,0);
    }

    @Test
    public void testTree() {
        Hashtable<LogicCoord, Integer> hashWithTreeScoreResults = new Hashtable<LogicCoord, Integer>();
        LogicCoord bubbleThatHasBeenHit = new LogicCoord(5,5);
        Board board = new Board(15,8);

        hashWithTreeScoreResults = board.HitResultTree(bubbleThatHasBeenHit);

        assertEquals(hashWithTreeScoreResults.size(), 7);
    }


    @Test
    public void testGetLowestNonEmptyRow() {

        Board board = new Board(15, 8, 10, new GameBoardController());
        LogicCoord coord = new LogicCoord(2, 29);
        BubbleFactory bubbleFactory = new BubbleFactory();
        Bubble bubbleOne = bubbleFactory.createBubble(1);

        board.setBubbleAt(coord, bubbleOne);
        int lowestNonEmptyRow = board.getLowestNonEmptyRow();

        assertEquals(0, lowestNonEmptyRow);

        coord = new LogicCoord(2, 0);

        board.setBubbleAt(coord, bubbleOne);
        lowestNonEmptyRow = board.getLowestNonEmptyRow();

        assertEquals(14, lowestNonEmptyRow);
    }

    @Test
    public void testConvertDataCoordToLogicCoord() {
        int rows = 15;
        int columns = 8;

        Board board = new Board(rows, columns, 10, new GameBoardController());

        DataCoord start = new DataCoord(2,4);

        LogicCoord result = start.convertToLogicCoord(rows, columns);
        assertEquals(new LogicCoord(1, 25), result);


        start = new DataCoord(0,0);
        result = start.convertToLogicCoord(rows, columns);

        assertEquals(new LogicCoord(-7, 29), result);
    }


    @Test
    public void testGetPointOfCollisionWithBottomOfLogicRow() {

        Board board = new Board(15, 8);

        int theta = 45;
        int row = 2;

        LogicCoord result = board.getPointOfCollisionWithBottomOfLogicRow(theta,row);

        assertEquals(3.175, result.getX(), 0.01);
        assertEquals(3.175, result.getY(), 0.01);


        theta = 25;
        result = board.getPointOfCollisionWithBottomOfLogicRow(theta,row);

        assertEquals(6.81, result.getX(), 0.01);
        assertEquals(3.175, result.getY(), 0.01);
    }

    @Test
    public void testDetectCollision() {

        Board board = new Board(15,8,10, new GameBoardController());
        LogicCoord collision = board.getCollisionPoint(1, 3, 3, 1);
        System.out.println(collision + " " + collision.convertToAspectLogicCoord(10));
    }
    */
}