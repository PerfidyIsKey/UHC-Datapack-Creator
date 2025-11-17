package nbt.tags;

import nbt.NBTTag;
import nbt.TagType;
import nbt.util.TagFactory;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class CompoundTag implements NBTTag {
    private String name;
    private final Map<String, NBTTag> value = new LinkedHashMap<>();

    public CompoundTag() {}
    public CompoundTag(String name) { this.name = name; }

    @Override public byte getTypeId() { return TagType.TAG_COMPOUND; }
    @Override public String getName() { return name; }
    @Override public void setName(String name) { this.name = name; }

    public Map<String, NBTTag> getValue() { return value; }

    public CompoundTag put(NBTTag t) {
        if (t.getName() == null) throw new IllegalArgumentException("Compound entries must have a name");
        value.put(t.getName(), t);
        return this;
    }

    public NBTTag get(String name) { return value.get(name); }

    @Override public void writePayload(DataOutput out) throws IOException {
        for (NBTTag t : value.values()) {
            out.writeByte(t.getTypeId());
            if (t.getTypeId() == TagType.TAG_END) continue;
            out.writeUTF(t.getName() == null ? "" : t.getName());
            t.writePayload(out);
        }
        out.writeByte(TagType.TAG_END);
    }

    @Override public void readPayload(DataInput in, int depth) throws IOException {
        while (true) {
            byte type = in.readByte();
            if (type == TagType.TAG_END) return;
            String name = in.readUTF();
            NBTTag t = TagFactory.create(type);
            t.setName(name);
            t.readPayload(in, depth + 1);
            value.put(name, t);
        }
    }
}