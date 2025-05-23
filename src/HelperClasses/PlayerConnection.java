package HelperClasses;

import TeamGeneration.Season;

import java.util.ArrayList;
import java.util.List;

public class PlayerConnection {
    private Player player1;
    private Player player2;

    private List<Season> seasons;

    public PlayerConnection(Player player1, Player player2, List<Season> seasons) {
        this.player1 = player1;
        this.player2 = player2;
        this.seasons = seasons == null ? new ArrayList<>() : seasons;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public int getTimesPlayedTogether() {
        return seasons == null ? 0 : seasons.size();
    }

    public void addSeason(Season season) {
        this.seasons.add(season);
    }

    public List<Season> getSeasons() {
        return seasons;
    }

    public PlayerConnection getPlayerConnection(Player player) {
        if (player.getInternalID() == this.player1.getInternalID() || player.getInternalID() == this.player2.getInternalID()) {
            if (player1.isPlaying() && player2.isPlaying()) {
                return this;
            }
        }
        return null;
    }

    public PlayerConnection getPlayerConnection(Player player1, Player player2) {
        if (player1.getInternalID() == this.player1.getInternalID() && player2.getInternalID() == this.player2.getInternalID() || player2.getInternalID() == this.player1.getInternalID() && player1.getInternalID() == this.player2.getInternalID()) {
            if (player1.isPlaying() && player2.isPlaying()) {
                return this;
            }
        }
        return null;
    }
}
