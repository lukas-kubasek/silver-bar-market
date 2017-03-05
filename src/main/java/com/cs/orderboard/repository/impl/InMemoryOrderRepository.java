package com.cs.orderboard.repository.impl;

import com.cs.orderboard.exception.OrderException;
import com.cs.orderboard.model.Order;
import com.cs.orderboard.repository.api.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.String.format;

@Component
public class InMemoryOrderRepository implements OrderRepository {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Map<String, Order> orders = new ConcurrentHashMap<>();

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(orders.values());
    }

    @Override
    public Order findOneById(String orderId) {
        Order order = orders.get(orderId);
        if (order == null) {
            throw new OrderException(format("Order %s does not exist", orderId));
        }
        return order;
    }

    @Override
    public void insert(Order order) {
        logger.info("Persisting new order {}", order);
        if (presentInRepository(order.getOrderId())) {
            throw new OrderException(format("Order %s already persisted", order.getOrderId()));
        }
        orders.put(order.getOrderId(), order);
    }

    @Override
    public void delete(String orderId) {
        logger.info("Deleting order {}", orderId);
        if (!presentInRepository(orderId)) {
            throw new OrderException(format("Order %s not found", orderId));
        }
        orders.remove(orderId);
    }

    @Override
    public void clean() {
        logger.info("Cleaning orders (current count = {})", orders.size());
        orders.clear();
    }

    private boolean presentInRepository(String orderId) {
        return orders.containsKey(orderId);
    }
}
