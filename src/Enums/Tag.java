package Enums;

public enum Tag {
    AttackingCP("AttackingCP"),
    CollarCheck("CollarCheck"),
    DontMakeTraitor("DontMakeTraitor"),
    IronMan("IronMan"),
    IronManCandidate("IronManCandidate"),
    LookingForTeamMate("LookingForTeamMate"),
    ReceivedPerk("ReceivedPerk"),
    Respawn("Respawn"),
    RespawnDisabled("RespawnDisabled"),
    Traitor("Traitor");

    private final String symbol;

    Tag(String symbol) {
        this.symbol = symbol;
    }

    public String extendName(int number) {
        return symbol + number;
    }

    public String extendName(String content) {
        return symbol + content;
    }
}
