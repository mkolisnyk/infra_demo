package com.sample.app.domain;

import java.util.Map;

public abstract class RatesProvider {
    public abstract Map<Float, Float> getRates(TaxCode code, Float income);
}
