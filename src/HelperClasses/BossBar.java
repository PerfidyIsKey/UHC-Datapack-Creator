package HelperClasses;

public class BossBar {

    private String name;
    private String prefix = "bossbar ";

    public BossBar(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String remove() {
        return prefix + "remove minecraft:" + name;
    }

    public String add(String title) {
        return prefix + "add minecraft:" + name + " \"" + title + "\"";
    }

    public String setMax(int max) {
        return prefix + "set minecraft:" + name + " max " + max;
    }

    public String setColor(String color) {
        return prefix + "set minecraft:" + name + " color " + color;
    }

    public String setTitle(String title) {
        return prefix + "set minecraft:" + name + " name \"" + title + "\"";
    }

    public String setVisible(Boolean visible) {
        return prefix + "set minecraft:" + name + " visible " + visible;
    }

    public String setPlayers(String players) {
        return prefix + "set minecraft:" + name + " players " + players;
    }

    public String setValue(String value) {
        return prefix + "minecraft:" + name + " value " + value;
    }
}
