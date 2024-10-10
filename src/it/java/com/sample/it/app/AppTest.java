package com.sample.it.app;

import com.sample.app.Application;
import com.sample.app.model.TaxRequest;
import com.sample.app.model.TaxResponse;
import com.sample.it.api.AppClient;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.params.provider.Arguments.arguments;

public class AppTest {
    private static List<Arguments> argumentSets() {
        return Arrays.asList(
                Arguments.arguments(0.f, 0.f),
                Arguments.arguments(25000.f, 2500.f),
                Arguments.arguments(50000.f, 7500.f),
                Arguments.arguments(51000.f, 7900.f),
                Arguments.arguments(123000.f, 36700.f),
                Arguments.arguments(124000.f, 39600.f),
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
}
