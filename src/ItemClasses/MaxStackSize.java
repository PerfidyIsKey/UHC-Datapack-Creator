package ItemClasses;

public class MaxStackSize implements Components{
    // Fields
    private int max_stack_size; // The maximum number of items that can fit in a stack. Must be a positive integer between 1 and 99 (inclusive). If it has a value greater than 1 (if the item can be stacked), cannot be combined with max_damage.

    // Constructor
    public MaxStackSize(int size) {
        this.max_stack_size = size;
    }

    // Make NBT tag
    public String GenerateNBT() {
        return null;
    }

    // Make component tag
    public String GenerateComponent() {
        return "\"max_stack_size\":" + max_stack_size + "\n";
    }

    //
}
