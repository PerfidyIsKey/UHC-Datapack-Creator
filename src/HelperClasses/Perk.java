package HelperClasses;

import Enums.AttributeType;
import Enums.Sound;
import HelperClasses.Execute;

public class Perk {

    private int id;
    private StatusEffect effect;
    private Attribute attribute;
    private Sound sound;
    private int activationTime;

    public Perk(int id, StatusEffect reward, Sound sound, int activationTime) {
        this.id = id;
        this.effect = reward;
        this.sound = sound;
        this.activationTime = activationTime;
    }

    public Perk(int id, Attribute reward, Sound sound, int activationTime) {
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

    public Sound getSound() {
        return sound;
    }

    public int getActivationTime() {
        return activationTime;
    }

    public int getId() {
        return id;
    }
}
