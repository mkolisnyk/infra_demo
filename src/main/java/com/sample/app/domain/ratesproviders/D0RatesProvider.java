package com.sample.app.domain.ratesproviders;

import com.sample.app.domain.ForCode;
import com.sample.app.domain.RatesProvider;
import com.sample.app.domain.TaxCode;

import java.util.LinkedHashMap;
import java.util.Map;

@ForCode(TaxCode.D0)
public class D0RatesProvider extends RatesProvider {
    @Override
    public Map<Float, Float> getRates(TaxCode code, Float income) {
        Map<Float, Float> rates = new LinkedHashMap<>();
        rates.put(153000.f, 0.4f);
        rates.put(Float.MAX_VALUE, 0.45f);
        return rates;
    }
}
