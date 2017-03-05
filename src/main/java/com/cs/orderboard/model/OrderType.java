package com.cs.orderboard.model;

public enum OrderType {

    BUY("Buy"), SELL("Sell");

    private String displayName;

    OrderType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
