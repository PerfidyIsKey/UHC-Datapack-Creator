package Enums;

public enum TagTemp {
    CarePackagesDropped("CarePackagesDropped"),
    CollarCheck("CollarCheck"),
    ControlPoint1Enabled("ControlPoint1Enabled"),
    ControlPoint2Enabled("ControlPoint2Enabled"),
    ControlPointCaptured("ControlPointCaptured"),
    CP("CP"),
    GameStarted("GameStarted"),
    PredictionCandidate("PredictionCandidate"),
    PredictionsCompleted("PredictionsCompleted"),
    Respawn("Respawn"),
    RespawnDisabled("RespawnDisabled"),
    Traitor("Traitor"),
    TraitorsAssigned("TraitorsAssigned");

    private final String symbol;

    TagTemp(String symbol) {
        this.symbol = symbol;
    }

    public String extendName(int number) {
        return symbol + number;
    }

    public String extendName(String content) {
        return symbol + content;
    }
}
