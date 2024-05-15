package HelperClasses;

import Enums.Biome;
import Enums.Dimension;

import static Enums.Dimension.overworld;

public class ControlPoint {

    private String name;

    private int maxVal;
    private int addRate;

    private Coordinate coordinate;
    private Enums.Biome biome;

    public ControlPoint(String name, int maxVal, int addRate, Coordinate coordinate) {
        this.name = name;
        this.maxVal = maxVal;
        this.addRate = addRate;
        this.coordinate = coordinate;
        this.biome = Biome.plains;
    }

    public ControlPoint(String name, int maxVal, int addRate, Coordinate coordinate, Enums.Biome biome) {
        this.name = name;
        this.maxVal = maxVal;
        this.addRate = addRate;
        this.coordinate = coordinate;
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

    public Coordinate getCoordinate() {
        return this.coordinate;
    }

    public Biome getBiome() { return biome; }

    public void setBiome(Biome biome) { this.biome = biome; }

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
            case badlands:
            case eroded_badlands:
            case wooded_badlands:
            case beach:
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
            case stony_peaks:
            case stony_shore:
            case meadow:
                result = "mountain";
                break;

            case forest:
            case flower_forest:
            case sunflower_plains:
            case windswept_forest:
                result = "nature_oak";
                break;

            case taiga:
            case old_growth_pine_taiga:
            case old_growth_spruce_taiga:
                result = "nature_spruce";
                break;

            case dark_forest:
                result = "nature_dark";
                break;

            case birch_forest:
            case old_growth_birch_forest:
                result = "nature_birch";
                break;

            case savanna:
            case savanna_plateau:
            case windswept_savanna:
                result = "nature_acacia";
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
            case ocean:
            case cold_ocean:
            case deep_ocean:
            case warm_ocean:
            case lukewarm_ocean:
            case deep_cold_ocean:
            case deep_lukewarm_ocean:
                result = "water";
                break;

            case frozen_ocean:
            case ice_spikes:
            case snowy_beach:
            case snowy_taiga:
            case frozen_peaks:
            case frozen_river:
            case snowy_plains:
            case snowy_slopes:
            case deep_frozen_ocean:
            case jagged_peaks:
                result = "snow";
                break;

            case cherry_grove:
                result = "nature_cherry";
                break;

            default:
                result = "legacy";
        }

        return ("minecraft:control_point_" + result);
    }
}
