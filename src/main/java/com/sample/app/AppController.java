package com.sample.app;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sample.app.model.TaxRequest;
import com.sample.app.model.TaxResponse;
import com.sample.core.Engine;


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