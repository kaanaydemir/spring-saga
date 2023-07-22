package com.kaanaydemir.paymentservice.command.api.events;

import com.kaanaydemir.commonservice.events.PaymentCancelledEvent;
import com.kaanaydemir.commonservice.events.PaymentProcessedEvent;
import com.kaanaydemir.paymentservice.command.api.data.Payment;
import com.kaanaydemir.paymentservice.command.api.data.PaymentRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class PaymentEventHandler {

    private PaymentRepository paymentRepository;

    public PaymentEventHandler(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @EventHandler
    public void on(PaymentProcessedEvent event) {
        Payment payment
                = Payment.builder()
                .paymentId(event.getPaymentId())
                .orderId(event.getOrderId())
                .paymentStatus("COMPLETED")
                .paymentDate(LocalDate.now())
                .build();

        paymentRepository.save(payment);
    }

    @EventHandler
    public void on(PaymentCancelledEvent event) {
        Payment payment = paymentRepository.findById(event.getPaymentId())
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        payment.setPaymentStatus(event.getPaymentStatus());
        paymentRepository.save(payment);
    }
}
