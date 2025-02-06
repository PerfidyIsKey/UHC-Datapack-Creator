package FileGeneration;

import Enums.BlockType;
import ItemModifiers.ItemModifier;
import java.util.ArrayList;

public class LootTableEntry {

    private int weight;
    private BlockType item;

    private ArrayList<ItemModifier> functions;

    public LootTableEntry(int weight, BlockType item) {
        this.weight = weight;
        this.item = item;
    }

    public LootTableEntry(int weight, BlockType item, ArrayList<ItemModifier> functions) {
        this.weight = weight;
        this.item = item;
        this.functions = functions;
    }

    public LootTableEntry(int weight, BlockType item, ItemModifier function) {
        ArrayList<ItemModifier> functions = new ArrayList<>();
        functions.add(function);

        this.weight = weight;
        this.item = item;
        this.functions = functions;
    }

    public String GetEntry() {
        String extendFunctions = "\n";

        if (functions != null) { extendFunctions = GenerateFunctions(); }

        return "{\n" +
                "\"type\":\"item\",\n" +
                "\"weight\":" + weight + ",\n" +
                "\"name\":\"" + item + "\"" +
                extendFunctions +
                "}";
    }

    private String GenerateFunctions() {
        // Create component structure
        String functionContent = ",\n" +
                "\"functions\":[\n";
        for (int i = 0; i < functions.size(); i++) {
            // Select current function
            ItemModifier currentFunction = functions.get(i);

            // Add content
            functionContent += currentFunction.GenerateFunction();

            if (i < (functions.size() - 1)) {   // Extend to next function
                functionContent += ",\n";
            } else {  // Close functions
                functionContent += "\n" +
                "]\n";
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


}
