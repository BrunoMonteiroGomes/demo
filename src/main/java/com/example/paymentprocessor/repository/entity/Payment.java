package com.example.paymentprocessor.repository.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Entity
@Data
@Getter
@Setter
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal originalValue;

    @ManyToOne(fetch = FetchType.LAZY)
    private Client client;
}
