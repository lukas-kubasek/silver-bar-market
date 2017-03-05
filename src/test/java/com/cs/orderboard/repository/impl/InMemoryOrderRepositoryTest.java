package com.cs.orderboard.repository.impl;

import com.cs.orderboard.exception.OrderException;
import com.cs.orderboard.model.Order;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.cs.orderboard.model.OrderType.SELL;
import static com.cs.orderboard.testhelper.BigDecimalSemanticBuilder.ofPrice;
import static com.cs.orderboard.testhelper.BigDecimalSemanticBuilder.ofQuantity;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryOrderRepositoryTest {

    private InMemoryOrderRepository orderRepository;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        this.orderRepository = new InMemoryOrderRepository();
    }

    @Test
    public void orderIsInserted() {
        Order order = anOrder();

        assertThat(orderRepository.findAll().size()).isEqualTo(0);
        orderRepository.insert(order);
        assertThat(orderRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    public void sameOrderCannotBeInsertedTwice() {
        Order order = anOrder();

        expectedException.expect(OrderException.class);
        expectedException.expectMessage(format("Order %s already persisted", order.getOrderId()));

        orderRepository.insert(order);
        orderRepository.insert(order);
    }

    @Test
    public void orderIsDeleted() {
        Order order = anOrder();

        assertThat(orderRepository.findAll().size()).isEqualTo(0);
        orderRepository.insert(order);
        assertThat(orderRepository.findAll().size()).isEqualTo(1);

        orderRepository.delete(order.getOrderId());
        assertThat(orderRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    public void nonExistingOrderCannotBeDeleted() {
        Order order = anOrder();

        expectedException.expect(OrderException.class);
        expectedException.expectMessage(format("Order %s not found", order.getOrderId()));

        orderRepository.delete(order.getOrderId());
    }

    private Order anOrder() {
        return new Order("user", ofQuantity("1.0"), ofPrice("100"), SELL);
    }

}