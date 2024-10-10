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
            put(50000.f, 0.2f);
            put(153000.f, 0.4f);
        }
    };
    public float calculateTax1250L(float income) {
        float tax = 0.f;
        float taxedAmount = taxAllowance;
        float lastBorder = 0;
        if (income <= taxAllowance) {
            return tax;
        }
        if (income > taxAllowanceCap) {
            taxedAmount = 0;
        }
        for (Entry<Float, Float> entry : rates.entrySet()) {
            lastBorder = entry.getKey();
            float charge = (Math.min(entry.getKey(), income) - taxedAmount);
            tax += charge * entry.getValue();
            taxedAmount += charge;
            if (income <= entry.getKey()) {
                break;
            }
        }
        if (income > lastBorder) {
            tax += (income - lastBorder) * upperRate;
        }
        return tax;
    }
}
