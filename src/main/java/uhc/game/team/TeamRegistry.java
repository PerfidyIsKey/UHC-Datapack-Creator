package uhc.game.team;

import uhc.resource.color.BossbarColor;
import uhc.resource.color.TextColor;
import uhc.resource.color.DyeColor;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * 🏛️ **Team Registry**
 * <p>
 * Centralized repository for all 16 standard Minecraft team definitions.
 * This registry acts as the "Single Source of Truth" for team-side data.
 * </p>
 * <p>
 * Teams are ordered by aesthetic priority:
 * <ol>
 * <li>Vibrant colors with native Bossbar support.</li>
 * <li>Standard light variants.</li>
 * <li>Delayed dark variants.</li>
 * <li>Black and White as terminal entries.</li>
 * </ol>
 * </p>
 */
public final class TeamRegistry {

    // --- 🌟 Priority 1: Vibrant Colors with Native Bossbars ---

    /** The Blue team profile. Native Bossbar: Blue. */
    public static final TeamData BLUE = TeamData.of(0, TextColor.BLUE, BossbarColor.BLUE, DyeColor.BLUE, 11, new float[]{0.33f, 0.33f, 1.0f});

    /** The Green team profile. Native Bossbar: Green. */
    public static final TeamData GREEN = TeamData.of(1, TextColor.GREEN, BossbarColor.GREEN, DyeColor.GREEN, 13, new float[]{0.33f, 1.0f, 0.33f});

    /** The Pink team profile (Light Purple). Native Bossbar: Pink. */
    public static final TeamData PINK = TeamData.of(2, TextColor.LIGHT_PURPLE, BossbarColor.PINK, DyeColor.PINK, 6, new float[]{1.0f, 0.33f, 1.0f});

    /** The Purple team profile (Dark Purple). Native Bossbar: Purple. */
    public static final TeamData PURPLE = TeamData.of(3, TextColor.DARK_PURPLE, BossbarColor.PURPLE, DyeColor.PURPLE, 10, new float[]{0.66f, 0.0f, 0.66f});

    /** The Red team profile. Native Bossbar: Red. */
    public static final TeamData RED = TeamData.of(4, TextColor.RED, BossbarColor.RED, DyeColor.RED, 14, new float[]{1.0f, 0.33f, 0.33f});

    /** The Yellow team profile. Native Bossbar: Yellow. */
    public static final TeamData YELLOW = TeamData.of(5, TextColor.YELLOW, BossbarColor.YELLOW, DyeColor.YELLOW, 4, new float[]{1.0f, 1.0f, 0.33f});

    // --- 💡 Priority 2: Standard Light Variants (White Bossbar) ---

    /** The Aqua team profile. Fallback Bossbar: White. */
    public static final TeamData AQUA = TeamData.of(6, TextColor.AQUA, BossbarColor.WHITE, DyeColor.CYAN, 9, new float[]{0.33f, 1.0f, 1.0f});

    /** The Gold team profile. Fallback Bossbar: White. */
    public static final TeamData GOLD = TeamData.of(7, TextColor.GOLD, BossbarColor.WHITE, DyeColor.ORANGE, 1, new float[]{1.0f, 0.66f, 0.0f});

    /** The Gray team profile. Fallback Bossbar: White. */
    public static final TeamData GRAY = TeamData.of(8, TextColor.GRAY, BossbarColor.WHITE, DyeColor.LIGHT_GRAY, 8, new float[]{0.66f, 0.66f, 0.66f});

    // --- 🌑 Priority 3: Delayed Dark Variants (White Bossbar) ---

    /** The Dark Blue team profile. Fallback Bossbar: White. */
    public static final TeamData DARK_BLUE = TeamData.of(9, TextColor.DARK_BLUE, BossbarColor.WHITE, DyeColor.BLUE, 11, new float[]{0.0f, 0.0f, 0.66f});

    /** The Dark Green team profile. Fallback Bossbar: White. */
    public static final TeamData DARK_GREEN = TeamData.of(10, TextColor.DARK_GREEN, BossbarColor.WHITE, DyeColor.GREEN, 13, new float[]{0.0f, 0.66f, 0.0f});

    /** The Dark Aqua team profile. Fallback Bossbar: White. */
    public static final TeamData DARK_AQUA = TeamData.of(11, TextColor.DARK_AQUA, BossbarColor.WHITE, DyeColor.CYAN, 9, new float[]{0.0f, 0.66f, 0.66f});

    /** The Dark Red team profile. Fallback Bossbar: White. */
    public static final TeamData DARK_RED = TeamData.of(12, TextColor.DARK_RED, BossbarColor.WHITE, DyeColor.RED, 14, new float[]{0.66f, 0.0f, 0.0f});

    /** The Dark Gray team profile. Fallback Bossbar: White. */
    public static final TeamData DARK_GRAY = TeamData.of(13, TextColor.DARK_GRAY, BossbarColor.WHITE, DyeColor.GRAY, 7, new float[]{0.33f, 0.33f, 0.33f});

    // --- 🏁 Priority 4: Final Colors ---

    /** The Black team profile. Fallback Bossbar: White. */
    public static final TeamData BLACK = TeamData.of(14, TextColor.BLACK, BossbarColor.WHITE, DyeColor.BLACK, 15, new float[]{0.0f, 0.0f, 0.0f});

    /** The White team profile. Native Bossbar: White. */
    public static final TeamData WHITE = TeamData.of(15, TextColor.WHITE, BossbarColor.WHITE, DyeColor.WHITE, 0, new float[]{1.0f, 1.0f, 1.0f});

    // --- 📋 Collections ---

    /** * An immutable list containing all 16 defined teams.
     * Useful for batch processing and command generation.
     */
    public static final List<TeamData> ALL;

    static {
        List<TeamData> teams = new ArrayList<>();
        try {
            teams.add(BLUE);
            teams.add(GREEN);
            teams.add(PINK);
            teams.add(PURPLE);
            teams.add(RED);
            teams.add(YELLOW);
            teams.add(AQUA);
            teams.add(GOLD);
            teams.add(GRAY);
            teams.add(DARK_BLUE);
            teams.add(DARK_GREEN);
            teams.add(DARK_AQUA);
            teams.add(DARK_RED);
            teams.add(DARK_GRAY);
            teams.add(BLACK);
            teams.add(WHITE);
        } catch (Exception e) {
            // Handle initialization errors if TeamData factory fails
            System.err.println("Critical Error: TeamRegistry failed to initialize. " + e.getMessage());
        }
        ALL = Collections.unmodifiableList(teams);
    }

    // --- 🛠️ Utility Methods ---

    /**
     * Retrieves a specific {@link TeamData} instance by its numerical ID.
     * * @param id The unique integer ID assigned to the team.
     * @return The matching {@link TeamData} object, or {@code null} if no match is found.
     */
    public static TeamData getById(int id) {
        try {
            for (TeamData team : ALL) {
                if (team != null && team.getId() == id) {
                    return team;
                }
            }
        } catch (Exception e) {
            // Log search failure but do not crash
            return null;
        }
        return null;
    }

    /**
     * Private constructor to prevent instantiation of a static utility class.
     */
    private TeamRegistry() {
        throw new UnsupportedOperationException("This is a static utility class and cannot be instantiated.");
    }
}