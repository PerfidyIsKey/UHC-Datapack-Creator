import Enums.GameMode;
import FileGeneration.FileTools;
import HelperClasses.Player;

import java.util.*;

//TODO: Create team files .mcfunction.
public class TeamGenerator {
    private List<Player> players = new ArrayList<>();
    private int playerAmount = 0;
    private int teamAmount;

    private boolean allPlayers = false;

    private int iterationsPerRun = 3000;
    private int iterations = 1000;

    private int averageRank;

    private int teamRankMargin = 20;
    private ArrayList<TeamGeneratorTeam> teams = new ArrayList<>();


    public static void main(String[] args) {
        new TeamGenerator().run();
    }

    public void run() {
        insertNewPlayers();
        if (playerAmount == 2 && isDivisible()) {
            generateTeamsOfTwo();
        } else {
            teams = generateFairTeamWithIterations(generateFairTeams(), iterationsPerRun, averageRank * playerAmount, averageRank * playerAmount, averageRank * playerAmount);
            //Change depth of 3 to use Collection sort. Results in max-depth.
            for (int i = 0; i < iterations; i++) {
                int highest = 0;
                int second = 0;
                int third = 0;
                for (TeamGeneratorTeam t : teams) {
                    int diff = t.getTotalRank();
                    if (diff < 0) diff = diff * -1;
                    if (diff > highest) {
                        third = second;
                        second = highest;
                        highest = diff;
                    } else if (diff > second) {
                        third = second;
                        second = diff;
                    } else if (diff > third) {
                        third = diff;
                    }
                }
                teams = generateFairTeamWithIterations(teams, iterationsPerRun, highest, second, third);
            }
        }


        displayTeams();
    }

    private void generateTeamsOfTwo() {
        ArrayList<Player> players = new ArrayList<>(this.players);
        Collections.sort(players, Comparator.comparing(Player::getRank));
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

    private ArrayList<TeamGeneratorTeam> generateFairTeamWithIterations(ArrayList<TeamGeneratorTeam> bestTeams, int iterations, int first, int second, int third) {
        int margin = 1;
        if (iterations == 0) {
            return bestTeams;
        }
        ArrayList<TeamGeneratorTeam> teams = generateFairTeams();
        int maxFirst = 0;
        int maxSecond = 0;
        int maxThird = 0;
        for (TeamGeneratorTeam t : teams) {
            int teamDiff = t.getTotalRank() - averageRank * playerAmount;
            if (teamDiff < 0) teamDiff = teamDiff * -1;
            if (teamDiff > maxFirst) {
                maxThird = maxSecond;
                maxSecond = maxFirst;
                maxFirst = teamDiff;
            } else if (teamDiff > maxSecond) {
                maxThird = maxSecond;
                maxSecond = teamDiff;
            } else if (teamDiff > maxThird) {
                maxThird = teamDiff;
            }
        }
        if (first + margin >= maxFirst && maxFirst >= first - margin && second + margin >= maxSecond && maxSecond >= second - margin) {
            if (maxThird < third) {
                return generateFairTeamWithIterations(teams, iterations - 1, maxFirst, maxSecond, maxThird);
            } else {
                return generateFairTeamWithIterations(bestTeams, iterations - 1, first, second, maxThird);
            }
        }
        if (first + margin >= maxFirst && maxFirst >= first - margin) {
            if (maxSecond < second) {
                return generateFairTeamWithIterations(teams, iterations - 1, maxFirst, maxSecond, maxThird);
            } else {
                return generateFairTeamWithIterations(bestTeams, iterations - 1, first, second, maxThird);
            }
        }
        if (maxFirst < first) {
            return generateFairTeamWithIterations(teams, iterations - 1, maxFirst, maxSecond, maxThird);
        } else {
            return generateFairTeamWithIterations(bestTeams, iterations - 1, first, second, maxThird);
        }
    }

    private void insertNewPlayers() {
        FileTools fileTools = new FileTools();
        ArrayList<String> playersString = fileTools.GetLinesFromFile("Files\\" + GameMode.DIORITE + "\\players.txt");
        for (String player : playersString) {
            String[] playerSplit = fileTools.splitLineOnComma(player);
            players.add(new Player(playerSplit[0], Integer.parseInt(playerSplit[1]), Boolean.parseBoolean(playerSplit[2]), Boolean.parseBoolean(playerSplit[3])));
        }
        if (!allPlayers) {
            removeInactivePlayers();
        } else {
            iterationsPerRun = iterationsPerRun / 10;
        }
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
            teams.add(new TeamGeneratorTeam(tempList, playerAmount));
        }
        if (!isDivisible()) {
            Collections.sort(teams, Comparator.comparing(TeamGeneratorTeam::getTotalRank));
            int count = 0;
            while (temp.size() > 0) {
                Collections.sort(temp, Comparator.comparing(Player::getRank));
                Player player = temp.get(temp.size() - 1);
                teams.get(count).addPlayer(player);
                temp.remove(player);
                count++;
            }
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

    private void determineAmountOfPlayersPerTeam() {
        if ((players.size() % 4) == 0 && players.size() >= 16) playerAmount = 4;
        else if ((players.size() % 2) == 0) playerAmount = 2;
        else if ((players.size() % 3) == 0) playerAmount = 3;

        else if (players.size() % 3 == 1) playerAmount = 3;
        else playerAmount = 2;
    }
}


