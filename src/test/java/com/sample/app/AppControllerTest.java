package com.sample.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.app.domain.TaxCode;
import com.sample.app.model.TaxRequest;
import com.sample.app.model.TaxResponse;
import com.sample.core.Engine;
import com.sample.lib.AssertEx;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {AppConfig.class})
@ExtendWith(MockitoExtension.class)
public class AppControllerTest {
    private static final String L_CODE = "1250L";
    // @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private AppController appController;
    @Mock
    private Engine engine;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(appController)
                .build();
    }

    @Test
    public void testPingEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .head("/api/v1/ping")).andExpect(status().isOk());
    }

    @Test
    void testCalculateTaxEndpoint() throws Exception {
        TaxRequest request = new TaxRequest();
        request.setIncome(50000.f);
        request.setCode(L_CODE);

        given(engine.calculateTax(L_CODE, 50000.f)).willReturn(19500.f);
        given(engine.calculateAllowance(TaxCode.L, 50000.f)).willReturn(12500.f);
        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/tax")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))).andReturn().getResponse();

        TaxResponse result = objectMapper.readValue(response.getContentAsString(), TaxResponse.class);
        Assertions.assertEquals(TaxCode.L, result.getCode());
        Assertions.assertEquals(12500.f, result.getAllowance());
        Assertions.assertEquals(19500.f, result.getTax());
        verify(engine, times(1)).calculateTax(Mockito.anyString(), Mockito.anyFloat());
        verify(engine, times(1)).calculateAllowance(Mockito.any(), Mockito.anyFloat());
    }

    @Test
    void testCalculateTaxesEndpoint() throws Exception {
        TaxRequest[] requests = new TaxRequest[] {
            new TaxRequest(50000.f, L_CODE),
            new TaxRequest(10000.f, "D0"),
            new TaxRequest(40000.f, "1000S")
        };

        given(engine.calculateTax(L_CODE, 50000.f)).willReturn(4500.f);
        given(engine.calculateTax("D0", 10000.f)).willReturn(3500.f);
        given(engine.calculateTax("1000S", 40000.f)).willReturn(5000.f);
        given(engine.calculateAllowance(TaxCode.L, 50000.f)).willReturn(12500.f);
        given(engine.calculateAllowance(TaxCode.D0, 10000.f)).willReturn(0.f);
        given(engine.calculateAllowance(TaxCode.S, 40000.f)).willReturn(10000.f);
        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/taxes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requests))).andReturn().getResponse();

        TaxResponse result = objectMapper.readValue(response.getContentAsString(), TaxResponse.class);
        Assertions.assertEquals(TaxCode.MULTIPLE, result.getCode());
        Assertions.assertEquals(22500.f, result.getAllowance());
        Assertions.assertEquals(13000.f, result.getTax());
        verify(engine, times(3)).calculateTax(Mockito.anyString(), Mockito.anyFloat());
        verify(engine, times(3)).calculateAllowance(Mockito.any(), Mockito.anyFloat());
    }

    @Test
    void testGetRatesEndpoint() throws Exception {
        Map<String, String> rates = new LinkedHashMap<>() {
            {
                put("100.0", "0.0");
                put("200.0", "0.1");
                put("300.0", "0.2");
                put("400.0", "0.3");
                put("500.0", "0.4");
                put("600.0", "0.5");
                put("700.0", "0.6");
            }
        };
        given(engine.getRates(L_CODE, 0.f)).willReturn(rates);
        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/rates/1250L")
                        .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        Map<String, String> actual = objectMapper.readValue(response.getContentAsString(), LinkedHashMap.class);
        AssertEx.assertMapEqualsAsStringMap(rates, actual, "Get rates result is not as expected");
        verify(engine, times(1)).getRates(Mockito.anyString(), Mockito.anyFloat());
    }
}
