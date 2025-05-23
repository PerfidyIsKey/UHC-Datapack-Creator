package FileGeneration;

import ItemClasses.Components;
import ItemModifiers.ItemModifier;

import java.util.ArrayList;

public class LootTable {
    // Fields
    private LootTableType type;
    private LootTableRolls rolls;
    private int bonusRolls;
    private ArrayList<LootTableEntry> entries;

    // Constructor
    public LootTable(LootTableType type, LootTableRolls rolls, int bonusRolls, ArrayList<LootTableEntry> entries) {
        this.type = type;
        this.rolls = rolls;
        this.bonusRolls = bonusRolls;
        this.entries = entries;
    }

    public String generateLootTable() {
        return "{\n" +
                "\"type\":\"" + type + "\",\n" +
                "\"pools\":[\n" +
                "{\n" +
                rolls.GetRolls() + ",\n" +
                "\"bonus_rolls\":" + bonusRolls + ",\n" +
                "\"entries\":[\n" +
                GenerateEntry() +
                "]\n" +
                "}\n" +
                "]\n" +
                "}";

    }

    private String GenerateEntry() {
        // Create component structure
        String entryContent = "";
        for (int i = 0; i < entries.size(); i++) {
            // Select current entry
            LootTableEntry currentEntry = entries.get(i);

            // Add content
            entryContent += currentEntry.GetEntry();

            if (i < (entries.size() - 1)) {   // Extend to next entry
                entryContent += ",\n";
            } else {  // Close entries
                entryContent += "\n";
            }
        }

        return entryContent;
    }

    public String GenerateRates() {
        // Determine total rates
        double totalWeight = 0;
        for (LootTableEntry entry : entries) {
            totalWeight += entry.getWeight();
        }

        // Determine individual rates
        String individualRates = "";
        for (int i = 0; i < entries.size(); i++) {
            LootTableEntry thisEntry = entries.get(i);
            individualRates += "#" + (i + 1) + " " + thisEntry.getItem() + " " + ((double)thisEntry.getWeight()*100/totalWeight) + "%";

            if (i < (entries.size() - 1)) {   // Extend to next entry
                individualRates += "\n";
            }
        }

        return "# Total weight: " + totalWeight + "\n" +
                individualRates;
    }
}
