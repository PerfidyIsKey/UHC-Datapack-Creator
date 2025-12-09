package uhc.core;

/**
 * Static configuration class holding all essential, high-level metadata
 * required by the datapack and the generator.
 */
public class DatapackConfig {

    // --- PACK METADATA (For pack.mcmeta) ---
    /**
     * The minimum resource pack format version required (e.g., 88 for Minecraft 1.21.9).
     * This value is used for both min_format and max_format.
     */
    public static final int PACK_FORMAT = 88;

    /**
     * The user-friendly description displayed in the Minecraft resource pack list.
     */
    public static final String PACK_DESCRIPTION = "Example pack for Minecraft 1.21.9";

    // --- FILE SYSTEM CONFIGURATION (For Generator) ---
    /**
     * The name of the custom namespace where all primary functions and resources reside
     * (e.g., used in data/uhc_core_pack/).
     */
    public static final String CUSTOM_NAMESPACE = "uhc_core_pack";

    /**
     * The name of the top-level folder generated inside the datapacks directory.
     */
    public static final String DATAPACK_FOLDER_NAME = "uhc_datapack";

    /**
     * The root directory where the final datapack folder will be placed.
     */
    public static final String OUTPUT_DIR_ROOT = "Server/world/datapacks";
}