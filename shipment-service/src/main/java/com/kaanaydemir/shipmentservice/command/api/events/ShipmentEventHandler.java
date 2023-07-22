package com.kaanaydemir.shipmentservice.command.api.events;

import com.kaanaydemir.commonservice.events.OrderShippedEvent;
import com.kaanaydemir.commonservice.events.ShipmentCancelledEvent;
import com.kaanaydemir.shipmentservice.command.api.data.Shipment;
import com.kaanaydemir.shipmentservice.command.api.data.ShipmentRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ShipmentEventHandler {

    private final ShipmentRepository shipmentRepository;

    public ShipmentEventHandler(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    @EventHandler
    public void on(OrderShippedEvent event) {
        Shipment shipment = Shipment.builder()
                .shipmentId(event.getShipmentId())
                .orderId(event.getOrderId())
                .shipmentId(event.getShipmentId())
                .shipmentStatus("SHIPPED")
                .build();

        shipmentRepository.save(shipment);
    }

    @EventHandler
    public void on(ShipmentCancelledEvent event) {
        Shipment shipment = shipmentRepository.findById(event.getShipmentId())
                .orElseThrow(() -> new IllegalArgumentException("Shipment not found"));
        shipment.setShipmentStatus(event.getShipmentStatus());
        shipmentRepository.save(shipment);
    }
}
