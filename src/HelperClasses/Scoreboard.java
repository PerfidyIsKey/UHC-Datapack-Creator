package HelperClasses;
import Enums.*;

public class Scoreboard {
    // Constructor
    public Scoreboard() {

    }

    private String Standard(String content) {
        return "scoreboard " + content;
    }

    /*
        scoreboard players
    */
    // players set
    public String Set(Entity entity, ScoreboardObjective objective, int value) {
        return Standard("players " + entity.getEntity() + " " + objective.getName() + " " + value);
    }

}
