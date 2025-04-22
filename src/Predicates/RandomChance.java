package Predicates;

public class RandomChance implements Predicate {
    // Fields
    private double chance;

    // Constructor
    public RandomChance(double chance) {
        this.chance = chance;
    }

    public String GenerateCondition() {
        return "{\n" +
                "\"condition\":\"random_chance\",\n" +
                "\"chance\":" + chance + "\n" +
                "}";
    }
}
