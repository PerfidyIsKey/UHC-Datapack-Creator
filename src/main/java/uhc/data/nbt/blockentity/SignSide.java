package uhc.data.nbt.blockentity;

import uhc.data.nbt.TagType;
import uhc.data.nbt.tags.CompoundTag;
import uhc.data.nbt.tags.ListTag;
import uhc.data.nbt.tags.StringTag;
import uhc.text.TextComponent;

/**
 * Represents the Compound Tag for one side of a sign (front_text or back_text).
 * Supports fluent chaining and type-safe TextComponents.
 */
public class SignSide extends CompoundTag {

    private final ListTag messages;
    private final SignEntity parent;

    /**
     * Constructor for a sign side.
     * @param name The tag name (usually "front_text" or "back_text").
     * @param parent Reference to the parent SignEntity for builder chaining.
     */
    public SignSide(String name, SignEntity parent) {
        super(name);
        this.parent = parent;
        this.messages = new ListTag("messages");
        this.messages.setElementType(TagType.TAG_STRING);
        this.put(messages);
    }

    /**
     * Adds a message line using a type-safe TextComponent.
     * @param component The TextComponent representing a line of text.
     * @return This SignSide instance for chaining.
     * @throws IllegalStateException if more than 4 lines are added.
     */
    public SignSide addMessage(TextComponent component) {
        if (messages.getElements().size() >= 4) {
            throw new IllegalStateException("A sign side cannot have more than 4 lines of text.");
        }
        // Build the component to JSON string and wrap in StringTag
        messages.add(new StringTag(null, component.build()));
        return this;
    }

    /**
     * Convenience method to add a message line from a plain string.
     * @param text The plain text string.
     * @return This SignSide instance for chaining.
     */
    public SignSide addMessage(String text) {
        return addMessage(TextComponent.simple(text));
    }

    /**
     * Completes configuration of this side and returns the parent SignEntity.
     * This allows you to switch between front_text and back_text or finish the builder.
     * @return The parent SignEntity.
     */
    public SignEntity done() {
        return parent;
    }

    /**
     * Clears all messages from this side.
     */
    public void clearMessages() {
        this.messages.getElements().clear();
    }
}