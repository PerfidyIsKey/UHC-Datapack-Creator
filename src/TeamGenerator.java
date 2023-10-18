import Enums.GameMode;
import FileGeneration.FileTools;
import HelperClasses.Player;
import HelperClasses.PlayerConnection;
import HelperClasses.Team;

import java.util.*;

//TODO: Create team files .mcfunction.
public class TeamGenerator {
    private List<Player> players = new ArrayList<>();

    private List<PlayerConnection> playerConnections = new ArrayList<>();
    private int playerAmount = 3;
    private int teamAmount;

    private int maxTimesPlayedTogether = 3;
    private int highestPlayerConnection;

    private int iterationsPerRun = 3000;
    private int iterations = 1000;

    private int averageRank;

    private int teamRankMargin = 20;
    private ArrayList<TeamGeneratorTeam> teams = new ArrayList<>();

    FileTools fileTools = new FileTools();


    public static void main(String[] args) {
        new TeamGenerator().run();
    }

    public void run() {
        insertNewPlayers();
        determinePlayerConnections();
//        if (playerAmount == 2 && isDivisible()) {
//            generateTeamsOfTwo();
//        } else {
        teams = generateFairTeamWithIterations(generateFairTeams(), iterationsPerRun);
        for (int i = 0; i < iterations; i++) {
            teams = generateFairTeamWithIterations(teams, iterationsPerRun);
        }
//        }
        displayTeams();
    }

    private void insertNewPlayers() {
        ArrayList<String> playersString = fileTools.GetLinesFromFile("Files\\" + GameMode.DIORITE + "\\players.txt");
        for (String player : playersString) {
            String[] playerSplit = fileTools.splitLineOnComma(player);
            players.add(new Player(Integer.parseInt(playerSplit[0]), playerSplit[1], Integer.parseInt(playerSplit[2]), Boolean.parseBoolean(playerSplit[3]), Boolean.parseBoolean(playerSplit[4])));
        }

        removeInactivePlayers();
        determineAmountOfPlayersPerTeam();

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

    private void determinePlayerConnections() {
        ArrayList<String> playerConnectionsString = fileTools.GetLinesFromFile("Files\\" + GameMode.DIORITE + "\\playerConnections.txt");
        for (String string : playerConnectionsString) {
            String[] playerConnectionsSplit = fileTools.splitLineOnComma(string);
            Player player1 = getPlayerByInternalID(Integer.parseInt(playerConnectionsSplit[0]));
            Player player2 = getPlayerByInternalID(Integer.parseInt(playerConnectionsSplit[1]));
            if (player1 != null && player2 != null) {
                playerConnections.add(new PlayerConnection(player1, player2, Integer.parseInt(playerConnectionsSplit[2])));
            }
        }
        Collections.sort(playerConnections, Comparator.comparing(PlayerConnection::getTimesPlayedTogether));
        highestPlayerConnection = playerConnections.get(playerConnections.size() - 1).getTimesPlayedTogether();
    }

    private Player getPlayerByInternalID(int internalID) {
        for (Player player : players) {
            if (player.getInternalID() == internalID) {
                return player;
            }
        }
        return null;
    }

    private void generateTeamsOfTwo() {
        ArrayList<Player> players = new ArrayList<>(this.players);
        sortPlayers(players);
        for (int i = 0; i < teamAmount; i++) {
            ArrayList<Player> temp = new ArrayList<>();
            Player p1 = players.get(0);
            Player p2 = players.get(players.size() - 1);
            temp.add(p1);
            temp.add(p2);
            players.remove(p1);
            players.remove(p2);
            teams.add(new TeamGeneratorTeam(temp));
        }
    }

    private ArrayList<TeamGeneratorTeam> generateFairTeamWithIterations(ArrayList<TeamGeneratorTeam> bestTeams, int iterations) {
        int margin = 1;
        if (iterations == 0) {
            return bestTeams;
        }
        ArrayList<TeamGeneratorTeam> newTeams = generateFairTeams();
        ArrayList<Integer> newTeamsMeta = getMetaData(newTeams);
        ArrayList<Integer> bestTeamsMeta = getMetaData(bestTeams);
        for (int i = 0; i < newTeamsMeta.size(); i++) {
            if (!(bestTeamsMeta.get(i) + margin >= newTeamsMeta.get(i) && newTeamsMeta.get(i) >= bestTeamsMeta.get(i) - margin)) {
                if (newTeamsMeta.get(i) < bestTeamsMeta.get(i)) {
                    return generateFairTeamWithIterations(newTeams, iterations - 1);
                } else {
                    return generateFairTeamWithIterations(bestTeams, iterations - 1);
                }
            }
        }
        return generateFairTeamWithIterations(bestTeams, iterations - 1);
    }

    private ArrayList<Integer> getMetaData(ArrayList<TeamGeneratorTeam> teams) {
        Collections.sort(teams, Comparator.comparing(TeamGeneratorTeam::getTotalRank));
        Collections.reverse(teams);
        ArrayList<Integer> teamNums = new ArrayList<>();
        for (TeamGeneratorTeam team : teams) {
            teamNums.add(confirmPositiveValue(team.getTotalRank()));
        }
        return teamNums;
    }

    private int confirmPositiveValue(int num) {
        if (num < 0) return num * -1;
        else return num;
    }

    private ArrayList<TeamGeneratorTeam> generateFairTeams() {
        ArrayList<TeamGeneratorTeam> teams = new ArrayList<>();
        ArrayList<Player> temp = new ArrayList<>(players);
        sortPlayersHighToLow(temp);

        for (int i = 0; i < teamAmount; i++) {
            Player player = temp.get(0);
            temp.remove(0);
            ArrayList<Player> tempList = new ArrayList<>();
            tempList.add(player);
            for (int j = 0; j < playerAmount - 1; j++) {
                Player teamMate = getFairTeammate(temp, tempList, teamRankMargin, 0);
                temp.remove(teamMate);
                tempList.add(teamMate);
            }
            teams.add(new TeamGeneratorTeam(tempList, playerAmount));
        }
        if (!isDivisible()) {
            Collections.sort(teams, Comparator.comparing(TeamGeneratorTeam::getTotalRank));
            int count = 0;
            while (temp.size() > 0) {
                sortPlayers(temp);
                Player player = temp.get(temp.size() - 1);
                teams.get(count).addPlayer(player);
                temp.remove(player);
                count++;
            }
        }
        return teams;
    }

    private Player getFairTeammate(ArrayList<Player> temp, ArrayList<Player> players, int teamRankMargin, int timesPlayedMargin) {
        ArrayList<Player> tempered = new ArrayList<>(temp);
        Collections.shuffle(tempered);
        int playerRank = 0;
        for (Player p : players) {
            playerRank += p.getRank();
        }

        int searchAmount = 0;
        if (playerRank < averageRank * playerAmount) {
            searchAmount = averageRank * playerAmount - playerRank;
        }
        int searchLowerBound = searchAmount - teamRankMargin;
        int searchHigherBound = searchAmount + teamRankMargin;
        if (searchLowerBound < 0) {
            searchLowerBound = 0;
        }
        for (Player p : tempered) {
            if (p.getRank() >= searchLowerBound && p.getRank() <= searchHigherBound) {
                ArrayList<PlayerConnection> playerConnections = getPlayerConnection(players, p);
                int count = 0;
                for (PlayerConnection playerConnection : playerConnections) {
                    if (playerConnection != null) {
                        int[] t = getLowestAndHighestAmountPlayedTogether(p);
                        if (playerConnection.getTimesPlayedTogether() == t[1] + timesPlayedMargin && t[1] != t[0]) {
                            count++;
                        }
                    }
                }
                if (count == 0) {
                    return p;
                }
            }
        }
        if (teamRankMargin > this.teamRankMargin + 9) {
            return getFairTeammate(tempered, players, teamRankMargin + 1, timesPlayedMargin + 1);
        }
        return getFairTeammate(tempered, players, teamRankMargin + 1, timesPlayedMargin);
    }

    private int[] getLowestAndHighestAmountPlayedTogether(Player player) {
        ArrayList<PlayerConnection> playerConnections1 = new ArrayList<>();
        for (PlayerConnection playerConnection : this.playerConnections) {
            if (playerConnection.getPlayerConnection(player) != null) playerConnections1.add(playerConnection);
        }
        playerConnections1.sort(Comparator.comparing(PlayerConnection::getTimesPlayedTogether));
        return new int[]{playerConnections1.get(0).getTimesPlayedTogether(), playerConnections1.get(playerConnections1.size() - 1).getTimesPlayedTogether()};
    }

    private ArrayList<PlayerConnection> getPlayerConnection(ArrayList<Player> players, Player player2) {
        ArrayList<PlayerConnection> playerConnections = new ArrayList<>();
        for (Player player : players) {
            playerConnections.add(getPlayerConnection(player, player2));
        }
        return playerConnections;
    }

    private PlayerConnection getPlayerConnection(Player player1, Player player2) {
        for (PlayerConnection playerConnection : playerConnections) {
            if (playerConnection.getPlayerConnection(player1, player2) != null) {
                return playerConnection;
            }
        }
        return null;
    }

    private int calcTeamAmount() {
        return players.size() / playerAmount;
    }

    private boolean isDivisible() {
        return this.players.size() % playerAmount == 0;
    }

    private void sortPlayers(ArrayList<Player> players) {
        Collections.sort(players, Comparator.comparing(Player::getRank));
    }

    private void sortPlayersHighToLow(ArrayList<Player> players) {
        sortPlayers(players);
        Collections.reverse(players);
    }

    private void determineAmountOfPlayersPerTeam() {
        if ((players.size() % 4) == 0 && players.size() >= 16) playerAmount = 4;
        else if ((players.size() % 2) == 0) playerAmount = 2;
        else if ((players.size() % 3) == 0) playerAmount = 3;
        else if (players.size() % 3 == 1) playerAmount = 3;
        else playerAmount = 2;
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

    private void displayPlayerConnections() {
        for (PlayerConnection playerConnection : playerConnections) {
            System.out.println(playerConnection.getPlayer1().getPlayerName() + ", " + playerConnection.getPlayer2().getPlayerName() + ", " + playerConnection.getTimesPlayedTogether());
        }
    }
}


