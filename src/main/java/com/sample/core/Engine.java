package com.sample.core;

import java.io.Serial;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Engine {
    private final float taxAllowance = 12500.f;
    private final float taxAllowanceCap = 123000.f;
    private final float upperRate = 0.45f;

    private final Map<Float, Float> rates = new LinkedHashMap<>() {
        @Serial
        private static final long serialVersionUID = 1L;

        {
            put(taxAllowance, 0.f);
            put(50000.f, 0.2f);
            put(153000.f, 0.4f);
            put(Float.MAX_VALUE, 0.45f);
        }
    };
    public float calculateTax1250L(float income) {
        float tax = 0.f;
        float taxedAmount = 0;
        if (income > taxAllowanceCap) {
            rates.remove(taxAllowance);
        }
        for (Entry<Float, Float> entry : rates.entrySet()) {
            float charge = (Math.min(entry.getKey(), income) - taxedAmount);
            tax += charge * entry.getValue();
            taxedAmount += charge;
            if (income <= entry.getKey()) {
                break;
            }
        }
        return tax;
    }
}
