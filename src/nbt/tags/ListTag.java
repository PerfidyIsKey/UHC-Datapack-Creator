package nbt.tags;

import nbt.NBTTag;
import nbt.TagType;
import nbt.util.TagFactory;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListTag implements NBTTag {
    private String name;
    private byte elementType;
    private final List<NBTTag> elements = new ArrayList<>();

    public ListTag() {}
    public ListTag(String name) { this.name = name; }

    @Override public byte getTypeId() { return TagType.TAG_LIST; }
    @Override public String getName() { return name; }
    @Override public void setName(String name) { this.name = name; }

    public void setElementType(byte elementType) { this.elementType = elementType; }
    public byte getElementType() { return elementType; }
    public List<NBTTag> getElements() { return elements; }

    public void add(NBTTag t) { elements.add(t); }

    @Override public void writePayload(DataOutput out) throws IOException {
        out.writeByte(elementType);
        out.writeInt(elements.size());
        for (NBTTag e : elements) {
            // list elements are written as payload only, unnamed
            e.writePayload(out);
        }
    }

    @Override public void readPayload(DataInput in, int depth) throws IOException {
        elementType = in.readByte();
        int len = in.readInt();
        for (int i = 0; i < len; i++) {
            NBTTag el = TagFactory.create(elementType);
            el.readPayload(in, depth + 1);
            elements.add(el);
        }
    }
}