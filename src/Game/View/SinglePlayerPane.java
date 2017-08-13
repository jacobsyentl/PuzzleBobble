package Game.View;

import Game.Controller.SPController;
import Game.Database.Player;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import java.util.HashMap;
import java.util.Map;

public class SinglePlayerPane extends GamePane {
    public SinglePlayerPane( StackPane rootPane, SPController parent){
        super(rootPane, parent);
    }

    protected void initComponents() {
        super.initComponents();
        pane.setMaxSize(320, 640);
        pane.setPrefSize(320, 640);
    }

    public void displayActivePowerBubbles(HashMap<String,Integer> powerBubbles, Player player){
        for (Map.Entry<String,Integer> entry: powerBubbles.entrySet()){
            String key = entry.getKey();
            int value = entry.getValue();

            Label label = new Label(key+": "+value);
            label.setLayoutX(10);
            this.getChildren().add(label);
        }
    }

    public void setRemainingBalls(int player,int remainingBalls) {
        Label label =new Label(player+": "+remainingBalls);
        label.setLayoutX(50);
        this.getChildren().add(label);
    }
}