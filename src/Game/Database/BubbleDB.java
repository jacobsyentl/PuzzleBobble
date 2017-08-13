package Game.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BubbleDB {
    Connection con;
    private static BubbleDB instance = null;

    protected BubbleDB() {
        String url = "jdbc:mysql://178.62.162.123/puzzlebobble";
        String username = "bubbleCon";
        String password = "bubblemania";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, username, password);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static BubbleDB getInstance() {
        if(instance == null) {
            instance = new BubbleDB();
        }
        return instance;
    }

    public ArrayList<Level> getLevels(){
        ArrayList<Level> levels = new ArrayList<>();
        try {
            PreparedStatement stmt = con.prepareStatement("SELECT * from levels");
            ResultSet set = stmt.executeQuery();
            while (set.next()) {
                int number = set.getInt("levelNumber");
                String levelName = set.getString("levelName");
                String surname = set.getString("levelSeed");

                Level level = new Level(number, levelName, surname);

                levels.add(level);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return levels;
    }

    public String[][] getHighscoresFromLevel(int levelNumber){
        String[][] highscoreData;
        try {
            PreparedStatement stmt = con.prepareStatement("SELECT COUNT(levelId) FROM playerlevelhighscores WHERE levelID = ?");
            stmt.setInt(1, levelNumber);
            ResultSet set = stmt.executeQuery();
            int rowCount = 0, counter = 0;

            while(set.next()){
                rowCount = set.getInt(1);
            }

            highscoreData = new String[rowCount][2];

            stmt = con.prepareStatement("SELECT * FROM playerlevelhighscores INNER JOIN players ON playerlevelhighscores.playerId = players.playerId WHERE levelId = ? ORDER BY highscore DESC");
            stmt.setInt(1, levelNumber);
            set = stmt.executeQuery();

            while(set.next()){
                String playerName = set.getString("playerName");
                int highscore = set.getInt("highscore");

                highscoreData[counter][0] = playerName;
                highscoreData[counter][1] = Integer.toString(highscore);
                counter++;
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        return highscoreData;
    }

    public ArrayList<Player> getMultiplayerData(){
        ArrayList<Player> playerList = new ArrayList<>();
        try {
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM players WHERE mpHighScoreCount > 0 ORDER BY mpHighScoreCount DESC");
            ResultSet set = stmt.executeQuery();

            while(set.next()){
                playerList.add(new Player(set.getInt("playerId"), set.getString("avatar"), set.getString("playerName"), set.getInt("mpHighScoreCount")));
            }
            return playerList;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String getLevelFromNumber(int levelNumber){
        String levelSeed = "";
        try {
            PreparedStatement stmt = con.prepareStatement("SELECT levelSeed FROM levels WHERE levelNumber = ?");
            stmt.setInt(1, levelNumber);
            ResultSet set = stmt.executeQuery();

            if(set.next()) {
                levelSeed = set.getString("levelSeed");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return levelSeed;
    }
    public List<Bubble> getBubbles(){
        List<Bubble> bubbles = new ArrayList<>();
        try {
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM bubbles ");
            ResultSet set = stmt.executeQuery();
            while(set.next()){
                int bubbleId = set.getInt("bubbleGameId");
                String bubbleName = set.getString("bubbleName");
                int bubbleScore;
                if (set.getInt("bubbleScore") == 0) {
                    bubbleScore = 10;
                } else bubbleScore = set.getInt("bubbleScore");
                String image = set.getString("image");
                int bubblePowerId = set.getInt("bubblePowerId");

                Bubble bubble = new Bubble(bubbleId, bubbleName, bubbleScore, image, bubblePowerId, null);
                bubbles.add(bubble);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return bubbles;
    }//Gets all the normal bubbles from the database. RETURNS List<Bubble>

    public List<Bubble> getNormalBubbles(){
        List<Bubble> bubbles = new ArrayList<>();
        try {
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM bubbles");
            ResultSet set = stmt.executeQuery();
            while(set.next()){
                int bubbleId = set.getInt("bubbleGameId");
                String bubbleName = set.getString("bubbleName");
                int bubbleScore = set.getInt("bubbleScore");
                String image = set.getString("image");
                int bubblePowerId = set.getInt("bubblePowerId");

                Bubble bubble = new Bubble(bubbleId, bubbleName, bubbleScore, image, bubblePowerId, null);
                bubbles.add(bubble);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return bubbles;
    }//Gets all the normal bubbles from the database. RETURNS List<Bubble>

    public int getAmountOfBubbles(){
        int amountOfBubbles = 0;
        try {
            PreparedStatement stmt = con.prepareStatement("SELECT COUNT(*) FROM bubbles");
            ResultSet set = stmt.executeQuery();
            if(set.next()) {
                amountOfBubbles = set.getInt(1);
            }

        } catch(Exception e){
            e.printStackTrace();
        }
        return amountOfBubbles;
    }//Gets the amount of bubbles from the database. RETURNS int

    public Player checkPlayer(String playerName){
        try {
            int playerId = 0, mpHighScoreCount = 0;
            String avatar = "test";
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM players WHERE playerName = ?");
            stmt.setString(1, playerName);
            ResultSet set = stmt.executeQuery();
            if(set.next()) {
                playerId = set.getInt("playerId");
                avatar = set.getString("avatar");
                playerName = set.getString("playerName");
            }
            Player player = new Player(playerId, avatar, playerName, 0);
            if(playerId == 0){
                return null;
            }
            else {
                return player;
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean createPlayer(String avatar, String playerName){
        try {
            PreparedStatement stmt = con.prepareStatement("INSERT INTO players (avatar, playerName) VALUES (?, ?)");
            stmt.setString(1, avatar);
            stmt.setString(2, playerName);
            stmt.executeUpdate();
            return true;
        } catch(Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public void writeScore(int playerId, int levelId, int highscore){
        try{
            PreparedStatement stmt = con.prepareStatement("INSERT INTO playerlevelhighscores (playerId, levelId, highscore) VALUES (?, ?, ?)");
            stmt.setInt(1, playerId);
            stmt.setInt(2, levelId);
            stmt.setInt(3, highscore);

            stmt.executeUpdate();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public int checkIfPlayerHasLevelScore(int playerId, int levelId){
        try{
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM playerlevelhighscores WHERE playerId = ? AND levelId = ?");
            stmt.setInt(1, playerId);
            stmt.setInt(2, levelId);
            ResultSet set = stmt.executeQuery();

            if(set.next()){
                return set.getInt("highscore");
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    public void updateHighscore(int playerId, int levelId, int score){
        try{
            PreparedStatement stmt = con.prepareStatement("UPDATE playerlevelhighscores SET highscore = ? WHERE playerId = ? AND levelId = ?");
            stmt.setInt(1, score);
            stmt.setInt(2, playerId);
            stmt.setInt(3, levelId);

            stmt.executeUpdate();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}