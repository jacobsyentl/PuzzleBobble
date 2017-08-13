package Game.Logic;

import javafx.geometry.Point2D;


public class LogicCoord extends Point2D implements Comparable {

    private int score;

    public LogicCoord(double x, double y){
        super(x,y);
    }

    public boolean invokedBy(LogicCoord bubbleThatHasBeenHit) {
        return false;
    }

    public int getScore() {
        return score;
    }


    public void setScore(int score) {
        this.score = score;
    }

    public DataCoord convertToDataCoord(int rows, int columns) {

        int y = (int) (rows - Math.floor(this.getY() /2) - 1);
        int x = (int) (Math.ceil((this.getX() + columns) / 2)) - 1;

        return new DataCoord(x,y);
    }

    public AspectLogicCoord convertToAspectLogicCoord(int offset) {

        double rowHeight = Math.sqrt(3) / 2;
        double x = this.getX();
        double y = (this.getY()+offset) * rowHeight ;

        return new AspectLogicCoord(x, y);
    }

    public VisualCoord convertToVisualCoord(int rows, int columns) {

        int logicBoardRows = rows*2;
        int logicBoardColumns = columns*2;
        LogicCoord deltaLogicCoordLogicAxisToVisualCoordLogicAxis = new LogicCoord(logicBoardColumns/2,-logicBoardRows+1);
        LogicCoord visualCoordLogicAxis =  this.add(deltaLogicCoordLogicAxisToVisualCoordLogicAxis);
        VisualCoord result = visualCoordLogicAxis.switchSignOfVerticalAxis();

        return result;

    }

    @Override
    public LogicCoord add(Point2D point) {
        Point2D result = super.add(point);
        return new LogicCoord(result.getX(), result.getY());
    }

    @Override
    public LogicCoord subtract(Point2D point) {
        Point2D result = super.subtract(point);
        return new LogicCoord(result.getX(), result.getY());
    }

    private VisualCoord switchSignOfVerticalAxis(){
        VisualCoord resultaat = new VisualCoord(this.getX(),this.getY()*-1);
        return resultaat;
    }

    @Override
    public int compareTo(Object o) {

        double compareQuantity = (int) ((LogicCoord) o).getX();

        //ascending order
        return (int) (this.getX() - compareQuantity);

        //descending order
        //return compareQuantity - this.quantity;

    }

    @Override
    public String toString() {
        return "LogicCoord [x = " + getX() + ", y = " + getY() + "]";
    }
}
