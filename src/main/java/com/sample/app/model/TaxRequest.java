package com.sample.app.model;

public class TaxRequest {
    private float income;
    private String code;
    public float getIncome() {
        return income;
    }

    public String getCode() {
        return code;
    }

    public void setIncome(float incomeValue) {
        this.income = incomeValue;
    }

    public void setCode(String codeValue) {
        this.code = codeValue;
    }
}
