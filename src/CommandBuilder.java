import Enums.*;
import HelperClasses.*;
import arguments.Entity;
import arguments.targetselector.SelectorArgumentsBuilder;
import arguments.targetselector.TargetSelector;
import commands.Attribute;
import commands.Summon;
import controlpoints.ControlPointTag;
import shared.*;
import shared.attributes.AttributeId;
import shared.item.ItemId;

import java.util.ArrayList;

public class CommandBuilder {

    // Setblock
    public static ArrayList<String> forceLoadAndSet(int x, int y, int z, String blockType) {
        return forceLoadAndSet(x, y, z, Dimension.overworld, blockType);
    }

    public static ArrayList<String> forceLoadAndSet(int x, int y, int z, StaticBlockId blockType) {
        return forceLoadAndSet(x, y, z, blockType + "");
    }

    public static ArrayList<String> forceLoadAndSet(int x, int y, int z, String blockType, SetBlockType type) {
        return forceLoadAndSet(x, y, z, Dimension.overworld, blockType, type);
    }

    public static ArrayList<String> forceLoadAndSet(int x, int y, int z, StaticBlockId blockType, SetBlockType type) {
        return forceLoadAndSet(x, y, z, blockType + "", type);
    }

    public static ArrayList<String> forceLoadAndSet(int x, int y, int z, Dimension dimension, String blockType) {
        ArrayList<String> fileCommands = new ArrayList<>();
        fileCommands.add(Execute.In(dimension) +
                "forceload add " + x + " " + z + " " + x + " " + z);
        fileCommands.add(Execute.In(dimension) +
                setBlock(x, y, z, blockType));
        fileCommands.add(Execute.In(dimension) +
                "forceload remove " + x + " " + z + " " + x + " " + z);
        return fileCommands;
    }

    public static ArrayList<String> forceLoadAndSet(int x, int y, int z, Dimension dimension, StaticBlockId blockType) {
        return forceLoadAndSet(x, y, z, dimension, blockType + "");
    }

    public static ArrayList<String> forceLoadAndSet(int x, int y, int z, Dimension dimension, String blockType, SetBlockType type) {
        ArrayList<String> fileCommands = new ArrayList<>();
        fileCommands.add(Execute.In(dimension) +
                "forceload add " + x + " " + z + " " + x + " " + z);
        fileCommands.add(Execute.In(dimension) +
                setBlock(x, y, z, blockType, type));
        fileCommands.add(Execute.In(dimension) +
                "forceload remove " + x + " " + z + " " + x + " " + z);
        return fileCommands;
    }

    public static ArrayList<String> forceLoadAndSet(int x, int y, int z, Dimension dimension, StaticBlockId blockType, SetBlockType type) {
        return forceLoadAndSet(x, y, z, dimension, blockType + "", type);
    }

    public static String addForceLoad(int x1, int z1, int x2, int z2) {
        return "forceload add " + x1 + " " + z1 + " " + x2 + " " + z2;
    }

    public static String addForceLoad(Coordinate coordinate) {
        return "forceload add " + coordinate.getX() + " " + coordinate.getZ() + " " + coordinate.getX() + " " + coordinate.getZ();
    }

    public static String removeForceLoad(int x1, int z1, int x2, int z2) {
        return "forceload remove " + x1 + " " + z1 + " " + x2 + " " + z2;
    }

    public static String removeForceLoad() {
        return "forceload remove all";
    }

    public static String setBlock(String x, String y, String z, String blockType) {
        return "setblock " + x + " " + y + " " + z + " " + blockType;
    }

    public static String setBlock(int x, int y, int z, String blockType) {
        return setBlock("" + x, "" + y, "" + z, blockType);
    }

    public static String setBlock(int x, int y, int z, String blockType, SetBlockType type) {
        return setBlock(x, y, z, blockType) + " " + type;
    }

    public static String summonEntity(EntityType entity, Coordinate coordinate, String nbt) {
        return "summon " + entity + " " + coordinate.getCoordinateString() + " " + nbt;
    }

    public static String replaceItem(String targets, ItemSlot slot, String item) {
        return "item replace entity " + targets + " " + slot.getCommandString() + " with " + item;
    }

    // Data
    public static String getData(String target, String path) {
        return "data get entity " + target + " " + path;
    }

    public static String getData(String target, String path, int scale) {
        return "data get entity " + target + " " + path + " " + scale;
    }

    public static String modifyData(String target, String targetPath, String value) {
        return "data modify entity " + target + " " + targetPath + " set value " + value + "b";
    }

    // Potions
    public static String giveSplashPotion(String targets, int slotNumber, EffectId effect, String colorHex, String displayName, String lore) {
        // Convert hex to decimal
        int potionColor = Integer.parseInt(colorHex, 16);

        return "item replace entity " + targets + " " + ItemSlot.HOTBAR.withSlotNumber(slotNumber).getCommandString() + " with " + ItemId.SPLASH_POTION + "[potion_contents={custom_color:" + potionColor + ",custom_effects:[{id:\"" + effect + "\",amplifier:0,duration:200,show_particles:0b,show_icon:0b,ambient:0b}]},lore=[\"" + lore + "\"],custom_name=\"" + displayName + "\"]";
    }

    // Trigger
    public static String setTrigger(ScoreboardObjective objective) {
        return "trigger " + objective.getName();
    }

    // Change title display time
    public static String changeTitleDisplayTime(String targets, int fadeIn, int stay, int fadeOut) {
        return changeTitleDisplayTime(targets, fadeIn, stay, fadeOut, Duration.SECONDS);
    }

    public static String changeTitleDisplayTime(String targets, int fadeIn, int stay, int fadeOut, Duration durationType) {
        return "title " + targets + " times " + fadeIn + durationType + " " + stay + durationType + " " + fadeOut + durationType;
    }

    public static String changeTitleDisplayTime(String targets, String fadeIn, String stay, String fadeOut) {
        return "title " + targets + " times " + fadeIn + " " + stay + " " + fadeOut;
    }

    public static String titleDefaultTiming(String targets) {
        return changeTitleDisplayTime(targets, 10, 70, 20, Duration.TICKS);
    }

    public static ArrayList<String> warnAndReplace(String targets, TextItem warning, String replacement) {
        ArrayList<String> fileCommands = new ArrayList<>();

        fileCommands.add(Execute.If(targets) +
                new TellRaw(targets, warning).sendRaw());
        fileCommands.add(replaceItem(targets, ItemSlot.MAINHAND, replacement));

        return fileCommands;
    }

    public static ArrayList<String> warnAndReplace(String targets, TextItem warning, ItemId replacement) {
        return warnAndReplace(targets, warning, replacement.toString());
    }

    public static String modifyWaypointColor(String waypoint, TextColor color) {
        return "waypoint modify " + waypoint + " color " + color;
    }

    public static String modifyWaypointColor(String waypoint) {
        return modifyWaypointColor(waypoint, TextColor.WHITE);
    }
}