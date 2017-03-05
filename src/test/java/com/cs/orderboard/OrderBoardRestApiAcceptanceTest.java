package com.cs.orderboard;

import com.cs.orderboard.model.Order;
import com.cs.orderboard.model.OrderSummary;
import com.cs.orderboard.repository.api.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.Contains;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.cs.orderboard.model.OrderType.SELL;
import static com.cs.orderboard.testhelper.fluentassertion.OrderFluentAssertion.assertThatOrder;
import static com.cs.orderboard.testhelper.fluentassertion.OrderSummaryFluentAssertion.assertThatOrderSummary;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class OrderBoardRestApiAcceptanceTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("unused")
    private JacksonTester<Order> jsonOrder;

    @SuppressWarnings("unused")
    private JacksonTester<OrderSummary> jsonOrderSummary;

    @Before
    public void setup() {
        ObjectMapper objectMapper = new ObjectMapper();
        JacksonTester.initFields(this, objectMapper);
    }

    @Autowired
    private OrderRepository orderRepository;

    @Before
    public void cleanRepository() {
        orderRepository.clean();
    }

    @Test
    public void registerNewValidOrder() throws Exception {
        // Given
        assertThat(orderRepository.findAll()).isEmpty();

        // When
        Order newOrder = postNewOrder("{ \"quantity\": 3.5, \"price\": 306, \"orderType\": \"SELL\"}");

        // Then
        assertThat(orderRepository.findAll()).hasSize(1);
        assertThatOrder(orderRepository.findOneById(newOrder.getOrderId()))
                .hasOrderId(newOrder.getOrderId())
                .hasUserId("john.doe")
                .hasQuantity("3.5")
                .hasPrice("306")
                .hasOrderType(SELL);
    }

    @Test
    public void registerNewInvalidOrderFails() throws Exception {
        // Given
        assertThat(orderRepository.findAll()).isEmpty();

        // When
        ResultActions resultActions = mockMvc.perform(post("/order/")
                .contentType(APPLICATION_JSON_UTF8)
                .content("{ \"price\": -999, \"orderType\": \"BUY\"}"));

        // Then
        resultActions
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(new Contains("is not valid")));
        assertThat(orderRepository.findAll()).isEmpty();
    }

    @Test
    public void cancelExistingOrder() throws Exception {
        // Given
        Order order = postNewOrder("{ \"quantity\": 1.2, \"price\": 310, \"orderType\": \"SELL\"}");
        assertThat(orderRepository.findAll()).hasSize(1);

        // When
        mockMvc.perform(delete("/order/{orderId}", order.getOrderId()))
                .andExpect(status().isNoContent());

        // Then
        assertThat(orderRepository.findAll()).isEmpty();
    }

    @Test
    public void cancelNonExistingOrderFails() throws Exception {
        // Given
        assertThat(orderRepository.findAll()).hasSize(0);

        // When
        ResultActions resultActions = mockMvc.perform(delete("/order/{orderId}", "some-invalid-orderId"));

        // Then
        resultActions
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Order some-invalid-orderId not found"));
        assertThat(orderRepository.findAll()).isEmpty();
    }

    @Test
    public void boardSummaryForSellOrders() throws Exception {
        // Given
        postNewOrder("{ \"quantity\": 3.5, \"price\": 306, \"orderType\": \"SELL\"}");
        postNewOrder("{ \"quantity\": 1.2, \"price\": 310, \"orderType\": \"SELL\"}");
        postNewOrder("{ \"quantity\": 1.5, \"price\": 307, \"orderType\": \"SELL\"}");
        postNewOrder("{ \"quantity\": 2.0, \"price\": 306, \"orderType\": \"SELL\"}");
        postNewOrder("{ \"quantity\": 9.9, \"price\": 999, \"orderType\": \"BUY\"}");
        assertThat(orderRepository.findAll()).hasSize(5);

        // When
        String response = mockMvc.perform(get("/order/summary/sell/")
                .accept(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        OrderSummary sellOrderSummary = jsonOrderSummary.parseObject(response);

        // Then
        assertThatOrderSummary(sellOrderSummary)
                .hasSummaryItemCount(3)
                .summaryItem(1)
                    .hasPrice("306")
                    .hasTotalQuantity("5.5")
                .and().summaryItem(2)
                    .hasPrice("307")
                    .hasTotalQuantity("1.5")
                .and().summaryItem(3)
                    .hasPrice("310")
                    .hasTotalQuantity("1.2");
    }

    @Test
    public void boardSummaryForBuyOrders() throws Exception {
        // Given
        postNewOrder("{ \"quantity\": 1.5, \"price\": 307, \"orderType\": \"BUY\"}");
        postNewOrder("{ \"quantity\": 2.0, \"price\": 300, \"orderType\": \"BUY\"}");
        postNewOrder("{ \"quantity\": 1.2, \"price\": 310, \"orderType\": \"BUY\"}");
        postNewOrder("{ \"quantity\": 3.5, \"price\": 306, \"orderType\": \"BUY\"}");
        postNewOrder("{ \"quantity\": 2.0, \"price\": 306, \"orderType\": \"BUY\"}");
        postNewOrder("{ \"quantity\": 9.9, \"price\": 999, \"orderType\": \"SELL\"}");
        assertThat(orderRepository.findAll()).hasSize(6);

        // When
        String response = mockMvc.perform(get("/order/summary/buy/")
                .accept(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        OrderSummary buyOrderSummary = jsonOrderSummary.parseObject(response);

        // Then
        assertThatOrderSummary(buyOrderSummary)
                .hasSummaryItemCount(4)
                .summaryItem(1)
                    .hasPrice("310")
                    .hasTotalQuantity("1.2")
                .and().summaryItem(2)
                    .hasPrice("307")
                    .hasTotalQuantity("1.5")
                .and().summaryItem(3)
                    .hasPrice("306")
                    .hasTotalQuantity("5.5")
                .and().summaryItem(4)
                    .hasPrice("300")
                    .hasTotalQuantity("2");
    }

    private Order postNewOrder(String requestJsonPayload) throws Exception {
        String response = mockMvc.perform(post("/order/")
                .contentType(APPLICATION_JSON_UTF8)
                .content(requestJsonPayload)
                .accept(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return jsonOrder.parseObject(response);
    }
}
