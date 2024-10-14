package com.sample.app.domain;


import java.util.LinkedHashMap;
import java.util.Map;

public class RatesContainer {
    private boolean useAllowance;
    private Map<String, String> rates;

    public boolean isUseAllowance() {
        return useAllowance;
    }

    public void setUseAllowance(boolean useAllowanceValue) {
        this.useAllowance = useAllowanceValue;
    }

    public Map<String, String> getRates() {
        return rates;
    }

    public void setRates(Map<String, String> ratesValue) {
        if (this.rates == null) {
            this.rates = new LinkedHashMap<String, String>();
        }
        this.rates.putAll(ratesValue);
    }

    public void processAllowance(TaxCode code, float income) {
        if (!this.isUseAllowance()) {
            return;
        }
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
        this.rates = new LinkedHashMap<String, String>();
        rates.put(String.valueOf(taxAllowance), "0.0");
    }
}
