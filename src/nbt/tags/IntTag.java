package nbt.tags;

import nbt.NBTTag;
import nbt.TagType;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class IntTag implements NBTTag {
    private String name;
    private int value;

    public IntTag() {}
    public IntTag(String name, int value) { this.name = name; this.value = value; }

    @Override public byte getTypeId() { return TagType.TAG_INT; }
    @Override public String getName() { return name; }
    @Override public void setName(String name) { this.name = name; }

    public int getValue() { return value; }
    public void setValue(int value) { this.value = value; }

    @Override public void writePayload(DataOutput out) throws IOException { out.writeInt(value); }

    @Override public void readPayload(DataInput in, int depth) throws IOException { value = in.readInt(); }
}