package HelperClasses;

public class Player {

    private String playerName;

    private int rank;

    private boolean ignoreTraitor;

    private boolean isPlaying;

    private int internalID;

    public Player(int internalID, String playerName, int rank) {
        this.internalID = internalID;
        this.playerName = playerName;
        this.rank = rank;
    }

    public Player(int internalID, String playerName, int rank, boolean ignoreTraitor, boolean isPlaying) {
        this.internalID = internalID;
        this.playerName = playerName;
        this.rank = rank;
        this.ignoreTraitor = ignoreTraitor;
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

    public boolean getIgnoreTraitor() {
        return ignoreTraitor;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public int getInternalID() {
        return internalID;
    }
}
