package com.cs.orderboard.testhelper.fluentassertion;

import com.cs.orderboard.model.Order;
import com.cs.orderboard.model.OrderType;

import static com.cs.orderboard.testhelper.BigDecimalSemanticBuilder.ofQuantity;
import static org.assertj.core.api.Assertions.assertThat;

public class OrderFluentAssertion {

    private Order order;

    private OrderFluentAssertion(Order order) {
        this.order = order;
    }

    public static OrderFluentAssertion assertThatOrder(Order order) {
        return new OrderFluentAssertion(order);
    }

    public OrderFluentAssertion hasOrderId(String orderId) {
        assertThat(order.getOrderId()).isEqualTo(orderId);
        return this;
    }

    public OrderFluentAssertion hasUserId(String userId) {
        assertThat(order.getUserId()).isEqualTo(userId);
        return this;
    }

    public OrderFluentAssertion hasQuantity(String quantity) {
        assertThat(order.getQuantity()).isEqualTo(ofQuantity(quantity));
        return this;
    }

    public OrderFluentAssertion hasPrice(String price) {
        assertThat(order.getPrice()).isEqualTo(ofQuantity(price));
        return this;
    }

    public OrderFluentAssertion hasOrderType(OrderType orderType) {
        assertThat(order.getOrderType()).isEqualTo(orderType);
        return this;
    }
}
