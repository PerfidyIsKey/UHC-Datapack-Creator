import EntityClasses.Attributes;
import EntityClasses.JumpStrength;
import EntityClasses.MovementSpeed;
import Enums.*;
import FileGeneration.*;
import HelperClasses.*;
import ItemClasses.*;
import ItemModifiers.*;
import Predicates.*;
import TeamGeneration.*;

import java.io.IOException;
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
    private String dataPackLocation;
    private String worldLocation;
    private String dataPackName;

    private String fileLocation;
    private CommunityMode communityMode = CommunityMode.DIORITE;
    private int teamMode;
    //DatapackData>

    //GameData<
    private static final int chestSize = 27;
    private static final String commandCenter = "s58";
    private static final String admin = "@e[type=marker]";
    private static final String adminSingle = "@e[type=marker,limit=1]";
    private Coordinate startCoordinate;
    private ArrayList<Team> teams = new ArrayList<>();
    private ArrayList<ControlPoint> cpList = new ArrayList<>();
    private ArrayList<ControlPoint> controlPoints = new ArrayList<>();
    private ArrayList<ScoreboardObjective> scoreboardObjectives = new ArrayList<>();
    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Season> seasons = new ArrayList<>();
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
    private static final int minJoinDistance = 10;
    private static final int minDamage = 9;
    private static final String[] cartesian = {"X", "Y", "Z"};
    private static int carePackageAmount;
    private static int carePackageSpread;
    private int minTraitorRank;
    private int traitorWaitTime;
    private static final int traitorMode = 1;
    private String communityName;
    private static final Execute execute = new Execute();
    private static final Scoreboard scoreboard = new Scoreboard();

    private final Text bannerText = new Text(Color.dark_gray, true, false, " | ");

    private TeamGenerator teamGenerator;

    //GameData>


    private ArrayList<FileData> files = new ArrayList<>();


    private void run(String[] args) {

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

    private void changeCommunitymode(int num) {
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

    private void communityModeChange() {
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
        uhcNumber = fileTools.getContentOutOfFile("Files\\" + communityMode + "\\uhc_data.txt", "uhcNumber");

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
        players = new ArrayList<>();
        seasons = new ArrayList<>();
        quotes = new ArrayList<>();

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

        // Bossbars
        bossBars.add(new BossBar("cp1"));
        bossBars.add(new BossBar("cp2"));

        // Data
        String[] splitStartCoordinates = fileTools.splitLineOnComma(fileTools.getContentOutOfFile("Files\\" + communityMode + "\\uhc_data.txt", "startCoordinate"));
        startCoordinate = new Coordinate(Integer.parseInt(splitStartCoordinates[0]), Integer.parseInt(splitStartCoordinates[1]), Integer.parseInt(splitStartCoordinates[2]));
        minTraitorRank = Integer.parseInt(fileTools.getContentOutOfFile("Files\\" + communityMode + "\\uhc_data.txt", "minTraitorRank"));
        traitorWaitTime = Integer.parseInt(fileTools.getContentOutOfFile("Files\\" + communityMode + "\\uhc_data.txt", "traitorWaitTime"));
        communityName = fileTools.getContentOutOfFile("Files\\" + communityMode + "\\uhc_data.txt", "communityName");

        // ControlPoints
        ArrayList<String> controlPointString = fileTools.GetLinesFromFile("Files\\" + communityMode + "\\controlPoints.txt");
        for (String controlPoint : controlPointString) {
            String[] controlPointSplit = fileTools.splitLineOnComma(controlPoint);
            cpList.add(new ControlPoint("CP", maxCPScoreBossbar, 0, new Coordinate(Integer.parseInt(controlPointSplit[0]), Integer.parseInt(controlPointSplit[1]), Integer.parseInt(controlPointSplit[2])), Biome.valueOf(controlPointSplit[3])));
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

        // World size based on amount of players
        if (players.size() <= 6) {
            worldSize = 500;
            carePackageSpread = 450;
            carePackageAmount = 200;
        }
        else if (players.size() <= 20) {
            worldSize = 750;
            carePackageSpread = 500;
            carePackageAmount = 200;
        }
        else {
            worldSize = 1000;
            carePackageSpread = 750;
            carePackageAmount = 450;
        }

        // Seasons
        ArrayList<String> seasonsString = fileTools.GetLinesFromFile("Files\\" + communityMode + "\\seasonData.txt");
        for (String season : seasonsString) {
            String[] seasonSplit = fileTools.splitLineOnComma(season);
            seasons.add(new Season(Double.parseDouble(seasonSplit[0]), Integer.parseInt(seasonSplit[1]), new Date(Integer.parseInt(seasonSplit[2]), Integer.parseInt(seasonSplit[3]), Integer.parseInt(seasonSplit[4]))));
        }

        // Quotes
        quotes = fileTools.GetLinesFromFile("Files\\" + communityMode + "\\quotes.txt");

        int[] addRates = {2, 3};
        Collections.shuffle(cpList);
        for (int i = 0; i < addRates.length; i++) {
            controlPoints.add(cpList.get(i));
            controlPoints.get(i).setAddRate(addRates[i]);
            controlPoints.get(i).setName("CP" + (i + 1));
        }

        // Scoreboard objectives
        scoreboardObjectives.add(new ScoreboardObjective(Objective.TimDum, ObjectiveType.dummy));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.TimeDum, ObjectiveType.dummy, "\"Elapsed Time\""));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.Time, ObjectiveType.dummy, "\"Elapsed Time\"", true));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.Time.extendName(2), ObjectiveType.dummy, "\"Elapsed Time\""));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.SideDum, ObjectiveType.dummy));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.CPScore, ObjectiveType.dummy, "\"Control Point score\"", true));
        for (int i = 0; i < 2; i++) {
            scoreboardObjectives.add(new ScoreboardObjective(Objective.Highscore.extendName(i + 1), ObjectiveType.dummy));
            scoreboardObjectives.add(new ScoreboardObjective(Objective.ControlPoint.extendName(i + 1), ObjectiveType.dummy));
            scoreboardObjectives.add(new ScoreboardObjective(Objective.CollarCheck.extendName(i), ObjectiveType.dummy));
            for (int j = 0; j < 2; j++) {
                scoreboardObjectives.add(new ScoreboardObjective(Objective.MSGDum.extendName((i + 1) + "CP" + (j + 1)), ObjectiveType.dummy));
            }
        }
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
        scoreboardObjectives.add(new ScoreboardObjective(Objective.TempKills, ObjectiveType.playerKillCount));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.Rank, ObjectiveType.dummy));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.WorldLoad, ObjectiveType.dummy));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.MinHealth, ObjectiveType.dummy));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.IsKiller, ObjectiveType.dummy));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.Victory, ObjectiveType.dummy));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.WolfAge, ObjectiveType.dummy));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.FoundTeam, ObjectiveType.dummy));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.Distance, ObjectiveType.dummy));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.TimesCalled, "minecraft.used:minecraft.goat_horn"));
        scoreboardObjectives.add(new ScoreboardObjective(Objective.DamageTaken, "minecraft.custom:minecraft.damage_taken"));
        for (String s : cartesian) {
            scoreboardObjectives.add(new ScoreboardObjective(Objective.Pos + s, ObjectiveType.dummy));
            scoreboardObjectives.add(new ScoreboardObjective(Objective.Square + s, ObjectiveType.dummy));
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
        entries.add(new LootTableEntry(17, BlockType.egg));

        // Entry #2
        entries.add(new LootTableEntry(17, BlockType.ladder, new SetCount(10)));

        // Entry #3
        entries.add(new LootTableEntry(15, BlockType.stick, new SetCount(8)));

        // Entry #4
        entries.add(new LootTableEntry(15, BlockType.diorite, new SetCount(16)));

        // Entry #5
        entries.add(new LootTableEntry(15, BlockType.amethyst_block, new SetCount(16)));

        // Entry #6
        entries.add(new LootTableEntry(15, BlockType.iron_ingot, new SetCount(8)));

        // Entry #7
        entries.add(new LootTableEntry(14, BlockType.arrow, new SetCount(10)));

        // Entry #8
        entries.add(new LootTableEntry(11, BlockType.bone, new SetCount(3, new RandomChance(0.4))));

        // Entry #9
        entries.add(new LootTableEntry(10, BlockType.copper_block, new SetCount(16)));

        // Entry #10
        entries.add(new LootTableEntry(10, BlockType.bread, new SetCount(5)));

        // Entry #11
        entries.add(new LootTableEntry(10, BlockType.cobweb, new SetCount(2, new RandomChance(0.4))));

        // Entry #12
        Enchantments enchantment = new Enchantments(EnchantmentType.lure, 3);
        entries.add(new LootTableEntry(8, BlockType.fishing_rod, new SetComponents(enchantment)));

        // Entry #13
        entries.add(new LootTableEntry(8, BlockType.obsidian, new SetCount(4)));

        // Entry #14
        entries.add(new LootTableEntry(7, BlockType.glass, new SetCount(3)));

        // Entry #15
        entries.add(new LootTableEntry(7, BlockType.melon_slice, new SetCount(3, new RandomChance(0.4))));

        // Entry #16
        entries.add(new LootTableEntry(5, BlockType.tnt, new SetCount(4)));

        // Entry #17
        entries.add(new LootTableEntry(5, BlockType.experience_bottle, new SetCount(3, new RandomChance(0.2))));

        // Entry #18
        entries.add(new LootTableEntry(5, BlockType.book));

        // Entry #19
        entries.add(new LootTableEntry(5, BlockType.redstone, new SetCount(16)));

        // Entry #20
        entries.add(new LootTableEntry(5, BlockType.gunpowder, new SetCount(16)));

        // Entry #21
        entries.add(new LootTableEntry(5, BlockType.gold_ingot, new SetCount(4, new RandomChance(0.3))));

        // Entry #22
        entries.add(new LootTableEntry(5, BlockType.lapis_lazuli, new SetCount(10)));

        // Entry #23
        entries.add(new LootTableEntry(4, BlockType.lava_bucket));

        // Entry #24
        entries.add(new LootTableEntry(4, BlockType.apple, new SetCount(2, new RandomChance(0.3))));

        // Entry #25
        entries.add(new LootTableEntry(2, BlockType.diamond, new SetCount(2, new RandomChance(0.3))));

        // Entry #26
        entries.add(new LootTableEntry(3, BlockType.saddle));

        // Entry #27
        entries.add(new LootTableEntry(3, BlockType.spectral_arrow, new SetCount(10)));

        // Entry #28
        ArrayList<Attributes> attributes = new ArrayList<>();
        attributes.add(new JumpStrength(1));
        attributes.add(new MovementSpeed(0.1));
        Text text = new Text(false, false, "Driftwood");
        EntityData horse = new Horse(60, true, 5, text, attributes);

        SetName name = new SetName(new Text(false, false, "Driftwood's return"));

        functions.add(new SetComponents(horse));
        functions.add(name);

        entries.add(new LootTableEntry(10, BlockType.horse_spawn_egg, functions));
        functions = new ArrayList<>();

        // Entry #29
        entries.add(new LootTableEntry(3, BlockType.glowstone_dust, new SetCount(6)));

        // Entry #30
        entries.add(new LootTableEntry(3, BlockType.ender_pearl, new SetCount(2, new RandomChance(0.5))));

        // Entry #31
        entries.add(new LootTableEntry(2, BlockType.nether_wart, new SetCount(5)));

        // Entry #32
        entries.add(new LootTableEntry(2, BlockType.blaze_rod, new SetCount(2, new RandomChance(0.1))));

        // Entry #33
        entries.add(new LootTableEntry(2, BlockType.golden_apple));

        // Entry #34
        entries.add(new LootTableEntry(2, BlockType.anvil));

        // Entry #35
        entries.add(new LootTableEntry(4, BlockType.spyglass));

        // Entry #36
        entries.add(new LootTableEntry(2, BlockType.wolf_spawn_egg, new SetCount(2, new RandomChance(0.01))));

        // Entry #37
        entries.add(new LootTableEntry(1, BlockType.diamond_horse_armor));

        // Entry #38
        entries.add(new LootTableEntry(1, BlockType.netherite_hoe));

        // Entry #39
        enchantment = new Enchantments(EnchantmentType.loyalty, 3);
        entries.add(new LootTableEntry(1, BlockType.trident, new SetComponents(enchantment)));

        // Entry #40
        entries.add(new LootTableEntry(1, BlockType.netherite_upgrade_smithing_template));

        // Entry #42
        RandomChance condition = new RandomChance(0.001);
        entries.add(new LootTableEntry(1, BlockType.netherite_scrap, new SetCount(4, condition)));

        // Entry #43
        PotionContents contents = new PotionContents(Effect.luck, 0, 600, "59C106", true, false, true);
        name = new SetName(new Text(false, false, "Potion of Care Package luck"));

        functions.add(new SetComponents(contents));
        functions.add(name);

        entries.add(new LootTableEntry(2, BlockType.splash_potion, functions));
        functions = new ArrayList<>();

        // Entry #44
        contents = new PotionContents(Effect.poison, 0, 5, "4E9331", false, true, true);
        name = new SetName(new Text(false, false, "Potion of Poison"));

        functions.add(new SetComponents(contents));
        functions.add(name);

        entries.add(new LootTableEntry(2, BlockType.splash_potion, functions));
        functions = new ArrayList<>();

        // Entry #45
        contents = new PotionContents(Effect.blindness, 0, 10, "1F1F23", false, true, true);
        MaxStackSize stack = new MaxStackSize(64);
        name = new SetName(new Text(false, false, "Potion of Blindness"));
        SetCount count = new SetCount(5, new RandomChance(0.3));

        components.add(contents);
        components.add(stack);

        functions.add(new SetComponents(components));
        functions.add(name);
        functions.add(count);

        entries.add(new LootTableEntry(2, BlockType.splash_potion, functions));
        functions = new ArrayList<>();
        components = new ArrayList<>();

        // Entry #46
        attributes = new ArrayList<>();
        attributes.add(new JumpStrength(0.7));
        attributes.add(new MovementSpeed(0.34));
        text = new Text(false, false, "Scuderia");
        horse = new Horse(5, true, 4, text, attributes);

        name = new SetName(new Text(false, false, "Grazie Ragazzi"));

        functions.add(new SetComponents(horse));
        functions.add(name);

        entries.add(new LootTableEntry(2, BlockType.horse_spawn_egg, functions));
        functions = new ArrayList<>();

        // Entry #47
        // Set title and author
        String title = "Terraria [Ep1]";
        String author = "Mr9Madness";

        // Compile pages
        ArrayList<ArrayList<TextItem>>pages = new ArrayList<>();
        ArrayList<TextItem>texts = new ArrayList<>();
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
        entries.add(new LootTableEntry(1, BlockType.written_book, new SetComponents(components)));
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


        texts.add(new Text(false, false,"Here are the teams:\\n" +
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
        entries.add(new LootTableEntry(1, BlockType.written_book, new SetComponents(components)));
        components = new ArrayList<>();

        // Entry #49
        entries.add(new LootTableEntry(2, BlockType.wind_charge, new SetCount(5)));

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

    // Get by name functions
    private BossBar getBossbarByName(String name) {
        return bossBars.stream().filter(bossBar -> name.equals(bossBar.getName())).findAny().orElse(null);
    }

    private ScoreboardObjective getObjectiveByName(String name) {
        return scoreboardObjectives.stream().filter(objective -> name.equals(objective.getName())).findAny().orElse(null);
    }

    private ScoreboardObjective getObjectiveByName(Objective name) {
        return scoreboardObjectives.stream().filter(objective -> name.toString().equals(objective.getName())).findAny().orElse(null);
    }

    // Call function through other function
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

   // Clear schedule
    private String clearFunction(String functionName) {
        return "schedule clear uhc:" + functionName;
    }

    private String clearFunction(FileName functionName) {
        return clearFunction("" + functionName);
    }

   // Setblock
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

    private String addForceLoad(int x1, int z1, int x2, int z2) { return "forceload add " + x1 + " " + z1 + " " + x2 + " " + z2; }

    private String removeForceLoad(int x1, int z1, int x2, int z2) { return "forceload remove " + x1 + " " + z1 + " " + x2 + " " + z2; }

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

    // Fill blocks
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

    // Gamerules
    private String setGameRule(GameRule gamerule, boolean bool) {
        return setGameRule(gamerule, "" + bool);
    }

    private String setGameRule(GameRule gamerule, int num) {
        return setGameRule(gamerule, "" + num);
    }

    private String setGameRule(GameRule gamerule, String string) {
        return "gamerule " + gamerule + " " + string;
    }

    // Play sound
    private String playSound(Sound sound, SoundSource source, String entity, String x, String y, String z, String x1, String y1, String z1) {
        return "playsound " + sound.getValue() + " " + source + " " + entity + " " + x + " " + y + " " + z + " " + x1 + " " + y1 + " " + z1;
    }

    private String setAttributeBase(String entity, AttributeType attribute, double value) {
        return "attribute " + entity + " minecraft:" + attribute + " base set " + value;
    }

    // Status effects
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

    // Difficulty
    private String setDifficulty(Difficulty difficulty) { return "difficulty " + difficulty; }

    // Gamemode
    private String setDefaultGameMode(GameMode gameMode) { return "defaultgamemode " + gameMode; }

    private String setGameMode(GameMode gameMode, String entity) { return "gamemode " + gameMode + " " + entity; }

    // Set world spawn
    private String setWorldSpawn(Coordinate coordinate) { return "setworldspawn " + coordinate.getCoordinateString(); }

    // Entities
    private String summonEntity(String entity) { return summonEntity(entity, new Coordinate(0, 0, 0, ReferenceFrame.relative)); }

    private String summonEntity(String entity, Coordinate coordinate) { return "summon minecraft:" + entity + " " + coordinate.getCoordinateString(); }

    private String summonEntity(String entity, String nbt) { return summonEntity(entity, new Coordinate(0, 0, 0, ReferenceFrame.relative), nbt); }

    private String summonEntity(String entity, Coordinate coordinate, String nbt) { return "summon minecraft:" + entity + " " + coordinate.getCoordinateString() + " " + nbt; }

    private String summonEntity(EntityType entity) { return summonEntity(entity, new Coordinate(0, 0, 0, ReferenceFrame.relative)); }

    private String summonEntity(EntityType entity, Coordinate coordinate) { return "summon minecraft:" + entity + " " + coordinate.getCoordinateString(); }

    private String summonEntity(EntityType entity, String nbt) { return summonEntity(entity, new Coordinate(0, 0, 0, ReferenceFrame.relative), nbt); }

    private String summonEntity(EntityType entity, Coordinate coordinate, String nbt) { return "summon minecraft:" + entity + " " + coordinate.getCoordinateString() + " " + nbt; }

    private String killEntity(String entity) { return "kill " + entity; }

    // Teleportation
    private String teleportEntity(String entity, Coordinate coordinate) { return "tp " + entity + " " + coordinate.getCoordinateString(); }

    private String teleportEntity(String entity1, String entity2) { return "tp " + entity1 + " " + entity2; }

    // Tags
    private String addTag(String entity, Tag tag) { return "tag " + entity + " add " + tag; }

    private String addTag(String entity, String tag) { return "tag " + entity + " add " + tag; }

    private String removeTag(String entity, Tag tag) { return "tag " + entity + " remove " + tag; }

    private String removeTag(String entity, String tag) { return "tag " + entity + " remove " + tag; }

    // Give item
    private String giveItem(String entity, BlockType item) { return giveItem(entity, item, ""); }

    private String giveItem(String entity, BlockType item, String nbt) { return "give " + entity + " " + item + nbt; }

    private String giveItem(String entity, String item, String nbt) { return "give " + entity + " " + item + nbt; }

    private String replaceItem(String targets, InventorySlot slot, BlockType item) { return "item replace entity " + targets + " " + slot + " with " + item; }

    private String replaceItem(String targets, String slot, BlockType item) { return "item replace entity " + targets + " " + slot + " with " + item; }

    private String replaceItem(String targets, InventorySlot slot, BlockType item, int count) { return "item replace entity " + targets + " " + slot + " with " + item + " " + count; }

    private String replaceItem(String targets, String slot, BlockType item, int count) { return "item replace entity " + targets + " " + slot + " with " + item + " " + count; }

    private String replaceItem(String targets, InventorySlot slot, String item) { return "item replace entity " + targets + " " + slot + " with " + item; }

    private String replaceItem(String targets, String slot, String item) { return "item replace entity " + targets + " " + slot + " with " + item; }

    private String replaceItem(String targets, InventorySlot slot, String item, int count) { return "item replace entity " + targets + " " + slot + " with " + item + " " + count; }

    private String replaceItem(String targets, String slot, String item, int count) { return "item replace entity " + targets + " " + slot + " with " + item + " " + count; }


    // Worldborder
    private String setWorldBorder(int size, int duration) { return "worldborder set " + size + " " + duration; }

    private String setWorldBorder(int size) { return "worldborder set " + size; }

    // Spreadplayers
    private String spreadPlayers(int xCenter, int yCenter, int minRange, int maxRange, Boolean respectTeam, String entities) { return "spreadplayers " + xCenter + " " + yCenter + " " + minRange + " " + maxRange + " " + respectTeam + " " + entities; }

    // Experience
    private String setExperience(String target, int amount, ExperienceType type) { return "xp set " + target + " " + amount + " " + type; }

    // Advancements
    private String revokeAdvancement(String target) { return "advancement revoke " + target + " everything"; }

    // Data
    private String getData(String target, String path) { return "data get entity " + target + " " + path; }

    private String getData(String target, String path, int scale) { return "data get entity " + target + " " + path + " " + scale; }

    private String modifyData(String target, String targetPath, String value) { return "data modify entity " + target + " " + targetPath + " set value " + value + "b"; }

    // Clear inventory
    private String clearInventory(String targets, BlockType item) { return "clear " + targets + " " + item; }

    private String clearInventory(String targets) { return "clear " + targets; }

    // Game time
    private String setTime(int time) { return "time set " + time; }

    // Recipes
    private String giveRecipe(String targets, BlockType recipe) { return "recipe give " + targets + " " + recipe; }

    private String giveRecipe(String targets, String recipe) { return "recipe give " + targets + " " + recipe; }

    private String takeRecipe(String targets, BlockType recipe) { return "recipe take " + targets + " " + recipe; }

    private String takeRecipe(String targets, String recipe) { return "recipe take " + targets + " " + recipe; }

    // Particle
    private String createParticle(Particle name, Coordinate pos, Coordinate delta, int speed, int count, String viewers) {
        return "particle " + name + " " + pos.getCoordinateString() + " " + delta.getCoordinateString() + " " + speed + " " + count + " normal " + viewers;
    }

    private String createParticle(String name, Coordinate pos, Coordinate delta, int speed, int count, String viewers) {
        return "particle " + name + " " + pos.getCoordinateString() + " " + delta.getCoordinateString() + " " + speed + " " + count + " normal " + viewers;
    }

    // Potions
    private String giveSplashPotion(String targets, int slotNumber, Effect effect, String colorHex, String displayName, String lore) {
        // Convert hex to decimal
        int potionColor = Integer.parseInt(colorHex, 16);

        return "item replace entity " + targets + " " + InventorySlot.hotbar.setSlotNumber(slotNumber) + " with " + BlockType.splash_potion + "[potion_contents={custom_color:" + potionColor + ",custom_effects:[{id:" + effect + ",amplifier:0,duration:200,show_particles:0b,show_icon:0b,ambient:0b}]},lore=['\"" + lore + "\"'],custom_name='\"" + displayName + "\"']";
    }

    // Trigger
    private String setTrigger(ScoreboardObjective objective) {
        return "trigger " + objective.getName();
    }

    // Change title display time
    private String changeTitleDisplayTime(String targets, int fadeIn, int duration, int fadeOut) {
        return changeTitleDisplayTime(targets, fadeIn, duration, fadeOut, Duration.seconds);
    }

    private String changeTitleDisplayTime(String targets, int fadeIn, int duration, int fadeOut, Duration durationType) {
        return "title " + targets + " times " + fadeIn + durationType +  " " + duration + durationType + " " + fadeOut + durationType;
    }

    private String changeTitleDisplayTime(String targets, String fadeIn, String duration, String fadeOut) {
        return "title " + targets + " times " + fadeIn + " " + duration + " " + fadeOut;
    }

    // Create function files
    private void makeFunctionFiles() {
        files.add(Initialize());
        files.add(DropPlayerHeads());
        files.add(BossBarValue());
        files.add(ClearEnderChest());
        files.add(EquipGear());
        files.add(GodMode());
        files.add(DeveloperMode());
        files.add(GetStartPotions());

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
        files.add(VictoryMessageSolo());
        files.add(VictoryTraitor());
        files.add(InitiateDeathMatch());
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
        files.add(JoinTeam());
        files.add(AnnounceIronMan());
        files.add(CheckIronMan());
        files.add(UpdatePlayerDistance());
        files.add(DebugGive());
        files.add(DebugRemove());
        files.add(TitleDefaultTiming());
        files.add(CurrentTestFunction());
    }

    private FileData Initialize() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Set gamerules
        fileCommands.add(setGameRule(GameRule.naturalRegeneration, false));
        fileCommands.add(setGameRule(GameRule.doImmediateRespawn, true));
        fileCommands.add(setGameRule(GameRule.doPatrolSpawning, false));
        fileCommands.add(setGameRule(GameRule.doMobSpawning, false));
        fileCommands.add(setGameRule(GameRule.doWeatherCycle, false));

        // Set difficulty
        fileCommands.add(setDifficulty(Difficulty.hard));

        // Set default gamemode
        fileCommands.add(setDefaultGameMode(GameMode.adventure));

        // Set world spawn
        fileCommands.add(setWorldSpawn(new Coordinate(0, 221, 0)));

        // Create scoreboard objectives
        for (ScoreboardObjective objective : scoreboardObjectives) {
            fileCommands.add(objective.add());
        }
        fileCommands.add(new ScoreboardObjective().setDisplay(ScoreboardLocation.below_name, Objective.Hearts));
        fileCommands.add(new ScoreboardObjective().setDisplay(ScoreboardLocation.list, Objective.Hearts));

        // Create bossbars
        fileCommands.add(getBossbarByName("cp1").remove());
        fileCommands.add(getBossbarByName("cp2").remove());
        fileCommands.add(getBossbarByName("cp1").add(controlPoints.get(0).getName() + ": " + controlPoints.get(0).getCoordinate().getX() + ", " + controlPoints.get(0).getCoordinate().getY() + ", " + controlPoints.get(0).getCoordinate().getZ() + " (" + controlPoints.get(0).getCoordinate().getDimensionName() + ")"));
        fileCommands.add(getBossbarByName("cp1").setMax(controlPoints.get(0).getMaxVal()));
        fileCommands.add(getBossbarByName("cp2").add(controlPoints.get(1).getName() + " soon: " + controlPoints.get(1).getCoordinate().getX() + ", " + controlPoints.get(1).getCoordinate().getY() + ", " + controlPoints.get(1).getCoordinate().getZ() + " (" + controlPoints.get(1).getCoordinate().getDimensionName() + ")"));
        fileCommands.add(getBossbarByName("cp2").setMax(controlPoints.get(1).getMaxVal()));

        // Create teams
        for (Team t : teams) {
            fileCommands.add(t.add());
            fileCommands.add(t.setTeamColor());
            for (int i = 1; i < controlPoints.size() + 1; i++) {
                scoreboardObjectives.add(new ScoreboardObjective(Objective.CP.toString() + i + t.getName(), ObjectiveType.dummy));
                fileCommands.add(scoreboardObjectives.get(scoreboardObjectives.size() - 1).add());
            }
        }

        // Create staging area
        fileCommands.add(execute.In(Dimension.overworld) +
                fill(-6, 220, -6, 6, 226, 6, BlockType.barrier));
        fileCommands.add(execute.In(Dimension.overworld) +
                fill(-5, 221, -5, 5, 226, 5, BlockType.air));
        fileCommands.add(execute.In(Dimension.overworld) +
                setBlock(0, 222, -5, BlockType.cherry_wall_sign + "[facing=south,waterlogged=false]{back_text:{messages:['{\"text\":\"You have\"}','{\"text\":\"angered\"}','{\"text\":\"the Gods!\"}','{\"text\":\"\"}']},front_text:{messages:['{\"text\":\"Teleport\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"" + teleportEntity("@s", new Coordinate(5, worldBottom + 5, 5)) + "\"}}','{\"text\":\"to the\"}','{\"text\":\"Command center\"}','{\"text\":\"\"}']},is_waxed:0b}"));

        // Create command center
        fileCommands.add(execute.In(Dimension.overworld) +
                setBlock(-2, worldBottom, -2, BlockType.structure_block + "[mode=load]{metadata:\"\",mirror:\"NONE\",ignoreEnti" +
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

        return new FileData(FileName.initialize, fileCommands);
    }

    private FileData PlayerDeathHandler() {
        ArrayList<String> fileCommands = new ArrayList<>();
        Boolean debug = false;

        // Indicate when the first 20 minutes of the game have elapsed
        fileCommands.add(execute.If("@e[scores={Time2=24000..}]", false) +
                execute.UnlessNext("@e[tag=" + Tag.RespawnDisabled + "]", true) +
                callFunction(FileName.disable_respawn));

        // Play thunder sound
        fileCommands.add(playSound(Sound.THUNDER, SoundSource.master, "@a", "~", "~50", "~", "100", "1", "0"));

        // Set all dead players to spectator mode
        fileCommands.add(setGameMode(GameMode.spectator, "@a[scores={Deaths=1},gamemode=!spectator]"));

        // Reset scores
        for (int i = 0; i < 2; i++) {
            fileCommands.add(scoreboard.Set("@a[scores={Deaths=1}]", getObjectiveByName(Objective.ControlPoint.extendName(i + 1)), 0));
            fileCommands.add(scoreboard.Set(admin, getObjectiveByName(Objective.Highscore.extendName(i + 1)), 1));
        }

        // Reset player with lowest health
        fileCommands.add(scoreboard.Set(admin, getObjectiveByName(Objective.MinHealth), 20));

        // Announce traitor deaths
        ArrayList<TextItem> texts = new ArrayList<>();
        texts.add(bannerText);
        texts.add(new Text(Color.red, true, false, "A TRAITOR HAS BEEN ELIMINATED"));
        texts.add(bannerText);
        texts.add(new Text(Color.gold, true, false, "WELL DONE"));
        texts.add(bannerText);
        fileCommands.add(execute.If(new Entity("@p[scores={Deaths=1},tag=" + Tag.Traitor + "]")) +
                new TellRaw("@a", texts).sendRaw());
        texts.clear();

        // Add respawn tag to players who die in the first 20 minutes
        fileCommands.add(execute.Unless("@e[tag=" + Tag.RespawnDisabled + "]") +
                addTag("@p[scores={Deaths=1}]", Tag.Respawn));

        // Drop player head
        fileCommands.add(callFunction(FileName.drop_player_heads));

        // Do not allow killers to form a team
        if (teamMode == 2) {
            String killer = "@p[team=,scores={TempKills=1}]";
            String dead = "@p[team=,scores={Deaths=1}]";

            if (debug) {
                texts.add(new Select(false, false, killer));
                texts.add(new Text(Color.white, false, false, " has killed and is not in a team."));
                fileCommands.add(new TellRaw("@a[tag=Debug]", texts).sendRaw());
                texts.clear();

                texts.add(new Select(false, false, dead));
                texts.add(new Text(Color.white, false, false, " has died and is not in a team."));
                fileCommands.add(new TellRaw("@a[tag=Debug]", texts).sendRaw());
                texts.clear();

                texts.add(new Select(false, false, "@p[team=,scores={TempKills=1,IsKiller=1}]"));
                texts.add(new Text(Color.white, false, false, " already has been assigned as a killer."));
                fileCommands.add(new TellRaw("@a[tag=Debug]", texts).sendRaw());
                texts.clear();
            }

            texts.add(new Text(Color.red, true, false, "Looks like you do not want a teammate."));
            fileCommands.add(execute.If(killer, false) +
                    execute.IfNext(dead) +
                    execute.UnlessNext(killer, Objective.IsKiller, 1, true) +
                    new TellRaw(killer, texts).sendRaw());
            texts.clear();

            fileCommands.add(execute.If(killer, false) +
                    execute.IfNext(dead, true) +
                    scoreboard.Set(killer, Objective.IsKiller, 1));

            if (debug) {
                texts.add(new Select(false, false, "@p[scores={IsKiller=1}]"));
                texts.add(new Text(Color.white, false, false, " has been assigned as a killer."));
                fileCommands.add(new TellRaw("@a[tag=Debug]", texts).sendRaw());
                texts.clear();
            }
        }

        // Reset death count
        fileCommands.add(scoreboard.Reset("@p[scores={Deaths=1}]", Objective.Deaths));

        // Reset temporary kill count
        fileCommands.add(scoreboard.Reset("@p[scores={TempKills=1}]", Objective.TempKills));

        // Do automatic respawn in the first 20 minutes
        fileCommands.add(execute.Unless("@e[tag=" + Tag.RespawnDisabled + "]") +
                callFunction(FileName.respawn_player, 1));

        return new FileData(FileName.handle_player_death, fileCommands);
    }

    private FileData DropPlayerHeads() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Summon a player head upon dying
        for (Player p : players) {
            fileCommands.add(execute.At(new Entity("@p[name=" + p.getPlayerName() + ",scores={Deaths=1}]")) +
                    summonEntity(EntityType.item, "{Item:{id:\"minecraft:player_head\",count:1,components:{\"minecraft:profile\":{name:" + p.getPlayerName() + "}}}}"));
        }

        return new FileData(FileName.drop_player_heads, fileCommands);
    }

    private FileData BossBarValue() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Players in a team
        for (Team t : teams) {
            fileCommands.add(execute.If(adminSingle, getObjectiveByName(Objective.CP.toString() + 1 + t.getName()), ComparatorType.greater,adminSingle, getObjectiveByName(Objective.Highscore.extendName(1))) +
                    getBossbarByName("cp1").setColor(t.getBossbarColor()));
            fileCommands.add(execute.If(adminSingle, getObjectiveByName(Objective.CP.toString() + 2 + t.getName()), ComparatorType.greater, "@e[limit=1,scores={Highscore1=14400..}]", getObjectiveByName(Objective.Highscore.extendName(2))) +
                    getBossbarByName("cp2").setColor(t.getBossbarColor()));
            for (int i = 0; i < controlPoints.size(); i++) {
                fileCommands.add(scoreboard.Operation(admin, getObjectiveByName(Objective.Highscore.extendName(i + 1)), ComparatorType.greater, admin, getObjectiveByName("" + Objective.CP + (i + 1) + t.getName())));
            }
        }

        // Individual players
        fileCommands.add(execute.If("@r[limit=1,team=]", getObjectiveByName(Objective.ControlPoint.extendName(1)), ComparatorType.greater, "@e[type=marker,limit=1]", getObjectiveByName(Objective.Highscore.extendName(1))) +
                getBossbarByName("cp1").setColor(BossBarColor.white));
        fileCommands.add(execute.If("@r[limit=1,team=]", getObjectiveByName(Objective.ControlPoint.extendName(2)), ComparatorType.greater, "@e[scores={Highscore1=14400..},limit=1]", getObjectiveByName(Objective.Highscore.extendName(2))) +
                getBossbarByName("cp2").setColor(BossBarColor.white));
        for (int i = 0; i < controlPoints.size(); i++) {
            fileCommands.add(scoreboard.Operation(admin, getObjectiveByName(Objective.Highscore.extendName(i + 1)), ComparatorType.greater, "@r[limit=1,team=]", getObjectiveByName(Objective.ControlPoint.extendName(i + 1))));
        }

        // Update value of bossbars
        fileCommands.add(execute.Store(ExecuteStore.result, getBossbarByName("cp1"), BossBarStore.value) +
                scoreboard.Get(adminSingle, getObjectiveByName(Objective.Highscore.extendName(1))));
        fileCommands.add(execute.Store(ExecuteStore.result, getBossbarByName("cp2"), BossBarStore.value) +
                scoreboard.Get("@e[limit=1,scores={Highscore1=14400..}]", getObjectiveByName(Objective.Highscore.extendName(2))));

        return new FileData(FileName.bbvalue, fileCommands);
    }

    private FileData ClearEnderChest() {
        ArrayList<String> fileCommands = new ArrayList<>();
        for (int i = 0; i < chestSize; i++) {
            fileCommands.add(replaceItem("@a", InventorySlot.enderchest.setSlotNumber(i), BlockType.air, 1));
            }

        return new FileData(FileName.clear_enderchest, fileCommands);
    }

    private FileData EquipGear() {
        ArrayList<String> fileCommands = new ArrayList<>();
        fileCommands.add(replaceItem("@a", InventorySlot.chest, BlockType.iron_chestplate));
        fileCommands.add(replaceItem("@a", InventorySlot.feet, BlockType.iron_boots));
        fileCommands.add(replaceItem("@a", InventorySlot.head, BlockType.iron_helmet));
        fileCommands.add(replaceItem("@a", InventorySlot.legs, BlockType.iron_leggings));
        fileCommands.add(replaceItem("@a", InventorySlot.offhand, BlockType.shield));
        fileCommands.add(replaceItem("@a", InventorySlot.mainhand, BlockType.iron_axe));
        fileCommands.add(replaceItem("@a", InventorySlot.inventory.setSlotNumber(0), BlockType.iron_sword));
        fileCommands.add(giveEffect("@a", Effect.regeneration, 1, 255, true));

        return new FileData(FileName.equip_gear, fileCommands);
    }

    private FileData GodMode() {
        ArrayList<String> fileCommands = new ArrayList<>();
        fileCommands.add(giveEffect("@s", Effect.resistance, 99999, 4, true));
        fileCommands.add(replaceItem("@s", InventorySlot.mainhand, BlockType.trident + "[custom_name='[{\"bold\":false,\"color\":\"white\",\"italic\":false,\"obfuscated\":true,\"text\":\"aA\"},{\"bold\":true,\"color\":\"#8C3CC1\",\"obfuscated\":false,\"text\":\" The\"},{\"bold\":true,\"color\":\"#E280FF\",\"obfuscated\":false,\"text\":\" Impaler \"},{\"color\":\"white\",\"obfuscated\":true,\"text\":\"Aa\"}]',lore=['{\"text\":\"This holy weapon impales anything it touches\"}'],unbreakable={show_in_tooltip:false},damage=0,enchantments={levels:{\"minecraft:fire_aspect\":255,\"minecraft:sharpness\":255,\"minecraft:efficiency\":255,'impaling':255},show_in_tooltip:false},attribute_modifiers={modifiers:[{id:\"" + AttributeType.armor + "\",type:\"" + AttributeType.attack_damage + "\",amount:1000,operation:\"add_value\",slot:\"mainhand\"}],show_in_tooltip:false}]"));

        return new FileData(FileName.god_mode, fileCommands);
    }

    private FileData GetStartPotions() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Clear inventory
        fileCommands.add(clearInventory("@s"));

        // Give potions
        if (teamMode == 1) {
            fileCommands.add(giveSplashPotion("@s", 0, Effect.speed, "808080", "Developer Mode", "Set operational mode to Developer Mode."));
            fileCommands.add(giveSplashPotion("@s", 1, Effect.weakness, "FF9933", "Assign Teams", "Assign players to teams."));
            fileCommands.add(giveSplashPotion("@s", 2, Effect.slow_falling, "6633CC", "Predictions", "Who will win this season?."));
            fileCommands.add(giveSplashPotion("@s", 3, Effect.invisibility, "3399FF", "Into Calls", "Allow players to gather in their Discord channel."));
            fileCommands.add(giveSplashPotion("@s", 4, Effect.poison, "00CC66", "Spread players", "Spread players across the map."));
            fileCommands.add(giveSplashPotion("@s", 5, Effect.strength, "CC3333", "Survival Mode", "Set operational mode to Ready to Play."));
            fileCommands.add(giveSplashPotion("@s", 6, Effect.slowness, "00FF7F", "Start Game", "Start the game. Good luck!"));
        }
        else if (teamMode == 2) {
            fileCommands.add(giveSplashPotion("@s", 0, Effect.speed, "808080", "Developer Mode", "Set operational mode to Developer Mode."));
            fileCommands.add(giveSplashPotion("@s", 1, Effect.slow_falling, "6633CC", "Predictions", "Who will win this season?."));
            fileCommands.add(giveSplashPotion("@s", 2, Effect.invisibility, "3399FF", "Into Calls", "Allow players to gather in their Discord channel."));
            fileCommands.add(giveSplashPotion("@s", 3, Effect.poison, "00CC66", "Spread players", "Spread players across the map."));
            fileCommands.add(giveSplashPotion("@s", 4, Effect.strength, "CC3333", "Survival Mode", "Set operational mode to Ready to Play."));
            fileCommands.add(giveSplashPotion("@s", 5, Effect.slowness, "00FF7F", "Start Game", "Start the game. Good luck!"));
        }

        return new FileData(FileName.start_potions, fileCommands);
    }

    private FileData DeveloperMode() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Create marker entity
        fileCommands.add(killEntity("@e[type=marker]"));
        fileCommands.add(summonEntity(EntityType.marker, new Coordinate(0, worldBottom, 0), "{CustomName:\"\\\"Admin\\\"\"}"));

        // Set time
        fileCommands.add(setTime(0));

        // Set gamerules
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
        fileCommands.add(setGameRule(GameRule.disableRaids, true));
        fileCommands.add(setGameRule(GameRule.doInsomnia, false));

        // Reset scores of all entities
        fileCommands.add(scoreboard.Reset("@e"));
        for (int i = 1; i < controlPoints.size() + 1; i++) {
            fileCommands.add(scoreboard.Set(admin, getObjectiveByName(Objective.Highscore.extendName(i)), 1));
            for (int ii = 1; ii < 3; ii++) {
                fileCommands.add(scoreboard.Set("@a", getObjectiveByName(Objective.MSGDum.extendName(ii + "CP" + i)), 1));
            }
        }
        fileCommands.add(scoreboard.Set(admin, Objective.MinHealth, 20));
        fileCommands.add(scoreboard.Set(admin, Objective.Victory, 1));
        fileCommands.add(scoreboard.Set("@a", Objective.IsKiller, 0));

        // Deactivate game-critical command blocks
        fileCommands.add(execute.In(Dimension.overworld) +
                fill(0, worldBottom + 2, 15, 0, worldBottom + 2, 2, BlockType.bedrock, SetBlockType.replace));
        fileCommands.add(execute.In(Dimension.overworld) +
                fill(2, worldBottom + 2, 0, 8, worldBottom + 2, 0, BlockType.bedrock, SetBlockType.replace));
        fileCommands.add(execute.In(Dimension.overworld) +
                fill(15, worldBottom + 2, 3, 15, worldBottom + 2, 11, BlockType.bedrock, SetBlockType.replace));
        fileCommands.add(execute.In(Dimension.overworld) +
                setBlock(11, worldBottom + 2, 0, BlockType.bedrock, SetBlockType.destroy));
        fileCommands.add(execute.In(Dimension.overworld) +
                setBlock(10, worldBottom + 2, 0, BlockType.bedrock, SetBlockType.destroy));

        // Activate potion command blocks
        fileCommands.add(execute.In(Dimension.overworld) +
                fill(15, worldBottom + 2, 15, 9, worldBottom + 2, 15, BlockType.redstone_block, SetBlockType.replace));

        // Spawn new Control Points
        fileCommands.add(execute.In(controlPoints.get(0).getCoordinate().getDimension()) +
                addForceLoad(controlPoints.get(0).getCoordinate().getX(), controlPoints.get(0).getCoordinate().getZ(), controlPoints.get(0).getCoordinate().getX(), controlPoints.get(0).getCoordinate().getZ()));
        fileCommands.add(execute.In(controlPoints.get(1).getCoordinate().getDimension()) +
                addForceLoad(controlPoints.get(1).getCoordinate().getX(), controlPoints.get(1).getCoordinate().getZ(), controlPoints.get(1).getCoordinate().getX(), controlPoints.get(1).getCoordinate().getZ()));
        fileCommands.add(callFunction(FileName.spawn_controlpoints));
        fileCommands.add(execute.In(controlPoints.get(0).getCoordinate().getDimension()) +
                removeForceLoad(controlPoints.get(0).getCoordinate().getX(), controlPoints.get(0).getCoordinate().getZ(), controlPoints.get(0).getCoordinate().getX(), controlPoints.get(0).getCoordinate().getZ()));
        fileCommands.add(execute.In(controlPoints.get(1).getCoordinate().getDimension()) +
                removeForceLoad(controlPoints.get(1).getCoordinate().getX(), controlPoints.get(1).getCoordinate().getZ(), controlPoints.get(1).getCoordinate().getX(), controlPoints.get(1).getCoordinate().getZ()));

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

        // Create jukebox at 0,0
        fileCommands.add(execute.In(Dimension.overworld) +
                setBlock(startCoordinate,  BlockType.jukebox + "[has_record=true]{RecordItem:{Count:1b,id:\"minecraft:music_disc_stal\"}}", SetBlockType.replace));

        // Remove tags
        fileCommands.add(removeTag("@a", Tag.Traitor));
        fileCommands.add(removeTag("@a", Tag.DontMakeTraitor));
        fileCommands.add(removeTag("@a", Tag.RespawnDisabled));
        fileCommands.add(removeTag("@a", Tag.IronManCandidate));
        fileCommands.add(removeTag("@a", Tag.IronMan));
        fileCommands.add(removeTag(admin, Tag.CarePackagesSpread));
        for (int i = 0; i < 4; i++) {
            fileCommands.add(removeTag("@a", Tag.ReceivedPerk.extendName(i + 1)));
        }

        // Set world border
        fileCommands.add(setWorldBorder(2 * worldSize));

        // Display ranks
        fileCommands.add(callFunction(FileName.display_rank));

        // Set time dummy scoreboard entries
        fileCommands.add(scoreboard.Set("NightTime", getObjectiveByName(Objective.Time), 600));
        fileCommands.add(scoreboard.Set("CarePackages", getObjectiveByName(Objective.Time), 1200));
        fileCommands.add(scoreboard.Set("ControlPoints", getObjectiveByName(Objective.Time), 1800));
        fileCommands.add(scoreboard.Set("TraitorFaction", getObjectiveByName(Objective.Time), 2400));

        // Reset teams & solos
        for (Team t : teams) {
            fileCommands.add(t.emptyTeam());
            fileCommands.add(scoreboard.Reset(t.getPlayerColor(), getObjectiveByName(Objective.CPScore)));
            fileCommands.add(t.joinTeam(t.getPlayerColor()));
        }
        fileCommands.add(scoreboard.Reset("Solo", getObjectiveByName(Objective.CPScore)));

        // Set CP score dummy scoreboard entries
        int minToCPScore = secPerMinute * tickPerSecond * controlPoints.get(0).getAddRate();
        fileCommands.add(scoreboard.Set("Perk1", getObjectiveByName(Objective.CPScore), 3 * minToCPScore));
        fileCommands.add(scoreboard.Set("Perk2", getObjectiveByName(Objective.CPScore), 6 * minToCPScore));
        fileCommands.add(scoreboard.Set("Perk3", getObjectiveByName(Objective.CPScore), 12 * minToCPScore));
        fileCommands.add(scoreboard.Set("Perk4", getObjectiveByName(Objective.CPScore), 15 * minToCPScore));
        fileCommands.add(scoreboard.Set("TimeVictory", getObjectiveByName(Objective.CPScore), 20 * minToCPScore));

        // Reset player scales
        fileCommands.add(execute.As(new Entity("@a")) +
                        setAttributeBase("@s", AttributeType.scale, 1));

        // Set gamemode of player executing the command to creative
        fileCommands.add(setGameMode(GameMode.creative, "@s"));

        // Clear scheduled commands
        fileCommands.add(callFunction(FileName.clear_schedule));

        // Clear all player effects
        fileCommands.add(clearEffect("@a"));

        // Give admin start potions
        fileCommands.add(callFunction(FileName.start_potions));

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
        fileCommands.add(clearEffect("@a"));
        fileCommands.add(execute.In(Dimension.overworld) +
                teleportEntity("@a", new Coordinate(0, -100, 0)));

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
                teleportEntity("@a", startCoordinate));

        // Reset scores
        fileCommands.add(scoreboard.Reset("@a", getObjectiveByName(Objective.Deaths)));
        fileCommands.add(scoreboard.Reset("@a", getObjectiveByName(Objective.Kills)));

        // Make players invulnerable
        fileCommands.add(giveEffect("@a", Effect.resistance, 99999, 4, true));

        return new FileData(FileName.into_calls, fileCommands);
    }

    private FileData SpreadPlayers() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Spread teams together in assigned mode, separate in unassigned mode
        Boolean respectTeams = true;
        if (teamMode == 1) {
            respectTeams = true;
        }
        else if (teamMode == 2) {
            respectTeams = false;
        }

        fileCommands.add(execute.In(Dimension.overworld) +
                spreadPlayers(0, 0, (int) (0.3 * worldSize), (int) (0.9 * worldSize), respectTeams, "@a"));

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

        // Recipes
        // fileCommands.add(giveRecipe("@a", BlockType.golden_apple.setNamespace(Namespace.uhc)));
        fileCommands.add(takeRecipe("@a", BlockType.dragon_head.setNamespace(Namespace.uhc)));

        // Remove resistance
        fileCommands.add(clearEffect("@a", Effect.resistance));

        // Set scoreboard values
        fileCommands.add(scoreboard.Set("@a", getObjectiveByName(Objective.Hearts), 20));
        fileCommands.add(scoreboard.Set("@a", getObjectiveByName(Objective.DamageTaken), 0));

        return new FileData(FileName.survival_mode, fileCommands);
    }

    private FileData StartGame() {
        ArrayList<String> fileCommands = new ArrayList<>();
        ArrayList<TextItem> texts = new ArrayList<>();

        // Set world time
        fileCommands.add(setTime(0));

        // Give potion effect
        fileCommands.add(giveEffect("@a", Effect.regeneration, 1, 255));
        fileCommands.add(giveEffect("@a", Effect.saturation, 1, 255));
        fileCommands.add(giveEffect("@a", Effect.resistance, 20*60, 2, true));

        // Clear player inventories
        fileCommands.add(clearInventory("@a"));

        // Set all players to survival mode
        fileCommands.add(setGameMode(GameMode.survival, "@a"));

        // Activate command blocks
        fileCommands.add(execute.In(Dimension.overworld) +
                fill(0, worldBottom + 2, 15, 0, worldBottom + 2, 2, BlockType.redstone_block, SetBlockType.replace));
        fileCommands.add(execute.In(Dimension.overworld) +
                fill(2, worldBottom + 2, 0, 6, worldBottom + 2, 0, BlockType.redstone_block, SetBlockType.replace));
        fileCommands.add(execute.In(Dimension.overworld) +
                setBlock(10, worldBottom + 2, 0, BlockType.redstone_block, SetBlockType.destroy));

        // Deactivate startup command blocks
        fileCommands.add(execute.In(Dimension.overworld) +
                fill(15, worldBottom + 2, 15, 9, worldBottom + 2, 15, BlockType.bedrock));

        // Revoke all advancements
        fileCommands.add(revokeAdvancement("@a"));

        // Experience
        fileCommands.add(setExperience("@a", 0, ExperienceType.levels));
        fileCommands.add(setExperience("@a", 0, ExperienceType.points));

        // Give players teammate tools
        if (teamMode == 1) {
            // Teammate tracker
            for (int i = 0; i < teams.size(); i++) {
                fileCommands.add(giveItem("@a[team=" + teams.get(i).getName() + "]", BlockType.bundle.extendColor(teams.get(i).getGlassColor()), "[enchantments={levels:{\"minecraft:vanishing_curse\":1}},custom_data={locateTeammate:1b}]"));
            }
        }
        else if (teamMode == 2) {
            // Team caller
            fileCommands.add(giveItem("@a", BlockType.goat_horn, "[instrument=\"minecraft:ponder_goat_horn\",use_cooldown={seconds:30},enchantments={\"minecraft:vanishing_curse\":1}]"));
        }

        // Show world border size in actionbar
        texts.add(new Text(Color.light_purple, false, false, "World size: ±" + worldSize + " blocks"));
        Title showWorldSize = new Title("@a", TitleType.subtitle, texts);

        // Change title display time
        fileCommands.add(changeTitleDisplayTime("@a", 1, 5, 2));

        // Display world size
        fileCommands.add(showWorldSize.displayTitle());

        // Display game start
        fileCommands.add(new Title("@a", TitleType.title, new Text(Color.gold, true, true, "Game Starting Now!")).displayTitle());

        // Change title display time
        fileCommands.add(callFunction(FileName.title_default_timing, 5));

        // Destroy all ground items
        fileCommands.add(killEntity("@e[type=item]"));

        return new FileData(FileName.start_game, fileCommands);
    }

    private FileData BattleRoyale() {
        ArrayList<String> fileCommands = new ArrayList<>();
        fileCommands.add(execute.In(Dimension.overworld, false) +
                execute.PositionedNext(new Coordinate(0, 151, 0), true) +
                setGameMode(GameMode.survival, "@a[distance=..20,gamemode=!creative]"));
        fileCommands.add(execute.In(Dimension.overworld, false) +
                execute.PositionedNext(new Coordinate(0, 151, 0), true) +
                spreadPlayers(0, 0, (int) (0.3 * worldSize), (int) (0.9 * worldSize), true, "@a[distance=..20,gamemode=survival]"));

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

        // Set objective to victory achieved
        fileCommands.add(scoreboard.Set(admin, getObjectiveByName(Objective.Victory), 2));

        // Call deathmatch functions
        fileCommands.add(execute.If("@a[limit=2,gamemode=!spectator]") +
                callFunction(FileName.initiate_deathmatch));

        // Announce iron man
        fileCommands.add(execute.As("@a[scores={DamageTaken=.." + minDamage + "}]") +
                callFunction(FileName.announce_iron_man));

        return new FileData(FileName.victory, fileCommands);
    }

    private FileData InitiateDeathMatch() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Call deathmatch functions
        fileCommands.add(callFunction(FileName.minute_ + "2", 60));
        fileCommands.add(callFunction(FileName.minute_ + "1", 60 * 2));
        fileCommands.add(callFunction(FileName.death_match, 60 * 3));

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
        fileCommands.add(callFunction(FileName.victory));

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
        fileCommands.add(callFunction(FileName.victory));

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
        fileCommands.add(callFunction(FileName.victory));

        return new FileData(FileName.victory_message_traitor, fileCommands);
    }

    private FileData DeathMatch() {
        ArrayList<String> fileCommands = new ArrayList<>();

        fileCommands.add(setWorldBorder(400));
        fileCommands.add(setWorldBorder(20, 180));
        fileCommands.add(execute.In(Dimension.overworld) +
                teleportEntity("@a[gamemode=!spectator]", new Coordinate(3, 153, 3)));
        fileCommands.add(execute.In(Dimension.overworld) +
                spreadPlayers(0, 0, 75, 150, true, "@a[gamemode=!spectator,team=!]"));
        fileCommands.add(execute.In(Dimension.overworld) +
                spreadPlayers(0, 0, 75, 150, false, "@a[gamemode=!spectator,team=]"));

        return new FileData(FileName.death_match, fileCommands);
    }

    private FileData Controlpoint(int i) {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Players in teams
        for (Team team : teams) {
            // Give players on the Control Point score
            fileCommands.add(execute.As("@a[gamemode=!spectator,team=" + team.getName() + "]", false) +
                    execute.IfNext("@s[gamemode=!spectator,x=" + (controlPoints.get(i - 1).getCoordinate().getX() - 6) + ",y=" + (controlPoints.get(i - 1).getCoordinate().getY() - 1) + ",z=" + (controlPoints.get(i - 1).getCoordinate().getZ() - 6) + ",dx=12,dy=12,dz=12]") +
                    execute.UnlessNext("@a[gamemode=!spectator,x=" + (controlPoints.get(i - 1).getCoordinate().getX() - 6) + ",y=" + (controlPoints.get(i - 1).getCoordinate().getY() - 1) + ",z=" + (controlPoints.get(i - 1).getCoordinate().getZ() - 6) + ",dx=12,dy=12,dz=12,team=!" + team.getName() + "]", true) +
                    scoreboard.Add("@s", getObjectiveByName(Objective.ControlPoint.extendName(i)), controlPoints.get(i - 1).getAddRate()));

            // Update CP glass color
            fileCommands.add(execute.In(controlPoints.get(i - 1).getCoordinate().getDimension(), false) +
                    execute.IfNext("@r[limit=1,gamemode=!spectator,team=" + team.getName() + "]", getObjectiveByName(Objective.ControlPoint.extendName(i)), ComparatorType.greater, adminSingle, getObjectiveByName(Objective.Highscore.extendName(i)), true) +
                    setBlock(controlPoints.get(i - 1).getCoordinate().getX(), controlPoints.get(i - 1).getCoordinate().getY() + 1, controlPoints.get(i - 1).getCoordinate().getZ(), "minecraft:" + team.getGlassColor() + "_stained_glass", SetBlockType.replace));
        }

        // Players without a team
        // Give players on the Control Point score
        fileCommands.add(execute.As("@a[gamemode=!spectator,team=]", false) +
                execute.IfNext("@s[gamemode=!spectator,x=" + (controlPoints.get(i - 1).getCoordinate().getX() - 6) + ",y=" + (controlPoints.get(i - 1).getCoordinate().getY() - 1) + ",z=" + (controlPoints.get(i - 1).getCoordinate().getZ() - 6) + ",dx=12,dy=12,dz=12]") +
                execute.UnlessNext("@a[gamemode=!spectator,x=" + (controlPoints.get(i - 1).getCoordinate().getX() - 6) + ",y=" + (controlPoints.get(i - 1).getCoordinate().getY() - 1) + ",z=" + (controlPoints.get(i - 1).getCoordinate().getZ() - 6) + ",dx=12,dy=12,dz=12,team=!]", true) +
                scoreboard.Add("@s", getObjectiveByName(Objective.ControlPoint.extendName(i)), controlPoints.get(i - 1).getAddRate()));

        // Update CP glass color
        fileCommands.add(execute.In(controlPoints.get(i - 1).getCoordinate().getDimension(), false) +
                execute.IfNext("@r[limit=1,gamemode=!spectator,team=]", getObjectiveByName(Objective.ControlPoint.extendName(i)), ComparatorType.greater, adminSingle, getObjectiveByName(Objective.Highscore.extendName(i)), true) +
                setBlock(controlPoints.get(i - 1).getCoordinate().getX(), controlPoints.get(i - 1).getCoordinate().getY() + 1, controlPoints.get(i - 1).getCoordinate().getZ(), "minecraft:white_stained_glass", SetBlockType.replace));

        // Keep beacon active
        fileCommands.add(fill(controlPoints.get(i - 1).getCoordinate().getX() - 1, controlPoints.get(i - 1).getCoordinate().getY() - 1, controlPoints.get(i - 1).getCoordinate().getZ() - 1, controlPoints.get(i - 1).getCoordinate().getX() + 1, controlPoints.get(i - 1).getCoordinate().getY() - 1, controlPoints.get(i - 1).getCoordinate().getZ() + 1, BlockType.emerald_block));
        fileCommands.add(fill(controlPoints.get(i - 1).getCoordinate().getX(), controlPoints.get(i - 1).getCoordinate().getY(), controlPoints.get(i - 1).getCoordinate().getZ(), controlPoints.get(i - 1).getCoordinate().getX(), controlPoints.get(i - 1).getCoordinate().getY(), controlPoints.get(i - 1).getCoordinate().getZ(), BlockType.beacon));

        // Update CP messaging
        fileCommands.add(callFunction("" + FileName.controlpoint_messages_ + i));

        return new FileData("" + FileName.controlpoint_ + i, fileCommands);
    }

    private FileData ControlPointMessages(int i) {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Players in a team
        for (Team team : teams) {
            /* Under attack message */
            // Increment attacking counter if team is on CP and message is not sent
            fileCommands.add(execute.As(new Entity("@a[gamemode=!spectator,team=" + team.getName() + "]"), false) +
                    execute.IfNext(new Entity("@s[x=" + (controlPoints.get(i - 1).getCoordinate().getX() - 6) + ",y=" + (controlPoints.get(i - 1).getCoordinate().getY() - 1) + ",z=" + (controlPoints.get(i - 1).getCoordinate().getZ() - 6) + ",dx=12,dy=12,dz=12,scores={MSGDum1CP" + i + "=.." + cpMessageThreshold + "}]"), true) +
                    scoreboard.Add("@a[team=" + team.getName() + "]", getObjectiveByName(Objective.MSGDum.extendName("1CP" + i)), 1));

            // Reset abandonment counter
            fileCommands.add(execute.As(new Entity("@a[gamemode=!spectator,team=" + team.getName() + "]"), false) +
                    execute.IfNext(new Entity("@s[x=" + (controlPoints.get(i - 1).getCoordinate().getX() - 6) + ",y=" + (controlPoints.get(i - 1).getCoordinate().getY() - 1) + ",z=" + (controlPoints.get(i - 1).getCoordinate().getZ() - 6) + ",dx=12,dy=12,dz=12]"), true) +
                    scoreboard.Set("@a[gamemode=!spectator,team=" + team.getName() + "]", getObjectiveByName(Objective.MSGDum.extendName("2CP" + i)), 1));

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
                    addTag("@a[team=" + team.getName() + "]", Tag.AttackingCP.extendName(i)));

            /* Abandoned message */
            // Increment abandonment counter unless team is on the CP
            fileCommands.add(execute.If("@p[gamemode=!spectator,team=" + team.getName() + ",tag=" + Tag.AttackingCP.extendName(i) + "]", false) +
                    execute.UnlessNext("@p[gamemode=!spectator,team=" + team.getName() + ",x=" + (controlPoints.get(i - 1).getCoordinate().getX() - 6) + ",y=" + (controlPoints.get(i - 1).getCoordinate().getY() - 1) + ",z=" + (controlPoints.get(i - 1).getCoordinate().getZ() - 6) + ",dx=12,dy=12,dz=12]", true) +
                    scoreboard.Add("@a[team=" + team.getName() + "]", getObjectiveByName(Objective.MSGDum.extendName("2CP" + i)), 1));

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
                    scoreboard.Set("@a[gamemode=!spectator,team=" + team.getName() + "]", getObjectiveByName(Objective.MSGDum.extendName("1CP" + i)), 1));

            // Remove Attacking tag
            fileCommands.add(execute.As(new Entity("@a[team=" + team.getName() + ",limit=1]"), false) +
                    execute.IfNext(new Entity("@s[scores={MSGDum2CP" + i + "=" + cpMessageThreshold + "}]"), true) +
                    removeTag("@a[team=" + team.getName() + "]", Tag.AttackingCP.extendName(i)));
        }

        // Players without a team
        /* Under attack message */
        // Increment attacking counter if team is on CP and message is not sent
        fileCommands.add(execute.As(new Entity("@a[gamemode=!spectator,team=]"), false) +
                execute.IfNext(new Entity("@s[x=" + (controlPoints.get(i - 1).getCoordinate().getX() - 6) + ",y=" + (controlPoints.get(i - 1).getCoordinate().getY() - 1) + ",z=" + (controlPoints.get(i - 1).getCoordinate().getZ() - 6) + ",dx=12,dy=12,dz=12,scores={MSGDum1CP" + i + "=.." + cpMessageThreshold + "}]"), true) +
                scoreboard.Add("@a[team=]", getObjectiveByName(Objective.MSGDum.extendName("1CP" + i)), 1));

        // Reset abandonment counter
        fileCommands.add(execute.As(new Entity("@a[gamemode=!spectator,team=]"), false) +
                execute.IfNext(new Entity("@s[x=" + (controlPoints.get(i - 1).getCoordinate().getX() - 6) + ",y=" + (controlPoints.get(i - 1).getCoordinate().getY() - 1) + ",z=" + (controlPoints.get(i - 1).getCoordinate().getZ() - 6) + ",dx=12,dy=12,dz=12]"), true) +
                scoreboard.Set("@a[gamemode=!spectator,team=]", getObjectiveByName(Objective.MSGDum.extendName("2CP" + i)), 1));

        // Define CP attacking message
        ArrayList<TextItem> texts = new ArrayList<>();
        texts.add(new Text(Color.light_purple, false, false, "A "));
        texts.add(new Text(Color.white, false, false, "SOLO"));
        texts.add(new Text(Color.light_purple, false, false, " IS ATTACKING CONTROL POINT " + i + "!"));

        // Display message in chat if time threshold has been exceeded
        fileCommands.add(execute.As(new Entity("@a[team=,limit=1]"), false) +
                execute.IfNext(new Entity("@s[scores={MSGDum1CP" + i + "=" + cpMessageThreshold + "}]"), true) +
                new TellRaw("@a", texts).sendRaw());

        // Grant attacking players Attacking tag
        fileCommands.add(execute.As(new Entity("@a[team=,limit=1]"), false) +
                execute.IfNext(new Entity("@s[scores={MSGDum1CP" + i + "=" + cpMessageThreshold + "}]"), true) +
                addTag("@a[team=]", Tag.AttackingCP.extendName(i)));

        /* Abandoned message */
        // Increment abandonment counter unless team is on the CP
        fileCommands.add(execute.If("@p[gamemode=!spectator,team=,tag=" + Tag.AttackingCP.extendName(i) + "]", false) +
                execute.UnlessNext("@p[gamemode=!spectator,team=,x=" + (controlPoints.get(i - 1).getCoordinate().getX() - 6) + ",y=" + (controlPoints.get(i - 1).getCoordinate().getY() - 1) + ",z=" + (controlPoints.get(i - 1).getCoordinate().getZ() - 6) + ",dx=12,dy=12,dz=12]", true) +
                scoreboard.Add("@a[team=]", getObjectiveByName(Objective.MSGDum.extendName("2CP" + i)), 1));

        // Define CP abandonment message
        texts.clear();
        texts.add(new Text(Color.light_purple, false, false, "A "));
        texts.add(new Text(Color.white, false, false, "SOLO"));
        texts.add(new Text(Color.light_purple, false, false, " HAS ABANDONED CONTROL POINT " + i + "!"));

        // Display message in chat if time threshold has been exceeded
        fileCommands.add(execute.As(new Entity("@a[team=,limit=1]"), false) +
                execute.IfNext(new Entity("@s[scores={MSGDum2CP" + i + "=" + (cpMessageThreshold - 1) + "}]"), true) +
                new TellRaw("@a", texts).sendRaw());

        // Reset attacking counter
        fileCommands.add(execute.As(new Entity("@a[team=,limit=1]"), false) +
                execute.IfNext(new Entity("@s[scores={MSGDum2CP" + i + "=" + cpMessageThreshold + "}]"), true) +
                scoreboard.Set("@a[gamemode=!spectator,team=]", getObjectiveByName(Objective.MSGDum.extendName("1CP" + i)), 1));

        // Remove Attacking tag
        fileCommands.add(execute.As(new Entity("@a[team=,limit=1]"), false) +
                execute.IfNext(new Entity("@s[scores={MSGDum2CP" + i + "=" + cpMessageThreshold + "}]"), true) +
                removeTag("@a[team=]", Tag.AttackingCP.extendName(i)));

        return new FileData("" + FileName.controlpoint_messages_ + i, fileCommands);
    }

    private FileData DropCarepackages() {
        ArrayList<String> fileCommands = new ArrayList<>();
        ArrayList<TextItem> texts = new ArrayList<>();

        // Show world border size in actionbar
        texts.add(new Text(Color.light_purple, false, false, "To be found at ±" + carePackageSpread + " blocks"));
        Title showWorldSize = new Title("@a", TitleType.subtitle, texts);

        // Change title display time
        fileCommands.add(changeTitleDisplayTime("@a", 1, 5, 2));

        // Display world size
        fileCommands.add(showWorldSize.displayTitle());

        // Announce Care Packages
        fileCommands.add(new Title("@a", TitleType.title, new Text(Color.gold, true, true, carePackageAmount + " Care Packages!")).displayTitle());

        // Change title display time
        fileCommands.add(callFunction(FileName.title_default_timing, 5));

        // Summon Care Package entities
        for (int i = 0; i < carePackageAmount; i++) {
            fileCommands.add(execute.In(Dimension.overworld) +
                    summonEntity(EntityType.area_effect_cloud, new Coordinate(0, 5, 0, ReferenceFrame.relative), "{Passengers:[{id:falling_block,Time:1,DropItem:0b,BlockState:{Name:\"minecraft:chest\"},TileEntityData:{CustomName:\"\\\"Loot chest\\\"\",LootTable:\"uhc:supply_drop\"}}]}"));
        }

        return new FileData(FileName.drop_carepackages, fileCommands);
    }

    private FileData CarepackageDistributor() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Indicate that Care Packages are spread
        fileCommands.add(execute.If("@e[type=" + EntityType.falling_block + ",distance=..2]") +
                addTag(admin, Tag.CarePackagesSpread));

        // Spread Care Packages
        fileCommands.add(execute.In(Dimension.overworld, false) +
                execute.IfNext(new Entity("@e[type=" + EntityType.falling_block + ",distance=..2]"), true) +
                spreadPlayers(0, 0, 10, carePackageSpread, false, "@e[type=" + EntityType.falling_block + ",distance=..2]"));

        // Reset command blocks
        fileCommands.add(execute.In(Dimension.overworld, false) +
                execute.IfNext("@e[type=marker,limit=1,tag=" + Tag.CarePackagesSpread + "]", true) +
                fill(0, (worldBottom + 2), 10, 0, (worldBottom + 2), 9, BlockType.bedrock));

        return new FileData(FileName.carepackage_distributor, fileCommands);
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
                    fileCommands.add(addTag(p.getPlayerName(), Tag.DontMakeTraitor));
                }
            }
        }

        // Assign first traitor
        fileCommands.add(addTag("@r[limit=1,tag=!" + Tag.DontMakeTraitor + ",scores={Rank=" + minTraitorRank + "..},gamemode=!spectator]", Tag.Traitor));

        // Make traitor teammates ineligible for becoming traitor
        for (Team t : teams) {
            fileCommands.add(execute.If(new Entity("@p[tag=" + Tag.Traitor + ",team=" + t.getName() + "]")) +
                    addTag("@a[team=" + t.getName() + "]", Tag.DontMakeTraitor));
        }

        // Make solo traitors ineligible to become traitor again
        fileCommands.add(addTag("@a[tag=" + Tag.Traitor + ",team=]", Tag.DontMakeTraitor));

        // Assign second traitor
        fileCommands.add(addTag("@r[limit=1,tag=!" + Tag.DontMakeTraitor + ",scores={Rank=" + minTraitorRank + "..},gamemode=!spectator]", Tag.Traitor));

        // Add additional traitor
        if (traitorMode == 2) {
            fileCommands.add(removeTag("@a", Tag.DontMakeTraitor));
            if (traitorWaitTime > 0) {
                for (Player p : players) {
                    if (p.getLastTraitorSeason() >= seasons.get(seasons.size() - traitorWaitTime).getID()) {
                        fileCommands.add(addTag(p.getPlayerName(), Tag.DontMakeTraitor));
                    }
                }
            }
            fileCommands.add(addTag("@a[tag=" + Tag.Traitor + "]", Tag.DontMakeTraitor));
            fileCommands.add(addTag("@r[limit=1,tag=!" + Tag.DontMakeTraitor + ",gamemode=!spectator]", Tag.Traitor));
        }

        // Inform traitors
        ArrayList<TextItem> texts = new ArrayList<>();
        texts.add(new Text(Color.red, false, true, "You feel like betrayal today. You have become a Traitor. Your faction consists of: "));
        texts.add(new Select(false, true, "@a[tag=" + Tag.Traitor + "]"));
        texts.add(new Text(Color.red, false, true, "."));
        fileCommands.add(execute.As(new Entity("@a[tag=" + Tag.Traitor + "]")) +
                new TellRaw("@s", texts).sendRaw());

        // Announce Traitor Faction
        fileCommands.add(new Title("@a", TitleType.title, new Text(Color.red, true, false, "A Traitor Faction")).displayTitle());
        fileCommands.add(new Title("@a", TitleType.subtitle, new Text(Color.dark_red, true, false, "has been founded!")).displayTitle());

        // Disable traitor handout
        fileCommands.add(execute.In(Dimension.overworld) +
                setBlock(11, worldBottom + 2, 0, BlockType.redstone_block, SetBlockType.destroy));

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
        fileCommands.add(execute.As(new Entity("@a[tag=" + Tag.Traitor + "]")) +
                new Title("@s", TitleType.actionbar, texts).displayTitle());

        // Check if traitors have won
        fileCommands.add(execute.If(new Entity("@e[scores={Victory=1}]")) +
                callFunction(FileName.traitor_check));

        return new FileData(FileName.traitor_actionbar, fileCommands);
    }

    private FileData TeamScore() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // TODO This can definitely be improved

        for (int i = 1; i < controlPoints.size() + 1; i++) {
            for (Team t : teams) {
                fileCommands.add(execute.As(new Entity("@r[limit=1,gamemode=!spectator]")) +
                        scoreboard.Operation(admin, getObjectiveByName("" + Objective.CP + i + t.getName()), ComparatorType.greater, "@s[team=" + t.getName() + "]", getObjectiveByName(Objective.ControlPoint.extendName(i))));

                fileCommands.add(execute.As(new Entity("@r[limit=1,gamemode=!spectator]")) +
                        scoreboard.Operation("@s[team=" + t.getName() + "]", getObjectiveByName(Objective.ControlPoint.extendName(i)), ComparatorType.greater, admin, getObjectiveByName("" + Objective.CP + i + t.getName())));
            }
        }

        for (Team t : teams) {
            fileCommands.add(execute.In(controlPoints.get(0).getCoordinate().getDimension(), false) +
                    execute.AsNext(new Entity("@r[limit=1,gamemode=!spectator,x=" + (controlPoints.get(0).getCoordinate().getX() - 6) + ",y=" + (controlPoints.get(0).getCoordinate().getY() - 1) + ",z=" + (controlPoints.get(0).getCoordinate().getZ() - 6) + ",dx=12,dy=12,dz=12,team=" + t.getName() + "]"), true) +
                    scoreboard.Operation(admin, getObjectiveByName("" + Objective.CP + 1 + t.getName()), ComparatorType.greater, admin, getObjectiveByName("" + Objective.CP + 2 + t.getName())));

            fileCommands.add(execute.In(controlPoints.get(1).getCoordinate().getDimension(), false) +
                    execute.AsNext(new Entity("@r[limit=1,gamemode=!spectator,x=" + (controlPoints.get(1).getCoordinate().getX() - 6) + ",y=" + (controlPoints.get(1).getCoordinate().getY() - 1) + ",z=" + (controlPoints.get(1).getCoordinate().getZ() - 6) + ",dx=12,dy=12,dz=12,team=" + t.getName() + "]"), true) +
                    scoreboard.Operation(admin, getObjectiveByName("" + Objective.CP + 2 + t.getName()), ComparatorType.greater, admin, getObjectiveByName("" + Objective.CP + 1 + t.getName())));
        }
        fileCommands.add(callFunction(FileName.controlpoint_perks));

        return new FileData(FileName.team_score, fileCommands);
    }

    private FileData SpawnControlPoints() {
        ArrayList<String> fileCommands = new ArrayList<>();

        for (ControlPoint cp : cpList) {
            Coordinate c = cp.getCoordinate();
            fileCommands.add(execute.In(c.getDimension()) +
                    addForceLoad(c.getX(), c.getZ(), c.getX(), c.getZ()));
            fileCommands.add(execute.In(c.getDimension()) +
                    setBlock(c.getX(), c.getY() + 11, c.getZ(), BlockType.structure_block + "[mode=load]{metadata:\"\",mirror:\"NONE\",ignoreEntities:1b,powered:0b,seed:0L,author:\"?\",rotation:\"NONE\",posX:-6,mode:\"LOAD\",posY:-13,sizeX:13,posZ:-6,integrity:1.0f,showair:0b,name:\"" + cp.getStructureName() + "\",sizeY:14,sizeZ:13,showboundingbox:1b}", SetBlockType.destroy));

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
                    removeForceLoad(c.getX(), c.getZ(), c.getX(), c.getZ()));
        }

        // Remove leftover music discs from legacy Control Point
        fileCommands.add(killEntity("@e[type=item,nbt={Item:{id:\"minecraft:music_disc_stal\",count:1}}]"));

        return new FileData(FileName.spawn_controlpoints, fileCommands);
    }

    private FileData DisplayRank() {
        ArrayList<String> fileCommands = new ArrayList<>();

        for (Player p : players) {
            fileCommands.add(scoreboard.Set(p.getPlayerName(), getObjectiveByName(Objective.Rank), p.getRank()));
        }
        fileCommands.add(new ScoreboardObjective().setDisplay(ScoreboardLocation.sidebar, Objective.Rank));

        return new FileData(FileName.display_rank, fileCommands);
    }

    private FileData WorldPreload() {
        ArrayList<String> fileCommands = new ArrayList<>();

        fileCommands.add(scoreboard.Add(admin, getObjectiveByName(Objective.WorldLoad), 1));
        fileCommands.add(scoreboard.Add(admin, getObjectiveByName(Objective.Time), 1));
        fileCommands.add(execute.If(new Entity("@e[scores={WorldLoad=400..}]")) +
                spreadPlayers(0, 0, 5, worldSize, false, "@a"));
        fileCommands.add(execute.If(new Entity("@e[scores={Time=12000..}]"), false) +
                execute.InNext(Dimension.overworld, true) +
                setBlock(6, worldBottom + 2, 15, BlockType.bedrock));
        fileCommands.add(execute.If(new Entity("@e[scores={Time=12000..}]"), false) +
                execute.InNext(Dimension.overworld, true) +
                teleportEntity("@a[gamemode=creative]", new Coordinate(0, 221, 0)));
        fileCommands.add(execute.If(new Entity("@e[scores={Time=12000..}]")) +
                callFunction(FileName.developer_mode));
        fileCommands.add(execute.If(new Entity("@e[scores={WorldLoad=400..}]")) +
                scoreboard.Reset("@e", getObjectiveByName(Objective.WorldLoad)));

        return new FileData(FileName.world_pre_load, fileCommands);
    }

    private FileData WorldPreLoadActivation() {
        ArrayList<String> fileCommands = new ArrayList<>();

        fileCommands.add(setGameRule(GameRule.commandBlockOutput, false));
        fileCommands.add(execute.In(Dimension.overworld) +
                setBlock(6, worldBottom + 2, 15, BlockType.redstone_block));
        fileCommands.add(getObjectiveByName(Objective.WorldLoad).setDisplay(ScoreboardLocation.sidebar));

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

        // Get data
        for (int i = 0; i < 2; i++) {
            fileCommands.add(execute.As(new Entity("@e[type=minecraft:wolf]"), false) +
                    execute.StoreNext(ExecuteStore.result, "@s", getObjectiveByName(Objective.CollarCheck.extendName(i)), true) +
                    getData("@s", "Owner[" + i + "]"));

            fileCommands.add(execute.As(new Entity("@a"), false) +
                    execute.StoreNext(ExecuteStore.result, "@s", getObjectiveByName(Objective.CollarCheck.extendName(i)), true) +
                    getData("@s", "UUID[" + i + "]"));

        }
        // Players in a team
        for (Team t : teams) {
            fileCommands.add(addTag("@a[team=" + t.getName() + "]", Tag.CollarCheck));
            fileCommands.add(execute.As(new Entity("@e[type=wolf]"), false) +
                    execute.IfNext("@s", getObjectiveByName(Objective.CollarCheck.extendName(0)), ComparatorType.equal, "@p[tag=" + Tag.CollarCheck + "]", getObjectiveByName(Objective.CollarCheck.extendName(0))) +
                    execute.IfNext("@s", getObjectiveByName(Objective.CollarCheck.extendName(1)), ComparatorType.equal, "@p[tag=" + Tag.CollarCheck + "]", getObjectiveByName(Objective.CollarCheck.extendName(1)), true) +
                    modifyData("@s", "CollarColor", t.getCollarColor()));
            fileCommands.add(removeTag("@a[team=" + t.getName() + "]", Tag.CollarCheck));
        }

        // Individual players
        fileCommands.add(addTag("@a[team=]", Tag.CollarCheck));
        fileCommands.add(execute.As(new Entity("@e[type=wolf]"), false) +
                execute.IfNext("@s", getObjectiveByName(Objective.CollarCheck.extendName(0)), ComparatorType.equal, "@p[tag=" + Tag.CollarCheck + "]", getObjectiveByName(Objective.CollarCheck.extendName(0))) +
                execute.IfNext("@s", getObjectiveByName(Objective.CollarCheck.extendName(1)), ComparatorType.equal, "@p[tag=" + Tag.CollarCheck + "]", getObjectiveByName(Objective.CollarCheck.extendName(1)), true) +
                modifyData("@s", "CollarColor", "0"));
        fileCommands.add(removeTag("@a[team=]", Tag.CollarCheck));

        return new FileData(FileName.wolf_collar_execute, fileCommands);
    }

    private FileData UpdateSidebar() {
        ArrayList<String> fileCommands = new ArrayList<>();

        fileCommands.add(scoreboard.Add(admin, getObjectiveByName(Objective.SideDum), 1));
        int i = 0;
        for (ScoreboardObjective s : scoreboardObjectives) {
            if (s.getDisplaySideBar()) {
                i++;
                fileCommands.add(execute.If(new Entity("@e[scores={SideDum=" + (10 * tickPerSecond * i) + "}]")) +
                        s.setDisplay(ScoreboardLocation.sidebar));
            }
        }
        fileCommands.add(execute.If(new Entity("@e[scores={SideDum=" + (10 * tickPerSecond * i + 1) + "}]")) +
                scoreboard.Reset(admin, getObjectiveByName(Objective.SideDum)));

        // Update stripmine count
        fileCommands.add(scoreboard.Set("@a[scores={Mining=1..}]", getObjectiveByName(Objective.Mining), 0));
        fileCommands.add(execute.As(new Entity("@a")) +
                callFunction(FileName.update_mine_count));

        for (int ii = 0; ii < 5; ii++) {
            // Remove piercing enchantment
            fileCommands.add(execute.If(new Entity("@p[nbt={SelectedItem:{id:\"minecraft:crossbow\",count:1,components:{\"minecraft:enchantments\":{levels:{\"minecraft:piercing\":" + (ii + 1) + "}}}}}]")) +
                    new TellRaw("@p[nbt={SelectedItem:{id:\"minecraft:crossbow\",count:1,components:{\"minecraft:enchantments\":{levels:{\"minecraft:piercing\":" + (ii + 1) + "}}}}}]", new Text(Color.red, true, false, "PIERCING IS NOT ALLOWED, YOU NAUGHTY BUM!")).sendRaw());
            fileCommands.add(replaceItem("@p[nbt={SelectedItem:{id:\"minecraft:crossbow\",count:1,components:{\"minecraft:enchantments\":{levels:{\"minecraft:piercing\":" + (ii + 1) + "}}}}}]", InventorySlot.mainhand, BlockType.crossbow));

            // Remove power enchantment
            fileCommands.add(execute.If(new Entity("@p[nbt={SelectedItem:{id:\"minecraft:bow\",count:1,components:{\"minecraft:enchantments\":{levels:{\"minecraft:power\":" + (ii + 1) + "}}}}}]")) +
                    new TellRaw("@p[nbt={SelectedItem:{id:\"minecraft:bow\",count:1,components:{\"minecraft:enchantments\":{levels:{\"minecraft:power\":" + (ii + 1) + "}}}}}]", new Text(Color.red, true, false, "POWER IS NOT ALLOWED, YOU NAUGHTY BUM!")).sendRaw());
            fileCommands.add(replaceItem("@p[nbt={SelectedItem:{id:\"minecraft:bow\",count:1,components:{\"minecraft:enchantments\":{levels:{\"minecraft:power\":" + (ii + 1) + "}}}}}]", InventorySlot.mainhand, BlockType.bow));
        }
        // Remove wolf armor
        fileCommands.add(execute.If(new Entity("@p[nbt={SelectedItem:{id:\"minecraft:wolf_armor\",count:1}}]")) +
                new TellRaw("@p[nbt={SelectedItem:{id:\"minecraft:wolf_armor\",count:1}}]", new Text(Color.red, true, false, "WOLF ARMOR IS NOT ALLOWED, YOU NAUGHTY BUM!")).sendRaw());
        fileCommands.add(replaceItem("@p[nbt={SelectedItem:{id:\"minecraft:wolf_armor\",count:1}}]", InventorySlot.mainhand,  BlockType.leather_horse_armor));

        // Update public team CP scores
        fileCommands.add(callFunction(FileName.update_public_cp_score));

        return new FileData(FileName.update_sidebar, fileCommands);
    }

    private FileData Timer() {
        ArrayList<String> fileCommands = new ArrayList<>();
        ArrayList<TextItem> texts = new ArrayList<>();

        // Announce dead players
        fileCommands.add(execute.If(new Entity("@p[scores={Deaths=1}]")) +
                callFunction(FileName.handle_player_death));

        // Update sidebar
        fileCommands.add(callFunction(FileName.update_sidebar));

        // Add time
        fileCommands.add(scoreboard.Add(admin, getObjectiveByName(Objective.Time.extendName(2)), 1));
        fileCommands.add(scoreboard.Add(admin, getObjectiveByName(Objective.TimDum), 1));
        fileCommands.add(execute.If(new Entity("@e[scores={TimDum=" + tickPerSecond + "}]")) +
                scoreboard.Add(admin, getObjectiveByName(Objective.TimeDum), 1));
        fileCommands.add(execute.Store(ExecuteStore.result, "CurrentTime", getObjectiveByName(Objective.Time)) +
                scoreboard.Get(adminSingle, getObjectiveByName(Objective.TimeDum)));
        fileCommands.add(execute.If(new Entity("@e[scores={TimDum=" + tickPerSecond + "..}]")) +
                scoreboard.Reset(admin, getObjectiveByName(Objective.TimDum)));

        // PVP message
        fileCommands.add(execute.If(new Entity("@e[scores={Time2=" + (300 * tickPerSecond) + "}]")) +
                new TellRaw("@a", new Text(Color.gray, false, false, "PVP IS NOT ALLOWED UNTIL DAY 2!")).sendRaw());

        // Eternal day message
        texts.add(bannerText);
        texts.add(new Text(Color.gold, true, false, communityName + " UHC"));
        texts.add(bannerText);
        texts.add(new Text(Color.light_purple, true, false, "DAY TIME HAS ARRIVED & ETERNAL DAY ENABLED!"));
        texts.add(bannerText);
        fileCommands.add(execute.If(new Entity("@e[scores={Time2=" + (1200 * tickPerSecond) + "}]")) +
                new TellRaw("@a", texts).sendRaw());
        texts.clear();

        // Display quotes
        fileCommands.add(callFunction(FileName.display_quotes));

        // Locate teammates with bundle
        fileCommands.add(callFunction(FileName.locate_teammate));

        // Horse frost walker
        fileCommands.add(callFunction(FileName.horse_frost_walker));

        // Update minimum health
        fileCommands.add(callFunction(FileName.update_min_health));

        // Kill baby wolves
        fileCommands.add(callFunction(FileName.eliminate_baby_wolf));

        // Update wolf collars
        fileCommands.add(callFunction(FileName.wolf_collar_execute));

        // Set tamed wolf base health
        fileCommands.add(execute.As(new Entity("@e[type=wolf]"), false) +
                execute.IfNext(DataClasses.entity, "@s Owner", true) +
                setAttributeBase("@s", AttributeType.max_health, 20));

        // Let united players make a team
        fileCommands.add(execute.If("@p[scores={TimesCalled=1..}]") +
                callFunction(FileName.update_player_distance));

        // Update iron man candidates
        fileCommands.add(execute.Unless("@p[tag=IronMan]") +
                callFunction(FileName.check_iron_man));

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
                    fileCommands.add(execute.If(currentScoreCheck, false) +
                            execute.IfNext(currentPlayer, true) +
                            new TellRaw("@a", texts).sendRaw());

                    // Give rewards
                    String perkReceivers = "@a[team=" + team.getName() + ",tag=!" + Tag.ReceivedPerk.extendName(perk.getId()) + "]";
                    fileCommands.add(execute.If(currentScoreCheck) +
                            perk.getReward(perkReceivers));

                    // Play sound
                    fileCommands.add(execute.If(currentScoreCheck, false) +
                            execute.IfNext(currentPlayer, true) +
                            playSound(perk.getSound(), SoundSource.master, "@a", "~", "~50", "~", "100", "1", "0"));

                    // Add tag
                    fileCommands.add(execute.If(currentScoreCheck) +
                            addTag("@a[team=" + team.getName() + "]", Tag.ReceivedPerk.extendName(perk.getId())));
                }
            }

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
                fileCommands.add(execute.If(currentScoreCheck, false) +
                        execute.IfNext(currentPlayer, true) +
                        new TellRaw("@a", texts).sendRaw());

                // Give rewards
                fileCommands.add(execute.If(currentScoreCheck) +
                        perk.getReward(currentPlayer.getEntity()));

                // Play sound
                fileCommands.add(execute.If(currentScoreCheck, false) +
                        execute.IfNext(currentPlayer, true) +
                        playSound(perk.getSound(), SoundSource.master, "@a", "~", "~50", "~", "100", "1", "0"));

                // Add tag
                fileCommands.add(execute.If(currentScoreCheck) +
                        addTag(currentPlayer.getEntity(), Tag.ReceivedPerk.extendName(perk.getId())));
            }
        }

        return new FileData(FileName.controlpoint_perks, fileCommands);

    }

    // Display quotes during the match
    private FileData DisplayQuotes() {
        ArrayList<String> fileCommands = new ArrayList<>();

        for (int i = 0; i < 36; i++) {
            int index = (int) (Math.random() * quotes.size());
            fileCommands.add(execute.If(new Entity("@e[scores={Time2=" + (7 * secPerMinute * tickPerSecond * (i + 1)) + "}]")) +
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
            fileCommands.add(scoreboard.Operation("@s", getObjectiveByName(Objective.Mining), ComparatorType.add, "@s", getObjectiveByName(block)));
        }

        return new FileData(FileName.update_mine_count, fileCommands);
    }

    // Update minimum health
    private FileData UpdateMinHealth() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Find player with lowest health
        fileCommands.add(execute.As(new Entity("@r[gamemode=!spectator]"), false) +
                execute.IfNext("@s", getObjectiveByName(Objective.Hearts), ComparatorType.less, adminSingle, getObjectiveByName(Objective.MinHealth)) +
                execute.StoreNext(ExecuteStore.result, admin, getObjectiveByName(Objective.MinHealth), true) +
                scoreboard.Get("@s", getObjectiveByName(Objective.Hearts)));

        return new FileData(FileName.update_min_health, fileCommands);
    }

    // Reset health of respawned players
    private FileData RespawnPlayer() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Define player that needs to be respawned
        String respawnPlayer = "@p[tag=" + Tag.Respawn + "]";

        // Teleport player to their team
        for (Team t : teams) {
            fileCommands.add(execute.As(new Entity(respawnPlayer), false) +
                    execute.IfNext(new Entity("@s[team=" + t.getName() + "]"), true) +
                    teleportEntity("@s", "@r[gamemode=!spectator, team=" + t.getName() + "]"));
        }

        // Teleport player if they are not in a team
        fileCommands.add(execute.As(new Entity(respawnPlayer), false) +
                execute.IfNext(new Entity("@s[team=]"), true) +
                spreadPlayers(0, 0, (int) (0.3*worldSize), (int) (0.7*worldSize), false, "@s"));

        // Remove player heads
        fileCommands.add(execute.As(new Entity("@a[nbt={Inventory:[{id:\"minecraft:player_head\"}]}]")) +
                clearInventory("@s", BlockType.player_head));  // Remove from inventory
        fileCommands.add(execute.As(new Entity("@e[type=item,nbt={Item:{id:\"minecraft:player_head\"}}]")) +
                killEntity("@s")); // Remove item

        // Give players teammate tools
        if (teamMode == 2) {
            // Team caller
            fileCommands.add(execute.If("@p[tag=" + Tag.Respawn + ",team=]") +
                    giveItem(respawnPlayer, BlockType.goat_horn, "[instrument=\"minecraft:ponder_goat_horn\",use_cooldown={seconds:30},enchantments={\"minecraft:vanishing_curse\":1}]"));
        }

        // Teammate tracker
        for (Team team : teams) {
            fileCommands.add(execute.If("@p[tag=" + Tag.Respawn + ",team=" + team.getName() + "]") +
                    giveItem(respawnPlayer, BlockType.bundle.extendColor(team.getGlassColor()), "[enchantments={levels:{\"minecraft:vanishing_curse\":1}},custom_data={locateTeammate:1b}]"));
        }

        // Set respawn health
        for (int i = 0; i < 10; i++) {
            int indexFront = 2 * i + 1;
            int indexRear = 2 * (i + 1);

            fileCommands.add(execute.If(new Entity("@e[scores={MinHealth=" + indexFront + ".." + indexRear + "}]")) +
                    setAttributeBase(respawnPlayer, AttributeType.max_health, i + 1));
        }
        fileCommands.add(giveEffect(respawnPlayer, Effect.health_boost, 1, 0));
        fileCommands.add(clearEffect(respawnPlayer, Effect.health_boost));
        fileCommands.add(setAttributeBase(respawnPlayer, AttributeType.max_health, 20));

        // Set player's gamemode to survival
        fileCommands.add(execute.As(new Entity(respawnPlayer)) +
                setGameMode(GameMode.survival, "@s"));

        // Remove respawn tag
        fileCommands.add(removeTag(respawnPlayer, Tag.Respawn));

        return new FileData(FileName.respawn_player, fileCommands);
    }

    private FileData ControlPointCaptured() {
        ArrayList<String> fileCommands = new ArrayList<>();
        ArrayList<TextItem> texts = new ArrayList<>();

        // Disable command blocks
        fileCommands.add(execute.In(Dimension.overworld) +
                setBlock(8, worldBottom + 2, 0, BlockType.bedrock, SetBlockType.replace));
        fileCommands.add(execute.In(Dimension.overworld) +
                fill(15, worldBottom + 2, 3, 15, worldBottom + 2, 4, BlockType.bedrock));

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
        fileCommands.add(callFunction(FileName.teams_highscore_alive_check));

        return new FileData(FileName.control_point_captured, fileCommands);
    }

    private FileData TraitorCheck() {
        ArrayList<String> fileCommands = new ArrayList<>();

        //When no traitors remain start teams_alive_check
        fileCommands.add(execute.Unless("@a[limit=1,tag=" + Tag.Traitor + ",gamemode=!spectator]") +
                callFunction(FileName.teams_alive_check));

        fileCommands.add(execute.Unless("@a[limit=1,tag=!" + Tag.Traitor + ",gamemode=!spectator]") +
                callFunction(FileName.victory_message_traitor));

        return new FileData(FileName.traitor_check, fileCommands);
    }

    private FileData TeamsAliveCheck() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Players in teams
        for (int i = 0; i < teams.size(); i++) {
            fileCommands.add(execute.Unless("@a[limit=1,team=!" + teams.get(i).getName() + ",gamemode=!spectator]") +
                    callFunction("" + FileName.victory_message_ + i));
        }

        // Players without a team
        fileCommands.add(addTag("@p[team=,gamemode=!spectator]", Tag.AmIWinning));
        fileCommands.add(execute.Unless("@p[tag=!AmIWinning,gamemode=!spectator]", false) +
                execute.AsNext("@p[tag=AmIWinning]", true) +
                callFunction(FileName.victory_message_solo));
        fileCommands.add(removeTag("@p[tag=AmIWinning]", Tag.AmIWinning));

        return new FileData(FileName.teams_alive_check, fileCommands);
    }

    private FileData TeamsHighscoreCheck() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Players in teams
        for (int i = 0; i < teams.size(); i++) {
            for (int j = 1; j < 3; j++) {
                fileCommands.add(execute.If(new Entity("@e[scores={Victory=1}]"), false) +
                        execute.IfNext(new Entity("@p[team=" + teams.get(i).getName() + ",gamemode=!spectator,scores={ControlPoint" + j + "=" + (maxCPScore * tickPerSecond) + "..},tag=!" + Tag.Traitor + "]"), true) +
                        callFunction("" + FileName.victory_message_ + i));
                fileCommands.add(execute.If(new Entity("@e[scores={Victory=1}]"), false) +
                        execute.IfNext("@p[team=" + teams.get(i).getName() + ",gamemode=!spectator,scores={ControlPoint" + j + "=" + (maxCPScore * tickPerSecond) + "..},tag=" + Tag.Traitor + "]") +
                        execute.UnlessNext("@p[team=" + teams.get(i).getName() + ",gamemode=!spectator,scores={ControlPoint" + j + "=" + (maxCPScore * tickPerSecond) + "..},tag=!" + Tag.Traitor + "]", true) +
                        callFunction(FileName.victory_message_traitor));
            }
        }

        // Individual players
        for (int j = 1; j < 3; j++) {
            fileCommands.add(execute.If(new Entity("@e[scores={Victory=1}]"), false) +
                    execute.IfNext(new Entity("@p[team=,gamemode=!spectator,scores={ControlPoint" + j + "=" + (maxCPScore * tickPerSecond) + "..},tag=!" + Tag.Traitor + "]")) +
                    execute.AsNext("@p[team=,gamemode=!spectator,scores={ControlPoint" + j + "=" + (maxCPScore * tickPerSecond) + "..},tag=!" + Tag.Traitor + "]", true) +
                    callFunction(FileName.victory_message_solo));
            fileCommands.add(execute.If(new Entity("@e[scores={Victory=1}]"), false) +
                    execute.IfNext("@p[team=,gamemode=!spectator,scores={ControlPoint" + j + "=" + (maxCPScore * tickPerSecond) + "..},tag=" + Tag.Traitor + "]", true) +
                    callFunction(FileName.victory_message_traitor));
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
        boolean debug = false;

        for (Team t : teams) {
            for (int i = 0; i < 3; i++) {
                fileCommands.add(execute.As(new Entity("@a[team=" + t.getName() + ",nbt={SelectedItem:{id:\"minecraft:" + BlockType.bundle.extendColor(t.getGlassColor()) + "\",components:{\"minecraft:custom_data\":{locateTeammate:1b}}}}]"), false) +
                        execute.AtNext(new Entity("@s")) +
                        execute.IfNext(new Entity("@a[team=" + t.getName() + ",distance=0.1..,gamemode=!spectator]")) +
                        execute.FacingNext(new Entity("@a[team=" + t.getName() + ",distance=0.1..,gamemode=!spectator,limit=1,sort=nearest]"), EntityAnchor.eyes) +
                        execute.PositionedNext(new Coordinate(0, 1, 0, ReferenceFrame.relative)) +
                        execute.PositionedNext(new Coordinate(0, 0, i + 1, ReferenceFrame.relative_facing), true) +
                        createParticle(Particle.dust + "{color:[" + t.getDustColor() + "],scale:1}", new Coordinate(0, 0, 0, ReferenceFrame.relative), new Coordinate(0, 0, 0), 0, 1, "@s"));
            }

            if (debug) {
                ArrayList<TextItem> texts = new ArrayList<>();

                texts.add(new Text(false, false, t.getName() + " has players "));
                texts.add(new Select(false, false, "@a[team=" + t.getName() + ",nbt={SelectedItem:{id:\\\"minecraft:" + BlockType.bundle.extendColor(t.getGlassColor()) + "\\\",components:{\\\"minecraft:custom_data\\\":{locateTeammate:1b}}}}]"));
                texts.add(new Text(false, false, "Who are holding their bundle"));

                fileCommands.add(new TellRaw("@a[tag=Debug]", texts).sendRaw());
                texts.clear();
            }
        }



        return new FileData(FileName.locate_teammate, fileCommands);
    }

    private FileData EliminateBabyWolf() {
        ArrayList<String> fileCommands = new ArrayList<>();

        Entity babyWolf = new Entity("@e[type=wolf,scores={WolfAge=..-1}]");

        fileCommands.add(execute.As(new Entity("@e[limit=1, type=wolf, sort=random]"), false) +
                execute.StoreNext(ExecuteStore.result, "@s", getObjectiveByName(Objective.WolfAge), true) +
                getData("@s", "Age"));
        fileCommands.add(execute.At(babyWolf) +
                summonEntity(EntityType.dolphin));
        fileCommands.add(execute.As(babyWolf) +
                killEntity("@s"));

        return new FileData(FileName.eliminate_baby_wolf, fileCommands);
    }

    private FileData UpdatePublicCPScore() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Players in teams
        for (Team t : teams) {
            for (int i = 1; i < controlPoints.size() + 1; i++) {
                fileCommands.add(scoreboard.Operation(t.getPlayerColor(), getObjectiveByName(Objective.CPScore), ComparatorType.greater, admin, getObjectiveByName("" + Objective.CP + i + t.getName())));
            }
        }

        // Players without a team
        for (int i = 1; i < controlPoints.size() + 1; i++) {
            fileCommands.add(scoreboard.Operation("Solo", getObjectiveByName(Objective.CPScore), ComparatorType.greater, "@r[team=]", getObjectiveByName(Objective.ControlPoint.extendName(i))));
        }

        return new FileData(FileName.update_public_cp_score, fileCommands);
    }

    private FileData DisableRespawn() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Add first blood tag
        fileCommands.add(addTag(admin, Tag.RespawnDisabled));

        // Update immediate respawn
        fileCommands.add(setGameRule(GameRule.doImmediateRespawn, false));

        return new FileData(FileName.disable_respawn, fileCommands);
    }

    private FileData JoinTeam() {
        ArrayList<String> fileCommands = new ArrayList<>();
        Boolean debug = false;

        String lookingPlayer = "@p[tag=LookingForTeamMate]";

        String filledTeam;
        ArrayList<TextItem> texts = new ArrayList<>();
        for (int i = 0; i < (teams.size() - 1); i++) {
            filledTeam = execute.Unless("@p[team=" + teams.get(i).getName() + "]", false);

            if (debug) {
                texts.add(new Select(false, false, lookingPlayer));
                texts.add(new Text(Color.white, false, false, " is looking for a team mate"));
                fileCommands.add(new TellRaw("@a[tag=Debug]", texts).sendRaw());
                texts.clear();

                texts.add(new Select(false, false, "@p[tag=LookingForTeamMate,team=]"));
                texts.add(new Text(Color.white, false, false, " is looking for a team mate and is not in a team"));
                fileCommands.add(new TellRaw("@a[tag=Debug]", texts).sendRaw());
                texts.clear();

                texts.add(new Select(false, false, "@p[team=" + teams.get(i).getName() + "]"));
                texts.add(new Text(Color.white, false, false, " is already in team "));
                texts.add(new Text(teams.get(i).getColor(), false, false, teams.get(i).getJSONColor()));
                fileCommands.add(new TellRaw("@a[tag=Debug]", texts).sendRaw());
                texts.clear();

                texts.add(new Select(false, false, "@p[limit=2,team=,gamemode=!spectator]"));
                texts.add(new Text(Color.white, false, false, " will join "));
                texts.add(new Text(teams.get(i).getColor(), false, false, teams.get(i).getJSONColor()));
                fileCommands.add(execute.At(lookingPlayer) +
                        new TellRaw("@a[tag=Debug]", texts).sendRaw());
                texts.clear();
            }

            // Announce that players formed a team
            texts.add(new Select(false, false, "@p[limit=2,team=,gamemode=!spectator]"));
            texts.add(new Text(Color.white, false, false, " have decided to join forces as team "));
            texts.add(new Text(teams.get(i).getColor(), false, false, teams.get(i).getJSONColor()));
            texts.add(new Text(Color.white, false, false, "!"));

            fileCommands.add(filledTeam +
                    execute.IfNext("@p[tag=LookingForTeamMate,team=]") +
                    execute.AtNext(lookingPlayer, true) +
                    new TellRaw("@a", texts).sendRaw());
            texts.clear();

            // Try to let players join team
            fileCommands.add(filledTeam +
                    execute.IfNext("@p[tag=LookingForTeamMate,team=]") +
                    execute.AtNext(lookingPlayer, true) +
                    teams.get(i).joinTeam("@p[limit=2,team=,gamemode=!spectator]"));
        }

        fileCommands.add(execute.At(lookingPlayer) +
                clearInventory("@p[limit=2,gamemode=!spectator]", BlockType.goat_horn));


        for (Team team : teams) {
            fileCommands.add(execute.At(lookingPlayer, false) +
                    execute.IfNext("@p[tag=LookingForTeamMate,team=" + team.getName() + "]", true) +
                    giveItem("@p[limit=2,gamemode=!spectator]", BlockType.bundle.extendColor(team.getGlassColor()), "[enchantments={levels:{\"minecraft:vanishing_curse\":1}},custom_data={locateTeammate:1b}]"));
        }

        return new FileData(FileName.join_team, fileCommands);
    }

    private FileData UpdatePlayerDistance() {
        ArrayList<String> fileCommands = new ArrayList<>();
        ArrayList<TextItem> texts = new ArrayList<>();
        boolean debug = false;

        String checkingPlayer = "@p[team=,scores={TimesCalled=1..}]";
        ComparatorType comparator;

        // Give player playing the horn a tag
        fileCommands.add(addTag(checkingPlayer, Tag.LookingForTeamMate));

        if (debug) {
            texts.add(new Select(false, false, "@p[team=,scores={TimesCalled=1..}]"));
            texts.add(new Text(Color.white, false, false, " has been given the LookingForTeamMate tag."));
            fileCommands.add(new TellRaw("@a[tag=Debug]", texts).sendRaw());
            texts.clear();
        }

        // Loop through Cartesian coordinates
        for (int i = 0; i < cartesian.length; i++) {
            // Find positions of each player
            fileCommands.add(execute.As("@a[team=]", false) +
                    execute.StoreNext(ExecuteStore.result, "@s", getObjectiveByName(Objective.Pos + cartesian[i]), true) +
                    getData("@s", "Pos[" + i + "]", 1));

            if (debug) {
                for (Player player : players) {
                    texts.add(new Select(false, false, "@p[name=" + player.getPlayerName() + ",team=]"));
                    texts.add(new Text(Color.white, false, false, " is located at " + cartesian[i] + " = "));
                    texts.add(new Score(false, false, "@p[name=" + player.getPlayerName() + ",team=]", Objective.Pos.extendName(cartesian[i])));
                    fileCommands.add(new TellRaw("@a[tag=Debug]", texts).sendRaw());
                    texts.clear();
                }
            }

            // Subtract distance of nearest player in Cartesian coordinate
            fileCommands.add(execute.As(checkingPlayer, false) +
                    execute.AtNext(checkingPlayer, true) +
                    scoreboard.Operation("@s", getObjectiveByName(Objective.Pos + cartesian[i]), ComparatorType.subtract, "@p[tag=!LookingForTeamMate,team=,gamemode=!spectator]", getObjectiveByName(Objective.Pos + cartesian[i])));

            if (debug) {
                texts.add(new Text(Color.white, false, false, "The closest person to "));
                texts.add(new Select(false, false, checkingPlayer));
                texts.add(new Text(Color.white, false, false, " without the LookingForTeamMate tag is "));
                texts.add(new Select(false, false, "@p[tag=!LookingForTeamMate,team=]"));
                texts.add(new Text(Color.white, false, false, ".\\nThey are "));
                texts.add(new Score(false, false, checkingPlayer, Objective.Pos.extendName(cartesian[i])));
                texts.add(new Text(Color.white, false, false, " blocks away in the " + cartesian[i] + " direction."));
                fileCommands.add(execute.At(checkingPlayer) +
                        new TellRaw("@a[tag=Debug]", texts).sendRaw());
                texts.clear();
            }

            // Square the difference in Cartesian coordinates
            fileCommands.add(execute.As(checkingPlayer) +
                    scoreboard.Operation("@s", getObjectiveByName(Objective.Square + cartesian[i]), ComparatorType.equal, "@s", getObjectiveByName(Objective.Pos + cartesian[i])));
            fileCommands.add(execute.As(checkingPlayer) +
                    scoreboard.Operation("@s", getObjectiveByName(Objective.Square + cartesian[i]), ComparatorType.multiply, "@s", getObjectiveByName(Objective.Pos + cartesian[i])));

            if (i == 0) { comparator = ComparatorType.equal; }
            else { comparator = ComparatorType.add; }

            // Calculate distance to nearest player
            fileCommands.add(execute.As(checkingPlayer) +
                    scoreboard.Operation("@s", getObjectiveByName(Objective.Distance), comparator, "@s", getObjectiveByName(Objective.Square + cartesian[i])));
        }

        if (debug) {
            texts.add(new Text(Color.white, false, false, "The distance between "));
            texts.add(new Select(false, false, "@p[tag=!LookingForTeamMate]"));
            texts.add(new Text(Color.white, false, false, " without the LookingForTeamMate tag and "));
            texts.add(new Select(false, false, checkingPlayer));
            texts.add(new Text(Color.white, false, false, " is "));
            texts.add(new Score(false, false, checkingPlayer, Objective.Distance));
            texts.add(new Text(Color.white, false, false, "\\nThe distance needs to be less than " + (minJoinDistance * minJoinDistance)));
            fileCommands.add(execute.At(checkingPlayer) +
                    new TellRaw("@a[tag=Debug]", texts).sendRaw());
            texts.clear();
        }

        // Ignore players that are already in a team
        texts.add(new Text(Color.red, true, false, "You are already on a team! Don't be greedy!"));

        String playerInTeam = "@p[scores={TimesCalled=1..},team=!]";
        fileCommands.add(execute.If(playerInTeam) +
                new TellRaw(playerInTeam, texts).sendRaw());
        texts.clear();

        if (debug) {
            texts.add(new Select(false, false, "@p[scores={TimesCalled=1..},team=!]"));
            texts.add(new Text(Color.white, false, false, " already has a team and tries to team up."));
            fileCommands.add(new TellRaw("@a[tag=Debug]", texts).sendRaw());
            texts.clear();

            texts.add(new Select(false, false, "@p[tag=LookingForTeamMate,scores={Distance=.." + (minJoinDistance * minJoinDistance) + "},team=]"));
            texts.add(new Text(Color.white, false, false, " tries to team up, is in range and has no team yet."));
            fileCommands.add(new TellRaw("@a[tag=Debug]", texts).sendRaw());
            texts.clear();
        }

        // Call join team function if other player is in range
        String playerInRange = "@p[tag=LookingForTeamMate,scores={Distance=.." + (minJoinDistance * minJoinDistance) + ",IsKiller=0},team=]";
        fileCommands.add(execute.If(playerInRange, false) +
                execute.AtNext(playerInRange) +
                execute.UnlessNext("@p[tag=!LookingForTeamMate,gamemode=!spectator]", Objective.IsKiller, 1, true) +
                callFunction(FileName.join_team));

        // Refuse call if player is too far away
        texts.add(new Text(Color.red, true, false, "You need to be within " + minJoinDistance + " blocks of a player without a team to form a team!"));

        String playerTooFar = "@p[tag=LookingForTeamMate,scores={Distance=" + (minJoinDistance * minJoinDistance) + "..},team=,gamemode=!spectator]";
        fileCommands.add(execute.If(playerTooFar, false) +
                execute.UnlessNext(playerTooFar, Objective.IsKiller, 1, true) +
                new TellRaw(playerTooFar, texts).sendRaw());
        texts.clear();

        if (debug) {
            texts.add(new Select(false, false, "@p[tag=LookingForTeamMate,scores={Distance=" + (minJoinDistance * minJoinDistance) + "..},team=]"));
            texts.add(new Text(Color.white, false, false, " tries to team up, but is not in range."));
            fileCommands.add(new TellRaw("@a[tag=Debug]", texts).sendRaw());
            texts.clear();
        }

        // Refuse killers
        texts.add(new Text(Color.red, true, false, "You are a killer! No team for you!"));

        String playerKiller = "@p[tag=LookingForTeamMate,scores={IsKiller=1}]";
        fileCommands.add(execute.If(playerKiller) +
                new TellRaw(playerKiller, texts).sendRaw());
        texts.clear();

        // Warn against killers
        texts.add(new Text(Color.red, true, false, "Watch out! They are a killer!"));

        fileCommands.add(execute.At(playerInRange, false) +
                execute.IfNext("@p[tag=!LookingForTeamMate,gamemode=!spectator]", Objective.IsKiller, 1, true) +
                new TellRaw(playerInRange, texts).sendRaw());
        texts.clear();

        // Reset tag and call scoreboard objective
        fileCommands.add(removeTag("@p[scores={TimesCalled=1..}]", Tag.LookingForTeamMate));
        fileCommands.add(scoreboard.Reset("@p[scores={TimesCalled=1..}]", getObjectiveByName(Objective.TimesCalled)));

        return new FileData(FileName.update_player_distance, fileCommands);
    }

    private FileData CheckIronMan() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Give random player with no damage taken iron man candidate
        fileCommands.add(addTag("@r[scores={DamageTaken=.." + minDamage + "}]", Tag.IronManCandidate));

        // Check if there are other potential iron man candidates
        fileCommands.add(execute.Unless("@a[tag=!IronManCandidate,scores={DamageTaken=.." + minDamage + "}]", false) +
                execute.AsNext("@p[tag=IronManCandidate]", true) +
                callFunction(FileName.announce_iron_man));

        // Remove iron man candidate tag
        fileCommands.add(removeTag("@p[tag=IronManCandidate]", Tag.IronManCandidate));

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
        fileCommands.add(addTag("@s", Tag.IronMan));

        return new FileData(FileName.announce_iron_man, fileCommands);
    }

    private FileData DebugGive() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Give player Debug tag
        fileCommands.add(addTag("@s", Tag.Debug));

        return new FileData(FileName.debug_give, fileCommands);
    }

    private FileData DebugRemove() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Remove Debug tag from player
        fileCommands.add(removeTag("@s", Tag.Debug));

        return new FileData(FileName.debug_remove, fileCommands);
    }

    private FileData TitleDefaultTiming() {
        ArrayList<String> fileCommands = new ArrayList<>();

        // Change title display time
        fileCommands.add(changeTitleDisplayTime("@a", 10, 70, 20, Duration.ticks));

        return new FileData(FileName.title_default_timing, fileCommands);
    }

    private FileData CurrentTestFunction() {
        ArrayList<String> fileCommands = new ArrayList<>();

        return new FileData(FileName.current_test_function, fileCommands);
    }
}

