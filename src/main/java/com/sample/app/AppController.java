package com.sample.app;

import com.sample.app.domain.TaxCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.sample.app.model.TaxRequest;
import com.sample.app.model.TaxResponse;
import com.sample.core.Engine;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/v1")
public class AppController {
    private static final Logger LOGGER = LogManager.getLogger(AppController.class);
    @Autowired
    private Engine engine;

    public AppController(Engine engineObject) {
        this.engine = engineObject;
    }

    @GetMapping("/ping")
    ResponseEntity<?> ping() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/tax")
    ResponseEntity<?> calculateTax(@RequestBody TaxRequest request) throws Exception {
        float income = request.getIncome();
        String code = request.getCode();
        if (code == null) {
            code = "1250L";
        }
        LOGGER.info(String.format("Calculating tax for code %s and income %.2f", code, income));
        TaxResponse response = new TaxResponse();
        response.setTax(engine.calculateTax(code, income));
        response.setAllowance(engine.calculateAllowance(TaxCode.fromString(code), income));
        response.setCode(TaxCode.fromString(code));
        return ResponseEntity.ok(response);
    }
    @PostMapping("/taxes")
    ResponseEntity<?> calculateTax(@RequestBody TaxRequest[] request) throws Exception {
        LOGGER.info("Multiple tax calculation");
        float totalTax = 0.f;
        float totalAllowance = 0.f;
        for (TaxRequest item : request) {
            float income = item.getIncome();
            String code = item.getCode();
            if (code == null) {
                code = "1250L";
            }
            totalTax += engine.calculateTax(code, income);
            totalAllowance += engine.calculateAllowance(TaxCode.fromString(code), income);
        }
        TaxResponse response = new TaxResponse();
        response.setTax(totalTax);
        response.setAllowance(totalAllowance);
        response.setCode(TaxCode.MULTIPLE);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/rates/{code}")
    ResponseEntity<?> getRates(@PathVariable("code") String code) throws Exception {
        LOGGER.info(String.format("Retrieving rates for code %s", code));
        return ResponseEntity.ok(engine.getRates(code, 0.f));
    }
}
