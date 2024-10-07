package com.sample;

import java.util.HashMap;
import java.util.Map;

public class Engine {
    private final float TAX_ALLOWANCE = 12500.f;
    private final float TAX_ALLOWANCE_CAP = 123000.f;
    private Map<Float, Float> rates = new HashMap<Float, Float>() {
        {
            put(50000.f, 0.2f);
            put(145000.f, 0.4f);
        }
    };
    public float calculate1250L(float income) {
        float allowance = TAX_ALLOWANCE;
        float tax = 0.f;
        if (income > TAX_ALLOWANCE_CAP) {
            allowance = 0;
        }
        return tax;
    }
}
