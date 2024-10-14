package com.sample.app.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.params.provider.Arguments.arguments;

public class TaxCodeTest {
    private static List<Arguments> argumentSets() {
        return Arrays.asList(
                arguments("1257L", TaxCode.L, "1257"),
                arguments("BR", TaxCode.BR, ""),
                arguments("D0", TaxCode.D0, ""),
                arguments("Something", TaxCode.UNKNOWN, ""),
                arguments("1200S", TaxCode.S, "1200"),
                arguments("NT", TaxCode.NT, ""),
                arguments("SBR", TaxCode.SBR, ""),
                arguments("SD0", TaxCode.SD0, ""),
                arguments("SD1", TaxCode.SD1, ""),
                arguments("SD2", TaxCode.SD2, ""),
                arguments("SD3", TaxCode.SD3, ""),
                arguments("1200C", TaxCode.C, "1200"),
                arguments("CD0", TaxCode.CD0, ""),
                arguments("CD1", TaxCode.CD1, "")
        );
    }

    @ParameterizedTest(name = "Calculate tax for {0}")
    @MethodSource("argumentSets")
    public void testFromStringToTaxCode(String input, TaxCode expectedCode, String expectedValue) {
        TaxCode actual = TaxCode.fromString(input);
        Assertions.assertEquals(expectedCode, actual, "Unexpected code detected");
        Assertions.assertEquals(expectedValue, actual.getValue(), "Unexpected value detected");
    }

    @Test
    public void testTaxCodeParameters() {
        TaxCode code = TaxCode.C0T;
        Assertions.assertEquals(TaxCode.C0T.getCode(), code.getCode());
        Assertions.assertEquals(TaxCode.C0T.getDescription(), code.getDescription());
        Assertions.assertEquals(TaxCode.C0T.getValue(), code.getValue());
    }
}
