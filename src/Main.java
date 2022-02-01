import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        new Main().run();
    }

    //DatapackData<
    private static int gameMode = 1;
    /*
    * 1: The Diorite Experts
    * 2: University Racing Eindhoven
     */

    private static int adminMode = 1;
    /*
    * 1: Wouter
    * 2: Bas
     */

    private String uhcNumber;
    private static String version = "3.0";
    private String userFolder;
    private String worldName;
    private String dataPackLocation;
    private String worldLocation;
    private String dataPackName;
    private String fileLocation;

    //DatapackData>

    //GameData<
    private static int chestSize = 27;
    private String commandCenter = "s44";
    private String admin;
    private String startCoordinates;
    private ArrayList<Team> teams = new ArrayList<>();
    private ArrayList<ControlPoint> controlPoints = new ArrayList<>();
    private ArrayList<CarePackage> carePackages = new ArrayList<>();
    private ArrayList<ScoreboardObjective> scoreboardObjectives = new ArrayList<>();
    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<StatusEffect> effect = new ArrayList<>();
    private static int worldSize = 1500;
    private static int worldHeight = 257;
    private static int worldBottom = -64;
    private static int tickPerSecond = 20;
    private int minTraitorRank;
    private String communityName;
    //GameData>


    private ArrayList<FileData> files = new ArrayList<>();


    private void run() {
        boolean menuRunning = true;
        Scanner scanner = new Scanner(System.in);
        String input;
        initSaveDir();
        initGameData();
        makeFunctionFiles();
        makeRecipeFiles();
        makeLootTableFiles();
        System.out.println("Files available:\n");
        for (FileData data : files) {
            System.out.println(data.getName());
        }
        System.out.println("\n-----------------");
        while (menuRunning) {
            System.out.print("> ");
            input = scanner.nextLine();
            if (input.equals("exit")) {
                menuRunning = false;
                System.out.println("Exiting System.");
            } else if (input.startsWith("update ")) {
                String command = input.replace("update ", "");
                if (command.equals("all")) {
                    updateAllFiles();
                } else {
                    for (FileData f : files) {
                        if (f.getNameWithoutExtension().equals(command)) {
                            try {
                                writeFile(f, fileLocation);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } else if (input.equals("create datapack")) {
                try {
                    createDatapack();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Input not recognized.");
            }
        }
    }

    private void updateAllFiles() {
        for (FileData f : files) {
            try {
                writeFile(f, fileLocation);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createDatapack() throws IOException {
        File file = new File(dataPackLocation + dataPackName);
        if (file.mkdir()) {
            File pack = new File(dataPackLocation + dataPackName + "\\pack.mcmeta");
            BufferedWriter writer = new BufferedWriter(new FileWriter(pack));
            writer.write(" {");
            writer.newLine();
            writer.write("       \"pack\": {");
            writer.newLine();
            writer.write("           \"pack_format\": 3,");
            writer.newLine();
            writer.write("           \"description\": \"A datapack designed specifically for UHC. Version: " + version + ".\"");
            writer.newLine();
            writer.write("       }");
            writer.newLine();
            writer.write("   }");
            writer.close();

            File data = new File(dataPackLocation + dataPackName + "\\data");
            if (data.mkdir()) {
                File uhc = new File(dataPackLocation + dataPackName + "\\data\\uhc");
                if (uhc.mkdir()) {
                    File functions = new File(dataPackLocation + dataPackName + "\\data\\uhc\\functions");
                    if (!functions.mkdir()) {
                        System.out.println("Something went wrong creating Datapack 1");
                    }
                    File recipes = new File(dataPackLocation + dataPackName + "\\data\\uhc\\recipes");
                    if (!recipes.mkdir()) {
                        System.out.println("Something went wrong creating Datapack 2");
                    }
                    File lootTables = new File(dataPackLocation + dataPackName + "\\data\\uhc\\loot_tables");
                    if (lootTables.mkdir()) {
                        updateAllFiles();
                    } else {
                        System.out.println("Something went wrong creating Datapack 3");
                    }
                }
            }
            String from = "generated";
            String to = worldLocation + "\\generated";
            copyDirectory(from, to);
        } else {
            System.out.println("Datapack already exists: Updating files now...");
            updateAllFiles();
        }
    }

    public static void copyDirectory(String sourceDirectoryLocation, String destinationDirectoryLocation)
            throws IOException {
        Files.walk(Paths.get(sourceDirectoryLocation))
                .forEach(source -> {
                    Path destination = Paths.get(destinationDirectoryLocation, source.toString()
                            .substring(sourceDirectoryLocation.length()));
                    try {
                        Files.copy(source, destination);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    private void writeFile(FileData fileData, String fileLocation) throws IOException {
        ArrayList<String> fileText = fileData.getFileText();
        File file;
        if (fileData.getType().equals("recipe")) {
            file = new File(fileLocation + "recipes\\" + fileData.getName() + ".json");
        } else if (fileData.getType().equals("loot_tables")) {
            file = new File(fileLocation + "loot_tables\\" + fileData.getName() + ".json");
        }
        else {
            file = new File(fileLocation + "functions\\" + fileData.getName() + ".mcfunction");
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        for (String s : fileText) {
            writer.write(s);
            writer.newLine();
        }
        if (fileData.getType().equals("function")) {
            writer.write("#This file was made using the \"UHC-Datapack-Creator\" current version: " + version + ".");
            writer.newLine();
            writer.write("#If you want to make any changes to this file please contact the UHC-Committee member: Perfidy.");
            writer.newLine();
            writer.write("#He will know how to change it. (Without messing things up...)");
        }
        writer.close();
        System.out.println("File \"" + fileData.getName() + "\" Updated.");
    }

    private void initSaveDir() {
        switch (gameMode) {
            case 1:
                uhcNumber = "S44";

                admin = "PerfidyIsKey";
                break;
            case 2:
                uhcNumber = "URE5";

                admin = "Snodog627";
                break;
        }

        switch(adminMode){
            case 1:
                userFolder = "Wouter Baltus";
                worldName = "big-test";
                worldLocation = "D:\\Documents\\Gaming\\MinecraftServers\\MinecraftServers\\world\\";
                break;
            case 2:
                userFolder = "bthem";
                worldName = "world";
                worldLocation = "C:\\Users\\" + userFolder + "\\Desktop\\Server\\" + worldName + "\\";
                break;
        }

        dataPackLocation = worldLocation + "datapacks\\";

        dataPackName = "uhc-datapack-" + uhcNumber + "v" + version;
        fileLocation = dataPackLocation + dataPackName + "\\data\\uhc\\";

    }

    private void initGameData() {
        String[] colors = {"yellow", "blue", "red", "dark_purple", "dark_green", "light_purple", "black", "gold", "gray", "aqua", "dark_red", "dark_blue", "dark_aqua"};
        String[] bossbarColors = {"yellow", "blue", "red", "purple", "green", "pink", "white", "white", "white", "white", "white", "white", "white"};
        String[] glassColors = {"yellow", "light_blue", "red", "purple", "green", "pink", "black", "orange", "gray", "cyan", "red", "blue", "blue"};
        String[] collarColors = {"4", "3", "14", "10", "13", "6", "15", "1", "7", "9", "2", "11", "9"};
        for (int i = 0; i < colors.length; i++) {
            Team team = new Team("Team" + i, colors[i], bossbarColors[i], glassColors[i], collarColors[i]);
            teams.add(team);
        }

        ControlPoint cp1 = new ControlPoint("CP1", 48000, 2, 0, 0, 0);
        ControlPoint cp2 = new ControlPoint("CP2", 48000, 3, 0, 0, 0);

        CarePackage carePackage2 = new CarePackage("anti_cp", "Anti Controlpoint Drop",
                "[{Slot:1b,id:\"minecraft:gunpowder\",Count:1b},{Slot:2b,id:\"minecraft:gunpowder\",Count:1b},{Slot:3b,id:\"minecraft:tnt\",Count:1b},{Slot:4b,id:\"minecraft:flint_and_steel\",Count:1b},{Slot:5b,id:\"minecraft:tnt\",Count:1b},{Slot:6b,id:\"minecraft:sand\",Count:1b},{Slot:7b,id:\"minecraft:sand\",Count:1b},{Slot:11b,id:\"minecraft:enchanted_book\",Count:1b,tag:{StoredEnchantments:[{lvl:4s,id:\"minecraft:blast_protection\"}]}},{Slot:12b,id:\"minecraft:lava_bucket\",Count:1b},{Slot:13b,id:\"minecraft:tnt\",Count:1b},{Slot:14b,id:\"minecraft:lava_bucket\",Count:1b},{Slot:15b,id:\"minecraft:enchanted_book\",Count:1b,tag:{StoredEnchantments:[{lvl:4s,id:\"minecraft:blast_protection\"}]}},{Slot:19b,id:\"minecraft:sand\",Count:1b},{Slot:20b,id:\"minecraft:sand\",Count:1b},{Slot:21b,id:\"minecraft:tnt\",Count:1b},{Slot:22b,id:\"minecraft:flint_and_steel\",Count:1b},{Slot:23b,id:\"minecraft:tnt\",Count:1b},{Slot:24b,id:\"minecraft:gunpowder\",Count:1b},{Slot:25b,id:\"minecraft:gunpowder\",Count:1b}]",
                0, 0, 0);

        switch (gameMode) {
            case 1:
                startCoordinates = "4 64 5";
                minTraitorRank = 40;
                communityName = "THE DIORITE EXPERTS";

                // Control point
                cp1.setX(-31);
                cp1.setY(64);
                cp1.setZ(193);

                cp2.setX(-22);
                cp2.setY(63);
                cp2.setZ(-142);
                controlPoints.add(cp1);
                controlPoints.add(cp2);

                // Care Packages
                carePackage2.setX(25);
                carePackage2.setY(69);
                carePackage2.setZ(18);
                carePackages.add(carePackage2);

                // Players
                players.add(new Player("Snodog627",94,true));
                players.add(new Player("Mr9Madness",88));
                players.add(new Player("PR0BA",1));
                players.add(new Player("Tiba101",5));
                players.add(new Player("W0omy",20));
                players.add(new Player("MissTutuPrincess",17));
                players.add(new Player("Kalazniq",43));
                players.add(new Player("Vladik71",38));
                players.add(new Player("Smashking242",16));
                players.add(new Player("Pfalz_",18));
                players.add(new Player("ThurianBohan",44));
                players.add(new Player("PerfidyIsKey",69,true));
                players.add(new Player("deuce__",20));
                players.add(new Player("jonmo0105",64));
                players.add(new Player("TheDinoGame",216));
                players.add(new Player("BAAPABUGGETS",11));
                players.add(new Player("Kakarot057",45));
                players.add(new Player("viccietors",41));
                players.add(new Player("Rayqson",16));
                players.add(new Player("Xx__HexGamer__xX",72));
                players.add(new Player("Bobdafish",120));
                players.add(new Player("Alanaenae",0));
                players.add(new Player("jk20028",22));
                players.add(new Player("N_G0n",6));
                players.add(new Player("SpookySpiker",64));
                players.add(new Player("Clockweiz",11));
                players.add(new Player("Eason950116",13));
                players.add(new Player("CorruptUncle",38));
                players.add(new Player("Pimmie36",13));
                players.add(new Player("Jayroon123",0));
                players.add(new Player("PbQuinn",3));

                break;
            case 2:
                startCoordinates = "-4 85 -4";
                minTraitorRank = 20;
                communityName = "UNIVERSITY RACING EINDHOVEN";

                // Control point
                cp1.setX(-33);
                cp1.setY(57);
                cp1.setZ(163);

                cp2.setX(275);
                cp2.setY(50);
                cp2.setZ(-98);
                controlPoints.add(cp1);
                controlPoints.add(cp2);

                // Care package
                carePackage2.setX(146);
                carePackage2.setY(62);
                carePackage2.setZ(-15);
                carePackages.add(carePackage2);

                // Players
                players.add(new Player("Bertje13",0));
                players.add(new Player("Lefke67",8));
                players.add(new Player("SpookySpiker",16));
                players.add(new Player("joep359",32));
                players.add(new Player("Snodog627",156,true));
                players.add(new Player("Mafkees__10",91));
                players.add(new Player("woutje33",78));
                players.add(new Player("CorruptUncle",52));
                players.add(new Player("Luuk",2));
                players.add(new Player("sepertibos",6));
                players.add(new Player("Clik_clak",10));
                players.add(new Player("HumblesBumblesV2",12));
                players.add(new Player("RoyalGub",23));
                players.add(new Player("Chrissah58",16));
                players.add(new Player("TNTbuilder21",31));
                players.add(new Player("Pimmie36",96));
                players.add(new Player("lenschoenie98",52));
                players.add(new Player("PbQuinn",0));
                players.add(new Player("Luc_B21",2));
                players.add(new Player("Captain_Kills",5));
                players.add(new Player("PeterBeTripin",7));
                players.add(new Player("Jayroon123",16,true));
                players.add(new Player("JD329",12));
                players.add(new Player("maxim_rongen",17));
                players.add(new Player("ThurianBohan",19));
                players.add(new Player("Jobbo2002",88));

                break;
        }

        CarePackage carePackage1 = new CarePackage("enchanting", "Enchanting Drop",
                "[{Slot:3b,id:\"minecraft:enchanted_book\",Count:1b,tag:{StoredEnchantments:[{lvl:1s,id:\"minecraft:power\"}]}},{Slot:4b,id:\"minecraft:golden_apple\",Count:1b},{Slot:5b,id:\"minecraft:enchanted_book\",Count:1b,tag:{StoredEnchantments:[{lvl:2s,id:\"minecraft:sharpness\"}]}},{Slot:12b,id:\"minecraft:apple\",Count:1b},{Slot:13b,id:\"minecraft:anvil\",Count:1b},{Slot:14b,id:\"minecraft:apple\",Count:1b},{Slot:21b,id:\"minecraft:enchanted_book\",Count:1b,tag:{StoredEnchantments:[{lvl:2s,id:\"minecraft:sharpness\"}]}},{Slot:22b,id:\"minecraft:book\",Count:1b},{Slot:23b,id:\"minecraft:enchanted_book\",Count:1b,tag:{StoredEnchantments:[{lvl:1s,id:\"minecraft:protection\"}]}}]",
                16, 70, 236);
        //carePackages.add(carePackage1);

        // Scoreboard objectives
        scoreboardObjectives.add(new ScoreboardObjective("Admin", "dummy"));
        scoreboardObjectives.add(new ScoreboardObjective("TimDum", "dummy"));
        scoreboardObjectives.add(new ScoreboardObjective("TimeDum", "dummy", "\"Elapsed Time\""));
        scoreboardObjectives.add(new ScoreboardObjective("Time", "dummy", "\"Elapsed Time\"",true));
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
        scoreboardObjectives.add(new ScoreboardObjective("Hearts", "health",true));
        scoreboardObjectives.add(new ScoreboardObjective("Apples", "minecraft.used:minecraft.golden_apple", "\"Golden Apple\"",true));
        scoreboardObjectives.add(new ScoreboardObjective("Mining", "minecraft.mined:minecraft.stone", "\"I like mining-leaderboard\"",true));
        scoreboardObjectives.add(new ScoreboardObjective("Deaths", "deathCount"));
        scoreboardObjectives.add(new ScoreboardObjective("Kills", "playerKillCount",true));
        scoreboardObjectives.add(new ScoreboardObjective("Crystal", "dummy"));
        scoreboardObjectives.add(new ScoreboardObjective("Quits", "minecraft.custom:minecraft.leave_game"));
        scoreboardObjectives.add(new ScoreboardObjective("Rank", "dummy"));
        scoreboardObjectives.add(new ScoreboardObjective("WorldLoad","dummy"));
        scoreboardObjectives.add(new ScoreboardObjective("CollarCheck0","dummy"));
        scoreboardObjectives.add(new ScoreboardObjective("CollarCheck1","dummy"));

        // Status effects
        effect.add(new StatusEffect("glowing",30,1));
        effect.add(new StatusEffect("fire_resistance",20,1));
        effect.add(new StatusEffect("nausea",10,1));
        effect.add(new StatusEffect("speed",20,1));
    }

    private void makeLootTableFiles() {
        ArrayList<LootTableEntry> lootEntry = new ArrayList<>();

        // Loot table items
        lootEntry.add(new LootTableEntry(20,"egg"));
        lootEntry.add(new LootTableEntry(3,"saddle"));
        lootEntry.add(new LootTableEntry(1,"netherite_hoe"));
        lootEntry.add(new LootTableEntry(20,"ladder"));
        lootEntry.add(new LootTableEntry(3,"spectral_arrow", new LootTableFunction(2,0.3)));
        lootEntry.add(new LootTableEntry(1,"trident"));
        lootEntry.add(new LootTableEntry(3,"horse_spawn_egg"));
        lootEntry.add(new LootTableEntry(2,"diamond_horse_armor"));
        lootEntry.add(new LootTableEntry(5,"experience_bottle", new LootTableFunction(3,0.2)));
        lootEntry.add(new LootTableEntry(1,"wolf_spawn_egg", new LootTableFunction(2,0.01)));
        lootEntry.add(new LootTableEntry(4,"lapis_lazuli", new LootTableFunction(2)));
        lootEntry.add(new LootTableEntry(7,"glass", new LootTableFunction(3)));
        lootEntry.add(new LootTableEntry(2,"nether_wart", new LootTableFunction(5)));
        lootEntry.add(new LootTableEntry(2,"blaze_rod",new LootTableFunction(2,0.1)));
        lootEntry.add(new LootTableEntry(8,"melon_slice",new LootTableFunction(3,0.4)));
        lootEntry.add(new LootTableEntry(12,"bone",new LootTableFunction(3,0.4)));
        lootEntry.add(new LootTableEntry(5,"book"));
        lootEntry.add(new LootTableEntry(5,"redstone",new LootTableFunction(16)));
        lootEntry.add(new LootTableEntry(5,"gunpowder",new LootTableFunction(16)));
        lootEntry.add(new LootTableEntry(3,"glowstone_dust",new LootTableFunction(6)));
        lootEntry.add(new LootTableEntry(10,"fishing_rod"));
        lootEntry.add(new LootTableEntry(8,"obsidian",new LootTableFunction(4)));
        lootEntry.add(new LootTableEntry(4,"lava_bucket"));
        lootEntry.add(new LootTableEntry(2,"golden_apple"));
        lootEntry.add(new LootTableEntry(20,"stick",new LootTableFunction(8)));
        lootEntry.add(new LootTableEntry(5,"gold_ingot",new LootTableFunction(3,0.3)));
        lootEntry.add(new LootTableEntry(15,"arrow",new LootTableFunction(4)));
        lootEntry.add(new LootTableEntry(4,"apple",new LootTableFunction(2,0.3)));
        lootEntry.add(new LootTableEntry(2,"anvil"));
        lootEntry.add(new LootTableEntry(18,"diorite",new LootTableFunction(16)));
        lootEntry.add(new LootTableEntry(2,"cobweb",new LootTableFunction(2,0.4)));
        lootEntry.add(new LootTableEntry(4,"diamond",new LootTableFunction(2,0.3)));
        lootEntry.add(new LootTableEntry(12,"iron_ingot",new LootTableFunction(2,0.5)));
        lootEntry.add(new LootTableEntry(1,"diamond_chestplate"));
        lootEntry.add(new LootTableEntry(1,"diamond_leggings"));
        lootEntry.add(new LootTableEntry(1,"netherite_scrap",new LootTableFunction(4,0.001)));
        lootEntry.add(new LootTableEntry(2,"spyglass"));
        lootEntry.add(new LootTableEntry(18,"amethyst_block",new LootTableFunction(16)));
        lootEntry.add(new LootTableEntry(15,"copper_block",new LootTableFunction(16)));

        ArrayList<String> fileCommands = new ArrayList<>();

        fileCommands.add("{");
        fileCommands.add("  \"type\": \"minecraft:chest\",");
        fileCommands.add("  \"pools\": [");
        fileCommands.add("    {");
        fileCommands.add("      \"rolls\": {");
        fileCommands.add("        \"min\": 3,");
        fileCommands.add("        \"max\": 5");
        fileCommands.add("      },");
        fileCommands.add("      \"bonus_rolls\": 5,");
        fileCommands.add("      \"entries\": [");

        int counter = 1;
        int totalWeight = 0;
        for (LootTableEntry l : lootEntry)
        {
            fileCommands.add("        {");
            fileCommands.add("          \"type\": \"minecraft:item\",");
            fileCommands.add("          \"weight\": " + l.getWeight() + ",");
            if (l.getFunction() != null) {
                fileCommands.add("          \"name\": \"minecraft:" + l.getName() + "\",");
                fileCommands.add("          \"functions\": [");
                fileCommands.add("            {");
                fileCommands.add("              \"function\": \"minecraft:set_count\",");
                if (l.getFunction().getChance() > 0) {
                    fileCommands.add("              \"count\": " + l.getFunction().getCount() + ",");
                    fileCommands.add("              \"conditions\": [");
                    fileCommands.add("                {");
                    fileCommands.add("                  \"condition\": \"minecraft:random_chance\",");
                    fileCommands.add("                  \"chance\": " + l.getFunction().getChance());
                    fileCommands.add("                }");
                    fileCommands.add("              ]");
                }
                else
                {
                    fileCommands.add("              \"count\": " + l.getFunction().getCount());
                }
                fileCommands.add("            }");
                fileCommands.add("          ]");
            }
            else
            {
                fileCommands.add("          \"name\": \"minecraft:" + l.getName() + "\"");
            }
            if (counter < lootEntry.size()) {
                fileCommands.add("        },");
                counter++;
            }
            else {
                fileCommands.add("        }");
            }
            totalWeight += l.getWeight();
        }

        fileCommands.add("      ]");
        fileCommands.add("    }");
        fileCommands.add("  ]");
        fileCommands.add("}");
        //fileCommands.add("#Total weight = "+ totalWeight);

        FileData fileData = new FileData("supply_drop", fileCommands, "loot_tables");
        files.add(fileData);
    }

    private void makeRecipeFiles() {
        ArrayList<Recipe> recipes = new ArrayList<>();

        String[] grid = {" ", " ", " ", "1", "2", "1", " ", " ", " "};
        ArrayList<String> keys = new ArrayList<>();
        keys.add("ender_eye");
        keys.add("black_wool");
        Recipe recipe = new Recipe("crafting_shaped", grid, keys, "dragon_head", 1);
        recipes.add(recipe);

        /*
        String[] grid2 = {" ", "1", " ", "1", "2", "1", " ", "1", " "};
        ArrayList<String> keys2 = new ArrayList<>();
        keys2.add("gold_ingot");
        keys2.add("player_head");
        Recipe recipe2 = new Recipe("crafting_shaped", grid2, keys2, "golden_apple", 1);
        recipes.add(recipe2);
        */

        for (Recipe r : recipes) {
            ArrayList<String> fileCommands = new ArrayList<>();
            fileCommands.add(" {");
            fileCommands.add("        \"type\": \"" + r.type + "\",");
            fileCommands.add("        \"pattern\": [");
            fileCommands.add("            \"" + r.grid[0] + r.grid[1] + r.grid[2] + "\",");
            fileCommands.add("            \"" + r.grid[3] + r.grid[4] + r.grid[5] + "\",");
            fileCommands.add("            \"" + r.grid[6] + r.grid[7] + r.grid[8] + "\"");
            fileCommands.add("        ],");
            fileCommands.add("        \"key\": {");
            int counter = 1;
            for (String key : r.keys) {
                fileCommands.add("            \"" + counter + "\": {");
                fileCommands.add("                \"item\": \"" + key + "\"");
                if (r.keys.size() == counter) {
                    fileCommands.add("            }");
                } else {
                    fileCommands.add("            },");
                }
                counter++;
            }
            fileCommands.add("        },");
            fileCommands.add("        \"result\": {");
            fileCommands.add("            \"item\": \"" + r.resultItem + "\",");
            fileCommands.add("            \"count\": " + r.resultAmount);
            fileCommands.add("        }");
            fileCommands.add("    }");

            FileData fileData = new FileData(r.resultItem + r.resultAmount, fileCommands, "recipe");
            files.add(fileData);
        }
    }

    private void makeFunctionFiles() {
        ControlPoint cp1 = controlPoints.get(0);
        ControlPoint cp2 = controlPoints.get(1);

        ArrayList<String> fileCommands = new ArrayList<>();
        fileCommands.add("gamerule naturalRegeneration false");
        fileCommands.add("gamerule doImmediateRespawn true");
        fileCommands.add("gamerule doPatrolSpawning false");
        fileCommands.add("gamerule doMobSpawning false");
        fileCommands.add("gamerule doWeatherCycle false");
        fileCommands.add("difficulty hard");
        fileCommands.add("defaultgamemode adventure");
        fileCommands.add("setworldspawn 0 151 3");

        //scoreboard
        for (ScoreboardObjective objective : scoreboardObjectives) {
            fileCommands.add("scoreboard objectives add " + objective.getName() + " " + objective.getType() + " " + objective.getCustomName());
        }
        fileCommands.add("scoreboard objectives setdisplay belowName Hearts");
        fileCommands.add("scoreboard objectives setdisplay list Hearts");
        //end scoreboard
        //bossbar

        fileCommands.add("bossbar remove minecraft:cp1");
        fileCommands.add("bossbar remove minecraft:cp2");
        fileCommands.add("bossbar add minecraft:cp1 \"" + cp1.getName() + ": " + cp1.getX() + ", " + cp1.getY() + ", " + cp1.getZ() + "\"");
        fileCommands.add("bossbar set minecraft:cp1 max " + cp1.getMaxVal());
        fileCommands.add("bossbar add minecraft:cp2 \"" + cp2.getName() + " soon: " + cp2.getX() + ", " + cp2.getY() + ", " + cp2.getZ() + "\"");
        fileCommands.add("bossbar set minecraft:cp2 max " + cp2.getMaxVal());
        fileCommands.add("bossbar add minecraft:carepackage \"Care Package available at x, y, z\"");
        //end bossbar
        //teams
        for (Team t : teams) {
            fileCommands.add("team add " + t.getName());
            fileCommands.add("team modify " + t.getName() + " color " + t.getColor());
            for (int i = 1; i < controlPoints.size() + 1; i++) {
                fileCommands.add("scoreboard objectives add CP" + i + t.getName() + " dummy");
            }
        }
        //end teams
        //structure
        fileCommands.add("fill -1 " + (worldBottom + 5) + " -1 1 150 1 minecraft:barrier");
        fileCommands.add("fill 0 " + (worldBottom + 5) +" 0 0 150 0 minecraft:air");
        fileCommands.add("fill -6 150 -6 6 156 6 minecraft:barrier");
        fileCommands.add("fill -5 151 -5 5 156 5 minecraft:air");
        fileCommands.add("setblock 0 150 0 minecraft:air");
        fileCommands.add("setblock -2 " + worldBottom + " -2 minecraft:structure_block[mode=load]{metadata:\"\",mirror:\"NONE\",ignoreEnti" +
                "ties:0b,powered:0b,seed:0L,author:\"?\",rotation:\"NONE\",posX:1,mode:\"LOAD\",posY:1,sizeX:18,posZ:1," +
                "integrity:1.0f,showair:0b,name:\"minecraft:commandcenter_" + commandCenter + "\",sizeY:31,sizeZ:18,showboundingbox:1b}");
        fileCommands.add("setblock -2 " + (worldBottom + 1) + " -2 minecraft:redstone_block");
        fileCommands.add("fill 0 " + (worldBottom + 5) + " 1 0 " + (worldBottom + 6) + " 1 minecraft:air");
        //end structure
        //end structure
        //

        FileData file = new FileData("initialize", fileCommands);
        files.add(file);

        ArrayList<String> fileCommands2 = new ArrayList<>();
        fileCommands2.add("execute if entity @p[scores={Deaths=1}] run playsound minecraft:entity.lightning_bolt.thunder master @a ~ ~50 ~ 100 1 0");
        fileCommands2.add("gamemode spectator @a[scores={Deaths=1},gamemode=!spectator]");
        fileCommands2.add("scoreboard players set @a[scores={Deaths=1}] ControlPoint1 0");
        fileCommands2.add("scoreboard players set @a[scores={Deaths=1}] ControlPoint2 0");
        fileCommands2.add("scoreboard players set @p[scores={Admin=1}] Highscore1 1");
        fileCommands2.add("scoreboard players set @p[scores={Admin=1}] Highscore2 1");

        fileCommands2.add("execute if entity @p[scores={Deaths=1},tag=Traitor] run tellraw @a [\"\",{\"text\":\" | \",\"bold\":true,\"color\":\"dark_gray\"},{\"text\":\"A TRAITOR HAS BEEN ELIMINATED\",\"bold\":true,\"color\":\"red\"},{\"text\":\" | \",\"bold\":true,\"color\":\"dark_gray\"},{\"text\":\"WELL DONE\",\"bold\":true,\"color\":\"gold\"},{\"text\":\" | \",\"bold\":true,\"color\":\"dark_gray\"}]");
        /*for (String playerName : playerNames) {
            fileCommands2.add("execute at @p[name=" + playerName + ",scores={Deaths=1}] run summon minecraft:item ~ ~ ~ {Item:{id:player_head,Count:1,tag:{SkullOwner:" + playerName + "}}}");
        }*/
        for (Player p : players) {
            fileCommands2.add("execute at @p[name=" + p.getPlayerName() + ",scores={Deaths=1}] run summon minecraft:item ~ ~ ~ {Item:{id:player_head,Count:1,tag:{SkullOwner:" + p.getPlayerName() + "}}}");
        }
        fileCommands2.add("scoreboard players reset @a[scores={Deaths=1}] Deaths");

        FileData file2 = new FileData("drop_player_heads", fileCommands2);
        files.add(file2);

//        ArrayList<String> fileCommands3 = new ArrayList<>();
//        for (Team t : teams) {
//            fileCommands3.add("execute if score @r[limit=1,gamemode=!spectator,team=" + t.getName() + "] ControlPoint > @p[scores={Admin=1}] Highscore run bossbar set minecraft:cp color " + t.getBossbarColor());
//        }
//        fileCommands3.add("execute as @r[limit=1,gamemode=!spectator] run scoreboard players operation @p[scores={Admin=1}] Highscore > @s ControlPoint");
//        fileCommands3.add("execute store result bossbar minecraft:cp value run scoreboard players get @p[scores={Admin=1}] Highscore");
//
//        FileData file3 = new FileData("bbvalue", fileCommands3);
//        files.add(file3);

        ArrayList<String> fileCommands3 = new ArrayList<>();
        for (Team t : teams) {
            fileCommands3.add("execute if score @r[limit=1,gamemode=!spectator,team=" + t.getName() + "] ControlPoint1 > @p[scores={Admin=1}] Highscore1 run bossbar set minecraft:cp1 color " + t.getBossbarColor());
            fileCommands3.add("execute if score @r[limit=1,gamemode=!spectator,team=" + t.getName() + "] ControlPoint2 > @p[scores={Admin=1}] Highscore2 run bossbar set minecraft:cp2 color " + t.getBossbarColor());
        }
        fileCommands3.add("execute as @r[limit=1,gamemode=!spectator] run scoreboard players operation @p[scores={Admin=1}] Highscore1 > @s ControlPoint1");
        fileCommands3.add("execute store result bossbar minecraft:cp1 value run scoreboard players get @p[scores={Admin=1}] Highscore1");
        fileCommands3.add("execute as @r[limit=1,gamemode=!spectator] run scoreboard players operation @p[scores={Admin=1}] Highscore2 > @s ControlPoint2");
        fileCommands3.add("execute store result bossbar minecraft:cp2 value run scoreboard players get @p[scores={Admin=1}] Highscore2");

        FileData file3 = new FileData("bbvalue", fileCommands3);
        files.add(file3);

        ArrayList<String> fileCommands4 = new ArrayList<>();
        for (int i = 0; i < chestSize; i++) {
            fileCommands4.add("item replace entity @a enderchest." + i + " with air 1");
        }

        FileData file4 = new FileData("clear_enderchest", fileCommands4);
        files.add(file4);

        ArrayList<String> fileCommands5 = new ArrayList<>();
        fileCommands5.add("item replace entity @a armor.chest with minecraft:iron_chestplate");
        fileCommands5.add("item replace entity @a armor.feet with minecraft:iron_boots");
        fileCommands5.add("item replace entity @a armor.head with minecraft:iron_helmet");
        fileCommands5.add("item replace entity @a armor.legs with minecraft:iron_leggings");
        fileCommands5.add("item replace entity @a weapon.offhand with minecraft:shield");
        fileCommands5.add("item replace entity @a weapon.mainhand with minecraft:iron_axe");
        fileCommands5.add("item replace entity @a inventory.0 with minecraft:iron_sword");
        fileCommands5.add("effect give @a minecraft:regeneration 1 255 true");

        FileData file5 = new FileData("equip_gear", fileCommands5);
        files.add(file5);

        ArrayList<String> fileCommands6 = new ArrayList<>();
        fileCommands6.add("effect give @s minecraft:resistance 99999 4 true");
        fileCommands6.add("item replace entity @s weapon.mainhand with trident{display:{Name:\"{\\\"text\\\":\\\"The Impaler\\\"}\"}, Enchantments:[{id:sharpness,lvl:999999},{id:fire_aspect,lvl:999999},{id:unbreaking,lvl:999999},{id:loyalty,lvl:999999},{id:impaling,lvl:999999}]}");

        FileData file6 = new FileData("god_mode", fileCommands6);
        files.add(file6);

        ArrayList<String> fileCommands7 = new ArrayList<>();
        fileCommands7.add("gamerule commandBlockOutput true");
        fileCommands7.add("gamerule doDaylightCycle false");
        fileCommands7.add("gamerule keepInventory true");
        fileCommands7.add("gamerule doMobSpawning false");
        fileCommands7.add("gamerule doTileDrops false");
        fileCommands7.add("gamerule drowningDamage false");
        fileCommands7.add("gamerule fallDamage false");
        fileCommands7.add("gamerule fireDamage false");
        fileCommands7.add("gamerule sendCommandFeedback true");
        fileCommands7.add("gamerule doImmediateRespawn true");

        fileCommands7.add("scoreboard players reset @a");
        fileCommands7.add("fill 0 " + (worldBottom + 2) + " 15 0 " + (worldBottom + 2) + " 2 minecraft:bedrock replace");
        fileCommands7.add("fill 2 " + (worldBottom + 2) + " 0 8 " + (worldBottom + 2) + " 0 minecraft:bedrock replace");
        fileCommands7.add("fill 15 " + (worldBottom + 2) + " 3 15 " + (worldBottom + 2) + " 11 minecraft:bedrock replace");
        fileCommands7.add("scoreboard players set " + admin + " Admin 1");
        fileCommands7.add("fill 15 " + (worldBottom + 2) + " 15 9 " + (worldBottom + 2) + " 15 minecraft:redstone_block replace");
        fileCommands7.add("setblock 11 " + (worldBottom + 2) + " 0 minecraft:bedrock destroy");
        fileCommands7.add("setblock 10 " + (worldBottom + 2) + " 0 minecraft:bedrock destroy");
        fileCommands7.add("bossbar set minecraft:cp name \"" + cp1.getName() + ": " + cp1.getX() + ", " + cp1.getY() + ", " + cp1.getZ() + "; " + cp2.getName() + " soon: " + cp2.getX() + ", " + cp2.getY() + ", " + cp2.getZ() + "\"");
        fileCommands7.add("forceload add " + cp1.getX() + " " + cp1.getZ() + " " + cp1.getX() + " " + cp1.getZ());
        fileCommands7.add("forceload add " + cp2.getX() + " " + cp2.getZ() + " " + cp2.getX() + " " + cp2.getZ());
        fileCommands7.add("function uhc:spawn_controlpoints");
        fileCommands7.add("forceload remove " + cp1.getX() + " " + cp1.getZ() + " " + cp1.getX() + " " + cp1.getZ());
        fileCommands7.add("forceload remove " + cp2.getX() + " " + cp2.getZ() + " " + cp2.getX() + " " + cp2.getZ());
        fileCommands7.add("bossbar set minecraft:cp1 color white");
        fileCommands7.add("bossbar set minecraft:cp1 visible false");
        fileCommands7.add("bossbar set minecraft:cp1 players @a");
        fileCommands7.add("bossbar set minecraft:cp2 color white");
        fileCommands7.add("bossbar set minecraft:cp2 visible false");
        fileCommands7.add("bossbar set minecraft:cp2 players @a");
        fileCommands7.add("bossbar set minecraft:cp2 name \"" + cp2.getName() + " soon: " + cp2.getX() + ", " + cp2.getY() + ", " + cp2.getZ() + "\"");
        fileCommands7.add("bossbar set minecraft:carepackage players @a");
        fileCommands7.add("bossbar set minecraft:carepackage visible false");
        fileCommands7.add("tag @a remove Traitor");
        fileCommands7.add("tag @a remove DontMakeTraitor");
        fileCommands7.add("worldborder set " + worldSize + " 1");
        fileCommands7.add("team leave @a");
        fileCommands7.add("function uhc:display_rank");
        fileCommands7.add("scoreboard players set NightTime Time 600");
        fileCommands7.add("scoreboard players set CarePackages Time 1200");
        fileCommands7.add("scoreboard players set ControlPoints Time 1800");
        fileCommands7.add("scoreboard players set TraitorFaction Time 2400");

        for (CarePackage carepackage : carePackages) {
            fileCommands7.add("forceload add " + carepackage.getX() + " " + carepackage.getZ() + " " + carepackage.getX() + " " + carepackage.getZ());
            fileCommands7.add("setblock " + carepackage.getX() + " " + carepackage.getY() + " " + carepackage.getZ() + " minecraft:air replace");
            fileCommands7.add("forceload remove " + carepackage.getX() + " " + carepackage.getZ() + " " + carepackage.getX() + " " + carepackage.getZ());
        }

        fileCommands7.add("gamemode creative @s");

        FileData file7 = new FileData("developer_mode", fileCommands7);
        files.add(file7);

        for (int i = 1; i < 9; i++) {
            ArrayList<String> fileCommands8 = new ArrayList<>();
            for (Team t : teams) {
                fileCommands8.add("team join " + t.getName() + " @r[limit=" + i + ",team=]");
            }

            FileData file8 = new FileData("random_teams" + i, fileCommands8);
            files.add(file8);

        }

        ArrayList<String> fileCommands9 = new ArrayList<>();
        fileCommands9.add("effect clear @a");
        fileCommands9.add("tp @a 0 -100 0");
        fileCommands9.add("tellraw @a [\"\",{\"text\":\" ｜ \",\"color\":\"gray\"},{\"text\":\"" + communityName +
                " UHC\",\"color\":\"gold\"},{\"text\":\" ｜ \",\"color\":\"gray\"},{\"text\":\"PREDICTIONS COMPLETED" +
                "\",\"color\":\"light_purple\"},{\"text\":\" ｜ \",\"color\":\"gray\"}]");

        FileData file9 = new FileData("predictions", fileCommands9);
        files.add(file9);

        ArrayList<String> fileCommands10 = new ArrayList<>();
        fileCommands10.add("tp @a " + startCoordinates);
        fileCommands10.add("scoreboard players reset @a Deaths");
        fileCommands10.add("scoreboard players reset @a Kills");

        FileData file10 = new FileData("into_calls", fileCommands10);
        files.add(file10);

        ArrayList<String> fileCommands11 = new ArrayList<>();
        fileCommands11.add("spreadplayers 0 0 300 700 true @a");
        fileCommands11.add("scoreboard players set @p[scores={Admin=1}] Highscore 1");

        FileData file11 = new FileData("spread_players", fileCommands11);
        files.add(file11);

        ArrayList<String> fileCommands12 = new ArrayList<>();
        fileCommands12.add("gamerule commandBlockOutput false");
        fileCommands12.add("gamerule doDaylightCycle true");
        fileCommands12.add("gamerule keepInventory false");
        fileCommands12.add("gamerule doMobSpawning true");
        fileCommands12.add("gamerule doTileDrops true");
        fileCommands12.add("gamerule drowningDamage true");
        fileCommands12.add("gamerule fallDamage true");
        fileCommands12.add("gamerule fireDamage true");
        fileCommands12.add("gamerule doImmediateRespawn false");
        fileCommands12.add("function uhc:clear_enderchest");
        fileCommands12.add("recipe give @a uhc:golden_apple");
        fileCommands12.add("recipe take @a uhc:dragon_head");

        FileData file12 = new FileData("survival_mode", fileCommands12);
        files.add(file12);

        ArrayList<String> fileCommands13 = new ArrayList<>();
        fileCommands13.add("time set 0");
        fileCommands13.add("xp set @a 0 levels");
        fileCommands13.add("effect give @a minecraft:regeneration 1 255");
        fileCommands13.add("effect give @a minecraft:saturation 1 255");
        fileCommands13.add("clear @a");
        fileCommands13.add("title @a title {\"text\":\"Game Starting Now!\", \"bold\":true, \"italic\":true, \"color\":\"gold\"}");
        fileCommands13.add("gamemode survival @a");
        fileCommands13.add("gamerule sendCommandFeedback false");
        fileCommands13.add("fill 0 " + (worldBottom + 2) + " 15 0 " + (worldBottom + 2) + " 2 minecraft:redstone_block replace");
        fileCommands13.add("fill 2 " + (worldBottom + 2) + " 0 6 " + (worldBottom + 2) + " 0 minecraft:redstone_block replace");
        fileCommands13.add("fill 15 " + (worldBottom + 2) + " 15 9 " + (worldBottom + 2) + " 15 minecraft:bedrock");
        fileCommands13.add("setblock 10 " + (worldBottom + 2) + " 0 minecraft:redstone_block destroy");
        fileCommands13.add("advancement revoke @a everything");
        fileCommands13.add("xp set @a 0 points");
        //fileCommands13.add("execute as @a at @s run function uhc:give_instructions");

        FileData file13 = new FileData("start_game", fileCommands13);
        files.add(file13);

        ArrayList<String> fileCommands14 = new ArrayList<>();
        fileCommands14.add("execute positioned 0 151 0 run gamemode survival @a[distance=..20,gamemode=!creative]");
        fileCommands14.add("execute positioned 0 151 0 run spreadplayers 0 0 300 700 true @a[distance=..20,gamemode=survival]");

        FileData file14 = new FileData("battle_royale", fileCommands14);
        files.add(file14);

//        ArrayList<String> fileCommands15 = new ArrayList<>();
//        fileCommands15.add("title @a subtitle {\"text\":\"is now enabled!\", \"bold\":true, \"italic\":true, \"color\":\"light_purple\"}");
//        fileCommands15.add("setblock 7 2 0 minecraft:redstone_block replace");
//        fileCommands15.add("setblock 15 2 3 minecraft:redstone_block replace");
//        fileCommands15.add("title @a title {\"text\":\"Control Point 1\", \"bold\":true, \"italic\":true, \"color\":\"gold\"}");
//        fileCommands15.add("bossbar set minecraft:cp visible true");
//        fileCommands15.add("forceload add " + cp1.getX() + " " + cp1.getZ() + " " + cp1.getX() + " " + cp1.getZ());
//        fileCommands15.add("summon minecraft:end_crystal " + cp1.getX() + " " + cp1.getY() + " " + cp1.getZ() + " {ShowBottom: 0b,Invulnerable: 1b, BeamTarget:{X:" + cp1.getX() + ",Y:" + (cp1.getY() + 300) + ",Z:" + cp1.getZ() + "}}");
//        fileCommands15.add("execute positioned " + cp1.getX() + " " + cp1.getY() + " " + cp1.getZ() + " run scoreboard players set @e[type=minecraft:end_crystal,distance=..5] Crystal 1");
//        fileCommands15.add("execute positioned as @e[type=minecraft:end_crystal] run tp @e[type=minecraft:end_crystal] ~ ~-200 ~");
//        fileCommands15.add("forceload remove " + cp1.getX() + " " + cp1.getZ() + " " + cp1.getX() + " " + cp1.getZ());
//        fileCommands15.add("setblock 6 2 0 minecraft:bedrock replace");
//        fileCommands15.add("setblock 15 2 7 minecraft:redstone_block replace");
//        fileCommands15.add("setblock 15 2 6 minecraft:redstone_block replace");
//        fileCommands15.add("setblock 15 2 10 minecraft:redstone_block replace");
//        fileCommands15.add("gamerule doDaylightCycle false");
//
//        FileData file15 = new FileData("initialize_controlpoint", fileCommands15);
//        files.add(file15);

        ArrayList<String> fileCommands15 = new ArrayList<>();
        fileCommands15.add("title @a subtitle {\"text\":\"is now enabled!\", \"bold\":true, \"italic\":true, \"color\":\"light_purple\"}");
        fileCommands15.add("setblock 7 " + (worldBottom + 2) + " 0 minecraft:redstone_block replace");
        fileCommands15.add("title @a title {\"text\":\"Control Point 1\", \"bold\":true, \"italic\":true, \"color\":\"gold\"}");
        fileCommands15.add("bossbar set minecraft:cp1 visible true");
        fileCommands15.add("bossbar set minecraft:cp2 visible true");
        fileCommands15.add("setblock 6 " + (worldBottom + 2) + " 0 minecraft:bedrock replace");
        fileCommands15.add("forceload add " + cp1.getX() + " " + cp1.getZ() + " " + cp1.getX() + " " + cp1.getZ());
        fileCommands15.add("setblock " + cp1.getX() + " " + (cp1.getY() + 3) + " " + cp1.getZ() + " minecraft:air replace");
        fileCommands15.add("forceload remove " + cp1.getX() + " " + cp1.getZ() + " " + cp1.getX() + " " + cp1.getZ());
        fileCommands15.add("setblock 15 " + (worldBottom + 2) + " 7 minecraft:redstone_block replace");
        fileCommands15.add("setblock 15 " + (worldBottom + 2) + " 6 minecraft:redstone_block replace");
        fileCommands15.add("setblock 15 " + (worldBottom + 2) + " 10 minecraft:redstone_block replace");
        fileCommands15.add("gamerule doDaylightCycle false");

        FileData file15 = new FileData("initialize_controlpoint", fileCommands15);
        files.add(file15);


//        ArrayList<String> fileCommands16 = new ArrayList<>();
//        fileCommands16.add("setblock 7 2 0 minecraft:bedrock replace");
//        fileCommands16.add("setblock 8 2 0 minecraft:redstone_block replace");
//
//        fileCommands16.add("tellraw @a [\"\",{\"text\":\" ⎜ \",\"color\":\"gray\"},{\"text\":\"THE DIORITE EXPERTS UHC\",\"color\":\"gold\"},{\"text\":\" ⎜ \",\"color\":\"gray\"},{\"text\":\"CONTROL POINT 2 IS NOW AVAILABLE! ETERNAL DAY ENABLED!\",\"color\":\"light_purple\"},{\"text\":\" ⎜ \",\"color\":\"gray\"}]");
//        fileCommands16.add("forceload add " + cp2.getX() + " " + cp2.getZ() + " " + cp2.getX() + " " + cp2.getZ());
//        fileCommands16.add("summon minecraft:end_crystal " + cp2.getX() + " " + cp2.getY() + " " + cp2.getZ() + " {ShowBottom: 0b,Invulnerable: 1b, BeamTarget:{X:" + cp2.getX() + ",Y:" + (cp2.getY() + 300) + ",Z:" + cp2.getZ() + "}}");
//        fileCommands16.add("execute positioned " + cp2.getX() + " " + cp2.getY() + " " + cp2.getZ() + " run scoreboard players set @e[type=minecraft:end_crystal,distance=..5] Crystal 2");
//        fileCommands16.add("execute positioned as @e[type=minecraft:end_crystal,scores={Crystal=2}] run tp @e[type=minecraft:end_crystal,scores={Crystal=2}] ~ ~-200 ~");
//        fileCommands16.add("forceload remove " + cp2.getX() + " " + cp2.getZ() + " " + cp2.getX() + " " + cp2.getZ());
//        fileCommands16.add("setblock 15 2 11 minecraft:redstone_block replace");
//        fileCommands16.add("bossbar set minecraft:cp name \"CP1: " + cp1.getX() + ", " + cp1.getY() + ", " + cp1.getZ() + "; CP2: " + cp2.getX() + ", " + cp2.getY() + ", " + cp2.getZ() + " (faster capping!)\"");
//        fileCommands16.add("give @a[scores={ControlPoint=14400..}] minecraft:splash_potion{CustomPotionEffects:[{Id:11,Duration:1200},{Id:24,Duration:1200}],CustomPotionColor:15462415,display:{Name:\"\\\"Hero of the First Control Point\\\"\",Lore:[\"Thank you for enabling the second Control Point! Good luck with winning the match!\"]}}");
//        fileCommands16.add("function uhc:carepackage_" + carePackages.get(1).getName());
//
//
//        FileData file16 = new FileData("second_controlpoint", fileCommands16);
//        files.add(file16);

        ArrayList<String> fileCommands16 = new ArrayList<>();
        fileCommands16.add("setblock 7 " + (worldBottom + 2) + " 0 minecraft:bedrock replace");
        fileCommands16.add("setblock 8 " + (worldBottom + 2) + " 0 minecraft:redstone_block replace");
        fileCommands16.add("tellraw @a [\"\",{\"text\":\" ⎜ \",\"color\":\"gray\"},{\"text\":\"" + communityName + " UHC\",\"color\":\"gold\"},{\"text\":\" ⎜ \",\"color\":\"gray\"},{\"text\":\"CONTROL POINT 2 IS NOW AVAILABLE! ETERNAL DAY ENABLED!\",\"color\":\"light_purple\"},{\"text\":\" ⎜ \",\"color\":\"gray\"}]");
        fileCommands16.add("setblock 15 " + (worldBottom + 2) + " 11 minecraft:redstone_block replace");
        fileCommands16.add("forceload add " + cp2.getX() + " " + cp2.getZ() + " " + cp2.getX() + " " + cp2.getZ());
        fileCommands16.add("setblock " + cp2.getX() + " " + (cp2.getY() + 3) + " " + cp2.getZ() + " minecraft:air replace");
        fileCommands16.add("forceload remove " + cp2.getX() + " " + cp2.getZ() + " " + cp2.getX() + " " + cp2.getZ());
        fileCommands16.add("bossbar set minecraft:cp2 name \"CP2: " + cp2.getX() + ", " + cp2.getY() + ", " + cp2.getZ() + " (faster capping!)\"");
        fileCommands16.add("give @a[scores={ControlPoint1=14400..}] minecraft:splash_potion{CustomPotionEffects:[{Id:11,Duration:1200},{Id:24,Duration:1200}],CustomPotionColor:15462415,display:{Name:\"\\\"Hero of the First Control Point\\\"\",Lore:[\"Thank you for enabling the second Control Point! Good luck with winning the match!\"]}}");
        fileCommands16.add("function uhc:carepackage_" + carePackages.get(0).getName());


        FileData file16 = new FileData("second_controlpoint", fileCommands16);
        files.add(file16);

        ArrayList<String> fileCommands17 = new ArrayList<>();
        fileCommands17.add("setblock 8 " + (worldBottom + 2) + " 0 minecraft:bedrock replace");
        fileCommands17.add("tellraw @a [\"\",{\"text\":\" ⎜ \",\"color\":\"gray\"},{\"text\":\"" + communityName + " UHC\",\"color\":\"gold\"},{\"text\":\" ⎜ \",\"color\":\"gray\"},{\"text\":\"TIME VICTORY HAS BEEN ACHIEVED! 3 MINUTES UNTIL THE FINAL DEATHMATCH\",\"color\":\"light_purple\"},{\"text\":\" ⎜ \",\"color\":\"gray\"}]");
        fileCommands17.add("fill 15 " + (worldBottom + 2) + " 3 15 " + (worldBottom + 2) + " 4 minecraft:bedrock");
        fileCommands17.add("title @a subtitle {\"text\":\"has been achieved!\", \"bold\":true, \"italic\":true, \"color\":\"light_purple\"}");
        fileCommands17.add("title @a title {\"text\":\"Time Victory\", \"bold\":true, \"italic\":true, \"color\":\"gold\"}");
        fileCommands17.add("schedule function uhc:death_match 3600t");

        FileData file17 = new FileData("victory", fileCommands17);
        files.add(file17);

        ArrayList<String> fileCommands18 = new ArrayList<>();
        fileCommands18.add("worldborder set 400");
        fileCommands18.add("worldborder set 1 180");
        fileCommands18.add("execute in minecraft:overworld run tp @a[gamemode=!spectator] 3 153 3");
        fileCommands18.add("spreadplayers 0 0 75 150 true @a[gamemode=!spectator]");
        fileCommands18.add("bossbar set minecraft:cp visible false");

        FileData file18 = new FileData("death_match", fileCommands18);
        files.add(file18);

//        for (int i = 1; i < controlPoints.size()+1; i++) {
//            ArrayList<String> fileCommands19 = new ArrayList<>();
//            for (Team team : teams) {
//                fileCommands19.add("execute as @a[gamemode=!spectator,team=" + team.getName() + "] at @s positioned ~ ~-200 ~ if entity @e[type=minecraft:end_crystal,scores={Crystal=" + i + "}, distance=..5] at @s unless entity @a[gamemode=!spectator,distance=..10,team=!" + team.getName() + "] run scoreboard players add @s ControlPoint " + controlPoints.get(i - 1).getAddRate());
//            }
//
//            FileData file19 = new FileData("controlpoint_" + i, fileCommands19);
//            files.add(file19);
//        }

        for (int i = 1; i < controlPoints.size() + 1; i++) {
            ArrayList<String> fileCommands19 = new ArrayList<>();
            for (Team team : teams) {
                fileCommands19.add("execute as @a[gamemode=!spectator,team=" + team.getName() + "] if entity @a[gamemode=!spectator,x=" + (controlPoints.get(i - 1).getX() - 6) + ",y=" + (controlPoints.get(i - 1).getY() - 1) + ",z=" + (controlPoints.get(i - 1).getZ() - 6) + ",dx=12,dy=12,dz=12] at @s unless entity @a[gamemode=!spectator,x=" + (controlPoints.get(i - 1).getX() - 6) + ",y=" + (controlPoints.get(i - 1).getY() - 1) + ",z=" + (controlPoints.get(i - 1).getZ() - 6) + ",dx=12,dy=12,dz=12,team=!" + team.getName() + "] run scoreboard players add @s ControlPoint" + i + " " + controlPoints.get(i - 1).getAddRate());
                fileCommands19.add("execute if score @r[limit=1,gamemode=!spectator,team=" + team.getName() + "] ControlPoint" + i + " > @p[scores={Admin=1}] Highscore" + i + " run setblock " + controlPoints.get(i - 1).getX() + " " + (controlPoints.get(i - 1).getY() + 1) + " " + controlPoints.get(i - 1).getZ() + " minecraft:" + team.getGlassColor() + "_stained_glass replace");
            }
            fileCommands19.add("execute if entity @p[x=" + (controlPoints.get(i - 1).getX() - 6) + ",y=" + (controlPoints.get(i - 1).getY() - 1) + ",z=" + (controlPoints.get(i - 1).getZ() - 6) + ",dx=12,dy=12,dz=12,gamemode=!spectator] run scoreboard players add @a[x=" + (controlPoints.get(i - 1).getX() - 6) + ",y=" + (controlPoints.get(i - 1).getY() - 1) + ",z=" + (controlPoints.get(i - 1).getZ() - 6) + ",dx=12,dy=12,dz=12,gamemode=!spectator] MSGDum1CP" + i + " 1");
            fileCommands19.add("execute if entity @p[x=" + (controlPoints.get(i - 1).getX() - 6) + ",y=" + (controlPoints.get(i - 1).getY() - 1) + ",z=" + (controlPoints.get(i - 1).getZ() - 6) + ",dx=12,dy=12,dz=12,gamemode=!spectator,scores={MSGDum1CP" + i + "=200}] run tellraw @a [\"\",{\"text\":\" ⎜ \",\"color\":\"gray\"},{\"text\":\"" + communityName + " UHC\",\"color\":\"gold\"},{\"text\":\" ⎜ \",\"color\":\"gray\"},{\"text\":\"CONTROL POINT " + i + " IS UNDER ATTACK!\",\"color\":\"light_purple\"},{\"text\":\" ⎜ \",\"color\":\"gray\"}]");
            fileCommands19.add("execute if entity @p[x=" + (controlPoints.get(i - 1).getX() - 6) + ",y=" + (controlPoints.get(i - 1).getY() - 1) + ",z=" + (controlPoints.get(i - 1).getZ() - 6) + ",dx=12,dy=12,dz=12,gamemode=!spectator,scores={MSGDum1CP" + i + "=200}] run scoreboard players reset @a[x=" + (controlPoints.get(i - 1).getX() - 6) + ",y=" + (controlPoints.get(i - 1).getY() - 1) + ",z=" + (controlPoints.get(i - 1).getZ() - 6) + ",dx=12,dy=12,dz=12,gamemode=!spectator] MSGDum2CP" + i);

            fileCommands19.add("execute positioned " + controlPoints.get(i - 1).getX() + " " + (controlPoints.get(i - 1).getY() + 5) + " " + controlPoints.get(i - 1).getZ() + " if entity @p[distance=9..,gamemode=!spectator, scores={MSGDum1CP" + i + "=200..}] run scoreboard players add @a[distance=9..,gamemode=!spectator, scores={MSGDum1CP" + i + "=200..}] MSGDum2CP" + i + " 1");
                fileCommands19.add("execute positioned " + controlPoints.get(i - 1).getX() + " " + (controlPoints.get(i - 1).getY() + 5) + " " + controlPoints.get(i - 1).getZ() + " if entity @p[distance=9..,gamemode=!spectator,scores={MSGDum2CP" + i + "=200,MSGDum1CP" + i + "=200..}] run tellraw @a [\"\",{\"text\":\" ⎜ \",\"color\":\"gray\"},{\"text\":\"" + communityName + " UHC\",\"color\":\"gold\"},{\"text\":\" ⎜ \",\"color\":\"gray\"},{\"text\":\"CONTROL POINT " + i + " HAS BEEN ABANDONED\",\"color\":\"light_purple\"},{\"text\":\" ⎜ \",\"color\":\"gray\"}]");
            fileCommands19.add("execute positioned " + controlPoints.get(i - 1).getX() + " " + (controlPoints.get(i - 1).getY() + 5) + " " + controlPoints.get(i - 1).getZ() + " if entity @p[distance=9..,gamemode=!spectator,scores={MSGDum2CP" + i + "=200,MSGDum1CP" + i + "=200..}] run scoreboard players reset @a[distance=9..,gamemode=!spectator] MSGDum1CP" + i);


            FileData file19 = new FileData("controlpoint_" + i, fileCommands19);
            files.add(file19);
        }


        for (CarePackage carepackage : carePackages) {
            ArrayList<String> fileCommands20 = new ArrayList<>();
            fileCommands20.add("forceload add " + carepackage.getX() + " " + carepackage.getZ() + " " + carepackage.getX() + " " + carepackage.getZ());
            fileCommands20.add("setblock " + carepackage.getX() + " " + carepackage.getY() + " " + carepackage.getZ() + " chest{CustomName:\"\\\"" + carepackage.getDisplayName() + "\\\"\",Items:" + carepackage.getNbtTag() + "}");
            fileCommands20.add("forceload remove " + carepackage.getX() + " " + carepackage.getZ() + " " + carepackage.getX() + " " + carepackage.getZ());

            fileCommands20.add("title @a title {\"text\":\"" + carepackage.getDisplayName() + "!\", \"bold\":true, \"italic\":true, \"color\":\"gold\"}");
            fileCommands20.add("title @a subtitle {\"text\":\"Delivered now on the surface!\", \"bold\":true, \"italic\":true, \"color\":\"light_purple\"}");
            fileCommands20.add("give @a[gamemode=!spectator] minecraft:compass{display:{Name:\"{\\\"text\\\":\\\"" + carepackage.getDisplayName() + " available at " + carepackage.getX() + ", " + carepackage.getY() + ", " + carepackage.getZ() + "\\\"}\"}, LodestoneDimension:\"minecraft:overworld\",LodestoneTracked:0b,LodestonePos:{X:" + carepackage.getX() + ",Y:" + carepackage.getY() + ",Z:" + carepackage.getZ() + "}}");

            FileData file20 = new FileData("carepackage_" + carepackage.getName(), fileCommands20);
            files.add(file20);
        }

//        ArrayList<String> fileCommands21 = new ArrayList<>();
//        for (int i = 1; i < controlPoints.size()+1; i++) {
//            fileCommands21.add("execute at @e[type=minecraft:end_crystal,scores={Crystal=" + i + "}] positioned ~ ~200 ~ if entity @p[distance=..5,gamemode=!spectator] run scoreboard players add @a[distance=..5,gamemode=!spectator] MSGDum1CP" + i + " 1");
//            fileCommands21.add("execute at @e[type=minecraft:end_crystal,scores={Crystal=" + i + "}] positioned ~ ~200 ~ if entity @p[distance=..5,gamemode=!spectator,scores={MSGDum1CP" + i + "=200}] run tellraw @a [\"\",{\"text\":\" ⎜ \",\"color\":\"gray\"},{\"text\":\"THE DIORITE EXPERTS UHC\",\"color\":\"gold\"},{\"text\":\" ⎜ \",\"color\":\"gray\"},{\"text\":\"CONTROL POINT " + i + " IS UNDER ATTACK!\",\"color\":\"light_purple\"},{\"text\":\" ⎜ \",\"color\":\"gray\"}]");
//            fileCommands21.add("execute at @e[type=minecraft:end_crystal,scores={Crystal=" + i + "}] positioned ~ ~200 ~ if entity @p[distance=..5,gamemode=!spectator,scores={MSGDum1CP" + i + "=200}] run scoreboard players reset @a[distance=..5,gamemode=!spectator] MSGDum2CP" + i);
//
//            fileCommands21.add("execute at @e[type=minecraft:end_crystal,scores={Crystal=" + i + "}] positioned ~ ~200 ~ if entity @p[distance=6..,gamemode=!spectator, scores={MSGDum1CP" + i + "=200..}] run scoreboard players add @a[distance=6..,gamemode=!spectator, scores={MSGDum1CP" + i + "=200..}] MSGDum2CP" + i + " 1");
//            fileCommands21.add("execute at @e[type=minecraft:end_crystal,scores={Crystal=" + i + "}] positioned ~ ~200 ~ if entity @p[distance=6..,gamemode=!spectator,scores={MSGDum2CP" + i + "=200,MSGDum1CP" + i + "=200..}] run tellraw @a [\"\",{\"text\":\" ⎜ \",\"color\":\"gray\"},{\"text\":\"THE DIORITE EXPERTS UHC\",\"color\":\"gold\"},{\"text\":\" ⎜ \",\"color\":\"gray\"},{\"text\":\"CONTROL POINT " + i + " HAS BEEN ABANDONED\",\"color\":\"light_purple\"},{\"text\":\" ⎜ \",\"color\":\"gray\"}]");
//            fileCommands21.add("execute at @e[type=minecraft:end_crystal,scores={Crystal=" + i + "}] positioned ~ ~200 ~ if entity @p[distance=6..,gamemode=!spectator,scores={MSGDum2CP" + i + "=200,MSGDum1CP" + i + "=200..}] run scoreboard players reset @a[distance=6..,gamemode=!spectator] MSGDum1CP" + i);
//        }
//
//        FileData file21 = new FileData("controlpoint_messages", fileCommands21);
//        files.add(file21);

        ArrayList<String> fileCommands22 = new ArrayList<>();

        fileCommands22.add("title @a title {\"text\":\"" + "200 Supply Drops" + "!\", \"bold\":true, \"italic\":true, \"color\":\"gold\"}");
        fileCommands22.add("title @a subtitle {\"text\":\"Delivered NOW on the surface!\", \"bold\":true, \"italic\":true, \"color\":\"light_purple\"}");

        for (int i = 0; i < 200; i++) {
            fileCommands22.add("summon minecraft:area_effect_cloud ~ ~5 ~ {Passengers:[{id:falling_block,Time:1,DropItem:0b,BlockState:{Name:\"minecraft:chest\"},TileEntityData:{CustomName:\"\\\"Loot chest\\\"\",LootTable:\"uhc:supply_drop\"}}]}");
        }

        FileData file22 = new FileData("drop_carepackages", fileCommands22);
        files.add(file22);

        ArrayList<String> fileCommands23 = new ArrayList<>();

        fileCommands23.add("execute if entity @e[type=minecraft:falling_block,distance=..2] run spreadplayers 0 0 10 500 false @e[type=minecraft:falling_block,distance=..2]");

        FileData file23 = new FileData("carepackage_distributor", fileCommands23);
        files.add(file23);

        //fileCommands24 has been deleted.

        ArrayList<String> fileCommands25 = new ArrayList<>();

        //fileCommands25.add("give @p written_book{pages:['[\"\",{\"text\":\"The Diorite Experts\",\"bold\":true},{\"text\":\"\\\\nUltraHardCore S38\\\\nSa 07/11/2020\\\\n\\\\n\\\\n\",\"color\":\"reset\"},{\"text\":\"Rules & Information\",\"bold\":true,\"underlined\":true},{\"text\":\"\\\\n\\\\n\\\\n\\\\nWritten by: Snodog627\",\"color\":\"reset\"}]','[\"\",{\"text\":\"Contents\",\"bold\":true,\"underlined\":true},{\"text\":\"\\\\n\\\\n\",\"color\":\"reset\"},{\"text\":\"Introduction.......................3\",\"clickEvent\":{\"action\":\"change_page\",\"value\":3},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Go to Page 3\"}},{\"text\":\"\\\\n\"},{\"text\":\"Victory conditions........6\",\"clickEvent\":{\"action\":\"change_page\",\"value\":6},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Go to Page 6\"}},{\"text\":\"\\\\n\"},{\"text\":\"Rules........................................8\",\"clickEvent\":{\"action\":\"change_page\",\"value\":8},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Go to Page 8\"}},{\"text\":\"\\\\n\"},{\"text\":\"Utility.....................................10\",\"clickEvent\":{\"action\":\"change_page\",\"value\":10},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Go to Page 10\"}},{\"text\":\"\\\\n\"},{\"text\":\"Control Point.................11\",\"clickEvent\":{\"action\":\"change_page\",\"value\":11},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Go to Page 11\"}},{\"text\":\"\\\\n\"},{\"text\":\"Care Package...............16\",\"clickEvent\":{\"action\":\"change_page\",\"value\":16},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Go to Page 16\"}},{\"text\":\"\\\\n\"},{\"text\":\"Change log.....................19\",\"clickEvent\":{\"action\":\"change_page\",\"value\":19},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Go to Page 19\"}},{\"text\":\"\\\\n\"},{\"text\":\"Statistics...........................20\",\"clickEvent\":{\"action\":\"change_page\",\"value\":20},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Go to Page 20\"}},{\"text\":\"\\\\n \"}]','[\"\",{\"text\":\"Introduction\",\"bold\":true,\"underlined\":true},{\"text\":\"\\\\nWelcome to the 38th season of The Diorite Experts UltraHardCore! UHC is a survival/PvP game in Minecraft and is special because of its non-natural health regeneration. After the survival period where players get geared up it is time for the final PvP battle that can\",\"color\":\"reset\"}]','{\"text\":\"happen at any moment. The team of the last player standing wins the match. In addition to that, it is also possible to win the game by capturing Control Points. On the side, players can also gather loot by completing side quests: Care Package drops.\"}','[\"\",{\"text\":\"We hope you will have a wonderful time and good luck taking the crown!\\\\n\\\\n\\\\n\"},{\"text\":\"GLHF\",\"bold\":true},{\"text\":\"\\\\n~Bas, Luc, Wouter\\\\nS38 UHC Committee\\\\n \",\"color\":\"reset\"}]','[\"\",{\"text\":\"Victory Conditions\",\"bold\":true,\"underlined\":true},{\"text\":\"\\\\nThere are two ways to win UHC:\\\\n\",\"color\":\"reset\"},{\"text\":\"1.\",\"bold\":true},{\"text\":\" Be the last team standing\\\\n\",\"color\":\"reset\"},{\"text\":\"2. \",\"bold\":true},{\"text\":\"Earn 2400CP on the Control Points\\\\n\\\\n\\\\nCondition \",\"color\":\"reset\"},{\"text\":\"1.\",\"bold\":true},{\"text\":\" can only be claimed if condition \",\"color\":\"reset\"},{\"text\":\"2.\",\"bold\":true},{\"text\":\" has not been achieved yet.\",\"color\":\"reset\"}]','[\"\",{\"text\":\"The spoils:\",\"underlined\":true},{\"text\":\"\\\\nAs an appreciation of their skill, the winners of UltraHardCore will receive a role on Discord which elevates their spirits into divinity.\\\\n \",\"color\":\"reset\"}]','[\"\",{\"text\":\"Rules\",\"bold\":true,\"underlined\":true},{\"text\":\"\\\\nBanned items:\\\\n- Potion of regeneration,\\\\n- Potion of strength.\\\\n\\\\nA player cannot:\\\\n- trap a nether portal,\\\\n- share information that is not public after dying.\",\"color\":\"reset\"}]','[\"\",{\"text\":\"Other rules:\",\"underlined\":true},{\"text\":\"\\\\n- PvP is not allowed until the second day,\\\\n- Deaths due to PvE or glitches can be reversible, but respawns are handicapped,\\\\n- Players are not allowed to enter the spawn after the match has started.\",\"color\":\"reset\"}]','[\"\",{\"text\":\"Utility\",\"bold\":true,\"underlined\":true},{\"text\":\"\\\\n- Eternal day is enabled after 20 minutes,\\\\n- World size: 1500x1500\\\\n- A golden apple can be crafted like:\\\\n \\\\u0020 \\\\u0020 \\\\u0020 \\\\u0020 \\\\u0020\\\\u2610\\\\u2612\\\\u2610\\\\n \\\\u0020 \\\\u0020 \\\\u0020 \\\\u0020 \\\\u0020\\\\u2612\\\\u2611\\\\u2612\\\\n \\\\u0020 \\\\u0020 \\\\u0020 \\\\u0020 \\\\u0020\\\\u2610\\\\u2612\\\\u2610\\\\nwhere \\\\u2612 is a gold bar, \\\\u2611 is a playerhead.\",\"color\":\"reset\"}]','[\"\",{\"text\":\"Control Point\",\"bold\":true,\"underlined\":true},{\"text\":\"\\\\nThere are two Control Points in this game. When a player is within 5 blocks from the Control Point, they are awarded a CP score every second.\\\\n\\\\nCP1 awards 2CP per second and CP2 awards 3CP per second.\",\"color\":\"reset\"}]','[\"\",{\"text\":\"CP1 is enabled after 30 minutes of game time. After a team accumulates 720CP on CP1, CP2 will also be enabled.\\\\n\\\\nThe coordinates of both CP will be revealed after 30 minutes of game time.\\\\n\\\\n\"},{\"text\":\"Tip: When CP2 has just\",\"italic\":true}]','[\"\",{\"text\":\"been enabled it is faster to immediately control CP2!\",\"italic\":true},{\"text\":\"\\\\n\\\\nThe team that manages to first score 720CP receives a splash potion of resistance.\\\\n\\\\nControl Points are recognized by the beam of an end crystal.\",\"color\":\"reset\"}]','[\"\",{\"text\":\"When a team reaches 2400CP, they win the game. The remaining teams get 3 minutes to prepare and will afterwards be spread into a shrinking area for a final battle.\\\\n\\\\n\"},{\"text\":\"Note: It is not possible to capture a Control \",\"italic\":true}]','{\"text\":\"Point when players of other teams are present on that CP.\",\"italic\":true}','[\"\",{\"text\":\"Care Package\",\"bold\":true,\"underlined\":true},{\"text\":\"\\\\nThis game contains two Care Packages:\\\\n- \",\"color\":\"reset\"},{\"text\":\"Enchanting\",\"underlined\":true,\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://media.discordapp.net/attachments/505386630736248834/746784653922533506/unknown.png\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Click for preview of the contents\"}},{\"text\":\"\\\\n- \",\"color\":\"reset\"},{\"text\":\"Anti-ControlPoint\",\"underlined\":true,\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://media.discordapp.net/attachments/505386630736248834/746784937646096404/unknown.png\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Click for preview of the contents\"}},{\"text\":\"\\\\n\\\\nThe enchanting Care Package is deployed after 20 minutes of gametime. Its purpose is for those who are either unlucky or bored with caving. Its\",\"color\":\"reset\"}]','{\"text\":\"coordinates will be made available through a bossbar which shows up every 30 seconds.\\\\n\\\\nThe anti-ControlPoint Care Package is deployed after CP2 is enabled and contains all kind of havoc that makes CP capturers sweat. Its coordinates will be displayed in the\"}','[\"\",{\"text\":\"same aforementioned bossbar.\\\\n\\\\n\"},{\"text\":\"Tip: If you are not doing so well or late to the Control Point race, you might want to check these out!\",\"italic\":true}]','[\"\",{\"text\":\"Change log\",\"bold\":true,\"underlined\":true},{\"text\":\"\\\\n- Removed Traitor faction (will be tested in again in December!)\\\\n- CP bug fixed\\\\n- Team generation is now done through an algorithm made in MATLAB\",\"color\":\"reset\"}]','[\"\",{\"text\":\"Statistics\",\"bold\":true,\"underlined\":true},{\"text\":\"\\\\nFor more statistics check out \",\"color\":\"reset\"},{\"text\":\"/r/TheDioriteExpertsUHC\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://old.reddit.com/r/TheDioriteExpertsUHC/\"}},{\"text\":\"\\\\n\\\\n\"},{\"text\":\"Current Rankings:\",\"bold\":true},{\"text\":\"\\\\n1. Bobdafish (166)\\\\n2. Snodog627 (115)\\\\n3. TheDinoGame (113)\\\\n4. jonmo0105 (99)\\\\n5. Thurian (72)\\\\n\\\\n\\\\n\\\\n \",\"color\":\"reset\"}]','[\"\",{\"text\":\"Previous winners:\",\"bold\":true},{\"text\":\"\\\\n\",\"color\":\"reset\"},{\"text\":\"S33\",\"bold\":true},{\"text\":\": jonmo0105, PerfidyIsKey, Snodog627\\\\n\",\"color\":\"reset\"},{\"text\":\"S34\",\"bold\":true},{\"text\":\": jonmo0105\\\\n\",\"color\":\"reset\"},{\"text\":\"S35\",\"bold\":true},{\"text\":\": Kakarot057, PerfidyIsKey\\\\n\",\"color\":\"reset\"},{\"text\":\"S36\",\"bold\":true},{\"text\":\": Bobdafish, TheDinoGame, W0omy\\\\n\",\"color\":\"reset\"},{\"text\":\"S37:\",\"bold\":true},{\"text\":\" Bobdafish, TheDinoGame\\\\n\\\\n\\\\n\\\\n \",\"color\":\"reset\"}]','[\"\",{\"text\":\"Most Wins:\",\"bold\":true},{\"text\":\"\\\\n1. Snodog627 (18)\\\\n2. Mr9Madness (11)\\\\n3. PR0BA (5)\\\\n4. W0omy (5)\\\\n5. Thurian (4)\\\\n\\\\n\",\"color\":\"reset\"},{\"text\":\"Most Kills:\",\"bold\":true},{\"text\":\"\\\\n1. Snodog627 (40)\\\\n2. Mr9Madness (17)\\\\n3. TheDinoGame (9)\\\\n4. Tiba101 (8)\\\\n5. jonmo0105 (8)\\\\n\\\\n\\\\n\\\\n\\\\n \",\"color\":\"reset\"}]'],title:\"The Diorite Experts UHC RuleBook\",author:Snodog627,display:{Lore:[\"Information and rules on the 38th season of The Diorite Experts UltraHardCore\"]}}");
        //fileCommands25.add("give @p minecraft:compass");
        //FileData file25 = new FileData("give_instructions", fileCommands25);
        //files.add(file25);

        ArrayList<String> fileCommands26 = new ArrayList<>();

        //fileCommands26.add("execute as @a[scores={Quits=1..}] at @s run function uhc:give_instructions");
        fileCommands26.add("scoreboard players set @a[scores={Quits=1..}] Quits 0");

        FileData file26 = new FileData("instruction_handout_loop", fileCommands26);
        files.add(file26);

        // Assign players to Traitor Faction
        ArrayList<String> fileCommands27 = new ArrayList<>();
        //fileCommands27.add("tag @r[limit=1,tag=!DontMakeTraitor] add Traitor");
        for (Player p : players) {
            if (p.getIgnoreTraitor()) {
                fileCommands27.add("tag " + p.getPlayerName() + " add DontMakeTraitor");
            }
        }

        fileCommands27.add("tag @r[limit=1,tag=!DontMakeTraitor,scores={Rank=" + minTraitorRank + "..},gamemode=!spectator] add Traitor");

        for (Team t : teams) {
            fileCommands27.add("execute if entity @p[tag=Traitor,team=" + t.getName() + "] run tag @a[team=" + t.getName() + "] add DontMakeTraitor");
        }
        //fileCommands27.add("tag @r[limit=1,tag=!DontMakeTraitor] add Traitor");
        fileCommands27.add("tag @r[limit=1,tag=!DontMakeTraitor,scores={Rank="  + minTraitorRank + "..},gamemode=!spectator] add Traitor");
        fileCommands27.add("execute as @a[tag=Traitor] run tellraw @s [\"\",{\"text\":\"You feel like betrayal today. You have become a Traitor. Your faction consists of: \",\"italic\":true,\"color\":\"red\"},{\"selector\":\"@a[tag=Traitor]\",\"italic\":true,\"color\":\"red\"},{\"text\":\".\",\"italic\":true,\"color\":\"red\"}]");
        fileCommands27.add("title @a title [\"\",{\"text\":\"A Traitor Faction\",\"bold\":true,\"color\":\"red\"}]");
        fileCommands27.add("title @a subtitle [\"\",{\"text\":\"has been founded!\",\"bold\":true,\"color\":\"dark_red\"}]");
        fileCommands27.add("setblock 11 " + (worldBottom + 2) + " 0 minecraft:redstone_block destroy");

        FileData file27 = new FileData("traitor_handout", fileCommands27);
        files.add(file27);

        ArrayList<String> fileCommands28 = new ArrayList<>();

        fileCommands28.add("execute as @a[tag=Traitor] run title @s actionbar [\"\",{\"text\":\">>> \",\"color\":\"gold\"},{\"text\":\"Traitor Faction: \",\"color\":\"light_purple\"},{\"selector\":\"@a[tag=Traitor]\"},{\"text\":\" <<<\",\"color\":\"gold\"}]");

        FileData file28 = new FileData("traitor_actionbar", fileCommands28);
        files.add(file28);

//        ArrayList<String> fileCommands29 = new ArrayList<>();
//        for (Team t : teams) {
//            fileCommands29.add("execute as @r[limit=1,gamemode=!spectator] run scoreboard players operation @p[scores={Admin=1}] CP" + t.getName() + " > @s[team=" + t.getName() + "] ControlPoint");
//            fileCommands29.add("execute as @r[limit=1,gamemode=!spectator] run scoreboard players operation @s[team=" + t.getName() + "] ControlPoint > @p[scores={Admin=1}] CP" + t.getName());
//        }
//
//        FileData file29 = new FileData("team_score", fileCommands29);
//        files.add(file29);

        ArrayList<String> fileCommands29 = new ArrayList<>();
        for (int i = 1; i < controlPoints.size() + 1; i++) {
            for (Team t : teams) {
                fileCommands29.add("execute as @r[limit=1,gamemode=!spectator] run scoreboard players operation @p[scores={Admin=1}] CP" + i + t.getName() + " > @s[team=" + t.getName() + "] ControlPoint" + i);
                fileCommands29.add("execute as @r[limit=1,gamemode=!spectator] run scoreboard players operation @s[team=" + t.getName() + "] ControlPoint" + i + " > @p[scores={Admin=1}] CP" + i + t.getName());
            }
        }

        for (Team t : teams) {
            fileCommands29.add("execute as @r[limit=1,gamemode=!spectator,x=" + (cp1.getX() - 6) + ",y=" + (cp1.getY() - 1) + ",z=" + (cp1.getZ() - 6) + ",dx=12,dy=12,dz=12,team=" + t.getName() + "] run scoreboard players operation @p[scores={Admin=1}] CP1" + t.getName() + " > @p[scores={Admin=1}] CP2" + t.getName());
            fileCommands29.add("execute as @r[limit=1,gamemode=!spectator,x=" + (cp2.getX() - 6) + ",y=" + (cp2.getY() - 1) + ",z=" + (cp2.getZ() - 6) + ",dx=12,dy=12,dz=12,team=" + t.getName() + "] run scoreboard players operation @p[scores={Admin=1}] CP2" + t.getName() + " > @p[scores={Admin=1}] CP1" + t.getName());
        }


        FileData file29 = new FileData("team_score", fileCommands29);
        files.add(file29);

        ArrayList<String> fileCommands30 = new ArrayList<>();

        for (ControlPoint cp : controlPoints) {
            fileCommands30.add("forceload add " + cp.getX() + " " + cp.getZ() + " " + cp.getX() + " " + cp.getZ());
            fileCommands30.add("setblock " + cp.getX() + " " + (cp.getY() + 11) + " " + cp.getZ() + " " + "minecraft:structure_block[mode=load]{metadata:\"\",mirror:\"NONE\",ignoreEntities:1b,powered:0b,seed:0L,author:\"?\",rotation:\"NONE\",posX:-6,mode:\"LOAD\",posY:-13,sizeX:13,posZ:-6,integrity:1.0f,showair:0b,name:\"minecraft:control_point\",sizeY:14,sizeZ:13,showboundingbox:1b} replace");
            fileCommands30.add("setblock " + cp.getX() + " " + (cp.getY() + 10) + " " + cp.getZ() + " " + "minecraft:redstone_block replace");
            for (int i = cp.getY() + 11; i < worldHeight; i++) {
                fileCommands30.add("execute unless block " + cp.getX() + " " + i + " " + cp.getZ() + " minecraft:air run setblock " + cp.getX() + " " + i + " " + cp.getZ() + " minecraft:glass");
            }
            fileCommands30.add("forceload remove " + cp.getX() + " " + cp.getZ() + " " + cp.getX() + " " + cp.getZ());
        }

        FileData file30 = new FileData("spawn_controlpoints", fileCommands30);
        files.add(file30);

        ArrayList<String> fileCommands31 = new ArrayList<>();

        for (Player p : players) {
            fileCommands31.add("scoreboard players set " + p.getPlayerName() + " Rank " + p.getRank());
        }
        fileCommands31.add("scoreboard objectives setdisplay sidebar Rank");

        FileData file31 = new FileData("display_rank", fileCommands31);
        files.add(file31);

        // Effects given when the target blocks are hit on the Control Point
        for (int i = 0; i < 4; i++) {
            ArrayList<String> fileCommands32 = new ArrayList<>();

            for (ControlPoint cp : controlPoints) {
                fileCommands32.add("execute positioned " + cp.getX() + " " + cp.getY() + " " + cp.getZ() + " run effect give @p[gamemode=!spectator] minecraft:" + effect.get(i).getEffectName() + " " + effect.get(i).getDuration() + " " + effect.get(i).getAmplification());
            }


            FileData file32 = new FileData("give_status_effect" + i, fileCommands32);
            files.add(file32);

        }

        // World pre-loading
        ArrayList<String> fileCommands33 = new ArrayList<>();
        fileCommands33.add("setblock 6 " + (worldBottom + 2) + " 15 minecraft:redstone_block");

        FileData file33 = new FileData("world_pre_load",fileCommands33);
        files.add(file33);

        // Horse frost walker
        ArrayList<String> fileCommands34 = new ArrayList<>();
        fileCommands34.add("execute at @a[nbt={RootVehicle:{Entity:{id:\"minecraft:horse\"}}}] run fill ~-2 ~-2 ~-2 ~2 ~2 ~2 ice replace water");

        FileData file34 = new FileData("horse_frost_walker",fileCommands34);
        files.add(file34);

        // Update wolf collar color
        ArrayList<String> fileCommands35 = new ArrayList<>();
        fileCommands35.add("execute as @e[type=minecraft:wolf] store result score @s CollarCheck0 run data get entity @s Owner[0]");
        fileCommands35.add("execute as @e[type=minecraft:wolf] store result score @s CollarCheck1 run data get entity @s Owner[1]");
        for (Team t: teams) {
            fileCommands35.add("execute as @a[team=" + t.getName() + "] store result score @s CollarCheck0 run data get entity @s UUID[0]");
            fileCommands35.add("execute as @a[team=" + t.getName() + "] store result score @s CollarCheck1 run data get entity @s UUID[1]");
            fileCommands35.add("tag @a[team=" + t.getName() + "] add CollarCheck");
            fileCommands35.add("execute as @e[type=wolf] if score @s CollarCheck0 = @p[tag=CollarCheck] CollarCheck0 if score @s CollarCheck1 = @p[tag=CollarCheck] CollarCheck1 run data modify entity @s CollarColor set value " + t.getCollarColor() + "b");
            fileCommands35.add("tag @a[team=" + t.getName() + "] remove CollarCheck");
        }

        FileData file35 = new FileData("wolf_collar_execute",fileCommands35);
        files.add(file35);

        // Update side bar
        ArrayList<String> fileCommands36 = new ArrayList<>();
        fileCommands36.add("scoreboard players add @p[scores={Admin=1}] SideDum 1");
        int i = 0;
        for (ScoreboardObjective s: scoreboardObjectives)
        {
            if (s.getDisplaySideBar())
            {
                i++;
                fileCommands36.add("execute if entity @p[scores={SideDum=" + (5*tickPerSecond*i) + "}] run scoreboard objectives setdisplay sidebar " + s.getName());
            }
        }
        fileCommands36.add("execute if entity @p[scores={SideDum=" + (5*tickPerSecond*i+1) +"}] run scoreboard players reset @p[scores={Admin=1}] SideDum");

        FileData file36 = new FileData("update_sidebar",fileCommands36);
        files.add(file36);

        // Timer
        ArrayList<String> fileCommands37 = new ArrayList<>();
        fileCommands37.add("scoreboard players add @p[scores={Admin=1}] Time2 1");
        fileCommands37.add("scoreboard players add @p[scores={Admin=1}] TimDum 1");
        fileCommands37.add("execute if entity @p[scores={TimDum=" + tickPerSecond + "}] run scoreboard players add @p[scores={Admin=1}] TimeDum 1");
        fileCommands37.add("execute store result score CurrentTime Time run scoreboard players get @p[scores={Admin=1}] TimeDum");
        fileCommands37.add("execute if entity @p[scores={TimDum=" + tickPerSecond + "..}] run scoreboard players reset @p[scores={Admin=1}] TimDum");
        fileCommands37.add("execute if entity @p[scores={Time2=" + (300*tickPerSecond) + "}] run tellraw @a [\"\",{\"text\":\"PVP IS NOT ALLOWED UNTIL DAY 2!\",\"color\":\"gray\"}]");
        fileCommands37.add("execute if entity @p[scores={Time2=" + (1200*tickPerSecond) + "}] run tellraw @a [\"\",{\"text\":\" ｜ \",\"color\":\"gray\"},{\"text\":\"" + communityName + " UHC\",\"color\":\"gold\"},{\"text\":\" ｜ \",\"color\":\"gray\"},{\"text\":\"DAY TIME HAS ARRIVED!\",\"color\":\"light_purple\"},{\"text\":\" ｜ \",\"color\":\"gray\"}]");

        FileData file37 = new FileData("timer",fileCommands37);
        files.add(file37);
    }

}
