import EntityClasses.*;
import Enums.*;
import FileGeneration.*;
import HelperClasses.*;
import ItemClasses.*;
import ItemModifiers.*;
import Predicates.*;
import TeamGeneration.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

import static java.lang.Integer.parseInt;

public class Main {


    //TODO: Automate process using args.
    public static void main(String[] args) throws IOException {
        new Main().run(args);
    }

    //packages
    FileTools fileTools = new FileTools();

    //DatapackData<

    private String uhcNumber;
    private static final String version = "4.0";
    private String dataPackLocation;
    private String pluginLocation;
    private String worldLocation;
    private String dataPackName;
    private static final String namespace = "uhc";

    private String fileLocation;
    private CommunityMode communityMode = CommunityMode.DIORITE;
    private int teamMode;
    //DatapackData>

    //GameData<
    private static final int chestSize = 27;
    private Coordinate startCoordinate;
    private ArrayList<Team> teams = new ArrayList<>();
    private ArrayList<ControlPoint> cpList = new ArrayList<>();
    private ArrayList<ControlPoint> controlPoints = new ArrayList<>();
    private ArrayList<ScoreboardObjective> scoreboardObjectives = new ArrayList<>();
    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Season> seasons = new ArrayList<>();
    private ArrayList<String> quotes = new ArrayList<>();
    private ArrayList<BossBar> bossBars = new ArrayList<>();
    public static World world = new World(0, Constant.worldHeight, Constant.worldBottom, Constant.worldShape);
    private static final int cpTickPerSecond = 1;
    private static final int cp2ActivationInMin = 6;
    private int cp2ActivationScore;
    private int maxCPScore;
    private static final int cpCaptureInMin = 20;
    private static final int maxCPScoreBossbar = 20 * Constant.secPerMinute * cpTickPerSecond * 2;
    private static final int cpMessageThreshold = 5 * Constant.tickFrequencyLong;
    private static final int minJoinDistance = 10;
    private static final int minDamage = 9;
    private static final String[] cartesian = {"X", "Y", "Z"};
    private static int carePackageAmount;
    private static int carePackageSpread;
    private int minTraitorRank;
    private int traitorWaitTime;
    private static final int traitorMode = 1;
    private String communityName;
    public static final Scoreboard scoreboard = new Scoreboard();

    private final Text bannerText = new Text(Color.dark_gray, true, false, " | ");

    private TeamGenerator teamGenerator;
    private ServerProperties properties = new ServerProperties();
    private ArrayList<PaperPlugin> plugins = new ArrayList<>();

    private Singleton singleton;

    //GameData>


    private ArrayList<FileData> files = new ArrayList<>();


    private void run(String[] args) throws IOException {
        singleton = Singleton.getInstance();
        communityModeChange();
        createDatapack();
        System.out.println("Datapack created");
        if (args.length == 0) {
            boolean menuRunning = true;
            Scanner scanner = new Scanner(System.in);
            String input;
            while (menuRunning) {
                System.out.println("-----------------\n");
                System.out.println("Gamemode: " + communityMode);
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
                    changeCommunitymode(num);
                    createDatapack();
                    System.out.println("Datapack created");
                } else if (input.equals("r")) {
                    communityModeChange();
                    createDatapack();
                    System.out.println("Datapack created");
                } else if (input.equals("t")) {
                    teamGenerator = new TeamGenerator(Double.parseDouble(uhcNumber), fileLocation, teams, communityMode);
                    teamGenerator.run();
                } else {
                    System.out.println("Input not recognized.");
                }
            }
        } else {
            teamGenerator = new TeamGenerator(Double.parseDouble(uhcNumber), fileLocation, teams, communityMode);
            teamGenerator.run(args);
        }
    }

    private void changeCommunitymode(int num) throws IOException {
        if (num == 0) {
            communityMode = CommunityMode.DIORITE;
        }
        if (num == 1) {
            communityMode = CommunityMode.URE;
        }
        if (num == 2) {
            communityMode = CommunityMode.KINJIN;
        }

        communityModeChange();
    }


    private void createDatapack() {
        try {
            fileTools.createDatapack(files, fileLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void communityModeChange() throws IOException {
        files = new ArrayList<>();
        loadUHCData();
        makeServerProperties();
        initSaveDir();
        fileTools = new FileTools(version, dataPackLocation, dataPackName, worldLocation, pluginLocation, namespace);

        initGameData();
        makeFunctionFiles();
        files.addAll(fileTools.makeRecipeFiles());
        if (OperationMode.carePackages) {
            makeLootTableFiles();
        }
        definePlugins();
    }

    private void loadUHCData() {
        // Get data from uhc_data.txt
        uhcNumber = fileTools.getContentOutOfFile("Files\\" + communityMode + "\\uhc_data.txt", "uhcNumber");
        String[] splitStartCoordinates = fileTools.splitLineOnComma(fileTools.getContentOutOfFile("Files\\" + communityMode + "\\uhc_data.txt", "startCoordinate"));
        startCoordinate = new Coordinate(Integer.parseInt(splitStartCoordinates[0]), Integer.parseInt(splitStartCoordinates[1]), Integer.parseInt(splitStartCoordinates[2]));
        communityName = fileTools.getContentOutOfFile("Files\\" + communityMode + "\\uhc_data.txt", "communityName");

        if (OperationMode.traitorFaction) {
            minTraitorRank = Integer.parseInt(fileTools.getContentOutOfFile("Files\\" + communityMode + "\\uhc_data.txt", "minTraitorRank"));
            traitorWaitTime = Integer.parseInt(fileTools.getContentOutOfFile("Files\\" + communityMode + "\\uhc_data.txt", "traitorWaitTime"));
        }
    }

    private void makeServerProperties() throws IOException {
        String filePath = "Server\\server.properties";

        // Override fields
        properties.set("difficulty", Difficulty.hard);
        properties.set("enable-command-block", true);
        properties.set("gamemode", GameMode.adventure);
        properties.set("level-seed", -2901703172L);
        properties.set("max-players", 50);
        properties.set("motd", communityName + " UHC S" + uhcNumber);
        properties.set("simulation-distance", 5);
        properties.set("spawn-protection", 0);
        properties.set("view-distance", 7);
        if (OperationMode.bots) {
            properties.set("online-mode", false);
        }

        // Save back to the same file
        properties.saveToFile(filePath);

        System.out.println("Server properties updated successfully.");
    }

    private void initSaveDir() {
        // Set directories
        pluginLocation = "Server\\plugins\\";
        worldLocation = "Server\\" + properties.get("level-name") + "\\";
        dataPackLocation = worldLocation + "datapacks\\";
        dataPackName = "uhc-datapack-" + uhcNumber + "v" + version;
        fileLocation = dataPackLocation + dataPackName + "\\data\\";

        // Create new folders if non-existent
        Path path = Paths.get(dataPackLocation);
        try {
            if (Files.notExists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initGameData() {
        teams = new ArrayList<>();
        scoreboardObjectives = new ArrayList<>();
        players = new ArrayList<>();
        seasons = new ArrayList<>();
        quotes = new ArrayList<>();
        if (OperationMode.controlPoints) {
            bossBars = new ArrayList<>();
            cpList = new ArrayList<>();
            controlPoints = new ArrayList<>();
        }

        // Colors
        Color[] colors = {Color.yellow, Color.blue, Color.red, Color.dark_purple, Color.dark_green, Color.light_purple, Color.black, Color.gold, Color.gray, Color.aqua, Color.dark_red, Color.dark_blue, Color.dark_aqua};
        BossBarColor[] bossbarColors = {BossBarColor.yellow, BossBarColor.blue, BossBarColor.red, BossBarColor.purple, BossBarColor.green, BossBarColor.pink, BossBarColor.white, BossBarColor.white, BossBarColor.white, BossBarColor.white, BossBarColor.white, BossBarColor.white, BossBarColor.white};
        String[] glassColors = {"yellow", "light_blue", "red", "purple", "green", "pink", "black", "orange", "gray", "cyan", "red", "blue", "blue"};
        String[] collarColors = {"4", "3", "14", "10", "13", "6", "15", "1", "7", "9", "2", "11", "9"};
        String[] jsonColors = {"YELLOW", "BLUE", "RED", "PURPLE", "GREEN", "PINK", "BLACK", "ORANGE", "GRAY", "AQUA", "DARK RED", "DARK BLUE", "DARK AQUA"};
        String[] playerColors = {"Yellow", "Blue", "Red", "Purple", "Green", "Pink", "Black", "Orange", "Gray", "Aqua", "DarkRed", "DarkBlue", "DarkAqua"};
        String[] dustColors = {"1.0,1.0,0.3", "0.3,0.3,1.0", "1.0,0.3,0.3", "0.7,0.0,0.7", "0.3,1.0,0.3", "1.0,0.3,1.0", "0.0,0.0,0.0", "1.0,0.7,0.0", "0.7,0.7,0.7", "0.3,1.0,1.0", "0.7,0.0,0.0", "0.0,0.0,0.7", "0.0,0.7,0.7"};

        // Teams
        teamMode = Integer.parseInt(fileTools.getContentOutOfFile("Files\\" + communityMode + "\\uhc_data.txt", "teamMode"));
        for (int i = 0; i < colors.length; i++) {
            Team team = new Team("Team" + i, colors[i], bossbarColors[i], glassColors[i], collarColors[i], jsonColors[i], playerColors[i], dustColors[i]);
            teams.add(team);
        }

        if (OperationMode.controlPoints) {
            // Bossbars
            bossBars.add(new BossBar("cp1"));
            bossBars.add(new BossBar("cp2"));

            // ControlPoints
            ArrayList<String> controlPointString = fileTools.GetLinesFromFile("Files\\" + communityMode + "\\controlPoints.txt");
            for (String controlPoint : controlPointString) {
                String[] controlPointSplit = fileTools.splitLineOnComma(controlPoint);
                cpList.add(new ControlPoint("CP", maxCPScoreBossbar, 0, new Coordinate(Integer.parseInt(controlPointSplit[0]), Integer.parseInt(controlPointSplit[1]), Integer.parseInt(controlPointSplit[2])), Biome.valueOf(controlPointSplit[3])));
            }
        }

        // Players
        ArrayList<String> playersString = fileTools.GetLinesFromFile("Files\\" + communityMode + "\\players.txt");
        for (String player : playersString) {
            String[] playerSplit = fileTools.splitLineOnComma(player);
            boolean isPlaying = Boolean.parseBoolean(playerSplit[4]);
            if (isPlaying) {
                players.add(new Player(Integer.parseInt(playerSplit[0]), playerSplit[1], Integer.parseInt(playerSplit[2]), Double.parseDouble(playerSplit[3]), isPlaying));
            }
        }

        if (OperationMode.carePackages) {
            // World size based on amount of players
            if (players.size() <= 6) {
                carePackageSpread = 450;
                carePackageAmount = 200;
            } else if (players.size() <= 20) {
                carePackageSpread = 500;
                carePackageAmount = 200;
            } else {
                carePackageSpread = 750;
                carePackageAmount = 450;
            }
        }

        world.setWorldSizeByPlayers(players.size());

        // Seasons
        ArrayList<String> seasonsString = fileTools.GetLinesFromFile("Files\\" + communityMode + "\\seasonData.txt");
        for (String season : seasonsString) {
            String[] seasonSplit = fileTools.splitLineOnComma(season);
            seasons.add(new Season(Double.parseDouble(seasonSplit[0]), Integer.parseInt(seasonSplit[1]), new Date(Integer.parseInt(seasonSplit[2]), Integer.parseInt(seasonSplit[3]), Integer.parseInt(seasonSplit[4]))));
        }

        // Quotes
        quotes = fileTools.GetLinesFromFile("Files\\" + communityMode + "\\quotes.txt");

        if (OperationMode.controlPoints) {
            int[] addRates = {2, 3};
            Collections.shuffle(cpList);
            for (int i = 0; i < addRates.length; i++) {
                controlPoints.add(cpList.get(i));
                controlPoints.get(i).setAddRate(addRates[i]);
                controlPoints.get(i).setName(Tag.CP.extendName(i + 1));
            }

            // Control Point parameters
            singleton.setMinToCPScore(Constant.secPerMinute * cpTickPerSecond * controlPoints.get(0).getAddRate());
            cp2ActivationScore = cp2ActivationInMin * singleton.getMinToCPScore();
            maxCPScore = cpCaptureInMin * singleton.getMinToCPScore();
        }

        // Scoreboard objectives
        scoreboardObjectives.add(new ScoreboardObjective(Objective.TimeDum, ObjectiveType.dummy, "\"Elapsed Time\""));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.Time, ObjectiveType.dummy, "\"Elapsed Time\"", true));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.Time.extendName(2), ObjectiveType.dummy, "\"Elapsed Time\""));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.SideDum, ObjectiveType.dummy));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.Hearts, ObjectiveType.health));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.Apples, "minecraft.used:minecraft.golden_apple", "\"Golden Apple\"", true));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.Stone, "minecraft.mined:minecraft.stone"));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.Diorite, "minecraft.mined:minecraft.diorite"));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.Andesite, "minecraft.mined:minecraft.andesite"));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.Granite, "minecraft.mined:minecraft.granite"));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.Deepslate, "minecraft.mined:minecraft.deepslate"));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.Mining, ObjectiveType.dummy, "\"I like mining-leaderboard\"", true));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.Deaths, ObjectiveType.deathCount));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.Kills, ObjectiveType.playerKillCount, true));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.Rank, ObjectiveType.dummy));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.MinHealth, ObjectiveType.dummy));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.Victory, ObjectiveType.dummy));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.WolfAge, ObjectiveType.dummy));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.RandomQuotes, ObjectiveType.dummy));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.DamageTaken, "minecraft.custom:minecraft.damage_taken"));
        for (int i = 0; i < 2; i++) {
            scoreboardObjectives.add(new ScoreboardObjective(Objective.CollarCheck.extendName(i), ObjectiveType.dummy));
        }

        if (OperationMode.controlPoints) {
            scoreboardObjectives.add(new ScoreboardObjective(Objective.CPScore, ObjectiveType.dummy, "\"Control Point score\"", true));
            for (int i = 0; i < 2; i++) {
                scoreboardObjectives.add(new ScoreboardObjective(Objective.Highscore.extendName(i + 1), ObjectiveType.dummy));
                scoreboardObjectives.add(new ScoreboardObjective(Objective.ControlPoint.extendName(i + 1), ObjectiveType.dummy));
                for (int j = 0; j < 2; j++) {
                    scoreboardObjectives.add(new ScoreboardObjective(Objective.MSGDum.extendName((i + 1) + "CP" + (j + 1)), ObjectiveType.dummy));
                }
            }
        }

        if (OperationMode.teamCreationInGame) {
            scoreboardObjectives.add(new ScoreboardObjective(Objective.TempKills, ObjectiveType.playerKillCount));
            scoreboardObjectives.add(new ScoreboardObjective(Objective.IsKiller, ObjectiveType.dummy));
            scoreboardObjectives.add(new ScoreboardObjective(Objective.FoundTeam, ObjectiveType.dummy));
            scoreboardObjectives.add(new ScoreboardObjective(Objective.Distance, ObjectiveType.dummy));
            scoreboardObjectives.add(new ScoreboardObjective(Objective.TimesCalled, "minecraft.used:minecraft.goat_horn"));
            for (String s : cartesian) {
                scoreboardObjectives.add(new ScoreboardObjective(Objective.Pos + s, ObjectiveType.dummy));
                scoreboardObjectives.add(new ScoreboardObjective(Objective.Square + s, ObjectiveType.dummy));
            }
        }
    }

    private void makeLootTableFiles() {
        // Build loot table
        LootTableType type = LootTableType.chest;
        LootTableRolls rolls = new LootTableRolls(DistributionType.uniform, 3, 5);
        int bonusRolls = 2;

        // Loot table entry
        ArrayList<LootTableEntry> entries = new ArrayList<>();
        ArrayList<Components> components = new ArrayList<>();
        ArrayList<ItemModifier> functions = new ArrayList<>();

        // Entry #1
        entries.add(new LootTableEntry(17, BlockType.EGG));

        // Entry #2
        entries.add(new LootTableEntry(17, BlockType.LADDER, new SetCount(10)));

        // Entry #3
        entries.add(new LootTableEntry(15, BlockType.STICK, new SetCount(8)));

        // Entry #4
        entries.add(new LootTableEntry(15, BlockType.DIORITE, new SetCount(16)));

        // Entry #5
        entries.add(new LootTableEntry(15, BlockType.AMETHYST_BLOCK, new SetCount(16)));

        // Entry #6
        entries.add(new LootTableEntry(15, BlockType.IRON_INGOT, new SetCount(8)));

        // Entry #7
        entries.add(new LootTableEntry(14, BlockType.ARROW, new SetCount(10)));

        // Entry #8
        entries.add(new LootTableEntry(11, BlockType.BONE, new SetCount(3, new RandomChance(0.4))));

        // Entry #9
        entries.add(new LootTableEntry(10, BlockType.COPPER_BLOCK, new SetCount(16)));

        // Entry #10
        entries.add(new LootTableEntry(10, BlockType.BREAD, new SetCount(5)));

        // Entry #11
        entries.add(new LootTableEntry(10, BlockType.COBWEB, new SetCount(2, new RandomChance(0.4))));

        // Entry #12
        Enchantments enchantment = new Enchantments(EnchantmentType.LURE, 3);
        entries.add(new LootTableEntry(8, BlockType.FISHING_ROD, new SetComponents(enchantment)));

        // Entry #13
        entries.add(new LootTableEntry(8, BlockType.OBSIDIAN, new SetCount(4)));

        // Entry #14
        entries.add(new LootTableEntry(7, BlockType.GLASS, new SetCount(3)));

        // Entry #15
        entries.add(new LootTableEntry(7, BlockType.MELON_SLICE, new SetCount(3, new RandomChance(0.4))));

        // Entry #16
        entries.add(new LootTableEntry(5, BlockType.TNT, new SetCount(4)));

        // Entry #17
        entries.add(new LootTableEntry(5, BlockType.EXPERIENCE_BOTTLE, new SetCount(3, new RandomChance(0.2))));

        // Entry #18
        entries.add(new LootTableEntry(5, BlockType.BOOK));

        // Entry #19
        entries.add(new LootTableEntry(5, BlockType.REDSTONE, new SetCount(16)));

        // Entry #20
        entries.add(new LootTableEntry(5, BlockType.GUNPOWDER, new SetCount(16)));

        // Entry #21
        entries.add(new LootTableEntry(5, BlockType.GOLD_INGOT, new SetCount(4, new RandomChance(0.3))));

        // Entry #22
        entries.add(new LootTableEntry(5, BlockType.LAPIS_LAZULI, new SetCount(10)));

        // Entry #23
        entries.add(new LootTableEntry(4, BlockType.LAVA_BUCKET));

        // Entry #24
        entries.add(new LootTableEntry(4, BlockType.APPLE, new SetCount(2, new RandomChance(0.3))));

        // Entry #25
        entries.add(new LootTableEntry(2, BlockType.DIAMOND, new SetCount(2, new RandomChance(0.3))));

        // Entry #26
        entries.add(new LootTableEntry(3, BlockType.SADDLE));

        // Entry #27
        entries.add(new LootTableEntry(3, BlockType.SPECTRAL_ARROW, new SetCount(10)));

        // Entry #28
        ArrayList<Attributes> attributes = new ArrayList<>();
        attributes.add(new JumpStrength(1));
        attributes.add(new MovementSpeed(0.2));
        String text = "Driftwood";
        EntityData horse = new Horse(60, true, 5, text, attributes);

        SetName name = new SetName(new Text(false, false, "Driftwood's return"));

        functions.add(new SetComponents(horse));
        functions.add(name);

        entries.add(new LootTableEntry(10, BlockType.HORSE_SPAWN_EGG, functions));
        functions = new ArrayList<>();

        // Entry #29
        entries.add(new LootTableEntry(3, BlockType.GLOWSTONE_DUST, new SetCount(6)));

        // Entry #30
        entries.add(new LootTableEntry(3, BlockType.ENDER_PEARL, new SetCount(2, new RandomChance(0.5))));

        // Entry #31
        entries.add(new LootTableEntry(2, BlockType.NETHER_WART, new SetCount(5)));

        // Entry #32
        entries.add(new LootTableEntry(2, BlockType.BLAZE_ROD, new SetCount(2, new RandomChance(0.1))));

        // Entry #33
        entries.add(new LootTableEntry(2, BlockType.GOLDEN_APPLE));

        // Entry #34
        entries.add(new LootTableEntry(2, BlockType.ANVIL));

        // Entry #35
        entries.add(new LootTableEntry(4, BlockType.SPYGLASS));

        // Entry #36
        entries.add(new LootTableEntry(2, BlockType.WOLF_SPAWN_EGG, new SetCount(2, new RandomChance(0.01))));

        // Entry #37
        entries.add(new LootTableEntry(1, BlockType.DIAMOND_HORSE_ARMOR));

        // Entry #38
        entries.add(new LootTableEntry(1, BlockType.NETHERITE_HOE));

        // Entry #39
        enchantment = new Enchantments(EnchantmentType.LOYALTY, 3);
        entries.add(new LootTableEntry(1, BlockType.TRIDENT, new SetComponents(enchantment)));

        // Entry #40
        entries.add(new LootTableEntry(1, BlockType.NETHERITE_UPGRADE_SMITHING_TEMPLATE));

        // Entry #42
        RandomChance condition = new RandomChance(0.001);
        entries.add(new LootTableEntry(1, BlockType.NETHERITE_SCRAP, new SetCount(4, condition)));

        // Entry #43
        PotionContents contents = new PotionContents(Effect.LUCK, 0, 600, "59C106", true, false, true);
        name = new SetName(new Text(false, false, "Potion of Care Package luck"));

        functions.add(new SetComponents(contents));
        functions.add(name);

        entries.add(new LootTableEntry(2, BlockType.SPLASH_POTION, functions));
        functions = new ArrayList<>();

        // Entry #44
        contents = new PotionContents(Effect.POISON, 0, 5, "4E9331", false, true, true);
        name = new SetName(new Text(false, false, "Potion of Poison"));

        functions.add(new SetComponents(contents));
        functions.add(name);

        entries.add(new LootTableEntry(2, BlockType.SPLASH_POTION, functions));
        functions = new ArrayList<>();

        // Entry #45
        contents = new PotionContents(Effect.BLINDNESS, 0, 10, "1F1F23", false, true, true);
        MaxStackSize stack = new MaxStackSize(64);
        name = new SetName(new Text(false, false, "Potion of Blindness"));
        SetCount count = new SetCount(5, new RandomChance(0.3));

        components.add(contents);
        components.add(stack);

        functions.add(new SetComponents(components));
        functions.add(name);
        functions.add(count);

        entries.add(new LootTableEntry(2, BlockType.SPLASH_POTION, functions));
        functions = new ArrayList<>();
        components = new ArrayList<>();

        // Entry #46
        attributes = new ArrayList<>();
        attributes.add(new JumpStrength(0.7));
        attributes.add(new MovementSpeed(0.34));
        text = "Scuderia";
        horse = new Horse(5, true, 4, text, attributes);

        name = new SetName(new Text(false, false, "Grazie Ragazzi"));

        functions.add(new SetComponents(horse));
        functions.add(name);

        entries.add(new LootTableEntry(2, BlockType.HORSE_SPAWN_EGG, functions));
        functions = new ArrayList<>();

        // Entry #47
        // Set title and author
        String title = "Terraria [Ep1]";
        String author = "Mr9Madness";

        // Compile pages
        ArrayList<ArrayList<TextItem>> pages = new ArrayList<>();
        ArrayList<TextItem> texts = new ArrayList<>();
        texts.add(new Text(false, false, "Alright, guys, welcome to a new Let's Play!\\n\\n"));
        texts.add(new Text(false, false, "We're starting a new Terraria playthrough with a fresh character—of course, blue hair and blue eyes, because why not? Naming him "));
        texts.add(new Text(false, true, "Mr9Madness."));

        pages.add(texts);
        texts = new ArrayList<>();

        texts.add(new Text(false, false, "Creating a new large world... let’s call it "));
        texts.add(new Text(false, true, "YouTube"));
        texts.add(new Text(false, false, ". The game is generating everything—placing dirt, caves, dungeons, and, yes, a big jungle."));

        pages.add(texts);
        texts = new ArrayList<>();

        texts.add(new Text(false, false, "And we’re in!\\n" +
                "\\n" +
                "Terraria is all about surviving, like Minecraft, but 2D. First things first: gathering resources. I see a bunny... well, first blood goes to the bunny, as always."));

        pages.add(texts);
        texts = new ArrayList<>();

        texts.add(new Text(false, false, "Snodog627, I'm not saying your name!\\n" +
                "\\n" +
                "Yeah, someone I know doesn’t want me to play this game because he hates it. Too bad, I love it."));

        pages.add(texts);
        texts = new ArrayList<>();

        texts.add(new Text(false, false, "Exploring underground...\\n" +
                "\\n" +
                "Found silver, platinum—wait, normally platinum replaces silver? Whatever, I’ll take it. Died over here earlier, but let’s just jump down again, probably a smart move, right?\\n"));

        pages.add(texts);
        texts = new ArrayList<>();

        texts.add(new Text(false, false, "Oh, a hook!\\n" +
                "\\n" +
                "That’s rare! And a cloud in a bottle? First episode luck! Now I can double-jump. Also got a Suspicious Looking Eye—first boss summon ready.\\n"));

        pages.add(texts);
        texts = new ArrayList<>();

        texts.add(new Text(false, false, "Building my first house.\\n" +
                "\\n" +
                "It's going underground, don't ask why. Also, fireflies are in the game now—no idea what they do, but cool. Need more wood for a hammer, so time to chop trees.\\n"));

        pages.add(texts);
        texts = new ArrayList<>();

        texts.add(new Text(false, false, "Exploring further…\\n" +
                "\\n" +
                "Found a boomerang! Oh, and... "));
        texts.add(new Text(false, true, "corruption"));
        texts.add(new Text(false, false, ". Yeah, nope. "));
        texts.add(new Text(false, true, "Get banned!"));
        texts.add(new Text(false, false, " Not dealing with that right now.\\n" +
                "\\n" +
                "That’s it for the first episode! See you next time!"));

        pages.add(texts);
        texts = new ArrayList<>();

        components.add(new WrittenBookContent(title, author, pages));
        entries.add(new LootTableEntry(1, BlockType.WRITTEN_BOOK, new SetComponents(components)));
        components = new ArrayList<>();

        // Entry #48
        title = "The Diorite Experts UHC S01";
        author = "Snodog627";

        pages = new ArrayList<>();

        texts.add(new Text(false, false, "Alright, welcome everyone to The Diorite Experts UltraHardCore (UHC) Season 1!\\n" +
                "\\n" +
                "We’ve got random teams of three competing in this season."));

        pages.add(texts);
        texts = new ArrayList<>();


        texts.add(new Text(false, false, "Here are the teams:\\n" +
                "Team Red: Snodog627 and PR0BA.\\n" +
                "Team Lime: Tiba101 and WarriorJeroen (a.k.a. SGT_Prostidude).\\n" +
                "Team Blue: BananaKid99 (a.k.a. Mr9Madness) and S3R91."));

        pages.add(texts);
        texts = new ArrayList<>();

        texts.add(new Text(false, false, "Now, let’s go over the rules as usual:\\n" +
                "Regeneration potions are banned.\\n" +
                "No strip mining allowed.\\n" +
                "If you’re near (0,0), you have to stay there—escape is only allowed through the Nether."));

        pages.add(texts);
        texts = new ArrayList<>();

        texts.add(new Text(false, false, "Everyone clear on that? Good.\\n" +
                "\\n" +
                "Alright, let's talk predictions for this UHC! Who do you think is going to win?\\n" +
                "\\n" +
                "Oh, we’re making predictions already? Okay!"));

        pages.add(texts);
        texts = new ArrayList<>();

        texts.add(new Text(false, false, "Wait, does everyone have their starting positions? Is everyone set?\\n" +
                "\\n" +
                "Yeah, I think we're good to go!\\n" +
                "\\n" +
                "Alright then—let’s get started! Good luck, everyone!"));

        pages.add(texts);
        texts = new ArrayList<>();

        components.add(new WrittenBookContent(title, author, pages));
        entries.add(new LootTableEntry(1, BlockType.WRITTEN_BOOK, new SetComponents(components)));
        components = new ArrayList<>();

        // Entry #49
        entries.add(new LootTableEntry(2, BlockType.WIND_CHARGE, new SetCount(5)));

        // Make loot table
        LootTable lTable = new LootTable(type, rolls, bonusRolls, entries);

        ArrayList<String> fileCommands = new ArrayList<>();
        fileCommands.add(lTable.generateLootTable());

        FileData fileData = new FileData("supply_drop", fileCommands, "loot_table");
        files.add(fileData);

        // Percentages
        fileCommands = new ArrayList<>();
        fileCommands.add(lTable.GenerateRates());
        fileData = new FileData("supply_drop_rates", fileCommands, "loot_table");
        files.add(fileData);
    }

    private void definePlugins() throws IOException {
        plugins.add(new PaperPlugin("ViaVersion-5.4.2.jar", OperationMode.otherVersions, "ViaBackwards-5.4.2.jar"));
        plugins.add(new PaperPlugin("spark-1.10.119-bukkit.jar", OperationMode.debug));
        plugins.add(new PaperPlugin("Chunky-Bukkit-1.4.28.jar", OperationMode.debug));
        plugins.add(new PaperPlugin("openaudiomc-6.10.7.jar", OperationMode.proximity, "OpenAudioMc\\"));

        fileTools.copyPlugins(plugins);
    }

    // Get by name functions
    private BossBar getBossbarByName(String name) {
        return bossBars.stream().filter(bossBar -> name.equals(bossBar.getName())).findAny().orElse(null);
    }

    public ScoreboardObjective getObjectiveByName(String name) {
        return scoreboardObjectives.stream().filter(objective -> name.equals(objective.getName())).findAny().orElse(null);
    }

    public ScoreboardObjective getObjectiveByName(Objective name) {
        return scoreboardObjectives.stream().filter(objective -> name.toString().equals(objective.getName())).findAny().orElse(null);
    }


    // Create function files
    private void makeFunctionFiles() {
        // Developer mode
        files.add(Initialize());
        files.add(DeveloperMode());
        files.add(GetStartPotions());
        files.add(DeveloperPotionControl());
        files.add(ClearEnderChest());
        files.add(DisplayRank());
        files.add(ClearSchedule());
        files.add(DebugGive());
        files.add(DebugRemove());

        // Game start up
        for (int i = 1; i < 9; i++) {
            files.add(RandomTeams(i));
        }
        files.add(Predictions());
        files.add(PredictionsLoop());
        files.add(IntoCalls());
        files.add(SpreadPlayers());
        files.add(SurvivalMode());
        files.add(StartGame());
        files.add(GameStart.GameStarter());

        // Timers
        Update Updating = new Update();
        files.add(Updating.TimerMain1());
        files.add(Updating.TimerMain5());
        files.add(Updating.TimerMain20());
        files.add(Updating.TimerDeveloper20());

        // Messages
        files.add(ScheduleSingleMessages());
        files.add(MessagePVP());
        if (OperationMode.eternalDay) {
            files.add(MessageEternalDay());
        }

        // Updates
        files.add(HorseFrostWalker());
        files.add(UpdateSidebar());
        files.add(RemoveBannedItems());
        files.add(DisplayQuotes());
        files.add(UpdateMineCount());
        files.add(LocateTeammate());
        files.add(WolfUpdates());
        files.add(AnnounceIronMan());
        files.add(CheckIronMan());

        // Player death
        files.add(DropPlayerHeads());
        files.add(RespawnPlayer());
        files.add(UpdateMinHealth());
        files.add(DisableRespawn());
        files.add(PlayerDeathHandler());

        // Victory
        for (int i = 1; i < 3; i++) {
            files.add(Minute(i));
        }
        files.add(TeamsAliveCheck());
        files.add(Victory());
        for (int i = 0; i < teams.size(); i++) {
            files.add(VictoryMessage(teams.get(i), i));
        }
        files.add(InitiateDeathMatch());
        files.add(DeathMatch());

        // Care Packages
        if (OperationMode.carePackages) {
            files.add(DropCarepackages());
        }

        // Control Points
        if (OperationMode.controlPoints) {
            files.add(Updating.TimerControlPoint5());
            files.add(Updating.TimerControlPoint20());
            files.add(SpawnControlPoints());
            files.add(BossBarValue());
            files.add(InitializeControlpoint());
            files.add(SecondControlpoint());
            files.add(ControlPointCaptured());
            for (int i = 1; i < controlPoints.size() + 1; i++) {
                files.add(Controlpoint(i));
                files.add(ControlPointTag(i));
                files.add(ControlPointScore(i));
                files.add(ControlPointMessages(i));
            }
            files.add(ControlPointPerks());
            files.add(UpdatePublicCPScore());
            files.add(TeamScore());
            files.add(TeamsHighscoreCheck());
        }

        // Traitor Faction
        if (OperationMode.traitorFaction) {
            files.add(Updating.TimerTraitor5());
            files.add(Updating.TimerTraitor20());
            files.add(TraitorHandout());
            files.add(TraitorActionBar());
            files.add(VictoryTraitor());
            files.add(TraitorCheck());
        }

        // In game teams
        if (OperationMode.teamCreationInGame) {
            files.add(JoinTeam());
            files.add(UpdatePlayerDistance());
            files.add(VictoryMessageSolo());
        }

        // Misc
        files.add(EquipGear());
        files.add(GodMode());
        files.add(BattleRoyale());
    }

    private FileData Initialize() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Set gamerules
        for (Dimension dimension : Dimension.values()) {
            fileCommands.add(Execute.In(dimension) +
                    CommandBuilder.setGameRule(GameRule.naturalRegeneration, false));
        }
        fileCommands.add(CommandBuilder.setGameRule(GameRule.doImmediateRespawn, true));
        fileCommands.add(CommandBuilder.setGameRule(GameRule.doPatrolSpawning, false));
        fileCommands.add(CommandBuilder.setGameRule(GameRule.doMobSpawning, false));
        fileCommands.add(CommandBuilder.setGameRule(GameRule.doWeatherCycle, false));

        // Set difficulty
        fileCommands.add(CommandBuilder.setDifficulty(Difficulty.hard));

        // Set default gamemode
        fileCommands.add(CommandBuilder.setDefaultGameMode(GameMode.adventure));

        // Set world spawn
        fileCommands.add(CommandBuilder.setWorldSpawn(new Coordinate(0, 221, 0)));

        // Create scoreboard objectives
        for (ScoreboardObjective objective : scoreboardObjectives) {
            fileCommands.add(objective.add());
        }
        fileCommands.add(new ScoreboardObjective().setDisplay(ScoreboardLocation.below_name, Objective.Hearts));
        fileCommands.add(new ScoreboardObjective().setDisplay(ScoreboardLocation.list, Objective.Hearts));

        // Create teams
        for (Team t : teams) {
            fileCommands.add(t.add());
            fileCommands.add(t.setTeamColor());
        }

        // Create staging area
        fileCommands.add(Execute.In(Dimension.overworld) +
                CommandBuilder.fill(-6, 220, -6, 6, 226, 6, BlockType.BARRIER));
        fileCommands.add(Execute.In(Dimension.overworld) +
                CommandBuilder.fill(-5, 221, -5, 5, 226, 5, BlockType.AIR));
        fileCommands.add(Execute.In(Dimension.overworld) +
                CommandBuilder.setBlock(0, 222, -5, BlockType.CHERRY_WALL_SIGN + "[facing=south,waterlogged=false]{back_text:{messages:[\"You have\",\"angered\",\"the Gods!\",\"\"]},front_text:{messages:[{\"text\":\"In rememberance\",\"click_event\":{\"action\":\"run_command\",\"command\":\"" + CommandBuilder.summonEntity(EntityType.FIREWORK_ROCKET, new Coordinate(0, 0, 0, ReferenceFrame.relative)) + "\"}},\"of our\",\"Command Center\",\"2014-2025\"]},is_waxed:0b}"));

        // Control Point
        if (OperationMode.controlPoints) {
            // Create bossbars
            fileCommands.add(getBossbarByName("cp1").remove());
            fileCommands.add(getBossbarByName("cp2").remove());
            fileCommands.add(getBossbarByName("cp1").add(controlPoints.get(0).getName() + ": " + controlPoints.get(0).getCoordinate().getX() + ", " + controlPoints.get(0).getCoordinate().getY() + ", " + controlPoints.get(0).getCoordinate().getZ() + " (" + controlPoints.get(0).getCoordinate().getDimensionName() + ")"));
            fileCommands.add(getBossbarByName("cp1").setMax(controlPoints.get(0).getMaxVal()));
            fileCommands.add(getBossbarByName("cp2").add(controlPoints.get(1).getName() + " soon: " + controlPoints.get(1).getCoordinate().getX() + ", " + controlPoints.get(1).getCoordinate().getY() + ", " + controlPoints.get(1).getCoordinate().getZ() + " (" + controlPoints.get(1).getCoordinate().getDimensionName() + ")"));
            fileCommands.add(getBossbarByName("cp2").setMax(controlPoints.get(1).getMaxVal()));

            // Scoreboard objectives
            for (Team t: teams) {
                for (int i = 1; i < controlPoints.size() + 1; i++) {
                    scoreboardObjectives.add(new ScoreboardObjective(Objective.CP.extendName(i + t.getName()), ObjectiveType.dummy));
                    fileCommands.add(scoreboardObjectives.get(scoreboardObjectives.size() - 1).add());
                }
            }
        }


        return new FileData(FileName.initialize, fileCommands);
    }

    private FileData PlayerDeathHandler() {
        ArrayList<String> fileCommands = new ArrayList<>();
        ArrayList<TextItem> texts = new ArrayList<>();

        // Play thunder sound
        fileCommands.add(CommandBuilder.playSound(Sound.THUNDER, SoundSource.master, "@a", "~", "~50", "~", "100", "1", "0"));

        // Set all dead players to spectator mode
        fileCommands.add(CommandBuilder.setGameMode(GameMode.spectator, "@a[scores={Deaths=1},gamemode=!spectator]"));

        // Reset player with lowest health
        fileCommands.add(scoreboard.Set(Constant.admin, getObjectiveByName(Objective.MinHealth), 20));

        // Add respawn tag to players who die in the first 20 minutes
        fileCommands.add(Execute.Unless("@e[tag=" + Tag.RespawnDisabled + "]") +
                CommandBuilder.addTag("@p[scores={Deaths=1}]", Tag.Respawn));

        // Drop player head
        fileCommands.add(Schedule.callFunction(FileName.drop_player_heads));

        // Do automatic respawn in the first 20 minutes
        fileCommands.add(Execute.Unless("@e[tag=" + Tag.RespawnDisabled + "]") +
                Schedule.callFunction(FileName.respawn_player, 5, Duration.TICKS));

        // Control Point
        if (OperationMode.controlPoints) {
            // Reset scores
            for (int i = 0; i < 2; i++) {
                fileCommands.add(scoreboard.Set("@a[scores={Deaths=1}]", getObjectiveByName(Objective.ControlPoint.extendName(i + 1)), 0));
                fileCommands.add(scoreboard.Set(Constant.admin, getObjectiveByName(Objective.Highscore.extendName(i + 1)), 1));
            }
        }

        // Traitor Faction
        if (OperationMode.traitorFaction) {
            // Announce traitor deaths
            texts.add(bannerText);
            texts.add(new Text(Color.red, true, false, "A TRAITOR HAS BEEN ELIMINATED"));
            texts.add(bannerText);
            texts.add(new Text(Color.gold, true, false, "WELL DONE"));
            texts.add(bannerText);
            fileCommands.add(Execute.If(new Entity("@p[scores={Deaths=1},tag=" + Tag.Traitor + "]")) +
                    new TellRaw("@a", texts).sendRaw());
            texts.clear();
        }

        // In-game teams
        if (OperationMode.teamCreationInGame) {
            // Do not allow killers to form a team
            String killer = "@p[team=,scores={TempKills=1}]";
            String dead = "@p[team=,scores={Deaths=1}]";

            texts.add(new Text(Color.red, true, false, "Looks like you do not want a teammate."));
            fileCommands.add(Execute.If(killer, false) +
                    Execute.IfNext(dead) +
                    Execute.UnlessNext(killer, Objective.IsKiller, 1, true) +
                    new TellRaw(killer, texts).sendRaw());
            texts.clear();

            fileCommands.add(Execute.If(killer, false) +
                    Execute.IfNext(dead, true) +
                    scoreboard.Set(killer, Objective.IsKiller, 1));

            // Reset temporary kill count
            fileCommands.add(scoreboard.Reset("@p[scores={TempKills=1}]", Objective.TempKills));
        }

        // Reset death count
        fileCommands.add(scoreboard.Reset("@p[scores={Deaths=1}]", Objective.Deaths));

        return new FileData(FileName.handle_player_death, fileCommands);
    }

    private FileData DropPlayerHeads() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Summon a player head upon dying
        for (Player p : players) {
            fileCommands.add(Execute.At(new Entity("@p[name=" + p.getPlayerName() + ",scores={Deaths=1}]")) +
                    CommandBuilder.summonEntity(EntityType.ITEM, "{Item:{id:\"" + BlockType.PLAYER_HEAD + "\",count:1,components:{\"minecraft:profile\":{name:" + p.getPlayerName() + "}}}}"));
        }

        return new FileData(FileName.drop_player_heads, fileCommands);
    }

    private FileData BossBarValue() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Players in a team
        for (Team t : teams) {
            // Bossbar
            fileCommands.add(Execute.If(Constant.adminSingle, getObjectiveByName(Objective.CP.toString() + 1 + t.getName()), ComparatorType.GREATER, Constant.adminSingle, getObjectiveByName(Objective.Highscore.extendName(1))) +
                    getBossbarByName("cp1").setColor(t.getBossbarColor()));
            fileCommands.add(Execute.If(Constant.adminSingle, getObjectiveByName(Objective.CP.toString() + 2 + t.getName()), ComparatorType.GREATER, "@e[limit=1,scores={Highscore1=" + cp2ActivationScore + "..}]", getObjectiveByName(Objective.Highscore.extendName(2))) +
                    getBossbarByName("cp2").setColor(t.getBossbarColor()));

            // Locator bar
            fileCommands.add(Execute.If(Constant.adminSingle, getObjectiveByName(Objective.CP.toString() + 1 + t.getName()), ComparatorType.GREATER, Constant.adminSingle, getObjectiveByName(Objective.Highscore.extendName(1))) +
                    CommandBuilder.modifyWaypointColor("@n[tag=" + Tag.CP.extendName(1) + "]", t.getColor()));
            fileCommands.add(Execute.If(Constant.adminSingle, getObjectiveByName(Objective.CP.toString() + 2 + t.getName()), ComparatorType.GREATER, "@e[limit=1,scores={Highscore1=" + cp2ActivationScore + "..}]", getObjectiveByName(Objective.Highscore.extendName(2))) +
                    CommandBuilder.modifyWaypointColor("@n[tag=" + Tag.CP.extendName(2) + "]", t.getColor()));

            // Scoreboard objective
            for (int i = 0; i < controlPoints.size(); i++) {
                fileCommands.add(scoreboard.Operation(Constant.admin, getObjectiveByName(Objective.Highscore.extendName(i + 1)), ComparatorType.GREATER, Constant.admin, getObjectiveByName("" + Objective.CP + (i + 1) + t.getName())));
            }
        }

        // Individual players
        // Bossbar
        fileCommands.add(Execute.If("@r[limit=1,team=]", getObjectiveByName(Objective.ControlPoint.extendName(1)), ComparatorType.GREATER, Constant.adminSingle, getObjectiveByName(Objective.Highscore.extendName(1))) +
                getBossbarByName("cp1").setColor(BossBarColor.white));
        fileCommands.add(Execute.If("@r[limit=1,team=]", getObjectiveByName(Objective.ControlPoint.extendName(2)), ComparatorType.GREATER, "@e[scores={Highscore1=" + cp2ActivationScore + "..},limit=1]", getObjectiveByName(Objective.Highscore.extendName(2))) +
                getBossbarByName("cp2").setColor(BossBarColor.white));

        // Locator bar
        fileCommands.add(Execute.If("@r[limit=1,team=]", getObjectiveByName(Objective.ControlPoint.extendName(1)), ComparatorType.GREATER, Constant.adminSingle, getObjectiveByName(Objective.Highscore.extendName(1))) +
                CommandBuilder.modifyWaypointColor("@n[tag=" + Tag.CP.extendName(1) + "]"));
        fileCommands.add(Execute.If("@r[limit=1,team=]", getObjectiveByName(Objective.ControlPoint.extendName(2)), ComparatorType.GREATER, "@e[scores={Highscore1=" + cp2ActivationScore + "..},limit=1]", getObjectiveByName(Objective.Highscore.extendName(2))) +
                CommandBuilder.modifyWaypointColor("@n[tag=" + Tag.CP.extendName(2) + "]"));

        // Scoreboard objective
        for (int i = 0; i < controlPoints.size(); i++) {
            fileCommands.add(scoreboard.Operation(Constant.admin, getObjectiveByName(Objective.Highscore.extendName(i + 1)), ComparatorType.GREATER, "@r[limit=1,team=]", getObjectiveByName(Objective.ControlPoint.extendName(i + 1))));
        }

        // Update value of bossbars
        fileCommands.add(Execute.Store(ExecuteStore.result, getBossbarByName("cp1"), BossBarStore.value) +
                scoreboard.Get(Constant.adminSingle, getObjectiveByName(Objective.Highscore.extendName(1))));
        fileCommands.add(Execute.Store(ExecuteStore.result, getBossbarByName("cp2"), BossBarStore.value) +
                scoreboard.Get("@e[limit=1,scores={Highscore1=" + cp2ActivationScore + "..}]", getObjectiveByName(Objective.Highscore.extendName(2))));

        return new FileData(FileName.bbvalue, fileCommands);
    }

    private FileData ClearEnderChest() {
        ArrayList<String> fileCommands = new ArrayList<>();
        for (int i = 0; i < chestSize; i++) {
            fileCommands.add(CommandBuilder.replaceItem("@a", InventorySlot.ENDERCHEST.setSlotNumber(i), BlockType.AIR, 1));
        }

        return new FileData(FileName.clear_enderchest, fileCommands);
    }

    private FileData EquipGear() {
        ArrayList<String> fileCommands = new ArrayList<>();
        fileCommands.add(CommandBuilder.replaceItem("@a", InventorySlot.CHEST, BlockType.IRON_CHESTPLATE));
        fileCommands.add(CommandBuilder.replaceItem("@a", InventorySlot.FEET, BlockType.IRON_BOOTS));
        fileCommands.add(CommandBuilder.replaceItem("@a", InventorySlot.HEAD, BlockType.IRON_HELMET));
        fileCommands.add(CommandBuilder.replaceItem("@a", InventorySlot.LEGS, BlockType.IRON_LEGGINGS));
        fileCommands.add(CommandBuilder.replaceItem("@a", InventorySlot.OFFHAND, BlockType.SHIELD));
        fileCommands.add(CommandBuilder.replaceItem("@a", InventorySlot.MAINHAND, BlockType.IRON_AXE));
        fileCommands.add(CommandBuilder.replaceItem("@a", InventorySlot.INVENTORY.setSlotNumber(0), BlockType.IRON_SWORD));
        fileCommands.add(CommandBuilder.giveEffect("@a", Effect.REGENERATION, 1, 255, true));

        return new FileData(FileName.equip_gear, fileCommands);
    }

    private FileData GodMode() {
        ArrayList<String> fileCommands = new ArrayList<>();
        fileCommands.add(CommandBuilder.giveEffect("@s", Effect.RESISTANCE, 99999, 4, true));
        fileCommands.add(CommandBuilder.replaceItem("@s", InventorySlot.MAINHAND, BlockType.TRIDENT + "[custom_name=[{\"bold\":false,\"color\":\"white\",\"italic\":false,\"obfuscated\":true,\"text\":\"aA\"},{\"bold\":true,\"color\":\"#8C3CC1\",\"obfuscated\":false,\"text\":\"The\"},{\"bold\":true,\"color\":\"#E280FF\",\"obfuscated\":false,\"text\":\" Impaler \"},{\"color\":\"white\",\"obfuscated\":true,\"text\":\"Aa\"}],lore=[\"This holy weapon impales anything it touches\"],damage=0,enchantments={\"" + EnchantmentType.FIRE_ASPECT + "\":255,\"" + EnchantmentType.SHARPNESS + "\":255,\"" + EnchantmentType.IMPALING + "\":255,\"" + EnchantmentType.LOYALTY + "\":255,\"" + EnchantmentType.EFFICIENCY + "\":255},attribute_modifiers=[{id:\"" + AttributeType.ARMOR + "\",type:\"armor\",amount:1000,operation:\"add_value\",slot:\"armor\",display:{type:\"hidden\"}},{id:\"" + AttributeType.ATTACK_DAMAGE + "\",type:\"attack_damage\",amount:1000,operation:\"add_value\",slot:\"mainhand\",display:{type:\"hidden\"}}],unbreakable={}]"));

        return new FileData(FileName.god_mode, fileCommands);
    }

    private FileData GetStartPotions() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Clear inventory
        fileCommands.add(CommandBuilder.clearInventory("@s"));

        // Give potions
        if (!OperationMode.teamCreationInGame) {
            fileCommands.add(CommandBuilder.giveSplashPotion("@s", 0, Effect.SPEED, "808080", "Developer Mode", "Set operational mode to Developer Mode."));
            fileCommands.add(CommandBuilder.giveSplashPotion("@s", 1, Effect.WEAKNESS, "FF9933", "Assign Teams", "Assign players to teams."));
            fileCommands.add(CommandBuilder.giveSplashPotion("@s", 2, Effect.SLOW_FALLING, "6633CC", "Predictions", "Who will win this season?."));
            fileCommands.add(CommandBuilder.giveSplashPotion("@s", 3, Effect.INVISIBILITY, "3399FF", "Into Calls", "Allow players to gather in their Discord channel."));
            fileCommands.add(CommandBuilder.giveSplashPotion("@s", 4, Effect.POISON, "00CC66", "Spread players", "Spread players across the map."));
            fileCommands.add(CommandBuilder.giveSplashPotion("@s", 5, Effect.STRENGTH, "CC3333", "Survival Mode", "Set operational mode to Ready to Play."));
            fileCommands.add(CommandBuilder.giveSplashPotion("@s", 6, Effect.SLOWNESS, "00FF7F", "Start Game", "Start the game. Good luck!"));
        } else {
            fileCommands.add(CommandBuilder.giveSplashPotion("@s", 0, Effect.SPEED, "808080", "Developer Mode", "Set operational mode to Developer Mode."));
            fileCommands.add(CommandBuilder.giveSplashPotion("@s", 1, Effect.SLOW_FALLING, "6633CC", "Predictions", "Who will win this season?."));
            fileCommands.add(CommandBuilder.giveSplashPotion("@s", 2, Effect.INVISIBILITY, "3399FF", "Into Calls", "Allow players to gather in their Discord channel."));
            fileCommands.add(CommandBuilder.giveSplashPotion("@s", 3, Effect.POISON, "00CC66", "Spread players", "Spread players across the map."));
            fileCommands.add(CommandBuilder.giveSplashPotion("@s", 4, Effect.STRENGTH, "CC3333", "Survival Mode", "Set operational mode to Ready to Play."));
            fileCommands.add(CommandBuilder.giveSplashPotion("@s", 5, Effect.SLOWNESS, "00FF7F", "Start Game", "Start the game. Good luck!"));
        }

        return new FileData(FileName.start_potions, fileCommands);
    }

    private FileData DeveloperMode() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Create marker entity
        fileCommands.add(CommandBuilder.killEntity(Constant.admin));
        fileCommands.add(CommandBuilder.summonEntity(EntityType.MARKER, new Coordinate(0, Constant.worldBottom, 0), "{CustomName:\"Admin\"}"));

        // Set time
        fileCommands.add(CommandBuilder.setTime(0));

        // Set gamerules
        fileCommands.add(CommandBuilder.setGameRule(GameRule.commandBlockOutput, true));
        fileCommands.add(CommandBuilder.setGameRule(GameRule.doDaylightCycle, false));
        fileCommands.add(CommandBuilder.setGameRule(GameRule.keepInventory, true));
        fileCommands.add(CommandBuilder.setGameRule(GameRule.doMobSpawning, false));
        fileCommands.add(CommandBuilder.setGameRule(GameRule.doTileDrops, false));
        fileCommands.add(CommandBuilder.setGameRule(GameRule.drowningDamage, false));
        fileCommands.add(CommandBuilder.setGameRule(GameRule.fallDamage, false));
        fileCommands.add(CommandBuilder.setGameRule(GameRule.fireDamage, false));
        fileCommands.add(CommandBuilder.setGameRule(GameRule.sendCommandFeedback, true));
        fileCommands.add(CommandBuilder.setGameRule(GameRule.doImmediateRespawn, true));
        fileCommands.add(CommandBuilder.setGameRule(GameRule.disableRaids, true));
        fileCommands.add(CommandBuilder.setGameRule(GameRule.doInsomnia, false));

        // Reset scores of all entities
        fileCommands.add(scoreboard.Reset("@e"));
                fileCommands.add(scoreboard.Set(Constant.admin, Objective.MinHealth, 20));
        fileCommands.add(scoreboard.Set(Constant.admin, Objective.Victory, 1));

        // Create jukebox at 0,0
        fileCommands.add(Execute.In(Dimension.overworld) +
                CommandBuilder.setBlock(startCoordinate, BlockType.JUKEBOX + "[has_record=true]{RecordItem:{Count:1b,id:\"" + BlockType.MUSIC_DISC_STAL + "\"}}", SetBlockType.replace));

        // Remove tags
        fileCommands.add(CommandBuilder.removeTag("@a", Tag.RespawnDisabled));
        fileCommands.add(CommandBuilder.removeTag("@a", Tag.IronManCandidate));
        fileCommands.add(CommandBuilder.removeTag("@a", Tag.IronMan));
        fileCommands.add(CommandBuilder.removeTag("@a", Tag.Respawn));
        fileCommands.add(CommandBuilder.removeTag(Constant.admin, Tag.GameStarted));

        // Set world border
        fileCommands.add(CommandBuilder.setWorldBorder(2 * world.getSize()));

        // Display ranks
        fileCommands.add(Schedule.callFunction(FileName.display_rank));

        // Set time dummy scoreboard entries
        fileCommands.add(scoreboard.Set("NightTime", getObjectiveByName(Objective.Time), 600));

        // Reset teams & solos
        for (Team t : teams) {
            fileCommands.add(t.emptyTeam());
        }

        // Reset player attributes
        fileCommands.add(CommandBuilder.setAttributeBaseMultiple("@a", AttributeType.SCALE, 1));
        fileCommands.add(CommandBuilder.setAttributeBaseMultiple("@a", AttributeType.WAYPOINT_TRANSMIT_RANGE, 0));

        // Set gamemode of player executing the command to creative
        fileCommands.add(CommandBuilder.setGameMode(GameMode.creative, "@s"));

        // Clear scheduled commands
        fileCommands.add(Schedule.callFunction(FileName.clear_schedule));

        // Clear all player effects
        fileCommands.add(CommandBuilder.clearEffect("@a"));

        // Give admin start potions
        fileCommands.add(Schedule.callFunction(FileName.start_potions));

        // Start timers
        fileCommands.add(Schedule.callFunction(FileName.timer_developer_20));

        // Care Packages
        if (OperationMode.carePackages) {
            // Set scoreboard dummies
            fileCommands.add(scoreboard.Set("CarePackages", getObjectiveByName(Objective.Time), 1200));

            // Remove tags
            fileCommands.add(CommandBuilder.removeTag(Constant.admin, Tag.CarePackagesDropped));
        }

        // Control Point
        if (OperationMode.controlPoints) {
            // Reset scoreboard objectives
            for (int i = 1; i < controlPoints.size() + 1; i++) {
                fileCommands.add(scoreboard.Set(Constant.admin, getObjectiveByName(Objective.Highscore.extendName(i)), 1));
                for (int ii = 1; ii < 3; ii++) {
                    fileCommands.add(scoreboard.Set("@a", getObjectiveByName(Objective.MSGDum.extendName(ii + "CP" + i)), 1));
                }
            }
            fileCommands.add(scoreboard.Reset("Solo", getObjectiveByName(Objective.CPScore)));
            for (Team t : teams) {
                fileCommands.add(scoreboard.Reset(t.getPlayerColor(), getObjectiveByName(Objective.CPScore)));
                fileCommands.add(t.joinTeam(t.getPlayerColor()));
            }

            // Set scoreboard dummies
            fileCommands.add(scoreboard.Set("Perk1", getObjectiveByName(Objective.CPScore), 3 * singleton.getMinToCPScore()));
            fileCommands.add(scoreboard.Set("Perk2", getObjectiveByName(Objective.CPScore), 6 * singleton.getMinToCPScore()));
            fileCommands.add(scoreboard.Set("Perk3", getObjectiveByName(Objective.CPScore), 12 * singleton.getMinToCPScore()));
            fileCommands.add(scoreboard.Set("Perk4", getObjectiveByName(Objective.CPScore), 15 * singleton.getMinToCPScore()));
            fileCommands.add(scoreboard.Set("TimeVictory", getObjectiveByName(Objective.CPScore), 20 * singleton.getMinToCPScore()));
            fileCommands.add(scoreboard.Set("ControlPoints", getObjectiveByName(Objective.Time), 1800));

            // Remove tags
            fileCommands.add(CommandBuilder.removeTag("@a", Tag.OnCP + "" + 1));
            fileCommands.add(CommandBuilder.removeTag("@a", Tag.OnCP + "" + 2));
            fileCommands.add(CommandBuilder.removeTag("@a", Tag.Capping + "" + 1));
            fileCommands.add(CommandBuilder.removeTag("@a", Tag.Capping + "" + 2));
            fileCommands.add(CommandBuilder.removeTag("@a", Tag.AttackingCP + "" + 1));
            fileCommands.add(CommandBuilder.removeTag("@a", Tag.AttackingCP + "" + 2));
            fileCommands.add(CommandBuilder.removeTag(Constant.admin, Tag.ControlPoint1Enabled));
            fileCommands.add(CommandBuilder.removeTag(Constant.admin, Tag.ControlPoint2Enabled));
            fileCommands.add(CommandBuilder.removeTag(Constant.admin, Tag.ControlPointCaptured));
            for (int i = 0; i < 4; i++) {
                fileCommands.add(CommandBuilder.removeTag("@a", Tag.ReceivedPerk.extendName(i + 1)));
            }

            // Spawn new Control Points
            fileCommands.add(Execute.In(controlPoints.get(0).getCoordinate().getDimension()) +
                    CommandBuilder.addForceLoad(controlPoints.get(0).getCoordinate().getX(), controlPoints.get(0).getCoordinate().getZ(), controlPoints.get(0).getCoordinate().getX(), controlPoints.get(0).getCoordinate().getZ()));
            fileCommands.add(Execute.In(controlPoints.get(1).getCoordinate().getDimension()) +
                    CommandBuilder.addForceLoad(controlPoints.get(1).getCoordinate().getX(), controlPoints.get(1).getCoordinate().getZ(), controlPoints.get(1).getCoordinate().getX(), controlPoints.get(1).getCoordinate().getZ()));
            fileCommands.add(Schedule.callFunction(FileName.spawn_control_points));
            fileCommands.add(Execute.In(controlPoints.get(0).getCoordinate().getDimension()) +
                    CommandBuilder.removeForceLoad(controlPoints.get(0).getCoordinate().getX(), controlPoints.get(0).getCoordinate().getZ(), controlPoints.get(0).getCoordinate().getX(), controlPoints.get(0).getCoordinate().getZ()));
            fileCommands.add(Execute.In(controlPoints.get(1).getCoordinate().getDimension()) +
                    CommandBuilder.removeForceLoad(controlPoints.get(1).getCoordinate().getX(), controlPoints.get(1).getCoordinate().getZ(), controlPoints.get(1).getCoordinate().getX(), controlPoints.get(1).getCoordinate().getZ()));

            // Reset bossbars
            BossBar bossBarCp1 = getBossbarByName("cp1");
            BossBar bossBarCp2 = getBossbarByName("cp2");
            fileCommands.add(bossBarCp1.setColor(BossBarColor.white));
            fileCommands.add(bossBarCp1.setVisible(false));
            fileCommands.add(bossBarCp1.setPlayers("@a"));
            fileCommands.add(bossBarCp1.setTitle(controlPoints.get(0).getName() + ": " + controlPoints.get(0).getCoordinate().getX() + ", " + controlPoints.get(0).getCoordinate().getY() + ", " + controlPoints.get(0).getCoordinate().getZ() + " (" + controlPoints.get(0).getCoordinate().getDimensionName() + ")"));
            fileCommands.add(bossBarCp2.setColor(BossBarColor.white));
            fileCommands.add(bossBarCp2.setVisible(false));
            fileCommands.add(bossBarCp2.setPlayers("@a"));
            fileCommands.add(bossBarCp2.setTitle(controlPoints.get(1).getName() + " soon: " + controlPoints.get(1).getCoordinate().getX() + ", " + controlPoints.get(1).getCoordinate().getY() + ", " + controlPoints.get(1).getCoordinate().getZ() + " (" + controlPoints.get(1).getCoordinate().getDimensionName() + ")"));

            // Kill waypoints
            for (ControlPoint controlPoint : controlPoints) {
                fileCommands.add(CommandBuilder.killEntity("@n[tag=" + controlPoint.getName() + "]"));
            }
            fileCommands.add(CommandBuilder.removeForceLoad());

        }

        // Traitor Faction
        if (OperationMode.traitorFaction) {
            // Set scoreboard dummies
            fileCommands.add(scoreboard.Set("TraitorFaction", getObjectiveByName(Objective.Time), 2400));

            // Remove tags
            fileCommands.add(CommandBuilder.removeTag("@a", Tag.Traitor));
            fileCommands.add(CommandBuilder.removeTag("@a", Tag.DontMakeTraitor));
            fileCommands.add(CommandBuilder.removeTag(Constant.admin, Tag.TraitorsAssigned));
        }

        // In-game team creation
        if (OperationMode.teamCreationInGame) {
            // Reset scoreboard objectives
            fileCommands.add(scoreboard.Set("@a", Objective.IsKiller, 0));
        }

        return new FileData(FileName.developer_mode, fileCommands);
    }

    private FileData RandomTeams(int i) {
        ArrayList<String> fileCommands = new ArrayList<>();
        for (Team t : teams) {
            fileCommands.add(t.joinTeam("@r[limit=" + i + ",team=]"));
        }

        return new FileData("" + FileName.random_teams + i, fileCommands);
    }

    private FileData Predictions() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Remove resistance and give regeneration
        fileCommands.add(CommandBuilder.clearEffect("@a"));
        fileCommands.add(CommandBuilder.giveEffect("@a", Effect.REGENERATION, 1, 255));

        // Make players fall
        fileCommands.add(CommandBuilder.addTag("@a[gamemode=!adventure]", Tag.IsFlying));
        fileCommands.add(CommandBuilder.setGameMode(GameMode.adventure, "@a[tag=" + Tag.IsFlying + "]"));
        fileCommands.add(CommandBuilder.setGameMode(GameMode.creative, "@a[tag=" + Tag.IsFlying + "]"));
        fileCommands.add(CommandBuilder.removeTag("@a[tag=" + Tag.IsFlying + "]", Tag.IsFlying));

        // Set death count for comparison
        fileCommands.add(scoreboard.Set("@a", Objective.Deaths, 0));

        // Teleport everyone underneath the world
        fileCommands.add(Execute.In(Dimension.overworld) +
                CommandBuilder.teleportEntity("@a", new Coordinate(0, -100, 0)));

        // Announcement message
        ArrayList<TextItem> texts = new ArrayList<>();
        texts.add(bannerText);
        texts.add(new Text(Color.gold, true, false, communityName + " UHC"));
        texts.add(bannerText);
        texts.add(new Text(Color.light_purple, true, false, "PREDICTIONS STARTED! GOOD LUCK"));
        texts.add(bannerText);
        fileCommands.add(new TellRaw("@a", texts).sendRaw());

        // Call predictions loop
        fileCommands.add(Schedule.callFunction(FileName.predictions_loop));

        return new FileData(FileName.predictions, fileCommands);
    }

    private FileData PredictionsLoop() {
        ArrayList<String> fileCommands = new ArrayList<>();
        ArrayList<TextItem> texts = new ArrayList<>();

        // Check if anyone has won
        if (!OperationMode.teamCreationInGame) {
            for (Team t : teams) {
                // Get tag that predictions have been completed
                fileCommands.add(Execute.If("@p[team=" + t.getName() + ",scores={Deaths=0}]", false) +
                        Execute.UnlessNext("@p[team=!" + t.getName() + ",scores={Deaths=0}]", true) +
                        CommandBuilder.addTag(Constant.admin, Tag.PredictionsCompleted));

                // Chat message
                texts.add(bannerText);
                texts.add(new Text(Color.gold, true, false, communityName + " UHC"));
                texts.add(bannerText);
                texts.add(new Text(t.getColor(), true, false, t.getJSONColor()));
                texts.add(new Text(Color.light_purple, true, false, " WILL WIN THE SEASON!"));
                texts.add(bannerText);

                fileCommands.add(Execute.If("@p[team=" + t.getName() + ",scores={Deaths=0}]", false) +
                        Execute.UnlessNext("@p[team=!" + t.getName() + ",scores={Deaths=0}]", true) +
                        new TellRaw("@a", texts).sendRaw());
                texts.clear();
            }
        }
        else {
            // Choose player as candidate for having won
            fileCommands.add(CommandBuilder.addTag("@r[team=,scores={Deaths=0}]", Tag.PredictionCandidate));

            // Get tag that predictions have been completed
            fileCommands.add(Execute.Unless("@p[tag=!" + Tag.PredictionCandidate + ",scores={Deaths=0}]") +
                    CommandBuilder.addTag(Constant.admin, Tag.PredictionsCompleted));

            // Chat message
            texts.add(bannerText);
            texts.add(new Text(Color.gold, true, false, communityName + " UHC"));
            texts.add(bannerText);
            texts.add(new Select("@p[tag=" + Tag.PredictionCandidate + "]"));
            texts.add(new Text(Color.light_purple, true, false, " WILL WIN THE SEASON!"));
            texts.add(bannerText);

            fileCommands.add(Execute.Unless("@p[tag=!" + Tag.PredictionCandidate + ",scores={Deaths=0}]") +
                    new TellRaw("@a", texts).sendRaw());
            texts.clear();

            // Clear candidate tag
            fileCommands.add(CommandBuilder.removeTag("@p[tag=" + Tag.PredictionCandidate + "]", Tag.PredictionCandidate));
        }

        // Self-schedule function
        fileCommands.add(Execute.Unless("@e[tag=" + Tag.PredictionsCompleted + "]") +
                Schedule.callFunction(FileName.predictions_loop, 1, Duration.TICKS));

        return new FileData(FileName.predictions_loop, fileCommands);
    }

    private FileData IntoCalls() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Teleport to starting coordinates
        fileCommands.add(Execute.In(Dimension.overworld) +
                CommandBuilder.teleportEntity("@a", startCoordinate));

        // Reset scores
        fileCommands.add(scoreboard.Set("@a", getObjectiveByName(Objective.Deaths), 0));
        fileCommands.add(scoreboard.Set("@a", getObjectiveByName(Objective.Kills), 0));

        // Make players invulnerable
        fileCommands.add(CommandBuilder.giveEffect("@a", Effect.RESISTANCE, 99999, 4, true));

        return new FileData(FileName.into_calls, fileCommands);
    }

    private FileData SpreadPlayers() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Spread teams together in assigned mode, separate in unassigned mode
        Boolean respectTeams = !OperationMode.teamCreationInGame;

        fileCommands.add(Execute.In(Dimension.overworld) +
                CommandBuilder.spreadPlayers(0, 0, (int) (0.3 * world.getSize()), (int) (0.9 * world.getSize()), respectTeams, "@a"));

        return new FileData(FileName.spread_players, fileCommands);
    }

    private FileData SurvivalMode() {
        ArrayList<String> fileCommands = new ArrayList<>();
        fileCommands.add(CommandBuilder.setGameRule(GameRule.commandBlockOutput, false));
        fileCommands.add(CommandBuilder.setGameRule(GameRule.doDaylightCycle, true));
        fileCommands.add(CommandBuilder.setGameRule(GameRule.keepInventory, false));
        fileCommands.add(CommandBuilder.setGameRule(GameRule.doMobSpawning, true));
        fileCommands.add(CommandBuilder.setGameRule(GameRule.doTileDrops, true));
        fileCommands.add(CommandBuilder.setGameRule(GameRule.drowningDamage, true));
        fileCommands.add(CommandBuilder.setGameRule(GameRule.fallDamage, true));
        fileCommands.add(CommandBuilder.setGameRule(GameRule.fireDamage, true));
        fileCommands.add(CommandBuilder.setGameRule(GameRule.doImmediateRespawn, true));
        fileCommands.add(Schedule.callFunction(FileName.clear_enderchest));

        // Recipes
        // fileCommands.add(giveRecipe("@a", BlockType.GOLDEN_APPLE.setNamespace(Namespace.uhc)));
        fileCommands.add(CommandBuilder.takeRecipe("@a", BlockType.DRAGON_HEAD.setNamespace(Namespace.uhc)));

        // Remove resistance
        fileCommands.add(CommandBuilder.clearEffect("@a", Effect.RESISTANCE));

        // Set scoreboard values
        fileCommands.add(scoreboard.Set("@a", getObjectiveByName(Objective.Hearts), 20));
        fileCommands.add(scoreboard.Set("@a", getObjectiveByName(Objective.DamageTaken), 0));

        return new FileData(FileName.survival_mode, fileCommands);
    }

    private FileData StartGame() {
        ArrayList<String> fileCommands = new ArrayList<>();
        ArrayList<TextItem> texts = new ArrayList<>();

        // Set world time
        fileCommands.add(CommandBuilder.setTime(0));

        // Give potion effect
        fileCommands.add(CommandBuilder.giveEffect("@a", Effect.REGENERATION, 1, 255));
        fileCommands.add(CommandBuilder.giveEffect("@a", Effect.SATURATION, 1, 255));
        fileCommands.add(CommandBuilder.giveEffect("@a", Effect.RESISTANCE, 20 * 60, 2, true));

        // Clear player inventories
        fileCommands.add(CommandBuilder.clearInventory("@a"));

        // Set all players to survival mode
        fileCommands.add(CommandBuilder.setGameMode(GameMode.survival, "@a"));

        // Revoke all advancements
        fileCommands.add(CommandBuilder.revokeAdvancement("@a"));

        // Experience
        fileCommands.add(CommandBuilder.setExperience("@a", 0, ExperienceType.levels));
        fileCommands.add(CommandBuilder.setExperience("@a", 0, ExperienceType.points));

        // Give players teammate tools
        if (!OperationMode.teamCreationInGame) {
            // Teammate tracker
            for (Team team : teams) {
                fileCommands.add(CommandBuilder.giveItem("@a[team=" + team.getName() + "]", BlockType.BUNDLE.extendColor(team.getGlassColor()), "[enchantments={\"" + EnchantmentType.VANISHING_CURSE + "\":1},custom_data={locateTeammate:1b}]"));
            }
        } else {
            // Team caller
            fileCommands.add(CommandBuilder.giveItem("@a", BlockType.GOAT_HORN, "[instrument=\"minecraft:ponder_goat_horn\",use_cooldown={seconds:30},enchantments={\"" + EnchantmentType.VANISHING_CURSE + "\":1}]"));
        }

        // Show world border size in actionbar
        texts.add(new Text(Color.light_purple, false, false, "World size: ±" + world.getSize() + " blocks"));
        Title showWorldSize = new Title("@a", TitleType.subtitle, texts);

        // Change title display time
        fileCommands.add(CommandBuilder.changeTitleDisplayTime("@a", 1, 5, 2));

        // Display world size
        fileCommands.add(showWorldSize.displayTitle());

        // Display game start
        fileCommands.add(new Title("@a", TitleType.title, new Text(Color.gold, true, true, "Game Starting Now!")).displayTitle());

        // Change title display time
        fileCommands.add(CommandBuilder.titleDefaultTiming("@a"));

        // Destroy all ground items
        fileCommands.add(CommandBuilder.killEntity("@e[type=" + EntityType.ITEM + "]"));

        // Schedule continuous functions
        fileCommands.add(Schedule.callFunction(FileName.game_starter));

        return new FileData(FileName.start_game, fileCommands);
    }

    private FileData BattleRoyale() {
        ArrayList<String> fileCommands = new ArrayList<>();

        fileCommands.add(Execute.In(Dimension.overworld, false) +
                Execute.PositionedNext(new Coordinate(0, 151, 0), true) +
                CommandBuilder.setGameMode(GameMode.survival, "@a[distance=..20,gamemode=!creative]"));
        fileCommands.add(Execute.In(Dimension.overworld, false) +
                Execute.PositionedNext(new Coordinate(0, 151, 0), true) +
                CommandBuilder.spreadPlayers(0, 0, (int) (0.3 * world.getSize()), (int) (0.9 * world.getSize()), true, "@a[distance=..20,gamemode=survival]"));

        return new FileData(FileName.battle_royale, fileCommands);
    }

    private FileData InitializeControlpoint() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Display Control Point 1 enabled
        fileCommands.add(new Title("@a", TitleType.subtitle, new Text(Color.light_purple, true, true, "is now enabled!")).displayTitle());
        fileCommands.add(new Title("@a", TitleType.title, new Text(Color.gold, true, true, "Control Point 1")).displayTitle());

        // Make bossbars visible
        fileCommands.add(getBossbarByName("cp1").setVisible(true));
        fileCommands.add(getBossbarByName("cp2").setVisible(true));

        // Remove CP1 reinforced deepslate block
        fileCommands.addAll(CommandBuilder.forceLoadAndSet(controlPoints.get(0).getCoordinate().getX(), controlPoints.get(0).getCoordinate().getY() + 3, controlPoints.get(0).getCoordinate().getZ(), BlockType.AIR, SetBlockType.replace));

        // Summon armor stands for locator bar tracking
        for (ControlPoint controlPoint : controlPoints) {
            fileCommands.addAll(CommandBuilder.createWaypoint(controlPoint.getCoordinate(), controlPoint.getName()));
        }

        // Schedule continuous functions
        fileCommands.add(Schedule.callFunction(FileName.timer_control_point_5));
        fileCommands.add(Schedule.callFunction(FileName.timer_control_point_20));

        // Give admin tag for disabling self-rescheduling
        fileCommands.add(CommandBuilder.addTag(Constant.admin, Tag.ControlPoint1Enabled));

        return new FileData(FileName.initialize_control_point, fileCommands);
    }

    private FileData SecondControlpoint() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Announce that Control Point 2 is enabled
        ArrayList<TextItem> texts = new ArrayList<>();
        texts.add(bannerText);
        texts.add(new Text(Color.gold, true, false, communityName + " UHC"));
        texts.add(bannerText);
        texts.add(new Text(Color.light_purple, true, false, "CONTROL POINT 2 IS NOW AVAILABLE!"));
        texts.add(bannerText);
        fileCommands.add(new TellRaw("@a", texts).sendRaw());

        // Remove reinforced deepslate from CP2
        fileCommands.addAll(CommandBuilder.forceLoadAndSet(controlPoints.get(1).getCoordinate().getX(), controlPoints.get(1).getCoordinate().getY() + 3, controlPoints.get(1).getCoordinate().getZ(), controlPoints.get(1).getCoordinate().getDimension(), BlockType.AIR, SetBlockType.replace));

        // Change bossbar text
        fileCommands.add(getBossbarByName("cp2").setTitle("CP2: " + controlPoints.get(1).getCoordinate().getX() + ", " + controlPoints.get(1).getCoordinate().getY() + ", " + controlPoints.get(1).getCoordinate().getZ() + " (" + controlPoints.get(1).getCoordinate().getDimensionName() + ") - FASTER!!"));

        // Give admin tag for disabling self-rescheduling
        fileCommands.add(CommandBuilder.addTag(Constant.admin, Tag.ControlPoint2Enabled));

        return new FileData(FileName.second_control_point, fileCommands);
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

        // Set objective to victory achieved
        fileCommands.add(scoreboard.Set(Constant.admin, getObjectiveByName(Objective.Victory), 2));

        // Call deathmatch functions
        fileCommands.add(Execute.If("@a[limit=2,gamemode=!spectator]") +
                Schedule.callFunction(FileName.initiate_deathmatch));

        // Announce iron man
        fileCommands.add(Execute.As("@a[scores={DamageTaken=.." + minDamage + "}]") +
                Schedule.callFunction(FileName.announce_iron_man));

        return new FileData(FileName.victory, fileCommands);
    }

    private FileData InitiateDeathMatch() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Call deathmatch functions
        fileCommands.add(Schedule.callFunction(FileName.minute_ + "2", 60));
        fileCommands.add(Schedule.callFunction(FileName.minute_ + "1", 60 * 2));
        fileCommands.add(Schedule.callFunction(FileName.death_match, 60 * 3));

        return new FileData(FileName.initiate_deathmatch, fileCommands);
    }

    private FileData VictoryMessage(Team team, int i) {
        ArrayList<String> fileCommands = new ArrayList<>();
        ArrayList<TextItem> texts = new ArrayList<>();

        // Chat message
        texts.add(bannerText);
        texts.add(new Text(Color.gold, true, false, communityName + " UHC"));
        texts.add(bannerText);
        texts.add(new Text(team.getColor(), true, false, team.getJSONColor()));
        texts.add(new Text(Color.light_purple, true, false, " TEAM VICTORY HAS BEEN ACHIEVED! 3 MINUTES UNTIL THE FINAL DEATHMATCH"));
        texts.add(bannerText);
        fileCommands.add(new TellRaw("@a", texts).sendRaw());

        // Title
        fileCommands.add(new Title("@a", TitleType.subtitle, new Text(Color.light_purple, true, true, "has been achieved!")).displayTitle());
        fileCommands.add(new Title("@a", TitleType.title, new Text(Color.gold, true, true, team.getJSONColor() + " team victory")).displayTitle());

        // Proceed to victory mode
        fileCommands.add(Schedule.callFunction(FileName.victory));

        return new FileData("" + FileName.victory_message_ + i, fileCommands);
    }

    private FileData VictoryMessageSolo() {
        ArrayList<String> fileCommands = new ArrayList<>();
        ArrayList<TextItem> texts = new ArrayList<>();

        // Chat message
        texts.add(bannerText);
        texts.add(new Text(Color.gold, true, false, communityName + " UHC"));
        texts.add(bannerText);
        texts.add(new Select(Color.white, false, true, "@s"));
        texts.add(new Text(Color.light_purple, true, false, " HAS ACHIEVED VICTORY!"));
        texts.add(bannerText);

        // Title
        fileCommands.add(new TellRaw("@a", texts).sendRaw());
        texts.clear();
        fileCommands.add(new Title("@a", TitleType.subtitle, new Text(Color.light_purple, true, true, "Absolute chad.")).displayTitle());
        texts.add(new Select(Color.white, false, true, "@s"));
        texts.add(new Text(Color.gold, true, false, " victorious"));
        fileCommands.add(new Title("@a", TitleType.title, texts).displayTitle());

        // Proceed to victory mode
        fileCommands.add(Schedule.callFunction(FileName.victory));

        return new FileData(FileName.victory_message_solo, fileCommands);
    }

    private FileData VictoryTraitor() {
        ArrayList<String> fileCommands = new ArrayList<>();
        ArrayList<TextItem> texts = new ArrayList<>();

        // Chat message
        texts.add(bannerText);
        texts.add(new Text(Color.gold, true, false, communityName + " UHC"));
        texts.add(bannerText);
        texts.add(new Text(Color.light_purple, true, false, " TRAITOR VICTORY HAS BEEN ACHIEVED! 3 MINUTES UNTIL THE FINAL DEATHMATCH"));
        texts.add(bannerText);
        fileCommands.add(new TellRaw("@a", texts).sendRaw());

        // Title
        fileCommands.add(new Title("@a", TitleType.subtitle, new Text(Color.light_purple, true, true, "ggez")).displayTitle());
        fileCommands.add(new Title("@a", TitleType.title, new Text(Color.gold, true, true, "Traitors Win")).displayTitle());

        // Proceed to victory mode
        fileCommands.add(Schedule.callFunction(FileName.victory));

        return new FileData(FileName.victory_message_traitor, fileCommands);
    }

    private FileData DeathMatch() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Set start worldborder size
        fileCommands.add(CommandBuilder.setWorldBorder(400));

        // Set destination worldborder size
        fileCommands.add(CommandBuilder.setWorldBorder(20, 180));

        // Teleport all living players
        fileCommands.add(Execute.In(Dimension.overworld) +
                CommandBuilder.teleportEntity("@a[gamemode=!spectator]", new Coordinate(3, 153, 3)));

        // Spread players in a team together
        fileCommands.add(Execute.In(Dimension.overworld) +
                CommandBuilder.spreadPlayers(0, 0, 75, 150, true, "@a[gamemode=!spectator,team=!]"));

        if (OperationMode.teamCreationInGame) {
            // Spread players without a team alone
            fileCommands.add(Execute.In(Dimension.overworld) +
                    CommandBuilder.spreadPlayers(0, 0, 75, 150, false, "@a[gamemode=!spectator,team=]"));
        }

        return new FileData(FileName.death_match, fileCommands);
    }



    private FileData Controlpoint(int i) {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Current Control Point
        ControlPoint currentCP = controlPoints.get(i - 1);

        // Update CP glass color teams
        for (Team team : teams) {
            // Update CP glass color
            fileCommands.add(Execute.In(currentCP.getCoordinate().getDimension(), false) +
                    Execute.IfNext("@p[gamemode=!spectator,team=" + team.getName() + "]", getObjectiveByName(Objective.ControlPoint.extendName(i)), ComparatorType.GREATER, Constant.adminSingle, getObjectiveByName(Objective.Highscore.extendName(i)), true) +
                    CommandBuilder.setBlock(currentCP.getCoordinate().getX(), currentCP.getCoordinate().getY() + 1, currentCP.getCoordinate().getZ(), "minecraft:" + team.getGlassColor() + "_stained_glass", SetBlockType.replace));
        }

        if (OperationMode.teamCreationInGame) {
            // Update CP glass color solo
            fileCommands.add(Execute.In(currentCP.getCoordinate().getDimension(), false) +
                    Execute.IfNext("@r[limit=1,gamemode=!spectator,team=]", getObjectiveByName(Objective.ControlPoint.extendName(i)), ComparatorType.GREATER, Constant.adminSingle, getObjectiveByName(Objective.Highscore.extendName(i)), true) +
                    CommandBuilder.setBlock(currentCP.getCoordinate().getX(), currentCP.getCoordinate().getY() + 1, currentCP.getCoordinate().getZ(), BlockType.STAINED_GLASS.extendColor("white"), SetBlockType.replace));
        }

        // Keep beacon active
        fileCommands.add(Execute.In(currentCP.getCoordinate().getDimension()) +
                CommandBuilder.fill(currentCP.getCoordinate().getX() - 1, currentCP.getCoordinate().getY() - 1, currentCP.getCoordinate().getZ() - 1, currentCP.getCoordinate().getX() + 1, currentCP.getCoordinate().getY() - 1, currentCP.getCoordinate().getZ() + 1, BlockType.EMERALD_BLOCK));
        fileCommands.add(Execute.In(currentCP.getCoordinate().getDimension()) +
                CommandBuilder.fill(currentCP.getCoordinate().getX(), currentCP.getCoordinate().getY(), currentCP.getCoordinate().getZ(), currentCP.getCoordinate().getX(), currentCP.getCoordinate().getY(), currentCP.getCoordinate().getZ(), BlockType.BEACON));

        fileCommands.add(Schedule.callFunction("" + FileName.control_point_tag_ + i));
        fileCommands.add(Schedule.callFunction("" + FileName.control_point_score_ + i));

        return new FileData("" + FileName.control_point_ + i, fileCommands);
    }

    private FileData ControlPointScore(int i) {
        ArrayList<String> fileCommands = new ArrayList<>();
        ControlPoint currentCP = controlPoints.get(i - 1);

        // Give team players on the Control Point score
        for (Team team : teams) {
            fileCommands.add(Execute.In(currentCP.getCoordinate().getDimension(), false) +
                    Execute.AsNext("@p[gamemode=!spectator,tag=" + Tag.OnCP + i + ",team=" + team.getName() + "]") +
                    Execute.UnlessNext("@p[gamemode=!spectator,tag=" + Tag.OnCP + i + ",team=!" + team.getName() + "]", true) +
                    scoreboard.Add("@s", getObjectiveByName(Objective.ControlPoint.extendName(i)), currentCP.getAddRate()));
        }

        if (OperationMode.teamCreationInGame) {
            // Give single players on the Control Point score
            fileCommands.add(Execute.In(currentCP.getCoordinate().getDimension(), false) +
                    Execute.AsNext("@a[tag=" + Tag.Capping + i + "]", true) +
                    scoreboard.Add("@s", getObjectiveByName(Objective.ControlPoint.extendName(i)), currentCP.getAddRate()));
        }

        return new FileData("" + FileName.control_point_score_ + i, fileCommands);
    }

    private FileData ControlPointTag(int i) {
        ArrayList<String> fileCommands = new ArrayList<>();
        ControlPoint currentCP = controlPoints.get(i - 1);

        // Give player OnCP tag
        fileCommands.add(Execute.In(currentCP.getCoordinate().getDimension(), false) +
                Execute.AsNext("@p[gamemode=!spectator,tag=!" + Tag.OnCP + i + ",x=" + (currentCP.getCoordinate().getX() - 6) + ",y=" + (currentCP.getCoordinate().getY() - 1) + ",z=" + (currentCP.getCoordinate().getZ() - 6) + ",dx=12,dy=12,dz=12]", true) +
                CommandBuilder.addTag("@s", Tag.OnCP + "" + i));

        // Remove player OnCP tag
        fileCommands.add(Execute.In(currentCP.getCoordinate().getDimension(), false) +
                Execute.AsNext("@a[gamemode=!spectator,tag=" + Tag.OnCP + i + "]") +
                Execute.UnlessNext("@s[x=" + (currentCP.getCoordinate().getX() - 6) + ",y=" + (currentCP.getCoordinate().getY() - 1) + ",z=" + (currentCP.getCoordinate().getZ() - 6) + ",dx=12,dy=12,dz=12]", true) +
                CommandBuilder.removeTag("@s", Tag.OnCP + "" + i));

        if (OperationMode.teamCreationInGame) {
            //Give player Capping tag
            fileCommands.add(Execute.In(currentCP.getCoordinate().getDimension(), false) +
                    Execute.AsNext("@p[gamemode=!spectator,team=,tag=" + Tag.OnCP + i + "]") +
                    Execute.UnlessNext("@a[tag=" + Tag.Capping + i + "]", true) +
                    CommandBuilder.addTag("@s", Tag.Capping + "" + i));

            //remove player Capping tag
            fileCommands.add(Execute.In(currentCP.getCoordinate().getDimension(), false) +
                    Execute.AsNext("@a[gamemode=!spectator,tag=" + Tag.OnCP + i + "]") +
                    Execute.IfNext("@s[gamemode=!spectator,tag=!" + Tag.Capping + i + "]", true) +
                    CommandBuilder.removeTag("@a", Tag.Capping + "" + i));

            fileCommands.add(Execute.In(currentCP.getCoordinate().getDimension(), false) +
                    Execute.AsNext("@a[gamemode=!spectator,tag=" + Tag.Capping + i + "]") +
                    Execute.UnlessNext("@s[gamemode=!spectator,tag=" + Tag.OnCP + i + "]", true) +
                    CommandBuilder.removeTag("@a", Tag.Capping + "" + i));
        }

        return new FileData("" + FileName.control_point_tag_ + i, fileCommands);
    }

    private FileData ControlPointMessages(int i) {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Current Control Point
        ControlPoint currentCP = controlPoints.get(i - 1);

        // Players in a team
        for (Team team : teams) {
            /* Under attack message */
            // Increment attacking counter if team is on CP and message is not sent
            fileCommands.add(Execute.In(currentCP.getCoordinate().getDimension(), false) +
                    Execute.IfNext(new Entity("@p[gamemode=!spectator,team=" + team.getName() + ",x=" + (currentCP.getCoordinate().getX() - 6) + ",y=" + (currentCP.getCoordinate().getY() - 1) + ",z=" + (currentCP.getCoordinate().getZ() - 6) + ",dx=12,dy=12,dz=12,scores={MSGDum1CP" + i + "=.." + cpMessageThreshold + "}]"), true) +
                    scoreboard.Add("@a[team=" + team.getName() + "]", getObjectiveByName(Objective.MSGDum.extendName("1CP" + i)), 1));

            // Reset abandonment counter
            fileCommands.add(Execute.In(currentCP.getCoordinate().getDimension(), false) +
                    Execute.IfNext(new Entity("@p[gamemode=!spectator,team=" + team.getName() + ",x=" + (currentCP.getCoordinate().getX() - 6) + ",y=" + (currentCP.getCoordinate().getY() - 1) + ",z=" + (currentCP.getCoordinate().getZ() - 6) + ",dx=12,dy=12,dz=12]"), true) +
                    scoreboard.Set("@a[gamemode=!spectator,team=" + team.getName() + "]", getObjectiveByName(Objective.MSGDum.extendName("2CP" + i)), 1));

            // Define CP attacking message
            ArrayList<TextItem> texts = new ArrayList<>();
            texts.add(new Text(Color.light_purple, false, false, "TEAM "));
            texts.add(new Text(team.getColor(), false, false, team.getJSONColor()));
            texts.add(new Text(Color.light_purple, false, false, " IS ATTACKING CONTROL POINT " + i + "!"));

            // Display message in chat if time threshold has been exceeded
            fileCommands.add(Execute.If(new Entity("@p[team=" + team.getName() + ",scores={MSGDum1CP" + i + "=" + cpMessageThreshold + "}]")) +
                    new TellRaw("@a", texts).sendRaw());

            // Grant attacking players Attacking tag
            fileCommands.add(Execute.If(new Entity("@p[team=" + team.getName() + ",scores={MSGDum1CP" + i + "=" + cpMessageThreshold + "}]")) +
                    CommandBuilder.addTag("@a[team=" + team.getName() + "]", Tag.AttackingCP.extendName(i)));

            /* Abandoned message */
            // Increment abandonment counter unless team is on the CP
            fileCommands.add(Execute.In(currentCP.getCoordinate().getDimension(), false) +
                    Execute.IfNext("@p[gamemode=!spectator,team=" + team.getName() + ",tag=" + Tag.AttackingCP.extendName(i) + "]") +
                    Execute.UnlessNext("@p[gamemode=!spectator,team=" + team.getName() + ",x=" + (currentCP.getCoordinate().getX() - 6) + ",y=" + (currentCP.getCoordinate().getY() - 1) + ",z=" + (currentCP.getCoordinate().getZ() - 6) + ",dx=12,dy=12,dz=12]", true) +
                    scoreboard.Add("@a[team=" + team.getName() + "]", getObjectiveByName(Objective.MSGDum.extendName("2CP" + i)), 1));

            // Define CP abandonment message
            texts.clear();
            texts.add(new Text(Color.light_purple, false, false, "TEAM "));
            texts.add(new Text(team.getColor(), false, false, team.getJSONColor()));
            texts.add(new Text(Color.light_purple, false, false, " HAS ABANDONED CONTROL POINT " + i + "!"));

            // Display message in chat if time threshold has been exceeded
            fileCommands.add(Execute.If(new Entity("@p[team=" + team.getName() + ",scores={MSGDum2CP" + i + "=" + (cpMessageThreshold - 1) + "}]")) +
                    new TellRaw("@a", texts).sendRaw());

            // Reset attacking counter
            fileCommands.add(Execute.If(new Entity("@p[team=" + team.getName() + ",scores={MSGDum2CP" + i + "=" + cpMessageThreshold + "}]")) +
                    scoreboard.Set("@a[gamemode=!spectator,team=" + team.getName() + "]", getObjectiveByName(Objective.MSGDum.extendName("1CP" + i)), 1));

            // Remove Attacking tag
            fileCommands.add(Execute.If(new Entity("@p[team=" + team.getName() + ",scores={MSGDum2CP" + i + "=" + cpMessageThreshold + "}]")) +
                    CommandBuilder.removeTag("@a[team=" + team.getName() + "]", Tag.AttackingCP.extendName(i)));
        }

        if (OperationMode.teamCreationInGame) {
            // Players without a team
            /* Under attack message */
            // Increment attacking counter if team is on CP and message is not sent
            fileCommands.add(Execute.In(currentCP.getCoordinate().getDimension(), false) +
                    Execute.IfNext(new Entity("@p[gamemode=!spectator,team=,x=" + (currentCP.getCoordinate().getX() - 6) + ",y=" + (currentCP.getCoordinate().getY() - 1) + ",z=" + (currentCP.getCoordinate().getZ() - 6) + ",dx=12,dy=12,dz=12,scores={MSGDum1CP" + i + "=.." + cpMessageThreshold + "}]"), true) +
                    scoreboard.Add("@a[team=]", getObjectiveByName(Objective.MSGDum.extendName("1CP" + i)), 1));

            // Reset abandonment counter
            fileCommands.add(Execute.In(currentCP.getCoordinate().getDimension(), false) +
                    Execute.IfNext(new Entity("@p[gamemode=!spectator,team=,x=" + (currentCP.getCoordinate().getX() - 6) + ",y=" + (currentCP.getCoordinate().getY() - 1) + ",z=" + (currentCP.getCoordinate().getZ() - 6) + ",dx=12,dy=12,dz=12]"), true) +
                    scoreboard.Set("@a[gamemode=!spectator,team=]", getObjectiveByName(Objective.MSGDum.extendName("2CP" + i)), 1));

            // Define CP attacking message
            ArrayList<TextItem> texts = new ArrayList<>();
            texts.add(new Text(Color.light_purple, false, false, "A "));
            texts.add(new Text(Color.white, false, false, "SOLO"));
            texts.add(new Text(Color.light_purple, false, false, " IS ATTACKING CONTROL POINT " + i + "!"));

            // Display message in chat if time threshold has been exceeded
            fileCommands.add(Execute.If(new Entity("@p[team=,scores={MSGDum1CP" + i + "=" + cpMessageThreshold + "}]")) +
                    new TellRaw("@a", texts).sendRaw());

            // Grant attacking players Attacking tag
            fileCommands.add(Execute.If(new Entity("@p[team=,scores={MSGDum1CP" + i + "=" + cpMessageThreshold + "}]")) +
                    CommandBuilder.addTag("@a[team=]", Tag.AttackingCP.extendName(i)));

            /* Abandoned message */
            // Increment abandonment counter unless team is on the CP
            fileCommands.add(Execute.In(currentCP.getCoordinate().getDimension(), false) +
                    Execute.IfNext("@p[gamemode=!spectator,team=,tag=" + Tag.AttackingCP.extendName(i) + "]") +
                    Execute.UnlessNext("@p[gamemode=!spectator,team=,x=" + (currentCP.getCoordinate().getX() - 6) + ",y=" + (currentCP.getCoordinate().getY() - 1) + ",z=" + (currentCP.getCoordinate().getZ() - 6) + ",dx=12,dy=12,dz=12]", true) +
                    scoreboard.Add("@a[team=]", getObjectiveByName(Objective.MSGDum.extendName("2CP" + i)), 1));

            // Define CP abandonment message
            texts.clear();
            texts.add(new Text(Color.light_purple, false, false, "A "));
            texts.add(new Text(Color.white, false, false, "SOLO"));
            texts.add(new Text(Color.light_purple, false, false, " HAS ABANDONED CONTROL POINT " + i + "!"));

            // Display message in chat if time threshold has been exceeded
            fileCommands.add(Execute.If(new Entity("@p[team=,scores={MSGDum2CP" + i + "=" + (cpMessageThreshold - 1) + "}]")) +
                    new TellRaw("@a", texts).sendRaw());

            // Reset attacking counter
            fileCommands.add(Execute.If(new Entity("@p[team=,scores={MSGDum2CP" + i + "=" + cpMessageThreshold + "}]")) +
                    scoreboard.Set("@a[gamemode=!spectator,team=]", getObjectiveByName(Objective.MSGDum.extendName("1CP" + i)), 1));

            // Remove Attacking tag
            fileCommands.add(Execute.If(new Entity("@p[team=,scores={MSGDum2CP" + i + "=" + cpMessageThreshold + "}]")) +
                    CommandBuilder.removeTag("@a[team=]", Tag.AttackingCP.extendName(i)));
        }

        return new FileData("" + FileName.control_point_messages_ + i, fileCommands);
    }

    private FileData DropCarepackages() {
        ArrayList<String> fileCommands = new ArrayList<>();
        ArrayList<TextItem> texts = new ArrayList<>();
        Boolean debug = false;

        // Show world border size in actionbar
        texts.add(new Text(Color.light_purple, false, false, "To be found at ±" + carePackageSpread + " blocks"));
        Title showWorldSize = new Title("@a", TitleType.subtitle, texts);
        texts.clear();

        // Change title display time
        fileCommands.add(CommandBuilder.changeTitleDisplayTime("@a", 1, 5, 2));

        // Display world size
        fileCommands.add(showWorldSize.displayTitle());

        // Announce Care Packages
        fileCommands.add(new Title("@a", TitleType.title, new Text(Color.gold, true, true, carePackageAmount + " Care Packages!")).displayTitle());

        // Change title display time
        fileCommands.add(CommandBuilder.titleDefaultTiming("@a"));

        // Summon Care Package entities
        for (int i = 0; i < carePackageAmount; i++) {
            fileCommands.add(Execute.In(Dimension.overworld) +
                    CommandBuilder.summonEntity(EntityType.AREA_EFFECT_CLOUD, new Coordinate(0, 300, 0), "{Passengers:[{id:\"" + EntityType.FALLING_BLOCK + "\",BlockState:{Name:\"" + BlockType.CHEST + "\"},TileEntityData:{LootTable:\"uhc:supply_drop\",CustomName:\"Care Package\"},Time:1,DropItem:0b,Tags:[\"CarePackage\"]}]}"));
        }

        // Spread Care Packages
        fileCommands.add(Execute.In(Dimension.overworld, true) +
                CommandBuilder.spreadPlayers(0, 0, 10, carePackageSpread, false, "@e[type=" + EntityType.FALLING_BLOCK + ",nbt={Tags:[\"CarePackage\"]}]"));

        // Give admin tag for disabling self-rescheduling
        fileCommands.add(CommandBuilder.addTag(Constant.admin, Tag.CarePackagesDropped));

        return new FileData(FileName.drop_carepackages, fileCommands);
    }

    private FileData TraitorHandout() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Check if player can become traitor
        if (traitorWaitTime > 0) {
            ArrayList<Season> seasons = new ArrayList<>(this.seasons);
            seasons.sort(Comparator.comparing(Season::getID));
            for (Player p : players) {
                if (p.getLastTraitorSeason() >= seasons.get(seasons.size() - traitorWaitTime).getID()) {
                    // Exclude players who cannot become traitor
                    fileCommands.add(CommandBuilder.addTag(p.getPlayerName(), Tag.DontMakeTraitor));
                }
            }
        }

        // Assign first traitor
        fileCommands.add(CommandBuilder.addTag("@r[limit=1,tag=!" + Tag.DontMakeTraitor + ",scores={Rank=" + minTraitorRank + "..},gamemode=!spectator]", Tag.Traitor));

        // Make traitor teammates ineligible for becoming traitor
        for (Team t : teams) {
            fileCommands.add(Execute.If(new Entity("@p[tag=" + Tag.Traitor + ",team=" + t.getName() + "]")) +
                    CommandBuilder.addTag("@a[team=" + t.getName() + "]", Tag.DontMakeTraitor));
        }

        // Make solo traitors ineligible to become traitor again
        fileCommands.add(CommandBuilder.addTag("@a[tag=" + Tag.Traitor + ",team=]", Tag.DontMakeTraitor));

        // Assign second traitor
        fileCommands.add(CommandBuilder.addTag("@r[limit=1,tag=!" + Tag.DontMakeTraitor + ",scores={Rank=" + minTraitorRank + "..},gamemode=!spectator]", Tag.Traitor));

        // Add additional traitor
        if (traitorMode == 2) {
            fileCommands.add(CommandBuilder.removeTag("@a", Tag.DontMakeTraitor));
            if (traitorWaitTime > 0) {
                for (Player p : players) {
                    if (p.getLastTraitorSeason() >= seasons.get(seasons.size() - traitorWaitTime).getID()) {
                        fileCommands.add(CommandBuilder.addTag(p.getPlayerName(), Tag.DontMakeTraitor));
                    }
                }
            }
            fileCommands.add(CommandBuilder.addTag("@a[tag=" + Tag.Traitor + "]", Tag.DontMakeTraitor));
            fileCommands.add(CommandBuilder.addTag("@r[limit=1,tag=!" + Tag.DontMakeTraitor + ",gamemode=!spectator]", Tag.Traitor));
        }

        // Inform traitors
        ArrayList<TextItem> texts = new ArrayList<>();
        texts.add(new Text(Color.red, false, true, "You feel like betrayal today. You have become a Traitor. Your faction consists of: "));
        texts.add(new Select(false, true, "@a[tag=" + Tag.Traitor + "]"));
        texts.add(new Text(Color.red, false, true, "."));
        fileCommands.add(Execute.As(new Entity("@a[tag=" + Tag.Traitor + "]")) +
                new TellRaw("@s", texts).sendRaw());

        // Announce Traitor Faction
        fileCommands.add(new Title("@a", TitleType.title, new Text(Color.red, true, false, "A Traitor Faction")).displayTitle());
        fileCommands.add(new Title("@a", TitleType.subtitle, new Text(Color.dark_red, true, false, "has been founded!")).displayTitle());

        // Enable timers
        fileCommands.add(Schedule.callFunction(FileName.timer_traitor_5));
        fileCommands.add(Schedule.callFunction(FileName.timer_traitor_20));

        // Give admin tag for disabling self-rescheduling
        fileCommands.add(CommandBuilder.addTag(Constant.admin, Tag.TraitorsAssigned));

        return new FileData(FileName.traitor_handout, fileCommands);
    }

    private FileData TraitorActionBar() {
        ArrayList<String> fileCommands = new ArrayList<>();
        ArrayList<TextItem> texts = new ArrayList<>();

        // Show all traitors in actionbar
        texts.add(new Text(Color.gold, false, false, ">>> "));
        texts.add(new Text(Color.light_purple, false, false, "Traitor Faction: "));
        texts.add(new Select(Color.white, false, false, "@a[tag=" + Tag.Traitor + "]"));
        texts.add(new Text(Color.gold, false, false, " <<<"));
        fileCommands.add(Execute.As(new Entity("@a[tag=" + Tag.Traitor + "]")) +
                new Title("@s", TitleType.actionbar, texts).displayTitle());

        return new FileData(FileName.traitor_actionbar, fileCommands);
    }

    private FileData TeamScore() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Teams
        for (int i = 1; i < controlPoints.size() + 1; i++) {
            for (Team t : teams) {
                fileCommands.add(Execute.As(new Entity("@r[limit=1,gamemode=!spectator,team=" + t.getName() + "]")) +
                        scoreboard.Operation(Constant.admin, getObjectiveByName("" + Objective.CP + i + t.getName()), ComparatorType.GREATER, "@s", getObjectiveByName(Objective.ControlPoint.extendName(i))));

                fileCommands.add(Execute.As(new Entity("@a[gamemode=!spectator,team=" + t.getName() + "]")) +
                        scoreboard.Operation("@s", getObjectiveByName(Objective.ControlPoint.extendName(i)), ComparatorType.GREATER, Constant.admin, getObjectiveByName("" + Objective.CP + i + t.getName())));
            }
        }

        for (Team t : teams) {
            fileCommands.add(Execute.In(controlPoints.get(0).getCoordinate().getDimension(), false) +
                    Execute.AsNext(new Entity("@r[limit=1,gamemode=!spectator,x=" + (controlPoints.get(0).getCoordinate().getX() - 6) + ",y=" + (controlPoints.get(0).getCoordinate().getY() - 1) + ",z=" + (controlPoints.get(0).getCoordinate().getZ() - 6) + ",dx=12,dy=12,dz=12,team=" + t.getName() + "]"), true) +
                    scoreboard.Operation(Constant.admin, getObjectiveByName("" + Objective.CP + 1 + t.getName()), ComparatorType.GREATER, Constant.admin, getObjectiveByName("" + Objective.CP + 2 + t.getName())));

            fileCommands.add(Execute.In(controlPoints.get(1).getCoordinate().getDimension(), false) +
                    Execute.AsNext(new Entity("@r[limit=1,gamemode=!spectator,x=" + (controlPoints.get(1).getCoordinate().getX() - 6) + ",y=" + (controlPoints.get(1).getCoordinate().getY() - 1) + ",z=" + (controlPoints.get(1).getCoordinate().getZ() - 6) + ",dx=12,dy=12,dz=12,team=" + t.getName() + "]"), true) +
                    scoreboard.Operation(Constant.admin, getObjectiveByName("" + Objective.CP + 2 + t.getName()), ComparatorType.GREATER, Constant.admin, getObjectiveByName("" + Objective.CP + 1 + t.getName())));
        }

        if (OperationMode.teamCreationInGame) {
            // Individual players
            fileCommands.add(Execute.In(controlPoints.get(0).getCoordinate().getDimension(), false) +
                    Execute.AsNext(new Entity("@r[limit=1,gamemode=!spectator,x=" + (controlPoints.get(0).getCoordinate().getX() - 6) + ",y=" + (controlPoints.get(0).getCoordinate().getY() - 1) + ",z=" + (controlPoints.get(0).getCoordinate().getZ() - 6) + ",dx=12,dy=12,dz=12,team=]"), true) +
                    scoreboard.Operation("@s", getObjectiveByName(Objective.ControlPoint.extendName(1)), ComparatorType.GREATER, "@s", getObjectiveByName(Objective.ControlPoint.extendName(2))));

            fileCommands.add(Execute.In(controlPoints.get(1).getCoordinate().getDimension(), false) +
                    Execute.AsNext(new Entity("@r[limit=1,gamemode=!spectator,x=" + (controlPoints.get(1).getCoordinate().getX() - 6) + ",y=" + (controlPoints.get(1).getCoordinate().getY() - 1) + ",z=" + (controlPoints.get(1).getCoordinate().getZ() - 6) + ",dx=12,dy=12,dz=12,team=]"), true) +
                    scoreboard.Operation("@s", getObjectiveByName(Objective.ControlPoint.extendName(2)), ComparatorType.GREATER, "@s", getObjectiveByName(Objective.ControlPoint.extendName(1))));
        }

        return new FileData(FileName.team_score, fileCommands);
    }

    private FileData SpawnControlPoints() {
        ArrayList<String> fileCommands = new ArrayList<>();

        for (ControlPoint cp : cpList) {
            Coordinate c = cp.getCoordinate();
            fileCommands.add(Execute.In(c.getDimension()) +
                    CommandBuilder.addForceLoad(c.getX(), c.getZ(), c.getX(), c.getZ()));
            fileCommands.add(Execute.In(c.getDimension()) +
                    CommandBuilder.setBlock(c.getX(), c.getY() + 11, c.getZ(), BlockType.STRUCTURE_BLOCK + "[mode=load]{metadata:\"\",mirror:\"NONE\",ignoreEntities:1b,powered:0b,seed:0L,author:\"?\",rotation:\"NONE\",posX:-6,mode:\"LOAD\",posY:-13,sizeX:13,posZ:-6,integrity:1.0f,showair:0b,name:\"" + cp.getStructureName() + "\",sizeY:14,sizeZ:13,showboundingbox:1b}", SetBlockType.destroy));

            // Activate structure block
            fileCommands.add(Execute.In(c.getDimension()) +
                    CommandBuilder.setBlock(c.getX(), c.getY() + 10, c.getZ(), BlockType.REDSTONE_BLOCK, SetBlockType.destroy));

            // Initialize object
            for (int i = c.getY() + 12; i < Constant.worldHeight; i++) {
                // Specify block to be changed

                fileCommands.add(Execute.In(c.getDimension(), false) +
                        Execute.UnlessNext(c.getX(), i, c.getZ(), BlockType.AIR) +
                        Execute.UnlessNext(c.getX(), i, c.getZ(), BlockType.CAVE_AIR) +
                        Execute.UnlessNext(c.getX(), i, c.getZ(), BlockType.VOID_AIR) +
                        Execute.UnlessNext(c.getX(), i, c.getZ(), BlockType.BEDROCK, true) +
                        CommandBuilder.setBlock(c.getX(), i, c.getZ(), BlockType.GLASS));
            }

            fileCommands.add(Execute.In(c.getDimension()) +
                    CommandBuilder.removeForceLoad(c.getX(), c.getZ(), c.getX(), c.getZ()));
        }

        // Remove leftover music discs from legacy Control Point
        fileCommands.add(CommandBuilder.killEntity("@e[type=" + EntityType.ITEM + ",nbt={Item:{id:\"" + BlockType.MUSIC_DISC_STAL + "\",count:1}}]"));

        return new FileData(FileName.spawn_control_points, fileCommands);
    }

    private FileData DisplayRank() {
        ArrayList<String> fileCommands = new ArrayList<>();

        for (Player p : players) {
            fileCommands.add(scoreboard.Set(p.getPlayerName(), getObjectiveByName(Objective.Rank), p.getRank()));
        }
        fileCommands.add(new ScoreboardObjective().setDisplay(ScoreboardLocation.sidebar, Objective.Rank));

        return new FileData(FileName.display_rank, fileCommands);
    }

    private FileData HorseFrostWalker() {
        ArrayList<String> fileCommands = new ArrayList<>();

        fileCommands.add(Execute.At(new Entity("@a[nbt={RootVehicle:{Entity:{id:\"" + EntityType.HORSE + "\"}}}]")) +
                CommandBuilder.relativeFill(-2, -2, -2, 2, 0, 2, "ice", SetBlockType.replace, "water"));

        return new FileData(FileName.horse_frost_walker, fileCommands);
    }

    private FileData UpdateSidebar() {
        ArrayList<String> fileCommands = new ArrayList<>();

        fileCommands.add(scoreboard.Add(Constant.admin, getObjectiveByName(Objective.SideDum), 1));
        int i = 0;
        for (ScoreboardObjective s : scoreboardObjectives) {
            if (s.getDisplaySideBar()) {
                i++;
                fileCommands.add(Execute.If(new Entity("@e[scores={SideDum=" + (10 * Constant.tickFrequencyLong * i) + "}]")) +
                        s.setDisplay(ScoreboardLocation.sidebar));
            }
        }
        fileCommands.add(Execute.If(new Entity("@e[scores={SideDum=" + (10 * Constant.tickFrequencyLong * i + 1) + "}]")) +
                scoreboard.Reset(Constant.admin, getObjectiveByName(Objective.SideDum)));


        return new FileData(FileName.update_sidebar, fileCommands);
    }

    private FileData RemoveBannedItems() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Regeneration potions (normal + splash, strong, long)
        Text warning = new Text(Color.red, true, false, "REGENERATION POTIONS ARE NOT ALLOWED, YOU NAUGHTY BUM!");
        String target = "@p[nbt={SelectedItem:{id:\"" + BlockType.SPLASH_POTION + "\",count:1,components:{\"minecraft:potion_contents\":{potion:\"" + Effect.REGENERATION.getPotionTag() + "\"}}}}]";
        String replacement = BlockType.GLASS_BOTTLE.toString();
        fileCommands.addAll(CommandBuilder.warnAndReplace(target, warning, replacement));
        target = "@p[nbt={SelectedItem:{id:\"" + BlockType.SPLASH_POTION + "\",count:1,components:{\"minecraft:potion_contents\":{potion:\"" + Effect.REGENERATION.getPotionTag(true, false) + "\"}}}}]";
        fileCommands.addAll(CommandBuilder.warnAndReplace(target, warning, replacement));
        target = "@p[nbt={SelectedItem:{id:\"" + BlockType.SPLASH_POTION + "\",count:1,components:{\"minecraft:potion_contents\":{potion:\"" + Effect.REGENERATION.getPotionTag(false, true) + "\"}}}}]";
        fileCommands.addAll(CommandBuilder.warnAndReplace(target, warning, replacement));
        target = "@p[nbt={SelectedItem:{id:\"" + BlockType.POTION + "\",count:1,components:{\"minecraft:potion_contents\":{potion:\"" + Effect.REGENERATION.getPotionTag() + "\"}}}}]";
        fileCommands.addAll(CommandBuilder.warnAndReplace(target, warning, replacement));
        target = "@p[nbt={SelectedItem:{id:\"" + BlockType.POTION + "\",count:1,components:{\"minecraft:potion_contents\":{potion:\"" + Effect.REGENERATION.getPotionTag(true, false) + "\"}}}}]";
        fileCommands.addAll(CommandBuilder.warnAndReplace(target, warning, replacement));
        target = "@p[nbt={SelectedItem:{id:\"" + BlockType.POTION + "\",count:1,components:{\"minecraft:potion_contents\":{potion:\"" + Effect.REGENERATION.getPotionTag(false, true) + "\"}}}}]";
        fileCommands.addAll(CommandBuilder.warnAndReplace(target, warning, replacement));

        // Strength II potions
        warning.setText("STRENGTH II POTIONS ARE NOT ALLOWED, YOU NAUGHTY BUM!");
        target = "@p[nbt={SelectedItem:{id:\"" + BlockType.SPLASH_POTION + "\",count:1,components:{\"minecraft:potion_contents\":{potion:\"" + Effect.STRENGTH.getPotionTag(true, false) + "\"}}}}]";
        replacement = BlockType.SPLASH_POTION.addNBT("[potion_contents={potion:\"" + Effect.STRENGTH.getPotionTag() + "\"}]");
        fileCommands.addAll(CommandBuilder.warnAndReplace(target, warning, replacement));
        target = "@p[nbt={SelectedItem:{id:\"" + BlockType.POTION + "\",count:1,components:{\"minecraft:potion_contents\":{potion:\"" + Effect.STRENGTH.getPotionTag(true, false) + "\"}}}}]";
        replacement = BlockType.POTION.addNBT("[potion_contents={potion:\"" + Effect.STRENGTH.getPotionTag() + "\"}]");
        fileCommands.addAll(CommandBuilder.warnAndReplace(target, warning, replacement));

        for (int ii = 0; ii < 5; ii++) {
            // Piercing enchantment
            warning.setText("PIERCING IS NOT ALLOWED, YOU NAUGHTY BUM!");
            target = "@p[nbt={SelectedItem:{id:\"" + BlockType.CROSSBOW + "\",count:1,components:{\"minecraft:enchantments\":{\"" + EnchantmentType.PIERCING + "\":" + (ii + 1) + "}}}}]";
            fileCommands.addAll(CommandBuilder.warnAndReplace(target, warning, BlockType.CROSSBOW));

            // Power enchantment
            warning.setText("POWER IS NOT ALLOWED, YOU NAUGHTY BUM!");
            target = "@p[nbt={SelectedItem:{id:\"" + BlockType.BOW + "\",count:1,components:{\"minecraft:enchantments\":{\"" + EnchantmentType.POWER + "\":" + (ii + 1) + "}}}}]";
            fileCommands.addAll(CommandBuilder.warnAndReplace(target, warning, BlockType.BOW));
        }
        // Wolf armor
        warning.setText("WOLF ARMOR IS NOT ALLOWED, YOU NAUGHTY BUM!");
        target = "@p[nbt={SelectedItem:{id:\"" + BlockType.WOLF_ARMOR + "\",count:1}}]";
        fileCommands.addAll(CommandBuilder.warnAndReplace(target, warning, BlockType.LEATHER_HORSE_ARMOR));

        // Suspicious stew
        warning.setText("SUSPICIOUS STEW IS NOT ALLOWED, YOU NAUGHTY BUM!");
        target = "@p[nbt={SelectedItem:{id:\"" + BlockType.SUSPICIOUS_STEW + "\",count:1}}]";
        fileCommands.addAll(CommandBuilder.warnAndReplace(target, warning, BlockType.BOWL));

        return new FileData(FileName.remove_banned_items, fileCommands);
    }

    // Perks for being on the Control Point
    private FileData ControlPointPerks() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Define perk activation times
        ArrayList<Perk> perks = new ArrayList<>();
        perks.add(new Perk(1, new StatusEffect(Effect.SPEED, 999999, 0, false), Sound.BASALT, 3 * singleton.getMinToCPScore()));
        perks.add(new Perk(2, new Attribute(AttributeType.SCALE, 0.8), Sound.CRIMSON, 6 * singleton.getMinToCPScore()));
        perks.add(new Perk(3, new StatusEffect(Effect.HASTE, 999999, 2, false), Sound.WARPED, 12 * singleton.getMinToCPScore()));
        perks.add(new Perk(4, new StatusEffect(Effect.ABSORPTION, 999999, 1, false), Sound.WITHER, 15 * singleton.getMinToCPScore()));


        Entity currentPlayer = new Entity("");
        Entity currentScoreCheck = new Entity("");

        for (int i = 0; i < controlPoints.size(); i++) {
            // Players in a team
            for (Team team : teams) {
                // Display team receiving perk
                for (Perk perk : perks) {
                    // Set variables
                    currentPlayer.setEntity("@p[team=" + team.getName() + ",tag=!" + Tag.ReceivedPerk.extendName(perk.getId()) + "]");
                    currentScoreCheck.setEntity("@e[scores={CP" + (i + 1) + team.getName() + "=" + perk.getActivationTime() + "..}]");

                    // Create text to be displayed
                    ArrayList<TextItem> texts = new ArrayList<>();
                    texts.add(new Text(Color.light_purple, false, false, "TEAM "));
                    texts.add(new Text(team.getColor(), false, false, team.getJSONColor()));
                    texts.add(new Text(Color.light_purple, false, false, " HAS REACHED"));
                    texts.add(new Text(Color.gold, false, false, " PERK " + perk.getId() + "!"));

                    // Display text
                    fileCommands.add(Execute.If(currentScoreCheck, false) +
                            Execute.IfNext(currentPlayer, true) +
                            new TellRaw("@a", texts).sendRaw());

                    // Give rewards
                    String perkReceivers = "@a[team=" + team.getName() + ",tag=!" + Tag.ReceivedPerk.extendName(perk.getId()) + "]";
                    fileCommands.add(Execute.If(currentScoreCheck) +
                            perk.getReward(perkReceivers));

                    // Play sound
                    fileCommands.add(Execute.If(currentScoreCheck, false) +
                            Execute.IfNext(currentPlayer, true) +
                            CommandBuilder.playSound(perk.getSound(), SoundSource.master, "@a", "~", "~50", "~", "100", "1", "0"));

                    // Add tag
                    fileCommands.add(Execute.If(currentScoreCheck) +
                            CommandBuilder.addTag("@a[team=" + team.getName() + "]", Tag.ReceivedPerk.extendName(perk.getId())));
                }
            }

            if (OperationMode.teamCreationInGame) {
                // Individual players
                for (Perk perk : perks) {
                    // Set variables
                    currentPlayer.setEntity("@p[team=,scores={ControlPoint" + (i + 1) + "=" + perk.getActivationTime() + "..},tag=!" + Tag.ReceivedPerk.extendName(perk.getId()) + "]");
                    currentScoreCheck.setEntity("@p[team=,scores={ControlPoint" + (i + 1) + "=" + perk.getActivationTime() + "..}]");

                    // Create text to be displayed
                    ArrayList<TextItem> texts = new ArrayList<>();
                    texts.add(new Select(false, false, currentPlayer.getEntity()));
                    texts.add(new Text(Color.light_purple, false, false, " HAS REACHED"));
                    texts.add(new Text(Color.gold, false, false, " PERK " + perk.getId() + "!"));

                    // Display text
                    fileCommands.add(Execute.If(currentScoreCheck, false) +
                            Execute.IfNext(currentPlayer, true) +
                            new TellRaw("@a", texts).sendRaw());

                    // Give rewards
                    fileCommands.add(Execute.If(currentScoreCheck) +
                            perk.getReward(currentPlayer.getEntity()));

                    // Play sound
                    fileCommands.add(Execute.If(currentScoreCheck, false) +
                            Execute.IfNext(currentPlayer, true) +
                            CommandBuilder.playSound(perk.getSound(), SoundSource.master, "@a", "~", "~50", "~", "100", "1", "0"));

                    // Add tag
                    fileCommands.add(Execute.If(currentScoreCheck) +
                            CommandBuilder.addTag(currentPlayer.getEntity(), Tag.ReceivedPerk.extendName(perk.getId())));
                }
            }
        }

        return new FileData(FileName.control_point_perks, fileCommands);

    }

    // Display quotes during the match
    private FileData DisplayQuotes() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Roll a random number to pick a quote
        fileCommands.add(CommandBuilder.storeRandomNumber(Objective.RandomQuotes, 0, quotes.size() - 1));

        // Pick a quote from the listAdd commentMore actions
        for (int i = 0; i < quotes.size(); i++) {
            fileCommands.add(Execute.If(new Entity("@e[scores={RandomQuotes=" + i + "}]")) +
                    new TellRaw("@a", new Text(Color.white, false, false, quotes.get(i))).sendRaw());

        }

        // Reschedule displaying a new quote
        fileCommands.add(Schedule.callFunction(FileName.display_quotes, 7 * Constant.secPerMinute));

        return new FileData(FileName.display_quotes, fileCommands);
    }

    // Update amount stripmined
    private FileData UpdateMineCount() {
        ArrayList<String> fileCommands = new ArrayList<>();

        fileCommands.add(scoreboard.Set("@s", Objective.Mining, 0));

        ArrayList<String> blocks = new ArrayList<>();
        blocks.add("Stone");
        blocks.add("Deepslate");
        blocks.add("Diorite");
        blocks.add("Andesite");
        blocks.add("Granite");

        for (String block : blocks) {
            fileCommands.add(scoreboard.Operation("@s", getObjectiveByName(Objective.Mining), ComparatorType.ADD, "@s", getObjectiveByName(block)));
        }

        return new FileData(FileName.update_mine_count, fileCommands);
    }

    // Update minimum health
    private FileData UpdateMinHealth() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Find player with lowest health
        fileCommands.add(scoreboard.Operation(Constant.admin, Objective.MinHealth, ComparatorType.LESS, "@a[gamemode=!spectator]", Objective.Hearts));

        return new FileData(FileName.update_min_health, fileCommands);
    }

    // Reset health of respawned players
    private FileData RespawnPlayer() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Define player that needs to be respawned
        String respawnPlayer = "@a[tag=" + Tag.Respawn + "]";

        // Teleport player to their team
        for (Team t : teams) {
            fileCommands.add(Execute.As(new Entity(respawnPlayer)) +
                    CommandBuilder.teleportEntity("@s[team=" + t.getName() + "]", "@r[gamemode=!spectator, team=" + t.getName() + "]"));

            fileCommands.add(Execute.At(respawnPlayer, false) +
                    Execute.AsNext(respawnPlayer) +
                    Execute.UnlessNext("@p[team=" + t.getName() + ",tag=!Respawn]", true) +
                    CommandBuilder.spreadPlayers(0, 0, (int) (0.3 * world.getSize()), (int) (0.7 * world.getSize()), false, "@s[team=" + t.getName() + "]"));
        }

        if (OperationMode.teamCreationInGame) {
            // Teleport player if they are not in a team
            fileCommands.add(Execute.As(new Entity(respawnPlayer)) +
                    CommandBuilder.spreadPlayers(0, 0, (int) (0.3 * world.getSize()), (int) (0.7 * world.getSize()), false, "@s[team=]"));

            // Team caller
            fileCommands.add(Execute.As(new Entity(respawnPlayer)) +
                    CommandBuilder.giveItem("@s[team=]", BlockType.GOAT_HORN, "[instrument=\"minecraft:ponder_goat_horn\",use_cooldown={seconds:30},enchantments={\"" + EnchantmentType.VANISHING_CURSE + "\":1}]"));
        }

        // Remove player heads
        fileCommands.add(Execute.As(new Entity("@a[nbt={Inventory:[{id:\"" + BlockType.PLAYER_HEAD + "\"}]}]")) +
                CommandBuilder.clearInventory("@s", BlockType.PLAYER_HEAD));  // Remove from inventory
        fileCommands.add(Execute.As(new Entity("@e[type=" + EntityType.ITEM + ",nbt={Item:{id:\"" + BlockType.PLAYER_HEAD + "\"}}]")) +
                CommandBuilder.killEntity("@s")); // Remove item

        // Teammate tracker
        for (Team team : teams) {
            fileCommands.add(Execute.As(respawnPlayer) +
                    CommandBuilder.giveItem("@s[team=" + team.getName() + "]", BlockType.BUNDLE.extendColor(team.getGlassColor()), "[enchantments={\"" + EnchantmentType.VANISHING_CURSE + "\":1},custom_data={locateTeammate:1b}]"));
        }

        // Set respawn health
        for (int i = 0; i < 10; i++) {
            int indexFront = 2 * i + 1;
            int indexRear = 2 * (i + 1);

            fileCommands.add(Execute.As(respawnPlayer, false) +
                    Execute.IfNext(new Entity("@e[scores={MinHealth=" + indexFront + ".." + indexRear + "}]"), true) +
                    CommandBuilder.setAttributeBase("@s", AttributeType.MAX_HEALTH, i + 1));
        }
        fileCommands.add(CommandBuilder.giveEffect(respawnPlayer, Effect.HEALTH_BOOST, 1, 0));
        fileCommands.add(CommandBuilder.clearEffect(respawnPlayer, Effect.HEALTH_BOOST));
        fileCommands.add(CommandBuilder.setAttributeBaseMultiple(respawnPlayer, AttributeType.MAX_HEALTH, 20));

        // Set player's gamemode to survival
        fileCommands.add(Execute.As(new Entity(respawnPlayer)) +
                CommandBuilder.setGameMode(GameMode.survival, "@s"));

        // Remove respawn tag
        fileCommands.add(CommandBuilder.removeTag(respawnPlayer, Tag.Respawn));

        return new FileData(FileName.respawn_player, fileCommands);
    }

    private FileData ControlPointCaptured() {
        ArrayList<String> fileCommands = new ArrayList<>();
        ArrayList<TextItem> texts = new ArrayList<>();

        // Announce that Control Point has been captured
        texts.add(bannerText);
        texts.add(new Text(Color.gold, true, false, communityName + " UHC"));
        texts.add(bannerText);
        texts.add(new Text(Color.light_purple, true, false, "THE CONTROL POINT HAS BEEN CAPTURED!"));
        texts.add(bannerText);
        fileCommands.add(new TellRaw("@a", texts).sendRaw());
        texts.clear();
        fileCommands.add(new Title("@a", TitleType.subtitle, new Text(Color.light_purple, true, true, "has been captured!")).displayTitle());
        fileCommands.add(new Title("@a", TitleType.title, new Text(Color.gold, true, true, "The Control Point")).displayTitle());

        // Check which team has captured the Control Point
        fileCommands.add(Schedule.callFunction(FileName.teams_highscore_alive_check));

        // Give admin tag for disabling self-rescheduling
        fileCommands.add(CommandBuilder.addTag(Constant.admin, Tag.ControlPointCaptured));

        return new FileData(FileName.control_point_captured, fileCommands);
    }

    private FileData TraitorCheck() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // When no traitors remain start teams_alive_check
        fileCommands.add(Execute.Unless("@a[limit=1,tag=" + Tag.Traitor + ",gamemode=!spectator]") +
                Schedule.callFunction(FileName.teams_alive_check));

        // When no non-traitors remain, traitors have won
        fileCommands.add(Execute.Unless("@a[limit=1,tag=!" + Tag.Traitor + ",gamemode=!spectator]") +
                Schedule.callFunction(FileName.victory_message_traitor));

        return new FileData(FileName.traitor_check, fileCommands);
    }

    private FileData TeamsAliveCheck() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Players in teams
        for (int i = 0; i < teams.size(); i++) {
            fileCommands.add(Execute.Unless("@a[limit=1,team=!" + teams.get(i).getName() + ",gamemode=!spectator]") +
                    Schedule.callFunction("" + FileName.victory_message_ + i));
        }

        if (OperationMode.teamCreationInGame) {
            // Players without a team
            fileCommands.add(CommandBuilder.addTag("@p[team=,gamemode=!spectator]", Tag.AmIWinning));
            fileCommands.add(Execute.Unless("@p[tag=!AmIWinning,gamemode=!spectator]", false) +
                    Execute.AsNext("@p[tag=AmIWinning]", true) +
                    Schedule.callFunction(FileName.victory_message_solo));
            fileCommands.add(CommandBuilder.removeTag("@p[tag=AmIWinning]", Tag.AmIWinning));
        }

        return new FileData(FileName.teams_alive_check, fileCommands);
    }

    private FileData TeamsHighscoreCheck() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Players in teams
        for (int i = 0; i < teams.size(); i++) {
            for (int j = 1; j < 3; j++) {
                if (OperationMode.traitorFaction) {
                    // Traitor teams
                    fileCommands.add(Execute.If(new Entity("@e[scores={Victory=1}]"), false) +
                            Execute.IfNext("@p[team=" + teams.get(i).getName() + ",gamemode=!spectator,scores={ControlPoint" + j + "=" + maxCPScore + "..},tag=" + Tag.Traitor + "]") +
                            Execute.UnlessNext("@p[team=" + teams.get(i).getName() + ",gamemode=!spectator,tag=!" + Tag.Traitor + "]", true) +
                            Schedule.callFunction(FileName.victory_message_traitor));

                    // Regular teams
                    fileCommands.add(Execute.If(new Entity("@e[scores={Victory=1}]"), false) +
                            Execute.IfNext(new Entity("@p[team=" + teams.get(i).getName() + ",gamemode=!spectator,scores={ControlPoint" + j + "=" + maxCPScore + "..}]"), true) +
                            Schedule.callFunction("" + FileName.victory_message_ + i));
                }
                else {

                    // Regular teams
                    fileCommands.add(Execute.If(new Entity("@e[scores={Victory=1}]"), false) +
                            Execute.IfNext(new Entity("@p[team=" + teams.get(i).getName() + ",gamemode=!spectator,scores={ControlPoint" + j + "=" + maxCPScore + "..}]"), true) +
                            Schedule.callFunction("" + FileName.victory_message_ + i));
                }
            }
        }

        if (OperationMode.teamCreationInGame) {
            // Individual players
            for (int j = 1; j < 3; j++) {
                if (OperationMode.traitorFaction) {
                    // Regular solo
                    fileCommands.add(Execute.If(new Entity("@e[scores={Victory=1}]"), false) +
                            Execute.IfNext(new Entity("@p[team=,gamemode=!spectator,scores={ControlPoint" + j + "=" + maxCPScore + "..},tag=!" + Tag.Traitor + "]")) +
                            Execute.AsNext("@p[team=,gamemode=!spectator,scores={ControlPoint" + j + "=" + maxCPScore + "..},tag=!" + Tag.Traitor + "]", true) +
                            Schedule.callFunction(FileName.victory_message_solo));

                    // Traitor solo
                    fileCommands.add(Execute.If(new Entity("@e[scores={Victory=1}]"), false) +
                            Execute.IfNext("@p[team=,gamemode=!spectator,scores={ControlPoint" + j + "=" + maxCPScore + "..},tag=" + Tag.Traitor + "]", true) +
                            Schedule.callFunction(FileName.victory_message_traitor));
                }
                else {
                    // Solo
                    fileCommands.add(Execute.If(new Entity("@e[scores={Victory=1}]"), false) +
                            Execute.IfNext(new Entity("@p[team=,gamemode=!spectator,scores={ControlPoint" + j + "=" + maxCPScore + "..}]")) +
                            Execute.AsNext("@p[team=,gamemode=!spectator,scores={ControlPoint" + j + "=" + maxCPScore + "..}]", true) +
                            Schedule.callFunction(FileName.victory_message_solo));
                }
            }
        }

        return new FileData(FileName.teams_highscore_alive_check, fileCommands);
    }

    private FileData ClearSchedule() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Post-game functions
        fileCommands.add(Schedule.clearFunction(FileName.minute_ + "2"));
        fileCommands.add(Schedule.clearFunction(FileName.minute_ + "1"));
        fileCommands.add(Schedule.clearFunction(FileName.death_match));

        // Clear timer schedules
        fileCommands.add(Schedule.clearFunction(FileName.timer_main_1));
        fileCommands.add(Schedule.clearFunction(FileName.timer_main_5));
        fileCommands.add(Schedule.clearFunction(FileName.timer_main_20));
        if (OperationMode.controlPoints) {
            fileCommands.add(Schedule.clearFunction(FileName.timer_control_point_20));
        }
        if (OperationMode.traitorFaction) {
            fileCommands.add(Schedule.clearFunction(FileName.timer_traitor_5));
            fileCommands.add(Schedule.clearFunction(FileName.timer_traitor_20));
        }

        // Extra scheduled functions
        fileCommands.add(Schedule.clearFunction(FileName.display_quotes));
        if (OperationMode.eternalDay) {
            fileCommands.add(Schedule.clearFunction(FileName.messages_eternal_day));
        }
        fileCommands.add(Schedule.clearFunction(FileName.messages_pvp));
        fileCommands.add(Schedule.clearFunction(FileName.disable_respawn));

        return new FileData(FileName.clear_schedule, fileCommands);
    }

    private FileData LocateTeammate() {
        ArrayList<String> fileCommands = new ArrayList<>();

        for (Team t : teams) {
            for (int i = 0; i < 3; i++) {
                fileCommands.add(Execute.As(new Entity("@a[team=" + t.getName() + ",nbt={SelectedItem:{id:\"" + BlockType.BUNDLE.extendColor(t.getGlassColor()) + "\",components:{\"minecraft:custom_data\":{locateTeammate:1b}}}}]"), false) +
                        Execute.AtNext(new Entity("@s")) +
                        Execute.IfNext(new Entity("@a[team=" + t.getName() + ",distance=0.1..,gamemode=!spectator]")) +
                        Execute.FacingNext(new Entity("@a[team=" + t.getName() + ",distance=0.1..,gamemode=!spectator,limit=1,sort=random]"), EntityAnchor.eyes) +
                        Execute.PositionedNext(new Coordinate(0, 1, 0, ReferenceFrame.relative)) +
                        Execute.PositionedNext(new Coordinate(0, 0, i + 1, ReferenceFrame.relative_facing), true) +
                        CommandBuilder.createParticle(Particle.dust + "{color:[" + t.getDustColor() + "],scale:1}", new Coordinate(0, 0, 0, ReferenceFrame.relative), new Coordinate(0, 0, 0), 0, 1, "@s"));
            }
        }

        return new FileData(FileName.locate_teammate, fileCommands);
    }

    private FileData WolfUpdates() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Set wolf collar color
        // Get data
        for (int i = 0; i < 2; i++) {
            fileCommands.add(Execute.As(new Entity("@e[type=" + EntityType.WOLF +"]"), false) +
                    Execute.StoreNext(ExecuteStore.result, "@s", getObjectiveByName(Objective.CollarCheck.extendName(i)), true) +
                    CommandBuilder.getData("@s", "Owner[" + i + "]"));

            fileCommands.add(Execute.As(new Entity("@a"), false) +
                    Execute.StoreNext(ExecuteStore.result, "@s", getObjectiveByName(Objective.CollarCheck.extendName(i)), true) +
                    CommandBuilder.getData("@s", "UUID[" + i + "]"));

        }
        // Players in a team
        for (Team t : teams) {
            fileCommands.add(CommandBuilder.addTag("@a[team=" + t.getName() + "]", Tag.CollarCheck));
            fileCommands.add(Execute.As(new Entity("@e[type=" + EntityType.WOLF + "]"), false) +
                    Execute.IfNext("@s", getObjectiveByName(Objective.CollarCheck.extendName(0)), ComparatorType.EQUAL, "@p[tag=" + Tag.CollarCheck + "]", getObjectiveByName(Objective.CollarCheck.extendName(0))) +
                    Execute.IfNext("@s", getObjectiveByName(Objective.CollarCheck.extendName(1)), ComparatorType.EQUAL, "@p[tag=" + Tag.CollarCheck + "]", getObjectiveByName(Objective.CollarCheck.extendName(1)), true) +
                    CommandBuilder.modifyData("@s", "CollarColor", t.getCollarColor()));
            fileCommands.add(CommandBuilder.removeTag("@a[team=" + t.getName() + "]", Tag.CollarCheck));
        }

        if (OperationMode.teamCreationInGame) {
            // Individual players
            fileCommands.add(CommandBuilder.addTag("@a[team=]", Tag.CollarCheck));
            fileCommands.add(Execute.As(new Entity("@e[type=" + EntityType.WOLF + "]"), false) +
                    Execute.IfNext("@s", getObjectiveByName(Objective.CollarCheck.extendName(0)), ComparatorType.EQUAL, "@p[tag=" + Tag.CollarCheck + "]", getObjectiveByName(Objective.CollarCheck.extendName(0))) +
                    Execute.IfNext("@s", getObjectiveByName(Objective.CollarCheck.extendName(1)), ComparatorType.EQUAL, "@p[tag=" + Tag.CollarCheck + "]", getObjectiveByName(Objective.CollarCheck.extendName(1)), true) +
                    CommandBuilder.modifyData("@s", "CollarColor", "0"));
            fileCommands.add(CommandBuilder.removeTag("@a[team=]", Tag.CollarCheck));
        }

        // Eliminate baby wolves
        Entity babyWolf = new Entity("@e[type=" + EntityType.WOLF + ",scores={WolfAge=..-1}]");

        fileCommands.add(Execute.As(new Entity("@e[limit=1,type=" + EntityType.WOLF +",sort=random]"), false) +
                Execute.StoreNext(ExecuteStore.result, "@s", getObjectiveByName(Objective.WolfAge), true) +
                CommandBuilder.getData("@s", "Age"));
        fileCommands.add(Execute.At(babyWolf) +
                CommandBuilder.summonEntity(EntityType.DOLPHIN));
        fileCommands.add(Execute.As(babyWolf) +
                CommandBuilder.killEntity("@s"));

        // Set tamed wolf base health
        fileCommands.add(Execute.As(new Entity("@e[type=" + EntityType.WOLF + "]"), false) +
                Execute.IfNext(DataClasses.entity, "@s Owner", true) +
                CommandBuilder.setAttributeBase("@s", AttributeType.MAX_HEALTH, 20));

        return new FileData(FileName.wolf_updates, fileCommands);
    }

    private FileData UpdatePublicCPScore() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Players in teams
        for (Team t : teams) {
            for (int i = 1; i < controlPoints.size() + 1; i++) {
                fileCommands.add(scoreboard.Operation(t.getPlayerColor(), getObjectiveByName(Objective.CPScore), ComparatorType.GREATER, Constant.admin, getObjectiveByName("" + Objective.CP + i + t.getName())));
            }
        }

        if (OperationMode.teamCreationInGame) {
            // Players without a team
            for (int i = 1; i < controlPoints.size() + 1; i++) {
                fileCommands.add(Execute.As("@a[team=]") +
                        scoreboard.Operation("Solo", getObjectiveByName(Objective.CPScore), ComparatorType.GREATER, "@s", getObjectiveByName(Objective.ControlPoint.extendName(i))));
            }
        }

        return new FileData(FileName.update_public_cp_score, fileCommands);
    }

    private FileData DisableRespawn() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Add first blood tag
        fileCommands.add(CommandBuilder.addTag(Constant.admin, Tag.RespawnDisabled));

        // Update immediate respawn
        fileCommands.add(CommandBuilder.setGameRule(GameRule.doImmediateRespawn, false));

        return new FileData(FileName.disable_respawn, fileCommands);
    }

    private FileData JoinTeam() {
        ArrayList<String> fileCommands = new ArrayList<>();

        String lookingPlayer = "@p[tag=LookingForTeamMate]";

        String filledTeam;
        ArrayList<TextItem> texts = new ArrayList<>();
        for (int i = 0; i < (teams.size() - 1); i++) {
            filledTeam = Execute.Unless("@p[team=" + teams.get(i).getName() + "]", false);

            // Announce that players formed a team
            texts.add(new Select(false, false, "@p[limit=2,team=,gamemode=!spectator]"));
            texts.add(new Text(Color.white, false, false, " have decided to join forces as team "));
            texts.add(new Text(teams.get(i).getColor(), false, false, teams.get(i).getJSONColor()));
            texts.add(new Text(Color.white, false, false, "!"));

            fileCommands.add(filledTeam +
                    Execute.IfNext("@p[tag=LookingForTeamMate,team=]") +
                    Execute.AtNext(lookingPlayer, true) +
                    new TellRaw("@a", texts).sendRaw());
            texts.clear();

            // Try to let players join team
            fileCommands.add(filledTeam +
                    Execute.IfNext("@p[tag=LookingForTeamMate,team=]") +
                    Execute.AtNext(lookingPlayer, true) +
                    teams.get(i).joinTeam("@p[limit=2,team=,gamemode=!spectator]"));
        }

        fileCommands.add(Execute.At(lookingPlayer) +
                CommandBuilder.clearInventory("@p[limit=2,gamemode=!spectator]", BlockType.GOAT_HORN));


        for (Team team : teams) {
            fileCommands.add(Execute.At(lookingPlayer, false) +
                    Execute.IfNext("@p[tag=LookingForTeamMate,team=" + team.getName() + "]", true) +
                    CommandBuilder.giveItem("@p[limit=2,gamemode=!spectator]", BlockType.BUNDLE.extendColor(team.getGlassColor()), "[enchantments={\"" + EnchantmentType.VANISHING_CURSE + "\":1},custom_data={locateTeammate:1b}]"));
        }

        return new FileData(FileName.join_team, fileCommands);
    }

    private FileData UpdatePlayerDistance() {
        ArrayList<String> fileCommands = new ArrayList<>();
        ArrayList<TextItem> texts = new ArrayList<>();

        String checkingPlayer = "@p[team=,scores={TimesCalled=1..}]";
        ComparatorType comparator;

        // Give player playing the horn a tag
        fileCommands.add(CommandBuilder.addTag(checkingPlayer, Tag.LookingForTeamMate));

        // Loop through Cartesian coordinates
        for (int i = 0; i < cartesian.length; i++) {
            // Find positions of each player
            fileCommands.add(Execute.As("@a[team=]", false) +
                    Execute.StoreNext(ExecuteStore.result, "@s", getObjectiveByName(Objective.Pos + cartesian[i]), true) +
                    CommandBuilder.getData("@s", "Pos[" + i + "]", 1));

            // Subtract distance of nearest player in Cartesian coordinate
            fileCommands.add(Execute.As(checkingPlayer, false) +
                    Execute.AtNext(checkingPlayer, true) +
                    scoreboard.Operation("@s", getObjectiveByName(Objective.Pos + cartesian[i]), ComparatorType.SUBTRACT, "@p[tag=!LookingForTeamMate,team=,gamemode=!spectator]", getObjectiveByName(Objective.Pos + cartesian[i])));

            // Square the difference in Cartesian coordinates
            fileCommands.add(Execute.As(checkingPlayer) +
                    scoreboard.Operation("@s", getObjectiveByName(Objective.Square + cartesian[i]), ComparatorType.EQUAL, "@s", getObjectiveByName(Objective.Pos + cartesian[i])));
            fileCommands.add(Execute.As(checkingPlayer) +
                    scoreboard.Operation("@s", getObjectiveByName(Objective.Square + cartesian[i]), ComparatorType.MULTIPLY, "@s", getObjectiveByName(Objective.Pos + cartesian[i])));

            if (i == 0) {
                comparator = ComparatorType.EQUAL;
            } else {
                comparator = ComparatorType.ADD;
            }

            // Calculate distance to nearest player
            fileCommands.add(Execute.As(checkingPlayer) +
                    scoreboard.Operation("@s", getObjectiveByName(Objective.Distance), comparator, "@s", getObjectiveByName(Objective.Square + cartesian[i])));
        }

        // Ignore players that are already in a team
        texts.add(new Text(Color.red, true, false, "You are already on a team! Don't be greedy!"));

        String playerInTeam = "@p[scores={TimesCalled=1..},team=!]";
        fileCommands.add(Execute.If(playerInTeam) +
                new TellRaw(playerInTeam, texts).sendRaw());
        texts.clear();

        // Call join team function if other player is in range
        String playerInRange = "@p[tag=LookingForTeamMate,scores={Distance=.." + (minJoinDistance * minJoinDistance) + ",IsKiller=0},team=]";
        fileCommands.add(Execute.If(playerInRange, false) +
                Execute.AtNext(playerInRange) +
                Execute.UnlessNext("@p[tag=!LookingForTeamMate,gamemode=!spectator]", Objective.IsKiller, 1, true) +
                Schedule.callFunction(FileName.join_team));

        // Refuse call if player is too far away
        texts.add(new Text(Color.red, true, false, "You need to be within " + minJoinDistance + " blocks of a player without a team to form a team!"));

        String playerTooFar = "@p[tag=LookingForTeamMate,scores={Distance=" + (minJoinDistance * minJoinDistance) + "..},team=,gamemode=!spectator]";
        fileCommands.add(Execute.If(playerTooFar, false) +
                Execute.UnlessNext(playerTooFar, Objective.IsKiller, 1, true) +
                new TellRaw(playerTooFar, texts).sendRaw());
        texts.clear();

        // Refuse killers
        texts.add(new Text(Color.red, true, false, "You are a killer! No team for you!"));

        String playerKiller = "@p[tag=LookingForTeamMate,scores={IsKiller=1}]";
        fileCommands.add(Execute.If(playerKiller) +
                new TellRaw(playerKiller, texts).sendRaw());
        texts.clear();

        // Warn against killers
        texts.add(new Text(Color.red, true, false, "Watch out! They are a killer!"));

        fileCommands.add(Execute.At(playerInRange, false) +
                Execute.IfNext("@p[tag=!LookingForTeamMate,gamemode=!spectator]", Objective.IsKiller, 1, true) +
                new TellRaw(playerInRange, texts).sendRaw());
        texts.clear();

        // Reset tag and call scoreboard objective
        fileCommands.add(CommandBuilder.removeTag("@p[scores={TimesCalled=1..}]", Tag.LookingForTeamMate));
        fileCommands.add(scoreboard.Reset("@p[scores={TimesCalled=1..}]", getObjectiveByName(Objective.TimesCalled)));

        return new FileData(FileName.update_player_distance, fileCommands);
    }

    private FileData CheckIronMan() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Give random player with no damage taken iron man candidate
        fileCommands.add(CommandBuilder.addTag("@r[scores={DamageTaken=.." + minDamage + "}]", Tag.IronManCandidate));

        // Check if there are other potential iron man candidates
        fileCommands.add(Execute.Unless("@a[tag=!IronManCandidate,scores={DamageTaken=.." + minDamage + "}]", false) +
                Execute.AsNext("@p[tag=IronManCandidate]", true) +
                Schedule.callFunction(FileName.announce_iron_man));

        // Remove iron man candidate tag
        fileCommands.add(CommandBuilder.removeTag("@p[tag=IronManCandidate]", Tag.IronManCandidate));

        return new FileData(FileName.check_iron_man, fileCommands);
    }

    private FileData AnnounceIronMan() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Announce iron man
        ArrayList<TextItem> texts = new ArrayList<>();
        texts.add(new Select(false, false, "@s"));
        texts.add(new Text(Color.white, false, false, " is S" + uhcNumber + " iron man!"));
        fileCommands.add(new TellRaw("@a", texts).sendRaw());

        // Award the iron man with their crown
        fileCommands.add(CommandBuilder.addTag("@s", Tag.IronMan));

        return new FileData(FileName.announce_iron_man, fileCommands);
    }

    private FileData DebugGive() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Give player Debug tag
        fileCommands.add(CommandBuilder.addTag("@s", Tag.Debug));

        return new FileData(FileName.debug_give, fileCommands);
    }

    private FileData DebugRemove() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Remove Debug tag from player
        fileCommands.add(CommandBuilder.removeTag("@s", Tag.Debug));

        return new FileData(FileName.debug_remove, fileCommands);
    }

    private FileData DeveloperPotionControl() {
        // Turn potion effect into function execution
        ArrayList<String> fileCommands = new ArrayList<>();

        Effect[] effects = {Effect.SPEED, Effect.WEAKNESS, Effect.SLOW_FALLING, Effect.INVISIBILITY, Effect.POISON, Effect.STRENGTH, Effect.SLOWNESS};
        FileName[] functions = {FileName.developer_mode, FileName.random_teams, FileName.predictions, FileName.into_calls, FileName.spread_players, FileName.survival_mode, FileName.start_game};

        for (int i = 0; i < effects.length; i++) {
            fileCommands.add(Execute.If("@a[gamemode=creative,nbt={active_effects:[{id:\"" + effects[i] + "\"}]}]") +
                    Schedule.callFunction(functions[i]));

            fileCommands.add(Execute.If("@a[nbt={active_effects:[{id:\"" + effects[i] + "\"}]}]") +
                    CommandBuilder.clearEffect("@e", effects[i]));
        }

        return new FileData(FileName.developer_potion_control, fileCommands);
    }

    private FileData ScheduleSingleMessages() {
        // Schedule messages that are only shown once
        ArrayList<String> fileCommands = new ArrayList<>();

        fileCommands.add(Schedule.callFunction(FileName.messages_pvp, 5 * Constant.secPerMinute));
        if (OperationMode.eternalDay) {
            fileCommands.add(Schedule.callFunction(FileName.messages_eternal_day, 20 * Constant.secPerMinute));
        }

        return new FileData(FileName.messages_schedule_single, fileCommands);
    }

    private FileData MessagePVP() {
        // Schedule messages that are only shown once
        ArrayList<String> fileCommands = new ArrayList<>();

        // Message
        fileCommands.add(new TellRaw("@a", new Text(Color.gray, false, false, "PVP IS NOT ALLOWED UNTIL DAY 2!")).sendRaw());

        return new FileData(FileName.messages_pvp, fileCommands);
    }

    private FileData MessageEternalDay() {
        // Schedule messages that are only shown once
        ArrayList<String> fileCommands = new ArrayList<>();

        // Message
        ArrayList<TextItem> texts = new ArrayList<>();
        texts.add(bannerText);
        texts.add(new Text(Color.gold, true, false, communityName + " UHC"));
        texts.add(bannerText);
        texts.add(new Text(Color.light_purple, true, false, "DAY TIME HAS ARRIVED & ETERNAL DAY ENABLED!"));
        texts.add(bannerText);
        fileCommands.add(new TellRaw("@a", texts).sendRaw());
        texts.clear();

        // Set gamerule
        fileCommands.add(CommandBuilder.setGameRule(GameRule.doDaylightCycle, false));

        return new FileData(FileName.messages_eternal_day, fileCommands);
    }

}
