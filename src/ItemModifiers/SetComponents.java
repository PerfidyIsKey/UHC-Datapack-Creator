package ItemModifiers;

import ItemClasses.Components;
import java.util.ArrayList;

public class SetComponents implements ItemModifier {
    // Fields
    private ArrayList<Components> components;

    // Constructor
    public SetComponents(ArrayList<Components> components) {
        this.components = components;
    }

    public SetComponents(Components components) {
        ArrayList<Components> componentList = new ArrayList<>();
        componentList.add(components);
        this.components = componentList;
    }

    // Generate function text
    public String GenerateFunction() {

        return "{\n" +
                "\"function\":\"set_components\",\n" +
                "\"components\":" +
                GenerateComponents() +
                "\n" +
                "}";
    }

    private String GenerateComponents() {
        // Create component structure
        String componentContent = "";
        for (int i = 0; i < components.size(); i++) {
            // Select current page
            Components currentComponent = components.get(i);

            // Add content
            componentContent += currentComponent.GenerateComponent();

            if (i < (components.size() - 1)) {   // Extend to next function
                componentContent += ",\n";
            }
        }

        return componentContent;
    }
}
