package uhc.data.nbt.tags;

import uhc.data.nbt.NBTTag;
import uhc.data.nbt.TagType;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class LongTag implements NBTTag {
    private String name;
    private long value;

    public LongTag() {}
    public LongTag(String name, long value) { this.name = name; this.value = value; }

    @Override public byte getTypeId() { return TagType.TAG_LONG; }
    @Override public String getName() { return name; }
    @Override public void setName(String name) { this.name = name; }

    public long getValue() { return value; }
    public void setValue(long value) { this.value = value; }

    @Override public void writePayload(DataOutput out) throws IOException { out.writeLong(value); }

    @Override public void readPayload(DataInput in, int depth) throws IOException { value = in.readLong(); }
}