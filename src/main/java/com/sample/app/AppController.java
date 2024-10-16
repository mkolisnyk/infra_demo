package com.sample.app;

import com.sample.app.domain.TaxCode;
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
    private Engine engine = new Engine();
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
        TaxResponse response = new TaxResponse();
        response.setTax(engine.calculateTax(code, income));
        response.setAllowance(engine.calculateAllowance(TaxCode.fromString(code), income));
        response.setCode(TaxCode.fromString(code));
        return ResponseEntity.ok(response);
    }
    @PostMapping("/taxes")
    ResponseEntity<?> calculateTax(@RequestBody TaxRequest[] request) throws Exception {
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
        return ResponseEntity.ok(engine.getRates(code, 0.f));
    }
}
