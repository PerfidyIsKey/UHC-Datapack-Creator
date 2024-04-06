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
        scoreboard objectives
    */
    // objectives setdisplay
    public String SetDisplay(ObjectiveDisplay display, ScoreboardObjective objective) {
        return Standard("objectives setdisplay " + display + " " + objective.getName());
    }

    /*
        scoreboard players
    */
    // players add
    public String Add(Entity entity, ScoreboardObjective objective, int value) {
        return Standard("players add " + entity.getEntity() + " " + objective.getName() + " " + value);
    }

    // players get
    public String Get(Entity entity, ScoreboardObjective objective) {
        return Standard("players get " + entity.getEntity() + " " + objective.getName());
    }

    // players operation
    public String Operation(Entity entity1, ScoreboardObjective objective1, String operator, Entity entity2, ScoreboardObjective objective2) {
        return Standard("players operation " + entity1.getEntity() + " " + objective1.getName() + " " + operator + " " + entity2.getEntity() + " " + objective2.getName());
    }

    // players reset
    public String Reset(Entity entity, ScoreboardObjective objective) {
        return Standard("players reset " + entity.getEntity() + " " + objective.getName());
    }

    public String Reset(Entity entity) {
        return Standard("players reset " + entity.getEntity());
    }

    // players set
    public String Set(Entity entity, ScoreboardObjective objective, int value) {
        return Standard("players set " + entity.getEntity() + " " + objective.getName() + " " + value);
    }

}
