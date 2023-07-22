package com.kaanaydemir.shipmentservice.command.api.aggregate;

import com.kaanaydemir.commonservice.commands.CancelShipmentCommand;
import com.kaanaydemir.commonservice.commands.ShipOrderCommand;
import com.kaanaydemir.commonservice.events.OrderShippedEvent;
import com.kaanaydemir.commonservice.events.ShipmentCancelledEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import java.util.UUID;

@Aggregate
public class ShipmentAggregate {

    @AggregateIdentifier
    private String shipmentId;
    private String orderId;
    private String paymentId;
    private String shipmentStatus;

    public ShipmentAggregate() {
    }

    @CommandHandler
    public ShipmentAggregate(ShipOrderCommand shipOrderCommand) {
        // Validate the Command
        // If Command is Valid, then send OrderShippedEvent
        OrderShippedEvent orderShippedEvent = OrderShippedEvent.builder()
                .orderId(shipOrderCommand.getOrderId())
                .shipmentId(shipOrderCommand.getShipmentId())
                .paymentId(shipOrderCommand.getPaymentId())
                .shipmentStatus("SHIPPED")
                .build();

        AggregateLifecycle.apply(orderShippedEvent);
    }

    @EventSourcingHandler
    public void on(OrderShippedEvent orderShippedEvent) {
        this.shipmentId =  orderShippedEvent.getShipmentId();
        this.orderId = orderShippedEvent.getOrderId();
        this.shipmentStatus = orderShippedEvent.getShipmentStatus();
    }

    @CommandHandler
    public void handle(CancelShipmentCommand command) {
        ShipmentCancelledEvent shipmentCancelledEvent = new ShipmentCancelledEvent();
        BeanUtils.copyProperties(command, shipmentCancelledEvent);
        AggregateLifecycle.apply(shipmentCancelledEvent);
    }

    @EventSourcingHandler
    public void on(ShipmentCancelledEvent shipmentCancelledEvent) {
        this.shipmentStatus = shipmentCancelledEvent.getShipmentStatus();
    }
}
