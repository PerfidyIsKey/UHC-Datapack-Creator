package Enums;

public enum TagTemp {
    AmIWinning("AmIWinning"),
    AttackingCP("AttackingCP"),
    Capping("Capping"),
    CarePackagesDropped("CarePackagesDropped"),
    CollarCheck("CollarCheck"),
    ControlPoint1Enabled("ControlPoint1Enabled"),
    ControlPoint2Enabled("ControlPoint2Enabled"),
    ControlPointCaptured("ControlPointCaptured"),
    CP("CP"),
    CPAbandon("CPAbandon"),
    CPAttack("CPAttack"),
    Debug("Debug"),
    DontMakeTraitor("DontMakeTraitor"),
    GameStarted("GameStarted"),
    IronMan("IronMan"),
    IronManCandidate("IronManCandidate"),
    IsFlying("IsFlying"),
    LookingForTeamMate("LookingForTeamMate"),
    OnCP("OnCP"),
    PredictionCandidate("PredictionCandidate"),
    PredictionsCompleted("PredictionsCompleted"),
    ReceivedPerk("ReceivedPerk"),
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
