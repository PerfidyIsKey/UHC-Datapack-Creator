package ItemModifiers;

import Predicates.Predicate;
import java.util.ArrayList;

public class SetCount implements ItemModifier {
    // Fields
    private int count;
    private ArrayList<Predicate> conditions;


    // Constructors
    public SetCount(int count) {
        this.count = count;
    }

    public SetCount(int count, ArrayList<Predicate> conditions) {
        this.count = count;
        this.conditions = conditions;
    }

    public SetCount(int count, Predicate condition) {
        ArrayList<Predicate> conditions = new ArrayList<>();
        conditions.add(condition);

        this.count = count;
        this.conditions = conditions;
    }

    // Generate function text
    public String GenerateFunction() {
        String extendConditions = "\n";

        if (conditions != null) { extendConditions = GenerateConditions(); }

        return "{\n" +
                "\"function\":\"set_count\",\n" +
                "\"count\":" + count +
                extendConditions +
                "}";
    }

    private String GenerateConditions() {
        // Create condition structure
        String extendConditions = ",\n" +
                "\"conditions\":[\n";
        for (int i = 0; i < conditions.size(); i++) {
            // Select current condition
            Predicate currentCondition = conditions.get(i);

            // Add content
            extendConditions += currentCondition.GenerateCondition();

            if (i < (conditions.size() - 1)) {   // Extend to next condition
                extendConditions += ",\n";
            } else {  // Close conditions
                extendConditions += "\n" +
                "]\n";
            }
        }

        return extendConditions;
    }
}
