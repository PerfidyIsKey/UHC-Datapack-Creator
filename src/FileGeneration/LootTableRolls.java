package FileGeneration;

public class LootTableRolls {
    // Fields
    private DistributionType type;
    private int min;
    private int max;

    // Constructor
    public LootTableRolls(DistributionType type, int min, int max) {
        this.type = type;
        this.min = min;
        this.max = max;
    }

    public String GetRolls() {
        return "\"rolls\":{\n" +
                "\"type\":\"" + type + "\",\n" +
                "\"min\":" + min + ",\n" +
                "\"max\":" + max + "\n" +
                "}";
    }
}
