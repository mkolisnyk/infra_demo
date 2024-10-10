package com.sample.app.domain;

import java.util.Map;

public interface RatesProvider {
    public Map<Float, Float> getRates(TaxCode code, Float income);
}
