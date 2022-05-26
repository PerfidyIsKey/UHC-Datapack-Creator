package HelperClasses;

import Enums.BlockType;

public class Block extends Condition {

    public Block(int x, int y, int z, BlockType blockType) {
        super("block " + x + " " + y + " " + z + " minecraft:" + blockType);
    }
}
