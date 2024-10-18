package com.sample.app.domain;

import com.sample.core.Engine;
import java.util.LinkedHashMap;
import java.util.Map;

public class RatesContainer {
    private Engine engine = new Engine();
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
        Float taxAllowance = engine.calculateAllowance(code, income);

        this.rates = new LinkedHashMap<String, String>();
        rates.put(String.valueOf(taxAllowance), "0.0");
    }
}
