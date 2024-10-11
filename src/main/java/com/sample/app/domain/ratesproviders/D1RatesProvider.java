package com.sample.app.domain.ratesproviders;

import com.sample.app.domain.ForCode;
import com.sample.app.domain.RatesProvider;
import com.sample.app.domain.TaxCode;

import java.util.LinkedHashMap;
import java.util.Map;

@ForCode(TaxCode.D1)
public class D1RatesProvider extends RatesProvider {

    @Override
    public Map<Float, Float> getRates(TaxCode code, Float income) {
        Map<Float, Float> rates = new LinkedHashMap<>();
        rates.put(Float.MAX_VALUE, 0.45f);
        return rates;
    }
}
