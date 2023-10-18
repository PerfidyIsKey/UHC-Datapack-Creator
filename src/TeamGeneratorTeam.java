
import HelperClasses.Player;

import java.util.ArrayList;

public class TeamGeneratorTeam {
    private ArrayList<Player> players;
    private int teamSize = 0;

    private int extraMemberPoints = 30;

    public TeamGeneratorTeam(ArrayList<Player> players) {
        this.players = players;
    }

    public TeamGeneratorTeam(ArrayList<Player> players, int teamSize) {
        this.players = players;
        this.teamSize = teamSize;
    }

    public int getTotalRank() {
        return generateTotalRank();
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    private int generateTotalRank() {
        int total = 0;
        for (Player player : players) {
            total += player.getRank();
        }
        if (teamSize != 0) {
            if (players.size() > teamSize) total += extraMemberPoints;
        }

        return total;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public String getPlayersString() {
        StringBuilder result = new StringBuilder();
        for (Player player : players) {
            result.append(player.getPlayerName()).append(", ");
        }
        result.deleteCharAt(result.length() - 2);
        return result.toString();
    }

    public String getDisplayString(int averageRank) {
        int rankDiff = getTotalRank() - averageRank;
        String rankDiffDisplay;
        if (rankDiff > 0) {
            rankDiffDisplay = "+" + rankDiff;
        } else {
            rankDiffDisplay = rankDiff + "";
        }

        return "[" + getTotalRank() + "] " + rankDiffDisplay + " " + getPlayersString();
    }
}
