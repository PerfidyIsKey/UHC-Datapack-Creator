package HelperClasses;

import Enums.Effect;

public class StatusEffect {

    private Effect effect;

    private int duration;

    private int amplification;

    public StatusEffect(Effect effect, int duration, int amplification){
        this.effect = effect;
        this.duration = duration;
        this.amplification = amplification;
    }

    public Effect getEffect() {
        return this.effect;
    }

    public void setEffectName(Effect effect) { this.effect = effect; }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getAmplification() {
        return amplification;
    }

    public void setAmplification(int amplification) {
        this.amplification = amplification;
    }
}
