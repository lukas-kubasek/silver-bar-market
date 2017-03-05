package com.cs.orderboard.model;

import java.util.List;

public class OrderSummary {

    private List<OrderSummaryItem> orderSummaryItems;

    public OrderSummary() {
    }

    public OrderSummary(List<OrderSummaryItem> orderSummaryItems) {
        this.orderSummaryItems = orderSummaryItems;
    }

    public List<OrderSummaryItem> getOrderSummaryItems() {
        return orderSummaryItems;
    }

    public void setOrderSummaryItems(List<OrderSummaryItem> orderSummaryItems) {
        this.orderSummaryItems = orderSummaryItems;
    }
}
