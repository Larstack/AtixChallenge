package com.atixlabs.utils;

import java.math.BigDecimal;

/**
 * @author larstack
 */
public class Utils {

    public static BigDecimal round(Double value) {
        return BigDecimal.valueOf(value).setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }
}
