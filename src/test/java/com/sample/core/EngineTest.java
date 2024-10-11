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
    private static List<Arguments> argumentSets() {
        return Arrays.asList(
                arguments(0.f, 0.f),
                arguments(25000.f, 2500.f),
                arguments(50000.f, 7500.f),
                arguments(51000.f, 7900.f),
                arguments(123000.f, 36700.f),
                arguments(124000.f, 39600.f),
                arguments(128000.f, 41200.f),
                arguments(150000.f, 50000.f),
                arguments(155000.f, 52100.f),
                arguments(159000.f, 53900.f)
        );
    }

    @ParameterizedTest(name = "Calculate tax for {0}")
    @MethodSource("argumentSets")
    public void testEngineCalculate1250L(float income, float expectedTax) throws Exception {
        Engine engine = new Engine();
        Assertions.assertEquals(expectedTax, engine.calculateTax("1250L", income));
    }

    @Test
    public void testEngineCalculateBadCode() throws Exception {
        Engine engine = new Engine();
        Assertions.assertEquals(0.f, engine.calculateTax("BAD CODE", 50000.f));
    }

    @Test
    public void testGetRates() throws Exception {
        Engine engine = new Engine();
        Map<Float, Float> rates = engine.getRates("1250L", 50000.f);
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
    private static List<Arguments> argumentSetsFroTaxCodes() {
        return Arrays.asList(
                arguments(TaxCode.L),
                arguments(TaxCode.D0),
                arguments(TaxCode.D1)
        );
    }

    @ParameterizedTest(name = "Calculate tax for {0}")
    @MethodSource("argumentSetsFroTaxCodes")
    public void testGetRatesForCodes(TaxCode code) throws Exception {
        Engine engine = new Engine();
        Assertions.assertTrue(engine.getRates(code.getCode(), 0.f).size() > 0);
    }
}
