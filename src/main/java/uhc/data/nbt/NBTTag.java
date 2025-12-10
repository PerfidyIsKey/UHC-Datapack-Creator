package uhc.data.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public interface NBTTag {
    byte getTypeId();
    String getName();
    void setName(String name);

    // write only the payload, not the type id or the name
    void writePayload(DataOutput out) throws IOException;

    // read only the payload. 'depth' can be used to guard recursion
    void readPayload(DataInput in, int depth) throws IOException;
}