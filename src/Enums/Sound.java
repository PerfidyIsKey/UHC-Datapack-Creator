package Enums;

public enum Sound {
    BASALT("ambient.basalt_deltas.mood"),
    CRIMSON("ambient.crimson_forest.mood"),
    WARPED("ambient.warped_forest.mood"),
    WITHER("entity.wither.spawn"),
    THUNDER("entity.lightning_bolt.thunder")
    ;

    private String symbol;

    Sound(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return "minecraft:" + symbol;
    }
}

