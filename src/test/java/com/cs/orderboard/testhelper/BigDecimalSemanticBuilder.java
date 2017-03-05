package com.cs.orderboard.testhelper;

import java.math.BigDecimal;

public class BigDecimalSemanticBuilder {

    public static BigDecimal ofQuantity(String quantity) {
        return new BigDecimal(quantity);
    }

    public static BigDecimal ofPrice(String price) {
        return new BigDecimal(price);
    }

}
