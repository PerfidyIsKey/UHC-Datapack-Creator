import Enums.*;
import HelperClasses.*;

import java.util.ArrayList;

public class CommandBuilder {

    // Setblock
    public static ArrayList<String> forceLoadAndSet(int x, int y, int z, String blockType) {
        return forceLoadAndSet(x, y, z, Dimension.overworld, blockType);
    }

    public static ArrayList<String> forceLoadAndSet(int x, int y, int z, BlockType blockType) {
        return forceLoadAndSet(x, y, z, blockType + "");
    }

    public static ArrayList<String> forceLoadAndSet(int x, int y, int z, String blockType, SetBlockType type) {
        return forceLoadAndSet(x, y, z, Dimension.overworld, blockType, type);
    }

    public static ArrayList<String> forceLoadAndSet(int x, int y, int z, BlockType blockType, SetBlockType type) {
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

    public static ArrayList<String> forceLoadAndSet(int x, int y, int z, Dimension dimension, BlockType blockType) {
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

    public static ArrayList<String> forceLoadAndSet(int x, int y, int z, Dimension dimension, BlockType blockType, SetBlockType type) {
        return forceLoadAndSet(x, y, z, dimension, blockType + "", type);
    }

    public static String addForceLoad(int x1, int z1, int x2, int z2) {
        return "forceload add " + x1 + " " + z1 + " " + x2 + " " + z2;
    }

    public static String removeForceLoad(int x1, int z1, int x2, int z2) {
        return "forceload remove " + x1 + " " + z1 + " " + x2 + " " + z2;
    }

    public static String setBlock(String x, String y, String z, String blockType) {
        return "setblock " + x + " " + y + " " + z + " " + blockType;
    }

    public static String setBlock(int x, int y, int z, String blockType) {
        return setBlock("" + x, "" + y, "" + z, blockType);
    }

    public static String setBlock(int x, int y, int z, BlockType blockType) {
        return setBlock("" + x, "" + y, "" + z, blockType.toString());
    }

    public static String setBlock(Coordinate coordinate, String blockType) {
        return setBlock("" + coordinate.getX(), "" + coordinate.getY(), "" + coordinate.getZ(), blockType);
    }

    public static String setBlock(int x, int y, int z, String blockType, SetBlockType type) {
        return setBlock(x, y, z, blockType) + " " + type;
    }

    public static String setBlock(Coordinate coordinate, String blockType, SetBlockType type) {
        return setBlock(coordinate, blockType) + " " + type;
    }

    public static String setBlock(int x, int y, int z, BlockType blockType, SetBlockType type) {
        return setBlock(x, y, z, blockType) + " " + type;
    }

    public static String setBlockRelative(int x, int y, int z, String blockType) {
        return setBlock("~" + x, "~" + y, "~" + z, blockType);
    }

    // Fill blocks
    public static String fill(String x1, String y1, String z1, String x2, String y2, String z2, String blockType) {
        return "fill " + x1 + " " + y1 + " " + z1 + " " + x2 + " " + y2 + " " + z2 + " " + blockType;
    }

    public static String fill(int x1, int y1, int z1, int x2, int y2, int z2, String blockType) {
        return fill("" + x1, "" + y1, "" + z1, "" + x2, "" + y2, "" + z2, blockType);
    }

    public static String fill(int x1, int y1, int z1, int x2, int y2, int z2, String blockType, SetBlockType type) {
        return fill(x1, y1, z1, x2, y2, z2, blockType) + " " + type;
    }

    public static String fill(int x1, int y1, int z1, int x2, int y2, int z2, String blockType, SetBlockType type, String blockToReplace) {
        return fill(x1, y1, z1, x2, y2, z2, blockType, type) + " " + blockToReplace;
    }

    public static String fill(int x1, int y1, int z1, int x2, int y2, int z2, BlockType blockType) {
        return fill(x1, y1, z1, x2, y2, z2, blockType.toString());
    }

    public static String fill(int x1, int y1, int z1, int x2, int y2, int z2, BlockType blockType, SetBlockType type) {
        return fill(x1, y1, z1, x2, y2, z2, blockType) + " " + type;
    }

    public static String fill(int x1, int y1, int z1, int x2, int y2, int z2, BlockType blockType, SetBlockType type, String blockToReplace) {
        return fill(x1, y1, z1, x2, y2, z2, blockType, type) + " " + blockToReplace;
    }

    public static String relativeFill(int x1, int y1, int z1, int x2, int y2, int z2, String blockType, SetBlockType type, String blockToReplace) {
        return fill("~" + x1, "~" + y1, "~" + z1, "~" + x2, "~" + y2, "~" + z2, blockType) + " " + type + " " + blockToReplace;
    }

    // Gamerules
    public static String setGameRule(GameRule gamerule, boolean bool) {
        return setGameRule(gamerule, "" + bool);
    }

    public static String setGameRule(GameRule gamerule, int num) {
        return setGameRule(gamerule, "" + num);
    }

    public static String setGameRule(GameRule gamerule, String string) {
        return "gamerule " + gamerule + " " + string;
    }

    // Play sound
    public static String playSound(Sound sound, SoundSource source, String entity, String x, String y, String z, String x1, String y1, String z1) {
        return "playsound " + sound.getValue() + " " + source + " " + entity + " " + x + " " + y + " " + z + " " + x1 + " " + y1 + " " + z1;
    }

    public static String setAttributeBase(String entity, AttributeType attribute, double value) {
        return "attribute " + entity + " minecraft:" + attribute + " base set " + value;
    }

    // Status effects
    public static String giveEffect(String entity, Effect effect, int duration, int amplifier) {
        return giveEffect(entity, effect, duration, amplifier, false);
    }

    public static String giveEffect(String entity, Effect effect, int duration, int amplifier, Boolean hideParticles) {
        return "effect give " + entity + " " + effect + " " + duration + " " + amplifier + " " + hideParticles;
    }

    public static String clearEffect(String entity, Effect effect) {
        return "effect clear " + entity + " " + effect;
    }

    public static String clearEffect(String entity) {
        return "effect clear " + entity;
    }

    // Difficulty
    public static String setDifficulty(Difficulty difficulty) {
        return "difficulty " + difficulty;
    }

    // Gamemode
    public static String setDefaultGameMode(GameMode gameMode) {
        return "defaultgamemode " + gameMode;
    }

    public static String setGameMode(GameMode gameMode, String entity) {
        return "gamemode " + gameMode + " " + entity;
    }

    // Set world spawn
    public static String setWorldSpawn(Coordinate coordinate) {
        return "setworldspawn " + coordinate.getCoordinateString();
    }

    // Entities
    public static String summonEntity(String entity) {
        return summonEntity(entity, new Coordinate(0, 0, 0, ReferenceFrame.relative));
    }

    public static String summonEntity(String entity, Coordinate coordinate) {
        return "summon minecraft:" + entity + " " + coordinate.getCoordinateString();
    }

    public static String summonEntity(String entity, String nbt) {
        return summonEntity(entity, new Coordinate(0, 0, 0, ReferenceFrame.relative), nbt);
    }

    public static String summonEntity(String entity, Coordinate coordinate, String nbt) {
        return "summon minecraft:" + entity + " " + coordinate.getCoordinateString() + " " + nbt;
    }

    public static String summonEntity(EntityType entity) {
        return summonEntity(entity, new Coordinate(0, 0, 0, ReferenceFrame.relative));
    }

    public static String summonEntity(EntityType entity, Coordinate coordinate) {
        return "summon minecraft:" + entity + " " + coordinate.getCoordinateString();
    }

    public static String summonEntity(EntityType entity, String nbt) {
        return summonEntity(entity, new Coordinate(0, 0, 0, ReferenceFrame.relative), nbt);
    }

    public static String summonEntity(EntityType entity, Coordinate coordinate, String nbt) {
        return "summon minecraft:" + entity + " " + coordinate.getCoordinateString() + " " + nbt;
    }

    public static String killEntity(String entity) {
        return "kill " + entity;
    }

    // Teleportation
    public static String teleportEntity(String entity, Coordinate coordinate) {
        return "tp " + entity + " " + coordinate.getCoordinateString();
    }

    public static String teleportEntity(String entity1, String entity2) {
        return "tp " + entity1 + " " + entity2;
    }

    // Tags
    public static String addTag(String entity, Tag tag) {
        return "tag " + entity + " add " + tag;
    }

    public static String addTag(String entity, String tag) {
        return "tag " + entity + " add " + tag;
    }

    public static String removeTag(String entity, Tag tag) {
        return "tag " + entity + " remove " + tag;
    }

    public static String removeTag(String entity, String tag) {
        return "tag " + entity + " remove " + tag;
    }

    public static String removeTag(Tag tag) {
        return removeTag("@a", tag);
    }

    public static String removeTag(String tag) {
        return removeTag("@a", tag);
    }

    // Give item
    public static String giveItem(String entity, BlockType item) {
        return giveItem(entity, item, "");
    }

    public static String giveItem(String entity, BlockType item, String nbt) {
        return "give " + entity + " " + item + nbt;
    }

    public static String giveItem(String entity, String item, String nbt) {
        return "give " + entity + " " + item + nbt;
    }

    public static String replaceItem(String targets, InventorySlot slot, BlockType item) {
        return "item replace entity " + targets + " " + slot + " with " + item;
    }

    public static String replaceItem(String targets, String slot, BlockType item) {
        return "item replace entity " + targets + " " + slot + " with " + item;
    }

    public static String replaceItem(String targets, InventorySlot slot, BlockType item, int count) {
        return "item replace entity " + targets + " " + slot + " with " + item + " " + count;
    }

    public static String replaceItem(String targets, String slot, BlockType item, int count) {
        return "item replace entity " + targets + " " + slot + " with " + item + " " + count;
    }

    public static String replaceItem(String targets, InventorySlot slot, String item) {
        return "item replace entity " + targets + " " + slot + " with " + item;
    }

    public static String replaceItem(String targets, String slot, String item) {
        return "item replace entity " + targets + " " + slot + " with " + item;
    }

    public static String replaceItem(String targets, InventorySlot slot, String item, int count) {
        return "item replace entity " + targets + " " + slot + " with " + item + " " + count;
    }

    public static String replaceItem(String targets, String slot, String item, int count) {
        return "item replace entity " + targets + " " + slot + " with " + item + " " + count;
    }


    // Worldborder
    public static String setWorldBorder(int size, int duration) {
        return "worldborder set " + size + " " + duration;
    }

    public static String setWorldBorder(int size) {
        return "worldborder set " + size;
    }

    // Spreadplayers
    public static String spreadPlayers(int xCenter, int yCenter, int minRange, int maxRange, Boolean respectTeam, String entities) {
        return "spreadplayers " + xCenter + " " + yCenter + " " + minRange + " " + maxRange + " " + respectTeam + " " + entities;
    }

    // Experience
    public static String setExperience(String target, int amount, ExperienceType type) {
        return "xp set " + target + " " + amount + " " + type;
    }

    // Advancements
    public static String revokeAdvancement(String target) {
        return "advancement revoke " + target + " everything";
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

    // Clear inventory
    public static String clearInventory(String targets, BlockType item) {
        return "clear " + targets + " " + item;
    }

    public static String clearInventory(String targets) {
        return "clear " + targets;
    }

    // Game time
    public static String setTime(int time) {
        return "time set " + time;
    }

    // Recipes
    public static String giveRecipe(String targets, BlockType recipe) {
        return "recipe give " + targets + " " + recipe;
    }

    public static String giveRecipe(String targets, String recipe) {
        return "recipe give " + targets + " " + recipe;
    }

    public static String takeRecipe(String targets, BlockType recipe) {
        return "recipe take " + targets + " " + recipe;
    }

    public static String takeRecipe(String targets, String recipe) {
        return "recipe take " + targets + " " + recipe;
    }

    // Particle
    public static String createParticle(Particle name, Coordinate pos, Coordinate delta, int speed, int count, String viewers) {
        return "particle " + name + " " + pos.getCoordinateString() + " " + delta.getCoordinateString() + " " + speed + " " + count + " normal " + viewers;
    }

    public static String createParticle(String name, Coordinate pos, Coordinate delta, int speed, int count, String viewers) {
        return "particle " + name + " " + pos.getCoordinateString() + " " + delta.getCoordinateString() + " " + speed + " " + count + " normal " + viewers;
    }

    // Potions
    public static String giveSplashPotion(String targets, int slotNumber, Effect effect, String colorHex, String displayName, String lore) {
        // Convert hex to decimal
        int potionColor = Integer.parseInt(colorHex, 16);

        return "item replace entity " + targets + " " + InventorySlot.hotbar.setSlotNumber(slotNumber) + " with " + BlockType.SPLASH_POTION + "[potion_contents={custom_color:" + potionColor + ",custom_effects:[{id:\"" + effect + "\",amplifier:0,duration:200,show_particles:0b,show_icon:0b,ambient:0b}]},lore=[\"" + lore + "\"],custom_name=\"" + displayName + "\"]";
    }

    // Trigger
    public static String setTrigger(ScoreboardObjective objective) {
        return "trigger " + objective.getName();
    }

    // Change title display time
    public static String changeTitleDisplayTime(String targets, int fadeIn, int duration, int fadeOut) {
        return changeTitleDisplayTime(targets, fadeIn, duration, fadeOut, Duration.seconds);
    }

    public static String changeTitleDisplayTime(String targets, int fadeIn, int duration, int fadeOut, Duration durationType) {
        return "title " + targets + " times " + fadeIn + durationType + " " + duration + durationType + " " + fadeOut + durationType;
    }

    public static String changeTitleDisplayTime(String targets, String fadeIn, String duration, String fadeOut) {
        return "title " + targets + " times " + fadeIn + " " + duration + " " + fadeOut;
    }

    // Store random number
    public static String storeRandomNumber(String targets, String objective, int min, int max) {
        return Execute.Store(ExecuteStore.result, targets, objective) +
                "random value " + min + ".." + max;
    }

    public static String storeRandomNumber(String targets, Objective objective, int min, int max) {
        return Execute.Store(ExecuteStore.result, targets, objective) +
                "random value " + min + ".." + max;
    }

    public static String storeRandomNumber(String objective, int min, int max) {
        return storeRandomNumber(Constant.admin, objective, min, max);
    }

    public static String storeRandomNumber(Objective objective, int min, int max) {
        return storeRandomNumber(Constant.admin, objective, min, max);
    }

    public static ArrayList<String> warnAndReplace(String targets, TextItem warning, String replacement) {
        ArrayList<String> fileCommands = new ArrayList<>();

        fileCommands.add(Execute.If(targets) +
                new TellRaw(targets, warning).sendRaw());
        fileCommands.add(replaceItem(targets, InventorySlot.mainhand, replacement));

        return fileCommands;
    }

    public static ArrayList<String> warnAndReplace(String targets, TextItem warning, BlockType replacement) {
        return warnAndReplace(targets, warning, replacement.toString());
    }
}