package com.sample.core;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.reflections.Reflections;

import com.sample.app.domain.ForCode;
import com.sample.app.domain.RatesProvider;
import com.sample.app.domain.TaxCode;

public class Engine {

    public float calculateTax(String code, float income) throws Exception {
        float tax = 0.f;
        float taxedAmount = 0;
        Map<Float, Float> rates = getRates(code, income);
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

    public Map<Float, Float> getRates(String codeString, Float income) throws Exception {
        TaxCode code = TaxCode.fromString(codeString);
        Reflections reflections = new Reflections("com.sample.app.domain.ratesproviders");
        Set<Class<? extends RatesProvider>> subTypes = reflections.getSubTypesOf(RatesProvider.class);
        for (Class<? extends RatesProvider> type : subTypes) {
            System.out.println("Class: " + type.getCanonicalName());
            ForCode[] annotations = type.getAnnotationsByType(ForCode.class);
            for (ForCode annotation : annotations) {
                System.out.println("Attribute: " + annotation.value().getCode());
                if (annotation.value().getCode().equals(code.getCode())) {
                    return type.getConstructor().newInstance().getRates(code, income);
                }
            }
        }
        return new LinkedHashMap<Float, Float>();
    }
}
