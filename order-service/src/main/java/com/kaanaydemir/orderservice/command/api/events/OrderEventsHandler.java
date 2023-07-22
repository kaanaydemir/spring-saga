package com.kaanaydemir.orderservice.command.api.events;

import com.kaanaydemir.commonservice.events.OrderCancelledEvent;
import com.kaanaydemir.commonservice.events.OrderCompletedEvent;
import com.kaanaydemir.orderservice.command.api.data.Order;
import com.kaanaydemir.orderservice.command.api.data.OrderRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
public class OrderEventsHandler {

    private final OrderRepository orderRepository;

    public OrderEventsHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventHandler
    public void on(OrderCreatedEvent event) {
        Order order = Order.builder()
                .orderId(event.getOrderId())
                .productId(event.getProductId())
                .quantity(event.getQuantity())
                .addressId(event.getAddressId())
                .orderStatus("CREATED")
                .userId(event.getUserId())
                .build();
        orderRepository.save(order);
    }

    @EventHandler
    public void on(OrderCompletedEvent event) {
        Order order = orderRepository.findById(event.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        order.setOrderStatus(event.getOrderStatus());
        orderRepository.save(order);
    }

    @EventHandler
    public void on(OrderCancelledEvent event) {
        Order order = orderRepository.findById(event.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        order.setOrderStatus(event.getOrderStatus());
        orderRepository.save(order);
    }
}
