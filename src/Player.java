public class Player {

    private String playerName;

    private int rank;

    private boolean admin;

    public Player(String playerName, int rank) {
        this.playerName = playerName;
        this.rank = rank;
    }

    public Player(String playerName, int rank, boolean admin) {
        this.playerName = playerName;
        this.rank = rank;
        this.admin = admin;
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

    public boolean getAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
