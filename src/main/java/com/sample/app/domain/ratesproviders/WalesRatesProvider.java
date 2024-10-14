package com.sample.app.domain.ratesproviders;

import com.sample.app.domain.ForCode;
import com.sample.app.domain.RatesProvider;
import com.sample.app.domain.TaxCode;

import java.util.LinkedHashMap;
import java.util.Map;

@ForCode(TaxCode.C)
public class WalesRatesProvider extends RatesProvider {
    @Override
    public Map<Float, Float> getRates(TaxCode code, Float income) {
        Float taxAllowance = 0.f;
        if (code.getValue().matches("(\\d+)")) {
            taxAllowance = Float.valueOf(code.getValue()) * 10.f;
        }
        Map<Float, Float> rates = new LinkedHashMap<Float, Float>();

        rates.put(taxAllowance, 0.f);
        rates.put(50270.f, 0.2f);
        rates.put(125140.f, 0.4f);
        rates.put(Float.MAX_VALUE, 0.45f);
        return rates;
    }
}
