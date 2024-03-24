package HelperClasses;

public class ScoreboardPlayers {
    // Attributes
    private String entity;
    private ScoreboardObjective objective;

    // Constructors
    public ScoreboardPlayers(String entity, ScoreboardObjective objective){
        this.entity = entity;
        this.objective = objective;
    }

    // Methods
    public String getEntity() {
        return this.entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public ScoreboardObjective getObjective() {
        return objective;
    }

    public void setObjective(ScoreboardObjective objective) {
        this.objective = objective;
    }

    public String getText() {
        return this.entity + " " + this.objective.getName();
    }
}
