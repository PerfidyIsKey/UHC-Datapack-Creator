package uhc.command.commands;

import uhc.arguments.entity.Entity;
import uhc.command.MinecraftCommand;
import uhc.resource.color.TextColor;
import uhc.team.TeamData;
import uhc.text.TextComponent;

import java.util.Objects;

/**
 * 🚩 **Team Command Builder**
 * <p>
 * A comprehensive builder for constructing Minecraft {@code /team} commands.
 * This class uses specialized inner builders to handle different sub-actions
 * while maintaining strict type safety and PascalCase/camelCase requirements.
 * </p>
 */
public abstract class TeamCommand implements MinecraftCommand {

    // --- 🏗️ Entry Points ---

    /**
     * Lists all existing teams or specific members if a team is provided.
     * @return A new {@link ListBuilder}.
     */
    public static ListBuilder list() { return new ListBuilder(); }

    /**
     * Creates a new team based on the provided team data.
     * @param team The {@link TeamData} defining the team identity.
     * @return A new {@link ActionBuilder} configured for 'add'.
     */
    public static ActionBuilder add(TeamData team) { return new ActionBuilder("add", team); }

    /**
     * Deletes the specified team.
     * @param team The {@link TeamData} of the team to delete.
     * @return A new {@link ActionBuilder} configured for 'remove'.
     */
    public static ActionBuilder remove(TeamData team) { return new ActionBuilder("remove", team); }

    /**
     * Removes all members from the specified team.
     * @param team The {@link TeamData} of the team to empty.
     * @return A new {@link ActionBuilder} configured for 'empty'.
     */
    public static ActionBuilder empty(TeamData team) { return new ActionBuilder("empty", team); }

    /**
     * Adds entities to a specific team.
     * @param team The {@link TeamData} to join.
     * @return A new {@link JoinBuilder}.
     */
    public static JoinBuilder join(TeamData team) { return new JoinBuilder(team); }

    /**
     * Removes specified entities from their current team.
     * @param members The {@link Entity} selector of players to remove.
     * @return A new {@link LeaveBuilder}.
     */
    public static LeaveBuilder leave(Entity members) { return new LeaveBuilder(members); }

    /**
     * Modifies attributes (color, rules, etc.) of a specific team.
     * @param team The {@link TeamData} to modify.
     * @return A new {@link ModifyBuilder}.
     */
    public static ModifyBuilder modify(TeamData team) { return new ModifyBuilder(team); }

    // --- ⚙️ Core Contract ---

    /**
     * Generates the final Minecraft command string.
     * @return The formatted command (e.g., "team join Team0 @a").
     * @throws IllegalStateException if the builder state is invalid.
     */
    @Override
    public abstract String generate();

    /**
     * Returns the command string or an error comment if generation fails.
     * @return The raw command.
     */
    @Override
    public String toString() {
        try {
            return generate();
        } catch (Exception e) {
            return "/* Error generating team command: " + e.getMessage() + " */";
        }
    }

    // --- 📂 Internal Enums ---

    /**
     * ⚖️ **Team Rule**
     * <p>Unified enum for handling Visibility and Collision rules.</p>
     */
    public enum TeamRule {
        /** Rule is always active for everyone. */
        ALWAYS,
        /** Rule is never active. */
        NEVER,
        /** Rule only applies to members of the same team. */
        OWN_TEAM,
        /** Rule only applies to members of different teams. */
        OTHER_TEAMS;

        /**
         * Formats the enum value for Visibility options.
         * @return Minecraft-compliant string (e.g., "hideForOwnTeam").
         */
        public String toVisibility() {
            return switch (this) {
                case ALWAYS -> "always";
                case NEVER -> "never";
                case OWN_TEAM -> "hideForOwnTeam";
                case OTHER_TEAMS -> "hideForOtherTeams";
            };
        }

        /**
         * Formats the enum value for Collision options.
         * @return Minecraft-compliant string (e.g., "pushOtherTeams").
         */
        public String toCollision() {
            return switch (this) {
                case ALWAYS -> "always";
                case NEVER -> "never";
                case OWN_TEAM -> "pushOwnTeam";
                case OTHER_TEAMS -> "pushOtherTeams";
            };
        }
    }

    // --- 📦 Implementation Branches ---

    /** Handles: {@code team modify <team> <option> <value>} */
    public static class ModifyBuilder extends TeamCommand {
        private final TeamData team;
        private String option;
        private String value;

        /** @param team The team target. */
        public ModifyBuilder(TeamData team) {
            this.team = Objects.requireNonNull(team, "Target team for modify cannot be null.");
        }

        public ModifyBuilder displayName(TextComponent text) { return set("displayName", text.build()); }
        public ModifyBuilder color(TextColor color) { return set("color", color.getColor()); }
        public ModifyBuilder friendlyFire(boolean bool) { return set("friendlyFire", String.valueOf(bool)); }
        public ModifyBuilder seeFriendlyInvisibles(boolean bool) { return set("seeFriendlyInvisibles", String.valueOf(bool)); }
        public ModifyBuilder nametagVisibility(TeamRule rule) { return set("nametagVisibility", rule.toVisibility()); }
        public ModifyBuilder deathMessageVisibility(TeamRule rule) { return set("deathMessageVisibility", rule.toVisibility()); }
        public ModifyBuilder collisionRule(TeamRule rule) { return set("collisionRule", rule.toCollision()); }
        public ModifyBuilder prefix(TextComponent text) { return set("prefix", text.build()); }
        public ModifyBuilder suffix(TextComponent text) { return set("suffix", text.build()); }

        private ModifyBuilder set(String option, String value) {
            this.option = option;
            this.value = value;
            return this;
        }

        @Override public String generate() {
            if (option == null || value == null) {
                throw new IllegalStateException("Modify command requires an option and a value for team: " + team.name());
            }
            return "team modify " + team.name() + " " + option + " " + value;
        }
    }

    /** Handles: {@code team list [<team>]} */
    public static class ListBuilder extends TeamCommand {
        private TeamData team;

        /** @param team Optional team to list members from. */
        public ListBuilder team(TeamData team) { this.team = team; return this; }

        @Override public String generate() {
            return "team list" + (team != null ? " " + team.name() : "");
        }
    }

    /** Handles: {@code team add/remove/empty <team> [<displayName>]} */
    public static class ActionBuilder extends TeamCommand {
        private final String action;
        private final TeamData team;

        public ActionBuilder(String action, TeamData team) {
            this.action = Objects.requireNonNull(action);
            this.team = Objects.requireNonNull(team, "TeamData cannot be null for action: " + action);
        }

        @Override public String generate() {
            StringBuilder sb = new StringBuilder("team ").append(action).append(" ").append(team.name());
            if (action.equals("add") && team.getDisplayName() != null) {
                // Specified classes are NOT turned into strings here, we use the build() method.
                sb.append(" ").append(team.getDisplayName().build());
            }
            return sb.toString();
        }
    }

    /** Handles: {@code team join <team> [<members>]} */
    public static class JoinBuilder extends TeamCommand {
        private final TeamData team;
        private Entity members;

        public JoinBuilder(TeamData team) {
            this.team = Objects.requireNonNull(team, "Join target team cannot be null.");
        }

        /** @param members The entities to join the team. */
        public JoinBuilder members(Entity members) { this.members = members; return this; }

        @Override public String generate() {
            return "team join " + team.name() + (members != null ? " " + members : "");
        }
    }

    /** Handles: {@code team leave <members>} */
    public static class LeaveBuilder extends TeamCommand {
        private final Entity members;

        /** @param members The entities to remove from their teams. */
        public LeaveBuilder(Entity members) {
            this.members = Objects.requireNonNull(members, "Leave target entities cannot be null.");
        }

        @Override public String generate() {
            return "team leave " + members;
        }
    }
}