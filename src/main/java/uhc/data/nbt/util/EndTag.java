package uhc.data.nbt.util;

import uhc.data.nbt.NBTTag;
import uhc.data.nbt.TagType;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class EndTag implements NBTTag {
    @Override public byte getTypeId() { return TagType.TAG_END; }
    @Override public String getName() { return null; }
    @Override public void setName(String name) { /*ignored*/ }
    @Override public void writePayload(DataOutput out) throws IOException { }
    @Override public void readPayload(DataInput in, int depth) throws IOException { }
}
