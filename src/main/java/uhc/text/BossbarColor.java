package uhc.text;

public enum BossbarColor implements ColorType{
    BLUE,
    GREEN,
    PINK,
    PURPLE,
    RED,
    WHITE,
    YELLOW;

    @Override
    public String getColor() {
        return this.name().toLowerCase();
    }

    @Override
    public String toString() {
        return getColor();
    }
}
