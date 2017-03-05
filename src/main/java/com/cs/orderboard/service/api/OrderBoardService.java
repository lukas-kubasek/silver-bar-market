package com.cs.orderboard.service.api;

import com.cs.orderboard.model.*;

public interface OrderBoardService {

    Order registerOrder(OrderRequest orderRequest);

    void cancelOrder(String orderId);

    OrderSummary getOrderSummaryFor(OrderType orderType);
}
