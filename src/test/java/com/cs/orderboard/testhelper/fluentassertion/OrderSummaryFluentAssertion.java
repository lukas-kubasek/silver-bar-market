package com.cs.orderboard.testhelper.fluentassertion;

import com.cs.orderboard.model.OrderSummary;
import com.cs.orderboard.model.OrderSummaryItem;

import static com.cs.orderboard.testhelper.BigDecimalSemanticBuilder.ofQuantity;
import static org.assertj.core.api.Assertions.assertThat;

public class OrderSummaryFluentAssertion {

    private OrderSummary orderSummary;

    private OrderSummaryFluentAssertion(OrderSummary orderSummary) {
        this.orderSummary = orderSummary;
    }

    public static OrderSummaryFluentAssertion assertThatOrderSummary(OrderSummary orderSummary) {
        return new OrderSummaryFluentAssertion(orderSummary);
    }

    public OrderSummaryFluentAssertion hasSummaryItemCount(int count) {
        assertThat(orderSummary.getOrderSummaryItems()).hasSize(count);
        return this;
    }

    public OrderSummaryItemAssertion summaryItem(int itemNo) {
        return new OrderSummaryItemAssertion(orderSummary.getOrderSummaryItems().get(itemNo - 1), this);
    }

    public class OrderSummaryItemAssertion {

        private OrderSummaryItem orderSummaryItem;
        private OrderSummaryFluentAssertion parentAssertion;

        private OrderSummaryItemAssertion(OrderSummaryItem orderSummaryItem, OrderSummaryFluentAssertion orderSummaryAssertion) {
            this.orderSummaryItem = orderSummaryItem;
            parentAssertion = orderSummaryAssertion;
        }

        public OrderSummaryFluentAssertion and() {
            return parentAssertion;
        }

        public OrderSummaryItemAssertion hasPrice(String price) {
            assertThat(orderSummaryItem.getPrice()).isEqualTo(ofQuantity(price));
            return this;
        }

        public OrderSummaryItemAssertion hasTotalQuantity(String quantity) {
            assertThat(orderSummaryItem.getTotalQuantity()).isEqualTo(ofQuantity(quantity));
            return this;
        }
    }
}
