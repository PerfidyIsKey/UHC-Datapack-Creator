package nbt.tags;

import nbt.NBTTag;
import nbt.TagType;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class StringTag implements NBTTag {
    private String name;
    private String value;

    public StringTag() {}
    public StringTag(String name, String value) { this.name = name; this.value = value; }

    @Override public byte getTypeId() { return TagType.TAG_STRING; }
    @Override public String getName() { return name; }
    @Override public void setName(String name) { this.name = name; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    @Override public void writePayload(DataOutput out) throws IOException { out.writeUTF(value == null ? "" : value); }

    @Override public void readPayload(DataInput in, int depth) throws IOException { value = in.readUTF(); }
}