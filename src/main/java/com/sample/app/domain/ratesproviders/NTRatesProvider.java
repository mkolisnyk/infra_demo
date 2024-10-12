package com.sample.app.domain.ratesproviders;

import java.util.HashMap;
import java.util.Map;

import com.sample.app.domain.ForCode;
import com.sample.app.domain.RatesProvider;
import com.sample.app.domain.TaxCode;

@ForCode(TaxCode.NT)
public class NTRatesProvider extends RatesProvider {

    @Override
    public Map<Float, Float> getRates(TaxCode code, Float income) {
        return new HashMap<Float, Float>();
    }
}
