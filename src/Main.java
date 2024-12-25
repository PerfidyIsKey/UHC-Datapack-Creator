import Enums.*;
import FileGeneration.*;
import HelperClasses.*;
import TeamGeneration.Season;
import TeamGeneration.TeamGenerator;
import org.w3c.dom.Attr;


import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

import static java.lang.Integer.parseInt;

public class Main {


    //TODO: Automate process using args.
    public static void main(String[] args) {
        new Main().run(args);
    }

    //packages
    FileTools fileTools;

    //DatapackData<

    private String uhcNumber;
    private static final String version = "4.0";
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
    private static final String commandCenter = "s56";
    private String admin;
    private Coordinate startCoordinate;
    private Coordinate netherPortal;
    private ArrayList<Team> teams = new ArrayList<>();
    private ArrayList<ControlPoint> cpList = new ArrayList<>();
    private ArrayList<ControlPoint> controlPoints = new ArrayList<>();
    //private ArrayList<CarePackage> carePackages = new ArrayList<>();
    private ArrayList<ScoreboardObjective> scoreboardObjectives = new ArrayList<>();
    private ArrayList<Player> players = new ArrayList<>();
    private int teamAmount;
    private ArrayList<Season> seasons = new ArrayList<>();
    private ArrayList<StatusEffect> effects = new ArrayList<>();
    private ArrayList<String> quotes = new ArrayList<>();
    private ArrayList<BossBar> bossBars = new ArrayList<>();
    private static int worldSize;  // Maximum possible coordinate
    private static final int worldHeight = 257;
    private static final int worldBottom = -64;
    private static final int tickPerSecond = 20;
    private static final int secPerMinute = 60;
    private static final int maxCPScore = 2400;
    private static final int maxCPScoreBossbar = 20 * secPerMinute * tickPerSecond * 2;
    private static final int cpMessageThreshold = 5 * tickPerSecond;
    private static int carePackageAmount;
    private static int carePackageSpread;
    private int minTraitorRank;
    private int traitorWaitTime;
    private static final int traitorMode = 1;
    private String communityName;
    private static final Execute execute = new Execute();

    private final Text bannerText = new Text(Color.dark_gray, true, false, " | ");

    private TeamGenerator teamGenerator;

    //GameData>


    private ArrayList<FileData> files = new ArrayList<>();


    private void run(String[] args) {

        gameModeChange();
        createDatapack();
        System.out.println("Datapack created");
        if (args.length == 0) {
            boolean menuRunning = true;
            Scanner scanner = new Scanner(System.in);
            String input;
            while (menuRunning) {
                System.out.println("-----------------\n");
                System.out.println("Gamemode: " + gameMode);
                System.out.println("Options:\n");
                System.out.println("Change Gamemode (c[n])");
                System.out.println("Re-run (r)");
                System.out.println("Generate teams (t)");
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
                    System.out.println("Datapack created");
                } else if (input.equals("r")) {
                    gameModeChange();
                    createDatapack();
                    System.out.println("Datapack created");
                } else if (input.equals("t")) {
                    teamGenerator = new TeamGenerator(Double.parseDouble(uhcNumber), fileLocation, teams, gameMode);
                    teamGenerator.run();
                } else {
                    System.out.println("Input not recognized.");
                }
            }
        } else {
            teamGenerator = new TeamGenerator(Double.parseDouble(uhcNumber), fileLocation, teams, gameMode);
            teamGenerator.run(args);
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
        seasons = new ArrayList<>();
        quotes = new ArrayList<>();

        Color[] colors = {Color.yellow, Color.blue, Color.red, Color.dark_purple, Color.dark_green, Color.light_purple, Color.black, Color.gold, Color.gray, Color.aqua, Color.dark_red, Color.dark_blue, Color.dark_aqua};
        BossBarColor[] bossbarColors = {BossBarColor.yellow, BossBarColor.blue, BossBarColor.red, BossBarColor.purple, BossBarColor.green, BossBarColor.pink, BossBarColor.white, BossBarColor.white, BossBarColor.white, BossBarColor.white, BossBarColor.white, BossBarColor.white, BossBarColor.white};
        String[] glassColors = {"yellow", "light_blue", "red", "purple", "green", "pink", "black", "orange", "gray", "cyan", "red", "blue", "blue"};
        String[] collarColors = {"4", "3", "14", "10", "13", "6", "15", "1", "7", "9", "2", "11", "9"};
        String[] jsonColors = {"YELLOW", "BLUE", "RED", "PURPLE", "GREEN", "PINK", "BLACK", "ORANGE", "GRAY", "AQUA", "DARK RED", "DARK BLUE", "DARK AQUA"};
        String[] playerColors = {"Yellow", "Blue", "Red", "Purple", "Green", "Pink", "Black", "Orange", "Gray", "Aqua", "DarkRed", "DarkBlue", "DarkAqua"};
        String[] dustColors = {"1.0,1.0,0.3", "0.3,0.3,1.0", "1.0,0.3,0.3", "0.7,0.0,0.7", "0.3,1.0,0.3", "1.0,0.3,1.0", "0.0,0.0,0.0", "1.0,0.7,0.0", "0.7,0.7,0.7", "0.3,1.0,1.0", "0.7,0.0,0.0", "0.0,0.0,0.7", "0.0,0.7,0.7"};
        for (int i = 0; i < colors.length; i++) {
            Team team = new Team("Team" + i, colors[i], bossbarColors[i], glassColors[i], collarColors[i], jsonColors[i], playerColors[i], dustColors[i]);
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
        traitorWaitTime = Integer.parseInt(fileTools.getContentOutOfFile("Files\\" + gameMode + "\\uhc_data.txt", "traitorWaitTime"));
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
            boolean isPlaying = Boolean.parseBoolean(playerSplit[4]);
            if (isPlaying) {
                players.add(new Player(Integer.parseInt(playerSplit[0]), playerSplit[1], Integer.parseInt(playerSplit[2]), Double.parseDouble(playerSplit[3]), isPlaying));
            }
        }

        // Teams
        teamAmount = players.size() / determineAmountOfPlayersPerTeam();
        int relativeTeamAmount = teamAmount - 6;
        if (relativeTeamAmount < 0) relativeTeamAmount = 0;
        if (relativeTeamAmount > 10) relativeTeamAmount = 10;

        // Desired chest density
        double desiredChestDensity = 0.0002;

        int baseWorldSize = 750;
        int baseCarePackageSpread = 500;

        int maxTeamAmount = 10;
        int maxWorldSize = 1500;
        int maxCarePackageSpread = 800;

        int worldSizeStep = (maxWorldSize - baseWorldSize) / maxTeamAmount;
        int carePackageSpreadStep = (maxCarePackageSpread - baseCarePackageSpread) / maxTeamAmount;

        worldSize = baseWorldSize + worldSizeStep * relativeTeamAmount;
        carePackageSpread = baseCarePackageSpread + carePackageSpreadStep * relativeTeamAmount;

        carePackageAmount = (int) (desiredChestDensity * (carePackageSpread * 2) * (carePackageSpread * 2));

        // Seasons
        ArrayList<String> seasonsString = fileTools.GetLinesFromFile("Files\\" + gameMode + "\\seasonData.txt");
        for (String season : seasonsString) {
            String[] seasonSplit = fileTools.splitLineOnComma(season);
            seasons.add(new Season(Double.parseDouble(seasonSplit[0]), Integer.parseInt(seasonSplit[1]), new Date(Integer.parseInt(seasonSplit[2]), Integer.parseInt(seasonSplit[3]), Integer.parseInt(seasonSplit[4]))));
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
        scoreboardObjectives.add(new ScoreboardObjective("CPScore", "dummy", "\"Control Point score\"", true));
        scoreboardObjectives.add(new ScoreboardObjective("MSGDum1CP1", "dummy"));
        scoreboardObjectives.add(new ScoreboardObjective("MSGDum2CP1", "dummy"));
        scoreboardObjectives.add(new ScoreboardObjective("MSGDum1CP2", "dummy"));
        scoreboardObjectives.add(new ScoreboardObjective("MSGDum2CP2", "dummy"));
        scoreboardObjectives.add(new ScoreboardObjective("Highscore1", "dummy"));
        scoreboardObjectives.add(new ScoreboardObjective("Highscore2", "dummy"));
        scoreboardObjectives.add(new ScoreboardObjective("Hearts", "health"));
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
        scoreboardObjectives.add(new ScoreboardObjective("WolfAge", "dummy"));

        // Status effects
        effects.add(new StatusEffect(Effect.glowing, 30, 1));
        effects.add(new StatusEffect(Effect.fire_resistance, 20, 1));
        effects.add(new StatusEffect(Effect.nausea, 10, 1));
        effects.add(new StatusEffect(Effect.speed, 20, 1));
    }

    private int determineAmountOfPlayersPerTeam() {
        // Define variable
        int playerAmount;

        int totalPlayers = players.size();
        if ((totalPlayers % 3) == 0) playerAmount = 3;
        else if ((totalPlayers % 2) == 0) playerAmount = 2;
        else if (totalPlayers % 3 == 1) playerAmount = 3;
        else playerAmount = 2;

        return playerAmount;
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
        lootEntry.add(new LootTableEntry(3, "ender_pearl", new LootTableFunction(2, 0.5)));
        lootEntry.add(new LootTableEntry(2, "nether_wart", new LootTableFunction(5)));
        lootEntry.add(new LootTableEntry(2, "blaze_rod", new LootTableFunction(2, 0.1)));
        lootEntry.add(new LootTableEntry(2, "golden_apple"));
        lootEntry.add(new LootTableEntry(2, "anvil"));
        lootEntry.add(new LootTableEntry(2, "spyglass"));
        lootEntry.add(new LootTableEntry(2, "wolf_spawn_egg", new LootTableFunction(2, 0.01)));
        lootEntry.add(new LootTableEntry(1, "diamond_horse_armor"));
        lootEntry.add(new LootTableEntry(1, "netherite_hoe"));
        lootEntry.add(new LootTableEntry(1, "trident"));
        lootEntry.add(new LootTableEntry(1, "netherite_upgrade_smithing_template"));
        lootEntry.add(new LootTableEntry(1, "netherite_scrap", new LootTableFunction(4, 0.001)));

        ArrayList<String> fileCommands = fileTools.generateLootTable(lootEntry);

        FileData fileData = new FileData("supply_drop", fileCommands, "loot_table");
        files.add(fileData);
    }

    // Get by name functions
    private BossBar getBossbarByName(String name) {
        return bossBars.stream().filter(bossBar -> name.equals(bossBar.getName())).findAny().orElse(null);
    }

    private ScoreboardObjective getObjectiveByName(String name) {
        return scoreboardObjectives.stream().filter(objective -> name.equals(objective.getName())).findAny().orElse(null);
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

    private String setAttributeBase(String entity, AttributeType attribute, double value) {
        return "attribute " + entity + " minecraft:" + attribute + " base set " + value;
    }

    private String giveEffect(String entity, Effect effect, int duration, int amplifier) {
        return giveEffect(entity, effect, duration, amplifier, false);
    }

    private String giveEffect(String entity, Effect effect, int duration, int amplifier, Boolean hideParticles) {
        return "effect give " + entity + " minecraft:" + effect + " " + duration + " " + amplifier + " " + hideParticles;
    }

    private String clearEffect(String entity, Effect effect) {
        return "effect clear " + entity + " minecraft:" + effect;
    }

    private String clearEffect(String entity) {
        return "effect clear " + entity;
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
            files.add(ControlPointMessages(i));
        }

        files.add(DropCarepackages());
        files.add(CarepackageDistributor());
        files.add(TraitorHandout());
        files.add(TraitorActionBar());
        files.add(TeamScore());

        files.add(SpawnControlPoints());
        files.add(DisplayRank());

        files.add(WorldPreload());
        files.add(WorldPreLoadActivation());
        files.add(HorseFrostWalker());
        files.add(WolfCollarExecute());
        files.add(UpdateSidebar());
        files.add(Timer());
        files.add(ControlPointPerks());
        files.add(DisplayQuotes());
        files.add(UpdateMineCount());
        files.add(RespawnPlayer());
        files.add(UpdateMinHealth());
        files.add(ClearSchedule());
        files.add(LocateTeammate());
        files.add(EliminateBabyWolf());
        files.add(UpdatePublicCPScore());
        files.add(DisableRespawn());
        files.add(PlayerDeathHandler());
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
        fileCommands.add(new ScoreboardObjective().setDisplay("below_name", "Hearts"));
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
                scoreboardObjectives.add(new ScoreboardObjective("CP" + i + t.getName(), "dummy"));
                fileCommands.add(scoreboardObjectives.get(scoreboardObjectives.size() - 1).add());
            }
        }
        //end teams
        //structure
        // Staging area
        fileCommands.add(execute.In(Dimension.overworld) +
                fill(-6, 220, -6, 6, 226, 6, BlockType.barrier));
        fileCommands.add(execute.In(Dimension.overworld) +
                fill(-5, 221, -5, 5, 226, 5, BlockType.air));
        fileCommands.add(execute.In(Dimension.overworld) +
                setBlock(0, 222, -5, "cherry_wall_sign[facing=south,waterlogged=false]{back_text:{messages:['{\"text\":\"You have\"}','{\"text\":\"angered\"}','{\"text\":\"the Gods!\"}','{\"text\":\"\"}']},front_text:{messages:['{\"text\":\"Teleport\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"tp @s 5 " + (worldBottom + 5) + " 5\"}}','{\"text\":\"to the\"}','{\"text\":\"Command center\"}','{\"text\":\"\"}']},is_waxed:0b}"));

        // Command center
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

    private FileData PlayerDeathHandler() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Indicate when first blood has been taken
        fileCommands.add(execute.If("@p[scores={Time2=24000..}]", false) +
                execute.UnlessNext("@p[tag=" + Tag.RespawnDisabled + "]", true) +
                callFunction(FileName.disable_respawn));

        // Play thunder sound
        fileCommands.add(playSound(Sound.THUNDER, SoundSource.master, "@a", "~", "~50", "~", "100", "1", "0"));

        // Set all dead players to spectator mode
        fileCommands.add("gamemode spectator @a[scores={Deaths=1},gamemode=!spectator]");

        // Reset scores
        fileCommands.add("scoreboard players set @a[scores={Deaths=1}] ControlPoint1 0");
        fileCommands.add("scoreboard players set @a[scores={Deaths=1}] ControlPoint2 0");
        fileCommands.add("scoreboard players set @p[scores={Admin=1}] Highscore1 1");
        fileCommands.add("scoreboard players set @p[scores={Admin=1}] Highscore2 1");

        // Reset player with lowest health
        fileCommands.add("scoreboard players set @p[scores={Admin=1}] MinHealth 20");

        // Announce traitor deaths
        ArrayList<TextItem> texts = new ArrayList<>();
        texts.add(bannerText);
        texts.add(new Text(Color.red, true, false, "A TRAITOR HAS BEEN ELIMINATED"));
        texts.add(bannerText);
        texts.add(new Text(Color.gold, true, false, "WELL DONE"));
        texts.add(bannerText);

        fileCommands.add(execute.If(new Entity("@p[scores={Deaths=1},tag=Traitor]")) +
                new TellRaw("@a", texts).sendRaw());

        // Add respawn tag to players who die before first blood
        fileCommands.add(execute.Unless("@p[tag=" + Tag.RespawnDisabled + "]") +
                "tag @p[scores={Deaths=1}] add " + Tag.Respawn);

        // Drop player head
        fileCommands.add(callFunction(FileName.drop_player_heads));

        // Reset death count
        fileCommands.add("scoreboard players reset @p[scores={Deaths=1}] Deaths");

        // Do automatic respawn before first blood
        fileCommands.add(execute.Unless("@p[tag=" + Tag.RespawnDisabled + "]") +
                callFunction(FileName.respawn_player, 1));

        return new FileData(FileName.handle_player_death, fileCommands);
    }

    private FileData DropPlayerHeads() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Summon a player head upon dying
        for (Player p : players) {
            fileCommands.add(execute.At(new Entity("@p[name=" + p.getPlayerName() + ",scores={Deaths=1}]")) +
                    "summon minecraft:item ~ ~ ~ {Item:{id:\"minecraft:player_head\",count:1,components:{\"minecraft:profile\":{name:" + p.getPlayerName() + "}}}}");
        }

        return new FileData(FileName.drop_player_heads, fileCommands);
    }

    private FileData BossBarValue() {
        ArrayList<String> fileCommands = new ArrayList<>();

        for (Team t : teams) {
            fileCommands.add(execute.If("@p[scores={Admin=1}]", getObjectiveByName("CP1" + t.getName()), ComparatorType.greater,"@p[scores={Admin=1}]", getObjectiveByName("Highscore1")) +
                    getBossbarByName("cp1").setColor(t.getBossbarColor()));
            fileCommands.add(execute.If("@p[scores={Admin=1}]", getObjectiveByName("CP2" + t.getName()), ComparatorType.greater, "@p[scores={Admin=1,Highscore1=14400..}]", getObjectiveByName("Highscore2")) +
                    getBossbarByName("cp2").setColor(t.getBossbarColor()));
            fileCommands.add("scoreboard players operation @p[scores={Admin=1}] Highscore1 > @p[scores={Admin=1}] CP1" + t.getName());
            fileCommands.add("scoreboard players operation @p[scores={Admin=1}] Highscore2 > @p[scores={Admin=1}] CP2" + t.getName());
        }

        // Update value of bossbars
        fileCommands.add(execute.Store(ExecuteStore.result, getBossbarByName("cp1"), BossBarStore.value) +
                "scoreboard players get @p[scores={Admin=1}] Highscore1");
        fileCommands.add(execute.Store(ExecuteStore.result, getBossbarByName("cp2"), BossBarStore.value) +
                "scoreboard players get @p[scores={Admin=1,Highscore1=14400..}] Highscore2");

        return new FileData(FileName.bbvalue, fileCommands);
    }

    private FileData ClearEnderChest() {
        ArrayList<String> fileCommands = new ArrayList<>();
        for (int i = 0; i < chestSize; i++) {
            fileCommands.add("item replace entity @a enderchest." + i + " with air 1");
        }

        return new FileData(FileName.clear_enderchest, fileCommands);
    }

    private FileData EquipGear() {
        ArrayList<String> fileCommands = new ArrayList<>();
        fileCommands.add("item replace entity @a armor.chest with minecraft:iron_chestplate");
        fileCommands.add("item replace entity @a armor.feet with minecraft:iron_boots");
        fileCommands.add("item replace entity @a armor.head with minecraft:iron_helmet");
        fileCommands.add("item replace entity @a armor.legs with minecraft:iron_leggings");
        fileCommands.add("item replace entity @a weapon.offhand with minecraft:shield");
        fileCommands.add("item replace entity @a weapon.mainhand with minecraft:iron_axe");
        fileCommands.add("item replace entity @a inventory.0 with minecraft:iron_sword");
        fileCommands.add(giveEffect("@a", Effect.regeneration, 1, 255, true));

        return new FileData(FileName.equip_gear, fileCommands);
    }

    private FileData GodMode() {
        ArrayList<String> fileCommands = new ArrayList<>();
        fileCommands.add(giveEffect("@s", Effect.resistance, 99999, 4, true));
        fileCommands.add("item replace entity @s weapon.mainhand with trident[custom_name='[{\"bold\":false,\"color\":\"white\",\"italic\":false,\"obfuscated\":true,\"text\":\"aA\"},{\"bold\":true,\"color\":\"#8C3CC1\",\"obfuscated\":false,\"text\":\" The\"},{\"bold\":true,\"color\":\"#E280FF\",\"obfuscated\":false,\"text\":\" Impaler \"},{\"color\":\"white\",\"obfuscated\":true,\"text\":\"Aa\"}]',lore=['{\"text\":\"This holy weapon impales anything it touches\"}'],unbreakable={show_in_tooltip:false},damage=0,enchantments={levels:{\"minecraft:fire_aspect\":255,\"minecraft:sharpness\":255,\"minecraft:efficiency\":255,'impaling':255},show_in_tooltip:false},attribute_modifiers={modifiers:[{id:\"" + AttributeType.armor + "\",type:\"" + AttributeType.attack_damage + "\",amount:1000,operation:\"add_value\",slot:\"mainhand\"}],show_in_tooltip:false}]");

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
        fileCommands.add(execute.In(controlPoints.get(0).getCoordinate().getDimension()) +
                "forceload add " + controlPoints.get(0).getCoordinate().getX() + " " + controlPoints.get(0).getCoordinate().getZ() + " " + controlPoints.get(0).getCoordinate().getX() + " " + controlPoints.get(0).getCoordinate().getZ());
        fileCommands.add(execute.In(controlPoints.get(1).getCoordinate().getDimension()) +
                "forceload add " + controlPoints.get(1).getCoordinate().getX() + " " + controlPoints.get(1).getCoordinate().getZ() + " " + controlPoints.get(1).getCoordinate().getX() + " " + controlPoints.get(1).getCoordinate().getZ());
        fileCommands.add(callFunction(FileName.spawn_controlpoints));
        fileCommands.add(execute.In(controlPoints.get(0).getCoordinate().getDimension()) +
                "forceload remove " + controlPoints.get(0).getCoordinate().getX() + " " + controlPoints.get(0).getCoordinate().getZ() + " " + controlPoints.get(0).getCoordinate().getX() + " " + controlPoints.get(0).getCoordinate().getZ());
        fileCommands.add(execute.In(controlPoints.get(1).getCoordinate().getDimension()) +
                "forceload remove " + controlPoints.get(1).getCoordinate().getX() + " " + controlPoints.get(1).getCoordinate().getZ() + " " + controlPoints.get(1).getCoordinate().getX() + " " + controlPoints.get(1).getCoordinate().getZ());
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
        fileCommands.add("tag @a remove " + Tag.Traitor);
        fileCommands.add("tag @a remove " + Tag.DontMakeTraitor);
        fileCommands.add("tag @a remove " + Tag.RespawnDisabled);
        fileCommands.add("worldborder set " + 2 * worldSize + " 1");
        fileCommands.add("team leave @a");
        fileCommands.add(callFunction(FileName.display_rank));
        fileCommands.add("scoreboard players set NightTime Time 600");
        fileCommands.add("scoreboard players set CarePackages Time 1200");
        fileCommands.add("scoreboard players set ControlPoints Time 1800");
        fileCommands.add("scoreboard players set TraitorFaction Time 2400");
        for (int i = 0; i < 4; i++) {
            fileCommands.add("tag @a remove ReceivedPerk" + (i + 1));
        }

        for (Team t : teams) {
            fileCommands.add("scoreboard players reset " + t.getPlayerColor() + " CPScore");
            fileCommands.add("team join " + t.getName() + " " + t.getPlayerColor());
        }

        int minToCPScore = secPerMinute * tickPerSecond * controlPoints.get(0).getAddRate();
        fileCommands.add("scoreboard players set Perk1 CPScore " + 3 * minToCPScore);
        fileCommands.add("scoreboard players set Perk2 CPScore " + 6 * minToCPScore);
        fileCommands.add("scoreboard players set Perk3 CPScore " + 12 * minToCPScore);
        fileCommands.add("scoreboard players set Perk4 CPScore " + 15 * minToCPScore);
        fileCommands.add("scoreboard players set TimeVictory CPScore " + 20 * minToCPScore);

        // Reset player scales
        fileCommands.add(execute.As(new Entity("@a")) +
                        setAttributeBase("@s", AttributeType.scale, 1));

        fileCommands.add("gamemode creative @s");

        // Clear scheduled commands
        fileCommands.add(callFunction(FileName.clear_schedule));

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
        fileCommands.add(clearEffect("@a"));
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

        // Teleport to starting coordinates
        fileCommands.add(execute.In(Dimension.overworld) +
                "tp @a " + startCoordinate.getCoordinateString());

        // Reset scores
        fileCommands.add("scoreboard players reset @a Deaths");
        fileCommands.add("scoreboard players reset @a Kills");

        // Make players invulnerable
        fileCommands.add(giveEffect("@a", Effect.resistance, 99999, 4, true));

        return new FileData(FileName.into_calls, fileCommands);
    }

    private static FileData SpreadPlayers() {
        ArrayList<String> fileCommands = new ArrayList<>();
        fileCommands.add(execute.In(Dimension.overworld) +
                "spreadplayers 0 0 " + 0.3 * worldSize + " " + 0.9 * worldSize + " true @a");
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
        fileCommands.add(setGameRule(GameRule.doImmediateRespawn, true));
        fileCommands.add(callFunction(FileName.clear_enderchest));

        // fileCommands.add("recipe give @a uhc:golden_apple");
        fileCommands.add("recipe take @a uhc:dragon_head");

        // Remove resistance
        fileCommands.add(clearEffect("@a", Effect.resistance));

        return new FileData(FileName.survival_mode, fileCommands);
    }

    private FileData StartGame() {
        ArrayList<String> fileCommands = new ArrayList<>();
        fileCommands.add("time set 0");
        fileCommands.add("xp set @a 0 levels");
        fileCommands.add(giveEffect("@a", Effect.regeneration, 1, 255));
        fileCommands.add(giveEffect("@a", Effect.saturation, 1, 255));
        fileCommands.add(giveEffect("@a", Effect.resistance, 20*60, 2));
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
        fileCommands.add("give @a minecraft:bundle[custom_data={locateTeammate:1b}]");

        // Initialize CP messaging dummies
        for (int i = 1; i < controlPoints.size() + 1; i++) {
            for (int ii = 1; ii < 3; ii++) {
                fileCommands.add("scoreboard players set @a MSGDum" + ii + "CP" + i + " 1");
            }
        }

        return new FileData(FileName.start_game, fileCommands);
    }

    private static FileData BattleRoyale() {
        ArrayList<String> fileCommands = new ArrayList<>();
        fileCommands.add(execute.In(Dimension.overworld, false) +
                execute.PositionedNext(new Coordinate(0, 151, 0), true) +
                "gamemode survival @a[distance=..20,gamemode=!creative]");
        fileCommands.add(execute.In(Dimension.overworld, false) +
                execute.PositionedNext(new Coordinate(0, 151, 0), true) +
                "spreadplayers 0 0 " + 0.3 * worldSize + " " + 0.9 * worldSize + " true @a[distance=..20,gamemode=survival]");

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

        return new FileData(FileName.second_controlpoint, fileCommands);
    }

    private FileData Minute(int i) {
        ArrayList<String> fileCommands = new ArrayList<>();
        ArrayList<TextItem> texts = new ArrayList<>();
        texts.add(bannerText);
        texts.add(new Text(Color.gold, true, false, communityName + " UHC"));
        texts.add(bannerText);
        texts.add(new Text(Color.light_purple, true, false, i + " MINUTE(S) REMAINING"));
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
            // Give players on the Control Point score
            fileCommands.add(execute.As("@a[gamemode=!spectator,team=" + team.getName() + "]", false) +
                    execute.IfNext("@s[gamemode=!spectator,x=" + (controlPoints.get(i - 1).getCoordinate().getX() - 6) + ",y=" + (controlPoints.get(i - 1).getCoordinate().getY() - 1) + ",z=" + (controlPoints.get(i - 1).getCoordinate().getZ() - 6) + ",dx=12,dy=12,dz=12]" +
                    execute.UnlessNext("@a[gamemode=!spectator,x=" + (controlPoints.get(i - 1).getCoordinate().getX() - 6) + ",y=" + (controlPoints.get(i - 1).getCoordinate().getY() - 1) + ",z=" + (controlPoints.get(i - 1).getCoordinate().getZ() - 6) + ",dx=12,dy=12,dz=12,team=!" + team.getName() + "]"), true) +
                    "scoreboard players add @s ControlPoint" + i + " " + controlPoints.get(i - 1).getAddRate());

            // Update CP glass color
            fileCommands.add(execute.In(controlPoints.get(i - 1).getCoordinate().getDimension(), false) +
                    execute.IfNext("@r[limit=1,gamemode=!spectator,team=" + team.getName() + "]", getObjectiveByName("ControlPoint" + i), ComparatorType.greater, "@p[scores={Admin=1}]", getObjectiveByName("Highscore" + i), true) +
                    setBlock(controlPoints.get(i - 1).getCoordinate().getX(), controlPoints.get(i - 1).getCoordinate().getY() + 1, controlPoints.get(i - 1).getCoordinate().getZ(), "minecraft:" + team.getGlassColor() + "_stained_glass", SetBlockType.replace));
        }

        // Keep beacon active
        fileCommands.add(fill(controlPoints.get(i - 1).getCoordinate().getX() - 1, controlPoints.get(i - 1).getCoordinate().getY() - 1, controlPoints.get(i - 1).getCoordinate().getZ() - 1, controlPoints.get(i - 1).getCoordinate().getX() + 1, controlPoints.get(i - 1).getCoordinate().getY() - 1, controlPoints.get(i - 1).getCoordinate().getZ() + 1, BlockType.emerald_block));
        fileCommands.add(fill(controlPoints.get(i - 1).getCoordinate().getX(), controlPoints.get(i - 1).getCoordinate().getY(), controlPoints.get(i - 1).getCoordinate().getZ(), controlPoints.get(i - 1).getCoordinate().getX(), controlPoints.get(i - 1).getCoordinate().getY(), controlPoints.get(i - 1).getCoordinate().getZ(), BlockType.beacon));

        // Update CP messaging
        fileCommands.add(callFunction("" + FileName.controlpoint_messages_ + i));

        return new FileData("" + FileName.controlpoint_ + i, fileCommands);
    }

    private FileData ControlPointMessages(int i) {
        ArrayList<String> fileCommands = new ArrayList<>();

        for (Team team : teams) {
            /* Under attack message */
            // Increment attacking counter if team is on CP and message is not sent
            fileCommands.add(execute.As(new Entity("@a[gamemode=!spectator,team=" + team.getName() + "]"), false) +
                    execute.IfNext(new Entity("@s[x=" + (controlPoints.get(i - 1).getCoordinate().getX() - 6) + ",y=" + (controlPoints.get(i - 1).getCoordinate().getY() - 1) + ",z=" + (controlPoints.get(i - 1).getCoordinate().getZ() - 6) + ",dx=12,dy=12,dz=12,scores={MSGDum1CP" + i + "=.." + cpMessageThreshold + "}]"), true) +
                    "scoreboard players add @a[team=" + team.getName() + "] MSGDum1CP" + i + " 1");

            // Reset abandonment counter
            fileCommands.add(execute.As(new Entity("@a[gamemode=!spectator,team=" + team.getName() + "]"), false) +
                    execute.IfNext(new Entity("@s[x=" + (controlPoints.get(i - 1).getCoordinate().getX() - 6) + ",y=" + (controlPoints.get(i - 1).getCoordinate().getY() - 1) + ",z=" + (controlPoints.get(i - 1).getCoordinate().getZ() - 6) + ",dx=12,dy=12,dz=12]"), true) +
                    "scoreboard players set @a[gamemode=!spectator,team=" + team.getName() + "] MSGDum2CP" + i + " 1");

            // Define CP attacking message
            ArrayList<TextItem> texts = new ArrayList<>();
            texts.add(new Text(Color.light_purple, false, false, "TEAM "));
            texts.add(new Text(team.getColor(), false, false, team.getJSONColor()));
            texts.add(new Text(Color.light_purple, false, false, " IS ATTACKING CONTROL POINT " + i + "!"));

            // Display message in chat if time threshold has been exceeded
            fileCommands.add(execute.As(new Entity("@a[team=" + team.getName() + ",limit=1]"), false) +
                    execute.IfNext(new Entity("@s[scores={MSGDum1CP" + i + "=" + cpMessageThreshold + "}]"), true) +
                    new TellRaw("@a", texts).sendRaw());

            // Grant attacking players Attacking tag
            fileCommands.add(execute.As(new Entity("@a[team=" + team.getName() + ",limit=1]"), false) +
                    execute.IfNext(new Entity("@s[scores={MSGDum1CP" + i + "=" + cpMessageThreshold + "}]"), true) +
                    "tag @a[team=" + team.getName() + "] add " + Tag.AttackingCP + i);

            /* Abandoned message */
            // Increment abandonment counter unless team is on the CP
            fileCommands.add(execute.If("@p[gamemode=!spectator,team=" + team.getName() + ",tag=" + Tag.AttackingCP + i + "]", false) +
                    execute.UnlessNext("@p[gamemode=!spectator,team=" + team.getName() + ",x=" + (controlPoints.get(i - 1).getCoordinate().getX() - 6) + ",y=" + (controlPoints.get(i - 1).getCoordinate().getY() - 1) + ",z=" + (controlPoints.get(i - 1).getCoordinate().getZ() - 6) + ",dx=12,dy=12,dz=12]", true) +
                    "scoreboard players add @a[team=" + team.getName() + "] MSGDum2CP" + i + " 1");

            // Define CP abandonment message
            texts.clear();
            texts.add(new Text(Color.light_purple, false, false, "TEAM "));
            texts.add(new Text(team.getColor(), false, false, team.getJSONColor()));
            texts.add(new Text(Color.light_purple, false, false, " HAS ABANDONED CONTROL POINT " + i + "!"));

            // Display message in chat if time threshold has been exceeded
            fileCommands.add(execute.As(new Entity("@a[team=" + team.getName() + ",limit=1]"), false) +
                    execute.IfNext(new Entity("@s[scores={MSGDum2CP" + i + "=" + (cpMessageThreshold - 1) + "}]"), true) +
                    new TellRaw("@a", texts).sendRaw());

            // Reset attacking counter
            fileCommands.add(execute.As(new Entity("@a[team=" + team.getName() + ",limit=1]"), false) +
                    execute.IfNext(new Entity("@s[scores={MSGDum2CP" + i + "=" + cpMessageThreshold + "}]"), true) +
                    "scoreboard players set @a[gamemode=!spectator,team=" + team.getName() + "] MSGDum1CP" + i + " 1");

            // Remove Attacking tag
            fileCommands.add(execute.As(new Entity("@a[team=" + team.getName() + ",limit=1]"), false) +
                    execute.IfNext(new Entity("@s[scores={MSGDum2CP" + i + "=" + cpMessageThreshold + "}]"), true) +
                    "tag @a[team=" + team.getName() + "] remove " + Tag.AttackingCP + i);

        }
        return new FileData("" + FileName.controlpoint_messages_ + i, fileCommands);
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
                execute.IfNext(new Entity("@e[type=minecraft:falling_block,distance=..2]"), true) +
                "spreadplayers 0 0 10 " + carePackageSpread + " false @e[type=minecraft:falling_block,distance=..2]");

        return new FileData(FileName.carepackage_distributor, fileCommands);
    }

    private FileData TraitorHandout() {
        ArrayList<String> fileCommands = new ArrayList<>();
        ArrayList<Season> seasons = new ArrayList<>(this.seasons);
        seasons.sort(Comparator.comparing(Season::getID));
        //fileCommands.add("tag @r[limit=1,tag=!DontMakeTraitor] add Traitor");
        for (Player p : players) {
            if (p.getLastTraitorSeason() >= seasons.get(seasons.size() - traitorWaitTime).getID()) {
                fileCommands.add("tag " + p.getPlayerName() + " add DontMakeTraitor");
            }
        }

        fileCommands.add("tag @r[limit=1,tag=!DontMakeTraitor,scores={Rank=" + minTraitorRank + "..},gamemode=!spectator] add Traitor");

        for (Team t : teams) {
            fileCommands.add(execute.If(new Entity("@p[tag=Traitor,team=" + t.getName() + "]")) +
                    "tag @a[team=" + t.getName() + "] add DontMakeTraitor");
        }
        //fileCommands.add("tag @r[limit=1,tag=!DontMakeTraitor] add Traitor");
        fileCommands.add("tag @r[limit=1,tag=!DontMakeTraitor,scores={Rank=" + minTraitorRank + "..},gamemode=!spectator] add Traitor");

        // Add additional traitor
        if (traitorMode == 2) {
            fileCommands.add("tag @a remove DontMakeTraitor");
            for (Player p : players) {
                if (p.getLastTraitorSeason() >= seasons.get(seasons.size() - traitorWaitTime).getID()) {
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

        fileCommands.add(execute.As(new Entity("@a[tag=Traitor]")) +
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

        fileCommands.add(execute.As(new Entity("@a[tag=Traitor]")) +
                new Title("@s", TitleType.actionbar, texts).displayTitle());

        fileCommands.add(execute.If(new Entity("@p[scores={Admin=1,Victory=1}]")) +
                callFunction(FileName.traitor_check));

        return new FileData(FileName.traitor_actionbar, fileCommands);
    }

    private FileData TeamScore() {
        ArrayList<String> fileCommands = new ArrayList<>();
        for (int i = 1; i < controlPoints.size() + 1; i++) {
            for (Team t : teams) {
                fileCommands.add(execute.As(new Entity("@r[limit=1,gamemode=!spectator]")) +
                        "scoreboard players operation @p[scores={Admin=1}] CP" + i + t.getName() + " > @s[team=" + t.getName() + "] ControlPoint" + i);

                fileCommands.add(execute.As(new Entity("@r[limit=1,gamemode=!spectator]")) +
                        "scoreboard players operation @s[team=" + t.getName() + "] ControlPoint" + i + " > @p[scores={Admin=1}] CP" + i + t.getName());
            }
        }

        for (Team t : teams) {
            fileCommands.add(execute.In(controlPoints.get(0).getCoordinate().getDimension(), false) +
                    execute.AsNext(new Entity("@r[limit=1,gamemode=!spectator,x=" + (controlPoints.get(0).getCoordinate().getX() - 6) + ",y=" + (controlPoints.get(0).getCoordinate().getY() - 1) + ",z=" + (controlPoints.get(0).getCoordinate().getZ() - 6) + ",dx=12,dy=12,dz=12,team=" + t.getName() + "]"), true) +
                    "scoreboard players operation @p[scores={Admin=1}] CP1" + t.getName() + " > @p[scores={Admin=1}] CP2" + t.getName());

            fileCommands.add(execute.In(controlPoints.get(1).getCoordinate().getDimension(), false) +
                    execute.AsNext(new Entity("@r[limit=1,gamemode=!spectator,x=" + (controlPoints.get(1).getCoordinate().getX() - 6) + ",y=" + (controlPoints.get(1).getCoordinate().getY() - 1) + ",z=" + (controlPoints.get(1).getCoordinate().getZ() - 6) + ",dx=12,dy=12,dz=12,team=" + t.getName() + "]"), true) +
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

            // Activate structure block
            fileCommands.add(execute.In(c.getDimension()) +
                    setBlock(c.getX(), c.getY() + 10, c.getZ(), BlockType.redstone_block, SetBlockType.destroy));

            // Initialize object
            for (int i = c.getY() + 12; i < worldHeight; i++) {
                // Specify block to be changed
                c.setCoordinate(c.getX(), i, c.getZ());

                fileCommands.add(execute.In(c.getDimension(), false) +
                        execute.UnlessNext(c, BlockType.air) +
                        execute.UnlessNext(c, BlockType.cave_air) +
                        execute.UnlessNext(c, BlockType.void_air) +
                        execute.UnlessNext(c, BlockType.bedrock, true) +
                        setBlock(c.getX(), i, c.getZ(), BlockType.glass));
            }

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

    private FileData WorldPreload() {
        ArrayList<String> fileCommands = new ArrayList<>();
        fileCommands.add("scoreboard players add @a[gamemode=creative] WorldLoad 1");
        fileCommands.add("scoreboard players add @a[gamemode=creative] Time 1");
        fileCommands.add(execute.If(new Entity("@p[scores={WorldLoad=400..}]")) +
                "spreadplayers 0 0 5 " + worldSize + " false @a");
        fileCommands.add(execute.If(new Entity("@p[scores={Time=12000..}]"), false) +
                execute.InNext(Dimension.overworld, true) +
                setBlock(6, worldBottom + 2, 15, BlockType.bedrock));
        fileCommands.add(execute.If(new Entity("@p[scores={Time=12000..}]"), false) +
                execute.InNext(Dimension.overworld, true) +
                "tp @a[gamemode=creative] 0 221 0");
        fileCommands.add(execute.If(new Entity("@p[scores={Time=12000..}]")) +
                setGameRule(GameRule.commandBlockOutput, true));
        fileCommands.add(execute.If(new Entity("@p[scores={WorldLoad=400..}]")) +
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
        fileCommands.add(execute.At(new Entity("@a[nbt={RootVehicle:{Entity:{id:\"minecraft:horse\"}}}]")) +
                relativeFill(-2, -2, -2, 2, 0, 2, "ice", SetBlockType.replace, "water"));

        return new FileData(FileName.horse_frost_walker, fileCommands);
    }

    private FileData WolfCollarExecute() {
        ArrayList<String> fileCommands = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            fileCommands.add(execute.As(new Entity("@e[type=minecraft:wolf]"), false) +
                    execute.StoreNext(ExecuteStore.result, "@s", getObjectiveByName("CollarCheck" + i), true) +
                    "data get entity @s Owner[" + i + "]");

            for (Team t : teams) {
                fileCommands.add(execute.As(new Entity("@a[team=" + t.getName() + "]"), false) +
                        execute.StoreNext(ExecuteStore.result, "@s", getObjectiveByName("CollarCheck" + i), true) +
                        "data get entity @s UUID[" + i + "]");


            }
        }
        for (Team t : teams) {
            fileCommands.add("tag @a[team=" + t.getName() + "] add CollarCheck");
            fileCommands.add(execute.As(new Entity("@e[type=wolf]"), false) +
                    execute.IfNext("@s", getObjectiveByName("CollarCheck0"), ComparatorType.equal, "@p[tag=CollarCheck]", getObjectiveByName("CollarCheck0")) +
                    execute.IfNext("@s", getObjectiveByName("CollarCheck1"), ComparatorType.equal, "@p[tag=CollarCheck]", getObjectiveByName("CollarCheck1"), true) +
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
                fileCommands.add(execute.If(new Entity("@p[scores={SideDum=" + (10 * tickPerSecond * i) + "}]")) +
                        s.setDisplay("sidebar"));
            }
        }
        fileCommands.add(execute.If(new Entity("@p[scores={SideDum=" + (10 * tickPerSecond * i + 1) + "}]")) +
                "scoreboard players reset @p[scores={Admin=1}] SideDum");

        // Update stripmine count
        fileCommands.add("scoreboard players set @a[scores={Mining=1..}] Mining 0");
        fileCommands.add(execute.As(new Entity("@a")) +
                callFunction(FileName.update_mine_count));

        for (int ii = 0; ii < 5; ii++) {
            // Remove piercing enchantment
            fileCommands.add(execute.If(new Entity("@p[nbt={SelectedItem:{id:\"minecraft:crossbow\",count:1,components:{\"minecraft:enchantments\":{levels:{\"minecraft:piercing\":" + (ii + 1) + "}}}}}]")) +
                    new TellRaw("@p[nbt={SelectedItem:{id:\"minecraft:crossbow\",count:1,components:{\"minecraft:enchantments\":{levels:{\"minecraft:piercing\":" + (ii + 1) + "}}}}}]", new Text(Color.red, true, false, "PIERCING IS NOT ALLOWED, YOU NAUGHTY BUM!")).sendRaw());
            fileCommands.add("item replace entity @p[nbt={SelectedItem:{id:\"minecraft:crossbow\",count:1,components:{\"minecraft:enchantments\":{levels:{\"minecraft:piercing\":" + (ii + 1) + "}}}}}] weapon.mainhand with minecraft:crossbow");

            // Remove power enchantment
            fileCommands.add(execute.If(new Entity("@p[nbt={SelectedItem:{id:\"minecraft:bow\",count:1,components:{\"minecraft:enchantments\":{levels:{\"minecraft:power\":" + (ii + 1) + "}}}}}]")) +
                    new TellRaw("@p[nbt={SelectedItem:{id:\"minecraft:bow\",count:1,components:{\"minecraft:enchantments\":{levels:{\"minecraft:power\":" + (ii + 1) + "}}}}}]", new Text(Color.red, true, false, "POWER IS NOT ALLOWED, YOU NAUGHTY BUM!")).sendRaw());
            fileCommands.add("item replace entity @p[nbt={SelectedItem:{id:\"minecraft:bow\",count:1,components:{\"minecraft:enchantments\":{levels:{\"minecraft:power\":" + (ii + 1) + "}}}}}] weapon.mainhand with minecraft:bow");
        }
        // Remove wolf armor
        fileCommands.add(execute.If(new Entity("@p[nbt={SelectedItem:{id:\"minecraft:wolf_armor\",count:1}}]")) +
                new TellRaw("@p[nbt={SelectedItem:{id:\"minecraft:wolf_armor\",count:1}}]", new Text(Color.red, true, false, "WOLF ARMOR IS NOT ALLOWED, YOU NAUGHTY BUM!")).sendRaw());
        fileCommands.add("item replace entity @p[nbt={SelectedItem:{id:\"minecraft:wolf_armor\",count:1}}] weapon.mainhand with minecraft:leather_horse_armor");

        // Update public team CP scores
        fileCommands.add(callFunction(FileName.update_public_cp_score));

        return new FileData(FileName.update_sidebar, fileCommands);
    }

    private FileData Timer() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Announce dead players
        fileCommands.add(execute.If(new Entity("@p[scores={Deaths=1}]")) +
                callFunction(FileName.handle_player_death));

        fileCommands.add("scoreboard players add @p[scores={Admin=1}] Time2 1");
        fileCommands.add("scoreboard players add @p[scores={Admin=1}] TimDum 1");
        fileCommands.add(execute.If(new Entity("@p[scores={TimDum=" + tickPerSecond + "}]")) +
                "scoreboard players add @p[scores={Admin=1}] TimeDum 1");
        fileCommands.add(execute.Store(ExecuteStore.result, "CurrentTime", getObjectiveByName("Time")) +
                "scoreboard players get @p[scores={Admin=1}] TimeDum");
        fileCommands.add(execute.If(new Entity("@p[scores={TimDum=" + tickPerSecond + "..}]")) +
                "scoreboard players reset @p[scores={Admin=1}] TimDum");


        fileCommands.add(execute.If(new Entity("@p[scores={Time2=" + (300 * tickPerSecond) + "}]")) +
                new TellRaw("@a", new Text(Color.gray, false, false, "PVP IS NOT ALLOWED UNTIL DAY 2!")).sendRaw());

        ArrayList<TextItem> texts = new ArrayList<>();
        texts.add(bannerText);
        texts.add(new Text(Color.gold, true, false, communityName + " UHC"));
        texts.add(bannerText);
        texts.add(new Text(Color.light_purple, true, false, "DAY TIME HAS ARRIVED & ETERNAL DAY ENABLED!"));
        texts.add(bannerText);

        fileCommands.add(execute.If(new Entity("@p[scores={Time2=" + (1200 * tickPerSecond) + "}]")) +
                new TellRaw("@a", texts).sendRaw());
        fileCommands.add(callFunction(FileName.display_quotes));
        fileCommands.add(callFunction(FileName.locate_teammate));
        fileCommands.add(callFunction(FileName.eliminate_baby_wolf));

        // Update wolf collars
        fileCommands.add(callFunction(FileName.wolf_collar_execute));

        // Horse frost walker
        fileCommands.add(callFunction(FileName.horse_frost_walker));

        // Update minimum health
        fileCommands.add(callFunction(FileName.update_min_health));

        // Set tamed wolf base health
        fileCommands.add(execute.As(new Entity("@e[type=wolf]"), false) +
                execute.IfNext(DataClasses.entity, "@s Owner", true) +
                setAttributeBase("@s", AttributeType.max_health, 20));

        return new FileData(FileName.timer, fileCommands);
    }

    // Perks for being on the Control Point
    private FileData ControlPointPerks() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Define perk activation times
        int minToCPScore = secPerMinute * tickPerSecond * controlPoints.get(0).getAddRate();
        ArrayList<Perk> perks = new ArrayList<>();
        perks.add(new Perk(1, new StatusEffect(Effect.speed, 999999, 0, false), Sound.BASALT, 3 * minToCPScore));
        perks.add(new Perk(2, new Attribute(AttributeType.scale, 0.8), Sound.CRIMSON, 6 * minToCPScore));
        perks.add(new Perk(3, new StatusEffect(Effect.haste, 999999, 2, false), Sound.WARPED, 12 * minToCPScore));
        perks.add(new Perk(4, new StatusEffect(Effect.absorption, 999999, 1, false), Sound.WITHER, 15 * minToCPScore));


        Entity currentPlayer = new Entity("");
        Entity currentScoreCheck = new Entity("");

        for (int i = 0; i < controlPoints.size(); i++) {
            for (Team team : teams) {
                // Display team receiving perk
                for (Perk perk : perks) {
                    // Set variables
                    currentPlayer.setEntity("@p[team=" + team.getName() + ",tag=!" + Tag.ReceivedPerk + perk.getId() + "]");
                    currentScoreCheck.setEntity("@p[scores={CP" + (i + 1) + team.getName() + "=" + perk.getActivationTime() + "..}]");

                    // Create text to be displayed
                    ArrayList<TextItem> texts = new ArrayList<>();
                    texts.add(new Text(Color.light_purple, false, false, "TEAM "));
                    texts.add(new Text(team.getColor(), false, false, team.getJSONColor()));
                    texts.add(new Text(Color.light_purple, false, false, " HAS REACHED"));
                    texts.add(new Text(Color.gold, false, false, " PERK " + perk.getId() + "!"));

                    // Display text
                    fileCommands.add(execute.If(currentScoreCheck, false) +
                            execute.IfNext(currentPlayer, true) +
                            new TellRaw("@a", texts).sendRaw());

                    // Give rewards
                    String perkReceivers = "@a[team=" + team.getName() + ",tag=!" + Tag.ReceivedPerk + perk.getId() + "]";
                    fileCommands.add(execute.If(currentScoreCheck) +
                            perk.getReward(perkReceivers));

                    // Play sound
                    fileCommands.add(execute.If(currentScoreCheck, false) +
                            execute.IfNext(currentPlayer, true) +
                            playSound(perk.getSound(), SoundSource.master, "@a", "~", "~50", "~", "100", "1", "0"));

                    // Add tag
                    fileCommands.add(execute.If(currentScoreCheck) +
                            "tag @a[team=" + team.getName() + "] add " + Tag.ReceivedPerk + perk.getId());
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
            fileCommands.add(execute.If(new Entity("@p[scores={Time2=" + (7 * secPerMinute * tickPerSecond * (i + 1)) + "}]")) +
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

        // Find player with lowest health
        fileCommands.add(execute.As(new Entity("@r[gamemode=!spectator]"), false) +
                execute.IfNext("@s", getObjectiveByName("Hearts"), ComparatorType.less, "@p[scores={Admin=1}]", getObjectiveByName("MinHealth")) +
                execute.StoreNext(ExecuteStore.result, "@p[scores={Admin=1}]", getObjectiveByName("MinHealth"), true) +
                "scoreboard players get @s Hearts");

        return new FileData(FileName.update_min_health, fileCommands);
    }

    // Reset health of respawned players
    private FileData RespawnPlayer() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Define player that needs to be respawned
        String respawnPlayer = "@p[tag=Respawn]";

        // Teleport player to their team
        for (Team t : teams) {
            fileCommands.add(execute.As(new Entity(respawnPlayer), false) +
                    execute.IfNext(new Entity("@s[team=" + t.getName() + "]"), true) +
                    "tp @s @r[gamemode=!spectator, team=" + t.getName() + "]");
        }

        // Set player's gamemode to survival
        fileCommands.add(execute.As(new Entity(respawnPlayer)) +
                "gamemode survival @s");

        // Remove player heads
        fileCommands.add(execute.As(new Entity("@a[nbt={Inventory:[{id:\"minecraft:player_head\"}]}]")) +
                "clear @s minecraft:player_head");  // Remove from inventory
        fileCommands.add(execute.As(new Entity("@e[type=item,nbt={Item:{id:\"minecraft:player_head\"}}]")) +
                "kill @s"); // Remove item

        // Give new bundle to people who respawn
        fileCommands.add("give @p[tag=Respawn] minecraft:bundle[custom_data={locateTeammate:1b}]");

        // Set respawn health
        for (int i = 0; i < 10; i++) {
            int indexFront = 2 * i + 1;
            int indexRear = 2 * (i + 1);

            fileCommands.add(execute.If(new Entity("@p[scores={MinHealth=" + indexFront + ".." + indexRear + "}]")) +
                    setAttributeBase(respawnPlayer, AttributeType.max_health, i + 1));
        }
        fileCommands.add(giveEffect(respawnPlayer, Effect.health_boost, 1, 0));
        fileCommands.add(clearEffect(respawnPlayer, Effect.health_boost));
        fileCommands.add(setAttributeBase(respawnPlayer, AttributeType.max_health, 20));

        // Remove respawn tag
        fileCommands.add("tag @p[tag=Respawn] remove Respawn");

        return new FileData(FileName.respawn_player, fileCommands);
    }

    private FileData ControlPointCaptured() {
        ArrayList<String> fileCommands = new ArrayList<>();
        fileCommands.add(execute.In(Dimension.overworld) +
                setBlock(8, worldBottom + 2, 0, BlockType.bedrock, SetBlockType.replace));

        ArrayList<TextItem> texts = new ArrayList<>();
        texts.add(bannerText);
        texts.add(new Text(Color.gold, true, false, communityName + " UHC"));
        texts.add(bannerText);
        texts.add(new Text(Color.light_purple, true, false, "THE CONTROL POINT HAS BEEN CAPTURED!"));
        texts.add(bannerText);

        fileCommands.add(new TellRaw("@a", texts).sendRaw());
        fileCommands.add(execute.In(Dimension.overworld) +
                fill(15, worldBottom + 2, 3, 15, worldBottom + 2, 4, BlockType.bedrock));

        fileCommands.add(new Title("@a", TitleType.subtitle, new Text(Color.light_purple, true, true, "has been captured!")).displayTitle());
        fileCommands.add(new Title("@a", TitleType.title, new Text(Color.gold, true, true, "The Control Point")).displayTitle());

        fileCommands.add(callFunction(FileName.teams_highscore_alive_check));

        return new FileData(FileName.control_point_captured, fileCommands);
    }

    private FileData TraitorCheck() {
        ArrayList<String> fileCommands = new ArrayList<>();

        //When no traitors remain start teams_alive_check
        fileCommands.add(execute.Unless("@a[limit=1,tag=Traitor,gamemode=!spectator]") +
                callFunction(FileName.teams_alive_check));

        fileCommands.add(execute.Unless("@a[limit=1,tag=!Traitor,gamemode=!spectator]") +
                callFunction(FileName.victory_message_traitor));

        return new FileData(FileName.traitor_check, fileCommands);
    }

    private FileData TeamsAliveCheck() {
        ArrayList<String> fileCommands = new ArrayList<>();
        for (int i = 0; i < teams.size(); i++) {
            fileCommands.add(execute.Unless("@a[limit=1,team=!" + teams.get(i).getName() + ",gamemode=!spectator]") +
                    callFunction("" + FileName.victory_message_ + i));
        }
        return new FileData(FileName.teams_alive_check, fileCommands);
    }

    private FileData TeamsHighscoreCheck() {
        ArrayList<String> fileCommands = new ArrayList<>();
        for (int i = 0; i < teams.size(); i++) {
            for (int j = 1; j < 3; j++) {
                fileCommands.add(execute.If(new Entity("@a[limit=1,scores={Admin=1,Victory=1}]"), false) +
                        execute.IfNext(new Entity("@p[team=" + teams.get(i).getName() + ",gamemode=!spectator,scores={ControlPoint" + j + "=" + (maxCPScore * tickPerSecond) + "..},tag=!Traitor]"), true) +
                        callFunction("" + FileName.victory_message_ + i));
                fileCommands.add(execute.If(new Entity("@a[limit=1,scores={Admin=1,Victory=1}]"), false) +
                        execute.IfNext("@p[team=" + teams.get(i).getName() + ",gamemode=!spectator,scores={ControlPoint" + j + "=" + (maxCPScore * tickPerSecond) + "..},tag=Traitor]") +
                        execute.UnlessNext("@p[team=" + teams.get(i).getName() + ",gamemode=!spectator,scores={ControlPoint" + j + "=" + (maxCPScore * tickPerSecond) + "..},tag=!Traitor]", true) +
                        callFunction(FileName.victory_message_traitor));
            }
        }
        return new FileData(FileName.teams_highscore_alive_check, fileCommands);
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
                fileCommands.add(execute.As(new Entity("@a[team=" + team.getName() + ",nbt={SelectedItem:{id:\"minecraft:bundle\",components:{\"minecraft:custom_data\":{locateTeammate:1b}}}}]"), false) +
                        execute.AtNext(new Entity("@s")) +
                        execute.IfNext(new Entity("@a[team=" + team.getName() + ",distance=0.1..,gamemode=!spectator]")) +
                        execute.FacingNext(new Entity("@a[team=" + team.getName() + ",distance=0.1..,gamemode=!spectator,limit=1,sort=nearest]"), EntityAnchor.eyes) +
                        execute.PositionedNext(new Coordinate(0, 1, 0, ReferenceFrame.relative)) +
                        execute.PositionedNext(new Coordinate(0, 0, i + 1, ReferenceFrame.relative_facing), true) +
                        "particle dust{color:[" + team.getDustColor() + "],scale:1} ~ ~ ~ 0 0 0 0 1 normal @s");
            }
        }

        return new FileData(FileName.locate_teammate, fileCommands);
    }

    private FileData EliminateBabyWolf() {
        ArrayList<String> fileCommands = new ArrayList<>();

        Entity babyWolf = new Entity("@e[type=wolf, scores={WolfAge=..-1}]");

        fileCommands.add(execute.As(new Entity("@e[limit=1, type=wolf, sort=random]"), false) +
                execute.StoreNext(ExecuteStore.result, "@s", getObjectiveByName("WolfAge"), true) +
                "data get entity @s Age");
        fileCommands.add(execute.At(babyWolf) +
                "summon minecraft:dolphin ~ ~ ~");
        fileCommands.add(execute.As(babyWolf) +
                "kill @s");

        return new FileData(FileName.eliminate_baby_wolf, fileCommands);
    }

    private FileData UpdatePublicCPScore() {
        ArrayList<String> fileCommands = new ArrayList<>();

        for (Team t : teams) {
            for (int i = 1; i < controlPoints.size() + 1; i++) {
                fileCommands.add("scoreboard players operation " + t.getPlayerColor() + " CPScore > @p[scores={Admin=1}] CP" + i + t.getName());
            }
        }

        return new FileData(FileName.update_public_cp_score, fileCommands);
    }

    private FileData DisableRespawn() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Add first blood tag
        fileCommands.add("tag @p[scores={Admin=1}] add " + Tag.RespawnDisabled);

        // Update immediate respawn
        fileCommands.add(setGameRule(GameRule.doImmediateRespawn, false));

        return new FileData(FileName.disable_respawn, fileCommands);
    }
}

