package uhc.modules;

import uhc.core.Namespace;

public interface DatapackModule {
    /**
     * Registers functions, tags, and other components to the provided namespace.
     */
    void register(Namespace namespace);
}