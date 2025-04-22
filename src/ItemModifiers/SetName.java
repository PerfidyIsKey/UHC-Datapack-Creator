package ItemModifiers;

import HelperClasses.TextItem;

public class SetName implements ItemModifier {
    // Fields
    private TextItem name;

    // Constructor
    public SetName(TextItem name) {
        this.name = name;
    }

    // Generate function text
    public String GenerateFunction() {
        return "{\n" +
                "\"function\":\"set_name\",\n" +
                "\"name\":[\n" +
                name.getText() + "\n" +
                "]\n" +
                "}";
    }
}
