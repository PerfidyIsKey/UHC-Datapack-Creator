package Enums;

public enum Tag {
    AttackingCP("AttackingCP"),
    CollarCheck("CollarCheck"),
    DontMakeTraitor("DontMakeTraitor"),
    ReceivedPerk("ReceivedPerk"),
    Respawn("Respawn"),
    RespawnDisabled("RespawnDisabled"),
    Traitor("Traitor"),
    IronManCandidate("IronManCandidate"),
    LookingForTeamMate("LookingForTeamMate");

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
