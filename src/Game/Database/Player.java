package Game.Database;

public class Player {
    int playerId, mpHighScoreCount;
    String playerName, avatar;

    public Player(int playerId, String avatar, String playerName, int mpHighScoreCount){
        this.playerId = playerId;
        this.avatar = avatar;
        this.playerName = playerName;
        this.mpHighScoreCount = mpHighScoreCount;
    }

    public int getPlayerId(){
        return playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getMpHighScoreCount(){
        return mpHighScoreCount;
    }
}