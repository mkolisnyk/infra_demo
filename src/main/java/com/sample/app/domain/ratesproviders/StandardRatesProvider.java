package com.sample.app.domain.ratesproviders;

import com.sample.app.domain.ForCode;
import com.sample.app.domain.RatesProvider;
import com.sample.app.domain.TaxCode;

import java.util.LinkedHashMap;
import java.util.Map;

@ForCode(TaxCode.L)
public class StandardRatesProvider extends RatesProvider {
    @Override
    public Map<Float, Float> getRates(TaxCode code, Float income) {
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
        Map<Float, Float> rates = new LinkedHashMap<Float, Float>();
        rates.put(taxAllowance, 0.f);
        rates.put(50000.f, 0.2f);
        rates.put(153000.f, 0.4f);
        rates.put(Float.MAX_VALUE, 0.45f);
        return rates;
    }
}
