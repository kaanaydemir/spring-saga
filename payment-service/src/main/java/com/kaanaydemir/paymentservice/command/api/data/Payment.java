package com.kaanaydemir.paymentservice.command.api.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "payments")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    private String paymentId;
    private String orderId;
    private LocalDate paymentDate;
    private String paymentStatus;
}
