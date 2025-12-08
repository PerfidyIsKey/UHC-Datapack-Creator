package uhc;

import uhc.core.Datapack;
import uhc.core.Generator;
import uhc.core.Namespace;
import uhc.modules.DatapackModule;
import uhc.modules.GameLoopModule;
import uhc.modules.InitializationModule;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    private static final String OUTPUT_DIR_ROOT = "Server/world/datapacks";
    private static final String DATAPACK_FOLDER_NAME = "uhc_datapack";
    private static final String DATAPACK_NAMESPACE = "uhc_core_pack";
    private static final String DEFAULT_NAMESPACE = "minecraft"; // The Minecraft namespace (e.g., "uhc_core_pack:load")

    public static void main(String[] args) {

        // 1. Setup
        Datapack datapack = new Datapack("Example pack for Minecraft 1.21.9");
        Namespace namespace = datapack.getOrCreateNamespace(DATAPACK_NAMESPACE);

        // 2. Register Modules
        // As your project grows, you just add new classes to this list.
        List<DatapackModule> modules = List.of(
                new InitializationModule(),
                new GameLoopModule()
                // new CombatModule(),
                // new ScenariosModule(),
        );

        for (DatapackModule module : modules) {
            module.register(namespace);
        }

        // 3. Generate
        Generator generator = new Generator();
        generator.setPackFolderName(DATAPACK_FOLDER_NAME);

        // OS-INDEPENDENT PATH HANDLING
        // Paths.get() automatically uses '\' for Windows and '/' for Mac/Linux
        Path absolutePath = Paths.get(OUTPUT_DIR_ROOT).toAbsolutePath();

        System.out.println("Generating datapack to: " + absolutePath);

        try {
            generator.generate(datapack, absolutePath.toString());
            System.out.println("Generation complete.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}