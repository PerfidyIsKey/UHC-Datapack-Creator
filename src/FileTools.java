import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class FileTools {

    private String version;
    private String dataPackLocation;
    private String dataPackName;
    private String worldLocation;

    public FileTools (String version, String dataPackLocation, String dataPackName, String worldLocation) {
        this.version = version;
        this.dataPackLocation = dataPackLocation;
        this.dataPackName = dataPackName;
        this.worldLocation = worldLocation;
    }

    public ArrayList<FileData> makeRecipeFiles() {
        ArrayList<Recipe> recipes = new ArrayList<>();
        ArrayList<FileData> files = new ArrayList<>();

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
        return files;
    }

    public ArrayList<String> generateLootTable(ArrayList<LootTableEntry> lootEntry) {
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
        return fileCommands;
    }

    public ArrayList<String> GetLinesFromFile(String pathname) {
        ArrayList<String> fileLines = new ArrayList<>();
        try {
            File myObj = new File(pathname);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                fileLines.add(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return fileLines;
    }

    public void createDatapack(ArrayList<FileData> files, String fileLocation) throws IOException {
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
                        updateAllFiles(files, fileLocation);
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
            updateAllFiles(files, fileLocation);
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

    public void updateAllFiles(ArrayList<FileData> files, String fileLocation) {
        for (FileData f : files) {
            try {
                writeFile(f, fileLocation);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void writeFile(FileData fileData, String fileLocation) throws IOException {
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
}
