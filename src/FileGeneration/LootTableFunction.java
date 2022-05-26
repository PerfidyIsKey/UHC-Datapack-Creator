package FileGeneration;

public class LootTableFunction {

    private int itemCount;

    private double randomChance;

    public LootTableFunction(int itemCount)
    {
        this.itemCount = itemCount;
    }

    public LootTableFunction(int itemCount, double randomChance)
    {
        this.itemCount = itemCount;
        this.randomChance = randomChance;
    }

    public int getCount() {
        return itemCount;
    }

    public void setCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public double getChance() {
        return randomChance;
    }

    public void setChance(double randomChance) {
        this.randomChance = randomChance;
    }
}
