package Game.Database;

public class Level {
    private int levelNumber;
    private String levelName, levelSeed;

    public Level(int levelNumber, String levelName, String levelSeed){
        this.levelNumber = levelNumber;
        this.levelName = levelName;
        this.levelSeed = levelSeed;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    private void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    public String getLevelSeed() {
        return levelSeed;
    }

    private void setLevelSeed(String levelSeed) {
        this.levelSeed = levelSeed;
    }

    @Override
    public String toString() {
        return levelNumber + " " + levelNumber + " " + levelSeed;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }
}
