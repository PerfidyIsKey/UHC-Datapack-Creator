package ItemClasses;

import Enums.EnchantmentType;

public class Enchantments implements Components{
    // Fields
    EnchantmentType id; // The resource location of an enchantment
    int level;  // Level of enchantment

    // Constructor
    public Enchantments(EnchantmentType id, int level) {
        this.id = id;
        this.level = level;
    }


    @Override
    public String GenerateNBT() {
        return null;
    }

    @Override
    public String GenerateComponent() {
        return "\"enchantments\":{\n" +
                "\"levels\":{\n" +
                "\"" + id + "\":" + level + "\n" +
                "}\n" +
                "}";
    }
}
