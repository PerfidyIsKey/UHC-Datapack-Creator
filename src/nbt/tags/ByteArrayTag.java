package nbt.tags;

import nbt.NBTTag;
import nbt.TagType;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class ByteArrayTag implements NBTTag {
    private String name;
    private byte[] value;

    public ByteArrayTag() {}
    public ByteArrayTag(String name, byte[] value) { this.name = name; this.value = value; }

    @Override public byte getTypeId() { return TagType.TAG_BYTE_ARRAY; }
    @Override public String getName() { return name; }
    @Override public void setName(String name) { this.name = name; }

    public byte[] getValue() { return value; }
    public void setValue(byte[] value) { this.value = value; }

    @Override public void writePayload(DataOutput out) throws IOException {
        out.writeInt(value == null ? 0 : value.length);
        if (value != null) out.write(value);
    }

    @Override public void readPayload(DataInput in, int depth) throws IOException {
        int len = in.readInt();
        value = new byte[len];
        in.readFully(value);
    }
}