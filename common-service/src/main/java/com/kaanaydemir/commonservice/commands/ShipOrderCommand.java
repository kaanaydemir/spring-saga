package com.kaanaydemir.commonservice.commands;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class ShipOrderCommand {

    @TargetAggregateIdentifier
    private String shipmentId;
    private String orderId;
    private String paymentId;
}
