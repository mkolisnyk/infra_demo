package com.sample.it.api;

import com.sample.app.model.TaxRequest;
import com.sample.app.model.TaxResponse;

import java.util.Map;

public class AppClient {
    private String host = "";
    private ClientCore client = new ClientCore();
    public AppClient(String hostValue) {
        this.host = hostValue;
    }

    public TaxResponse calculate(TaxRequest input) throws Exception {
        return client.post(this.host + "/tax", input, TaxResponse.class);
    }
    public TaxResponse calculateMultiple(TaxRequest[] input) throws Exception {
        return client.post(this.host + "/taxes", input, TaxResponse.class);
    }
    public Map<Float, Float> getRates(String code) throws Exception {
        return client.get(this.host + "/rates/" + code, Map.class);
    }
}
