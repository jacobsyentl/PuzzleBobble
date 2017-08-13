package Game.Controller;

import Game.Database.Player;
import Game.View.PausedPane;
import Game.View.SettingsPane;
import javafx.scene.layout.StackPane;

public class PauseMenuController {
    private StackPane rootPane;

    public PauseMenuController(StackPane rootPane) {
        this.rootPane = rootPane;
        invokeView();
    }

    private void invokeView() {
        new PausedPane(rootPane, new GameBoardController());
    }

    public void invokeMenuController(){
        new MenuController(rootPane);
    }

    public void invokeSPController(Player player){
        new SPController(rootPane, player);
    }
}