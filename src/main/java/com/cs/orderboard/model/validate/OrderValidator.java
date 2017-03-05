package com.cs.orderboard.model.validate;

import com.cs.orderboard.exception.ValidationException;
import com.cs.orderboard.model.Order;
import org.springframework.stereotype.Component;

import static java.lang.String.format;
import static java.math.BigDecimal.ZERO;
import static org.springframework.util.StringUtils.isEmpty;

@Component
public class OrderValidator implements Validator<Order> {

    public boolean isValid(Order order) {
        return !isEmpty(order.getOrderId())
                && !isEmpty(order.getUserId())
                && order.getQuantity() != null && order.getQuantity().compareTo(ZERO) > 0
                && order.getPrice()!= null && order.getPrice().compareTo(ZERO) > 0
                && order.getOrderType() != null;
    }

    public void validate(Order order) {
        if (!isValid(order)) {
            throw new ValidationException(format("Order %s is not valid", order.getOrderId()));
        }
    }
}
