package com.kaanaydemir.commonservice.events;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderCancelledEvent {
    private String orderId;
    private String orderStatus = "CANCELLED";
}
