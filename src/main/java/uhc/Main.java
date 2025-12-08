package uhc;

import uhc.core.Datapack;
import uhc.core.Generator;
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

    // Your custom namespace where functions reside
    private static final String CUSTOM_NAMESPACE = "uhc_core_pack";

    public static void main(String[] args) {

        // 1. Setup Datapack
        Datapack datapack = new Datapack("Example pack for Minecraft 1.21.9");

        // 2. Define Modules
        List<DatapackModule> modules = List.of(
                new InitializationModule(),
                new GameLoopModule()
        );

        // 3. Register Modules
        // We pass the whole datapack + the name of our custom namespace
        for (DatapackModule module : modules) {
            module.register(datapack, CUSTOM_NAMESPACE);
        }

        // 4. Generate
        Generator generator = new Generator();
        generator.setPackFolderName(DATAPACK_FOLDER_NAME);

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