package FileGeneration;

import FileGeneration.FileData;
import FileGeneration.Recipe;
import HelperClasses.PlayerConnection;
import TeamGeneration.Season;

import javax.swing.*;
import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileTools {

    private String version;
    private String dataPackLocation;
    private String dataPackName;
    private String worldLocation;

    public FileTools() {
    }

    public FileTools(String version, String dataPackLocation, String dataPackName, String worldLocation) {
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

        String[] grid2 = {" ", "1", " ", "1", "2", "1", " ", "1", " "};
        ArrayList<String> keys2 = new ArrayList<>();
        keys2.add("gold_ingot");
        keys2.add("player_head");
        FileGeneration.Recipe recipe2 = new FileGeneration.Recipe("crafting_shaped", grid2, keys2, "golden_apple", 1);
        //recipes.add(recipe2);

        for (Recipe r : recipes) {
            ArrayList<String> fileCommands = new ArrayList<>();
            fileCommands.add("{");
            fileCommands.add("  \"type\": \"" + r.getType() + "\",");
            fileCommands.add("  \"category\": \"equipment\",");
            fileCommands.add("  \"pattern\": [");
            fileCommands.add("    \"" + r.getGrid()[0] + r.getGrid()[1] + r.getGrid()[2] + "\",");
            fileCommands.add("    \"" + r.getGrid()[3] + r.getGrid()[4] + r.getGrid()[5] + "\",");
            fileCommands.add("    \"" + r.getGrid()[6] + r.getGrid()[7] + r.getGrid()[8] + "\"");
            fileCommands.add("],");
            fileCommands.add("\"key\": {");
            int counter = 1;
            String keyEntry;
            for (String key : r.getKeys()) {
                keyEntry = "  \"" + counter + "\": \"" + key + "\"";
                if (r.getKeys().size() > counter) {
                    keyEntry += ",";
                }
                fileCommands.add(keyEntry);

                counter++;
            }
            fileCommands.add("},");
            fileCommands.add("  \"result\": {");
            fileCommands.add("    \"id\": \"" + r.getResultItem() + "\",");
            fileCommands.add("    \"count\": " + r.getResultAmount());
            fileCommands.add("  }");
            fileCommands.add("}");

            FileData fileData = new FileData(r.getResultItem() + r.getResultAmount(), fileCommands, "recipe");
            files.add(fileData);
        }
        return files;
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

    public String getContentOutOfFile(String pathName, String rowName) {
        ArrayList<String> lines = GetLinesFromFile(pathName);
        for (String line : lines) {
            if (line.startsWith(rowName)) {
                line = line.replace(rowName + ':', "");
                return line;
            }
        }
        return "";
    }

    public String[] splitLineOnComma(String line) {
        return line.split(",");
    }

    public void createDatapack(ArrayList<FileData> files, String fileLocation) throws IOException {


        File file = new File(dataPackLocation);
        file.mkdir();

        File file2 = new File(dataPackLocation + dataPackName);
        if (file2.mkdir()) {
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
                    File functions = new File(dataPackLocation + dataPackName + "\\data\\uhc\\function");
                    if (!functions.mkdir()) {
                        System.out.println("No functions dir");
                    }
                    File recipes = new File(dataPackLocation + dataPackName + "\\data\\uhc\\recipe");
                    if (!recipes.mkdir()) {
                        System.out.println("No recipes dir");
                    }
                    File lootTables = new File(dataPackLocation + dataPackName + "\\data\\uhc\\loot_table");
                    if (lootTables.mkdir()) {
                        updateAllFiles(files, fileLocation);
                    } else {
                        System.out.println("No lootTables dir");
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

    private static void copyDirectory(String sourceDirectoryLocation, String destinationDirectoryLocation)
            throws IOException {
        Files.walk(Paths.get(sourceDirectoryLocation))
                .forEach(source -> {
                    Path destination = Paths.get(destinationDirectoryLocation, source.toString()
                            .substring(sourceDirectoryLocation.length()));

                    try {
                        try {
                            Files.copy(source, destination);
                        } catch (FileAlreadyExistsException e) {
                            System.out.println("File already exists at location");
                        }
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
            file = new File(fileLocation + "recipe\\" + fileData.getName() + ".json");
        } else if (fileData.getType().equals("loot_table")) {
            file = new File(fileLocation + "loot_table\\" + fileData.getName() + ".json");
        } else {
            file = new File(fileLocation + "function\\" + fileData.getName() + ".mcfunction");
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
            writer.newLine();
            writer.write("#Or take a look at: https://github.com/PerfidyIsKey/UHC-Datapack-Creator");
        }
        writer.close();
        System.out.println("File \"" + fileData.getName() + "\" Updated.");
    }

    public void createPlayerConnection(String fileLocation, String fileName, PlayerConnection playerConnection) throws IOException {
        File file = new File(fileLocation + fileName + ".txt");
        ArrayList<String> fileContents = getFileContents(file);
        double seasonID = playerConnection.getSeasons().get(0).getID();
        fileContents.add(playerConnection.getPlayer1().getInternalID() + "," + playerConnection.getPlayer2().getInternalID() + ",[" + seasonID +"]");
        writeFileContents(file, fileContents);
    }

    public void updatePlayerConnections(String fileLocation, String fileName, ArrayList<PlayerConnection> playerConnections) throws IOException {
        File file = new File(fileLocation + fileName + ".txt");
        ArrayList<String> fileContents = getFileContents(file);
        ArrayList<String> outputFileContents = new ArrayList<>();
        for (String s : fileContents) {
            for (PlayerConnection p : playerConnections) {
                if (s.startsWith(p.getPlayer1().getInternalID() + "," + p.getPlayer2().getInternalID())) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (Season season: p.getSeasons()) {
                        stringBuilder.append(season.getID());
                        stringBuilder.append(",");
                    }
                    stringBuilder.setLength(stringBuilder.length() - 1);
                    s = p.getPlayer1().getInternalID() + "," + p.getPlayer2().getInternalID() + ",[" + stringBuilder + "]";
                }
            }
            outputFileContents.add(s);
        }
        writeFileContents(file, outputFileContents);

        System.out.println("File \"" + fileName + "\" Updated.");
    }

    public void makeFileCopy(String fileLocation, String fileName) throws IOException {
        File file = new File(fileLocation + fileName + ".txt");
        File fileCopy = new File(fileLocation + fileName + "_copy.txt");
        ArrayList<String> fileContents = getFileContents(file);
        writeFileContents(fileCopy, fileContents);
    }

    private ArrayList<String> getFileContents(File file) throws IOException {
        Scanner scanner = new Scanner(file);
        ArrayList<String> fileContents = new ArrayList<>();
        while (scanner.hasNextLine()) {
            fileContents.add(scanner.nextLine());
        }
        scanner.close();
        return fileContents;
    }

    private void writeFileContents(File file, List<String> fileContents) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        for (String s : fileContents) {
            writer.write(s);
            writer.newLine();
        }
        writer.close();
    }
}
