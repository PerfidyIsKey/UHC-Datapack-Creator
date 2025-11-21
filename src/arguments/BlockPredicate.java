package arguments;
import Enums.*;

public class BlockPredicate {
    private final String block_id;
    private String block_states;
    private String data_tags;

    public BlockPredicate(Block id) {
        this.block_id = id.toString();
    }

    public BlockPredicate(Block id, String states, String tags) {
        this.block_id = id.toString();
        this.block_states = states;
        this.data_tags = tags;
    }

    public BlockPredicate(RegistryTag id) {
        this.block_id = id.toString();
    }

    public BlockPredicate(RegistryTag id, String states, String tags) {
        this.block_id = id.toString();
        this.block_states = states;
        this.data_tags = tags;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(block_id);
        if (block_states != null) {
            sb.append("[").append(block_states).append("]");
        }
        if (data_tags != null) {
            sb.append("{").append(data_tags).append("}");
        }
        return sb.toString();
    }
}
