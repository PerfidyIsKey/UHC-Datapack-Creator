public class StatusEffect {

    private String effectName;

    private int duration;

    private int amplification;

    public StatusEffect(String effectName, int duration, int amplification){
        this.effectName = effectName;
        this.duration = duration;
        this.amplification = amplification;
    }

    public String getEffectName() {
        return effectName;
    }

    public void setEffectName(String effectName) { this.effectName = effectName; }

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
