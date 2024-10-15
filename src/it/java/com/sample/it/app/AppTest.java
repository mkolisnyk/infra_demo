package com.sample.it.app;

import com.sample.app.Application;
import com.sample.app.domain.TaxCode;
import com.sample.app.model.TaxRequest;
import com.sample.app.model.TaxResponse;
import com.sample.it.api.AppClient;
import com.sample.lib.AssertEx;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AppTest {
    private static List<Arguments> argumentSets() {
        return Arrays.asList(
                Arguments.arguments(0.f, 0.f),
                Arguments.arguments(25000.f, 2500.f),
                Arguments.arguments(50000.f, 7500.f),
                Arguments.arguments(51000.f, 7900.f),
                Arguments.arguments(123000.f, 39000.f),
                Arguments.arguments(124000.f, 39500.f),
                Arguments.arguments(128000.f, 41200.f),
                Arguments.arguments(150000.f, 50000.f),
                Arguments.arguments(155000.f, 52100.f)
        );
    }

    private static ConfigurableApplicationContext ctx;
    private AppClient client;

    @BeforeAll
    public static void startUp() {
        String[] args = {};
        ctx = SpringApplication.run(Application.class, args);
    }

    @AfterAll
    public static void shutDown() {
        ctx.close();
    }

    @BeforeEach
    public void setUp() {
        client = new AppClient("http://localhost:8080");
    }

    @ParameterizedTest(name = "App request Calculate tax for {0}")
    @MethodSource("argumentSets")
    public void testAppCalculate1250L(float income, float expectedTax) throws Exception {
        TaxRequest input = new TaxRequest();
        input.setIncome(income);

        TaxResponse result = client.calculate(input);
        Assertions.assertEquals(expectedTax, result.getTax());
    }

    private static List<Arguments> argumentSetsWithTaxCode() {
        return Arrays.asList(
                Arguments.arguments(0.f, "1000L", 0.f, TaxCode.L, "1000", 10000.f),
                Arguments.arguments(25000.f, "500L", 4000.f, TaxCode.L, "500", 5000.f),
                Arguments.arguments(50000.f, "1250L", 7500.f, TaxCode.L, "1250", 12500.f),
                Arguments.arguments(51000.f, "D0", 20400.f, TaxCode.D0, "", 0.f),
                Arguments.arguments(123000.f, "D1", 55350.f, TaxCode.D1, "", 0.f)
        );
    }

    @ParameterizedTest(name = "App request Calculate tax (code {1}) for {0}")
    @MethodSource("argumentSetsWithTaxCode")
    public void testAppCalculate(float income, String code, float expectedTax,
                                 TaxCode expectedCode, String expectedValue,
                                 Float expectedAllowance) throws Exception {
        TaxRequest input = new TaxRequest();
        input.setIncome(income);
        input.setCode(code);

        TaxResponse result = client.calculate(input);
        Assertions.assertEquals(expectedTax, result.getTax());
        Assertions.assertEquals(expectedCode, result.getCode());
        Assertions.assertEquals(expectedValue, result.getCode().getValue());
        Assertions.assertEquals(expectedAllowance, result.getAllowance());
    }

    private Map<String, String> convertToStringMap(Map<Float, Float> input) {
        Map<String, String> output = new LinkedHashMap<>();
        for (Map.Entry entry : input.entrySet()) {
            output.put(entry.getKey().toString(), entry.getValue().toString());
        }
        return output;
    }

    @Test
    public void testGetRates1250L() throws Exception {
        Map<Float, Float> expectedRates = new LinkedHashMap<>() {
            {
                put(12500.f, 0.f);
                put(50000.f, 0.2f);
                put(153000.f, 0.4f);
                put(Float.MAX_VALUE, 0.45f);
            }
        };
        Map<Float, Float> rates = client.getRates("1250L");
        AssertEx.assertMapEquals(
                convertToStringMap(expectedRates),
                convertToStringMap(rates),
                "Unexpected standard rates calculation");
    }

    @Test
    public void testCalculateMultipleTages() throws Exception {
        TaxRequest[] inputs = new TaxRequest[3];
        inputs[0] = new TaxRequest();
        inputs[0].setIncome(10000);
        inputs[0].setCode(TaxCode.BR.getCode());

        inputs[1] = new TaxRequest();
        inputs[1].setIncome(20000);
        inputs[1].setCode(TaxCode.D0.getCode());

        inputs[2] = new TaxRequest();
        inputs[2].setIncome(50000);
        inputs[2].setCode("1250L");

        TaxResponse result = client.calculateMultiple(inputs);
        Assertions.assertEquals(17500.f, result.getTax());
        Assertions.assertEquals(TaxCode.MULTIPLE, result.getCode());
        Assertions.assertEquals("", result.getCode().getValue());
    }
}
