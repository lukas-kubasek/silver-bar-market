package com.cs.orderboard.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.math.BigDecimal;
import java.util.UUID;

public class Order {

    private String orderId;
    private String userId;
    private BigDecimal quantity;
    private BigDecimal price;
    private OrderType orderType;

    public Order() {
    }

    public Order(String userId, BigDecimal quantity, BigDecimal price, OrderType orderType) {
        this.orderId = UUID.randomUUID().toString();
        this.userId = userId;
        this.quantity = quantity;
        this.price = price;
        this.orderType = orderType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equal(orderId, order.orderId) &&
                Objects.equal(userId, order.userId) &&
                Objects.equal(quantity, order.quantity) &&
                Objects.equal(price, order.price) &&
                orderType == order.orderType;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(orderId, userId, quantity, price, orderType);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("orderId", orderId)
                .add("userId", userId)
                .add("quantity", quantity)
                .add("price", price)
                .add("orderType", orderType)
                .toString();
    }
}
