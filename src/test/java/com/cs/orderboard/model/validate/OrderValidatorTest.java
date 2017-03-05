package com.cs.orderboard.model.validate;

import com.cs.orderboard.model.Order;
import org.junit.Before;
import org.junit.Test;

import static com.cs.orderboard.model.OrderType.BUY;
import static com.cs.orderboard.model.OrderType.SELL;
import static com.cs.orderboard.testhelper.BigDecimalSemanticBuilder.ofPrice;
import static com.cs.orderboard.testhelper.BigDecimalSemanticBuilder.ofQuantity;
import static org.assertj.core.api.Assertions.assertThat;

public class OrderValidatorTest {

    private OrderValidator orderValidator;

    @Before
    public void setUp() {
        this.orderValidator = new OrderValidator();
    }

    @Test
    public void validOrder() {
        Order order = new Order("user", ofQuantity("1.0"), ofPrice("100"), SELL);
        assertThat(orderValidator.isValid(order)).isTrue();
    }

    @Test
    public void invalidOrderMissingAllProperties() {
        Order order = new Order();
        assertThat(orderValidator.isValid(order)).isFalse();
    }

    @Test
    public void invalidOrderContainingAnInvalidProperties() {
        Order order = new Order("user", ofQuantity("-5"), ofPrice("-99"), BUY);
        assertThat(orderValidator.isValid(order)).isFalse();
    }

}