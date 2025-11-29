package HelperClasses;

import Enums.BossBarColor;
import shared.TextColor;
import shared.DyeColor;

public class Team {

    private int id;
    private String name;
    private TextColor color;
    private BossBarColor bossbarColor;
    private DyeColor dyeColor;
    private int collarColor;
    private String jsonColor;
    private String playerColor;
    private float[] dustColor;

    public Team(int id, TextColor color, BossBarColor bossbarColor, DyeColor dyeColor, int collarColor, String jsonColor, String playerColor, float[] dustColor) {
        this.id = id;
        this.name = "Team" + id;
        this.color = color;
        this.bossbarColor = bossbarColor;
        this.dyeColor = dyeColor;
        this.collarColor = collarColor;
        this.jsonColor = jsonColor;
        this.playerColor = playerColor;
        this.dustColor = dustColor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TextColor getColor() {
        return color;
    }

    public void setColor(TextColor color) {
        this.color = color;
    }

    public BossBarColor getBossbarColor() {
        return bossbarColor;
    }

    public void setBossbarColor(BossBarColor color2) {
        this.bossbarColor = color2;
    }

    public DyeColor getDyeColor() {
        return dyeColor;
    }

    public void setDyeColor(DyeColor color) {
        this.dyeColor = color;
    }

    public int getCollarColor() {
        return collarColor;
    }

    public void setCollarColor(int collarColor) {
        this.collarColor = collarColor;
    }

    public String getJSONColor() {
        return jsonColor;
    }

    public void setJSONColor(String jsonColor) {
        this.jsonColor = jsonColor;
    }

    public String getPlayerColor() { return playerColor; }

    public void setPlayerColor(String playerColor) { this.playerColor = playerColor; }

    public float[] getDustColor() { return dustColor; }

    public void setDustColor(float[] dustColor) { this.dustColor = dustColor; }

    public String add(){
        return "team add " + this.name;
    }

    public String setTeamColor() { return "team modify " + this.name + " color " + this.color; }

    public String leaveTeam(String entity) { return "team leave " + entity; }

    public String joinTeam(String entity) { return "team join " + this.name + " " + entity; }

    public String emptyTeam() { return "team empty " + this.name; }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }
}
