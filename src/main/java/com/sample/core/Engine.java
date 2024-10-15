package com.sample.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.app.domain.RatesContainer;
import com.sample.app.domain.TaxCode;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;


public class Engine {

    public float calculateTax(String code, float income) throws Exception {
        float tax = 0.f;
        float taxedAmount = 0;
        Map<String, String> rates = getRates(code, income);
        for (Entry<String, String> entry : rates.entrySet()) {
            float key = Float.valueOf(entry.getKey());
            float charge = (Math.min(key, income) - taxedAmount);
            tax += charge * Float.valueOf(entry.getValue());
            taxedAmount += charge;
            if (income <= key) {
                break;
            }
        }
        return tax;
    }

    public Map<String, String> getRates(String codeString, Float income) throws Exception {
        TaxCode code = TaxCode.fromString(codeString);
        ObjectMapper mapper = new ObjectMapper();
        LinkedHashMap<String, LinkedHashMap> ratesMap = new LinkedHashMap<String, LinkedHashMap>();
        InputStream resource = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("./config_rates.json");
        try {
            ratesMap =
                    mapper.readValue(resource, LinkedHashMap.class);
        } finally {
            resource.close();
        }
        if (ratesMap.containsKey(code.getCode())) {
            RatesContainer container = new RatesContainer();
            container.setUseAllowance((Boolean) ratesMap.get(code.getCode()).get("useAllowance"));
            container.processAllowance(code, income);
            container.setRates((Map<String, String>) ratesMap.get(code.getCode()).get("rates"));
            return container.getRates();
        }
        return new LinkedHashMap<String, String>();
    }

    public Float calculateAllowance(TaxCode code, Float income) {
        Float taxAllowanceCap = 100000.f;
        Float taxAllowance = 0.f;
        if (code.getValue().matches("(\\d+)")) {
            taxAllowance = Float.valueOf(code.getValue()) * 10.f;
        }
        if (income > taxAllowanceCap) {
            taxAllowance -= (income - taxAllowanceCap) * 0.5f;
            if (taxAllowance < 0) {
                taxAllowance = 0.f;
            }
        }
        return taxAllowance;
    }
}
