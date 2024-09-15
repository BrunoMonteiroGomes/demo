package com.example.paymentprocessor.service;

import com.example.paymentprocessor.repository.ClientRepository;
import com.example.paymentprocessor.repository.PaymentRepository;
import com.example.paymentprocessor.repository.entity.Client;
import com.example.paymentprocessor.repository.entity.Payment;
import com.example.paymentprocessor.controller.dto.PaymentItemDTO;
import com.example.paymentprocessor.controller.dto.PaymentDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class PaymentProcessorServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private QueueService queueService;

    @InjectMocks
    private PaymentProcessorService paymentProcessorService;

    @Test
    public void testProcessPayment_Success() {
        // Arrange
        Client client = new Client();
        client.setId(1L);

        Payment payment = new Payment();
        payment.setId(1L);
        payment.setOriginalValue(new BigDecimal("100.00"));

        Mockito.when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        Mockito.when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setClientId("1");
        PaymentItemDTO item = new PaymentItemDTO();
        item.setPaymentId("1");
        item.setPaymentValue(new BigDecimal("100.00"));
        paymentDTO.setPaymentItems(Collections.singletonList(item));

        // Act
        PaymentDTO result = paymentProcessorService.processPayment(paymentDTO);

        // Assert
        Assertions.assertEquals(PaymentStatus.TOTAL.toString(), result.getPaymentItems().get(0).getPaymentStatus());
        Mockito.verify(queueService).sendToQueue(Mockito.any(), Mockito.eq(PaymentStatus.TOTAL));
    }

    @Test
    public void testProcessPayment_ClientNotFound() {
        // Arrange
        Mockito.when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setClientId("1");

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            paymentProcessorService.processPayment(paymentDTO);
        });

        Assertions.assertEquals("Client not found", exception.getMessage());
    }
}
