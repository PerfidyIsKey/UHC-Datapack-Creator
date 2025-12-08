package nbt.tags;

import nbt.NBTTag;
import nbt.TagType;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class DoubleTag implements NBTTag {
    private String name;
    private double value;

    public DoubleTag() {}
    public DoubleTag(String name, double value) { this.name = name; this.value = value; }

    @Override public byte getTypeId() { return TagType.TAG_DOUBLE; }
    @Override public String getName() { return name; }
    @Override public void setName(String name) { this.name = name; }

    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }

    @Override public void writePayload(DataOutput out) throws IOException { out.writeDouble(value); }

    @Override public void readPayload(DataInput in, int depth) throws IOException { value = in.readDouble(); }
}