package com.sample.app.model;

import com.sample.app.domain.TaxCode;

public class TaxResponse {
    private float allowance;
    private float tax;
    private TaxCode code;

    public float getAllowance() {
        return allowance;
    }

    public void setAllowance(float allowanceValue) {
        this.allowance = allowanceValue;
    }

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
