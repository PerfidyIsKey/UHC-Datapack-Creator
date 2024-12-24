package HelperClasses;

import Enums.AttributeType;

import javax.swing.text.DefaultStyledDocument;

public class Attribute {
    private AttributeType attribute;
    private double baseValue;

    public Attribute(AttributeType attribute, double baseValue) {
        this.attribute = attribute;
        this.baseValue = baseValue;
    }

    public String setAttributeBase(String entity) {
        return "attribute " + entity + " minecraft:" + attribute + " base set " + baseValue;
    }


}
