package HelperClasses;

import Enums.BossBarColor;
import Enums.Color;

public class Team {

    private String name;
    private Color color;
    private BossBarColor bossbarColor;
    private String glassColor;
    private String collarColor;
    private String jsonColor;

    private String playerColor;

    public Team(String name, Color color, BossBarColor bossbarColor, String glassColor, String collarColor, String jsonColor, String playerColor) {
        this.name = name;
        this.color = color;
        this.bossbarColor = bossbarColor;
        this.glassColor = glassColor;
        this.collarColor = collarColor;
        this.jsonColor = jsonColor;
        this.playerColor = playerColor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public BossBarColor getBossbarColor() {
        return bossbarColor;
    }

    public void setBossbarColor(BossBarColor color2) {
        this.bossbarColor = color2;
    }

    public String getGlassColor() {
        return glassColor;
    }

    public void setGlassColor(String color2) {
        this.glassColor = color2;
    }

    public String getCollarColor() {
        return collarColor;
    }

    public void setCollarColor(String collarColor) {
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

    public String add(){
        return "team add " + this.name;
    }
}
