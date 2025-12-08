package uhc;

import uhc.core.Datapack;
import uhc.core.Generator;
import uhc.core.Namespace;
import uhc.components.functions.Function;
import uhc.components.tags.FunctionTag; // New import

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    /**
     * The target output directory where the final datapack folder will be placed.
     */
    private static final String OUTPUT_DIR_ROOT = "Server/world/datapacks";
    private static final String DATAPACK_FOLDER_NAME = "uhc_datapack"; // The folder containing pack.mcmeta
    private static final String DATAPACK_NAMESPACE = "uhc_core_pack"; // The Minecraft namespace (e.g., "uhc_core_pack:load")
    private static final String DEFAULT_NAMESPACE = "minecraft"; // The Minecraft namespace (e.g., "uhc_core_pack:load")

    public static void main(String[] args) {

        // 1. **Initialize Core Structures**
        // Pass the new description string to the constructor.
        Datapack datapack = new Datapack("Example pack for Minecraft 1.21.9");
        Namespace uhcNamespace = datapack.getOrCreateNamespace(DATAPACK_NAMESPACE);

        // --- 2. Create and Populate Functions ---

        // Function 1: Load (runs once)
        String loadFunctionName = "init/load";
        Function loadFunction = new Function(loadFunctionName);
        loadFunction.addCommand("say [UHC] Datapack initializing! Version: 1.0");
        loadFunction.addCommand("scoreboard objectives add uhc_status dummy");
        loadFunction.addCommand("function " + DATAPACK_NAMESPACE + ":loop/tick"); // Schedule first tick
        uhcNamespace.addComponent(loadFunction);

        // Function 2: Tick (runs continuously)
        String tickFunctionName = "loop/tick";
        Function tickFunction = new Function(tickFunctionName);
        tickFunction.addCommand("# Place all repeating, time-based commands here");
        tickFunction.addCommand("execute as @a at @s run particle minecraft:cloud ~ ~ ~ 0.1 0.1 0.1 0 1 normal");
        uhcNamespace.addComponent(tickFunction);

        // --- 3. Create Function Tags (Auto-Execution) ---

        // Tag 1: 'load' tag (runs once on world load)
        FunctionTag loadTag = new FunctionTag("load");
        // The ID is constructed as "namespace:path/to/function"
        loadTag.addFunction(DEFAULT_NAMESPACE + ":" + loadFunctionName);
        uhcNamespace.addComponent(loadTag);

        // Tag 2: 'tick' tag (runs every game tick)
        FunctionTag tickTag = new FunctionTag("tick");
        tickTag.addFunction(DEFAULT_NAMESPACE + ":" + tickFunctionName);
        uhcNamespace.addComponent(tickTag);

        // --- 4. Generate the Files ---
        Generator generator = new Generator();
        generator.setPackFolderName(DATAPACK_FOLDER_NAME);

        // Use a Path object to handle OS-specific path separators correctly for the root output
        Path absolutePath = Paths.get(OUTPUT_DIR_ROOT).toAbsolutePath();

        System.out.println("Starting Datapack generation...");
        System.out.println("Target Directory: " + absolutePath);

        try {
            generator.generate(datapack, absolutePath.toString());
        } catch (IOException e) {
            System.err.println("FATAL ERROR: Could not generate datapack.");
            e.printStackTrace();
        }
    }
}