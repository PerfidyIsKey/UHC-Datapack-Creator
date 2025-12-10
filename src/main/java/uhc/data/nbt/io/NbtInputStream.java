package uhc.data.nbt.io;

import uhc.data.nbt.tags.CompoundTag;
import uhc.data.nbt.util.TagFactory;
import uhc.data.nbt.NBTTag;
import uhc.data.nbt.TagType;

import java.io.*;
import java.util.zip.GZIPInputStream;

public class NbtInputStream implements Closeable {
    private final DataInput in;

    public NbtInputStream(InputStream is, boolean gzipped) throws IOException {
        InputStream base = gzipped ? new GZIPInputStream(is) : is;
        this.in = new DataInputStream(base);
    }

    // reads a named root tag, expected to be a CompoundTag
    public CompoundTag readNamedCompound() throws IOException {
        byte type = in.readByte();
        if (type == TagType.TAG_END) return CompoundTag.create();
        String name = in.readUTF();
        NBTTag root = TagFactory.create(type);
        root.setName(name);
        root.readPayload(in, 0);
        if (!(root instanceof CompoundTag)) throw new IOException("Root tag is not a compound");
        return (CompoundTag) root;
    }

    @Override public void close() throws IOException {
        if (in instanceof Closeable) ((Closeable) in).close();
    }
}