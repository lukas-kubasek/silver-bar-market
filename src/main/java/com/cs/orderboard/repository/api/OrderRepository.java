package com.cs.orderboard.repository.api;

import com.cs.orderboard.model.Order;

import java.util.List;

public interface OrderRepository {

    List<Order> findAll();

    Order findOneById(String orderId);

    void insert(Order order);

    void delete(String orderId);

    void clean();
}
