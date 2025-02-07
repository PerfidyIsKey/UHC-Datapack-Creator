package ItemClasses;

import EntityClasses.Attributes;
import Enums.EntityName;
import HelperClasses.Text;

import java.util.ArrayList;

public class Horse extends Mob{
    // Fields
    int Tame;
    int Variant;

    // Constructors
    public Horse(float health, Boolean tame, int variant, Text name, ArrayList<Attributes> attributes) {
        super(EntityName.horse, health, name, attributes);
        this.Tame = tame ? 1 : 0;
        this.Variant = variant;
    }

    public Horse(float health, Boolean tame, int variant, Text name, Attributes attribute) {
        super(EntityName.horse, health, name, attribute);
        this.Tame = tame ? 1 : 0;
        this.Variant = variant;
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
}
