package com.cs.orderboard.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.math.BigDecimal;

public class OrderSummaryItem {

    private BigDecimal price;
    private BigDecimal totalQuantity;

    public OrderSummaryItem() {
    }

    public OrderSummaryItem(BigDecimal price, BigDecimal totalQuantity) {
        this.price = price.stripTrailingZeros();
        this.totalQuantity = totalQuantity.stripTrailingZeros();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(BigDecimal totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderSummaryItem that = (OrderSummaryItem) o;
        return Objects.equal(price, that.price) &&
                Objects.equal(totalQuantity, that.totalQuantity);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(price, totalQuantity);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("price", price)
                .add("totalQuantity", totalQuantity)
                .toString();
    }
}
