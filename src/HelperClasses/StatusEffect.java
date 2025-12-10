package HelperClasses;

import uhc.resource.EffectId;

public class StatusEffect {

    private EffectId effect;

    private int duration;

    private int amplification;

    private Boolean hideParticles;

    public StatusEffect(EffectId effect, int duration, int amplification){
        this.effect = effect;
        this.duration = duration;
        this.amplification = amplification;
        this.hideParticles = false;
    }

    public StatusEffect(EffectId effect, int duration, int amplification, Boolean particles){
        this.effect = effect;
        this.duration = duration;
        this.amplification = amplification;
        this.hideParticles = particles;
    }

    public EffectId getEffectName() {
        return this.effect;
    }

    public void setEffectName(EffectId effect) { this.effect = effect; }

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

    public String giveEffect(String entity) {
        return "effect give " + entity + " " + effect + " " + duration + " " + amplification + " " + hideParticles;
    }
}
