package uhc.data.nbt.tags;

import uhc.data.nbt.NBTTag;
import uhc.data.nbt.TagType;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class ByteTag implements NBTTag {
    private String name;
    private byte value;

    public ByteTag() {}
    public ByteTag(String name, byte value) { this.name = name; this.value = value; }

    @Override public byte getTypeId() { return TagType.TAG_BYTE; }
    @Override public String getName() { return name; }
    @Override public void setName(String name) { this.name = name; }

    public byte getValue() { return value; }
    public void setValue(byte value) { this.value = value; }

    @Override public void writePayload(DataOutput out) throws IOException { out.writeByte(value); }

    @Override public void readPayload(DataInput in, int depth) throws IOException { value = in.readByte(); }
}