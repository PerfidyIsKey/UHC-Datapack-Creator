package HelperClasses;

import Enums.Color;
import Enums.Objective;

public class Score extends TextItem {
    private String name;
    private String objective;

    public Score(Color color, boolean isBold, boolean isItalic, String name, String objective) {
        super(color, isBold, isItalic);
        this.name = name;
        this.objective = objective;
    }

    public Score(boolean isBold, boolean isItalic, String name, String objective) {
        super(isBold, isItalic);
        this.name = name;
        this.objective = objective;
    }

    public Score(Color color, boolean isBold, boolean isItalic, String name, Objective objective) {
        super(color, isBold, isItalic);
        this.name = name;
        this.objective = objective.toString();
    }

    public Score(boolean isBold, boolean isItalic, String name, Objective objective) {
        super(isBold, isItalic);
        this.name = name;
        this.objective = objective.toString();
    }

    public String getText() {
        return "{\"score\":{\"name\":\"" + name + "\",\"objective\":\"" + objective + "\"}" + bold() + italic() + colorShow() +"}";
    }
}
