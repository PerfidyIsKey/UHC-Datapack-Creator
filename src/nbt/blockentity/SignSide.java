package nbt.blockentity;

import nbt.TagType;
import nbt.tags.CompoundTag;
import nbt.tags.ListTag;
import nbt.tags.StringTag;

/**
 * Represents the Compound Tag for one side of a sign (front_text or back_text).
 */
public class SignSide extends CompoundTag {

    private final ListTag messages;
    private final SignEntity parent; // NEW: Store reference to parent entity

    // Constructor now requires the parent entity
    public SignSide(String name, SignEntity parent) { // MODIFIED
        super(name);
        this.parent = parent; // ASSIGN PARENT
        this.messages = new ListTag("messages");
        this.messages.setElementType(TagType.TAG_STRING);
        this.put(messages);
    }

    /**
     * Adds a single message (JSON string component) to the list and returns the SignSide for chaining.
     */
    public SignSide addMessage(String jsonComponentString) {
        messages.add(new StringTag(null, jsonComponentString));
        return this; // RETURNS SignSide
    }

    /**
     * Completes configuration of this side and returns the parent SignEntity
     * to continue the main chaining sequence.
     */
    public SignEntity done() { // NEW METHOD
        return parent; // RETURNS SignEntity
    }

    /**
     * Helper to clear all messages from this side.
     */
    public void clearMessages() {
        this.messages.getElements().clear();
    }
}