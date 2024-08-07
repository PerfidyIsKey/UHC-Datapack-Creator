package TeamGeneration;

import HelperClasses.Player;
import HelperClasses.PlayerConnection;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TeamGeneratorTeam {
    private List<Player> players;
    private int teamSize;

    private final int extraMemberPoints = 30;

    private List<PlayerConnection> allPlayerConnections;
    private List<PlayerConnection> playerConnections;

    public TeamGeneratorTeam(ArrayList<Player> players) {
        this.players = players;
    }

    public TeamGeneratorTeam(ArrayList<Player> players, int teamSize, List<PlayerConnection> playerConnections) {
        this.players = players;
        this.teamSize = teamSize;
        this.allPlayerConnections = playerConnections;
        this.playerConnections = ensurePlayerConnections(filterPlayerConnections(playerConnections));
    }

    private List<PlayerConnection> filterPlayerConnections(List<PlayerConnection> playerConnections) {
        List<PlayerConnection> filteredPlayerConnections = new ArrayList<>();
        for (PlayerConnection playerConnection : playerConnections) {
            for (Player player : players) {
                if (playerConnection.getPlayer1().getInternalID() == player.getInternalID())
                    for (Player player2 : players) {
                        if (playerConnection.getPlayer2().getInternalID() == player2.getInternalID())
                            filteredPlayerConnections.add(playerConnection);
                    }
            }
        }
        return filteredPlayerConnections;
    }

    private List<PlayerConnection> ensurePlayerConnections(List<PlayerConnection> playerConnections) {
        for (Player player1 : players) {
            for (Player player2 : players) {
                if (player1.getInternalID() != player2.getInternalID()) {
                    int connections = 0;
                    for (PlayerConnection playerConnection : playerConnections) {
                        if (player1.getInternalID() == playerConnection.getPlayer1().getInternalID() && player2.getInternalID() == playerConnection.getPlayer2().getInternalID()) {
                            connections++;
                        } else if (player1.getInternalID() == playerConnection.getPlayer2().getInternalID() && player2.getInternalID() == playerConnection.getPlayer1().getInternalID()) {
                            connections++;
                        }
                    }
                    if (connections == 0) {
                        playerConnections.add(new PlayerConnection(player1, player2, null));
                    }
                }
            }
        }
        return playerConnections;
    }


    public int getTotalRank() {
        return generateTotalRank();
    }

    public void addPlayer(Player player) {
        this.players.add(player);
        try {
            this.playerConnections = ensurePlayerConnections(filterPlayerConnections(allPlayerConnections));
        } catch (Exception e) {

        }
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
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

    public List<Player> getPlayers() {
        return players;
    }

    private List<PlayerConnection> getPlayerConnections(Player player) {
        return playerConnections.stream().filter(playerConnection -> playerConnection.getPlayer1().getInternalID() == player.getInternalID() || playerConnection.getPlayer2().getInternalID() == player.getInternalID()).collect(Collectors.toList());
    }

    private int getHighestConnection(List<PlayerConnection> playerConnections) {
        if (playerConnections.isEmpty()) return 0;
        playerConnections.sort(Comparator.comparing(PlayerConnection::getTimesPlayedTogether));
        return playerConnections.get(playerConnections.size() - 1).getTimesPlayedTogether();
    }

    public String getPlayersString() {
        StringBuilder result = new StringBuilder();
        for (Player player : players) {
            result.append(player.getPlayerName()).append("{").append(getHighestConnection(getPlayerConnections(player))).append("}, ");
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

    public List<PlayerConnection> updatePlayerConnections(Season season) {
        playerConnections.forEach(playerConnection -> playerConnection.addSeason(season));
        return this.playerConnections;
    }
}
