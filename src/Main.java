import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        new Main().run();
    }

    //DatapackData<
    private static String uhcNumber = "S42";
    private static String version = "2.0";

    private static String userFolder = "bthem";
    private static String worldName = "big-test";
    //private static String dataPackLocation = "C:\\Users\\" + userFolder + "\\AppData\\Roaming\\.minecraft\\saves\\" + worldName + "\\datapacks\\";
    private static String dataPackLocation = "C:\\Users\\bthem\\Desktop\\Server\\world\\datapacks\\";
    private static String dataPackLocationServer = "D:\\Documents\\Gaming\\MinecraftServers\\MinecraftServers\\world\\datapacks\\";
    private static String dataPackName = "uhc-datapack-s" + uhcNumber + "v" + version;
    private static String fileLocation = dataPackLocation + dataPackName + "\\data\\uhc\\";
    private static String fileLocationServer = dataPackLocationServer + dataPackName + "\\data\\uhc\\";

    //DatapackData>

    //GameData<
    private static int chestSize = 27;
    private static String admin = "PerfidyIsKey";
    private static String startCoordinates = "-5 70 -4";
    private ArrayList<Team> teams = new ArrayList<>();
    private ArrayList<ControlPoint> controlPoints = new ArrayList<>();
    private ArrayList<CarePackage> carePackages = new ArrayList<>();
    private ArrayList<ScoreboardObjective> scoreboardObjectives = new ArrayList<>();
    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<StatusEffect> effect = new ArrayList<>();
    private static int worldSize = 1500;
    private static int worldHeight = 257;
    private static int minTraitorRank = 50;
    private static String communityName = "THE DIORITE EXPERTS";
    //GameData>


    private ArrayList<FileData> files = new ArrayList<>();


    private void run() {
        boolean menuRunning = true;
        Scanner scanner = new Scanner(System.in);
        String input;
        initGameData();
        makeFunctionFiles();
        makeRecipeFiles();
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
                                //writeFile(f, fileLocationServer);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } else if (input.equals("create datapack")) {
                try {
                    createDatapack(dataPackLocation);
                    //createDatapack(dataPackLocationServer);
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
                //writeFile(f, fileLocationServer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createDatapack(String dataPackLocation) throws IOException {
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
                    if (recipes.mkdir()) {
                        updateAllFiles();
                    } else {
                        System.out.println("Something went wrong creating Datapack 2");
                    }
                }
            }
        } else {
            System.out.println("Something went wrong creating Datapack 3");
        }
    }

    private void writeFile(FileData fileData, String fileLocation) throws IOException {
        ArrayList<String> fileText = fileData.getFileText();
        File file;
        if (fileData.getType().equals("recipe")) {
            file = new File(fileLocation + "recipes\\" + fileData.getName() + ".json");
        } else {
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

    private void initGameData() {
        String[] colors = {"yellow", "blue", "red", "dark_purple", "dark_green", "light_purple", "black", "gold", "gray", "aqua", "dark_red", "dark_blue", "dark_aqua"};
        String[] bossbarColors = {"yellow", "blue", "red", "purple", "green", "pink", "white", "white", "white", "white", "white", "white", "white"};
        for (int i = 0; i < colors.length; i++) {
            Team team = new Team("Team" + i, colors[i], bossbarColors[i]);
            teams.add(team);
        }

        ControlPoint cp1 = new ControlPoint("CP1", 48000, 2, 253, 88, 77);
        ControlPoint cp2 = new ControlPoint("CP2", 48000, 3, 8, 8, 254);
        controlPoints.add(cp1);
        controlPoints.add(cp2);

        CarePackage carePackage1 = new CarePackage("enchanting", "Enchanting Drop",
                "[{Slot:3b,id:\"minecraft:enchanted_book\",Count:1b,tag:{StoredEnchantments:[{lvl:1s,id:\"minecraft:power\"}]}},{Slot:4b,id:\"minecraft:golden_apple\",Count:1b},{Slot:5b,id:\"minecraft:enchanted_book\",Count:1b,tag:{StoredEnchantments:[{lvl:2s,id:\"minecraft:sharpness\"}]}},{Slot:12b,id:\"minecraft:apple\",Count:1b},{Slot:13b,id:\"minecraft:anvil\",Count:1b},{Slot:14b,id:\"minecraft:apple\",Count:1b},{Slot:21b,id:\"minecraft:enchanted_book\",Count:1b,tag:{StoredEnchantments:[{lvl:2s,id:\"minecraft:sharpness\"}]}},{Slot:22b,id:\"minecraft:book\",Count:1b},{Slot:23b,id:\"minecraft:enchanted_book\",Count:1b,tag:{StoredEnchantments:[{lvl:1s,id:\"minecraft:protection\"}]}}]",
                16, 70, 236);
        carePackages.add(carePackage1);

        CarePackage carePackage2 = new CarePackage("anti_cp", "Anti Controlpoint Drop",
                "[{Slot:1b,id:\"minecraft:gunpowder\",Count:1b},{Slot:2b,id:\"minecraft:gunpowder\",Count:1b},{Slot:3b,id:\"minecraft:tnt\",Count:1b},{Slot:4b,id:\"minecraft:flint_and_steel\",Count:1b},{Slot:5b,id:\"minecraft:tnt\",Count:1b},{Slot:6b,id:\"minecraft:sand\",Count:1b},{Slot:7b,id:\"minecraft:sand\",Count:1b},{Slot:11b,id:\"minecraft:enchanted_book\",Count:1b,tag:{StoredEnchantments:[{lvl:4s,id:\"minecraft:blast_protection\"}]}},{Slot:12b,id:\"minecraft:lava_bucket\",Count:1b},{Slot:13b,id:\"minecraft:tnt\",Count:1b},{Slot:14b,id:\"minecraft:lava_bucket\",Count:1b},{Slot:15b,id:\"minecraft:enchanted_book\",Count:1b,tag:{StoredEnchantments:[{lvl:4s,id:\"minecraft:blast_protection\"}]}},{Slot:19b,id:\"minecraft:sand\",Count:1b},{Slot:20b,id:\"minecraft:sand\",Count:1b},{Slot:21b,id:\"minecraft:tnt\",Count:1b},{Slot:22b,id:\"minecraft:flint_and_steel\",Count:1b},{Slot:23b,id:\"minecraft:tnt\",Count:1b},{Slot:24b,id:\"minecraft:gunpowder\",Count:1b},{Slot:25b,id:\"minecraft:gunpowder\",Count:1b}]",
                284, 69, -16);
        carePackages.add(carePackage2);

        scoreboardObjectives.add(new ScoreboardObjective("Admin", "dummy"));
        scoreboardObjectives.add(new ScoreboardObjective("TimDum", "dummy"));
        scoreboardObjectives.add(new ScoreboardObjective("Time", "dummy", "\"Elapsed Time\""));
        scoreboardObjectives.add(new ScoreboardObjective("Time2", "dummy", "\"Elapsed Time\""));
        scoreboardObjectives.add(new ScoreboardObjective("SideDum", "dummy"));
//        scoreboardObjectives.add(new ScoreboardObjective("CPDum1", "dummy"));
//        scoreboardObjectives.add(new ScoreboardObjective("CPDum2", "dummy"));
        scoreboardObjectives.add(new ScoreboardObjective("ControlPoint1", "dummy"));
        scoreboardObjectives.add(new ScoreboardObjective("ControlPoint2", "dummy"));
        scoreboardObjectives.add(new ScoreboardObjective("MSGDum1CP1", "dummy"));
        scoreboardObjectives.add(new ScoreboardObjective("MSGDum2CP1", "dummy"));
        scoreboardObjectives.add(new ScoreboardObjective("MSGDum1CP2", "dummy"));
        scoreboardObjectives.add(new ScoreboardObjective("MSGDum2CP2", "dummy"));
        scoreboardObjectives.add(new ScoreboardObjective("Highscore1", "dummy"));
        scoreboardObjectives.add(new ScoreboardObjective("Highscore2", "dummy"));
        scoreboardObjectives.add(new ScoreboardObjective("Hearts", "health"));
        scoreboardObjectives.add(new ScoreboardObjective("Apples", "minecraft.used:minecraft.golden_apple", "\"Golden Apple\""));
        scoreboardObjectives.add(new ScoreboardObjective("Mining", "minecraft.mined:minecraft.stone", "\"I like mining-leaderboard\""));
        scoreboardObjectives.add(new ScoreboardObjective("Deaths", "deathCount"));
        scoreboardObjectives.add(new ScoreboardObjective("Kills", "playerKillCount"));
        scoreboardObjectives.add(new ScoreboardObjective("Crystal", "dummy"));
        scoreboardObjectives.add(new ScoreboardObjective("Quits", "minecraft.custom:minecraft.leave_game"));
        scoreboardObjectives.add(new ScoreboardObjective("Rank", "dummy"));
        scoreboardObjectives.add(new ScoreboardObjective("WorldLoad","dummy"));

        players.add(new Player("Snodog627",92));
        players.add(new Player("Mr9Madness",71,true));
        players.add(new Player("PR0BA",8));
        players.add(new Player("Tiba101",7));
        players.add(new Player("W0omy",22));
        players.add(new Player("MissTutuPrincess",22));
        players.add(new Player("Kalazniq",44));
        players.add(new Player("Vladik71",32));
        players.add(new Player("Smashking242",20));
        players.add(new Player("lilskrut",5));
        players.add(new Player("Pfalz_",20));
        players.add(new Player("ThurianBohan",51));
        players.add(new Player("PerfidyIsKey",65));
        players.add(new Player("deuce__",29));
        players.add(new Player("jonmo0105",77));
        players.add(new Player("TheDinoGame",199));
        players.add(new Player("BAAPABUGGETS",13));
        players.add(new Player("Kakarot057",49));
        players.add(new Player("viccietors",45));
        players.add(new Player("Rayqson",17));
        players.add(new Player("Xx__HexGamer__xX",83));
        players.add(new Player("Bobdafish",146));
        players.add(new Player("Alanaenae",0));
        players.add(new Player("jk20028",13));
        players.add(new Player("N_G0n",7));
        players.add(new Player("SpookySpiker",33));
        players.add(new Player("Clockweiz",12));
        players.add(new Player("Eason950116",14));
        players.add(new Player("CorruptUncle",37));
        players.add(new Player("Pimmie36",104));

        // Status effects
        effect.add(new StatusEffect("glowing",30,1));
        effect.add(new StatusEffect("fire_resistance",20,1));
        effect.add(new StatusEffect("nausea",10,1));
        effect.add(new StatusEffect("speed",20,1));

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
        fileCommands.add("fill -1 5 -1 1 150 1 minecraft:barrier");
        fileCommands.add("fill 0 5 0 0 150 0 minecraft:air");
        fileCommands.add("fill -6 150 -6 6 156 6 minecraft:barrier");
        fileCommands.add("fill -5 151 -5 5 156 5 minecraft:air");
        fileCommands.add("setblock 0 150 0 minecraft:air");
        fileCommands.add("setblock -2 0 -2 minecraft:structure_block[mode=load]{metadata:\"\",mirror:\"NONE\",ignoreEnti" +
                "ties:0b,powered:0b,seed:0L,author:\"?\",rotation:\"NONE\",posX:1,mode:\"LOAD\",posY:1,sizeX:18,posZ:1," +
                "integrity:1.0f,showair:0b,name:\"minecraft:commandcenter_s" + uhcNumber + "\",sizeY:31,sizeZ:18,showboundingbox:1b}");
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
            fileCommands4.add("replaceitem entity @a enderchest." + i + " air 1");
        }

        FileData file4 = new FileData("clear_enderchest", fileCommands4);
        files.add(file4);

        ArrayList<String> fileCommands5 = new ArrayList<>();
        fileCommands5.add("replaceitem entity @a armor.chest minecraft:iron_chestplate");
        fileCommands5.add("replaceitem entity @a armor.feet minecraft:iron_boots");
        fileCommands5.add("replaceitem entity @a armor.head minecraft:iron_helmet");
        fileCommands5.add("replaceitem entity @a armor.legs minecraft:iron_leggings");
        fileCommands5.add("replaceitem entity @a weapon.offhand minecraft:shield");
        fileCommands5.add("replaceitem entity @a weapon.mainhand minecraft:iron_axe");
        fileCommands5.add("replaceitem entity @a inventory.0 minecraft:iron_sword");
        fileCommands5.add("effect give @a minecraft:regeneration 1 255 true");

        FileData file5 = new FileData("equip_gear", fileCommands5);
        files.add(file5);

        ArrayList<String> fileCommands6 = new ArrayList<>();
        fileCommands6.add("effect give @s minecraft:resistance 99999 4 true");
        fileCommands6.add("replaceitem entity @s weapon.mainhand trident{display:{Name:\"{\\\"text\\\":\\\"The Impaler\\\"}\"}, Enchantments:[{id:sharpness,lvl:999999},{id:fire_aspect,lvl:999999},{id:unbreaking,lvl:999999},{id:loyalty,lvl:999999},{id:impaling,lvl:999999}]}");

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
        fileCommands7.add("fill 0 2 15 0 2 2 minecraft:bedrock replace");
        fileCommands7.add("fill 2 2 0 8 2 0 minecraft:bedrock replace");
        fileCommands7.add("fill 15 2 3 15 2 11 minecraft:bedrock replace");
        fileCommands7.add("scoreboard players set " + admin + " Admin 1");
        fileCommands7.add("fill 15 2 15 9 2 15 minecraft:redstone_block replace");
        fileCommands7.add("setblock 11 2 0 minecraft:bedrock destroy");
        fileCommands7.add("setblock 10 2 0 minecraft:bedrock destroy");
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
        fileCommands7.add("worldborder set "+worldSize+" 1");
        fileCommands7.add("team leave @a");
        fileCommands7.add("function uhc:display_rank");

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
        /*fileCommands9.add("tellraw @a [\"\",{\"text\":\" ｜ \",\"color\":\"gray\"},{\"text\":\"THE DIORITE EXPERTS" +
                " UHC\",\"color\":\"gold\"},{\"text\":\" ｜ \",\"color\":\"gray\"},{\"text\":\"PREDICTIONS COMPLETED" +
                "\",\"color\":\"light_purple\"},{\"text\":\" ｜ \",\"color\":\"gray\"}]");*/
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
        fileCommands13.add("fill 0 2 15 0 2 2 minecraft:redstone_block replace");
        fileCommands13.add("fill 2 2 0 6 2 0 minecraft:redstone_block replace");
        fileCommands13.add("fill 15 2 15 9 2 15 minecraft:bedrock");
        fileCommands13.add("setblock 10 2 0 minecraft:redstone_block destroy");
        fileCommands13.add("advancement revoke @a everything");
        fileCommands13.add("xp set @a 0 points");
        fileCommands13.add("execute as @a at @s run function uhc:give_instructions");

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
        fileCommands15.add("setblock 7 2 0 minecraft:redstone_block replace");
        fileCommands15.add("title @a title {\"text\":\"Control Point 1\", \"bold\":true, \"italic\":true, \"color\":\"gold\"}");
        fileCommands15.add("bossbar set minecraft:cp1 visible true");
        fileCommands15.add("bossbar set minecraft:cp2 visible true");
        fileCommands15.add("setblock 6 2 0 minecraft:bedrock replace");
        fileCommands15.add("forceload add " + cp1.getX() + " " + cp1.getZ() + " " + cp1.getX() + " " + cp1.getZ());
        fileCommands15.add("setblock " + cp1.getX() + " " + (cp1.getY() + 3) + " " + cp1.getZ() + " minecraft:air replace");
        fileCommands15.add("forceload remove " + cp1.getX() + " " + cp1.getZ() + " " + cp1.getX() + " " + cp1.getZ());
        fileCommands15.add("setblock 15 2 7 minecraft:redstone_block replace");
        fileCommands15.add("setblock 15 2 6 minecraft:redstone_block replace");
        fileCommands15.add("setblock 15 2 10 minecraft:redstone_block replace");
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
        fileCommands16.add("setblock 7 2 0 minecraft:bedrock replace");
        fileCommands16.add("setblock 8 2 0 minecraft:redstone_block replace");
        fileCommands16.add("tellraw @a [\"\",{\"text\":\" ⎜ \",\"color\":\"gray\"},{\"text\":\"" + communityName + " UHC\",\"color\":\"gold\"},{\"text\":\" ⎜ \",\"color\":\"gray\"},{\"text\":\"CONTROL POINT 2 IS NOW AVAILABLE! ETERNAL DAY ENABLED!\",\"color\":\"light_purple\"},{\"text\":\" ⎜ \",\"color\":\"gray\"}]");
        fileCommands16.add("setblock 15 2 11 minecraft:redstone_block replace");
        fileCommands16.add("forceload add " + cp2.getX() + " " + cp2.getZ() + " " + cp2.getX() + " " + cp2.getZ());
        fileCommands16.add("setblock " + cp2.getX() + " " + (cp2.getY() + 3) + " " + cp2.getZ() + " minecraft:air replace");
        fileCommands16.add("forceload remove " + cp2.getX() + " " + cp2.getZ() + " " + cp2.getX() + " " + cp2.getZ());
        fileCommands16.add("bossbar set minecraft:cp2 name \"CP2: " + cp2.getX() + ", " + cp2.getY() + ", " + cp2.getZ() + " (faster capping!)\"");
        fileCommands16.add("give @a[scores={ControlPoint1=14400..}] minecraft:splash_potion{CustomPotionEffects:[{Id:11,Duration:1200},{Id:24,Duration:1200}],CustomPotionColor:15462415,display:{Name:\"\\\"Hero of the First Control Point\\\"\",Lore:[\"Thank you for enabling the second Control Point! Good luck with winning the match!\"]}}");
        fileCommands16.add("function uhc:carepackage_" + carePackages.get(1).getName());


        FileData file16 = new FileData("second_controlpoint", fileCommands16);
        files.add(file16);

        ArrayList<String> fileCommands17 = new ArrayList<>();
        fileCommands17.add("setblock 8 2 0 minecraft:bedrock replace");
        fileCommands17.add("tellraw @a [\"\",{\"text\":\" ⎜ \",\"color\":\"gray\"},{\"text\":\"" + communityName + " UHC\",\"color\":\"gold\"},{\"text\":\" ⎜ \",\"color\":\"gray\"},{\"text\":\"TIME VICTORY HAS BEEN ACHIEVED! 3 MINUTES UNTIL THE FINAL DEATHMATCH\",\"color\":\"light_purple\"},{\"text\":\" ⎜ \",\"color\":\"gray\"}]");
        fileCommands17.add("fill 15 2 3 15 2 4 minecraft:bedrock");
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
                fileCommands19.add("execute if score @r[limit=1,gamemode=!spectator,team=" + team.getName() + "] ControlPoint"+ i +" > @p[scores={Admin=1}] Highscore" + i +" run setblock " + controlPoints.get(i - 1).getX() + " " + (controlPoints.get(i - 1).getY() + 1) + " " + controlPoints.get(i - 1).getZ() + " minecraft:"+team.getBossbarColor()+"_stained_glass replace");
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
            fileCommands20.add("give @a[gamemode=!spectator] minecraft:compass{display:{Name:\"{\\\"text\\\":\\\"" + carepackage.getDisplayName() + " available at " + carepackage.getX() + ", " + carepackage.getY() + ", " + carepackage.getZ() + "\\\"}\"}, LodestoneDimension:\"minecraft:overworld\",LodestoneTracked:0b,LodestonePos:{X:"+carepackage.getX()+",Y:"+carepackage.getY()+",Z:"+carepackage.getZ()+"}}");

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

        fileCommands25.add("give @p written_book{pages:['[\"\",{\"text\":\"The Diorite Experts\",\"bold\":true},{\"text\":\"\\\\nUltraHardCore S38\\\\nSa 07/11/2020\\\\n\\\\n\\\\n\",\"color\":\"reset\"},{\"text\":\"Rules & Information\",\"bold\":true,\"underlined\":true},{\"text\":\"\\\\n\\\\n\\\\n\\\\nWritten by: Snodog627\",\"color\":\"reset\"}]','[\"\",{\"text\":\"Contents\",\"bold\":true,\"underlined\":true},{\"text\":\"\\\\n\\\\n\",\"color\":\"reset\"},{\"text\":\"Introduction.......................3\",\"clickEvent\":{\"action\":\"change_page\",\"value\":3},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Go to Page 3\"}},{\"text\":\"\\\\n\"},{\"text\":\"Victory conditions........6\",\"clickEvent\":{\"action\":\"change_page\",\"value\":6},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Go to Page 6\"}},{\"text\":\"\\\\n\"},{\"text\":\"Rules........................................8\",\"clickEvent\":{\"action\":\"change_page\",\"value\":8},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Go to Page 8\"}},{\"text\":\"\\\\n\"},{\"text\":\"Utility.....................................10\",\"clickEvent\":{\"action\":\"change_page\",\"value\":10},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Go to Page 10\"}},{\"text\":\"\\\\n\"},{\"text\":\"Control Point.................11\",\"clickEvent\":{\"action\":\"change_page\",\"value\":11},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Go to Page 11\"}},{\"text\":\"\\\\n\"},{\"text\":\"Care Package...............16\",\"clickEvent\":{\"action\":\"change_page\",\"value\":16},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Go to Page 16\"}},{\"text\":\"\\\\n\"},{\"text\":\"Change log.....................19\",\"clickEvent\":{\"action\":\"change_page\",\"value\":19},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Go to Page 19\"}},{\"text\":\"\\\\n\"},{\"text\":\"Statistics...........................20\",\"clickEvent\":{\"action\":\"change_page\",\"value\":20},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Go to Page 20\"}},{\"text\":\"\\\\n \"}]','[\"\",{\"text\":\"Introduction\",\"bold\":true,\"underlined\":true},{\"text\":\"\\\\nWelcome to the 38th season of The Diorite Experts UltraHardCore! UHC is a survival/PvP game in Minecraft and is special because of its non-natural health regeneration. After the survival period where players get geared up it is time for the final PvP battle that can\",\"color\":\"reset\"}]','{\"text\":\"happen at any moment. The team of the last player standing wins the match. In addition to that, it is also possible to win the game by capturing Control Points. On the side, players can also gather loot by completing side quests: Care Package drops.\"}','[\"\",{\"text\":\"We hope you will have a wonderful time and good luck taking the crown!\\\\n\\\\n\\\\n\"},{\"text\":\"GLHF\",\"bold\":true},{\"text\":\"\\\\n~Bas, Luc, Wouter\\\\nS38 UHC Committee\\\\n \",\"color\":\"reset\"}]','[\"\",{\"text\":\"Victory Conditions\",\"bold\":true,\"underlined\":true},{\"text\":\"\\\\nThere are two ways to win UHC:\\\\n\",\"color\":\"reset\"},{\"text\":\"1.\",\"bold\":true},{\"text\":\" Be the last team standing\\\\n\",\"color\":\"reset\"},{\"text\":\"2. \",\"bold\":true},{\"text\":\"Earn 2400CP on the Control Points\\\\n\\\\n\\\\nCondition \",\"color\":\"reset\"},{\"text\":\"1.\",\"bold\":true},{\"text\":\" can only be claimed if condition \",\"color\":\"reset\"},{\"text\":\"2.\",\"bold\":true},{\"text\":\" has not been achieved yet.\",\"color\":\"reset\"}]','[\"\",{\"text\":\"The spoils:\",\"underlined\":true},{\"text\":\"\\\\nAs an appreciation of their skill, the winners of UltraHardCore will receive a role on Discord which elevates their spirits into divinity.\\\\n \",\"color\":\"reset\"}]','[\"\",{\"text\":\"Rules\",\"bold\":true,\"underlined\":true},{\"text\":\"\\\\nBanned items:\\\\n- Potion of regeneration,\\\\n- Potion of strength.\\\\n\\\\nA player cannot:\\\\n- trap a nether portal,\\\\n- share information that is not public after dying.\",\"color\":\"reset\"}]','[\"\",{\"text\":\"Other rules:\",\"underlined\":true},{\"text\":\"\\\\n- PvP is not allowed until the second day,\\\\n- Deaths due to PvE or glitches can be reversible, but respawns are handicapped,\\\\n- Players are not allowed to enter the spawn after the match has started.\",\"color\":\"reset\"}]','[\"\",{\"text\":\"Utility\",\"bold\":true,\"underlined\":true},{\"text\":\"\\\\n- Eternal day is enabled after 20 minutes,\\\\n- World size: 1500x1500\\\\n- A golden apple can be crafted like:\\\\n \\\\u0020 \\\\u0020 \\\\u0020 \\\\u0020 \\\\u0020\\\\u2610\\\\u2612\\\\u2610\\\\n \\\\u0020 \\\\u0020 \\\\u0020 \\\\u0020 \\\\u0020\\\\u2612\\\\u2611\\\\u2612\\\\n \\\\u0020 \\\\u0020 \\\\u0020 \\\\u0020 \\\\u0020\\\\u2610\\\\u2612\\\\u2610\\\\nwhere \\\\u2612 is a gold bar, \\\\u2611 is a playerhead.\",\"color\":\"reset\"}]','[\"\",{\"text\":\"Control Point\",\"bold\":true,\"underlined\":true},{\"text\":\"\\\\nThere are two Control Points in this game. When a player is within 5 blocks from the Control Point, they are awarded a CP score every second.\\\\n\\\\nCP1 awards 2CP per second and CP2 awards 3CP per second.\",\"color\":\"reset\"}]','[\"\",{\"text\":\"CP1 is enabled after 30 minutes of game time. After a team accumulates 720CP on CP1, CP2 will also be enabled.\\\\n\\\\nThe coordinates of both CP will be revealed after 30 minutes of game time.\\\\n\\\\n\"},{\"text\":\"Tip: When CP2 has just\",\"italic\":true}]','[\"\",{\"text\":\"been enabled it is faster to immediately control CP2!\",\"italic\":true},{\"text\":\"\\\\n\\\\nThe team that manages to first score 720CP receives a splash potion of resistance.\\\\n\\\\nControl Points are recognized by the beam of an end crystal.\",\"color\":\"reset\"}]','[\"\",{\"text\":\"When a team reaches 2400CP, they win the game. The remaining teams get 3 minutes to prepare and will afterwards be spread into a shrinking area for a final battle.\\\\n\\\\n\"},{\"text\":\"Note: It is not possible to capture a Control \",\"italic\":true}]','{\"text\":\"Point when players of other teams are present on that CP.\",\"italic\":true}','[\"\",{\"text\":\"Care Package\",\"bold\":true,\"underlined\":true},{\"text\":\"\\\\nThis game contains two Care Packages:\\\\n- \",\"color\":\"reset\"},{\"text\":\"Enchanting\",\"underlined\":true,\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://media.discordapp.net/attachments/505386630736248834/746784653922533506/unknown.png\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Click for preview of the contents\"}},{\"text\":\"\\\\n- \",\"color\":\"reset\"},{\"text\":\"Anti-ControlPoint\",\"underlined\":true,\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://media.discordapp.net/attachments/505386630736248834/746784937646096404/unknown.png\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Click for preview of the contents\"}},{\"text\":\"\\\\n\\\\nThe enchanting Care Package is deployed after 20 minutes of gametime. Its purpose is for those who are either unlucky or bored with caving. Its\",\"color\":\"reset\"}]','{\"text\":\"coordinates will be made available through a bossbar which shows up every 30 seconds.\\\\n\\\\nThe anti-ControlPoint Care Package is deployed after CP2 is enabled and contains all kind of havoc that makes CP capturers sweat. Its coordinates will be displayed in the\"}','[\"\",{\"text\":\"same aforementioned bossbar.\\\\n\\\\n\"},{\"text\":\"Tip: If you are not doing so well or late to the Control Point race, you might want to check these out!\",\"italic\":true}]','[\"\",{\"text\":\"Change log\",\"bold\":true,\"underlined\":true},{\"text\":\"\\\\n- Removed Traitor faction (will be tested in again in December!)\\\\n- CP bug fixed\\\\n- Team generation is now done through an algorithm made in MATLAB\",\"color\":\"reset\"}]','[\"\",{\"text\":\"Statistics\",\"bold\":true,\"underlined\":true},{\"text\":\"\\\\nFor more statistics check out \",\"color\":\"reset\"},{\"text\":\"/r/TheDioriteExpertsUHC\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://old.reddit.com/r/TheDioriteExpertsUHC/\"}},{\"text\":\"\\\\n\\\\n\"},{\"text\":\"Current Rankings:\",\"bold\":true},{\"text\":\"\\\\n1. Bobdafish (166)\\\\n2. Snodog627 (115)\\\\n3. TheDinoGame (113)\\\\n4. jonmo0105 (99)\\\\n5. Thurian (72)\\\\n\\\\n\\\\n\\\\n \",\"color\":\"reset\"}]','[\"\",{\"text\":\"Previous winners:\",\"bold\":true},{\"text\":\"\\\\n\",\"color\":\"reset\"},{\"text\":\"S33\",\"bold\":true},{\"text\":\": jonmo0105, PerfidyIsKey, Snodog627\\\\n\",\"color\":\"reset\"},{\"text\":\"S34\",\"bold\":true},{\"text\":\": jonmo0105\\\\n\",\"color\":\"reset\"},{\"text\":\"S35\",\"bold\":true},{\"text\":\": Kakarot057, PerfidyIsKey\\\\n\",\"color\":\"reset\"},{\"text\":\"S36\",\"bold\":true},{\"text\":\": Bobdafish, TheDinoGame, W0omy\\\\n\",\"color\":\"reset\"},{\"text\":\"S37:\",\"bold\":true},{\"text\":\" Bobdafish, TheDinoGame\\\\n\\\\n\\\\n\\\\n \",\"color\":\"reset\"}]','[\"\",{\"text\":\"Most Wins:\",\"bold\":true},{\"text\":\"\\\\n1. Snodog627 (18)\\\\n2. Mr9Madness (11)\\\\n3. PR0BA (5)\\\\n4. W0omy (5)\\\\n5. Thurian (4)\\\\n\\\\n\",\"color\":\"reset\"},{\"text\":\"Most Kills:\",\"bold\":true},{\"text\":\"\\\\n1. Snodog627 (40)\\\\n2. Mr9Madness (17)\\\\n3. TheDinoGame (9)\\\\n4. Tiba101 (8)\\\\n5. jonmo0105 (8)\\\\n\\\\n\\\\n\\\\n\\\\n \",\"color\":\"reset\"}]'],title:\"The Diorite Experts UHC RuleBook\",author:Snodog627,display:{Lore:[\"Information and rules on the 38th season of The Diorite Experts UltraHardCore\"]}}");
        fileCommands25.add("give @p minecraft:compass");
        FileData file25 = new FileData("give_instructions", fileCommands25);
        files.add(file25);

        ArrayList<String> fileCommands26 = new ArrayList<>();

        fileCommands26.add("execute as @a[scores={Quits=1..}] at @s run function uhc:give_instructions");
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
        fileCommands27.add("setblock 11 2 0 minecraft:redstone_block destroy");

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
                fileCommands29.add("execute as @r[limit=1,gamemode=!spectator] run scoreboard players operation @s[team=" + t.getName() + "] ControlPoint"+ i +" > @p[scores={Admin=1}] CP" + i + t.getName());
            }
        }

        for (Team t : teams) {
            fileCommands29.add("execute as @r[limit=1,gamemode=!spectator,x=" + (cp1.getX() - 6) + ",y=" + (cp1.getY() - 1) + ",z=" + (cp1.getZ() - 6) + ",dx=12,dy=12,dz=12,team="+ t.getName() + "] run scoreboard players operation @p[scores={Admin=1}] CP1" + t.getName() + " > @p[scores={Admin=1}] CP2" + t.getName());
            fileCommands29.add("execute as @r[limit=1,gamemode=!spectator,x=" + (cp2.getX() - 6) + ",y=" + (cp2.getY() - 1) + ",z=" + (cp2.getZ() - 6) + ",dx=12,dy=12,dz=12,team="+ t.getName() + "] run scoreboard players operation @p[scores={Admin=1}] CP2" + t.getName() + " > @p[scores={Admin=1}] CP1" + t.getName());
        }


        FileData file29 = new FileData("team_score", fileCommands29);
        files.add(file29);

        ArrayList<String> fileCommands30 = new ArrayList<>();

        for (ControlPoint cp : controlPoints) {
            fileCommands30.add("forceload add " + cp.getX() + " " + cp.getZ() + " " + cp.getX() + " " + cp.getZ());
            fileCommands30.add("setblock " + cp.getX() + " " + (cp.getY() + 11) + " " + cp.getZ() + " " + "minecraft:structure_block[mode=load]{metadata:\"\",mirror:\"NONE\",ignoreEntities:1b,powered:0b,seed:0L,author:\"?\",rotation:\"NONE\",posX:-6,mode:\"LOAD\",posY:-13,sizeX:13,posZ:-6,integrity:1.0f,showair:0b,name:\"minecraft:controlpoint\",sizeY:14,sizeZ:13,showboundingbox:1b} replace");
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
        fileCommands33.add("scoreboard players add WorldLoad 1");
        fileCommands33.add("execute if entity @p[scores={WorldLoad=400..}] run spreadplayers 0 0 5 " + worldSize + " false @a");
        fileCommands33.add("execute if entity @p[scores={WorldLoad=400..}] run scoreboard players reset @a WorldLoad");

        FileData file33 = new FileData("world_pre_load",fileCommands33);
        files.add(file33);
    }

}
