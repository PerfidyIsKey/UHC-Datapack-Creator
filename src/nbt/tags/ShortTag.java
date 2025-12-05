package nbt.tags;

import nbt.NBTTag;
import nbt.TagType;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class ShortTag implements NBTTag {
    private String name;
    private short value;

    public ShortTag() {}
    public ShortTag(String name, short value) { this.name = name; this.value = value; }

    @Override public byte getTypeId() { return TagType.TAG_SHORT; }
    @Override public String getName() { return name; }
    @Override public void setName(String name) { this.name = name; }

    public short getValue() { return value; }
    public void setValue(short value) { this.value = value; }

    @Override public void writePayload(DataOutput out) throws IOException { out.writeShort(value); }

    @Override public void readPayload(DataInput in, int depth) throws IOException { value = in.readShort(); }
}