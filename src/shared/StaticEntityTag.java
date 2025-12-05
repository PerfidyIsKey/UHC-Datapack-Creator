package shared;

/**
 * Enum for type-safe representation of fixed, predefined Minecraft entity tags.
 */
public enum StaticEntityTag implements EntityTag {
    AM_I_WINNING("AmIWinning"),
    CARE_PACKAGE("CarePackage"),
    CARE_PACKAGES_DROPPED("CarePackagesDropped"),
    COLLAR_CHECK("CollarCheck"),
    CONTROL_POINT_CAPTURED("ControlPointCaptured"),
    CONTROL_POINT_1_ENABLED("ControlPoint1Enabled"),
    CONTROL_POINT_2_ENABLED("ControlPoint2Enabled"),
    DEBUG("Debug"),
    DONT_MAKE_TRAITOR("DontMakeTraitor"),
    GAME_STARTED("GameStarted"),
    IRON_MAN("IronMan"),
    IRON_MAN_CANDIDATE("IronManCandidate"),
    IS_FLYING("IsFlying"),
    LOOKING_FOR_TEAM_MATE("LookingForTeamMate"),
    PREDICTION_CANDIDATE("PredicationCandidate"),
    PREDICTIONS_COMPLETED("PredictionsCompleted"),
    RESPAWN("Respawn"),
    RESPAWN_DISABLED("RespawnDisabled"),
    TRAITOR("Traitor"),
    TRAITORS_ASSIGNED("TraitorsAssigned");

    private final String tagName;

    StaticEntityTag(String tagName) {
        this.tagName = tagName;
    }

    /**
     * Gets the raw string value required for the NBT Tag List.
     */
    public String getTagName() {
        return tagName;
    }

    @Override
    public String toString() { return tagName; }
}