package HelperClasses;

import Enums.*;

public class Scoreboard {
    // Constructor
    public Scoreboard() {}

    /*
     scoreboard players
    */
    // players add
    public String Add(String targets, ScoreboardObjective objective, int score) {
        return "scoreboard players add " + targets + " " + objective.getName() + " " + score;
    }
    public String Add(String targets, Objective objective, int score) {
        return "scoreboard players add " + targets + " " + objective + " " + score;
    }

    // players get
    public String Get(String target, ScoreboardObjective objective) {
        return "scoreboard players get " + target + " " + objective.getName();
    }
    public String Get(String target, Objective objective) {
        return "scoreboard players get " + target + " " + objective;
    }

    // players operation
    public String Operation(String targets, ScoreboardObjective targetObjective, ComparatorType operation, String source, ScoreboardObjective sourceObjective) {
        return "scoreboard players operation " + targets + " " + targetObjective.getName() + " " + operation + " " + source + " " + sourceObjective.getName();
    }

    public String Operation(String targets, Objective targetObjective, ComparatorType operation, String source, Objective sourceObjective) {
        return "scoreboard players operation " + targets + " " + targetObjective + " " + operation + " " + source + " " + sourceObjective;
    }

    // players reset
    public String Reset(String targets) {
        return "scoreboard players reset " + targets;
    }

    public String Reset(String targets, ScoreboardObjective objective) {
        return "scoreboard players reset " + targets + " " + objective.getName();
    }

    public String Reset(String targets, Objective objective) {
        return "scoreboard players reset " + targets + " " + objective;
    }

    // players set
    public String Set(String targets, ScoreboardObjective objective, int score) {
        return "scoreboard players set " + targets + " " + objective.getName() + " " + score;
    }

    public String Set(String targets, Objective objective, int score) {
        return "scoreboard players set " + targets + " " + objective + " " + score;
    }
}
