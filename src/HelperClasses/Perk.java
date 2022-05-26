package HelperClasses;

public class Perk {

    private int id;
    private String reward;
    private String soundEffect;
    private int activationTime;

    private String rewardType;

    public Perk(int id, String reward, String rewardType, String soundEffect, int activationTime) {
        this.id = id;
        this.reward = reward;
        this.rewardType = rewardType;
        this.soundEffect = soundEffect;
        this.activationTime = activationTime;
    }

    public String getReward() {
        return reward;
    }

    public String getSoundEffect() {
        return soundEffect;
    }

    public int getActivationTime() {
        return activationTime;
    }

    public String getRewardType() {
        return rewardType;
    }

    public int getId() {
        return id;
    }
}
