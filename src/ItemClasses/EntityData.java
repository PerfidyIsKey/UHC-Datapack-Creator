package ItemClasses;

import EntityClasses.Attributes;
import Enums.EntityName;
import HelperClasses.Text;

import java.util.ArrayList;

public class EntityData implements Components{
    // Fields
    EntityName id;  // String representation of the entity's ID. Does not exist for the Player entity.
    Text CustomName;    // Entity name

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
