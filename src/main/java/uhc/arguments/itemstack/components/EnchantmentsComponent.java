package uhc.arguments.itemstack.components;

import uhc.arguments.itemstack.ItemComponentTag;
import uhc.resource.EnchantmentId;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Represents the 'enchantments' component.
 * Format: enchantments={"minecraft:vanishing_curse":1}
 */
public class EnchantmentsComponent implements ItemComponentTag {
    private final Map<EnchantmentId, Integer> enchantments;

    private EnchantmentsComponent(Map<EnchantmentId, Integer> enchantments) {
        this.enchantments = enchantments;
    }

    public static EnchantmentsComponent create(Map<EnchantmentId, Integer> enchantments) {
        return new EnchantmentsComponent(enchantments);
    }

    @Override
    public String buildComponentString() {
        if (enchantments.isEmpty()) {
            return "";
        }

        StringJoiner innerJoiner = new StringJoiner(",", "{", "}");

        for (Map.Entry<EnchantmentId, Integer> entry : enchantments.entrySet()) {
            // Inner format: "minecraft:vanishing_curse":1
            String key = "\"" + entry.getKey().getResourceLocation() + "\"";
            String value = String.valueOf(entry.getValue());
            innerJoiner.add(key + ":" + value);
        }

        // Final output format: enchantments={"minecraft:vanishing_curse":1}
        return "enchantments=" + innerJoiner;
    }
}