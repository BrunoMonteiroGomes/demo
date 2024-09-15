package com.example.paymentprocessor.service;

import com.example.paymentprocessor.repository.ClientRepository;
import com.example.paymentprocessor.repository.PaymentRepository;
import com.example.paymentprocessor.repository.entity.Client;
import com.example.paymentprocessor.repository.entity.Payment;
import com.example.paymentprocessor.controller.dto.PaymentItemDTO;
import com.example.paymentprocessor.controller.dto.PaymentDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentProcessorService {


    private final ClientRepository clientRepository;
    private final PaymentRepository paymentRepository;
    private final QueueService queueService;

    public PaymentProcessorService(ClientRepository clientRepository, PaymentRepository paymentRepository, QueueService queueService) {
        this.clientRepository = clientRepository;
        this.paymentRepository = paymentRepository;
        this.queueService = queueService;
    }

    public PaymentDTO processPayment(PaymentDTO payment) {
        // Validate Client
        Client client = clientRepository.findById(Long.valueOf(payment.getClientId()))
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));

        // Process each payment
        for (PaymentItemDTO item : payment.getPaymentItems()) {
            Payment paymentEntity = paymentRepository.findById(Long.valueOf(item.getPaymentId()))
                    .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

            BigDecimal originalValue = paymentEntity.getOriginalValue();
            BigDecimal paidValue = item.getPaymentValue();

            PaymentStatus status;
            if (paidValue.compareTo(originalValue) < 0) {
                status = PaymentStatus.PARCIAL;
            } else if (paidValue.compareTo(originalValue) == 0) {
                status = PaymentStatus.TOTAL;
            } else {
                status = PaymentStatus.EXCEDENTE;
            }

            item.setPaymentStatus(status.name());

            // Send to appropriate SQS queue based on payment status
            queueService.sendToQueue(item, status);
        }

        return payment;
    }

}
