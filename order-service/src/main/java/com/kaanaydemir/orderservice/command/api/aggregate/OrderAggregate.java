package com.kaanaydemir.orderservice.command.api.aggregate;

import com.kaanaydemir.commonservice.commands.CancelOrderCommand;
import com.kaanaydemir.commonservice.commands.CompleteOrderCommand;
import com.kaanaydemir.commonservice.events.OrderCancelledEvent;
import com.kaanaydemir.commonservice.events.OrderCompletedEvent;
import com.kaanaydemir.commonservice.events.OrderShippedEvent;
import com.kaanaydemir.orderservice.command.api.command.CreateOrderCommand;
import com.kaanaydemir.orderservice.command.api.events.OrderCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate
public class OrderAggregate {

    @AggregateIdentifier
    private String orderId;
    private String productId;
    private String userId;
    private String addressId;
    private Integer quantity;
    private String orderStatus;


    public OrderAggregate() {
    }

    @CommandHandler
    public OrderAggregate(CreateOrderCommand createOrderCommand) {
        // Validate The Command
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent();
        BeanUtils.copyProperties(createOrderCommand, orderCreatedEvent);
        AggregateLifecycle.apply(orderCreatedEvent);
    }

    @EventSourcingHandler
    public void on(OrderCreatedEvent event) {
        this.orderId = event.getOrderId();
        this.productId = event.getProductId();
        this.userId = event.getUserId();
        this.addressId = event.getAddressId();
        this.quantity = event.getQuantity();
        this.orderStatus = event.getOrderStatus();
    }

    @CommandHandler
    public void handle(CompleteOrderCommand completeOrderCommand) {
        // Validate The Command
        // Publish Event
        OrderCompletedEvent orderCompletedEvent = OrderCompletedEvent.builder()
                .orderId(completeOrderCommand.getOrderId())
                .orderStatus(completeOrderCommand.getOrderStatus())
                .build();

        AggregateLifecycle.apply(orderCompletedEvent);
    }

    @EventSourcingHandler
    public void on(OrderCompletedEvent event) {
        this.orderStatus = event.getOrderStatus();
    }

    @CommandHandler
    public void handle(CancelOrderCommand cancelOrderCommand) {
        OrderCancelledEvent orderCancelledEvent = OrderCancelledEvent.builder()
                .orderId(cancelOrderCommand.getOrderId())
                .orderStatus(cancelOrderCommand.getOrderStatus())
                .build();

        AggregateLifecycle.apply(orderCancelledEvent);
    }

    @EventSourcingHandler
    public void on(OrderCancelledEvent event) {
        this.orderStatus = event.getOrderStatus();
    }
}
