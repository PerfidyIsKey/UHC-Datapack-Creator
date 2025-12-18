package uhc.resource.bossbar;

public enum BossbarStyle {
    NOTCHED_6,
    NOTCHED_10,
    NOTCHED_12,
    NOTCHED_20,
    PROGRESS;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
