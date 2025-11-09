package Commands;

import ArgumentTypes.*;
import Enums.FillMode;

public class Fill {
    private final BlockPos from;
    private final BlockPos to;
    private final BlockState block;
    private FillMode mode;
    private BlockPredicate filter; // Only for REPLACE variant

    private Fill(BlockPos from, BlockPos to, BlockState block) {
        this.from = from;
        this.to = to;
        this.block = block;
    }

    public static Fill create(BlockPos from, BlockPos to, BlockState block) {
        return new Fill(from, to, block);
    }

    public Fill mode(FillMode mode) {
        this.mode = mode;
        return this;
    }

    public Fill filter(BlockPredicate filter) {
        this.filter = filter;
        return this;
    }

    public String build() {
        StringBuilder sb = new StringBuilder("fill ");
        sb.append(from).append(" ").append(to).append(" ").append(block);

        if (filter != null) {
            sb.append(" replace ").append(filter);
            if (mode != null) {
                if (mode == FillMode.REPLACE || mode == FillMode.KEEP) {
                    throw new IllegalArgumentException("Invalid fill mode: " + mode + ". Use [outline|hollow|destroy|strict].");
                } else {
                    sb.append(" ").append(mode);
                }
            }

        } else {
            if (mode != null) {
                sb.append(" ").append(mode);
            }
        }

        return sb.toString();
    }
}