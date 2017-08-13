package Game.Logic;

import Game.Controller.GameBoardController;
import Game.Database.Bubble;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import sun.rmi.runtime.Log;

import java.lang.reflect.Array;
import java.util.*;

public class Board {

    private Bubble[][] board;
    private int rows, columns;
    int shooterOffset;
    int bubbleRadius;
    int score;
    GameBoardController parent;

    public Board(int rows, int columns, int shooterOffset, GameBoardController parent) {
        this.rows = rows;
        this.columns = columns;
        createBoard();
        bubbleRadius = 1;
        this.shooterOffset = shooterOffset;
        this.parent = parent;
    }

    public Bubble[][] getBoard() {
        return board;
    }

    public int getNumberOfRows() {
        return rows;
    }

    public int getNumberOfColumns() {
        return columns;
    }

    private void createBoard(){
        Boolean rowNumberIsEven;
        board = new Bubble[rows][];

        for(int i = 0; i < rows; i++){
            rowNumberIsEven = (i%2 == 0);
            if(rowNumberIsEven) board[i] = new Bubble[columns];
            else board[i] = new Bubble[columns-1];
        }
    }

    public void loadLevel(String seed) {
        int rowCount = 0, colCount = 0;
        Seed convertSeed = new Seed();
        Bubble[] convertedString = convertSeed.convertToBubbleArray(seed);


        for(int i = 0; i < convertedString.length; i++){
            Bubble bubbleType = convertedString[i];
            LogicCoord logicCoord = new DataCoord(colCount,rowCount).convertToLogicCoord(rows,columns);
            setBubbleAt(logicCoord, bubbleType);
            ++colCount;
            if(rowCount % 2 == 0 ){
                if(colCount == 8){
                    rowCount++;
                    colCount = 0;
                }
            }
            else {
                if(colCount == 7){
                    rowCount++;
                    colCount = 0;
                }
            }
        }
    }

    public LogicCoord getCollisionPointThetaZero(double x0, double y0, double r) {
        double a = 1;
        double b = -2 * y0;
        double c = Math.pow(x0,2)+ Math.pow(y0,2)- 4 * Math.pow(r,2);

        double discriminant  = Math.round(Math.pow(b,2) - 4*a*c);

        if (discriminant >= 0) {
            double y = (-b - Math.sqrt(discriminant)) / (2 * a);

            double x = 0;

            y = y0 - y;

            LogicCoord result = new AspectLogicCoord(x, y).convertToLogicCoord(0);

            return result;

        } else {
            return null;
        }
    }

    public LogicCoord getCollisionPoint(double rico, double x0, double y0, double r) {
        rico = Math.abs(rico);


        double a = 1 + Math.pow(rico,2);
        double b = -2*x0 - 2*rico*y0;
        double c = Math.pow(x0,2) + Math.pow(y0,2)-4 * Math.pow(r,2);

        double discriminant  = Math.round(Math.pow(b,2) - 4*a*c);

        if (discriminant >= 0) {
            double x = (-b - Math.sqrt(discriminant)) / (2 * a);
            double y = rico * x ;

            x= (x0 - x);

            y = y0 - y;

            LogicCoord result = new AspectLogicCoord(x, y).convertToLogicCoord(0);

            return result;

        } else {
            return null;
        }
    }

    public double getDistanceBetweenCenters(Point2D center1, Point2D center2) {
        double deltaX = center1.getX() - center2.getX();
        double deltaY = center1.getY() - center2.getY();
        double distance = Math.sqrt((Math.pow(deltaX, 2)) + (Math.pow(deltaY, 2)));

        return distance;
    }

    public Point2D convertLogicCoordLogicAxisToVisualCoordLogicAxis(Point2D logicCoordLogicAxis) {
        int logicBoardRows = rows*2;
        int logicBoardColumns = columns*2;
        Point2D deltaLogicCoordLogicAxisToVisualCoordLogicAxis = new Point2D(logicBoardColumns/2,-logicBoardRows+1);
        Point2D visualCoordLogicAxis = logicCoordLogicAxis.add(deltaLogicCoordLogicAxisToVisualCoordLogicAxis);
        visualCoordLogicAxis = new Point2D(visualCoordLogicAxis.getX(),visualCoordLogicAxis.getY());
        return visualCoordLogicAxis;
    }

    public Point2D switchSignOfVerticalAxis(Point2D coord ){
        Point2D result = new Point2D(coord.getX(),coord.getY()*-1);
        return result;
    }

    public LogicCoord getCoordOfCollisionWithEdge(double thetaFromPointerInRadians, LogicCoord start) {

        if (thetaFromPointerInRadians == 0){
            return new LogicCoord(0, rows*2);
        } else {
            byte signOfTheta = (byte) (Math.abs(thetaFromPointerInRadians) / thetaFromPointerInRadians);
            double thetaFromBottomInRadians = Math.PI/2 - thetaFromPointerInRadians;

            double width = Math.abs(signOfTheta * (columns - 1) - start.getX());

            double deltaY = width * Math.abs(Math.tan(thetaFromBottomInRadians));
            AspectLogicCoord collisionCoord = new AspectLogicCoord(signOfTheta * (columns - 1), deltaY + start.convertToAspectLogicCoord(shooterOffset).getY());

            return collisionCoord.convertToLogicCoord(shooterOffset);
        }

    }

    public Hashtable<Bubble, Integer> hitResultTree(Bubble currentBubble) {
        return hitResultTree(currentBubble, null);
    }

    public Hashtable<Bubble, Integer> hitResultTree(Bubble currentBubble, Hashtable<Bubble,Integer> scoreTable) {

        boolean first = false;
        ArrayList<Bubble> firstNeighbours;
        firstNeighbours = new ArrayList<>();
        if (scoreTable == null) {
            scoreTable = new Hashtable<>();
            first = true;

        }

        scoreTable.put(currentBubble, currentBubble.getBubbleScore());
        ArrayList<Bubble> neighbours = getNeighboursOf(currentBubble.getDataCoord().convertToLogicCoord(rows, columns));

        for (Bubble neighbour : neighbours) {
            if (!scoreTable.containsKey(neighbour)) {
                if (currentBubble.getBubbleName().equals("Bomb") || (neighbour.getBubbleName().equals("Color Blind") && scoreTable.size() <= 1) || (neighbour.getBubbleName().equals("Less Choice") && scoreTable.size() <= 1) || neighbour.invokedBy(currentBubble)) {
                    scoreTable.put(neighbour, neighbour.getBubbleScore());
                    if (first) {
                        firstNeighbours.add(neighbour);
                    }
                }
                else {
                    scoreTable.put(neighbour, 0);
                }
                if (neighbour.invokedBy(currentBubble)) {
                    hitResultTree(neighbour, scoreTable);
                }
            }

        }

        if (first) {

            int length = firstNeighbours.size();


            if (length > 0) {
                if (currentBubble.getBubbleName().equals("Rainbow")) {

                Random rand = new Random();

                Bubble randomBubble = firstNeighbours.get(rand.nextInt(length));
                currentBubble.setFill(parent.getBubbleFactory().getImagePattern(randomBubble.getBubbleGameId()-1));
                currentBubble.setBubbleGameId(randomBubble.getBubbleGameId());
                    currentBubble.setBubbleName(randomBubble.getBubbleName());

                }
            }

        }

        return scoreTable;
    }

    private ArrayList<Bubble> getNeighboursOf(LogicCoord bubbleThatHasBeenHit) {
        ArrayList<Bubble> result = new ArrayList<>();

        DataCoord dataCoord =  bubbleThatHasBeenHit.convertToDataCoord(rows, columns);

        int lengthOfRow = board[(int) dataCoord.getY()].length;
        int x = (int) dataCoord.getX();
        int y = (int) dataCoord.getY();

        int deltaX = 0;
        if (y%2 == 0) {
            deltaX = -1;
        }

        if (0 <= x-1) {
            if (board[y][x-1] != null) {
                result.add(board[y][x - 1]);
            }

        }

        if (x+1 <= lengthOfRow - 1) {
            if (board[y][x+1] != null) {
                result.add(board[y][x + 1]);
            }
        }

        if (0<y) {

            if (0  <=  x+ deltaX) {
                if (board[y-1][x+deltaX] != null) {
                    result.add(board[y - 1][x + deltaX]);
                }
            }

            if (x + 1 + deltaX <= board[y - 1].length - 1 ) {
                if (board[y-1][x + 1 + deltaX] != null) {
                    result.add(board[y - 1][x + 1 + deltaX]);
                }
            }
        }

        if (y<rows-1) {

            if (0 <= x + deltaX) {
                if (board[y + 1][x + deltaX] != null) {
                    result.add(board[y + 1][x + deltaX]);
                }
            }

            if (x + 1 + deltaX <= board[y + 1].length - 1) {
                if (board[y + 1][x + 1 +deltaX] != null) {
                    result.add(board[y + 1][x + 1 + deltaX]);
                }
            }
        }

        return result;

    }

    public ArrayList<Bubble> getBallsUnderClusterToDrop(ArrayList<Bubble> cluster) {
        cluster  = new ArrayList<>(cluster);
        ArrayList<Bubble> ballsUnderCluster = getBallsUnderCluster(cluster);

        HashMap<Bubble, Boolean> visited = new HashMap<>();

        ArrayList<Bubble> ballsUnderClusterToDrop = new ArrayList<>();

        for (Bubble ballUnderCluster : ballsUnderCluster) {

            if (visited.containsKey(ballUnderCluster)) continue;

            ArrayList<Bubble> currentVisited = new ArrayList<>(roofTree(ballUnderCluster, visited, cluster, null));
            boolean hitsRoof = false;

            for (Bubble bubble : currentVisited) {
                if (bubble.getDataCoord().getY() == 0 ) {
                    hitsRoof = true;
                }
            }

            visited = mergeVisitedWithCurrentVisited(visited, currentVisited, hitsRoof);


            if (hitsRoof == false) {
                for (Bubble bubble : currentVisited) {
                    ballsUnderClusterToDrop.add(bubble);
                }
            }
        }

        return ballsUnderClusterToDrop;
    }

    private HashMap<Bubble, Boolean> mergeVisitedWithCurrentVisited(HashMap<Bubble, Boolean> visited, ArrayList<Bubble> currentVisited, boolean hitsRoof) {
        HashMap<Bubble, Boolean> result = new HashMap<>(visited);
        currentVisited = new ArrayList<>(currentVisited);

        for (Bubble bubble : currentVisited) {
            if (result.containsKey(bubble)) {
                if (result.get(bubble) == false) {result.put(bubble, hitsRoof);}
            } else result.put(bubble, hitsRoof);

        }
        return result;

    }

    public ArrayList<Bubble> roofTree(Bubble bubble, HashMap<Bubble, Boolean> visited, ArrayList<Bubble> cluster, ArrayList<Bubble> currentVisited) {

     if (currentVisited == null) {
            currentVisited = new ArrayList<>();
        } else { currentVisited = new ArrayList<>(currentVisited); }

        visited = new HashMap<>(visited);
        cluster = new ArrayList<> (cluster);

        currentVisited.add(bubble);

        ArrayList<Bubble> neighbours = getNeighboursOf(bubble.getDataCoord().convertToLogicCoord(rows, columns));

        for (Bubble neighbour : neighbours) {
            if (!visited.containsKey(neighbour) && !currentVisited.contains(neighbour)) {
                if (!cluster.contains(neighbour)) {

                    for (Bubble neighbourBubble : roofTree(neighbour, visited, cluster, currentVisited)) {
                        if (!currentVisited.contains(neighbourBubble)) {
                            currentVisited.add(neighbourBubble);
                        }
                    }
                } else currentVisited.add(neighbour);
            }
        }
         return currentVisited;
    }

    public ArrayList<Bubble> getBallsUnderCluster(ArrayList<Bubble> cluster) {

        cluster = new ArrayList<Bubble> (cluster);
        Collections.sort(cluster, new BubbleCompareByPositionBottomLeft(rows, columns));

         Iterator<Bubble> it = cluster.iterator();
        int currentColumn = -1;
        int index = 0;

        while (it.hasNext()) {
            Bubble current = it.next();
            LogicCoord logicCoord = current.getDataCoord().convertToLogicCoord(rows, columns);

            if (index == 0) {
                currentColumn = (int) logicCoord.getX();
                index++;
                continue;
            }

            if ((int) logicCoord.getX() == currentColumn) {
                {
                    it.remove();
                }
            } else {
                    currentColumn = (int) logicCoord.getX();
            }
            index++;
        }

        // rij eronder

        ArrayList<Bubble> result = new ArrayList<>();

        it = cluster.iterator();

        while (it.hasNext()) {

            Bubble current = it.next();
            int x = (int) current.getDataCoord().convertToLogicCoord(rows, columns).getX();
            int y = (int) current.getDataCoord().convertToLogicCoord(rows, columns).getY();

            ArrayList<Bubble> neighbours = getNeighboursOf(current.getDataCoord().convertToLogicCoord(rows, columns));

            for (Bubble neighbour : neighbours) {
                if (containsLogicCoord(neighbour.getDataCoord().convertToLogicCoord(rows, columns))) {
                    DataCoord dataCoord = neighbour.getDataCoord();

                    if (board[(int) dataCoord.getY()][(int) dataCoord.getX()] != null) {
                        if (!result.contains(neighbour)) {
                            result.add(neighbour);
                        }
                    }
                }
            }

        }
        return result;

    }

    public Bubble getBubbleAt(LogicCoord coord)
    {
        DataCoord dataCoord = coord.convertToDataCoord(rows, columns);
        return board[(int)dataCoord.getY()][(int)dataCoord.getX()];
    }

    public void setBubbleAt(LogicCoord coord, Bubble value)
    {
        DataCoord dataCoord = coord.convertToDataCoord(rows, columns);

        if( board[(int)dataCoord.getY()][(int)dataCoord.getX()] != null) {
            getBubbleAt(coord).setDataCoord(null);
        }

        board[(int)dataCoord.getY()][(int)dataCoord.getX()] = value;

        if (value != null) {
            value.setDataCoord(coord.convertToDataCoord(rows, columns));
        }
    }

    public boolean containsLogicCoord(LogicCoord coord) {
        if (containsXOf(coord) && containsYOf(coord)) {
            return true;
        } else return false;
    }

    public boolean containsXOf(LogicCoord coord) {
        if (Math.abs(coord.getX()) <= columns - 1) {
            return true;
        } else return false;
    }

    public boolean containsYOf(LogicCoord coord) {
        if (0 < coord.getY() && coord.getY() < rows * 2) {
            return true;
        } else return false;
    }

    private int getLowestNonEmptyRow() {
        for (int i=rows-1; i>=0; i--) {

            for (int j=0; j<board[i].length; j++) {
                if (board[i][j] != null) return i;
            }
        }
        return rows-1;
    }

    public LogicCoord getLogicCoordOfCollisionWithBottomOfLogicRow(double thetaFromPointerInRadians, int row, AspectLogicCoord start) {
        double thetaFromBottomInRadians = Math.PI/2 - thetaFromPointerInRadians;
        double rowBottomY = new LogicCoord(0,row*2).convertToAspectLogicCoord(shooterOffset).getY();
        double deltaYAspect = rowBottomY - 1 - (1-Math.sqrt(3)/2) - start.getY();

        double deltaXAspect = deltaYAspect / Math.tan(thetaFromBottomInRadians) ;

        return new AspectLogicCoord(start.getX()+deltaXAspect,start.getY()+deltaYAspect).convertToLogicCoord(shooterOffset);
    }

    public ArrayList<LogicCoord> getBubblesWithinRangeOfProjectile(LogicCoord coord, double thetaFromPointerInRadians) {
        double thetaFromBottomInRadians = Math.PI/2 - thetaFromPointerInRadians;
        double deltaYBetweenBubbles = Math.sqrt(3);
        double rowHeight = deltaYBetweenBubbles/2;
        double cotTheta = 1/Math.tan(thetaFromBottomInRadians);
        byte direction;
        if (thetaFromPointerInRadians != 0) {direction = (byte) (Math.abs(thetaFromPointerInRadians)/thetaFromPointerInRadians);} else {direction = 1;};
        double heightTwoRowsOfBubbles = 2*bubbleRadius+deltaYBetweenBubbles;
        double lengthCenterThroughRows =  Math.abs(heightTwoRowsOfBubbles * cotTheta);

     //   double xStart =  Math.round(coord.getX() - direction * Math.ceil((1 - Math.abs(Math.cos(thetaRad)))));
        double xStart =  Math.round(coord.getX() - direction);
      //  double xEnd = xStart + direction * Math.ceil(lengthCenterThroughRows + (1 - Math.abs(Math.cos(Math.PI - thetaRad)))) ;
        double xEnd = xStart + direction * Math.ceil(lengthCenterThroughRows + (2 )) ;

        if (Math.abs(xEnd) > (columns-1)) {
            xEnd = xEnd/Math.abs(xEnd) * (columns - 1);
        }
        if (Math.abs(xStart) > (columns-1)) {
            xStart = xStart/Math.abs(xStart) * (columns - 1);
        }

        ArrayList<LogicCoord> result = new ArrayList<>();

        int smallest, biggest;
        if (xStart < xEnd) {
            smallest = (int) xStart;
            biggest = (int) Math.ceil(xEnd);
        } else {
            smallest = (int) xEnd;
            biggest = (int) xStart;
        }

        for (double i=smallest; i<=biggest; i++) {
            int j = (int) Math.round(coord.getY()+bubbleRadius/rowHeight+(bubbleRadius/rowHeight-1)+1);

            if (j%2==0) j--;
            if (((rows*2 - 1 - j)/ 2) % 2 == 0) {
                if (Math.abs(i % 2) == 0)j+=2;

                if (!containsYOf(new LogicCoord(0,j)))continue;
            } else {
                if (Math.abs(i % 2) == 1)j+=2;
                if (!containsYOf(new LogicCoord(0,j)))continue;
            }

            result.add(new LogicCoord((int) i,j));
        }

        return result;
    }

    public ArrayList<LogicCoord> filterLogicCoordsByBallsOnBoard(ArrayList<LogicCoord> list) {

        ArrayList<LogicCoord> result = new ArrayList<>();
        for (LogicCoord i : list) {
            if (getBubbleAt(i) != null) {
                result.add(i);
            }
        }

        return result;
    }

    public LogicCoord getSnapPositionOf(LogicCoord point, double thetaFromPointerInRadians) {
        double x = Math.round(point.getX());
        double y = Math.round(point.getY());
        byte signOfTheta;
        double signDeltaX;
        double signDeltaY;

        if (thetaFromPointerInRadians != 0) {
            signOfTheta = (byte) (Math.abs(thetaFromPointerInRadians)/thetaFromPointerInRadians);
        } else signOfTheta = 1;

        if ((x - point.getX() ) != 0) {
            signDeltaX = Math.abs((x - point.getX())) / (x - point.getX());
        } else signDeltaX = 1;

        if ((y - point.getY() ) != 0) {
            signDeltaY = Math.abs((y - point.getY())) / (y - point.getY());
        } else signDeltaY = 1;


        if (y % 2 == 0) {
            y -= signDeltaY;
        }

        if (((y+1)/2)%2 == 0) {
            if (Math.abs(x%2) == 1) {
                x -= signDeltaX;
            }
        } else {
            if (Math.abs(x%2) == 0) {
                x -= signDeltaX ;
            }

        }

        if (x > (columns - 1)) x = columns - 1;
        if (x < -(columns - 1)) x = -(columns - 1);

        LogicCoord snapPoint = new LogicCoord(x,y);
        return snapPoint;
    }

    public LogicCoord getLowestDeltaXDeltaY(ArrayList<LogicCoord> collisions, double thetaFromPointerInRadians, LogicCoord currentPositionOfPointer) {

        LogicCoord lowest = null;
        double deltaXLowest;
        double deltaYLowest;

        for (LogicCoord i : collisions) {

            if( lowest!= null) {
                double deltaX = Math.abs(i.getX() - currentPositionOfPointer.getX());
                double deltaY = Math.abs(i.getY() - currentPositionOfPointer.getY());
                deltaXLowest = Math.abs(lowest.getX() - currentPositionOfPointer.getX());
                deltaYLowest = Math.abs(lowest.getY() - currentPositionOfPointer.getY());
                if(deltaX < deltaXLowest) { lowest = i;}
                else if (deltaX == deltaXLowest) {
                    if (deltaY < deltaYLowest) {
                        lowest = i;
                    }
                }
            } else lowest = i;
        }

        return lowest;
    }

    public ArrayList<LogicCoord> getCollisions(ArrayList<LogicCoord> potentialCoordsOfCollision, LogicCoord currentPositionOfProjectile, double thetaFromPointerInRadians) {

        ArrayList<LogicCoord> collisions = new ArrayList<LogicCoord>();

        for (LogicCoord i : potentialCoordsOfCollision) {

            double thetaFromBottomInRadians = Math.PI/2-Math.abs(thetaFromPointerInRadians);
            byte signOfTheta = (byte) (Math.abs(thetaFromPointerInRadians)/thetaFromPointerInRadians);

            double deltaX = (i.convertToAspectLogicCoord(shooterOffset).getX()-currentPositionOfProjectile.convertToAspectLogicCoord(shooterOffset).getX()) * signOfTheta;
            double deltaY = i.convertToAspectLogicCoord(shooterOffset).getY()-currentPositionOfProjectile.convertToAspectLogicCoord(shooterOffset).getY();

            LogicCoord ifCollisionDeltaFromProjectileToBubbleRelativeToCurrentPosition;

            if (thetaFromPointerInRadians != 0) {
                ifCollisionDeltaFromProjectileToBubbleRelativeToCurrentPosition = getCollisionPoint(Math.tan(thetaFromBottomInRadians), deltaX, deltaY, 1);
            } else { ifCollisionDeltaFromProjectileToBubbleRelativeToCurrentPosition = getCollisionPointThetaZero(deltaX, deltaY, 1); }

           // collision = delta from bubble it bounces with, in direction of currentPositionOfProjectile
            if (ifCollisionDeltaFromProjectileToBubbleRelativeToCurrentPosition != null) {
                double collisionX;
                if (thetaFromPointerInRadians != 0) {
                    // if the thetaFromPointerInRadians is negative, we need to add deltaX
                    if (thetaFromPointerInRadians < 0) {
                        collisionX = i.getX() + ifCollisionDeltaFromProjectileToBubbleRelativeToCurrentPosition.getX();

                    } else collisionX = i.getX() - ifCollisionDeltaFromProjectileToBubbleRelativeToCurrentPosition.getX();

                } else collisionX = 0;

                LogicCoord collision = new LogicCoord(collisionX,i.getY() - ifCollisionDeltaFromProjectileToBubbleRelativeToCurrentPosition.getY() );
                if (Math.abs(collision.getX()) <= (columns - 1) && Math.abs(collision.getY()) <= rows*2-2) {

                    collisions.add(collision);
                }

            }
        }
        return collisions;
    }

    public Hashtable<String, Object> getCollisionResult(ArrayList<LogicCoord> collisions, double thetaFromPointerInRadians, ArrayList<VisualCoord> queueProjectile, LogicCoord currentPositionOfProjectile) {

        queueProjectile = new ArrayList(queueProjectile);
        Bubble currentBubble = parent.getCurrentBubble();

        VisualCoord add = getLowestDeltaXDeltaY(collisions, thetaFromPointerInRadians, currentPositionOfProjectile).convertToVisualCoord(getNumberOfRows(), getNumberOfColumns());
        LogicCoord bubbleThatHasBeenHit = getLowestDeltaXDeltaY(collisions, thetaFromPointerInRadians, currentPositionOfProjectile);
        LogicCoord resultCoord = getSnapPositionOf(bubbleThatHasBeenHit,thetaFromPointerInRadians);
        System.out.println(resultCoord.getY());
        if(resultCoord.getY() >=1) {

            setBubbleAt(resultCoord, currentBubble);

            Hashtable<Bubble, Integer> tree = hitResultTree(getBubbleAt(resultCoord));

            ArrayList<Bubble> bubblesInCluster = filterHashtableToCluster(tree);
            if (clusterContainsBubbleWithName(bubblesInCluster, "Less Choice")) {
                parent.setLesserChoiceCounter(currentBubble.getBubbleName());
            }

            if (clusterContainsBubbleWithName(bubblesInCluster, "Color Blind")) {
                parent.setColorblindCounter(1);
            }
            if (clusterContainsBubbleWithName(bubblesInCluster, "More chances")) {
                parent.incrementRemainingBubbles(5);
            }
            System.out.println(bubblesInCluster);

            if (bubblesInCluster.size() >= 3 || clusterContainsBubbleWithName(bubblesInCluster, "Rainbow") || clusterContainsBubbleWithName(bubblesInCluster, "Bomb") || clusterContainsBubbleWithName(bubblesInCluster, "More chances")  || clusterContainsBubbleWithName(bubblesInCluster, "Color Blind") || clusterContainsBubbleWithName(bubblesInCluster, "Less Choice" )) {
                removeClusterFromBoard(bubblesInCluster);
            } else bubblesInCluster = new ArrayList<>();

            ArrayList<Bubble> bubblesUnderCluster = getBallsUnderCluster(bubblesInCluster);
            ArrayList<Bubble> bubblesUnderClusterToDrop = getBallsUnderClusterToDrop(bubblesInCluster);

            removeClusterFromBoard(bubblesUnderClusterToDrop);

            queueProjectile.add(add);
            queueProjectile.add(resultCoord.convertToVisualCoord(getNumberOfRows(), getNumberOfColumns()));
            queueProjectile.add(add);
            queueProjectile.add(resultCoord.convertToVisualCoord(getNumberOfRows(), getNumberOfColumns()));

            Hashtable<String, Object> result = new Hashtable<>();
            result.put("projectile", queueProjectile);
            result.put("bubblesInCluster", bubblesInCluster);
            result.put("bubblesUnderCluster", bubblesUnderClusterToDrop);

            return result;
        }

        else return null;

    }

    public boolean clusterContainsBubbleWithName(ArrayList<Bubble> cluster, String bubbleName) {
        for (Bubble bubble : cluster) {
            if (bubble.getBubbleName().equals(bubbleName)) {
                return true;
            }
        }
        return false;
    }

    public void removeClusterFromBoard(ArrayList<Bubble> cluster) {
        for (Bubble bubble : cluster) {
            DataCoord dataCoord = bubble.getDataCoord();
            board[(int) dataCoord.getY()][(int) dataCoord.getX()] = null;
        }
    }

    public ArrayList<Bubble> filterHashtableToCluster(Hashtable<Bubble, Integer> tree) {
        Set<Bubble> treeKeys = tree.keySet();
        Iterator<Bubble> iterator = treeKeys.iterator();
        ArrayList<Bubble> cluster = new ArrayList<>();

        Bubble key;

        while (iterator.hasNext()) {
            // Getting Key
            key = iterator.next();

            if (tree.get(key) != 0) {
                cluster.add(key);
            }
        }
        return cluster;
    }

    public boolean isBoardEmpty() {

        for (int i=0; i < board.length; i++) {
            for (int j=0; j < board[i].length; j++) {
                if (board[i][j] != null) { return false; };
            }
        }

        return true;
    }

    public void clearBoard() {
        board = null;
        createBoard();
    }

    public boolean isBoardFull() {
        for (int i=0; i < board[rows-1].length; i++) {
            if (board[rows-1][i] != null) { return true; };
        } return false;
    }

    private void incrementScore(int increment) {
        this.score += increment;
    }

    public void makeAllBallsGray(boolean value) {
        if (value == true) {
            for (int i=0; i<board.length; i++) {
                for (int j=0; j < board[i].length; j++) {
                    if (board[i][j] != null) {
                        board[i][j].setFill(parent.getBubbleFactory().getImagePattern(5));
                    }
                }
            }
        } else {
            for (int i=0; i<board.length; i++) {
                for (int j=0; j < board[i].length; j++) {
                    if (board[i][j] != null) {
                        board[i][j].setFill(parent.getBubbleFactory().getImagePattern(board[i][j].getBubbleGameId() - 1));
                    }
                }
            }
        }
    }
}