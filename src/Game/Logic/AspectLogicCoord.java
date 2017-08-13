package Game.Logic;

import javafx.geometry.Point2D;

public class AspectLogicCoord extends Point2D {

    private int score;

    public AspectLogicCoord(double x, double y){
        super(x,y);
    }


    public LogicCoord convertToLogicCoord(int offset) {

        double rowHeight = Math.sqrt(3)/ 2;
        double y = (this.getY() / rowHeight)-offset ;
        double x = this.getX();

        return new LogicCoord(x,y);
    }

    @Override
    public String toString() {
        return "AspectLogicCoord [x = " + getX() + ", y = " + getY() + "]";
    }
}
