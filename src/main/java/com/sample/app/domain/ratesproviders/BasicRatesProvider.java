package com.sample.app.domain.ratesproviders;

import com.sample.app.domain.RatesProvider;
import com.sample.app.domain.TaxCode;

import java.io.Serial;
import java.util.LinkedHashMap;
import java.util.Map;

public class BasicRatesProvider implements RatesProvider {
    @Override
    public Map<Float, Float> getRates(TaxCode code, Float income) {
        Float taxAllowanceCap = 123000.f;
        Float taxAllowance = Float.valueOf(code.getValue()) * 10.f;
        Map<Float, Float> rates = new LinkedHashMap<>();

        if (income < taxAllowanceCap) {
            rates.put(taxAllowance, 0.f);
        }
        rates.put(50000.f, 0.2f);
        rates.put(153000.f, 0.4f);
        rates.put(Float.MAX_VALUE, 0.45f);
        return rates;
    }
}
