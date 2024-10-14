package com.sample.app.domain;

public enum TaxCode {
    L("L", "You’re entitled to the standard tax-free Personal Allowance"),
    M("M", "Marriage Allowance: you’ve received a transfer "
            + "of 10% of your partner’s Personal Allowance"),
    N("N", "Marriage Allowance: you’ve transferred 10% of your "
            + "Personal Allowance to your partner"),
    T("T", "Your tax code includes other calculations to work out "
            + "your Personal Allowance"),
    OT("0T", "Your Personal Allowance has been used up, or you’ve "
            + "started a new job and your employer does not have the details they need to give you a tax code"),
    BR("BR", "All your income from this job or pension is taxed at "
            + "the basic rate (usually used if you’ve got more than one job or pension)"),
    D0("D0", "All your income from this job or pension is taxed at the higher rate "
            + "(usually used if you’ve got more than one job or pension)"),
    D1("D1", "All your income from this job or pension is taxed at the additional rate "
            + "(usually used if you’ve got more than one job or pension)"),
    NT("NT", "You’re not paying any tax on this income"),
    S("S", "Your income or pension is taxed using the rates in Scotland"),
    S0T("S0T", "Your Personal Allowance (Scotland) has been used up, or you’ve started"
            + " a new job and your employer does not have the details they need to give you a tax code"),
    SBR("SBR", "All your income from this job or pension is taxed at the basic"
            + " rate in Scotland (usually used if you’ve got more than one job or pension)"),
    SD0("SD0", "All your income from this job or pension is taxed"
            + " at the intermediate rate in Scotland (usually used if you’ve got more than one job or pension)"),
    SD1("SD1", "All your income from this job or pension is taxed at"
            + " the higher rate in Scotland (usually used if you’ve got more than one job or pension)"),
    SD2("SD2", "All your income from this job or pension is taxed at the"
            + " advanced rate in Scotland (usually used if you’ve got more than one job or pension)"),
    SD3("SD3", "All your income from this job or pension is taxed"
            + " at the top rate in Scotland (usually used if you’ve got more than one job or pension)"),
    C("C", "Your income or pension is taxed using the rates in Wales"),
    C0T("C0T", "Your Personal Allowance (Wales) has been used up, or you’ve started"
            + " a new job and your employer does not have the details they need to give you a tax code"),
    CBR("CBR", "All your income from this job or pension is taxed at the basic"
            + " rate in Wales (usually used if you’ve got more than one job or pension)"),
    CD0("CD0", "All your income from this job or pension is taxed at the higher"
            + " rate in Wales (usually used if you’ve got more than one job or pension)"),
    CD1("CD1", "All your income from this job or pension is taxed at the"
            + " additional rate in Wales (usually used if you’ve got more than one job or pension)"),
    MULTIPLE("MUL", "Multiple tax codes combined"),
    UNKNOWN("UNK", "Unknown");

    private final String code;
    private final String description;
    private String value = "";
    TaxCode(String codeValue, String descriptionValue) {
        this.code = codeValue;
        this.description = descriptionValue;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getValue() {
        return value;
    }

    private void setValue(String newValue) {
        this.value = newValue;
    }

    public static TaxCode fromString(String code) {
        for (TaxCode item : TaxCode.values()) {
            if (code.matches("(\\d+)" + item.getCode()) || code.equals(item.getCode())) {
                item.setValue(code.substring(0, code.lastIndexOf(item.getCode())));
                return item;
            }
        }
        return UNKNOWN;
    }
}
