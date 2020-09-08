public class ScoreboardObjective {
    private String name;
    private String type;
    private String customName = "";

    public ScoreboardObjective(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public ScoreboardObjective(String name, String type, String customName) {
        this.name = name;
        this.type = type;
        this.customName = customName;
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
}
