package commands;

import arguments.*;
import Enums.SetMode;

public class SetBlock {
    private final BlockPos pos;
    private final BlockState block;
    private SetMode mode;

    private SetBlock(BlockPos pos, BlockState block) {
        this.pos = pos;
        this.block = block;
    }

    public static SetBlock create(BlockPos pos, BlockState block) { return new SetBlock(pos, block); }

    public SetBlock mode(SetMode mode) {
        this.mode = mode;
        return this;
    }

    public String build() {
        StringBuilder sb = new StringBuilder("setblock ");
        sb.append(pos).append(" ").append(block);

        if (mode != null) {
            sb.append(" ").append(mode);
        }

        return sb.toString();
    }

}
