package commands.item;

import arguments.BlockPos;

public class ItemTargetBlock implements ItemTarget {
    private final BlockPos pos;
    public ItemTargetBlock(BlockPos pos) { this.pos = pos; }
    @Override
    public String getCommandString() {
        return "block " + pos.toString();
    }
}