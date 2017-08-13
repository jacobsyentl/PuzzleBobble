package Game.Logic;

import javafx.geometry.Point2D;


public class DataCoord extends Point2D {

    private int score;

    public DataCoord(double x, double y){
        super(x,y);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public LogicCoord convertToLogicCoord(int rows, int columns) {
           int xOffset;
            int yOffset = 0;

            if (getY() %2 ==1) {
                xOffset = 1;
            }else{
                xOffset =0;
            }
            double x =getX()*2 - (columns-1) + xOffset;
            double y = (rows-(getY()+1)) * 2 + 1 +  yOffset;
            return new LogicCoord(x,y);
    }
}
