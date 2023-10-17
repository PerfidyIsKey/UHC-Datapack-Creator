
import HelperClasses.Player;

import java.util.ArrayList;

public class TeamGeneratorTeam {
    private ArrayList<Player> players;
    private int totalRank;

    public TeamGeneratorTeam(ArrayList<Player> players) {
        this.players = players;
        this.totalRank = generateTotalRank();
    }

    public int getTotalRank() {
        return totalRank;
    }

    private int generateTotalRank() {
        int total = 0;
        for (Player player : players) {
            total += player.getRank();
        }
        return total;
    }

    public String getPlayers() {
        StringBuilder result = new StringBuilder();
        for (Player player : players) {
            result.append(player.getPlayerName()).append(", ");
        }
        result.deleteCharAt(result.length() - 2);
        return result.toString();
    }

    public String getDisplayString(int averageRank) {
        int rankDiff = totalRank - averageRank;
        String rankDiffDisplay;
        if (rankDiff > 0) {
            rankDiffDisplay = "+" + rankDiff;
        } else {
            rankDiffDisplay = rankDiff + "";
        }

        return "[" + totalRank + "] " + rankDiffDisplay + " " + getPlayers();
    }
}
