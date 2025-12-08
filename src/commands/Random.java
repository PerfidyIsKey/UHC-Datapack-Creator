package commands;

import commands.random.RandomAction;

public class Random {
    private final RandomAction action;
    private Integer rangeMin;
    private Integer rangeMax;

    private Random(RandomAction action) {
        if (action == null) {
            throw new IllegalArgumentException("Random action cannot be null.");
        }
        this.action = action;
    }

    public static Random create(RandomAction action) {
        return new Random(action);
    }

    /**
     * Sets the inclusive range [min, max] for the random number generation.
     * This method is only relevant when the action is VALUE or ROLL.
     * * @param min The minimum inclusive value.
     * @param max The maximum inclusive value.
     * @return The current Random builder instance.
     * @throws IllegalArgumentException if min is not strictly less than max.
     * @throws IllegalStateException if this method is called when action is RESET.
     */
    public Random range(int min, int max) {
        if (action == RandomAction.RESET) {
            throw new IllegalStateException("The 'reset' action does not accept a range. Use build() directly.");
        }

        // Assignment Safety: The first number must always be smaller than the second.
        if (min >= max) {
            throw new IllegalArgumentException("Minimum range value (" + min + ") must be strictly less than maximum value (" + max + ").");
        }

        this.rangeMin = min;
        this.rangeMax = max;
        return this;
    }

    /**
     * @deprecated Use range(int min, int max) instead for clearer parameter handling and validation.
     * @throws IllegalStateException if this method is called.
     */
    @Deprecated
    public Random range(int[] range) {
        if (action == RandomAction.RESET) {
            throw new IllegalStateException("The 'reset' action does not accept a range. Use build() directly.");
        }

        if (range == null || range.length != 2) {
            throw new IllegalArgumentException("Range array must contain exactly two integers: [min, max].");
        }

        // Assignment Safety: The first number must always be smaller than the second.
        if (range[0] >= range[1]) {
            throw new IllegalArgumentException("First index (" + range[0] + ") must be strictly less than second index (" + range[1] + ").");
        }

        return range(range[0], range[1]);
    }


    /**
     * Builds the final /random command string.
     * @throws IllegalStateException if the action is VALUE or ROLL and the range has not been set.
     */
    public String build() {
        StringBuilder sb = new StringBuilder("random ");

        sb.append(action);

        if (action == RandomAction.VALUE || action == RandomAction.ROLL) {
            // Assignment Safety: Check if range is present for VALUE or ROLL
            if (rangeMin == null || rangeMax == null) {
                throw new IllegalStateException("A range must be set for the action '" + action + "'. Call range(min, max) before build().");
            }
            sb.append(" ").append(rangeMin).append("..").append(rangeMax);
        }
        else if (action == RandomAction.RESET) {
            // The "reset" action must be followed by '*'
            sb.append(" *");
        }

        return sb.toString();
    }
}