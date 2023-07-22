package com.kaanaydemir.shipmentservice.command.api.data;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "shipment")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Shipment {

    @Id
    private String shipmentId;
    private String orderId;
    private String shipmentStatus;
}
