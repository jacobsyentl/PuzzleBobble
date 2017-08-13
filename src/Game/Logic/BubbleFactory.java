package Game.Logic;

import Game.Database.Bubble;
import Game.Database.BubbleDB;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

import java.util.List;


public class BubbleFactory {
    List<Bubble> bubbles;
    ImagePattern[] imagePatterns;

    public BubbleFactory(){
        BubbleDB bubbleDB = BubbleDB.getInstance();
        bubbles = bubbleDB.getBubbles();
        imagePatterns = new ImagePattern[11];
        for (Bubble bubble : bubbles) {
            imagePatterns[bubble.getBubbleGameId()-1] = new ImagePattern(new Image(bubble.getUrl()));
        }
    }

    public ImagePattern getImagePattern(int id) {
        return imagePatterns[id];
    }
    
    public Bubble createBubble(int bubbleNumber){
        Bubble bubble;

        if (bubbleNumber == 0) return null;

        bubbleNumber--;
        int bubbleGameId, bubbleScore, bubblePowerId;
        String bubbleName, url;
        ImagePattern fill;

        bubbleGameId = bubbles.get(bubbleNumber).getBubbleGameId();
        bubbleName = bubbles.get(bubbleNumber).getBubbleName();
        bubbleScore = bubbles.get(bubbleNumber).getBubbleScore();
        url = bubbles.get(bubbleNumber).getUrl();
        bubblePowerId = bubbles.get(bubbleNumber).getBubblePowerId();
        fill = imagePatterns[bubbles.get(bubbleNumber).getBubbleGameId()-1];

        bubble = new Bubble(bubbleGameId, bubbleName, bubbleScore, url, bubblePowerId, fill);

        return bubble;
    }
}