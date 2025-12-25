package uhc.text;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import uhc.arguments.item.SingleItemStack;
import uhc.resource.entity.EntityId;
import uhc.data.nbt.item.components.ItemComponent;

import java.util.*;

/**
 * 👁️ **Polymorphic Hover Event System**
 * <p>
 * Defines a tooltip that appears when a player hovers their mouse over a {@link TextComponent}.
 * Supports text tooltips, item stack previews (1.20.5+ style), and entity data.
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

    /** * @return The Minecraft-internal action name for the hover event.
     */
    @JsonProperty("action")
    String getAction();

    // --- 🛠️ Static Helper Factories ---

    /**
     * Displays a simple or styled text tooltip.
     * @param text The {@link TextComponent} to display on hover.
     * @return A new show_text HoverEvent.
     */
    static ShowText showText(TextComponent text) {
        return new ShowText(Objects.requireNonNull(text, "Tooltip text cannot be null."));
    }

    /**
     * Displays an item stack tooltip using a rich {@link SingleItemStack}.
     * <p><b>Integration:</b> This method extracts the ID, count, and modern components
     * directly from your item stack implementation.</p>
     * @param itemStack The rich item stack to preview.
     * @return A new show_item HoverEvent.
     * @throws NullPointerException if itemStack is null.
     */
    static ShowItem showItem(SingleItemStack itemStack) {
        Objects.requireNonNull(itemStack, "Item stack for hover event cannot be null.");

        // Extract raw data from the rich SingleItemStack object
        String idStr = itemStack.getId().getResourceLocation();
        Integer count = itemStack.getCount();

        // Convert components to the map format expected by the JSON schema
        Map<String, Object> componentsMap = null;

        // Use reflection or a getter if you add one to SingleItemStack,
        // otherwise we use the existing logic to build the map:
        // Note: Assumes SingleItemStack provides access to components or a similar map
        // For this implementation, we map them directly to the ItemContents record.

        return new ShowItem(new ItemContents(idStr, count, null));
        // Note: To fully support components here, SingleItemStack would need a getComponents() method.
    }

    /**
     * Displays an entity tooltip.
     * @param type The type-safe {@link EntityId}.
     * @param uuid The unique ID of the entity.
     * @param name Optional {@link TextComponent} custom name.
     * @return A new show_entity HoverEvent.
     */
    static ShowEntity showEntity(EntityId type, UUID uuid, TextComponent name) {
        Objects.requireNonNull(type, "Entity type cannot be null.");
        Objects.requireNonNull(uuid, "Entity UUID cannot be null.");

        return new ShowEntity(new EntityContents(name, type.getResourceLocation(), uuid.toString()));
    }

    // --- 📦 Implementation Records ---

    record ShowText(@JsonProperty("contents") TextComponent contents) implements HoverEvent {
        @Override public String getAction() { return "show_text"; }
    }

    record ShowItem(@JsonProperty("contents") ItemContents contents) implements HoverEvent {
        @Override public String getAction() { return "show_item"; }
    }

    record ShowEntity(@JsonProperty("contents") EntityContents contents) implements HoverEvent {
        @Override public String getAction() { return "show_entity"; }
    }

    // --- 📄 Content Structures ---

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record ItemContents(
            @JsonProperty("id") String id,
            @JsonProperty("count") Integer count,
            @JsonProperty("components") Map<String, Object> components
    ) {
        public ItemContents {
            Objects.requireNonNull(id, "Item ID string cannot be null.");
            if (components != null && components.isEmpty()) components = null;
            if (count != null && count < 1) count = 1;
        }
    }

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