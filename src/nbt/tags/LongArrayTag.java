package nbt.tags;

import nbt.NBTTag;
import nbt.TagType;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class LongArrayTag implements NBTTag {
    private String name;
    private long[] value;

    public LongArrayTag() {}
    public LongArrayTag(String name, long[] value) { this.name = name; this.value = value; }

    @Override public byte getTypeId() { return TagType.TAG_LONG_ARRAY; }
    @Override public String getName() { return name; }
    @Override public void setName(String name) { this.name = name; }

    public long[] getValue() { return value; }
    public void setValue(long[] value) { this.value = value; }

    @Override public void writePayload(DataOutput out) throws IOException {
        out.writeInt(value == null ? 0 : value.length);
        if (value != null) for (long v : value) out.writeLong(v);
    }

    @Override public void readPayload(DataInput in, int depth) throws IOException {
        int len = in.readInt();
        value = new long[len];
        for (int i = 0; i < len; i++) value[i] = in.readLong();
    }
}