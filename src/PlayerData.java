import java.util.ArrayList;

public class PlayerData {

    private int internalID;
    private ArrayList<Integer> kills;
    private ArrayList<Integer> positions;
    private ArrayList<Boolean> winners;

    private ArrayList<Boolean> participation;

    public PlayerData(int internalID, ArrayList<Integer> kills, ArrayList<Integer> positions, ArrayList<Boolean> winners, ArrayList<Boolean> participation) {
        this.internalID = internalID;
        this.kills = kills;
        this.positions = positions;
        this.winners = winners;
        this.participation = participation;
    }

    public int getInternalID() {
        return internalID;
    }

    public ArrayList<Integer> getKills() {
        return kills;
    }

    public ArrayList<Integer> getPositions() {
        return positions;
    }

    public ArrayList<Boolean> getWinners() {
        return winners;
    }

    public ArrayList<Boolean> getParticipation() {
        return participation;
    }
}
