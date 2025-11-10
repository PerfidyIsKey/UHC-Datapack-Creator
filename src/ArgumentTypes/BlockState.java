package ArgumentTypes;

import Enums.Block;

public class BlockState implements ArgumentType {
    private final Block blockId;
    private String blockStates; // e.g. "[facing=north]"
    private String dataTags;    // e.g. "{CustomName:\"MyChest\"}"

    public BlockState(Block blockId, String blockStates, String dataTags) {
        this.blockId = blockId;
        this.blockStates = blockStates;
        this.dataTags = dataTags;
    }

    public BlockState(Block blockId) {
        this.blockId = blockId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(blockId.toString());
        if (blockStates != null) {
            sb.append("[").append(blockStates).append("]");
        }
        if (dataTags != null) {
            sb.append("{").append(dataTags).append("}");
        }

        return sb.toString();
    }

    public void Sync() {
    }
}
