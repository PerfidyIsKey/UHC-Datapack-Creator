package ItemClasses;

import EntityClasses.Attributes;
import Enums.EntityName;
import HelperClasses.Text;

import java.util.ArrayList;

public class EntityData implements Components{
    // Fields
    EntityName id;  // String representation of the entity's ID. Does not exist for the Player entity.
    float Health;   // Health of entity
    int Tame;   // Indicates whether the entity is tame
    int Variant;    // Variant type
    Text CustomName;    // Entity name
    ArrayList<Attributes> attributes;   // Attributes the entity possesses

    // Constructor
    public EntityData(EntityName id, float health, Boolean tame, int variant, Text name, ArrayList<Attributes> attributes) {
        this.id = id;
        this.Health = health;
        this.Tame = tame ? 1 : 0;
        this.Variant = variant;
        this.CustomName = name;
        this.attributes = attributes;
    }

    public EntityData(EntityName id, float health, Boolean tame, int variant, Text name, Attributes attribute) {
        ArrayList<Attributes> attributes = new ArrayList<>();
        attributes.add(attribute);

        this.id = id;
        this.Health = health;
        this.Tame = tame ? 1 : 0;
        this.Variant = variant;
        this.CustomName = name;
        this.attributes = attributes;
    }


    @Override
    public String GenerateNBT() {
        return null;
    }

    @Override
    public String GenerateComponent() {
        return "\"entity_data\":{\n" +
                "\"id\":\"" + id + "\",\n" +
                "\"Health\":\"" + Health + "f\",\n" +
                "\"Tame\":" + Tame + ",\n" +
                "\"Variant\":" + Variant + ",\n" +
                "\"CustomName\":\"" + CustomName.getText(true) + "\",\n" +
                "\"attributes\":[\n" +
                getAttributes() +
                "]\n" +
                "}";
    }

    private String getAttributes() {
        // Open attribute string
        String attributeContent = "";
        for (int i = 0; i < attributes.size(); i++) {
            // Open content
            attributeContent += "{\n";

            // Select current attribute
            Attributes currentAttribute = attributes.get(i);

            // Add content
            attributeContent += currentAttribute.GetAttribute();

            if (i < (attributes.size() - 1)) {   // Extend to next attribute
                attributeContent += "\n" +
                        "},\n";
            } else {  // Close attributes
                attributeContent += "\n" +
                        "}\n";
            }
        }

        return attributeContent;
    }
}
