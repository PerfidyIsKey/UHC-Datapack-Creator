package Enums;

public enum Sound {
    BASALT("minecraft:ambient.basalt_deltas.mood"),
    CRIMSON("minecraft:ambient.crimson_forest.mood"),
    WARPED("minecraft:ambient.warped_forest.mood"),
    WITHER("minecraft:entity.wither.spawn"),

    THUNDER("minecraft:entity.lightning_bolt.thunder")
    ;

    private String value;

    Sound(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

