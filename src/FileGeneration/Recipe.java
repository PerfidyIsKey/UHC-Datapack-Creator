package FileGeneration;

import java.util.ArrayList;

public class Recipe {
    private String type;
    private String[] grid;
    private ArrayList<String> keys;
    private String resultItem;
    private int resultAmount;

    public Recipe(String type, String[] grid, ArrayList<String> keys, String resultItem, int resultAmount) {
        this.type = type;
        this.grid = grid;
        this.keys = keys;
        this.resultItem = resultItem;
        this.resultAmount = resultAmount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String[] getGrid() {
        return grid;
    }

    public void setGrid(String[] grid) {
        this.grid = grid;
    }

    public ArrayList<String> getKeys() {
        return keys;
    }

    public void setKeys(ArrayList<String> keys) {
        this.keys = keys;
    }

    public String getResultItem() {
        return resultItem;
    }

    public void setResultItem(String resultItem) {
        this.resultItem = resultItem;
    }

    public int getResultAmount() {
        return resultAmount;
    }

    public void setResultAmount(int resultAmount) {
        this.resultAmount = resultAmount;
    }
}
