package shared.nbt;

public enum CustomEffectsKey implements NbtKey {
    ID,
    AMPLIFIER,
    DURATION,
    SHOW_PARTICLES,
    SHOW_ICON,
    AMBIENT;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
