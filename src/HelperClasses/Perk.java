package HelperClasses;

import Enums.Sound;

public class Perk {

    private int id;
    private String reward;
    private Sound sound;
    private int activationTime;

    private String rewardType;

    public Perk(int id, String reward, String rewardType, Sound sound, int activationTime) {
        this.id = id;
        this.reward = reward;
        this.rewardType = rewardType;
        this.sound = sound;
        this.activationTime = activationTime;
    }

    public String getReward() {
        return reward;
    }

    public Sound getSound() {
        return sound;
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
