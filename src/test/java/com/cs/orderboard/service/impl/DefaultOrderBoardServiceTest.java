package com.cs.orderboard.service.impl;

import com.cs.orderboard.exception.ValidationException;
import com.cs.orderboard.model.Order;
import com.cs.orderboard.model.OrderRequest;
import com.cs.orderboard.model.OrderSummary;
import com.cs.orderboard.model.OrderSummaryItem;
import com.cs.orderboard.model.validate.OrderValidator;
import com.cs.orderboard.repository.api.OrderRepository;
import com.cs.orderboard.service.api.OrderBoardService;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.cs.orderboard.model.OrderType.BUY;
import static com.cs.orderboard.model.OrderType.SELL;
import static com.cs.orderboard.testhelper.BigDecimalSemanticBuilder.ofPrice;
import static com.cs.orderboard.testhelper.BigDecimalSemanticBuilder.ofQuantity;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class DefaultOrderBoardServiceTest {

    private OrderBoardService orderBoardService;

    private OrderValidator orderValidator;
    private OrderRepository orderRepository;
    private OrderRequest orderRequest;

    @Before
    public void setUp() throws Exception {
        this.orderValidator = mock(OrderValidator.class);
        this.orderRepository = mock(OrderRepository.class);
        this.orderRequest = mock(OrderRequest.class);
        this.orderBoardService = new DefaultOrderBoardService(orderValidator, orderRepository);
    }

    @Test
    public void registerValidOrderSucceeds() throws Exception {
        OrderRequest orderRequest = new OrderRequest(ofQuantity("3.5"), ofPrice("303"), SELL);

        Order order = orderBoardService.registerOrder(orderRequest);

        assertThat(order.getOrderId()).isNotBlank();
        assertThat(order.getUserId()).isEqualTo("john.doe");
        assertThat(order.getQuantity()).isEqualTo(ofQuantity("3.5"));
        assertThat(order.getPrice()).isEqualTo(ofQuantity("303"));
        assertThat(order.getOrderType()).isEqualTo(SELL);

        verify(orderValidator, times(1)).validate(order);
        verify(orderRepository, times(1)).insert(order);
    }

    @Test
    public void registerInvalidOrderFailsWithValidationException() throws Exception {
        doThrow(ValidationException.class).when(orderValidator).validate(any(Order.class));
        try {
            orderBoardService.registerOrder(orderRequest);
            fail();
        } catch (Exception e) {
            verify(orderValidator, times(1)).validate(any(Order.class));
            verify(orderRepository, never()).insert(any(Order.class));
        }
    }

    @Test
    public void cancelOrder() throws Exception {
        orderBoardService.cancelOrder("123");
        verify(orderRepository, times(1)).delete("123");
    }

    @Test
    public void getOrderSummaryForSellOrders() throws Exception {
        when(orderRepository.findAll()).thenReturn(asList(
                new Order("user1", ofQuantity("3.5"), ofPrice("306"), SELL),
                new Order("user2", ofQuantity("1.2"), ofPrice("310"), SELL),
                new Order("user3", ofQuantity("1.5"), ofPrice("307"), SELL),
                new Order("user4", ofQuantity("2.0"), ofPrice("306"), SELL)));

        OrderSummary orderSummary = orderBoardService.getOrderSummaryFor(SELL);
        List<OrderSummaryItem> summaryItems = orderSummary.getOrderSummaryItems();

        assertThat(summaryItems).hasSize(3);
        assertThat(summaryItems.get(0)).isEqualTo(new OrderSummaryItem(ofPrice("306"), ofQuantity("5.5")));
        assertThat(summaryItems.get(1)).isEqualTo(new OrderSummaryItem(ofPrice("307"), ofQuantity("1.5")));
        assertThat(summaryItems.get(2)).isEqualTo(new OrderSummaryItem(ofPrice("310"), ofQuantity("1.2")));
    }

    @Test
    public void getOrderSummaryForBuyOrders() throws Exception {
        when(orderRepository.findAll()).thenReturn(asList(
                new Order("user1", ofQuantity("3.5"), ofPrice("306.50"), BUY),
                new Order("user2", ofQuantity("10.2"), ofPrice("310"), BUY),
                new Order("user3", ofQuantity("1.5"), ofPrice("307"), BUY),
                new Order("user4", ofQuantity("2.0"), ofPrice("306.50"), BUY),
                new Order("user4", ofQuantity("2.5"), ofPrice("306"), BUY),
                new Order("user5", ofQuantity("10.5"), ofPrice("307"), BUY)));


        OrderSummary orderSummary = orderBoardService.getOrderSummaryFor(BUY);

        List<OrderSummaryItem> summaryItems = orderSummary.getOrderSummaryItems();
        assertThat(summaryItems).hasSize(4);
        assertThat(summaryItems.get(0)).isEqualTo(new OrderSummaryItem(ofPrice("310"), ofQuantity("10.2")));
        assertThat(summaryItems.get(1)).isEqualTo(new OrderSummaryItem(ofPrice("307"), ofQuantity("12")));
        assertThat(summaryItems.get(2)).isEqualTo(new OrderSummaryItem(ofPrice("306.50"), ofQuantity("5.5")));
        assertThat(summaryItems.get(3)).isEqualTo(new OrderSummaryItem(ofPrice("306"), ofQuantity("2.5")));
    }

}