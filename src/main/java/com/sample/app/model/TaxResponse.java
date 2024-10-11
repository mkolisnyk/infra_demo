package com.sample.app.model;

import com.sample.app.domain.TaxCode;

public class TaxResponse {
    private float tax;
    private TaxCode code;

    public float getTax() {
        return tax;
    }

    public void setTax(float taxValue) {
        this.tax = taxValue;
    }

    public TaxCode getCode() {
        return code;
    }

    public void setCode(TaxCode codeValue) {
        this.code = codeValue;
    }
}
