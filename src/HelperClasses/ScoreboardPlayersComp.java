package HelperClasses;

public class ScoreboardPlayersComp {
    //Attributes
    private ScoreboardPlayers param1;   // First player to be compared
    private String operator;            // Comparison operator
    private ScoreboardPlayers param2;   // Second player to be compared

    // Constructors
    public ScoreboardPlayersComp(ScoreboardPlayers param1, String operator, ScoreboardPlayers param2) {
    this.param1 = param1;
    this.operator = operator;
    this.param2 = param2;
    }

    public String getComparison() {
        return this.param1.getText() + " " + this.operator + " " + this.param2.getText();
    }

}
