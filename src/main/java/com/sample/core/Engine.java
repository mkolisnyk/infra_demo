package com.sample.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.app.domain.RatesContainer;
import com.sample.app.domain.TaxCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

@Service("engine")
public class Engine {
    private static final Logger LOGGER = LogManager.getLogger(Engine.class);

    public float calculateTax(String code, float income) throws Exception {
        LOGGER.info(String.format("Calculating tax for code %s, income %.2f", code, income));
        float tax = 0.f;
        float taxedAmount = 0;
        Map<String, String> rates = getRates(code, income);
        LOGGER.info(String.format("Retrieved rates are: %s", Utils.stringify(rates)));
        for (Entry<String, String> entry : rates.entrySet()) {
            float key = Float.valueOf(entry.getKey());
            float charge = (Math.min(key, income) - taxedAmount);
            tax += charge * Float.valueOf(entry.getValue());
            taxedAmount += charge;
            if (income <= key) {
                break;
            }
        }
        LOGGER.info(String.format("Calculated tax for code %s and income %.2f is %.2f", code, income, tax));
        return tax;
    }

    public Map<String, String> getRates(String codeString, Float income) throws Exception {
        TaxCode code = TaxCode.fromString(codeString);
        LOGGER.info(String.format("Original tax code: %s. Converted tax code: %s", codeString, code));
        ObjectMapper mapper = new ObjectMapper();
        LOGGER.info("Reading rates");
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
            LOGGER.info(String.format("Initial configuration for code %s is: %s",
                    code, Utils.stringify(ratesMap.get(code.getCode()))));
            RatesContainer container = new RatesContainer();
            container.setUseAllowance((Boolean) ratesMap.get(code.getCode()).get("useAllowance"));
            container.processAllowance(code, income);
            container.setRates((Map<String, String>) ratesMap.get(code.getCode()).get("rates"));
            return container.getRates();
        }
        LOGGER.info(String.format("No rates were found for code %s", code));
        return new LinkedHashMap<String, String>();
    }

    public Float calculateAllowance(TaxCode code, Float income) {
        LOGGER.info(String.format("Calculating allowance for code %s, income %.2f", code, income));
        Float taxAllowanceCap = 100000.f;
        Float taxAllowance = 0.f;
        if (code.getValue().matches("(\\d+)")) {
            taxAllowance = Float.valueOf(code.getValue()) * 10.f;
        }
        if (income > taxAllowanceCap) {
            LOGGER.info(String.format("The income of %.2f is bigger than the cap of %.2f Adjusting allowance",
                    income, taxAllowanceCap));
            taxAllowance -= (income - taxAllowanceCap) * 0.5f;
            if (taxAllowance < 0) {
                taxAllowance = 0.f;
            }
        }
        LOGGER.info(String.format("Calculated allowance for tax code %s, income %.2f is %.2f",
                code, income, taxAllowance));
        return taxAllowance;
    }
}
