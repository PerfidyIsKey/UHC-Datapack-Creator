package ItemClasses;

import Enums.EntityName;
import HelperClasses.Text;

public class EntityData implements Components{
    // Fields
    EntityName id;  // String representation of the entity's ID. Does not exist for the Player entity.
    Text CustomName;    // The custom name JSON text component of this entity. Appears in player death messages and villager trading interfaces, as well as above the entity when the player's cursor is over it. May be empty or not exist.

    // Constructor
    public EntityData(EntityName id, Text name) {
        this.id = id;
        this.CustomName = name;
    }

    @Override
    public String GenerateNBT() {
        return null;
    }

    @Override
    public String GenerateComponent() {
        return "\"entity_data\":{\n" +
                "\"id\":\"" + id + "\",\n" +
                "\"CustomName\":\"" + CustomName.getText(true) + "\"\n" +
                "}";
    }
}
