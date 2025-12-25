package uhc.text;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import uhc.resource.item.ItemId;
import uhc.resource.entity.EntityId;
import uhc.data.nbt.item.components.ItemComponent;

import java.util.*;

/**
 * 👁️ **Polymorphic Hover Event System**
 * <p>
 * Defines a tooltip that appears when a player hovers their mouse over a {@link TextComponent}.
 * Supports text tooltips, item stack previews (1.20.5+ component style), and entity data.
 * </p>
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "action"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = HoverEvent.ShowText.class, name = "show_text"),
        @JsonSubTypes.Type(value = HoverEvent.ShowItem.class, name = "show_item"),
        @JsonSubTypes.Type(value = HoverEvent.ShowEntity.class, name = "show_entity")
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface HoverEvent {

    /** @return The Minecraft-internal action name for the hover event. */
    @JsonProperty("action")
    String getAction();

    // --- Static Helper Factories ---

    /**
     * Displays a simple or styled text tooltip.
     * @param text The {@link TextComponent} to display on hover.
     * @return A new show_text HoverEvent.
     */
    static ShowText showText(TextComponent text) {
        return new ShowText(Objects.requireNonNull(text, "Tooltip text cannot be null."));
    }

    /**
     * Displays an item stack tooltip, including modern Data Components.
     * @param id The type-safe {@link ItemId}.
     * @param count The stack size (optional, null defaults to 1).
     * @param components A list of {@link ItemComponent}s (e.g., Lore, Enchantments).
     * @return A new show_item HoverEvent.
     */
    static ShowItem showItem(ItemId id, Integer count, List<ItemComponent> components) {
        Objects.requireNonNull(id, "Item ID cannot be null.");

        Map<String, Object> componentsMap = null;
        if (components != null && !components.isEmpty()) {
            componentsMap = new HashMap<>();
            for (ItemComponent component : components) {
                if (component != null && component.getId() != null) {
                    componentsMap.put(component.getId().getResourceLocation(), component.toNbt());
                }
            }
        }

        return new ShowItem(new ItemContents(id.getResourceLocation(), count, componentsMap));
    }

    /**
     * Displays an entity tooltip.
     * @param type The type-safe {@link EntityId} (e.g., WOLF).
     * @param uuid The unique ID of the entity.
     * @param name Optional {@link TextComponent} custom name to display.
     * @return A new show_entity HoverEvent.
     */
    static ShowEntity showEntity(EntityId type, UUID uuid, TextComponent name) {
        Objects.requireNonNull(type, "Entity type cannot be null.");
        Objects.requireNonNull(uuid, "Entity UUID cannot be null.");

        return new ShowEntity(new EntityContents(name, type.getResourceLocation(), uuid.toString()));
    }

    // --- Implementation Records ---

    /** Action to show a text component. */
    record ShowText(@JsonProperty("contents") TextComponent contents) implements HoverEvent {
        @Override public String getAction() { return "show_text"; }
    }

    /** Action to show an item's details. */
    record ShowItem(@JsonProperty("contents") ItemContents contents) implements HoverEvent {
        @Override public String getAction() { return "show_item"; }
    }

    /** Action to show entity metadata. */
    record ShowEntity(@JsonProperty("contents") EntityContents contents) implements HoverEvent {
        @Override public String getAction() { return "show_entity"; }
    }

    // --- Content Records ---

    /**
     * Represents the internal structure of an item tooltip.
     * @param id The namespaced item ID.
     * @param count Optional amount.
     * @param components Map of component IDs to their NBT data.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    record ItemContents(
            @JsonProperty("id") String id,
            @JsonProperty("count") Integer count,
            @JsonProperty("components") Map<String, Object> components
    ) {
        public ItemContents {
            Objects.requireNonNull(id, "Item ID string cannot be null.");
            // Strip empty component maps to keep JSON compact
            if (components != null && components.isEmpty()) components = null;
            // Prevent negative item counts
            if (count != null && count < 0) count = 1;
        }
    }

    /**
     * Represents the internal structure of an entity tooltip.
     * @param name The display name of the entity.
     * @param type The namespaced entity type.
     * @param uuid The UUID string (keyed as 'id').
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    record EntityContents(
            @JsonProperty("name") TextComponent name,
            @JsonProperty("type") String type,
            @JsonProperty("id") String uuid
    ) {
        public EntityContents {
            Objects.requireNonNull(type, "Entity type string cannot be null.");
            Objects.requireNonNull(uuid, "Entity UUID string cannot be null.");
        }
    }
}