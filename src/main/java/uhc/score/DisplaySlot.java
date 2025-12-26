package uhc.score;

import uhc.resource.color.TextColor;
import java.util.Objects;

/**
 * 📊 **Scoreboard Display Slot Identifier**
 * <p>
 * Defines the contract for all scoreboard display locations. This interface
 * allows for type-safe handling of both static vanilla slots and dynamic
 * team-specific sidebar slots.
 * </p>
 */
public interface DisplaySlot {

    // --- 📋 Static Constants (Standard Slots) ---

    /** The Player List (Tab menu). */
    DisplaySlot LIST = new StandardDisplaySlot("list");

    /** The global Sidebar. */
    DisplaySlot SIDEBAR = new StandardDisplaySlot("sidebar");

    /** The area directly under player nameplates. */
    DisplaySlot BELOW_NAME = new StandardDisplaySlot("below_name");

    // --- 🛰️ Logic & Accessors ---

    /**
     * Retrieves the lowercase NBT-compatible name for the display slot.
     * @return The identifier string (e.g., "sidebar.team.red").
     */
    String getSlotName();

    /**
     * Validates the slot identifier against Minecraft's naming conventions.
     * @throws IllegalStateException if the slot name is malformed.
     */
    default void validate() throws IllegalStateException {
        if (getSlotName() == null || getSlotName().isBlank()) {
            throw new IllegalStateException("DisplaySlot identifier cannot be null or blank.");
        }
    }

    // --- 🛠️ Static Factory Methods ---

    /**
     * Creates a type-safe Team Sidebar slot.
     * <p><b>Logic:</b> Wraps a {@link TextColor} into a unique DisplaySlot instance.</p>
     * @param color The target team color.
     * @return A DisplaySlot representing {@code sidebar.team.<color>}.
     */
    static DisplaySlot teamSidebar(TextColor color) {
        return new TeamSidebarSlot(Objects.requireNonNull(color, "TextColor is required for team sidebars."));
    }

    // --- 📦 Private Implementations ---

    /**
     * Implementation for standard, fixed Minecraft display slots.
     */
    record StandardDisplaySlot(String name) implements DisplaySlot {
        @Override
        public String getSlotName() {
            return name;
        }

        @Override
        public String toString() {
            return getSlotName();
        }
    }

    /**
     * Implementation for dynamic team-colored sidebar slots.
     */
    record TeamSidebarSlot(TextColor color) implements DisplaySlot {
        @Override
        public String getSlotName() {
            return "sidebar.team." + color.getColor();
        }

        @Override
        public String toString() {
            return getSlotName();
        }
    }
}