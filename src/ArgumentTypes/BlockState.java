package ArgumentTypes;

import Enums.Block;

public class BlockState implements ArgumentType {
    private final Block blockId;
    private final String blockStates; // e.g. "[facing=north]"
    private final String dataTags;    // e.g. "{CustomName:\"MyChest\"}"

    public BlockState(Block blockId, String blockStates, String dataTags) {
        this.blockId = blockId;
        this.blockStates = blockStates == null ? "" : blockStates;
        this.dataTags = dataTags == null ? "" : dataTags;
    }

    public BlockState(Block blockId) {
        this.blockId = blockId;
        this.blockStates = "";
        this.dataTags = "";
    }

    @Override
    public String toString() {
        return blockId + blockStates + dataTags;
    }

    public void Sync() {
    }
}
