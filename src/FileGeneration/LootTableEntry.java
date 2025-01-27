package FileGeneration;

import Enums.BlockType;
import ItemClasses.Components;
import ItemModifiers.ItemModifier;

import java.util.ArrayList;

public class LootTableEntry {

    private int weight;

    private String itemName;
    private BlockType item;

    private LootTableFunction function;

    private ArrayList<ItemModifier> functions;

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

    public LootTableEntry(int weight, BlockType item, ArrayList<ItemModifier> functions)
    {
        this.weight = weight;
        this.item = item;
        this.functions = functions;
    }

    public String GetEntry() {
        return "\"type\":\"item\",\n" +
                "\"weight\":" + weight + ",\n" +
                "\"name\":\"" + item + "\",\n" +
                "\"functions\":[\n" + GenerateFunctions() + "\n" +
                "]";
    }

    private String GenerateFunctions() {
        // Create component structure
        String functionContent = "";
        for (int i = 0; i < functions.size(); i++) {
            // Select current function
            ItemModifier currentFunction = functions.get(i);

            // Add content
            functionContent += currentFunction.GenerateFunction();

            if (i < (functions.size() - 1)) {   // Extend to next function
                functionContent += ",\n";
            } else {  // Close functions
                functionContent += "";
            }
        }

        return functionContent;
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
