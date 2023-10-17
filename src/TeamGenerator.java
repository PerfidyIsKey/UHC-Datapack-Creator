import Enums.GameMode;
import FileGeneration.FileTools;
import HelperClasses.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TeamGenerator {
    private List<Player> players = new ArrayList<>();
    private int playerAmount = 3;
    private int teamAmount;

    private int averageRank;

    private int teamRankMargin = 20;
    private ArrayList<TeamGeneratorTeam> teams = new ArrayList<>();


    public static void main(String[] args) {
        new TeamGenerator().run();
    }

    public void run() {
        insertNewPlayers();
        teams = generateFairTeamWithIterations(generateFairTeams(), 10, averageRank * playerAmount);
        displayTeams();
    }

    private ArrayList<TeamGeneratorTeam> generateFairTeamWithIterations(ArrayList<TeamGeneratorTeam> bestTeams, int iterations, int diff) {
        if (iterations == 0) {
            return bestTeams;
        }
        ArrayList<TeamGeneratorTeam> teams = generateFairTeams();
        int maxDiff = diff;
        for (TeamGeneratorTeam t : teams) {
            int teamDiff = t.getTotalRank() - averageRank * playerAmount;
            if (teamDiff < 0) teamDiff = teamDiff * -1;
            if (teamDiff > maxDiff) {
                maxDiff = teamDiff;
            }
        }
        if (maxDiff < diff) {
            return generateFairTeamWithIterations(teams, iterations - 1, maxDiff);
        } else {
            return generateFairTeamWithIterations(bestTeams, iterations - 1, diff);
        }

    }

    private void insertNewPlayers() {
        FileTools fileTools = new FileTools();
        ArrayList<String> playersString = fileTools.GetLinesFromFile("Files\\" + GameMode.DIORITE + "\\players.txt");
        for (String player : playersString) {
            String[] playerSplit = fileTools.splitLineOnComma(player);
            players.add(new Player(playerSplit[0], Integer.parseInt(playerSplit[1]), Boolean.parseBoolean(playerSplit[2]), Boolean.parseBoolean(playerSplit[3])));
        }
        removeInactivePlayers();
        averageRank = getAverageRankOfPlayers();
        teamAmount = calcTeamAmount();
    }

    private void removeInactivePlayers() {
        ArrayList<Player> temp = new ArrayList<>(players);
        for (Player player : temp) {
            if (!player.isPlaying()) {
                players.remove(player);
            }
        }
    }

    private int getAverageRankOfPlayers() {
        int total = 0;
        for (Player player : players) {
            total += player.getRank();
        }
        return total / players.size();
    }

    public void generateEqualSizeTeams() {
        ArrayList<Player> playersTemp = new ArrayList<>(players);
        Collections.shuffle(playersTemp);
        for (int i = 0; i < teamAmount; i++) {
            teams.add(new TeamGeneratorTeam(new ArrayList(playersTemp.subList(i * playerAmount, i * playerAmount + playerAmount))));
        }
    }

    private void displayTeams() {
        int totalRank = 0;
        for (TeamGeneratorTeam team : teams) {
            totalRank += team.getTotalRank();
        }
        System.out.println("Average rank for teams: " + totalRank / teamAmount + " With a total of " + players.size() + " players.");
        for (TeamGeneratorTeam team : teams) {
            System.out.println(team.getDisplayString(totalRank / teamAmount));
        }
    }

    private ArrayList<TeamGeneratorTeam> generateFairTeams() {
        ArrayList<TeamGeneratorTeam> teams = new ArrayList<>();
        ArrayList<Player> temp = sortPlayersHighToLow(new ArrayList<>(players));

        for (int i = 0; i < teamAmount; i++) {
            Player player = temp.get(0);
            temp.remove(0);
            ArrayList<Player> tempList = new ArrayList<>();
            tempList.add(player);
            for (int j = 0; j < playerAmount - 1; j++) {
                Player teamMate = getFairTeammate(temp, tempList, teamRankMargin);
                temp.remove(teamMate);
                tempList.add(teamMate);
            }
            if (!isDivisible() && i == teamAmount - 1) {
                tempList.addAll(temp);
            }
            teams.add(new TeamGeneratorTeam(tempList));
        }
        return teams;
    }

    private Player getFairTeammate(ArrayList<Player> temp, ArrayList<Player> players, int teamRankMargin) {
        ArrayList<Player> tempered = new ArrayList<>(temp);
        Collections.shuffle(tempered);
        int playerRank = 0;
        for (Player p : players) {
            playerRank += p.getRank();
        }

        int searchAmount = 0;
        if (playerRank < averageRank * playerAmount) {
            searchAmount = averageRank * playerAmount - playerRank;
        } else {
            searchAmount = 0;
        }
        if (searchAmount < 0) {
            searchAmount = 0;
        }
        int searchLowerBound = searchAmount - teamRankMargin;
        int searchHigherBound = searchAmount + teamRankMargin;
        if (searchLowerBound < 0) {
            searchLowerBound = 0;
        }
        for (Player p : tempered) {
            if (p.getRank() >= searchLowerBound && p.getRank() <= searchHigherBound) {
                return p;
            }
        }
        return getFairTeammate(tempered, players, teamRankMargin + 1);
    }

    private int calcTeamAmount() {
        return players.size() / playerAmount;
    }

    private boolean isDivisible() {
        return this.players.size() % playerAmount == 0;
    }

    private ArrayList<Player> sortPlayersHighToLow(ArrayList<Player> players) {
        Collections.sort(players, Comparator.comparing(Player::getRank));
        Collections.reverse(players);
        return players;
    }
}


