package uhc.data.nbt.tags;

import uhc.data.nbt.NBTTag;
import uhc.data.nbt.TagType;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class IntArrayTag implements NBTTag {
    private String name;
    private int[] value;

    public IntArrayTag() {}
    public IntArrayTag(String name, int[] value) { this.name = name; this.value = value; }

    @Override public byte getTypeId() { return TagType.TAG_INT_ARRAY; }
    @Override public String getName() { return name; }
    @Override public void setName(String name) { this.name = name; }

    public int[] getValue() { return value; }
    public void setValue(int[] value) { this.value = value; }

    @Override public void writePayload(DataOutput out) throws IOException {
        out.writeInt(value == null ? 0 : value.length);
        if (value != null) for (int v : value) out.writeInt(v);
    }

    @Override public void readPayload(DataInput in, int depth) throws IOException {
        int len = in.readInt();
        value = new int[len];
        for (int i = 0; i < len; i++) value[i] = in.readInt();
    }
}