package com.kaanaydemir.commonservice.commands;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
public class CancelShipmentCommand {

    @TargetAggregateIdentifier
    private String shipmentId;
    private String orderId;
    private String paymentId;
    private String shipmentStatus = "CANCELLED";
}
