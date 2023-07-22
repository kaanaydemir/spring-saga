package com.kaanaydemir.commonservice.events;

import lombok.Data;

@Data
public class ShipmentCancelledEvent {
    private String shipmentId;
    private String orderId;
    private String paymentId;
    private String shipmentStatus = "CANCELLED";
}
