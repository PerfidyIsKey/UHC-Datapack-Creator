package ItemClasses;

import EntityClasses.Attributes;
import Enums.EntityName;
import HelperClasses.Text;

import java.util.ArrayList;

public class Mob extends EntityData{
    // Fields
    float Health; // Number of health the entity has.
    ArrayList<Attributes> attributes;   // A list of Attributes for this mob. These are used for many purposes in internal calculations, and can be considered a mob's "statistics".

    // Constructors
    public Mob(EntityName id, float health, Text name, ArrayList<Attributes> attributes) {
        super(id, name);
        this.Health = health;
        this.attributes = attributes;
    }

    public Mob(EntityName id, float health, Text name, Attributes attribute) {
        super(id, name);
        this.Health = health;
        ArrayList<Attributes> attributes = new ArrayList<>();
        attributes.add(attribute);
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
                "\"CustomName\":\"" + CustomName.getText(true) + "\",\n" +
                "\"attributes\":[\n" +
                getAttributes() +
                "]\n" +
                "}";
    }

    protected String getAttributes() {
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
