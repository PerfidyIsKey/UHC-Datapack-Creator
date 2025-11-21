package arguments.itemstack.components;

import arguments.itemstack.ItemComponentTag;
import shared.GoatHornInstrumentId;

/**
 * Represents the 'instrument' component for goat horns.
 * Format: instrument="minecraft:ponder_goat_horn"
 */
public class InstrumentComponent implements ItemComponentTag {
    private final GoatHornInstrumentId instrumentId;

    private InstrumentComponent(GoatHornInstrumentId instrumentId) {
        this.instrumentId = instrumentId;
    }

    public static InstrumentComponent create(GoatHornInstrumentId instrumentId) {
        return new InstrumentComponent(instrumentId);
    }

    @Override
    public String buildComponentString() {
        // Output format: instrument="minecraft:ponder_goat_horn"
        return "instrument=\"" + instrumentId.getResourceLocation() + "\"";
    }
}