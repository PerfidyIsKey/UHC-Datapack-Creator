package FileGeneration;

public class LootTableEntry {

    private int weight;

    private String itemName;

    private LootTableFunction function;

    public LootTableEntry(int weight, String itemName)
    {
        this.weight = weight;
        this.itemName = itemName;
    }

    public LootTableEntry(int weight, String itemName, LootTableFunction function)
    {
        this.weight = weight;
        this.itemName = itemName;
        this.function = function;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getName() {
        return itemName;
    }

    public void setName(String itemName) {
        this.itemName = itemName;
    }

    public LootTableFunction getFunction() {
        return function;
    }

    public void setFunction(LootTableFunction function) {
        this.function = function;
    }
}
