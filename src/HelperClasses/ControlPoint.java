package HelperClasses;

import Enums.Biome;
import Enums.Dimension;

import static Enums.Dimension.overworld;

public class ControlPoint {

    private String name;

    private int maxVal;
    private int addRate;

    private int x;
    private int y;
    private int z;

    private Enums.Dimension dimension;
    private Enums.Biome biome;

    public ControlPoint(String name, int maxVal, int addRate, int x, int y, int z) {
        this.name = name;
        this.maxVal = maxVal;
        this.addRate = addRate;
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = overworld;
        this.biome = Biome.plains;
    }

    public ControlPoint(String name, int maxVal, int addRate, int x, int y, int z, Enums.Biome biome) {
        this.name = name;
        this.maxVal = maxVal;
        this.addRate = addRate;
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = overworld;
        this.biome = biome;
    }

    public ControlPoint(String name, int maxVal, int addRate, int x, int y, int z, Enums.Dimension dimension, Enums.Biome biome) {
        this.name = name;
        this.maxVal = maxVal;
        this.addRate = addRate;
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = dimension;
        this.biome = biome;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxVal() {
        return maxVal;
    }

    public void setMaxVal(int maxVal) {
        this.maxVal = maxVal;
    }

    public int getAddRate() {
        return addRate;
    }

    public void setAddRate(int addRate) {
        this.addRate = addRate;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public Enums.Dimension getDimension() { return dimension; }

    public void setDimension(Enums.Dimension dimension) { this.dimension = dimension; }

    public Biome getBiome() { return biome; }

    public void setBiome(Biome biome) { this.biome = biome; }

    public String getDimensionName() {
        String result;
        switch (dimension) {
            case the_end:
                result = "End";
                break;
            case the_nether:
                result = "Nether";
                break;
            default:
                result = "Overworld";
        }

        return result;
    }

    public String getStructureName() {
        String result;
        switch (biome) {
            case cave:
            case dripstone_caves:
            case lush_caves:
            case deep_dark:
                result = "cave";
                break;

            case desert:
            case savanna:
            case savanna_plateau:
            case windswept_savanna:
            case badlands:
            case eroded_badlands:
            case wooded_badlands:
                result = "desert";
                break;

            case jungle:
            case bamboo_jungle:
            case sparse_jungle:
                result = "jungle";
                break;

            case windswept_gravelly_hills:
            case windswept_hills:
            case grove:
            case jagged_peaks:
            case stony_peaks:
            case stony_shore:
                result = "mountain";
                break;

            case forest:
            case taiga:
            case dark_forest:
            case birch_forest:
            case flower_forest:
            case sunflower_plains:
            case windswept_forest:
            case old_growth_pine_taiga:
            case old_growth_birch_forest:
            case old_growth_spruce_taiga:
                result = "nature";
                break;

            case basalt_deltas:
            case nether_wastes:
            case warped_forest:
            case crimson_forest:
            case soul_sand_valley:
                result = "nether";
                break;

            case swamp:
            case mangrove_swamp:
                result = "swamp";
                break;

            case river:
            case beach:
            case ocean:
            case cold_ocean:
            case deep_ocean:
            case warm_ocean:
            case lukewarm_ocean:
            case deep_cold_ocean:
            case deep_lukewarm_ocean:
                result = "water";
                break;

            default:
                result = "legacy";
        }

        return ("minecraft:control_point_" + result);
    }
}
