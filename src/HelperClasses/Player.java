package HelperClasses;

public class Player {

    private String playerName;

    private int rank;

    private double lastTraitorSeason;

    private boolean isPlaying;

    private int internalID;

    public Player(int internalID, String playerName, int rank) {
        this.internalID = internalID;
        this.playerName = playerName;
        this.rank = rank;
    }

    public Player(int internalID, String playerName, int rank, double lastTraitorSeason, boolean isPlaying) {
        this.internalID = internalID;
        this.playerName = playerName;
        this.rank = rank;
        this.lastTraitorSeason = lastTraitorSeason;
        this.isPlaying = isPlaying;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public double getLastTraitorSeason() {
        return lastTraitorSeason;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public int getInternalID() {
        return internalID;
    }
}
