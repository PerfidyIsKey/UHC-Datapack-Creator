package uhc.command.commands;

import uhc.arguments.entity.Entity;
import uhc.command.MinecraftCommand;
import uhc.resource.color.TextColor;
import uhc.game.team.TeamData;
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

    // --- 🏗️ Macro / Entry Points ---

    /**
     * 🚀 **The Macro Approach**
     * <p>Creates a sequence of commands to fully register a team and
     * immediately apply its primary color from {@link TeamData}.</p>
     * * @param team The {@link TeamData} containing the team's identity and color.
     * @return An array containing the {@code add} and {@code modify color} command objects.
     */
    public static MinecraftCommand[] initialize(TeamData team) {
        try {
            Objects.requireNonNull(team, "Cannot initialize a null team.");
            return new MinecraftCommand[] {
                    add(team),
                    modify(team).color(team.getTextColor())
            };
        } catch (Exception e) {
            // Returns an empty array to prevent crashing, allowing logs to catch the error
            return new MinecraftCommand[0];
        }
    }

    /** * @return A {@link ListBuilder} to generate {@code /team list} commands.
     */
    public static ListBuilder list() { return new ListBuilder(); }

    /** * @param team The team to create.
     * @return An {@link ActionBuilder} for {@code /team add}.
     */
    public static ActionBuilder add(TeamData team) { return new ActionBuilder("add", team); }

    /** * @param team The team to remove.
     * @return An {@link ActionBuilder} for {@code /team remove}.
     */
    public static ActionBuilder remove(TeamData team) { return new ActionBuilder("remove", team); }

    /** * @param team The team to clear of players.
     * @return An {@link ActionBuilder} for {@code /team empty}.
     */
    public static ActionBuilder empty(TeamData team) { return new ActionBuilder("empty", team); }

    /** * @param team The team to be joined.
     * @return A {@link JoinBuilder} for {@code /team join}.
     */
    public static JoinBuilder join(TeamData team) { return new JoinBuilder(team); }

    /** * @param members The {@link Entity} selector representing players who should leave their team.
     * @return A {@link LeaveBuilder} for {@code /team leave}.
     */
    public static LeaveBuilder leave(Entity members) { return new LeaveBuilder(members); }

    /** * @param team The team whose attributes should be changed.
     * @return A {@link ModifyBuilder} for {@code /team modify}.
     */
    public static ModifyBuilder modify(TeamData team) { return new ModifyBuilder(team); }

    // --- ⚙️ Core Contract ---

    /**
     * Generates the raw Minecraft command string.
     * @return A string ready for execution in-game or in a datapack.
     * @throws IllegalStateException if the builder is missing required parameters.
     */
    @Override
    public abstract String generate();

    /**
     * Safely attempts to generate the command string for logging or debugging.
     * @return The command string, or an error comment if generation fails.
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

    /** * ⚖️ **Team Rule Options**
     * <p>Defines how nametags, death messages, and collisions are handled for a team.</p>
     */
    public enum TeamRule {
        /** Default behavior. */
        ALWAYS,
        /** Disabled for everyone. */
        NEVER,
        /** Only visible/active for fellow teammates. */
        OWN_TEAM,
        /** Only visible/active for enemies. */
        OTHER_TEAMS;

        /** @return The camelCase string used for visibility options. */
        public String toVisibility() {
            return switch (this) {
                case ALWAYS -> "always";
                case NEVER -> "never";
                case OWN_TEAM -> "hideForOwnTeam";
                case OTHER_TEAMS -> "hideForOtherTeams";
            };
        }

        /** @return The camelCase string used for collision options. */
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

    /** * 🛠️ **Modify Builder**
     * <p>Handles specific attribute changes for a team.</p>
     */
    public static class ModifyBuilder extends TeamCommand {
        /** The team being modified. */
        private final TeamData team;
        /** The specific Minecraft modification option (e.g., "color"). */
        private String option;
        /** The value to set for the chosen option. */
        private String value;

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

    /** * 📋 **List Builder**
     * <p>Handles the {@code /team list} command.</p>
     */
    public static class ListBuilder extends TeamCommand {
        /** Optional team filter. */
        private TeamData team;

        public ListBuilder team(TeamData team) { this.team = team; return this; }

        @Override public String generate() {
            return "team list" + (team != null ? " " + team.name() : "");
        }
    }

    /** * ⚡ **Action Builder**
     * <p>Handles basic operations like adding, removing, or emptying teams.</p>
     */
    public static class ActionBuilder extends TeamCommand {
        /** The action keyword (add/remove/empty). */
        private final String action;
        /** The target team. */
        private final TeamData team;

        public ActionBuilder(String action, TeamData team) {
            this.action = Objects.requireNonNull(action);
            this.team = Objects.requireNonNull(team, "TeamData cannot be null for action: " + action);
        }

        @Override public String generate() {
            StringBuilder sb = new StringBuilder("team ").append(action).append(" ").append(team.name());
            if (action.equals("add")) {
                // Specified class TextComponent is not turned into a string here, but built.
                sb.append(" ").append(team.getDisplayName().build());
            }
            return sb.toString();
        }
    }

    /** * 🤝 **Join Builder**
     * <p>Handles adding players to a team.</p>
     */
    public static class JoinBuilder extends TeamCommand {
        /** The team to be joined. */
        private final TeamData team;
        /** The players/entities joining. */
        private Entity members;

        public JoinBuilder(TeamData team) {
            this.team = Objects.requireNonNull(team, "Join target team cannot be null.");
        }

        public JoinBuilder members(Entity members) { this.members = members; return this; }

        @Override public String generate() {
            return "team join " + team.name() + (members != null ? " " + members : "");
        }
    }

    /** * 👋 **Leave Builder**
     * <p>Handles removing players from their current teams.</p>
     */
    public static class LeaveBuilder extends TeamCommand {
        /** The entities who should leave their current team. */
        private final Entity members;

        public LeaveBuilder(Entity members) {
            this.members = Objects.requireNonNull(members, "Leave target entities cannot be null.");
        }

        @Override public String generate() { return "team leave " + members; }
    }
}