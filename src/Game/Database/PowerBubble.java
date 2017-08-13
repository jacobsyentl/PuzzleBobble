package Game.Database;

import javafx.scene.paint.ImagePattern;

public class PowerBubble extends Bubble{
    private int bubblePowerId, isDroppable, isProjectile, timeConstraint, gameParameterId;
    private String activationType;

    public PowerBubble(Bubble bubble, int bubblePowerId, int isDroppable, int isProjectile, String activationType, int timeConstraint, int gameParameterId, ImagePattern fill){
        super(bubble.getBubbleGameId(), bubble.getBubbleName(), bubble.getBubbleScore(), bubble.getUrl(), bubble.getBubblePowerId(), null);
        this.bubblePowerId = bubblePowerId;
        this.isDroppable = isDroppable;
        this.isProjectile = isProjectile;
        this.activationType = activationType;
        this.timeConstraint = timeConstraint;
        this.gameParameterId = gameParameterId;
    }


    @Override
    public int getBubblePowerId() {
        return bubblePowerId;
    }

    @Override
    public void setBubblePowerId(int bubblePowerId) {
        this.bubblePowerId = bubblePowerId;
    }

    public int getIsDroppable() {
        return isDroppable;
    }

    public void setIsDroppable(int isDroppable) {
        this.isDroppable = isDroppable;
    }

    public int getIsProjectile() {
        return isProjectile;
    }

    public void setIsProjectile(int isProjectile) {
        this.isProjectile = isProjectile;
    }

    public int getTimeConstraint() {
        return timeConstraint;
    }

    public void setTimeConstraint(int timeConstraint) {
        this.timeConstraint = timeConstraint;
    }

    public int getGameParameterId() {
        return gameParameterId;
    }

    public void setGameParameterId(int gameParameterId) {
        this.gameParameterId = gameParameterId;
    }

    public String getActivationType() {
        return activationType;
    }

    public void setActivationType(String activationType) {
        this.activationType = activationType;
    }
}