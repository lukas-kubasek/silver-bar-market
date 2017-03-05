package com.cs.orderboard.model;

import com.google.common.base.MoreObjects;

import java.math.BigDecimal;

public class OrderRequest {

    private BigDecimal quantity;
    private BigDecimal price;
    private OrderType orderType;

    public OrderRequest() {
    }

    public OrderRequest(BigDecimal quantity, BigDecimal price, OrderType orderType) {
        this.quantity = quantity;
        this.price = price;
        this.orderType = orderType;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("quantity", quantity)
                .add("price", price)
                .add("orderType", orderType)
                .toString();
    }
}
