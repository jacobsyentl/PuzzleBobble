package Game.Controller;

import Game.View.WinPane;
import javafx.scene.layout.StackPane;

public class WinController {
    private StackPane rootPane;

    public WinController(StackPane rootPane){
        this.rootPane = rootPane;
        invokeView();
    }

    private void invokeView() {
        new WinPane(rootPane, new GameBoardController());
    }

    public void invokeMenuController(){
        new MenuController(rootPane);
    }
}