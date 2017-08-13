package Game.Database;

import Game.Logic.DataCoord;
import Game.Logic.LogicCoord;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.util.Comparator;

public class Bubble extends Circle {
    private int  bubbleGameId, bubbleScore, bubblePowerId;
    private String bubbleName, url;
    private DataCoord dataCoord;

    public Bubble(int bubbleGameId, String bubbleName, int bubbleScore, String url, int bubblePowerId, ImagePattern fill){
        this.bubbleGameId = bubbleGameId;
        this.bubbleName = bubbleName;
        this.url = url;
        this.bubbleScore = bubbleScore;
        this.bubblePowerId = bubblePowerId;
        this.setFill(fill);
    }


    public int getBubbleScore() {
        return bubbleScore;
    }

    public void setBubbleScore(int bubbleScore) {
        this.bubbleScore = bubbleScore;
    }

    public int getBubblePowerId() {
        return bubblePowerId;
    }

    public void setBubblePowerId(int bubblePowerId) {
        this.bubblePowerId = bubblePowerId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBubbleName() {
        return bubbleName;
    }

    public void setBubbleName(String bubbleName) {
        this.bubbleName = bubbleName;
    }

    public boolean invokedBy(Bubble currentBubble) {
        if (!getBubbleName().equals("fixed"))  {
            if ( (getBubbleName().equals(currentBubble.getBubbleName()) || currentBubble.getBubbleName().equals("Rainbow"))) {
                return true;
            }
        }
        return false;
    }

    public DataCoord getDataCoord() {
        return dataCoord;
    }

    public void setDataCoord(DataCoord dataCoord) {
        this.dataCoord = dataCoord;
    }

    public int getBubbleGameId() {
        return bubbleGameId;
    }

    public void setBubbleGameId(int bubbleGameId) {
        this.bubbleGameId = bubbleGameId;
    }

    @Override
    public String toString() {
        if (getDataCoord() != null) {
            return getDataCoord().toString();
        } else return "null";
    }
}
