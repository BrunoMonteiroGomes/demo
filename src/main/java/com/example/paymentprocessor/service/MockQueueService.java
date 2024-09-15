package com.example.paymentprocessor.service;

import com.example.paymentprocessor.controller.dto.PaymentItemDTO;
import org.springframework.stereotype.Service;

@Service
public class MockQueueService implements QueueService {

    @Override
    public void sendToQueue(PaymentItemDTO paymentItemDTO, PaymentStatus status) {

    }
}
