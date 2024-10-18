package com.sample.app.domain;

import com.sample.core.Engine;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RatesContainer {
    private static final Logger LOGGER = LogManager.getLogger();
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
        LOGGER.info("Calculating allowance");
        if (!this.isUseAllowance()) {
            LOGGER.info(String.format("Allowance is not defined for the tax code %s", code.getCode()));
            return;
        }
        Float taxAllowance = engine.calculateAllowance(code, income);
        LOGGER.info(String.format("Updating rates with %.2f allowance", taxAllowance));
        this.rates = new LinkedHashMap<String, String>();
        rates.put(String.valueOf(taxAllowance), "0.0");
    }
}
