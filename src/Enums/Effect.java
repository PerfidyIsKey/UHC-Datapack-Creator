package Enums;

public enum Effect {
    absorption(22),
    blindness(15),
    fire_resistance(12),
    glowing(24),
    haste(3),
    health_boost(21),
    invisibility(14),
    luck(26),
    nausea(9),
    poison(19),
    regeneration(10),
    resistance(11),
    saturation(23),
    slow_falling(28),
    slowness(2),
    speed(1),
    strength(5),
    weakness(18);

    private final int id;

    Effect(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }
}
