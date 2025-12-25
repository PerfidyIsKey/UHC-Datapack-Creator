package uhc.data.nbt.item.components;

import uhc.data.nbt.NBTTag;
import uhc.arguments.sound.SoundEvent;
import uhc.data.nbt.tags.*;
import uhc.resource.item.components.ComponentId;
import uhc.resource.sound.InstrumentId;
import uhc.text.TextComponent;

/**
 * 🎺 **Instrument Component Implementation**
 * <p>
 * Manages the "minecraft:instrument" component.
 * Handles the logic for Goat Horns, allowing for either a simple registry reference
 * or a fully custom sound and tooltip definition.
 * </p>
 */
public class InstrumentComponent implements ItemComponent {

    private InstrumentId registryId;

    // Custom Definition Fields
    private TextComponent description;
    private SoundEvent soundEvent;
    private float useDuration;
    private float range;

    /**
     * Private constructor to enforce the use of the static factory method.
     */
    private InstrumentComponent() {}

    /**
     * Creates a new InstrumentComponent instance.
     * @return A new builder instance.
     */
    public static InstrumentComponent create() {
        return new InstrumentComponent();
    }

    /**
     * Sets the instrument to a vanilla or predefined registry ID.
     * <p>
     * If this is the only field set, it serializes as a {@link StringTag}.
     * </p>
     * @param registryId The enum value representing the instrument.
     */
    public InstrumentComponent registryId(InstrumentId registryId) {
        this.registryId = registryId;
        return this;
    }

    /**
     * Sets a custom description for the instrument's tooltip.
     * @param description A {@link TextComponent} (usually serialized to JSON).
     */
    public InstrumentComponent description(TextComponent description) {
        this.description = description;
        return this;
    }

    /**
     * Sets the specific {@link SoundEvent} for this instrument.
     * @param soundEvent The sound data (ID and optional range).
     */
    public InstrumentComponent soundEvent(SoundEvent soundEvent) {
        this.soundEvent = soundEvent;
        return this;
    }

    /**
     * Sets the time required to "charge" or play the instrument.
     * @param useDuration Non-negative float in seconds.
     */
    public InstrumentComponent useDuration(float useDuration) {
        this.useDuration = Math.max(0.0f, useDuration);
        return this;
    }

    /**
     * Sets the block radius within which the instrument can be heard.
     * @param range Non-negative block distance.
     */
    public InstrumentComponent range(float range) {
        this.range = Math.max(0.0f, range);
        return this;
    }

    @Override
    public ComponentId getId() {
        return ComponentId.INSTRUMENT;
    }

    /**
     * Converts the component into its NBT representation.
     * <p>
     * <b>Polymorphic Logic:</b>
     * 1. If only {@code registryId} is provided, returns a {@link StringTag}.
     * 2. If custom fields are provided, returns a {@link CompoundTag} definition.
     * </p>
     */
    @Override
    public NBTTag toNbt() {
        String componentKey = getId().getResourceLocation();

        // Mode 1: Simple Registry Reference
        // We output a StringTag if a registry ID exists and no custom overrides are present.
        if (registryId != null && description == null && soundEvent == null) {
            return new StringTag(componentKey, registryId.getResourceLocation());
        }

        // Mode 2: Custom Inline Definition
        CompoundTag root = CompoundTag.create(componentKey);

        if (description != null) {
            root.put(new StringTag("description", description.toString()));
        }

        if (soundEvent != null) {
            Object nbt = soundEvent.toNbt();
            if (nbt instanceof StringTag) {
                StringTag st = (StringTag) nbt;
                st.setName("sound_event");
                root.put(st);
            } else if (nbt instanceof CompoundTag) {
                CompoundTag ct = (CompoundTag) nbt;
                ct.setName("sound_event");
                root.put(ct);
            }
        }

        root.put(new FloatTag("use_duration", useDuration));
        root.put(new FloatTag("range", range));

        return root;
    }
}