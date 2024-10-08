package com.sample.core;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Engine {
    private final float TAX_ALLOWANCE = 12500.f;
    private final float TAX_ALLOWANCE_CAP = 123000.f;
    private final float UPPER_RATE = 0.45f;
    
    private Map<Float, Float> rates = new LinkedHashMap<Float, Float>() {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
            put(50000.f, 0.2f);
            put(145000.f, 0.4f);
        }
    };
    public float calculateTax1250L(float income) {
        float tax = 0.f;
        float taxedAmount = TAX_ALLOWANCE;
        float lastBorder = 0;
        if (income <= TAX_ALLOWANCE) {
        	return tax;
        }
        if (income > TAX_ALLOWANCE_CAP) {
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
        	tax += (income - lastBorder) * UPPER_RATE;
        }
        return tax;
    }
}
