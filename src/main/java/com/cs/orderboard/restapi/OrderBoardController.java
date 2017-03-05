package com.cs.orderboard.restapi;

import com.cs.orderboard.model.Order;
import com.cs.orderboard.model.OrderRequest;
import com.cs.orderboard.model.OrderSummary;
import com.cs.orderboard.service.api.OrderBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.cs.orderboard.model.OrderType.BUY;
import static com.cs.orderboard.model.OrderType.SELL;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/order")
public class OrderBoardController {

    private OrderBoardService orderBoardService;

    @Autowired
    public OrderBoardController(OrderBoardService orderBoardService) {
        this.orderBoardService = orderBoardService;
    }

    @RequestMapping(value = "/", method = POST)
    public Order registerOrder(@RequestBody OrderRequest orderRequest) {
        return orderBoardService.registerOrder(orderRequest);
    }

    @RequestMapping(value = "/{orderId}", method = DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelOrder(@PathVariable String orderId) {
        orderBoardService.cancelOrder(orderId);
    }

    @RequestMapping(value = "/summary/sell/", method = GET)
    public OrderSummary sellOrderSummary() {
        return orderBoardService.getOrderSummaryFor(SELL);
    }

    @RequestMapping(value = "/summary/buy/", method = GET)
    public OrderSummary buyOrderSummary() {
        return orderBoardService.getOrderSummaryFor(BUY);
    }
}
