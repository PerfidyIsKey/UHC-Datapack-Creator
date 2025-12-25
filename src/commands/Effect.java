package commands;

import uhc.arguments.entity.Entity;
import uhc.resource.effect.EffectId;

public class Effect {
    private final uhc.command.commands.EffectCommand.EffectAction action;
    private Entity targets;
    private EffectId effect;
    private int seconds;
    private int amplifier;
    private boolean hideParticles;

    private Effect(uhc.command.commands.EffectCommand.EffectAction action) {
        this.action = action;
    }

    public static Effect create(uhc.command.commands.EffectCommand.EffectAction action) {
        return new Effect(action);
    }

    public Effect targets(Entity targets) {
        this.targets = targets;
        return this;
    }

    public Effect effect(EffectId effect) {
        this.effect = effect;
        return this;
    }

    public Effect seconds(int seconds) {
        this.seconds = seconds;
        return this;
    }

    public Effect amplifier(int amplifier) {
        this.amplifier = amplifier;
        return this;
    }

    public Effect hideParticles(boolean hideParticles) {
        this.hideParticles = hideParticles;
        return this;
    }

    public String build() {
        StringBuilder sb = new StringBuilder("effect ");
        switch (action) {
            case CLEAR:
                sb.append(uhc.command.commands.EffectCommand.EffectAction.CLEAR);
                if (targets != null) {
                    sb.append(" ").append(targets);
                    if (effect != null) {
                        sb.append(" ").append(effect);
                    }
                }
                break;

            case GIVE:
                sb.append(uhc.command.commands.EffectCommand.EffectAction.GIVE).append(" ").append(targets).append(" ").append(effect);
                if (seconds != 0) {
                    sb.append(" ").append(seconds);
                }
                else {
                    sb.append(" ").append("infinite");
                }
                if (amplifier != 0) {
                    sb.append(" ").append(amplifier);
                    if (hideParticles) {
                        sb.append(" ").append(hideParticles);
                    }
                }
                break;
        }

        return sb.toString();
    }
}
