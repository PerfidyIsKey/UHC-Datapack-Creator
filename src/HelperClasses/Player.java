package HelperClasses;

public class Player {

    private String playerName;

    private int rank;

    private boolean ignoreTraitor;

    public Player(String playerName, int rank) {
        this.playerName = playerName;
        this.rank = rank;
    }

    public Player(String playerName, int rank, boolean ignoreTraitor) {
        this.playerName = playerName;
        this.rank = rank;
        this.ignoreTraitor = ignoreTraitor;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) { this.playerName = playerName; }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public boolean getIgnoreTraitor() {
        return ignoreTraitor;
    }

    public void setIgnoreTraitor(boolean admin) {
        this.ignoreTraitor = ignoreTraitor;
    }
}
