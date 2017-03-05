package com.cs.orderboard.service.impl;

import com.cs.orderboard.model.Order;
import com.cs.orderboard.model.OrderSummary;
import com.cs.orderboard.model.OrderSummaryItem;
import com.cs.orderboard.model.OrderType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static com.cs.orderboard.model.OrderType.SELL;

class OrderSummaryCalculator {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private List<Order> orders;

    OrderSummaryCalculator(List<Order> orders) {
        this.orders = orders;
    }

    OrderSummary calculateSummaryFor(OrderType orderType) {
        logger.debug("Calculating {} summary from {} orders", orderType.getDisplayName(), orders.size());
        Map<BigDecimal, BigDecimal> priceQuantityAggregation = aggregateQuantitiesByPrice(orderType);
        List<OrderSummaryItem> summaryItems = createSortedSummaryItems(orderType, priceQuantityAggregation);
        logger.debug("Summarised into {} items", summaryItems.size());
        return new OrderSummary(summaryItems);
    }

    private Map<BigDecimal, BigDecimal> aggregateQuantitiesByPrice(OrderType orderType) {
        return orders.stream()
                .filter(order -> order.getOrderType() == orderType)
                .collect(Collectors.groupingBy(
                        Order::getPrice,
                        sumUpQuantities()));
    }

    private Collector<Order, ?, BigDecimal> sumUpQuantities() {
        return Collectors.reducing(
                BigDecimal.ZERO,
                Order::getQuantity,
                BigDecimal::add);
    }

    private List<OrderSummaryItem> createSortedSummaryItems(OrderType orderType, Map<BigDecimal, BigDecimal> priceQuantityAggregation) {
        return priceQuantityAggregation.entrySet().stream()
                    .map(item -> new OrderSummaryItem(item.getKey(), item.getValue()))
                    .sorted(sortByPrice(orderType))
                    .collect(Collectors.toList());
    }

    private Comparator<OrderSummaryItem> sortByPrice(OrderType orderType) {
        return orderType == SELL
                ? Comparator.comparing(OrderSummaryItem::getPrice)
                : Comparator.comparing(OrderSummaryItem::getPrice).reversed();
    }
}
