package com.sample;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.params.provider.Arguments.arguments;

public class EngineTest {
    private static List<Arguments> argumentSets() {
        return Arrays.asList(
                arguments(0.f, 0.f),
                arguments(25000.f, 1.f),
                arguments(50000.f, 1.f),
                arguments(51000.f, 1.f),
                arguments(123000.f, 1.f),
                arguments(128000.f, 1.f),
                arguments(145000.f, 1.f)
        );
    }


    @ParameterizedTest(name = "Calculate tax for {0}")
    @MethodSource("argumentSets")
    public void testEngineCalculate1250L(float income, float expectedTax) {
        Engine engine = new Engine();
        Assertions.assertEquals(expectedTax, engine.calculate1250L(income));
    }
}
