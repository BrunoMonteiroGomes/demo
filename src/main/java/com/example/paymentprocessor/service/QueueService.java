package com.example.paymentprocessor.service;

import com.example.paymentprocessor.controller.dto.PaymentItemDTO;

public interface QueueService {

    void sendToQueue(PaymentItemDTO paymentItemDTO, PaymentStatus status);
}
