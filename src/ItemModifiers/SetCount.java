package ItemModifiers;

public class SetCount implements ItemModifier {
    // Fields
    private int count;

    // Constructor
    public SetCount(int count) {
        this.count = count;
    }

    // Generate function text
    public String GenerateFunction() {
        return "{\n" +
                "\"function\":\"set_count\",\n" +
                "\"count\":" + count + "\n" +
                "}";
    }
}
