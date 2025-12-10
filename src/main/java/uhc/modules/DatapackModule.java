package uhc.modules;

import uhc.core.Datapack;

/**
 * 🧱 **Datapack Module Contract**
 * <p>
 * Defines the contract for all functional units or feature sets within the datapack
 * generation project (e.g., an Initialization Module, a Scoreboard Module, etc.).
 * </p>
 * Implementing classes must contain the encapsulated logic necessary to create and register
 * all associated {@code DatapackComponent}s (functions, tags, advancements, etc.)
 * into the correct {@code Namespace} objects provided by the {@code Datapack} container.
 */
public interface DatapackModule {

    /**
     * Executes the module's component registration logic.
     * <p>
     * This is the entry point for the module to instantiate its defined {@code DatapackComponent}s
     * and add them to the appropriate namespaces (custom or 'minecraft').
     * </p>
     * @param datapack The main {@code Datapack} container. Used to retrieve or create
     * namespaces (e.g., 'minecraft' for engine tags, and the custom namespace for functions).
     * @param namespaceName The {@code String} name of the custom namespace
     * (e.g., {@code "uhc_core_pack"}), typically sourced from {@code DatapackConfig}.
     */
    void register(Datapack datapack, String namespaceName);
}