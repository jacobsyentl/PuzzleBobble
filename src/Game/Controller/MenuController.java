package Game.Controller;

import Game.View.MenuPane;
import javafx.scene.layout.StackPane;

public class MenuController {

    private StackPane rootPane;

    public MenuController(StackPane rootPane) {
        this.rootPane = rootPane;
        invokeView();
    }

    private void invokeView(){
        new MenuPane(rootPane,this);
    }

    public void invokeSettingsController(){
        new SettingsController(rootPane);
        //new SPController(rootPane);
    }

    public void invokeMPController(){
        new MPController(rootPane);
    }

    public void invokeHighscoresController() {
        new HighscoresController(rootPane);
    }

    public void invokeCreditsController(){
        new CreditsController(rootPane);
    }
}
