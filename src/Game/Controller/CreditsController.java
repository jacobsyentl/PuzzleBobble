package Game.Controller;

import Game.View.CreditsPane;
import javafx.scene.layout.StackPane;

public class CreditsController {
    private StackPane rootPane;
    private int score;
    private String name;

    public CreditsController(StackPane rootPane){
        this.rootPane = rootPane;
        invokeView();
    }

    private void invokeView() {
        new CreditsPane(this, rootPane);
    }

    public void invokeMenuController() {
       new MenuController(rootPane);
    }
}