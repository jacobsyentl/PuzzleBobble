package Game.Logic;

import Game.Database.Bubble;

import java.util.Comparator;

/**
 * Created by Shane on 16-12-2015.
 */
public class BubbleCompareByPositionBottomLeft implements Comparator<Bubble> {

    int rows;
    int columns;

    public BubbleCompareByPositionBottomLeft(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
    }

    @Override
    public int compare(Bubble o1, Bubble o2) {

        LogicCoord logicCoordO1 = o1.getDataCoord().convertToLogicCoord(rows, columns);
        LogicCoord logicCoordO2 = o2.getDataCoord().convertToLogicCoord(rows, columns);

        if (logicCoordO1.getX() < logicCoordO2.getX()) {
            return -1;
        } else if (logicCoordO1.getX() == logicCoordO2.getX()) {
            if (logicCoordO1.getY() < logicCoordO2.getY()) {
                return -1;
            } else if (logicCoordO1.getY() == logicCoordO2.getY()) {
                return  0;
            } else return 1;
        } else return 1;
    }
}
