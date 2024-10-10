package com.sample.app;

import org.springframework.http.ResponseEntity;

import com.sample.app.model.TaxRequest;
import com.sample.app.model.TaxResponse;
import com.sample.core.Engine;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AppController {
    @GetMapping("/ping")
    ResponseEntity<?> ping() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/tax")
    ResponseEntity<?> calculateTax(@RequestBody TaxRequest request) {
        Engine engine = new Engine();
        TaxResponse response = new TaxResponse();
        response.setTax(engine.calculateTax1250L(request.getIncome()));
        return ResponseEntity.ok(response);
    }
}
