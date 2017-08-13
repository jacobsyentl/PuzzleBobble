package Game.Controller;

import Game.Database.BubbleDB;
import Game.Database.HighscorePerLevel;
import Game.Database.Level;
import Game.Database.Player;
import Game.View.HighscoresPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.StackPane;
import java.util.ArrayList;

public class HighscoresController {
    private StackPane rootPane;
    BubbleDB bubbleDB = BubbleDB.getInstance();

    public HighscoresController(StackPane rootPane){
        this.rootPane = rootPane;
        invokeView();
    }

    private void invokeView() {
        new HighscoresPane(rootPane, this);
    }

    public void invokeMenuController(){
        new MenuController(rootPane);
    }

    public ObservableList<HighscorePerLevel> getLevelData(int levelNumber){
        String[][] data = bubbleDB.getHighscoresFromLevel(levelNumber);
        ArrayList<HighscorePerLevel> dataArrayList = new ArrayList<HighscorePerLevel>();

        for(int i = 0; i < data.length; i++){
            dataArrayList.add(new HighscorePerLevel(Integer.parseInt(data[i][1]), data[i][0]));
        }

        ObservableList<HighscorePerLevel> objects = FXCollections.observableArrayList(dataArrayList);

        return objects;
    }

    public ArrayList<String> getLevels(){
        ArrayList<Level> levelData;
        ArrayList<String> data = new ArrayList<String>();

        levelData = bubbleDB.getLevels();

        for(int i = 0; i < levelData.size(); i++){
            data.add(levelData.get(i).getLevelName());
        }
        return data;
    }

    public ObservableList<Player> getPlayers(){
        ArrayList<Player> players;
        players = bubbleDB.getMultiplayerData();

        ObservableList<Player> objects2 = FXCollections.observableArrayList(players);

        return objects2;
    }
}