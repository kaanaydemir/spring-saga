package com.kaanaydemir.orderservice.command.api.controller;

import com.kaanaydemir.orderservice.command.api.command.CreateOrderCommand;
import com.kaanaydemir.orderservice.command.api.model.OrderRestModel;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/order")
public class OrderCommandController {
    private final CommandGateway commandGateway;

    public OrderCommandController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping
    public String createOrder(@RequestBody OrderRestModel orderRestModel) {
        String orderId = UUID.randomUUID().toString();
        CreateOrderCommand createOrderCommand = CreateOrderCommand.builder()
                .orderId(orderId)
                .productId(orderRestModel.getProductId())
                .userId(orderRestModel.getUserId())
                .addressId(orderRestModel.getAddressId())
                .quantity(orderRestModel.getQuantity())
                .orderStatus("CREATED")
                .build();

        return commandGateway.sendAndWait(createOrderCommand);
    }
}
