package uhc.data.nbt.tags;


import uhc.data.nbt.NBTTag;
import uhc.data.nbt.TagType;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class FloatTag implements NBTTag {
    private String name;
    private float value;

    public FloatTag() {}
    public FloatTag(String name, float value) { this.name = name; this.value = value; }

    @Override public byte getTypeId() { return TagType.TAG_FLOAT; }
    @Override public String getName() { return name; }
    @Override public void setName(String name) { this.name = name; }

    public float getValue() { return value; }
    public void setValue(float value) { this.value = value; }

    @Override public void writePayload(DataOutput out) throws IOException { out.writeFloat(value); }

    @Override public void readPayload(DataInput in, int depth) throws IOException { value = in.readFloat(); }
}