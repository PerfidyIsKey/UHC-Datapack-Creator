package uhc.resource.item.components;

/**
 * 👤 **Player Model Type**
 * <p>
 * Defines the arm thickness for player skins in the profile component.
 * </p>
 */
public enum PlayerModel {
    /** Standard 4-pixel wide arms (Steve model) */
    WIDE("wide"),

    /** 3-pixel wide arms (Alex model) */
    SLIM("slim");

    private final String nbtValue;

    PlayerModel(String nbtValue) {
        this.nbtValue = nbtValue;
    }

    /**
     * @return The lowercase string required by Minecraft NBT.
     */
    public String getNbtValue() {
        return nbtValue;
    }

    @Override
    public String toString() {
        return nbtValue;
    }
}