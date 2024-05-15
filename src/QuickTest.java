import Enums.GameMode;
import FileGeneration.FileTools;
import HelperClasses.Player;
import HelperClasses.PlayerConnection;
import HelperClasses.Team;
import TeamGeneration.Season;
import TeamGeneration.TeamGenerator;
import TeamGeneration.TeamGeneratorTeam;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class QuickTest {

    private final GameMode gameMode = GameMode.DIORITE;
    ArrayList<Player> players = new ArrayList<>();
    ArrayList<PlayerConnection> playerConnections = new ArrayList<>();

    //TODO: Make teams from playerconnections and then use that to update TeamNumber.
    ArrayList<ArrayList<TeamGeneratorTeam>> teamsArray = new ArrayList<>();
    ArrayList<Season> seasons = new ArrayList<>();
    ArrayList<Integer> years = new ArrayList<>();
    ArrayList<Integer> months = new ArrayList<>();
    ArrayList<Integer> days = new ArrayList<>();

    ArrayList<PlayerData> playerDataArrayList = new ArrayList<>();

    FileTools fileTools = new FileTools();

    public static void main(String[] args) {
        new QuickTest().run();
    }

    public void run() {
        importPlayers();
        importSeasons();
        importPlayerData();
        playerConnections = determinePlayerConnections();
        determineTeamsFromConnections();
        json();
    }

    private void commaSeparated() {
        try {
            File file = new File("Files\\KINJIN\\players.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (Player player : players) {
                writer.write(player.getPlayerName() + ',' + player.getRank() + ',' + "player.getIgnoreTraitor()");
                writer.newLine();
            }
            writer.close();
        } catch (Exception e) {

        }
    }

    private void determineTeamsFromConnections() {
        ArrayList<ArrayList<TeamGeneratorTeam>> teamsArray = new ArrayList<>();
        for (Season season : seasons) {

            ArrayList<TeamGeneratorTeam> teams = new ArrayList<>();
            for (PlayerConnection playerconnection : playerConnections) {

                if (isConnectionInSeason(season, playerconnection.getSeasons())) {

                    TeamGeneratorTeam t1 = isPlayerInATeam(playerconnection.getPlayer1(), teams);
                    TeamGeneratorTeam t2 = isPlayerInATeam(playerconnection.getPlayer2(), teams);

                    if (t1 != null) {
                        if (playerconnection.getPlayer2() != null) {
                            t1.addPlayer(playerconnection.getPlayer2());
                            teams.set(teams.indexOf(t1), t1);
                        }

                    }
                    if (t2 != null) {
                        if (playerconnection.getPlayer1() != null) {
                            t2.addPlayer(playerconnection.getPlayer1());
                            teams.set(teams.indexOf(t2), t2);
                        }

                    }
                    if (t1 == null && t2 == null) {
                        ArrayList<Player> players = new ArrayList<>();
                        if (playerconnection.getPlayer1() != null) {
                            players.add(playerconnection.getPlayer1());
                        }
                        if (playerconnection.getPlayer2() != null) {
                            players.add(playerconnection.getPlayer2());
                        }
                        TeamGeneratorTeam team = new TeamGeneratorTeam(players);
                        teams.add(team);
                    }
//                    if(t1 != null) {
//                        removeCopy(t1,teams);
//                    }

                }

            }
            for (TeamGeneratorTeam team : teams) {
                List<Player> uniquePlayers = team.getPlayers().stream().distinct().toList();
                team.setPlayers(uniquePlayers);
            }

            teamsArray.add(teams);

        }
        this.teamsArray = teamsArray;
    }

    private void removeCopy(TeamGeneratorTeam team, ArrayList<TeamGeneratorTeam> teams) {
        TeamGeneratorTeam teamTemp = null;
        for (TeamGeneratorTeam t : teams) {
            int count = 0;
            for (Player p : t.getPlayers()) {
                if (team.getPlayers().contains(p)) {
                    count++;
                }
            }
            if (count == t.getPlayers().size() && count == team.getPlayers().size()) {
                teamTemp = t;
                break;
            }
        }
        teams.remove(teamTemp);
    }

    private boolean isConnectionInSeason(Season season, List<Season> seasons) {
        for (Season s : seasons) {
            if (s.getID() == season.getID()) return true;
        }
        return false;
    }

    private TeamGeneratorTeam isPlayerInATeam(Player player, ArrayList<TeamGeneratorTeam> teams) {
        if (player == null) return null;
        for (TeamGeneratorTeam team : teams) {
            if (isPlayerInTeam(player, team.getPlayers())) return team;
        }
        return null;
    }

    private boolean isPlayerInTeam(Player player, List<Player> players) {
        if (player == null) return false;
        for (Player p : players) {
            if (p.getInternalID() == player.getInternalID()) return true;
        }
        return false;
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
            if (player1 != null || player2 != null) {
                List<Season> connectionSeasons = new ArrayList<>();
                for (String s : seasonsSplit) {
                    connectionSeasons.add(getSeasonByID(Double.parseDouble(s)));
                }
                playerConnections.add(new PlayerConnection(player1, player2, connectionSeasons));
            }
        }
        playerConnections.sort(Comparator.comparing(PlayerConnection::getTimesPlayedTogether));
        return playerConnections;
    }

    private Player getPlayerByInternalID(int internalID) {
        return players.stream().filter(p -> p.getInternalID() == internalID).findAny().orElse(null);
    }

    private Season getSeasonByID(double ID) {
        return seasons.stream().filter(s -> s.getID() == ID).findAny().orElse(null);
    }

    private PlayerData getPlayerDataByID(double ID) {
        return playerDataArrayList.stream().filter(s -> s.getInternalID() == ID).findAny().orElse(null);
    }

    private void importSeasons() {
        ArrayList<String> seasonsString = fileTools.GetLinesFromFile("Files\\" + gameMode + "\\seasonData.txt");
        for (String season : seasonsString) {
            String[] seasonSplit = fileTools.splitLineOnComma(season);
            seasons.add(new Season(Double.parseDouble(seasonSplit[0]), Integer.parseInt(seasonSplit[1]), new Date(Integer.parseInt(seasonSplit[2]), Integer.parseInt(seasonSplit[3]), Integer.parseInt(seasonSplit[4]))));
            years.add(Integer.parseInt(seasonSplit[2]));
            months.add(Integer.parseInt(seasonSplit[3]));
            days.add(Integer.parseInt(seasonSplit[4]));
        }
    }

    private void importPlayerData() {
        ArrayList<String> string = fileTools.GetLinesFromFile("Files\\" + gameMode + "\\playerData.txt");
        for (String s : string) {
            String[] split = fileTools.splitLineOnComma(s);
            String[] split2 = s.split("\\[");

            ArrayList<Boolean> participation = new ArrayList<>();

            split2[1] = split2[1].replace("],", "");
            String[] split3 = fileTools.splitLineOnComma(split2[1]);
            for (String s3 : split3) {
                participation.add(Boolean.parseBoolean(s3));
            }

            ArrayList<Integer> kills = new ArrayList<>();

            split2[2] = split2[2].replace("],", "");
            String[] split4 = fileTools.splitLineOnComma(split2[2]);
            for (String s3 : split4) {
                kills.add(Integer.parseInt(s3));
            }

            ArrayList<Integer> positions = new ArrayList<>();

            split2[3] = split2[3].replace("]", "");
            String[] split5 = fileTools.splitLineOnComma(split2[3]);
            for (String s3 : split5) {
                if (s3.equals("NaN")) s3 = "0";
                positions.add(Integer.parseInt(s3));
            }


            ArrayList<Boolean> winners = new ArrayList<>();

            split2[4] = split2[4].replace("],", "");
            String[] split6 = fileTools.splitLineOnComma(split2[4]);
            for (String s3 : split6) {
                winners.add(Boolean.parseBoolean(s3));
            }


            playerDataArrayList.add(new PlayerData(Integer.parseInt(split[0]), kills, positions, winners, participation));
        }
    }

    private void importPlayers() {
        ArrayList<String> playersString = fileTools.GetLinesFromFile("Files\\" + gameMode + "\\players.txt");
        for (String player : playersString) {
            String[] playerSplit = fileTools.splitLineOnComma(player);
            players.add(new Player(Integer.parseInt(playerSplit[0]), playerSplit[1], Integer.parseInt(playerSplit[2]), Double.parseDouble(playerSplit[3]), Boolean.parseBoolean(playerSplit[4])));
        }
    }

    private TeamGeneratorTeam getTeamByPlayer(Player player, ArrayList<TeamGeneratorTeam> teams) {
        for (TeamGeneratorTeam team : teams) {
            if (team.getPlayers().contains(player)) return team;
        }
        return null;
    }

    private boolean hasPlayerConnection(PlayerConnection connection, Season season) {
        for (Season s : connection.getSeasons()) {
            if (s.getID() == season.getID()) return true;
        }
        return false;
    }

    private boolean doesListContainPlayer(List<Player> players, Player player) {
        for (Player p : players) {
            if (p.getInternalID() == player.getInternalID()) return true;
        }
        return false;
    }

    private void json() {
        File file = new File("Files\\" + GameMode.DIORITE + "\\playerData_JSON.txt");
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("[");

        int seasonCount = 0;
        for (Season season : seasons) {
            ArrayList<TeamGeneratorTeam> teams = teamsArray.get(seasonCount);
            for (TeamGeneratorTeam team : teams) {
                int count = 0;
                for (Player player : players) {
                    PlayerData playerData = getPlayerDataByID(player.getInternalID());
                    if (doesListContainPlayer(team.getPlayers(), player)) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("{");
                        stringBuilder.append("\"");
                        stringBuilder.append("PlayerId");
                        stringBuilder.append("\"");
                        stringBuilder.append(":");
                        stringBuilder.append("\"");
                        stringBuilder.append(count + 4);
                        stringBuilder.append("\"");
                        stringBuilder.append(",");
                        stringBuilder.append("\"");
                        stringBuilder.append("SeasonId");
                        stringBuilder.append("\"");
                        stringBuilder.append(":");
                        stringBuilder.append("\"");
                        stringBuilder.append(seasonCount + 4);
                        stringBuilder.append("\"");
                        stringBuilder.append(",");
                        stringBuilder.append("\"");
                        stringBuilder.append("TeamNumber");
                        stringBuilder.append("\"");
                        stringBuilder.append(":");
                        stringBuilder.append("\"");
                        stringBuilder.append(teams.indexOf(getTeamByPlayer(player, teams)));
                        stringBuilder.append("\"");
                        if (seasonCount < 52) {
                            stringBuilder.append(",");
                            stringBuilder.append("\"");
                            stringBuilder.append("kills");
                            stringBuilder.append("\"");
                            stringBuilder.append(":");
                            stringBuilder.append("\"");
                            stringBuilder.append(playerData.getKills().get(seasonCount));
                            stringBuilder.append("\"");
                            stringBuilder.append(",");
                            stringBuilder.append("\"");
                            stringBuilder.append("position");
                            stringBuilder.append("\"");
                            stringBuilder.append(":");
                            stringBuilder.append("\"");
                            stringBuilder.append(playerData.getPositions().get(seasonCount));
                            stringBuilder.append("\"");
                            stringBuilder.append(",");
                            stringBuilder.append("\"");
                            stringBuilder.append("haswon");
                            stringBuilder.append("\"");
                            stringBuilder.append(":");
                            stringBuilder.append("\"");
                            stringBuilder.append(playerData.getWinners().get(seasonCount));
                            stringBuilder.append("\"");
                        }
                        stringBuilder.append("}");
                        HttpClient client = HttpClient.newHttpClient();
                        HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create("http://localhost:8080/Connection"))
                                .POST(HttpRequest.BodyPublishers.ofString(stringBuilder.toString()))
                                .header("Content-Type", "application/json")
                                .build();

                        HttpResponse<String> response = null;
                        try {
                            response = client.send(request,
                                    HttpResponse.BodyHandlers.ofString());
                            if(!response.body().equals("Connection already exists.")) {
                                Thread.sleep(500);
                            }
                        } catch (IOException e) {
                            System.out.println("IOException");
                            throw new RuntimeException(e);
                        } catch (InterruptedException e) {
                            System.out.println("InterruptedException");
                            throw new RuntimeException(e);
                        }
                        System.out.println(response.body());
                    }
                    count++;


                }
            }
            seasonCount++;
        }
        stringBuilder2.setLength(stringBuilder2.length() - 1);
        stringBuilder2.append("]");
        System.out.println(stringBuilder2);

    }
}
