package HelperClasses;

public class ScoreboardObjective {
    private String name;
    private String type;
    private String customName = "";
    private boolean displaySideBar = false;

    public ScoreboardObjective() {

    }

    public ScoreboardObjective(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public ScoreboardObjective(String name, String type, String customName) {
        this.name = name;
        this.type = type;
        this.customName = customName;
    }

    public ScoreboardObjective(String name, String type, boolean displaySideBar) {
        this.name = name;
        this.type = type;
        this.displaySideBar = displaySideBar;
    }

    public ScoreboardObjective(String name, String type, String customName, boolean displaySideBar) {
        this.name = name;
        this.type = type;
        this.customName = customName;
        this.displaySideBar = displaySideBar;
    }

    public String objectiveAdd() {
        return "scoreboard objectives add " + name + " " + type + " " + customName;
    }

    public String objectiveAdd(String name, String type) {
        return "scoreboard objectives add " + name + " " + type;
    }

    public String setDisplay(String location) {
        return "scoreboard objectives setdisplay " + location + " " + name;
    }

    public String setDisplay(String location, String name) {
        return "scoreboard objectives setdisplay " + location + " " + name;
    }

    public String get(Selector selector) {
        return "scoreboard players get " + selector + " " + name;
    }

    public String set(Condition condition, int value) {
        return "scoreboard players set " + condition + " " + name + " " + value;
    }

    public String add(Condition condition, int value) {
        return "scoreboard players add " + condition + " " + name + " " + value;
    }

    public String operation(Selector selector, String score, String operator, Selector selector2, String score2) {
        return "scoreboard players operation " + selector + " " + score + " " + operator + " " + selector2 + " " + score2;
    }

    public String reset(Selector selector) {
        return "scoreboard players reset " + selector + " " + name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public boolean getDisplaySideBar() {
        return displaySideBar;
    }

    public void setDisplaySideBar(boolean displaySideBar) {
        this.displaySideBar = displaySideBar;
    }
}
