package TeamGeneration;

import Enums.FileName;
import Enums.GameMode;
import FileGeneration.FileData;
import FileGeneration.FileTools;
import HelperClasses.Player;
import HelperClasses.PlayerConnection;
import HelperClasses.Team;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class TeamGenerator {

    private Season currentSeason;
    private List<Player> players = new ArrayList<>();
    private List<Season> seasons = new ArrayList<>();
    private List<TeamGeneratorTeam> preAssignedTeams = new ArrayList<>();

    private List<PlayerConnection> playerConnections = new ArrayList<>();
    private int playerAmount = 3;
    private int teamAmount;

    private ArrayList<Team> teams;

    private int iterationsPerRun = 3000;
    private int iterations = 1000;

    private int averageRank;

    ArrayList<ArrayList<TeamGeneratorTeam>> teamss = new ArrayList<>();
    private int teamRankMargin = 20;
    FileTools fileTools = new FileTools();

    String fileLocation;
    String[] args;

    GameMode gameMode;

    private double seasonID;

    public TeamGenerator(double seasonID, String fileLocation, ArrayList<Team> teams, GameMode gameMode) {
        this.seasonID = seasonID;
        this.teams = teams;
        this.fileLocation = fileLocation;
        this.gameMode = gameMode;
    }

    public void run() {
        execute(false);
    }

    public void run(String[] args) {
        this.args = args;
        execute(true);
    }

    public void execute(boolean auto) {
        importPlayers();
        establishSeason();
        importSeasons();
        playerConnections = determinePlayerConnections();
        teamss.add(generateTeams(teamRankMargin, playerAmount, iterationsPerRun));
        ArrayList<TeamGeneratorTeam> teams;
        if (!auto) {
            for (int i = 0; i < 5; i++) {
                teamss.add(generateTeams(teamRankMargin + i * 3, playerAmount, 300));
            }
            Scanner scanner = new Scanner(System.in);
            System.out.println("Choose a team composition. [num]");
            int input = scanner.nextInt();
            teams = chooseTeams(input);
        } else {
            teams = chooseTeams(0);
        }
        boolean update = false;
        if (!auto) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Should playerconnections be updated in official files? (y/n)");
            String input = scanner.nextLine();
            if (input.equals("y")) update = true;
        } else if (this.args[0].equals("-y")) update = true;

        updatePlayerConnections(teams, update);
    }

    private void establishSeason() {
        currentSeason = new Season(seasonID,players.size(), new Date());
    }


    //TODO: generate Season data in files.
    private void updatePlayerConnections(ArrayList<TeamGeneratorTeam> teams, boolean shouldUpdate) {
        String fileLocation = "Files\\" + gameMode + "\\";
        String fileName = "playerConnections";
        if (!shouldUpdate) {
            try {
                fileTools.makeFileCopy(fileLocation, fileName);
            } catch (IOException e) {
                System.out.println("Error duplicating file");
            } finally {
                fileName = "playerConnections_copy";
            }
        }
        ArrayList<PlayerConnection> playerConnections = new ArrayList<>();
        for (TeamGeneratorTeam team : teams) {
            playerConnections.addAll(team.updatePlayerConnections(currentSeason));
        }
        ArrayList<PlayerConnection> playerConnectionsToRemove = new ArrayList<>();
        for (PlayerConnection playerConnection : playerConnections) {
            if (playerConnection.getTimesPlayedTogether() == 1) {
                try {
                    fileTools.createPlayerConnection(fileLocation, fileName, playerConnection);
                    playerConnectionsToRemove.add(playerConnection);
                } catch (IOException e) {
                    System.out.println("There was an error creating a player connection");
                }
            }
        }
        playerConnections.remove(playerConnectionsToRemove);
        try {
            fileTools.updatePlayerConnections(fileLocation, fileName, playerConnections);
        } catch (IOException e) {
            System.out.println("There was an error updating player connections.");
        }
    }

    private ArrayList<TeamGeneratorTeam> chooseTeams(int num) {
        if (num <= 0) {
            num = 1;
        }

        try {
            ArrayList<TeamGeneratorTeam> teams = teamss.get(num - 1);
            teams.addAll(preAssignedTeams);
            System.out.println("Teams chosen: " + num);
            displayTeams(teams, 0);
            try {
                fileTools.writeFile(generateTeamFile(teams), fileLocation);
            } catch (IOException e) {
                System.out.println("Error writing team file");
            }
            return teams;
        } catch (Exception e) {
            return chooseTeams(teamss.size());
        }
    }

    private ArrayList<TeamGeneratorTeam> preAssignedTeams() {
        ArrayList<TeamGeneratorTeam> teamGeneratorTeams = new ArrayList<>();

        ArrayList<String> preAssignedString = fileTools.GetLinesFromFile("Files\\" + gameMode + "\\preAssignedTeams.txt");
        for (String team : preAssignedString) {
            String[] teamSplit = fileTools.splitLineOnComma(team);

            ArrayList<Player> playerArray = new ArrayList<>();
            for (String playerID : teamSplit ) {
                playerArray.add(getPlayerByInternalID(Integer.parseInt(playerID)));
            }


            teamGeneratorTeams.add(new TeamGeneratorTeam(playerArray, playerAmount, playerConnections));
        }

        return teamGeneratorTeams;
    }

    private FileData generateTeamFile(ArrayList<TeamGeneratorTeam> teamGeneratorTeams) {
        ArrayList<String> fileCommands = new ArrayList<>();
        for (int i = 0; i < teamGeneratorTeams.size(); i++) {
            for (Player p : teamGeneratorTeams.get(i).getPlayers()) {
                fileCommands.add("team join " + teams.get(i).getName() + " " + p.getPlayerName());
            }
        }
        return new FileData(FileName.teams, fileCommands);
    }

    private ArrayList<TeamGeneratorTeam> generateTeams(int teamRankMargin, int playerAmount, int iterationsPerRun) {
        this.teamRankMargin = teamRankMargin;
        this.playerAmount = playerAmount;
        this.iterationsPerRun = iterationsPerRun;
        ArrayList<TeamGeneratorTeam> teams;
        teams = generateFairTeamWithIterations(generateFairTeams(), iterationsPerRun);
        for (int i = 0; i < iterations; i++) {
            teams = generateFairTeamWithIterations(teams, iterationsPerRun);
        }
        displayTeams(teams, iterationsPerRun);
        return teams;
    }

    private void importPlayers() {
        ArrayList<String> playersString = fileTools.GetLinesFromFile("Files\\" + gameMode + "\\players.txt");
        for (String player : playersString) {
            String[] playerSplit = fileTools.splitLineOnComma(player);
            players.add(new Player(Integer.parseInt(playerSplit[0]), playerSplit[1], Integer.parseInt(playerSplit[2]), Double.parseDouble(playerSplit[3]), Boolean.parseBoolean(playerSplit[4])));
        }

        preAssignedTeams = preAssignedTeams();  // Add pre assigned teams

        removeInactivePlayers();
        determineAmountOfPlayersPerTeam();

        averageRank = getAverageRankOfPlayers();
        teamAmount = calcTeamAmount();
    }

    private void importSeasons() {
        ArrayList<String> seasonsString = fileTools.GetLinesFromFile("Files\\" + gameMode + "\\seasonData.txt");
        for (String season : seasonsString) {
            String[] seasonSplit = fileTools.splitLineOnComma(season);
            seasons.add(new Season(Double.parseDouble(seasonSplit[0]), Integer.parseInt(seasonSplit[1]), new Date(Integer.parseInt(seasonSplit[2]), Integer.parseInt(seasonSplit[3]), Integer.parseInt(seasonSplit[4]))));
        }
    }

    private void removeInactivePlayers() {
        ArrayList<Player> temp = new ArrayList<>(players);
        for (Player player : temp) {
            if (!player.isPlaying()) {
                players.remove(player);
            }
        }

        // Remove players from pre assigned teams
        ArrayList<Player> temp2 = preAssignedTeams.stream().map(TeamGeneratorTeam::getPlayers).findAny().orElse(null);
        if (temp2 != null && !temp2.isEmpty()) players.remove(temp2);
    }

    private int getAverageRankOfPlayers() {
        int total = 0;
        for (Player player : players) {
            total += player.getRank();
        }
        return total / players.size();
    }

    private ArrayList<PlayerConnection> determinePlayerConnections() {
        ArrayList<PlayerConnection> playerConnections = new ArrayList<>();
        ArrayList<String> playerConnectionsString = fileTools.GetLinesFromFile("Files\\" + gameMode + "\\playerConnections.txt");
        for (String string : playerConnectionsString) {
            String seasons = string.split("\\[")[1];
            seasons = seasons.substring(0, seasons.length() - 1);
            String[] playerConnectionsSplit = fileTools.splitLineOnComma(string);
            String[] seasonsSplit = fileTools.splitLineOnComma(seasons);
            Player player1 = getPlayerByInternalID(Integer.parseInt(playerConnectionsSplit[0]));
            Player player2 = getPlayerByInternalID(Integer.parseInt(playerConnectionsSplit[1]));
            if (player1 != null && player2 != null) {
                List<Season> connectionSeasons = new ArrayList<>();
                for (String s:seasonsSplit) {
                    connectionSeasons.add(getSeasonByID(Double.parseDouble(s)));
                }
                playerConnections.add(new PlayerConnection(player1, player2, connectionSeasons));
            }
        }
        playerConnections.sort(Comparator.comparing(PlayerConnection::getTimesPlayedTogether));
        return playerConnections;
    }

    private Season getSeasonByID(double ID){
        for (Season season: seasons) {
            if(season.getID() == ID) return season;
        }
        return null;
    }

    private Player getPlayerByInternalID(int internalID) {
        for (Player player : players) {
            if (player.getInternalID() == internalID) {
                return player;
            }
        }
        return null;
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
                if (newTeamsMeta.size() != i+1 && newTeamsMeta.get(i+1) < bestTeamsMeta.get(i+1)) {
                    return generateFairTeamWithIterations(newTeams, iterations - 1);
                } else {
                    return generateFairTeamWithIterations(bestTeams, iterations - 1);
                }
            }
        }
        return generateFairTeamWithIterations(bestTeams, iterations - 1);
    }

    private ArrayList<Integer> getMetaData(ArrayList<TeamGeneratorTeam> teams) {
        teams.sort(Comparator.comparing(TeamGeneratorTeam::getTotalRank));
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
            teams.add(new TeamGeneratorTeam(tempList, playerAmount, playerConnections));
        }
        if (!isDivisible()) {
            teams.sort(Comparator.comparing(TeamGeneratorTeam::getTotalRank));
            int count = 0;
            while (!temp.isEmpty()) {
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
        players.sort(Comparator.comparing(Player::getRank));
    }

    private void sortPlayersHighToLow(ArrayList<Player> players) {
        sortPlayers(players);
        Collections.reverse(players);
    }

    private void determineAmountOfPlayersPerTeam() {
        if ((players.size() % 4) == 0 && players.size() >= 16) playerAmount = 4;
        else if ((players.size() % 3) == 0) playerAmount = 3;
        else if ((players.size() % 2) == 0) playerAmount = 2;
        else if (players.size() % 3 == 1) playerAmount = 3;
        else playerAmount = 2;
    }

    private void displayTeams(ArrayList<TeamGeneratorTeam> teams, int iterationsPerRun) {
        String iterations = "";
        if (iterationsPerRun != 0) {
            iterations = stringFormat(iterationsPerRun * this.iterations) + " iterations checked.";
        }
        int totalRank = 0;
        for (TeamGeneratorTeam team : teams) {
            totalRank += team.getTotalRank();
        }
        System.out.println("Average rank for teams: " + totalRank / teamAmount + " With a total of " + players.size() + " players." + " " + iterations);
        for (TeamGeneratorTeam team : teams) {
            System.out.println(team.getDisplayString(totalRank / teamAmount));
        }
        System.out.println();
    }

    private String stringFormat(int num) {
        String numString = num + "";
        int length = numString.length();
        int interval = length % 3;
        StringBuilder sb = new StringBuilder(numString);
        int amountOfDots = length / 3;
        for (int i = 0; i < amountOfDots; i++) {
            if (interval == 0) {
                if (i != 0) {
                    sb.insert((interval + i * 4) - 1, '.');
                }
            } else {
                sb.insert(interval + i * 4, '.');
            }
        }
        return sb.toString();
    }
}


