package com.cs.orderboard.service.impl;

import com.cs.orderboard.model.Order;
import com.cs.orderboard.model.OrderRequest;
import com.cs.orderboard.model.OrderSummary;
import com.cs.orderboard.model.OrderType;
import com.cs.orderboard.model.validate.Validator;
import com.cs.orderboard.repository.api.OrderRepository;
import com.cs.orderboard.service.api.OrderBoardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultOrderBoardService implements OrderBoardService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Validator<Order> orderValidator;
    private OrderRepository orderRepository;

    @Autowired
    public DefaultOrderBoardService(Validator<Order> orderValidator, OrderRepository orderRepository) {
        this.orderValidator = orderValidator;
        this.orderRepository = orderRepository;
    }

    @Override
    public Order registerOrder(OrderRequest orderRequest) {
        logger.info("Registering new order {}", orderRequest);
        Order order = new Order(getAuthenticatedUserId(), orderRequest.getQuantity(), orderRequest.getPrice(), orderRequest.getOrderType());
        orderValidator.validate(order);
        orderRepository.insert(order);
        return order;
    }

    @Override
    public void cancelOrder(String orderId) {
        logger.info("Cancelling order {}", orderId);
        orderRepository.delete(orderId);
    }

    @Override
    public OrderSummary getOrderSummaryFor(OrderType orderType) {
        logger.info("Generating summary for {} orders", orderType.getDisplayName());
        List<Order> allOrders = orderRepository.findAll();
        return new OrderSummaryCalculator(allOrders).calculateSummaryFor(orderType);
    }

    /**
     * In real-life application the authenticated user would be provided by some sort of authentication service.
     * For simplicity we just return a hardcoded username here for now.
     */
    private String getAuthenticatedUserId() {
        return "john.doe";
    }
}
