package com.sample.core;

import com.sample.app.domain.TaxCode;
import com.sample.lib.AssertEx;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.params.provider.Arguments.arguments;

public class EngineTest {
    private static final String E_TAX_CODE = "1250L";
    private static final String S_TAX_CODE = "1250S";
    private static final String C_TAX_CODE = "1250C";
    private static List<Arguments> argumentSets() {
        return Arrays.asList(
            arguments(E_TAX_CODE, 0.f, 0.f),
            arguments(E_TAX_CODE, 25000.f, 2500.f),
            arguments(E_TAX_CODE, 50000.f, 7500.f),
            arguments(E_TAX_CODE, 51000.f, 7900.f),
            arguments(E_TAX_CODE, 110000.f, 32500.f),
            arguments(E_TAX_CODE, 123000.f, 39000.f),
            arguments(E_TAX_CODE, 124000.f, 39500.f),
            arguments(E_TAX_CODE, 128000.f, 41200.f),
            arguments(E_TAX_CODE, 150000.f, 50000.f),
            arguments(E_TAX_CODE, 155000.f, 52100.f),
            arguments(E_TAX_CODE, 159000.f, 53900.f),
            arguments(TaxCode.BR.getCode(), 25000.f, 5000.f),
            arguments("NT", 10000.f, 0.f),
            arguments("NT", 12000.f, 0.f),
            arguments("NT", 20000.f, 0.f),
            arguments(S_TAX_CODE, 0.f, 0.f),
            arguments(S_TAX_CODE, 25000.f, 2476.24f),
            arguments(S_TAX_CODE, 50000.f, 9041.609f),
            arguments(S_TAX_CODE, 51000.f, 9461.609f),
            arguments(S_TAX_CODE, 110000.f, 36241.61f),
            arguments(S_TAX_CODE, 123000.f, 43326.61f),
            arguments(S_TAX_CODE, 124000.f, 43871.61f),
            arguments(S_TAX_CODE, 128000.f, 45766.61f),
            arguments(S_TAX_CODE, 150000.f, 55666.61f),
            arguments(S_TAX_CODE, 155000.f, 57916.61f),
            arguments(S_TAX_CODE, 159000.f, 59716.61f),
            arguments(TaxCode.SBR.getCode(), 150000.f, 30000.f),
            arguments(TaxCode.SD0.getCode(), 150000.f, 31499.998f),
            arguments(TaxCode.SD1.getCode(), 150000.f, 62999.996f),
            arguments(TaxCode.SD2.getCode(), 150000.f, 67500.f),
            arguments(TaxCode.SD3.getCode(), 150000.f, 72000.0f),
            arguments(TaxCode.CBR.getCode(), 150000.f, 30000.f),
            arguments(TaxCode.CD0.getCode(), 150000.f, 60000.f),
            arguments(TaxCode.CD1.getCode(), 150000.f, 67500.f),
            arguments(C_TAX_CODE, 0.f, 0.f),
            arguments(C_TAX_CODE, 25000.f, 2500.f),
            arguments(C_TAX_CODE, 50000.f, 7500.f),
            arguments(C_TAX_CODE, 51000.f, 7846.f),
            arguments(C_TAX_CODE, 123000.f, 36646.f),
            arguments(C_TAX_CODE, 124000.f, 37046.f),
            arguments(C_TAX_CODE, 128000.f, 38789.f),
            arguments(C_TAX_CODE, 150000.f, 48689.f),
            arguments(C_TAX_CODE, 155000.f, 50939.f),
            arguments(C_TAX_CODE, 159000.f, 52739.f)
        );
    }

    @ParameterizedTest(name = "Calculate tax for {1}. Code: {0}")
    @MethodSource("argumentSets")
    public void testEngineCalculate1250L(String code, float income, float expectedTax) throws Exception {
        Engine engine = new Engine();
        Assertions.assertEquals(expectedTax, engine.calculateTax(code, income));
    }

    @Test
    public void testEngineCalculateBadCode() throws Exception {
        Engine engine = new Engine();
        Assertions.assertEquals(0.f, engine.calculateTax("BAD CODE", 50000.f));
    }

    @Test
    public void testGetRates() throws Exception {
        Engine engine = new Engine();
        Map<Float, Float> rates = engine.getRates(E_TAX_CODE, 50000.f);
        Map<Float, Float> expectedRates = new LinkedHashMap<>() {
            {
                put(12500.f, 0.f);
                put(50000.f, 0.2f);
                put(153000.f, 0.4f);
                put(Float.MAX_VALUE, 0.45f);
            }
        };
        AssertEx.assertMapEquals(expectedRates, rates, "Unexpected standard rates calculation");
    }

    @Test
    public void testGetRatesForBadTaxCode() throws Exception {
        Engine engine = new Engine();
        Map<Float, Float> rates = engine.getRates("NAAAAH", 50000.f);
        Map<Float, Float> expectedRates = new LinkedHashMap<>();
        AssertEx.assertMapEquals(expectedRates, rates, "Unexpected standard rates calculation");
    }
    private static List<Arguments> argumentSetsForTaxCodes() {
        return Arrays.asList(
                arguments(TaxCode.L),
                arguments(TaxCode.D0),
                arguments(TaxCode.D1),
                arguments(TaxCode.C),
                arguments(TaxCode.CBR),
                arguments(TaxCode.CD0),
                arguments(TaxCode.CD1),
                arguments(TaxCode.S),
                arguments(TaxCode.SBR),
                arguments(TaxCode.SD0),
                arguments(TaxCode.SD1),
                arguments(TaxCode.SD2),
                arguments(TaxCode.SD3)
        );
    }

    @ParameterizedTest(name = "Calculate tax for {0}")
    @MethodSource("argumentSetsForTaxCodes")
    public void testGetRatesForCodes(TaxCode code) throws Exception {
        Engine engine = new Engine();
        Assertions.assertTrue(engine.getRates(code.getCode(), 0.f).size() > 0);
    }

    private static List<Arguments> argumentEmptySetsForTaxCodes() {
        return Arrays.asList(
                arguments(TaxCode.NT),
                arguments(TaxCode.UNKNOWN)
        );
    }
    @ParameterizedTest(name = "Get empty tax rates for {0}")
    @MethodSource("argumentEmptySetsForTaxCodes")
    public void testGetRatesForEmptyCodes(TaxCode code) throws Exception {
        Engine engine = new Engine();
        Assertions.assertEquals(0, engine.getRates(code.getCode(), 0.f).size());
    }
}
