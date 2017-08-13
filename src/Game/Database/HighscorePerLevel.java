package Game.Database;

public class HighscorePerLevel {
    private int highscore;
    private String playerName;

    public HighscorePerLevel(int highscore, String playerName){
        this.highscore = highscore;
        this.playerName = playerName;
    }

    public int getHighscore() {
        return highscore;
    }

    public void setHighscore(int highscore) {
        this.highscore = highscore;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
