public class Team {

    private String name;
    private String color;
    private String bossbarColor;
    private String collarColor;

    public Team(String name, String color, String bossbarColor, String collarColor) {
        this.name = name;
        this.color = color;
        this.bossbarColor = bossbarColor;
        this.collarColor = collarColor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBossbarColor() {
        return bossbarColor;
    }

    public void setBossbarColor(String color2) {
        this.bossbarColor = color2;
    }

    public String getCollarColor() {
        return collarColor;
    }

    public void setCollarColor(String collarColor) {
        this.collarColor = collarColor;
    }
}
