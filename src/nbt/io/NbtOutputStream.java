package nbt.io;

import nbt.tags.CompoundTag;
import nbt.TagType;

import java.io.*;
import java.util.zip.GZIPOutputStream;

public class NbtOutputStream implements Closeable {
    private final DataOutput out;

    public NbtOutputStream(OutputStream os, boolean gzipped) throws IOException {
        OutputStream base = gzipped ? new GZIPOutputStream(os) : os;
        this.out = new DataOutputStream(base);
    }

    public void writeNamedTag(CompoundTag root) throws IOException {
        out.writeByte(root.getTypeId());
        out.writeUTF(root.getName() == null ? "" : root.getName());
        root.writePayload((DataOutput) out);
        if (out instanceof Flushable) ((Flushable) out).flush();
    }

    @Override public void close() throws IOException {
        if (out instanceof Closeable) ((Closeable) out).close();
    }
}