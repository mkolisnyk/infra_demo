package com.sample.app.domain;


import com.sample.core.Engine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RatesContainerTest {
    @InjectMocks
    private RatesContainer ratesContainer;
    @Mock
    private Engine engine;

    @Test
    public void testProcessAllowance() {
        given(engine.calculateAllowance(TaxCode.L, 50000.f)).willReturn(999.f);

        ratesContainer.setUseAllowance(true);
        ratesContainer.processAllowance(TaxCode.L, 50000.f);

        Assertions.assertEquals(1, ratesContainer.getRates().size());
        Assertions.assertTrue(ratesContainer.getRates().containsKey("999.0"));
        Assertions.assertEquals("0.0", ratesContainer.getRates().get("999.0"));
        verify(engine, times(1)).calculateAllowance(Mockito.any(), Mockito.anyFloat());
    }
}
