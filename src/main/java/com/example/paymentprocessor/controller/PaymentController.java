package com.example.paymentprocessor.controller;


import com.example.paymentprocessor.controller.dto.PaymentDTO;
import com.example.paymentprocessor.service.PaymentProcessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PaymentController {

    private final PaymentProcessorService paymentProcessorService;

    @PutMapping(path = "/api/payment")
    public ResponseEntity<PaymentDTO> setPayment(@RequestBody PaymentDTO request) {

        try {
            PaymentDTO processedPayment = paymentProcessorService.processPayment(request);
            return ResponseEntity.ok(processedPayment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
