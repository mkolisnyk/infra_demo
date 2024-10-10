package com.sample.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.sample.app.domain.TaxCode;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
        Assertions.assertEquals(expectedTax, engine.calculateTax1250L(income));
    }
    
    @Test
    public void testGetRates() throws Exception {
        Engine engine = new Engine();
        Map<Float, Float> rates = engine.getRates("1257L", 50000.f);
        for (Entry<Float, Float> entry : rates.entrySet()) {
        	System.out.println("Rate: " + entry.getKey() + " : " + entry.getValue());
        }
    }
}
