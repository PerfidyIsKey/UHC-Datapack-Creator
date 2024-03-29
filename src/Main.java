import Enums.*;
import FileGeneration.*;
import HelperClasses.*;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class Main {

    public static void main(String[] args) {
        new Main().run();
    }

    //packages
    FileTools fileTools;

    //DatapackData<

    private String uhcNumber = "s50";
    private static final String version = "3.0";
    private String userFolder;
    private String worldName;
    private String dataPackLocation;
    private String worldLocation;
    private String dataPackName;

    private String fileLocation;
    private GameMode gameMode = GameMode.DIORITE;
    //DatapackData>

    //GameData<
    private static final int chestSize = 27;
    private static final String commandCenter = "s46";
    private String admin = "Snodog627";
    private Coordinate startCoordinate;
    private Coordinate netherPortal;
    private ArrayList<Team> teams = new ArrayList<>();
    private ArrayList<ControlPoint> cpList = new ArrayList<>();
    private ArrayList<ControlPoint> controlPoints = new ArrayList<>();
    //private ArrayList<CarePackage> carePackages = new ArrayList<>();
    private ArrayList<ScoreboardObjective> scoreboardObjectives = new ArrayList<>();
    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<StatusEffect> effects = new ArrayList<>();
    private ArrayList<String> quotes = new ArrayList<>();
    private ArrayList<BossBar> bossBars = new ArrayList<>();
    private static final int worldSize = 1500;
    private static final int worldHeight = 257;
    private static final int worldBottom = -64;
    private static final int tickPerSecond = 20;
    private static final int secPerMinute = 60;
    private static final int maxCPScore = 2400;
    private static final int maxCPScoreBossbar = 20 * secPerMinute * tickPerSecond * 2;

    private static final int carePackageAmount = 200;
    private int minTraitorRank;
    private static final int traitorMode = 1;
    private String communityName;
    private static final Execute execute = new Execute();

    private final Text bannerText = new Text(Color.dark_gray, true, false, " | ");

    //GameData>


    private ArrayList<FileData> files = new ArrayList<>();


    private void run() {

        gameModeChange();
        createDatapack();
        boolean menuRunning = true;
        Scanner scanner = new Scanner(System.in);
        String input;
        System.out.println("-----------------\n");
        while (menuRunning) {
            System.out.println("Datapack created under Gamemode: " + gameMode);
            System.out.println("Options:\n");
            System.out.println("Change Gamemode (c[n])");
            System.out.println("Re-run (r)");
            System.out.println("-----------------");
            System.out.print("> ");
            input = scanner.nextLine();
            if (input.equals("exit")) {
                menuRunning = false;
                System.out.println("Exiting System.");
            } else if (input.startsWith("c")) {
                String command = input.replace("c", "");
                int num = parseInt(command);
                changeGamemode(num);
                createDatapack();
            } else if (input.equals("r")) {
                gameModeChange();
                createDatapack();
            } else {
                System.out.println("Input not recognized.");
            }
        }
    }

    private void changeGamemode(int num) {
        if (num == 0) {
            gameMode = GameMode.DIORITE;
        }
        if (num == 1) {
            gameMode = GameMode.URE;
        }
        if (num == 2) {
            gameMode = GameMode.KINJIN;
        }

        gameModeChange();
    }


    private void createDatapack() {
        try {
            fileTools.createDatapack(files, fileLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void gameModeChange() {
        files = new ArrayList<>();
        initSaveDir();
        fileTools = new FileTools(version, dataPackLocation, dataPackName, worldLocation);

        initGameData();
        makeFunctionFiles();
        files.addAll(fileTools.makeRecipeFiles());
        makeLootTableFiles();
    }

    private void initSaveDir() {
        if (fileTools == null) {
            fileTools = new FileTools();
        }
        uhcNumber = fileTools.getContentOutOfFile("Files\\" + gameMode + "\\uhc_data.txt", "uhcNumber");
        admin = fileTools.getContentOutOfFile("Files\\" + gameMode + "\\uhc_data.txt", "admin");

        worldLocation = "Server\\world\\";

        dataPackLocation = worldLocation + "datapacks\\";

        dataPackName = "uhc-datapack-" + uhcNumber + "v" + version;
        fileLocation = dataPackLocation + dataPackName + "\\data\\uhc\\";

    }

    private void initGameData() {
        teams = new ArrayList<>();
        bossBars = new ArrayList<>();
        cpList = new ArrayList<>();
        controlPoints = new ArrayList<>();
        scoreboardObjectives = new ArrayList<>();
        effects = new ArrayList<>();
        players = new ArrayList<>();
        quotes = new ArrayList<>();

        Color[] colors = {Color.yellow, Color.blue, Color.red, Color.dark_purple, Color.dark_green, Color.light_purple, Color.black, Color.gold, Color.gray, Color.aqua, Color.dark_red, Color.dark_blue, Color.dark_aqua};
        BossBarColor[] bossbarColors = {BossBarColor.yellow, BossBarColor.blue, BossBarColor.red, BossBarColor.purple, BossBarColor.green, BossBarColor.pink, BossBarColor.white, BossBarColor.white, BossBarColor.white, BossBarColor.white, BossBarColor.white, BossBarColor.white, BossBarColor.white};
        String[] glassColors = {"yellow", "light_blue", "red", "purple", "green", "pink", "black", "orange", "gray", "cyan", "red", "blue", "blue"};
        String[] collarColors = {"4", "3", "14", "10", "13", "6", "15", "1", "7", "9", "2", "11", "9"};
        String[] jsonColors = {"YELLOW", "BLUE", "RED", "PURPLE", "GREEN", "PINK", "BLACK", "ORANGE", "GRAY", "AQUA", "DARK RED", "DARK BLUE", "DARK AQUA"};
        for (int i = 0; i < colors.length; i++) {
            Team team = new Team("Team" + i, colors[i], bossbarColors[i], glassColors[i], collarColors[i], jsonColors[i]);
            teams.add(team);
        }

        CarePackage carePackage2 = new CarePackage("anti_cp", "Anti Controlpoint Drop",
                "[{Slot:1b,id:\"minecraft:gunpowder\",Count:1b},{Slot:2b,id:\"minecraft:gunpowder\",Count:1b},{Slot:3b,id:\"minecraft:tnt\",Count:1b},{Slot:4b,id:\"minecraft:flint_and_steel\",Count:1b},{Slot:5b,id:\"minecraft:tnt\",Count:1b},{Slot:6b,id:\"minecraft:sand\",Count:1b},{Slot:7b,id:\"minecraft:sand\",Count:1b},{Slot:11b,id:\"minecraft:enchanted_book\",Count:1b,tag:{StoredEnchantments:[{lvl:4s,id:\"minecraft:blast_protection\"}]}},{Slot:12b,id:\"minecraft:lava_bucket\",Count:1b},{Slot:13b,id:\"minecraft:tnt\",Count:1b},{Slot:14b,id:\"minecraft:lava_bucket\",Count:1b},{Slot:15b,id:\"minecraft:enchanted_book\",Count:1b,tag:{StoredEnchantments:[{lvl:4s,id:\"minecraft:blast_protection\"}]}},{Slot:19b,id:\"minecraft:sand\",Count:1b},{Slot:20b,id:\"minecraft:sand\",Count:1b},{Slot:21b,id:\"minecraft:tnt\",Count:1b},{Slot:22b,id:\"minecraft:flint_and_steel\",Count:1b},{Slot:23b,id:\"minecraft:tnt\",Count:1b},{Slot:24b,id:\"minecraft:gunpowder\",Count:1b},{Slot:25b,id:\"minecraft:gunpowder\",Count:1b}]",
                0, 0, 0);

        bossBars.add(new BossBar("cp"));
        bossBars.add(new BossBar("cp1"));
        bossBars.add(new BossBar("cp2"));
        bossBars.add(new BossBar("carepackage"));

        // Data
        String[] splitStartCoordinates = fileTools.splitLineOnComma(fileTools.getContentOutOfFile("Files\\" + gameMode + "\\uhc_data.txt", "startCoordinate"));
        startCoordinate = new Coordinate(Integer.parseInt(splitStartCoordinates[0]), Integer.parseInt(splitStartCoordinates[1]), Integer.parseInt(splitStartCoordinates[2]));
        String[] splitNetherPortal = fileTools.splitLineOnComma(fileTools.getContentOutOfFile("Files\\" + gameMode + "\\uhc_data.txt", "netherPortal"));
        netherPortal = new Coordinate(Integer.parseInt(splitNetherPortal[0]), Integer.parseInt(splitNetherPortal[1]), Integer.parseInt(splitNetherPortal[2]));
        minTraitorRank = Integer.parseInt(fileTools.getContentOutOfFile("Files\\" + gameMode + "\\uhc_data.txt", "minTraitorRank"));
        communityName = fileTools.getContentOutOfFile("Files\\" + gameMode + "\\uhc_data.txt", "communityName");

        // ControlPoints
        ArrayList<String> controlPointString = fileTools.GetLinesFromFile("Files\\" + gameMode + "\\controlPoints.txt");
        for (String controlPoint : controlPointString) {
            String[] controlPointSplit = fileTools.splitLineOnComma(controlPoint);
            cpList.add(new ControlPoint("CP", maxCPScoreBossbar, 0, new Coordinate(Integer.parseInt(controlPointSplit[0]), Integer.parseInt(controlPointSplit[1]), Integer.parseInt(controlPointSplit[2])), Biome.valueOf(controlPointSplit[3])));
        }

        // Players
        ArrayList<String> playersString = fileTools.GetLinesFromFile("Files\\" + gameMode + "\\players.txt");
        for (String player : playersString) {
            String[] playerSplit = fileTools.splitLineOnComma(player);
            players.add(new Player(playerSplit[0], Integer.parseInt(playerSplit[1]), Boolean.parseBoolean(playerSplit[2])));
        }

        // Quotes
        quotes = fileTools.GetLinesFromFile("Files\\" + gameMode + "\\quotes.txt");

        int[] addRates = {2, 3};
        Collections.shuffle(cpList);
        for (int i = 0; i < addRates.length; i++) {
            controlPoints.add(cpList.get(i));
            controlPoints.get(i).setAddRate(addRates[i]);
            controlPoints.get(i).setName("CP" + (i + 1));
        }

        CarePackage carePackage1 = new CarePackage("enchanting", "Enchanting Drop",
                "[{Slot:3b,id:\"minecraft:enchanted_book\",Count:1b,tag:{StoredEnchantments:[{lvl:1s,id:\"minecraft:power\"}]}},{Slot:4b,id:\"minecraft:golden_apple\",Count:1b},{Slot:5b,id:\"minecraft:enchanted_book\",Count:1b,tag:{StoredEnchantments:[{lvl:2s,id:\"minecraft:sharpness\"}]}},{Slot:12b,id:\"minecraft:apple\",Count:1b},{Slot:13b,id:\"minecraft:anvil\",Count:1b},{Slot:14b,id:\"minecraft:apple\",Count:1b},{Slot:21b,id:\"minecraft:enchanted_book\",Count:1b,tag:{StoredEnchantments:[{lvl:2s,id:\"minecraft:sharpness\"}]}},{Slot:22b,id:\"minecraft:book\",Count:1b},{Slot:23b,id:\"minecraft:enchanted_book\",Count:1b,tag:{StoredEnchantments:[{lvl:1s,id:\"minecraft:protection\"}]}}]",
                16, 70, 236);
        //carePackages.add(carePackage1);

        // Scoreboard objectives
        scoreboardObjectives.add(new ScoreboardObjective("Admin", "dummy"));
        scoreboardObjectives.add(new ScoreboardObjective("TimDum", "dummy"));
        scoreboardObjectives.add(new ScoreboardObjective("TimeDum", "dummy", "\"Elapsed Time\""));
        scoreboardObjectives.add(new ScoreboardObjective("Time", "dummy", "\"Elapsed Time\"", true));
        scoreboardObjectives.add(new ScoreboardObjective("Time2", "dummy", "\"Elapsed Time\""));
        scoreboardObjectives.add(new ScoreboardObjective("SideDum", "dummy"));
        scoreboardObjectives.add(new ScoreboardObjective("ControlPoint1", "dummy"));
        scoreboardObjectives.add(new ScoreboardObjective("ControlPoint2", "dummy"));
        scoreboardObjectives.add(new ScoreboardObjective("MSGDum1CP1", "dummy"));
        scoreboardObjectives.add(new ScoreboardObjective("MSGDum2CP1", "dummy"));
        scoreboardObjectives.add(new ScoreboardObjective("MSGDum1CP2", "dummy"));
        scoreboardObjectives.add(new ScoreboardObjective("MSGDum2CP2", "dummy"));
        scoreboardObjectives.add(new ScoreboardObjective("Highscore1", "dummy"));
        scoreboardObjectives.add(new ScoreboardObjective("Highscore2", "dummy"));
        scoreboardObjectives.add(new ScoreboardObjective("Hearts", "health", true));
        scoreboardObjectives.add(new ScoreboardObjective("Apples", "minecraft.used:minecraft.golden_apple", "\"Golden Apple\"", true));
        scoreboardObjectives.add(new ScoreboardObjective("Stone", "minecraft.mined:minecraft.stone"));
        scoreboardObjectives.add(new ScoreboardObjective("Diorite", "minecraft.mined:minecraft.diorite"));
        scoreboardObjectives.add(new ScoreboardObjective("Andesite", "minecraft.mined:minecraft.andesite"));
        scoreboardObjectives.add(new ScoreboardObjective("Granite", "minecraft.mined:minecraft.granite"));
        scoreboardObjectives.add(new ScoreboardObjective("Deepslate", "minecraft.mined:minecraft.deepslate"));
        scoreboardObjectives.add(new ScoreboardObjective("Mining", "dummy", "\"I like mining-leaderboard\"", true));
        scoreboardObjectives.add(new ScoreboardObjective("Deaths", "deathCount"));
        scoreboardObjectives.add(new ScoreboardObjective("Kills", "playerKillCount", true));
        scoreboardObjectives.add(new ScoreboardObjective("Crystal", "dummy"));
        scoreboardObjectives.add(new ScoreboardObjective("Quits", "minecraft.custom:minecraft.leave_game"));
        scoreboardObjectives.add(new ScoreboardObjective("Rank", "dummy"));
        scoreboardObjectives.add(new ScoreboardObjective("WorldLoad", "dummy"));
        scoreboardObjectives.add(new ScoreboardObjective("CollarCheck0", "dummy"));
        scoreboardObjectives.add(new ScoreboardObjective("CollarCheck1", "dummy"));
        scoreboardObjectives.add(new ScoreboardObjective("MinHealth", "dummy"));
        scoreboardObjectives.add(new ScoreboardObjective("Victory", "dummy"));

        // Status effects
        effects.add(new StatusEffect(Effect.glowing, 30, 1));
        effects.add(new StatusEffect(Effect.fire_resistance, 20, 1));
        effects.add(new StatusEffect(Effect.nausea, 10, 1));
        effects.add(new StatusEffect(Effect.speed, 20, 1));
    }

    private void makeLootTableFiles() {
        ArrayList<LootTableEntry> lootEntry = new ArrayList<>();

        // Loot table items
        lootEntry.add(new LootTableEntry(17, "egg"));
        lootEntry.add(new LootTableEntry(17, "ladder", new LootTableFunction(10)));
        lootEntry.add(new LootTableEntry(15, "stick", new LootTableFunction(8)));
        lootEntry.add(new LootTableEntry(15, "diorite", new LootTableFunction(16)));
        lootEntry.add(new LootTableEntry(15, "amethyst_block", new LootTableFunction(16)));
        lootEntry.add(new LootTableEntry(15, "iron_ingot", new LootTableFunction(8)));
        lootEntry.add(new LootTableEntry(14, "arrow", new LootTableFunction(10)));
        lootEntry.add(new LootTableEntry(11, "bone", new LootTableFunction(3, 0.4)));
        lootEntry.add(new LootTableEntry(10, "copper_block", new LootTableFunction(16)));
        lootEntry.add(new LootTableEntry(10, "bread", new LootTableFunction(5)));
        lootEntry.add(new LootTableEntry(10, "cobweb", new LootTableFunction(2, 0.4)));
        lootEntry.add(new LootTableEntry(8, "fishing_rod"));
        lootEntry.add(new LootTableEntry(8, "obsidian", new LootTableFunction(4)));
        lootEntry.add(new LootTableEntry(7, "glass", new LootTableFunction(3)));
        lootEntry.add(new LootTableEntry(7, "melon_slice", new LootTableFunction(3, 0.4)));
        lootEntry.add(new LootTableEntry(5, "tnt", new LootTableFunction(4)));
        lootEntry.add(new LootTableEntry(5, "experience_bottle", new LootTableFunction(3, 0.2)));
        lootEntry.add(new LootTableEntry(5, "book"));
        lootEntry.add(new LootTableEntry(5, "redstone", new LootTableFunction(16)));
        lootEntry.add(new LootTableEntry(5, "gunpowder", new LootTableFunction(16)));
        lootEntry.add(new LootTableEntry(5, "gold_ingot", new LootTableFunction(3, 0.3)));
        lootEntry.add(new LootTableEntry(5, "lapis_lazuli", new LootTableFunction(10)));
        lootEntry.add(new LootTableEntry(4, "lava_bucket"));
        lootEntry.add(new LootTableEntry(4, "apple", new LootTableFunction(2, 0.3)));
        lootEntry.add(new LootTableEntry(4, "diamond", new LootTableFunction(2, 0.3)));
        lootEntry.add(new LootTableEntry(3, "saddle"));
        lootEntry.add(new LootTableEntry(3, "spectral_arrow", new LootTableFunction(10)));
        lootEntry.add(new LootTableEntry(3, "horse_spawn_egg"));
        lootEntry.add(new LootTableEntry(3, "glowstone_dust", new LootTableFunction(6)));
        lootEntry.add(new LootTableEntry(1, "diamond_horse_armor"));
        lootEntry.add(new LootTableEntry(2, "nether_wart", new LootTableFunction(5)));
        lootEntry.add(new LootTableEntry(2, "blaze_rod", new LootTableFunction(2, 0.1)));
        lootEntry.add(new LootTableEntry(2, "golden_apple"));
        lootEntry.add(new LootTableEntry(2, "anvil"));
        lootEntry.add(new LootTableEntry(2, "spyglass"));
        lootEntry.add(new LootTableEntry(2, "wolf_spawn_egg", new LootTableFunction(2, 0.01)));
        lootEntry.add(new LootTableEntry(1, "ender_pearl", new LootTableFunction(2, 0.5)));
        lootEntry.add(new LootTableEntry(1, "netherite_hoe"));
        lootEntry.add(new LootTableEntry(1, "trident"));
        lootEntry.add(new LootTableEntry(1, "diamond_chestplate"));
        lootEntry.add(new LootTableEntry(1, "diamond_leggings"));
        lootEntry.add(new LootTableEntry(1, "netherite_upgrade_smithing_template"));
        lootEntry.add(new LootTableEntry(1, "netherite_scrap", new LootTableFunction(4, 0.001)));

        ArrayList<String> fileCommands = fileTools.generateLootTable(lootEntry);

        FileData fileData = new FileData("supply_drop", fileCommands, "loot_tables");
        files.add(fileData);
    }

    private BossBar getBossbarByName(String name) {
        return bossBars.stream().filter(bossBar -> name.equals(bossBar.getName())).findAny().orElse(null);
    }

    private String callFunction(String functionName) {
        return "function uhc:" + functionName;
    }

    private String callFunction(FileName functionName) {
        return callFunction("" + functionName);
    }

    private String callFunction(String functionName, double delayInSeconds) {
        return "schedule " + callFunction(functionName) + " " + (int) (delayInSeconds * tickPerSecond) + "t";
    }

    private String callFunction(FileName functionName, double delayInSeconds) {
        return callFunction("" + functionName, delayInSeconds);
    }

    private String clearFunction(String functionName) {
        return "schedule clear uhc:" + functionName;
    }

    private String clearFunction(FileName functionName) {
        return clearFunction("" + functionName);
    }

    private ArrayList<String> forceLoadAndSet(int x, int y, int z, String blockType) {
        return forceLoadAndSet(x, y, z, Dimension.overworld, blockType);
    }

    private ArrayList<String> forceLoadAndSet(int x, int y, int z, BlockType blockType) {
        return forceLoadAndSet(x, y, z, blockType + "");
    }

    private ArrayList<String> forceLoadAndSet(int x, int y, int z, String blockType, SetBlockType type) {
        return forceLoadAndSet(x, y, z, Dimension.overworld, blockType, type);
    }

    private ArrayList<String> forceLoadAndSet(int x, int y, int z, BlockType blockType, SetBlockType type) {
        return forceLoadAndSet(x, y, z, blockType + "", type);
    }

    private ArrayList<String> forceLoadAndSet(int x, int y, int z, Dimension dimension, String blockType) {
        ArrayList<String> fileCommands = new ArrayList<>();
        fileCommands.add(execute.In(dimension) +
                "forceload add " + x + " " + z + " " + x + " " + z);
        fileCommands.add(execute.In(dimension) +
                setBlock(x, y, z, blockType));
        fileCommands.add(execute.In(dimension) +
                "forceload remove " + x + " " + z + " " + x + " " + z);
        return fileCommands;
    }

    private ArrayList<String> forceLoadAndSet(int x, int y, int z, Dimension dimension, BlockType blockType) {
        return forceLoadAndSet(x, y, z, dimension, blockType + "");
    }

    private ArrayList<String> forceLoadAndSet(int x, int y, int z, Dimension dimension, String blockType, SetBlockType type) {
        ArrayList<String> fileCommands = new ArrayList<>();
        fileCommands.add(execute.In(dimension) +
                "forceload add " + x + " " + z + " " + x + " " + z);
        fileCommands.add(execute.In(dimension) +
                setBlock(x, y, z, blockType, type));
        fileCommands.add(execute.In(dimension) +
                "forceload remove " + x + " " + z + " " + x + " " + z);
        return fileCommands;
    }

    private ArrayList<String> forceLoadAndSet(int x, int y, int z, Dimension dimension, BlockType blockType, SetBlockType type) {
        return forceLoadAndSet(x, y, z, dimension, blockType + "", type);
    }

    private String setBlock(String x, String y, String z, String blockType) {
        return "setblock " + x + " " + y + " " + z + " " + blockType;
    }

    private String setBlock(int x, int y, int z, String blockType) {
        return setBlock("" + x, "" + y, "" + z, blockType);
    }

    private String setBlock(Coordinate coordinate, String blockType) {
        return setBlock("" + coordinate.getX(), "" + coordinate.getY(), "" + coordinate.getZ(), blockType);
    }

    private String setBlock(int x, int y, int z, String blockType, SetBlockType type) {
        return setBlock(x, y, z, blockType) + " " + type;
    }

    private String setBlock(Coordinate coordinate, String blockType, SetBlockType type) {
        return setBlock(coordinate, blockType) + " " + type;
    }

    private String setBlock(int x, int y, int z, BlockType blockType) {
        return setBlock(x, y, z, "minecraft:" + blockType);
    }

    private String setBlock(Coordinate coordinate, BlockType blockType) {
        return setBlock(coordinate, "minecraft:" + blockType);
    }

    private String setBlock(int x, int y, int z, BlockType blockType, SetBlockType type) {
        return setBlock(x, y, z, blockType) + " " + type;
    }

    private String setBlockRelative(int x, int y, int z, String blockType) {
        return setBlock("~" + x, "~" + y, "~" + z, blockType);
    }

    private String setBlockRelative(int x, int y, int z, BlockType blockType) {
        return setBlockRelative(x, y, z, "minecraft:" + blockType);
    }

    private String fill(String x1, String y1, String z1, String x2, String y2, String z2, String blockType) {
        return "fill " + x1 + " " + y1 + " " + z1 + " " + x2 + " " + y2 + " " + z2 + " " + blockType;
    }

    private String fill(int x1, int y1, int z1, int x2, int y2, int z2, String blockType) {
        return fill("" + x1, "" + y1, "" + z1, "" + x2, "" + y2, "" + z2, blockType);
    }

    private String fill(int x1, int y1, int z1, int x2, int y2, int z2, String blockType, SetBlockType type) {
        return fill(x1, y1, z1, x2, y2, z2, blockType) + " " + type;
    }

    private String fill(int x1, int y1, int z1, int x2, int y2, int z2, String blockType, SetBlockType type, String blockToReplace) {
        return fill(x1, y1, z1, x2, y2, z2, blockType, type) + " " + blockToReplace;
    }

    private String fill(int x1, int y1, int z1, int x2, int y2, int z2, BlockType blockType) {
        return fill(x1, y1, z1, x2, y2, z2, "minecraft:" + blockType);
    }

    private String fill(int x1, int y1, int z1, int x2, int y2, int z2, BlockType blockType, SetBlockType type) {
        return fill(x1, y1, z1, x2, y2, z2, blockType) + " " + type;
    }

    private String fill(int x1, int y1, int z1, int x2, int y2, int z2, BlockType blockType, SetBlockType type, String blockToReplace) {
        return fill(x1, y1, z1, x2, y2, z2, blockType, type) + " " + blockToReplace;
    }

    private String relativeFill(int x1, int y1, int z1, int x2, int y2, int z2, String blockType, SetBlockType type, String blockToReplace) {
        return fill("~" + x1, "~" + y1, "~" + z1, "~" + x2, "~" + y2, "~" + z2, blockType) + " " + type + " " + blockToReplace;
    }

    private String setGameRule(GameRule gamerule, boolean bool) {
        return setGameRule(gamerule, "" + bool);
    }

    private String setGameRule(GameRule gamerule, int num) {
        return setGameRule(gamerule, "" + num);
    }

    private String setGameRule(GameRule gamerule, String string) {
        return "gamerule " + gamerule + " " + string;
    }

    private String playSound(Sound sound, SoundSource source, String entity, String x, String y, String z, String x1, String y1, String z1) {
        return "playsound " + sound.getValue() + " " + source + " " + entity + " " + x + " " + y + " " + z + " " + x1 + " " + y1 + " " + z1;
    }

    private void makeFunctionFiles() {
        files.add(Initialize());
        files.add(DropPlayerHeads());
        files.add(BossBarValue());
        files.add(ClearEnderChest());
        files.add(EquipGear());
        files.add(GodMode());
        files.add(DeveloperMode());

        for (int i = 1; i < 9; i++) {
            files.add(RandomTeams(i));
        }
        files.add(Predictions());
        files.add(IntoCalls());
        files.add(SpreadPlayers());
        files.add(SurvivalMode());
        files.add(StartGame());
        files.add(BattleRoyale());
        files.add(InitializeControlpoint());
        files.add(SecondControlpoint());

        for (int i = 1; i < 3; i++) {
            files.add(Minute(i));
        }
        files.add(TraitorCheck());
        files.add(TeamsAliveCheck());
        files.add(TeamsHighscoreCheck());
        files.add(ControlPointCaptured());
        files.add(Victory());
        for (int i = 0; i < teams.size(); i++) {
            files.add(VictoryMessage(teams.get(i), i));
        }
        files.add(VictoryTraitor());
        files.add(DeathMatch());

        for (int i = 1; i < controlPoints.size() + 1; i++) {
            files.add(Controlpoint(i));
        }

        //for (CarePackage carepackage : carePackages) {
        //    files.add(Carepackage(carepackage));
        //}
        //files.add(ControlpointMessages());
        files.add(DropCarepackages());
        files.add(CarepackageDistributor());
        //files.add(GiveInstructions());
        files.add(InstructionHandoutLoop());
        files.add(TraitorHandout());
        files.add(TraitorActionBar());
        //files.add(TeamScoreLegacy());
        files.add(TeamScore());

        files.add(SpawnControlPoints());
        files.add(DisplayRank());

        // Effects given when the target blocks are hit on the Control Point
        for (int i = 0; i < 4; i++) {
            files.add(GiveStatusEffect(i));
        }
        files.add(WorldPreload());
        files.add(WorldPreLoadActivation());
        files.add(HorseFrostWalker());
        files.add(WolfCollarExecute());
        files.add(UpdateSidebar());
        files.add(Timer());
        //files.add(CreateControlpointRedstone());
        files.add(ControlPointPerks());
        files.add(DisplayQuotes());
        files.add(UpdateMineCount());
        files.add(ResetRespawnHealth());
        files.add(UpdateMinHealth());
        //files.add(SpawnNetherPortal());
        files.add(ClearSchedule());
        files.add(LocateTeammate());
        files.add(UnleashLava());
    }

    private FileData Initialize() {
        ArrayList<String> fileCommands = new ArrayList<>();
        fileCommands.add(setGameRule(GameRule.naturalRegeneration, false));
        fileCommands.add(setGameRule(GameRule.doImmediateRespawn, true));
        fileCommands.add(setGameRule(GameRule.doPatrolSpawning, false));
        fileCommands.add(setGameRule(GameRule.doMobSpawning, false));
        fileCommands.add(setGameRule(GameRule.doWeatherCycle, false));
        fileCommands.add("difficulty hard");
        fileCommands.add("defaultgamemode adventure");
        fileCommands.add("setworldspawn 0 221 0");

        //scoreboard
        for (ScoreboardObjective objective : scoreboardObjectives) {
            fileCommands.add(objective.add());
        }
        fileCommands.add(new ScoreboardObjective().setDisplay("belowName", "Hearts"));
        fileCommands.add(new ScoreboardObjective().setDisplay("list", "Hearts"));
        //end scoreboard
        //bossbar

        fileCommands.add(getBossbarByName("cp1").remove());
        fileCommands.add(getBossbarByName("cp2").remove());
        fileCommands.add(getBossbarByName("cp1").add(controlPoints.get(0).getName() + ": " + controlPoints.get(0).getCoordinate().getX() + ", " + controlPoints.get(0).getCoordinate().getY() + ", " + controlPoints.get(0).getCoordinate().getZ() + " (" + controlPoints.get(0).getCoordinate().getDimensionName() + ")"));
        fileCommands.add(getBossbarByName("cp1").setMax(controlPoints.get(0).getMaxVal()));
        fileCommands.add(getBossbarByName("cp2").add(controlPoints.get(1).getName() + " soon: " + controlPoints.get(1).getCoordinate().getX() + ", " + controlPoints.get(1).getCoordinate().getY() + ", " + controlPoints.get(1).getCoordinate().getZ() + " (" + controlPoints.get(1).getCoordinate().getDimensionName() + ")"));
        fileCommands.add(getBossbarByName("cp2").setMax(controlPoints.get(1).getMaxVal()));
        fileCommands.add(getBossbarByName("carepackage").add("Care Package available at x, y, z"));
        //end bossbar
        //teams
        for (Team t : teams) {
            fileCommands.add(t.add());
            fileCommands.add("team modify " + t.getName() + " color " + t.getColor());
            for (int i = 1; i < controlPoints.size() + 1; i++) {
                fileCommands.add(new ScoreboardObjective().add("CP" + i + t.getName(), "dummy"));
            }
        }
        //end teams
        //structure
        fileCommands.add(execute.In(Dimension.overworld) +
                fill(-6, 220, -6, 6, 226, 6, BlockType.barrier));
        fileCommands.add(execute.In(Dimension.overworld) +
                fill(-5, 221, -5, 5, 226, 5, BlockType.air));
        fileCommands.add(execute.In(Dimension.overworld) +
                setBlock(0, 222, -5, "minecraft:oak_wall_sign[facing=south,waterlogged=false]{Color:\"black\",GlowingText:0b,Text1:'{\"clickEvent\":{\"action\":\"run_command\",\"value\":\"tp @s 5 " + (worldBottom + 5) + " 5\"},\"text\":\"\"}',Text2:'{\"text\":\"Teleport to\"}',Text3:'{\"text\":\"Command Center\"}',Text4:'{\"text\":\"\"}'}"));
        fileCommands.add(execute.In(Dimension.overworld) +
                setBlock(-2, worldBottom, -2, "minecraft:structure_block[mode=load]{metadata:\"\",mirror:\"NONE\",ignoreEnti" +
                        "ties:0b,powered:0b,seed:0L,author:\"?\",rotation:\"NONE\",posX:1,mode:\"LOAD\",posY:1,sizeX:18,posZ:1," +
                        "integrity:1.0f,showair:0b,name:\"minecraft:commandcenter_" + commandCenter + "\",sizeY:31,sizeZ:18,showboundingbox:1b}"));
        fileCommands.add(execute.In(Dimension.overworld) +
                setBlock(-2, worldBottom + 1, -2, BlockType.redstone_block));
        fileCommands.add(execute.In(Dimension.overworld) +
                fill(0, worldBottom + 5, 1, 0, worldBottom + 6, 1, BlockType.air));
        fileCommands.add(execute.In(Dimension.overworld) +
                fill(15, worldBottom + 2, 15, 9, worldBottom + 2, 15, BlockType.bedrock));
        fileCommands.add(execute.In(Dimension.overworld) +
                fill(15, worldBottom + 2, 15, 9, worldBottom + 2, 15, BlockType.redstone_block));
        //end structure
        //end structure
        //

        return new FileData(FileName.initialize, fileCommands);
    }

    private FileData DropPlayerHeads() {
        ArrayList<String> fileCommands = new ArrayList<>();
        fileCommands.add(execute.If(new Selector("@p[scores={Deaths=1}]")) +
                playSound(Sound.THUNDER, SoundSource.master, "@a", "~", "~50", "~", "100", "1", "0"));
        fileCommands.add("gamemode spectator @a[scores={Deaths=1},gamemode=!spectator]");
        fileCommands.add("scoreboard players set @a[scores={Deaths=1}] ControlPoint1 0");
        fileCommands.add("scoreboard players set @a[scores={Deaths=1}] ControlPoint2 0");
        fileCommands.add("scoreboard players set @p[scores={Admin=1}] Highscore1 1");
        fileCommands.add("scoreboard players set @p[scores={Admin=1}] Highscore2 1");
        fileCommands.add("scoreboard players set @p[scores={Admin=1}] MinHealth 20");

        ArrayList<TextItem> texts = new ArrayList<>();
        texts.add(bannerText);
        texts.add(new Text(Color.red, true, false, "A TRAITOR HAS BEEN ELIMINATED"));
        texts.add(bannerText);
        texts.add(new Text(Color.gold, true, false, "WELL DONE"));
        texts.add(bannerText);

        fileCommands.add(execute.If(new Selector("@p[scores={Deaths=1},tag=Traitor]")) +
                new TellRaw("@a", texts).sendRaw());
        for (Player p : players) {
            fileCommands.add(execute.At(new Location("@p[name=" + p.getPlayerName() + ",scores={Deaths=1}]")) +
                    "summon minecraft:item ~ ~ ~ {Item:{id:player_head,Count:1,tag:{SkullOwner:" + p.getPlayerName() + "}}}");
        }
        fileCommands.add("scoreboard players reset @a[scores={Deaths=1}] Deaths");

        return new FileData(FileName.drop_player_heads, fileCommands);
    }

    private FileData BossBarValue() {
        ArrayList<String> fileCommands = new ArrayList<>();
        for (Team t : teams) {
            fileCommands.add(execute.If(new Score("@r[limit=1,gamemode=!spectator,team=" + t.getName() + "]", "ControlPoint1", ">", "@p[scores={Admin=1}] Highscore1")) +
                    getBossbarByName("cp1").setColor(t.getBossbarColor()));
            fileCommands.add(execute.If(new Score("@r[limit=1,gamemode=!spectator,team=" + t.getName() + "]", "ControlPoint2", ">", "@p[scores={Admin=1}] Highscore2")) +
                    getBossbarByName("cp2").setColor(t.getBossbarColor()));
        }
        fileCommands.add(execute.As("@r[limit=1,gamemode=!spectator]") +
                "scoreboard players operation @p[scores={Admin=1}] Highscore1 > @s ControlPoint1");
        fileCommands.add(execute.StoreResult(getBossbarByName("cp1")
                .setValue("run scoreboard players get @p[scores={Admin=1}] Highscore1")));
        fileCommands.add(execute.As("@r[limit=1,gamemode=!spectator]") +
                "scoreboard players operation @p[scores={Admin=1}] Highscore2 > @s ControlPoint2");
        fileCommands.add(execute.StoreResult(getBossbarByName("cp2")
                .setValue("run scoreboard players get @p[scores={Admin=1}] Highscore2")));

        return new FileData(FileName.bbvalue, fileCommands);
    }

    private FileData ClearEnderChest() {
        ArrayList<String> fileCommands = new ArrayList<>();
        for (int i = 0; i < chestSize; i++) {
            fileCommands.add("item replace entity @a enderchest." + i + " with air 1");
        }

        return new FileData(FileName.clear_enderchest, fileCommands);
    }

    private static FileData EquipGear() {
        ArrayList<String> fileCommands = new ArrayList<>();
        fileCommands.add("item replace entity @a armor.chest with minecraft:iron_chestplate");
        fileCommands.add("item replace entity @a armor.feet with minecraft:iron_boots");
        fileCommands.add("item replace entity @a armor.head with minecraft:iron_helmet");
        fileCommands.add("item replace entity @a armor.legs with minecraft:iron_leggings");
        fileCommands.add("item replace entity @a weapon.offhand with minecraft:shield");
        fileCommands.add("item replace entity @a weapon.mainhand with minecraft:iron_axe");
        fileCommands.add("item replace entity @a inventory.0 with minecraft:iron_sword");
        fileCommands.add("effect give @a minecraft:" + Effect.regeneration + " 1 255 true");

        return new FileData(FileName.equip_gear, fileCommands);
    }

    private static FileData GodMode() {
        ArrayList<String> fileCommands = new ArrayList<>();
        fileCommands.add("effect give @s minecraft:" + Effect.resistance + " 99999 4 true");
        fileCommands.add("item replace entity @s weapon.mainhand with trident{display:{Name:\"{\\\"text\\\":\\\"The Impaler\\\"}\"}, Enchantments:[{id:sharpness,lvl:999999},{id:fire_aspect,lvl:999999},{id:unbreaking,lvl:999999},{id:loyalty,lvl:999999},{id:impaling,lvl:999999}]}");

        return new FileData(FileName.god_mode, fileCommands);
    }

    private static FileData GetStartPotions() {
        ArrayList<String> fileCommands = new ArrayList<>();
        //TODO: create;
        return new FileData(FileName.god_mode, fileCommands);
    }

    private FileData DeveloperMode() {
        ArrayList<String> fileCommands = new ArrayList<>();
        fileCommands.add(setGameRule(GameRule.commandBlockOutput, true));
        fileCommands.add(setGameRule(GameRule.doDaylightCycle, false));
        fileCommands.add(setGameRule(GameRule.keepInventory, true));
        fileCommands.add(setGameRule(GameRule.doMobSpawning, false));
        fileCommands.add(setGameRule(GameRule.doTileDrops, false));
        fileCommands.add(setGameRule(GameRule.drowningDamage, false));
        fileCommands.add(setGameRule(GameRule.fallDamage, false));
        fileCommands.add(setGameRule(GameRule.fireDamage, false));
        fileCommands.add(setGameRule(GameRule.sendCommandFeedback, true));
        fileCommands.add(setGameRule(GameRule.doImmediateRespawn, true));
        fileCommands.add("scoreboard players reset @a");
        fileCommands.add(execute.In(Dimension.overworld) +
                fill(0, worldBottom + 2, 15, 0, worldBottom + 2, 2, BlockType.bedrock, SetBlockType.replace));
        fileCommands.add(execute.In(Dimension.overworld) +
                fill(2, worldBottom + 2, 0, 8, worldBottom + 2, 0, BlockType.bedrock, SetBlockType.replace));
        fileCommands.add(execute.In(Dimension.overworld) +
                fill(15, worldBottom + 2, 3, 15, worldBottom + 2, 11, BlockType.bedrock, SetBlockType.replace));
        fileCommands.add("scoreboard players set " + admin + " Admin 1");
        fileCommands.add(execute.In(Dimension.overworld) +
                fill(15, worldBottom + 2, 15, 9, worldBottom + 2, 15, BlockType.redstone_block, SetBlockType.replace));
        fileCommands.add(execute.In(Dimension.overworld) +
                setBlock(11, worldBottom + 2, 0, BlockType.bedrock, SetBlockType.destroy));
        fileCommands.add(execute.In(Dimension.overworld) +
                setBlock(10, worldBottom + 2, 0, BlockType.bedrock, SetBlockType.destroy));
        //fileCommands.add(getBossbarByName("cp").setTitle(cp1.getName() + ": " + cp1.getX() + ", " + cp1.getY() + ", " + cp1.getZ() + "; " + cp2.getName() + " soon: " + cp2.getX() + ", " + cp2.getY() + ", " + cp2.getZ()));
        fileCommands.add(execute.In(controlPoints.get(0).getCoordinate().getDimension()) + "forceload add " + controlPoints.get(0).getCoordinate().getX() + " " + controlPoints.get(0).getCoordinate().getZ() + " " + controlPoints.get(0).getCoordinate().getX() + " " + controlPoints.get(0).getCoordinate().getZ());
        fileCommands.add(execute.In(controlPoints.get(1).getCoordinate().getDimension()) + "forceload add " + controlPoints.get(1).getCoordinate().getX() + " " + controlPoints.get(1).getCoordinate().getZ() + " " + controlPoints.get(1).getCoordinate().getX() + " " + controlPoints.get(1).getCoordinate().getZ());
        fileCommands.add(callFunction(FileName.spawn_controlpoints));
        fileCommands.add(execute.In(controlPoints.get(0).getCoordinate().getDimension()) + "forceload remove " + controlPoints.get(0).getCoordinate().getX() + " " + controlPoints.get(0).getCoordinate().getZ() + " " + controlPoints.get(0).getCoordinate().getX() + " " + controlPoints.get(0).getCoordinate().getZ());
        fileCommands.add(execute.In(controlPoints.get(1).getCoordinate().getDimension()) + "forceload remove " + controlPoints.get(1).getCoordinate().getX() + " " + controlPoints.get(1).getCoordinate().getZ() + " " + controlPoints.get(1).getCoordinate().getX() + " " + controlPoints.get(1).getCoordinate().getZ());
        BossBar bossBarCp1 = getBossbarByName("cp1");
        BossBar bossBarCp2 = getBossbarByName("cp2");
        BossBar bossBarCarePackage = getBossbarByName("carepackage");
        fileCommands.add(bossBarCp1.setColor(BossBarColor.white));
        fileCommands.add(bossBarCp1.setVisible(false));
        fileCommands.add(bossBarCp1.setPlayers("@a"));
        fileCommands.add(bossBarCp1.setTitle(controlPoints.get(0).getName() + ": " + controlPoints.get(0).getCoordinate().getX() + ", " + controlPoints.get(0).getCoordinate().getY() + ", " + controlPoints.get(0).getCoordinate().getZ() + " (" + controlPoints.get(0).getCoordinate().getDimensionName() + ")"));
        fileCommands.add(bossBarCp2.setColor(BossBarColor.white));
        fileCommands.add(bossBarCp2.setVisible(false));
        fileCommands.add(bossBarCp2.setPlayers("@a"));
        fileCommands.add(bossBarCp2.setTitle(controlPoints.get(1).getName() + " soon: " + controlPoints.get(1).getCoordinate().getX() + ", " + controlPoints.get(1).getCoordinate().getY() + ", " + controlPoints.get(1).getCoordinate().getZ() + " (" + controlPoints.get(1).getCoordinate().getDimensionName() + ")"));
        fileCommands.add(bossBarCarePackage.setVisible(false));
        fileCommands.add(bossBarCarePackage.setPlayers("@a"));
        fileCommands.add(execute.In(Dimension.overworld) +
                setBlock(startCoordinate, "minecraft:jukebox[has_record=true]{RecordItem:{Count:1b,id:\"minecraft:music_disc_stal\"}}", SetBlockType.replace));
        fileCommands.add("tag @a remove Traitor");
        fileCommands.add("tag @a remove DontMakeTraitor");
        fileCommands.add("worldborder set " + worldSize + " 1");
        fileCommands.add("team leave @a");
        fileCommands.add(callFunction(FileName.display_rank));
        fileCommands.add("scoreboard players set NightTime Time 600");
        fileCommands.add("scoreboard players set CarePackages Time 1200");
        fileCommands.add("scoreboard players set ControlPoints Time 1800");
        fileCommands.add("scoreboard players set TraitorFaction Time 2400");
        for (int i = 0; i < 4; i++) {
            fileCommands.add("tag @a remove ReceivedPerk" + (i + 1));
        }

        //for (CarePackage carepackage : carePackages) {
        //    fileCommands.addAll(forceLoadAndSet(carepackage.getX(), carepackage.getY(), carepackage.getZ(), BlockType.air, SetBlockType.replace));
        //}

        fileCommands.add("gamemode creative @s");

        return new FileData(FileName.developer_mode, fileCommands);
    }

    private FileData RandomTeams(int i) {
        ArrayList<String> fileCommands = new ArrayList<>();
        for (Team t : teams) {
            fileCommands.add("team join " + t.getName() + " @r[limit=" + i + ",team=]");
        }

        return new FileData("" + FileName.random_teams + i, fileCommands);
    }

    private FileData Predictions() {
        ArrayList<String> fileCommands = new ArrayList<>();
        fileCommands.add("effect clear @a");
        fileCommands.add(execute.In(Dimension.overworld) +
                "tp @a 0 -100 0");

        ArrayList<TextItem> texts = new ArrayList<>();
        texts.add(bannerText);
        texts.add(new Text(Color.gold, true, false, communityName + " UHC"));
        texts.add(bannerText);
        texts.add(new Text(Color.light_purple, true, false, "PREDICTIONS COMPLETED"));
        texts.add(bannerText);

        fileCommands.add(new TellRaw("@a", texts).sendRaw());

        return new FileData(FileName.predictions, fileCommands);
    }

    private FileData IntoCalls() {
        ArrayList<String> fileCommands = new ArrayList<>();
        fileCommands.add(execute.In(Dimension.overworld) +
                "tp @a " + startCoordinate.getCoordinateString());
        fileCommands.add("scoreboard players reset @a Deaths");
        fileCommands.add("scoreboard players reset @a Kills");

        return new FileData(FileName.into_calls, fileCommands);
    }

    private static FileData SpreadPlayers() {
        ArrayList<String> fileCommands = new ArrayList<>();
        fileCommands.add(execute.In(Dimension.overworld) +
                "spreadplayers 0 0 300 700 true @a");
        fileCommands.add("scoreboard players set @p[scores={Admin=1}] Highscore 1");
        fileCommands.add("scoreboard players set @p[scores={Admin=1}] MinHealth 20");

        return new FileData(FileName.spread_players, fileCommands);
    }

    private FileData SurvivalMode() {
        ArrayList<String> fileCommands = new ArrayList<>();
        fileCommands.add(setGameRule(GameRule.commandBlockOutput, false));
        fileCommands.add(setGameRule(GameRule.doDaylightCycle, true));
        fileCommands.add(setGameRule(GameRule.keepInventory, false));
        fileCommands.add(setGameRule(GameRule.doMobSpawning, true));
        fileCommands.add(setGameRule(GameRule.doTileDrops, true));
        fileCommands.add(setGameRule(GameRule.drowningDamage, true));
        fileCommands.add(setGameRule(GameRule.fallDamage, true));
        fileCommands.add(setGameRule(GameRule.fireDamage, true));
        fileCommands.add(setGameRule(GameRule.doImmediateRespawn, false));
        fileCommands.add(callFunction(FileName.clear_enderchest));

        fileCommands.add("recipe give @a uhc:golden_apple");
        fileCommands.add("recipe take @a uhc:dragon_head");

        return new FileData(FileName.survival_mode, fileCommands);
    }

    private FileData StartGame() {
        ArrayList<String> fileCommands = new ArrayList<>();
        fileCommands.add("time set 0");
        fileCommands.add("xp set @a 0 levels");
        fileCommands.add("effect give @a minecraft:" + Effect.regeneration + " 1 255");
        fileCommands.add("effect give @a minecraft:" + Effect.saturation + " 1 255");
        fileCommands.add("clear @a");
        fileCommands.add(new Title("@a", TitleType.title, new Text(Color.gold, true, true, "Game Starting Now!")).displayTitle());
        fileCommands.add("gamemode survival @a");
        fileCommands.add(setGameRule(GameRule.sendCommandFeedback, false));
        fileCommands.add(execute.In(Dimension.overworld) +
                fill(0, worldBottom + 2, 15, 0, worldBottom + 2, 2, BlockType.redstone_block, SetBlockType.replace));
        fileCommands.add(execute.In(Dimension.overworld) +
                fill(2, worldBottom + 2, 0, 6, worldBottom + 2, 0, BlockType.redstone_block, SetBlockType.replace));
        fileCommands.add(execute.In(Dimension.overworld) +
                fill(15, worldBottom + 2, 15, 9, worldBottom + 2, 15, BlockType.bedrock));
        fileCommands.add(execute.In(Dimension.overworld) +
                setBlock(10, worldBottom + 2, 0, BlockType.redstone_block, SetBlockType.destroy));
        fileCommands.add("advancement revoke @a everything");
        fileCommands.add("xp set @a 0 points");
        fileCommands.add("scoreboard players set @p[scores={Admin=1}] Victory 1");
        fileCommands.add("give @a minecraft:bundle{tag:LocateTeammate}");
        //fileCommands.add("execute as @a at @s run function uhc:give_instructions");

        return new FileData(FileName.start_game, fileCommands);
    }

    private static FileData BattleRoyale() {
        ArrayList<String> fileCommands = new ArrayList<>();
        fileCommands.add(execute.In(Dimension.overworld, false) +
                execute.PositionedNext(0, 151, 0, true) +
                "gamemode survival @a[distance=..20,gamemode=!creative]");
        fileCommands.add(execute.In(Dimension.overworld, false) +
                execute.PositionedNext(0, 151, 0, true) +
                "spreadplayers 0 0 300 700 true @a[distance=..20,gamemode=survival]");

        return new FileData(FileName.battle_royale, fileCommands);
    }

    private FileData InitializeControlpoint() {
        ArrayList<String> fileCommands = new ArrayList<>();
        fileCommands.add(new Title("@a", TitleType.subtitle, new Text(Color.light_purple, true, true, "is now enabled!")).displayTitle());
        fileCommands.add(execute.In(Dimension.overworld) +
                setBlock(7, worldBottom + 2, 0, BlockType.redstone_block, SetBlockType.replace));
        fileCommands.add(new Title("@a", TitleType.title, new Text(Color.gold, true, true, "Control Point 1")).displayTitle());
        fileCommands.add(getBossbarByName("cp1").setVisible(true));
        fileCommands.add(getBossbarByName("cp2").setVisible(true));
        fileCommands.add(execute.In(Dimension.overworld) +
                setBlock(6, worldBottom + 2, 0, BlockType.bedrock, SetBlockType.replace));
        fileCommands.addAll(forceLoadAndSet(controlPoints.get(0).getCoordinate().getX(), controlPoints.get(0).getCoordinate().getY() + 3, controlPoints.get(0).getCoordinate().getZ(), BlockType.air, SetBlockType.replace));
        fileCommands.add(execute.In(Dimension.overworld) +
                setBlock(15, worldBottom + 2, 7, BlockType.redstone_block, SetBlockType.replace));
        fileCommands.add(execute.In(Dimension.overworld) +
                setBlock(15, worldBottom + 2, 6, BlockType.redstone_block, SetBlockType.replace));
        fileCommands.add(execute.In(Dimension.overworld) +
                setBlock(15, worldBottom + 2, 10, BlockType.redstone_block, SetBlockType.replace));
        fileCommands.add(setGameRule(GameRule.doDaylightCycle, false));

        return new FileData(FileName.initialize_controlpoint, fileCommands);
    }

    private FileData SecondControlpoint() {
        ArrayList<String> fileCommands = new ArrayList<>();
        fileCommands.add(execute.In(Dimension.overworld) +
                setBlock(7, worldBottom + 2, 0, BlockType.bedrock, SetBlockType.replace));
        fileCommands.add(execute.In(Dimension.overworld) +
                setBlock(8, worldBottom + 2, 0, BlockType.redstone_block, SetBlockType.replace));

        ArrayList<TextItem> texts = new ArrayList<>();
        texts.add(bannerText);
        texts.add(new Text(Color.gold, true, false, communityName + " UHC"));
        texts.add(bannerText);
        texts.add(new Text(Color.light_purple, true, false, "CONTROL POINT 2 IS NOW AVAILABLE!"));
        texts.add(bannerText);

        fileCommands.add(new TellRaw("@a", texts).sendRaw());
        fileCommands.add(execute.In(Dimension.overworld) +
                setBlock(15, worldBottom + 2, 11, BlockType.redstone_block, SetBlockType.replace));
        fileCommands.addAll(forceLoadAndSet(controlPoints.get(1).getCoordinate().getX(), controlPoints.get(1).getCoordinate().getY() + 3, controlPoints.get(1).getCoordinate().getZ(), controlPoints.get(1).getCoordinate().getDimension(), BlockType.air, SetBlockType.replace));
        fileCommands.add(getBossbarByName("cp2").setTitle("CP2: " + controlPoints.get(1).getCoordinate().getX() + ", " + controlPoints.get(1).getCoordinate().getY() + ", " + controlPoints.get(1).getCoordinate().getZ() + " (" + controlPoints.get(1).getCoordinate().getDimensionName() + ") - FASTER!!"));
        //fileCommands.add("give @a[scores={ControlPoint1=14400..}] minecraft:splash_potion{CustomPotionEffects:[{Id:11,Duration:1200},{Id:24,Duration:1200}],CustomPotionColor:15462415,display:{Name:\"\\\"Hero of the First Control Point\\\"\",Lore:[\"Thank you for enabling the second Control Point! Good luck with winning the match!\"]}}");
        //fileCommands.add(callFunction(FileName.carepackage_ + carePackages.get(0).getName()));


        return new FileData(FileName.second_controlpoint, fileCommands);
    }

    private FileData Minute(int i) {
        ArrayList<String> fileCommands = new ArrayList<>();
        ArrayList<TextItem> texts = new ArrayList<>();
        texts.add(bannerText);
        texts.add(new Text(Color.gold, true, false, communityName + " UHC"));
        texts.add(bannerText);
        texts.add(new Text(Color.light_purple, true, false, i + " minute(s) remaining"));
        texts.add(bannerText);

        fileCommands.add(new TellRaw("@a", texts).sendRaw());
        fileCommands.add(new Title("@a", TitleType.title, new Text(Color.gold, true, true, i + " minute(s) remaining.")).displayTitle());
        return new FileData("" + FileName.minute_ + i, fileCommands);
    }

    private FileData Victory() {
        ArrayList<String> fileCommands = new ArrayList<>();
        fileCommands.add("scoreboard players set @p[scores={Admin=1}] Victory 2");
        fileCommands.add(callFunction(FileName.minute_ + "2", 60));
        fileCommands.add(callFunction(FileName.minute_ + "1", 60 * 2));
        fileCommands.add(callFunction(FileName.death_match, 60 * 3));
        return new FileData(FileName.victory, fileCommands);
    }

    private FileData VictoryMessage(Team team, int i) {
        ArrayList<String> fileCommands = new ArrayList<>();
        ArrayList<TextItem> texts = new ArrayList<>();
        texts.add(bannerText);
        texts.add(new Text(Color.gold, true, false, communityName + " UHC"));
        texts.add(bannerText);
        texts.add(new Text(team.getColor(), true, false, team.getJSONColor()));
        texts.add(new Text(Color.light_purple, true, false, " TEAM VICTORY HAS BEEN ACHIEVED! 3 MINUTES UNTIL THE FINAL DEATHMATCH"));
        texts.add(bannerText);

        fileCommands.add(new TellRaw("@a", texts).sendRaw());
        fileCommands.add(new Title("@a", TitleType.subtitle, new Text(Color.light_purple, true, true, "has been achieved!")).displayTitle());
        fileCommands.add(new Title("@a", TitleType.title, new Text(Color.gold, true, true, team.getJSONColor() + " team victory")).displayTitle());
        fileCommands.add(callFunction(FileName.victory));
        return new FileData("" + FileName.victory_message_ + i, fileCommands);
    }

    private FileData VictoryTraitor() {
        ArrayList<String> fileCommands = new ArrayList<>();
        ArrayList<TextItem> texts = new ArrayList<>();
        texts.add(bannerText);
        texts.add(new Text(Color.gold, true, false, communityName + " UHC"));
        texts.add(bannerText);
        texts.add(new Text(Color.light_purple, true, false, " TRAITOR VICTORY HAS BEEN ACHIEVED! 3 MINUTES UNTIL THE FINAL DEATHMATCH"));
        texts.add(bannerText);

        fileCommands.add(new TellRaw("@a", texts).sendRaw());
        fileCommands.add(new Title("@a", TitleType.subtitle, new Text(Color.light_purple, true, true, "ggez")).displayTitle());
        fileCommands.add(new Title("@a", TitleType.title, new Text(Color.gold, true, true, "Traitors Win")).displayTitle());
        fileCommands.add(callFunction(FileName.victory));
        return new FileData(FileName.victory_message_traitor, fileCommands);
    }

    private FileData DeathMatch() {
        ArrayList<String> fileCommands = new ArrayList<>();
        fileCommands.add("worldborder set 400");
        fileCommands.add("worldborder set 20 180");
        fileCommands.add(execute.In(Dimension.overworld) +
                "tp @a[gamemode=!spectator] 3 153 3");
        fileCommands.add(execute.In(Dimension.overworld) +
                "spreadplayers 0 0 75 150 true @a[gamemode=!spectator]");
        fileCommands.add(getBossbarByName("cp").setVisible(false));

        return new FileData(FileName.death_match, fileCommands);
    }

    private FileData Controlpoint(int i) {
        ArrayList<String> fileCommands = new ArrayList<>();
        for (Team team : teams) {
            fileCommands.add(execute.In(controlPoints.get(i - 1).getCoordinate().getDimension(), false) +
                    execute.AsNext("@a[gamemode=!spectator,team=" + team.getName() + "]", false) +
                    execute.IfNext(new Selector("@a[gamemode=!spectator,x=" + (controlPoints.get(i - 1).getCoordinate().getX() - 6) + ",y=" + (controlPoints.get(i - 1).getCoordinate().getY() - 1) + ",z=" + (controlPoints.get(i - 1).getCoordinate().getZ() - 6) + ",dx=12,dy=12,dz=12]")) +
                    execute.AtNext(new Location("@s")) +
                    execute.UnlessNext(new Selector("@a[gamemode=!spectator,x=" + (controlPoints.get(i - 1).getCoordinate().getX() - 6) + ",y=" + (controlPoints.get(i - 1).getCoordinate().getY() - 1) + ",z=" + (controlPoints.get(i - 1).getCoordinate().getZ() - 6) + ",dx=12,dy=12,dz=12,team=!" + team.getName() + "]"), true) +
                    "scoreboard players add @s ControlPoint" + i + " " + controlPoints.get(i - 1).getAddRate());

            fileCommands.add(execute.In(controlPoints.get(i - 1).getCoordinate().getDimension(), false) +
                    execute.IfNext(new Score("@r[limit=1,gamemode=!spectator,team=" + team.getName() + "]", "ControlPoint" + i, ">", "@p[scores={Admin=1}] Highscore" + i), true) +
                    setBlock(controlPoints.get(i - 1).getCoordinate().getX(), controlPoints.get(i - 1).getCoordinate().getY() + 1, controlPoints.get(i - 1).getCoordinate().getZ(), "minecraft:" + team.getGlassColor() + "_stained_glass", SetBlockType.replace));
        }
        fileCommands.add(execute.In(controlPoints.get(i - 1).getCoordinate().getDimension(), false) +
                execute.IfNext(new Selector("@p[x=" + (controlPoints.get(i - 1).getCoordinate().getX() - 6) + ",y=" + (controlPoints.get(i - 1).getCoordinate().getY() - 1) + ",z=" + (controlPoints.get(i - 1).getCoordinate().getZ() - 6) + ",dx=12,dy=12,dz=12,gamemode=!spectator]"), true) +
                "scoreboard players add @a[x=" + (controlPoints.get(i - 1).getCoordinate().getX() - 6) + ",y=" + (controlPoints.get(i - 1).getCoordinate().getY() - 1) + ",z=" + (controlPoints.get(i - 1).getCoordinate().getZ() - 6) + ",dx=12,dy=12,dz=12,gamemode=!spectator] MSGDum1CP" + i + " 1");

        ArrayList<TextItem> texts = new ArrayList<>();
        texts.add(bannerText);
        texts.add(new Text(Color.gold, true, false, communityName + " UHC"));
        texts.add(bannerText);
        texts.add(new Text(Color.light_purple, true, false, "A PLAYER IS ATTACKING CONTROL POINT " + i + "!"));
        texts.add(bannerText);

        fileCommands.add(execute.In(controlPoints.get(i - 1).getCoordinate().getDimension(), false) +
                execute.IfNext(new Selector("@p[x=" + (controlPoints.get(i - 1).getCoordinate().getX() - 6) + ",y=" + (controlPoints.get(i - 1).getCoordinate().getY() - 1) + ",z=" + (controlPoints.get(i - 1).getCoordinate().getZ() - 6) + ",dx=12,dy=12,dz=12,gamemode=!spectator,scores={MSGDum1CP" + i + "=" + (10 * tickPerSecond) + "}]"), true) +
                new TellRaw("@a", texts).sendRaw());

        fileCommands.add(execute.In(controlPoints.get(i - 1).getCoordinate().getDimension(), false) +
                execute.IfNext(new Selector("@p[x=" + (controlPoints.get(i - 1).getCoordinate().getX() - 6) + ",y=" + (controlPoints.get(i - 1).getCoordinate().getY() - 1) + ",z=" + (controlPoints.get(i - 1).getCoordinate().getZ() - 6) + ",dx=12,dy=12,dz=12,gamemode=!spectator,scores={MSGDum1CP" + i + "=" + (10 * tickPerSecond) + "}]"), true) +
                "scoreboard players reset @a[x=" + (controlPoints.get(i - 1).getCoordinate().getX() - 6) + ",y=" + (controlPoints.get(i - 1).getCoordinate().getY() - 1) + ",z=" + (controlPoints.get(i - 1).getCoordinate().getZ() - 6) + ",dx=12,dy=12,dz=12,gamemode=!spectator] MSGDum2CP" + i);

        fileCommands.add(execute.In(controlPoints.get(i - 1).getCoordinate().getDimension(), false) +
                execute.PositionedNext(controlPoints.get(i - 1).getCoordinate().getX(), controlPoints.get(i - 1).getCoordinate().getY() + 5, controlPoints.get(i - 1).getCoordinate().getZ(), false) +
                execute.IfNext(new Selector("@p[distance=9..,gamemode=!spectator, scores={MSGDum1CP" + i + "=" + (10 * tickPerSecond) + "..}]"), true) +
                "scoreboard players add @a[distance=9..,gamemode=!spectator, scores={MSGDum1CP" + i + "=" + (10 * tickPerSecond) + "..}] MSGDum2CP" + i + " 1");

        ArrayList<TextItem> texts2 = new ArrayList<>();
        texts2.add(bannerText);
        texts2.add(new Text(Color.gold, true, false, communityName + " UHC"));
        texts2.add(bannerText);
        texts2.add(new Text(Color.light_purple, true, false, "A PLAYER HAS LEFT CONTROL POINT " + i));
        texts2.add(bannerText);

        fileCommands.add(execute.In(controlPoints.get(i - 1).getCoordinate().getDimension(), false) +
                execute.PositionedNext(controlPoints.get(i - 1).getCoordinate().getX(), controlPoints.get(i - 1).getCoordinate().getY() + 5, controlPoints.get(i - 1).getCoordinate().getZ(), false) +
                execute.IfNext(new Selector("@p[distance=9..,gamemode=!spectator,scores={MSGDum2CP" + i + "=" + (10 * tickPerSecond) + ",MSGDum1CP" + i + "=" + (10 * tickPerSecond) + "..}]"), true) +
                new TellRaw("@a", texts2).sendRaw());

        fileCommands.add(execute.In(controlPoints.get(i - 1).getCoordinate().getDimension(), false) +
                execute.PositionedNext(controlPoints.get(i - 1).getCoordinate().getX(), controlPoints.get(i - 1).getCoordinate().getY() + 5, controlPoints.get(i - 1).getCoordinate().getZ(), false) +
                execute.IfNext(new Selector("@p[distance=9..,gamemode=!spectator,scores={MSGDum2CP" + i + "=" + (10 * tickPerSecond) + ",MSGDum1CP" + i + "=" + (10 * tickPerSecond) + "..}]"), true) +
                "scoreboard players reset @a[distance=9..,gamemode=!spectator] MSGDum1CP" + i);

        return new FileData("" + FileName.controlpoint_ + i, fileCommands);
    }

    private FileData Carepackage(CarePackage carepackage) {
        ArrayList<String> fileCommands = forceLoadAndSet(carepackage.getX(), carepackage.getY(), carepackage.getZ(), "chest{CustomName:\"\\\"" + carepackage.getDisplayName() + "\\\"\",Items:" + carepackage.getNbtTag() + "}");

        fileCommands.add(new Title("@a", TitleType.title, new Text(Color.gold, true, true, carepackage.getDisplayName() + "!")).displayTitle());
        fileCommands.add(new Title("@a", TitleType.subtitle, new Text(Color.light_purple, true, true, "Delivered now on the surface!")).displayTitle());
        fileCommands.add("give @a[gamemode=!spectator] minecraft:compass{display:{Name:\"{\\\"text\\\":\\\"" + carepackage.getDisplayName() + " available at " + carepackage.getX() + ", " + carepackage.getY() + ", " + carepackage.getZ() + "\\\"}\"}, LodestoneDimension:\"minecraft:" + Dimension.overworld + "\",LodestoneTracked:0b,LodestonePos:{X:" + carepackage.getX() + ",Y:" + carepackage.getY() + ",Z:" + carepackage.getZ() + "}}");

        return new FileData("" + FileName.carepackage_ + carepackage.getName(), fileCommands);
    }

    private FileData ControlpointMessages() {
        ArrayList<String> fileCommands = new ArrayList<>();
        for (int i = 1; i < controlPoints.size() + 1; i++) {
            fileCommands.add("execute at @e[type=minecraft:end_crystal,scores={Crystal=" + i + "}] positioned ~ ~200 ~ if entity @p[distance=..5,gamemode=!spectator] run scoreboard players add @a[distance=..5,gamemode=!spectator] MSGDum1CP" + i + " 1");
            fileCommands.add("execute at @e[type=minecraft:end_crystal,scores={Crystal=" + i + "}] positioned ~ ~200 ~ if entity @p[distance=..5,gamemode=!spectator,scores={MSGDum1CP" + i + "=200}] run tellraw @a [\"\",{\"text\":\" ⎜ \",\"color\":\"gray\"},{\"text\":\"THE DIORITE EXPERTS UHC\",\"color\":\"gold\"},{\"text\":\" ⎜ \",\"color\":\"gray\"},{\"text\":\"A PLAYER IS ATTACKING CONTROL POINT " + i + "!\",\"color\":\"light_purple\"},{\"text\":\" ⎜ \",\"color\":\"gray\"}]");
            fileCommands.add("execute at @e[type=minecraft:end_crystal,scores={Crystal=" + i + "}] positioned ~ ~200 ~ if entity @p[distance=..5,gamemode=!spectator,scores={MSGDum1CP" + i + "=200}] run scoreboard players reset @a[distance=..5,gamemode=!spectator] MSGDum2CP" + i);

            fileCommands.add("execute at @e[type=minecraft:end_crystal,scores={Crystal=" + i + "}] positioned ~ ~200 ~ if entity @p[distance=6..,gamemode=!spectator, scores={MSGDum1CP" + i + "=200..}] run scoreboard players add @a[distance=6..,gamemode=!spectator, scores={MSGDum1CP" + i + "=200..}] MSGDum2CP" + i + " 1");
            fileCommands.add("execute at @e[type=minecraft:end_crystal,scores={Crystal=" + i + "}] positioned ~ ~200 ~ if entity @p[distance=6..,gamemode=!spectator,scores={MSGDum2CP" + i + "=200,MSGDum1CP" + i + "=200..}] run tellraw @a [\"\",{\"text\":\" ⎜ \",\"color\":\"gray\"},{\"text\":\"THE DIORITE EXPERTS UHC\",\"color\":\"gold\"},{\"text\":\" ⎜ \",\"color\":\"gray\"},{\"text\":\"A PLAYER HAS LEFT CONTROL POINT " + i + "\",\"color\":\"light_purple\"},{\"text\":\" ⎜ \",\"color\":\"gray\"}]");
            fileCommands.add("execute at @e[type=minecraft:end_crystal,scores={Crystal=" + i + "}] positioned ~ ~200 ~ if entity @p[distance=6..,gamemode=!spectator,scores={MSGDum2CP" + i + "=200,MSGDum1CP" + i + "=200..}] run scoreboard players reset @a[distance=6..,gamemode=!spectator] MSGDum1CP" + i);
        }

        return new FileData(FileName.controlpoint_messages, fileCommands);
    }

    private static FileData DropCarepackages() {
        ArrayList<String> fileCommands = new ArrayList<>();

        fileCommands.add(new Title("@a", TitleType.title, new Text(Color.gold, true, true, carePackageAmount + " Supply Drops!")).displayTitle());
        fileCommands.add(new Title("@a", TitleType.subtitle, new Text(Color.light_purple, true, true, "Delivered NOW on the surface!")).displayTitle());

        for (int i = 0; i < carePackageAmount; i++) {
            fileCommands.add(execute.In(Dimension.overworld) +
                    "summon minecraft:area_effect_cloud ~ ~5 ~ {Passengers:[{id:falling_block,Time:1,DropItem:0b,BlockState:{Name:\"minecraft:chest\"},TileEntityData:{CustomName:\"\\\"Loot chest\\\"\",LootTable:\"uhc:supply_drop\"}}]}");
        }

        return new FileData(FileName.drop_carepackages, fileCommands);
    }

    private static FileData CarepackageDistributor() {
        ArrayList<String> fileCommands = new ArrayList<>();

        fileCommands.add(execute.In(Dimension.overworld, false) +
                execute.IfNext(new Selector("@e[type=minecraft:falling_block,distance=..2]"), true) +
                "spreadplayers 0 0 10 500 false @e[type=minecraft:falling_block,distance=..2]");

        return new FileData(FileName.carepackage_distributor, fileCommands);
    }

    private FileData GiveInstructions() {
        ArrayList<String> fileCommands = new ArrayList<>();

        fileCommands.add("give @p written_book{pages:['[\"\",{\"text\":\"The Diorite Experts\",\"bold\":true},{\"text\":\"\\\\nUltraHardCore S38\\\\nSa 07/11/2020\\\\n\\\\n\\\\n\",\"color\":\"reset\"},{\"text\":\"Rules & Information\",\"bold\":true,\"underlined\":true},{\"text\":\"\\\\n\\\\n\\\\n\\\\nWritten by: Snodog627\",\"color\":\"reset\"}]','[\"\",{\"text\":\"Contents\",\"bold\":true,\"underlined\":true},{\"text\":\"\\\\n\\\\n\",\"color\":\"reset\"},{\"text\":\"Introduction.......................3\",\"clickEvent\":{\"action\":\"change_page\",\"value\":3},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Go to Page 3\"}},{\"text\":\"\\\\n\"},{\"text\":\"Victory conditions........6\",\"clickEvent\":{\"action\":\"change_page\",\"value\":6},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Go to Page 6\"}},{\"text\":\"\\\\n\"},{\"text\":\"Rules........................................8\",\"clickEvent\":{\"action\":\"change_page\",\"value\":8},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Go to Page 8\"}},{\"text\":\"\\\\n\"},{\"text\":\"Utility.....................................10\",\"clickEvent\":{\"action\":\"change_page\",\"value\":10},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Go to Page 10\"}},{\"text\":\"\\\\n\"},{\"text\":\"Control Point.................11\",\"clickEvent\":{\"action\":\"change_page\",\"value\":11},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Go to Page 11\"}},{\"text\":\"\\\\n\"},{\"text\":\"Care Package...............16\",\"clickEvent\":{\"action\":\"change_page\",\"value\":16},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Go to Page 16\"}},{\"text\":\"\\\\n\"},{\"text\":\"Change log.....................19\",\"clickEvent\":{\"action\":\"change_page\",\"value\":19},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Go to Page 19\"}},{\"text\":\"\\\\n\"},{\"text\":\"Statistics...........................20\",\"clickEvent\":{\"action\":\"change_page\",\"value\":20},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Go to Page 20\"}},{\"text\":\"\\\\n \"}]','[\"\",{\"text\":\"Introduction\",\"bold\":true,\"underlined\":true},{\"text\":\"\\\\nWelcome to the 38th season of The Diorite Experts UltraHardCore! UHC is a survival/PvP game in Minecraft and is special because of its non-natural health regeneration. After the survival period where players get geared up it is time for the final PvP battle that can\",\"color\":\"reset\"}]','{\"text\":\"happen at any moment. The team of the last player standing wins the match. In addition to that, it is also possible to win the game by capturing Control Points. On the side, players can also gather loot by completing side quests: Care Package drops.\"}','[\"\",{\"text\":\"We hope you will have a wonderful time and good luck taking the crown!\\\\n\\\\n\\\\n\"},{\"text\":\"GLHF\",\"bold\":true},{\"text\":\"\\\\n~Bas, Luc, Wouter\\\\nS38 UHC Committee\\\\n \",\"color\":\"reset\"}]','[\"\",{\"text\":\"Victory Conditions\",\"bold\":true,\"underlined\":true},{\"text\":\"\\\\nThere are two ways to win UHC:\\\\n\",\"color\":\"reset\"},{\"text\":\"1.\",\"bold\":true},{\"text\":\" Be the last team standing\\\\n\",\"color\":\"reset\"},{\"text\":\"2. \",\"bold\":true},{\"text\":\"Earn 2400CP on the Control Points\\\\n\\\\n\\\\nCondition \",\"color\":\"reset\"},{\"text\":\"1.\",\"bold\":true},{\"text\":\" can only be claimed if condition \",\"color\":\"reset\"},{\"text\":\"2.\",\"bold\":true},{\"text\":\" has not been achieved yet.\",\"color\":\"reset\"}]','[\"\",{\"text\":\"The spoils:\",\"underlined\":true},{\"text\":\"\\\\nAs an appreciation of their skill, the winners of UltraHardCore will receive a role on Discord which elevates their spirits into divinity.\\\\n \",\"color\":\"reset\"}]','[\"\",{\"text\":\"Rules\",\"bold\":true,\"underlined\":true},{\"text\":\"\\\\nBanned items:\\\\n- Potion of regeneration,\\\\n- Potion of strength.\\\\n\\\\nA player cannot:\\\\n- trap a nether portal,\\\\n- share information that is not public after dying.\",\"color\":\"reset\"}]','[\"\",{\"text\":\"Other rules:\",\"underlined\":true},{\"text\":\"\\\\n- PvP is not allowed until the second day,\\\\n- Deaths due to PvE or glitches can be reversible, but respawns are handicapped,\\\\n- Players are not allowed to enter the spawn after the match has started.\",\"color\":\"reset\"}]','[\"\",{\"text\":\"Utility\",\"bold\":true,\"underlined\":true},{\"text\":\"\\\\n- Eternal day is enabled after 20 minutes,\\\\n- World size: 1500x1500\\\\n- A golden apple can be crafted like:\\\\n \\\\u0020 \\\\u0020 \\\\u0020 \\\\u0020 \\\\u0020\\\\u2610\\\\u2612\\\\u2610\\\\n \\\\u0020 \\\\u0020 \\\\u0020 \\\\u0020 \\\\u0020\\\\u2612\\\\u2611\\\\u2612\\\\n \\\\u0020 \\\\u0020 \\\\u0020 \\\\u0020 \\\\u0020\\\\u2610\\\\u2612\\\\u2610\\\\nwhere \\\\u2612 is a gold bar, \\\\u2611 is a playerhead.\",\"color\":\"reset\"}]','[\"\",{\"text\":\"Control Point\",\"bold\":true,\"underlined\":true},{\"text\":\"\\\\nThere are two Control Points in this game. When a player is within 5 blocks from the Control Point, they are awarded a CP score every second.\\\\n\\\\nCP1 awards 2CP per second and CP2 awards 3CP per second.\",\"color\":\"reset\"}]','[\"\",{\"text\":\"CP1 is enabled after 30 minutes of game time. After a team accumulates 720CP on CP1, CP2 will also be enabled.\\\\n\\\\nThe coordinates of both CP will be revealed after 30 minutes of game time.\\\\n\\\\n\"},{\"text\":\"Tip: When CP2 has just\",\"italic\":true}]','[\"\",{\"text\":\"been enabled it is faster to immediately control CP2!\",\"italic\":true},{\"text\":\"\\\\n\\\\nThe team that manages to first score 720CP receives a splash potion of resistance.\\\\n\\\\nControl Points are recognized by the beam of an end crystal.\",\"color\":\"reset\"}]','[\"\",{\"text\":\"When a team reaches 2400CP, they win the game. The remaining teams get 3 minutes to prepare and will afterwards be spread into a shrinking area for a final battle.\\\\n\\\\n\"},{\"text\":\"Note: It is not possible to capture a Control \",\"italic\":true}]','{\"text\":\"Point when players of other teams are present on that CP.\",\"italic\":true}','[\"\",{\"text\":\"Care Package\",\"bold\":true,\"underlined\":true},{\"text\":\"\\\\nThis game contains two Care Packages:\\\\n- \",\"color\":\"reset\"},{\"text\":\"Enchanting\",\"underlined\":true,\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://media.discordapp.net/attachments/505386630736248834/746784653922533506/unknown.png\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Click for preview of the contents\"}},{\"text\":\"\\\\n- \",\"color\":\"reset\"},{\"text\":\"Anti-ControlPoint\",\"underlined\":true,\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://media.discordapp.net/attachments/505386630736248834/746784937646096404/unknown.png\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Click for preview of the contents\"}},{\"text\":\"\\\\n\\\\nThe enchanting Care Package is deployed after 20 minutes of gametime. Its purpose is for those who are either unlucky or bored with caving. Its\",\"color\":\"reset\"}]','{\"text\":\"coordinates will be made available through a bossbar which shows up every 30 seconds.\\\\n\\\\nThe anti-ControlPoint Care Package is deployed after CP2 is enabled and contains all kind of havoc that makes CP capturers sweat. Its coordinates will be displayed in the\"}','[\"\",{\"text\":\"same aforementioned bossbar.\\\\n\\\\n\"},{\"text\":\"Tip: If you are not doing so well or late to the Control Point race, you might want to check these out!\",\"italic\":true}]','[\"\",{\"text\":\"Change log\",\"bold\":true,\"underlined\":true},{\"text\":\"\\\\n- Removed Traitor faction (will be tested in again in December!)\\\\n- CP bug fixed\\\\n- Team generation is now done through an algorithm made in MATLAB\",\"color\":\"reset\"}]','[\"\",{\"text\":\"Statistics\",\"bold\":true,\"underlined\":true},{\"text\":\"\\\\nFor more statistics check out \",\"color\":\"reset\"},{\"text\":\"/r/TheDioriteExpertsUHC\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://old.reddit.com/r/TheDioriteExpertsUHC/\"}},{\"text\":\"\\\\n\\\\n\"},{\"text\":\"Current Rankings:\",\"bold\":true},{\"text\":\"\\\\n1. Bobdafish (166)\\\\n2. Snodog627 (115)\\\\n3. TheDinoGame (113)\\\\n4. jonmo0105 (99)\\\\n5. Thurian (72)\\\\n\\\\n\\\\n\\\\n \",\"color\":\"reset\"}]','[\"\",{\"text\":\"Previous winners:\",\"bold\":true},{\"text\":\"\\\\n\",\"color\":\"reset\"},{\"text\":\"S33\",\"bold\":true},{\"text\":\": jonmo0105, PerfidyIsKey, Snodog627\\\\n\",\"color\":\"reset\"},{\"text\":\"S34\",\"bold\":true},{\"text\":\": jonmo0105\\\\n\",\"color\":\"reset\"},{\"text\":\"S35\",\"bold\":true},{\"text\":\": Kakarot057, PerfidyIsKey\\\\n\",\"color\":\"reset\"},{\"text\":\"S36\",\"bold\":true},{\"text\":\": Bobdafish, TheDinoGame, W0omy\\\\n\",\"color\":\"reset\"},{\"text\":\"S37:\",\"bold\":true},{\"text\":\" Bobdafish, TheDinoGame\\\\n\\\\n\\\\n\\\\n \",\"color\":\"reset\"}]','[\"\",{\"text\":\"Most Wins:\",\"bold\":true},{\"text\":\"\\\\n1. Snodog627 (18)\\\\n2. Mr9Madness (11)\\\\n3. PR0BA (5)\\\\n4. W0omy (5)\\\\n5. Thurian (4)\\\\n\\\\n\",\"color\":\"reset\"},{\"text\":\"Most Kills:\",\"bold\":true},{\"text\":\"\\\\n1. Snodog627 (40)\\\\n2. Mr9Madness (17)\\\\n3. TheDinoGame (9)\\\\n4. Tiba101 (8)\\\\n5. jonmo0105 (8)\\\\n\\\\n\\\\n\\\\n\\\\n \",\"color\":\"reset\"}]'],title:\"The Diorite Experts UHC RuleBook\",author:Snodog627,display:{Lore:[\"Information and rules on the 38th season of The Diorite Experts UltraHardCore\"]}}");
        fileCommands.add("give @p minecraft:compass");
        return new FileData(FileName.give_instructions, fileCommands);
    }

    private static FileData InstructionHandoutLoop() {
        ArrayList<String> fileCommands = new ArrayList<>();

        //fileCommands.add("execute as @a[scores={Quits=1..}] at @s run function uhc:give_instructions");
        fileCommands.add("scoreboard players set @a[scores={Quits=1..}] Quits 0");

        return new FileData(FileName.instruction_handout_loop, fileCommands);
    }

    private FileData TraitorHandout() {
        ArrayList<String> fileCommands = new ArrayList<>();
        //fileCommands.add("tag @r[limit=1,tag=!DontMakeTraitor] add Traitor");
        for (Player p : players) {
            if (p.getIgnoreTraitor()) {
                fileCommands.add("tag " + p.getPlayerName() + " add DontMakeTraitor");
            }
        }

        fileCommands.add("tag @r[limit=1,tag=!DontMakeTraitor,scores={Rank=" + minTraitorRank + "..},gamemode=!spectator] add Traitor");

        for (Team t : teams) {
            fileCommands.add(execute.If(new Selector("@p[tag=Traitor,team=" + t.getName() + "]")) +
                    "tag @a[team=" + t.getName() + "] add DontMakeTraitor");
        }
        //fileCommands.add("tag @r[limit=1,tag=!DontMakeTraitor] add Traitor");
        fileCommands.add("tag @r[limit=1,tag=!DontMakeTraitor,scores={Rank=" + minTraitorRank + "..},gamemode=!spectator] add Traitor");

        // Add additional traitor
        if (traitorMode == 2) {
            fileCommands.add("tag @a remove DontMakeTraitor");
            for (Player p : players) {
                if (p.getIgnoreTraitor()) {
                    fileCommands.add("tag " + p.getPlayerName() + " add DontMakeTraitor");
                }
            }
            fileCommands.add("tag @a[tag=Traitor] add DontMakeTraitor");
            fileCommands.add("tag @r[limit=1,tag=!DontMakeTraitor,gamemode=!spectator] add Traitor");
        }

        ArrayList<TextItem> texts = new ArrayList<>();
        texts.add(new Text(Color.red, false, true, "You feel like betrayal today. You have become a Traitor. Your faction consists of: "));
        texts.add(new Select(Color.red, false, true, "@a[tag=Traitor]"));
        texts.add(new Text(Color.red, false, true, "."));

        fileCommands.add(execute.As("@a[tag=Traitor]") +
                new TellRaw("@s", texts).sendRaw());

        fileCommands.add(new Title("@a", TitleType.title, new Text(Color.red, true, false, "A Traitor Faction")).displayTitle());
        fileCommands.add(new Title("@a", TitleType.subtitle, new Text(Color.dark_red, true, false, "has been founded!")).displayTitle());
        fileCommands.add(execute.In(Dimension.overworld) +
                setBlock(11, worldBottom + 2, 0, BlockType.redstone_block, SetBlockType.destroy));
        fileCommands.add(callFunction(FileName.traitor_check));
        return new FileData(FileName.traitor_handout, fileCommands);
    }

    private FileData TraitorActionBar() {
        ArrayList<String> fileCommands = new ArrayList<>();
        ArrayList<TextItem> texts = new ArrayList<>();
        texts.add(new Text(Color.gold, false, false, ">>> "));
        texts.add(new Text(Color.light_purple, false, false, "Traitor Faction: "));
        texts.add(new Select(false, false, "@a[tag=Traitor]"));
        texts.add(new Text(Color.gold, false, false, " <<<"));

        fileCommands.add(execute.As("@a[tag=Traitor]") +
                new Title("@s", TitleType.actionbar, texts).displayTitle());

        fileCommands.add(execute.If(new Selector("@p[scores={Admin=1,Victory=1}]")) +
                callFunction(FileName.traitor_check));

        return new FileData(FileName.traitor_actionbar, fileCommands);
    }

    private FileData TeamScoreLegacy() {
        ArrayList<String> fileCommands = new ArrayList<>();
        for (Team t : teams) {
            fileCommands.add(execute.As("@r[limit=1,gamemode=!spectator]") +
                    "scoreboard players operation @p[scores={Admin=1}] CP" + t.getName() + " > @s[team=" + t.getName() + "] ControlPoint");

            fileCommands.add(execute.As("@r[limit=1,gamemode=!spectator]") +
                    "scoreboard players operation @s[team=" + t.getName() + "] ControlPoint > @p[scores={Admin=1}] CP" + t.getName());
        }

        return new FileData(FileName.team_score, fileCommands);
    }

    private FileData TeamScore() {
        ArrayList<String> fileCommands = new ArrayList<>();
        for (int i = 1; i < controlPoints.size() + 1; i++) {
            for (Team t : teams) {
                fileCommands.add(execute.As("@r[limit=1,gamemode=!spectator]") +
                        "scoreboard players operation @p[scores={Admin=1}] CP" + i + t.getName() + " > @s[team=" + t.getName() + "] ControlPoint" + i);

                fileCommands.add(execute.As("@r[limit=1,gamemode=!spectator]") +
                        "scoreboard players operation @s[team=" + t.getName() + "] ControlPoint" + i + " > @p[scores={Admin=1}] CP" + i + t.getName());
            }
        }

        for (Team t : teams) {
            fileCommands.add(execute.In(controlPoints.get(0).getCoordinate().getDimension(), false) +
                    execute.AsNext("@r[limit=1,gamemode=!spectator,x=" + (controlPoints.get(0).getCoordinate().getX() - 6) + ",y=" + (controlPoints.get(0).getCoordinate().getY() - 1) + ",z=" + (controlPoints.get(0).getCoordinate().getZ() - 6) + ",dx=12,dy=12,dz=12,team=" + t.getName() + "]", true) +
                    "scoreboard players operation @p[scores={Admin=1}] CP1" + t.getName() + " > @p[scores={Admin=1}] CP2" + t.getName());

            fileCommands.add(execute.In(controlPoints.get(1).getCoordinate().getDimension(), false) +
                    execute.AsNext("@r[limit=1,gamemode=!spectator,x=" + (controlPoints.get(1).getCoordinate().getX() - 6) + ",y=" + (controlPoints.get(1).getCoordinate().getY() - 1) + ",z=" + (controlPoints.get(1).getCoordinate().getZ() - 6) + ",dx=12,dy=12,dz=12,team=" + t.getName() + "]", true) +
                    "scoreboard players operation @p[scores={Admin=1}] CP2" + t.getName() + " > @p[scores={Admin=1}] CP1" + t.getName());
        }
        fileCommands.add(callFunction(FileName.controlpoint_perks));

        return new FileData(FileName.team_score, fileCommands);
    }

    private FileData SpawnControlPoints() {
        ArrayList<String> fileCommands = new ArrayList<>();

        for (ControlPoint cp : cpList) {
            Coordinate c = cp.getCoordinate();
            fileCommands.add(execute.In(c.getDimension()) +
                    "forceload add " + c.getX() + " " + c.getZ() + " " + c.getX() + " " + c.getZ());
            fileCommands.add(execute.In(c.getDimension()) +
                    setBlock(c.getX(), c.getY() + 11, c.getZ(), "minecraft:structure_block[mode=load]{metadata:\"\",mirror:\"NONE\",ignoreEntities:1b,powered:0b,seed:0L,author:\"?\",rotation:\"NONE\",posX:-6,mode:\"LOAD\",posY:-13,sizeX:13,posZ:-6,integrity:1.0f,showair:0b,name:\"" + cp.getStructureName() + "\",sizeY:14,sizeZ:13,showboundingbox:1b}", SetBlockType.destroy));
            for (int i = c.getY() + 12; i < worldHeight; i++) {
                fileCommands.add(execute.In(c.getDimension(), false) +
                        execute.UnlessNext(new Block(c.getX(), i, c.getZ(), BlockType.air)) +
                        execute.UnlessNext(new Block(c.getX(), i, c.getZ(), BlockType.cave_air)) +
                        execute.UnlessNext(new Block(c.getX(), i, c.getZ(), BlockType.void_air)) +
                        execute.UnlessNext(new Block(c.getX(), i, c.getZ(), BlockType.bedrock), true) +
                        setBlock(c.getX(), i, c.getZ(), BlockType.glass));
            }
            fileCommands.add(execute.In(c.getDimension()) +
                    setBlock(c.getX(), c.getY() + 10, c.getZ(), BlockType.redstone_block, SetBlockType.destroy));
            fileCommands.add(execute.In(c.getDimension()) +
                    "forceload remove " + c.getX() + " " + c.getZ() + " " + c.getX() + " " + c.getZ());
        }

        return new FileData(FileName.spawn_controlpoints, fileCommands);
    }

    private FileData DisplayRank() {
        ArrayList<String> fileCommands = new ArrayList<>();

        for (Player p : players) {
            fileCommands.add("scoreboard players set " + p.getPlayerName() + " Rank " + p.getRank());
        }
        fileCommands.add(new ScoreboardObjective().setDisplay("sidebar", "Rank"));

        return new FileData(FileName.display_rank, fileCommands);
    }

    private FileData GiveStatusEffect(int i) {
        ArrayList<String> fileCommands = new ArrayList<>();

        for (ControlPoint cp : controlPoints) {
            Coordinate c = cp.getCoordinate();
            fileCommands.add(execute.In(c.getDimension(), false) +
                    execute.PositionedNext(c.getX(), c.getY(), c.getZ(), true) +
                    "effect give @p[gamemode=!spectator] minecraft:" + effects.get(i).getEffect() + " " + effects.get(i).getDuration() + " " + effects.get(i).getAmplification());
        }


        return new FileData("" + FileName.give_status_effect + i, fileCommands);
    }

    private FileData WorldPreload() {
        ArrayList<String> fileCommands = new ArrayList<>();
        fileCommands.add("scoreboard players add @a[gamemode=creative] WorldLoad 1");
        fileCommands.add("scoreboard players add @a[gamemode=creative] Time 1");
        fileCommands.add(execute.If(new Selector("@p[scores={WorldLoad=400..}]")) +
                "spreadplayers 0 0 5 750 false @a");
        fileCommands.add(execute.If(new Selector("@p[scores={Time=12000..}]"), false) +
                execute.InNext(Dimension.overworld, true) +
                setBlock(6, worldBottom + 2, 15, BlockType.bedrock));
        fileCommands.add(execute.If(new Selector("@p[scores={Time=12000..}]"), false) +
                execute.InNext(Dimension.overworld, true) +
                "tp @a[gamemode=creative] 0 221 0");
        fileCommands.add(execute.If(new Selector("@p[scores={Time=12000..}]")) +
                setGameRule(GameRule.commandBlockOutput, true));
        fileCommands.add(execute.If(new Selector("@p[scores={WorldLoad=400..}]")) +
                "scoreboard players reset @a WorldLoad");

        return new FileData(FileName.world_pre_load, fileCommands);
    }

    private FileData WorldPreLoadActivation() {
        ArrayList<String> fileCommands = new ArrayList<>();
        fileCommands.add(setGameRule(GameRule.commandBlockOutput, false));
        fileCommands.add(execute.In(Dimension.overworld) +
                setBlock(6, worldBottom + 2, 15, BlockType.redstone_block));
        fileCommands.add("scoreboard objectives setdisplay sidebar WorldLoad");

        return new FileData(FileName.world_pre_load_activation, fileCommands);
    }

    private FileData HorseFrostWalker() {
        ArrayList<String> fileCommands = new ArrayList<>();
        fileCommands.add(execute.At(new Location("@a[nbt={RootVehicle:{Entity:{id:\"minecraft:horse\"}}}]")) +
                relativeFill(-2, -2, -2, 2, 0, 2, "ice", SetBlockType.replace, "water"));

        return new FileData(FileName.horse_frost_walker, fileCommands);
    }

    private FileData WolfCollarExecute() {
        ArrayList<String> fileCommands = new ArrayList<>();
        fileCommands.add(execute.As("@e[type=minecraft:wolf]", false) +
                execute.StoreResultNext("score @s CollarCheck0 run data get entity @s Owner[0]"));
        fileCommands.add(execute.As("@e[type=minecraft:wolf]", false) +
                execute.StoreResultNext("score @s CollarCheck1 run data get entity @s Owner[1]"));
        for (Team t : teams) {
            fileCommands.add(execute.As("@a[team=" + t.getName() + "]", false) +
                    execute.StoreResultNext("score @s CollarCheck0 run data get entity @s UUID[0]"));
            fileCommands.add(execute.As("@a[team=" + t.getName() + "]", false) +
                    execute.StoreResultNext("score @s CollarCheck1 run data get entity @s UUID[1]"));

            fileCommands.add("tag @a[team=" + t.getName() + "] add CollarCheck");
            fileCommands.add(execute.As("@e[type=wolf]", false) +
                    execute.IfNext(new Score("@s", "CollarCheck0", "=", "@p[tag=CollarCheck] CollarCheck0")) +
                    execute.IfNext(new Score("@s", "CollarCheck1", "=", "@p[tag=CollarCheck] CollarCheck1"), true) +
                    "data modify entity @s CollarColor set value " + t.getCollarColor() + "b");
            fileCommands.add("tag @a[team=" + t.getName() + "] remove CollarCheck");
        }

        return new FileData(FileName.wolf_collar_execute, fileCommands);
    }

    private FileData UpdateSidebar() {
        ArrayList<String> fileCommands = new ArrayList<>();
        fileCommands.add("scoreboard players add @p[scores={Admin=1}] SideDum 1");
        int i = 0;
        for (ScoreboardObjective s : scoreboardObjectives) {
            if (s.getDisplaySideBar()) {
                i++;
                fileCommands.add(execute.If(new Selector("@p[scores={SideDum=" + (5 * tickPerSecond * i) + "}]")) +
                        s.setDisplay("sidebar"));
            }
        }
        fileCommands.add(execute.If(new Selector("@p[scores={SideDum=" + (5 * tickPerSecond * i + 1) + "}]")) +
                "scoreboard players reset @p[scores={Admin=1}] SideDum");

        // Update stripmine count
        fileCommands.add("scoreboard players set @a[scores={Mining=1..}] Mining 0");
        fileCommands.add(execute.As("@a") +
                callFunction(FileName.update_mine_count));

        // Update minimum health
        fileCommands.add(callFunction(FileName.update_min_health));

        return new FileData(FileName.update_sidebar, fileCommands);
    }

    private FileData Timer() {
        ArrayList<String> fileCommands = new ArrayList<>();
        fileCommands.add("scoreboard players add @p[scores={Admin=1}] Time2 1");
        fileCommands.add("scoreboard players add @p[scores={Admin=1}] TimDum 1");
        fileCommands.add(execute.If(new Selector("@p[scores={TimDum=" + tickPerSecond + "}]")) +
                "scoreboard players add @p[scores={Admin=1}] TimeDum 1");
        fileCommands.add(execute.StoreResult("score CurrentTime Time run scoreboard players get @p[scores={Admin=1}] TimeDum"));
        fileCommands.add(execute.If(new Selector("@p[scores={TimDum=" + tickPerSecond + "..}]")) +
                "scoreboard players reset @p[scores={Admin=1}] TimDum");


        fileCommands.add(execute.If(new Selector("@p[scores={Time2=" + (300 * tickPerSecond) + "}]")) +
                new TellRaw("@a", new Text(Color.gray, false, false, "PVP IS NOT ALLOWED UNTIL DAY 2!")).sendRaw());

        ArrayList<TextItem> texts = new ArrayList<>();
        texts.add(bannerText);
        texts.add(new Text(Color.gold, true, false, communityName + " UHC"));
        texts.add(bannerText);
        texts.add(new Text(Color.light_purple, true, false, "DAY TIME HAS ARRIVED & ETERNAL DAY ENABLED!"));
        texts.add(bannerText);

        fileCommands.add(execute.If(new Selector("@p[scores={Time2=" + (1200 * tickPerSecond) + "}]")) +
                new TellRaw("@a", texts).sendRaw());
        fileCommands.add(callFunction(FileName.display_quotes));
        fileCommands.add(callFunction(FileName.locate_teammate));

        return new FileData(FileName.timer, fileCommands);
    }

    // Sad function for Control Point spawning
    private FileData CreateControlpointRedstone() {
        ArrayList<String> fileCommands = new ArrayList<>();
        for (ControlPoint cp : controlPoints) {
            Coordinate c = cp.getCoordinate();
            fileCommands.add(execute.In(c.getDimension()) +
                    setBlock(c.getX(), c.getY() + 10, c.getZ(), BlockType.redstone_block, SetBlockType.destroy));
        }

        return new FileData(FileName.controlpoint_redstone, fileCommands);
    }

    // Perks for being on the Control Point
    private FileData ControlPointPerks() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Activation period
        int actPeriod = 50;

        // Define perk activation times
        int minToCPScore = secPerMinute * tickPerSecond * controlPoints.get(0).getAddRate();
        ArrayList<Perk> perks = new ArrayList<>();
        perks.add(new Perk(1, "minecraft:" + Effect.speed + " 999999 0 false", "effect give", Sound.BASALT, 3 * minToCPScore));
        perks.add(new Perk(2, "minecraft:" + Effect.resistance + " " + (10 * secPerMinute) + " 0 false", "effect give", Sound.CRIMSON, 6 * minToCPScore));
        perks.add(new Perk(3, "minecraft:" + Effect.haste + " 999999 2 false", "effect give", Sound.WARPED, 12 * minToCPScore));
        perks.add(new Perk(4, "minecraft:golden_apple", "give", Sound.WITHER, 15 * minToCPScore));

        for (int i = 0; i < controlPoints.size(); i++) {
            for (Team team : teams) {
                // Display team receiving perk
                for (Perk perk : perks) {
                    ArrayList<TextItem> texts = new ArrayList<>();
                    texts.add(new Text(Color.light_purple, false, false, "TEAM "));
                    texts.add(new Text(team.getColor(), false, false, team.getJSONColor()));
                    texts.add(new Text(Color.light_purple, false, false, " HAS REACHED"));
                    texts.add(new Text(Color.gold, false, false, " PERK " + perk.getId() + "!"));

                    fileCommands.add(execute.If(new Selector("@p[scores={CP" + (i + 1) + team.getName() + "=" + perk.getActivationTime() + ".." + (perk.getActivationTime() + actPeriod) + "}]"), false) +
                            execute.IfNext(new Selector("@p[team=" + team.getName() + ",tag=!ReceivedPerk" + perk.getId() + "]"), true) +
                            new TellRaw("@a", texts).sendRaw());

                    // give rewards
                    fileCommands.add(execute.If(new Selector("@p[scores={CP" + (i + 1) + team.getName() + "=" + perk.getActivationTime() + ".." + (perk.getActivationTime() + actPeriod) + "}]")) +
                            perk.getRewardType() + " @a[team=" + team.getName() + ",tag=!ReceivedPerk" + perk.getId() + "] " + perk.getReward());
                    fileCommands.add(execute.If(new Selector("@p[scores={CP" + (i + 1) + team.getName() + "=" + perk.getActivationTime() + ".." + (perk.getActivationTime() + actPeriod) + "}]"), false) +
                            execute.IfNext(new Selector("@p[team=" + team.getName() + ",tag=!ReceivedPerk" + perk.getId() + "]"), true) +
                            playSound(perk.getSound(), SoundSource.master, "@a", "~", "~50", "~", "100", "1", "0"));
                    fileCommands.add(execute.If(new Selector("@p[scores={CP" + (i + 1) + team.getName() + "=" + perk.getActivationTime() + ".." + (perk.getActivationTime() + actPeriod) + "}]")) +
                            "tag @a[team=" + team.getName() + "] add ReceivedPerk" + perk.getId());
                }
            }
        }

        return new FileData(FileName.controlpoint_perks, fileCommands);

    }

    // Display quotes during the match
    private FileData DisplayQuotes() {
        ArrayList<String> fileCommands = new ArrayList<>();

        for (int i = 0; i < 36; i++) {
            int index = (int) (Math.random() * quotes.size());
            fileCommands.add(execute.If(new Selector("@p[scores={Time2=" + (7 * secPerMinute * tickPerSecond * (i + 1)) + "}]")) +
                    new TellRaw("@a", new Text(Color.white, false, false, quotes.get(index))).sendRaw());
            quotes.remove(index);
        }

        return new FileData(FileName.display_quotes, fileCommands);
    }

    // Update amount stripmined
    private FileData UpdateMineCount() {
        ArrayList<String> fileCommands = new ArrayList<>();

        ArrayList<String> blocks = new ArrayList<>();
        blocks.add("Stone");
        blocks.add("Deepslate");
        blocks.add("Diorite");
        blocks.add("Andesite");
        blocks.add("Granite");

        for (String block : blocks) {
            fileCommands.add("scoreboard players operation @s Mining += @s " + block);
        }

        return new FileData(FileName.update_mine_count, fileCommands);
    }

    // Update minimum health
    private FileData UpdateMinHealth() {
        ArrayList<String> fileCommands = new ArrayList<>();

        fileCommands.add("execute as @r[gamemode=!spectator] if score @s Hearts < @p[scores={Admin=1}] MinHealth store" +
                " result score @p[scores={Admin=1}] MinHealth run scoreboard players get @s Hearts");

        return new FileData(FileName.update_min_health, fileCommands);
    }

    // Reset health of respawned players
    private FileData ResetRespawnHealth() {
        ArrayList<String> fileCommands = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            int indexFront = 2 * i + 1;
            int indexRear = 2 * (i + 1);

            fileCommands.add(execute.If(new Selector("@p[scores={MinHealth=" + indexFront + ".." + indexRear + "}]")) +
                    "attribute @p[tag=Respawn] generic.max_health base set " + (i + 1));
        }
        fileCommands.add("effect give @p[tag=Respawn] minecraft:" + Effect.health_boost + " 1 0");
        fileCommands.add("effect clear @p[tag=Respawn] minecraft:" + Effect.health_boost);
        fileCommands.add("attribute @p[tag=Respawn] generic.max_health base set 20");
        fileCommands.add("tag @p[tag=Respawn] remove Respawn");

        return new FileData(FileName.reset_respawn_health, fileCommands);
    }

    private FileData ControlPointCaptured() {
        ArrayList<String> fileCommands = new ArrayList<>();
        fileCommands.add(execute.In(Dimension.overworld) +
                setBlock(8, worldBottom + 2, 0, BlockType.bedrock, SetBlockType.replace));

        ArrayList<TextItem> texts = new ArrayList<>();
        texts.add(bannerText);
        texts.add(new Text(Color.gold, true, false, communityName + " UHC"));
        texts.add(bannerText);
        texts.add(new Text(Color.light_purple, true, false, "The Controlpoint has been captured!"));
        texts.add(bannerText);

        fileCommands.add(new TellRaw("@a", texts).sendRaw());
        fileCommands.add(execute.In(Dimension.overworld) +
                fill(15, worldBottom + 2, 3, 15, worldBottom + 2, 4, BlockType.bedrock));

        fileCommands.add(new Title("@a", TitleType.subtitle, new Text(Color.light_purple, true, true, "has been captured!")).displayTitle());
        fileCommands.add(new Title("@a", TitleType.title, new Text(Color.gold, true, true, "The Controlpoint")).displayTitle());

        fileCommands.add(callFunction(FileName.teams_highscore_alive_check));

        return new FileData(FileName.control_point_captured, fileCommands);
    }

    private FileData TraitorCheck() {
        ArrayList<String> fileCommands = new ArrayList<>();

        //When no traitors remain start teams_alive_check
        fileCommands.add(execute.Unless(new Selector("@a[limit=1,tag=Traitor,gamemode=!spectator]")) +
                callFunction(FileName.teams_alive_check));

        fileCommands.add(execute.Unless(new Selector("@a[limit=1,tag=!Traitor,gamemode=!spectator]")) +
                callFunction(FileName.victory_message_traitor));

        return new FileData(FileName.traitor_check, fileCommands);
    }

    private FileData TeamsAliveCheck() {
        ArrayList<String> fileCommands = new ArrayList<>();
        for (int i = 0; i < teams.size(); i++) {
            fileCommands.add(execute.Unless(new Selector("@a[limit=1,team=!" + teams.get(i).getName() + ",gamemode=!spectator]")) +
                    callFunction("" + FileName.victory_message_ + i));
        }
        return new FileData(FileName.teams_alive_check, fileCommands);
    }

    private FileData TeamsHighscoreCheck() {
        ArrayList<String> fileCommands = new ArrayList<>();
        for (int i = 0; i < teams.size(); i++) {
            for (int j = 1; j < 3; j++) {
                fileCommands.add(execute.If(new Selector("@a[limit=1,scores={Admin=1,Victory=1}]"), false) +
                        execute.IfNext(new Selector("@p[team=" + teams.get(i).getName() + ",gamemode=!spectator,scores={ControlPoint" + j + "=" + (maxCPScore * tickPerSecond) + "..},tag=!Traitor]"), true) +
                        callFunction("" + FileName.victory_message_ + i));
                fileCommands.add(execute.If(new Selector("@a[limit=1,scores={Admin=1,Victory=1}]"), false) +
                        execute.IfNext(new Selector("@p[team=" + teams.get(i).getName() + ",gamemode=!spectator,scores={ControlPoint" + j + "=" + (maxCPScore * tickPerSecond) + "..},tag=Traitor]")) +
                        execute.UnlessNext(new Selector("@p[team=" + teams.get(i).getName() + ",gamemode=!spectator,scores={ControlPoint" + j + "=" + (maxCPScore * tickPerSecond) + "..},tag=!Traitor]"), true) +
                        callFunction(FileName.victory_message_traitor));
            }
        }
        return new FileData(FileName.teams_highscore_alive_check, fileCommands);
    }

    private FileData SpawnNetherPortal() {
        ArrayList<String> fileCommands = new ArrayList<>();
        fileCommands.add(execute.In(Dimension.overworld) +
                "forceload add " + netherPortal.getX() + " " + netherPortal.getZ() + " " + netherPortal.getX() + " " + netherPortal.getZ());
        fileCommands.add(execute.In(Dimension.overworld) +
                setBlock(netherPortal, "minecraft:structure_block[mode=load]{author:\"?\",ignoreEntities:1b,integrity:1.0f,metadata:\"\",mirror:\"NONE\",mode:\"LOAD\",name:\"minecraft:nether_portal\",posX:-1,posY:0,posZ:0,powered:0b,rotation:\"NONE\",seed:0L,showair:0b,showboundingbox:1b,sizeX:4,sizeY:5,sizeZ:1}"));
        fileCommands.add(execute.In(Dimension.overworld) +
                setBlock(netherPortal.getX() - 1, netherPortal.getY(), netherPortal.getZ(), BlockType.redstone_block));
        fileCommands.add("give @a[gamemode=!spectator] minecraft:compass{display:{Name:\"{\\\"text\\\":\\\"Nether Portal -  located at " + netherPortal.getX() + ", " + netherPortal.getY() + ", " + netherPortal.getZ() + "\\\"}\"}, LodestoneDimension:\"minecraft:" + Dimension.overworld + "\",LodestoneTracked:0b,LodestonePos:{X:" + netherPortal.getX() + ",Y:" + netherPortal.getY() + ",Z:" + netherPortal.getZ() + "}}");
        fileCommands.add(execute.In(Dimension.overworld) +
                "forceload remove " + netherPortal.getX() + " " + netherPortal.getZ() + " " + netherPortal.getX() + " " + netherPortal.getZ());

        return new FileData(FileName.spawn_nether_portal, fileCommands);
    }

    private FileData ClearSchedule() {
        ArrayList<String> fileCommands = new ArrayList<>();
        fileCommands.add(clearFunction(FileName.minute_ + "2"));
        fileCommands.add(clearFunction(FileName.minute_ + "1"));
        fileCommands.add(clearFunction(FileName.death_match));

        return new FileData(FileName.clear_schedule, fileCommands);
    }

    private FileData LocateTeammate() {
        ArrayList<String> fileCommands = new ArrayList<>();
        for (Team team : teams) {
            for (int i = 0; i < 3; i++) {
                fileCommands.add(execute.As("@a[team=" + team.getName() + ",nbt={SelectedItem:{id:\"minecraft:bundle\",tag:{tag:LocateTeammate}}}]", false) +
                        execute.AtNext("@s") +
                        execute.IfNext(new Selector("@a[team=" + team.getName() + ",distance=0.1..,gamemode=!spectator]")) +
                        execute.FacingNext(new Selector("@a[team=" + team.getName() + ",distance=0.1..,gamemode=!spectator,limit=1,sort=nearest]"), EntityAnchor.eyes) +
                        execute.PositionedRelativeNext(0, 1, 0) +
                        execute.PositionedRelativeFacingNext(0, 0, i + 1, true) +
                        "particle minecraft:dust 1.0 1.0 1.0 1.0 ~ ~ ~ 0 0 0 0 1 normal @s");
            }
        }

        return new FileData(FileName.locate_teammate, fileCommands);
    }

    private FileData UnleashLava() {
        ArrayList<String> fileCommands = new ArrayList<>();
        fileCommands.add(setBlockRelative(0, -1, 0, BlockType.lava));

        return new FileData(FileName.unleash_lava, fileCommands);
    }

}
