package Game.Controller;

import Game.Database.Player;
import Game.View.SettingsPane;
import javafx.scene.layout.StackPane;

public class SettingsController {
    private StackPane rootPane;

    public SettingsController(StackPane rootPane){
        this.rootPane = rootPane;
        invokeView();
    }

    private void invokeView() {
        new SettingsPane(rootPane, this);
    }

    public void invokeMenuController(){
        new MenuController(rootPane);
    }

    public void invokeSPController(Player player){
        new SPController(rootPane, player);
    }
}

