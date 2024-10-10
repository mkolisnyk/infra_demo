package com.sample.it.api;

import com.sample.app.model.TaxRequest;
import com.sample.app.model.TaxResponse;

public class AppClient {
    private String host = "";
    private ClientCore client = new ClientCore();
    public AppClient(String hostValue) {
        this.host = hostValue;
    }

    public TaxResponse calculate(TaxRequest input) throws Exception {
        return client.post(this.host + "/tax", input, TaxResponse.class);
    }
}
