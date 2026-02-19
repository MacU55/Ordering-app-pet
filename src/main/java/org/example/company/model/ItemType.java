package org.example.company.model;

public enum ItemType {

    FOOD(0.05), EQUIPMENT(0.06), CLOTHES(0.07), UNDEFINED(0.08);

    private final double discountValue;

    ItemType(double value) {
        this.discountValue = value;
    }

    public double getDiscountValue() {
        return discountValue;
    }

}
