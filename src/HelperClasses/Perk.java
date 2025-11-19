package HelperClasses;

import shared.SoundId;

public class Perk {

    private int id;
    private StatusEffect effect;
    private Attribute attribute;
    private SoundId sound;
    private int activationTime;

    public Perk(int id, StatusEffect reward, SoundId sound, int activationTime) {
        this.id = id;
        this.effect = reward;
        this.sound = sound;
        this.activationTime = activationTime;
    }

    public Perk(int id, Attribute reward, SoundId sound, int activationTime) {
        this.id = id;
        this.attribute = reward;
        this.sound = sound;
        this.activationTime = activationTime;
    }

    public String getReward(String receiver) {
        String reward = "";
        Execute execute = new Execute();

        if (effect != null) {
            reward = effect.giveEffect(receiver);
        } else if (attribute != null) {
            reward = execute.As(new Entity(receiver)) +  attribute.setAttributeBase("@s");
        }
        else {
            reward = "say @a whoopsie Bassie did an oopsie hihi";
        }

        return reward;
    }

    public SoundId getSound() {
        return sound;
    }

    public int getActivationTime() {
        return activationTime;
    }

    public int getId() {
        return id;
    }
}
