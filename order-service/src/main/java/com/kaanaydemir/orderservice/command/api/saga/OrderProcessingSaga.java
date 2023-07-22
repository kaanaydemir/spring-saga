package com.kaanaydemir.orderservice.command.api.saga;

import com.kaanaydemir.commonservice.commands.*;
import com.kaanaydemir.commonservice.events.*;
import com.kaanaydemir.commonservice.model.User;
import com.kaanaydemir.commonservice.query.GetUserPaymentDetailsQuery;
import com.kaanaydemir.orderservice.command.api.events.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Saga
@Slf4j
public class OrderProcessingSaga {

    @Autowired
    private transient CommandGateway commandGateway;

    @Autowired
    private transient QueryGateway queryGateway;

    public OrderProcessingSaga() {
    }


    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    private void handle(OrderCreatedEvent event) {
        log.info("OrderCreatedEvent in Saga for Order Id : {}", event.getOrderId());

        GetUserPaymentDetailsQuery getUserPaymentDetailsQuery = new GetUserPaymentDetailsQuery("1");
        User user = queryGateway.query(getUserPaymentDetailsQuery, ResponseTypes.instanceOf(User.class)).join();

        try {
            user = queryGateway.query(getUserPaymentDetailsQuery, ResponseTypes.instanceOf(User.class)).join();
        } catch (Exception e) {
            log.error("Error Fetching User Payment Details, Cause: {}", e.getMessage());
            cancelOrderCommand(event.getOrderId());
        }


        ValidatePaymentCommand validatePaymentCommand = ValidatePaymentCommand.builder()
                .orderId(event.getOrderId())
                .paymentId(UUID.randomUUID().toString())
                .cardDetail(user.getCardDetail())
                .build();

        commandGateway.send(validatePaymentCommand);
    }

    @SagaEventHandler(associationProperty = "orderId")
    private void handle(PaymentProcessedEvent event) {
        log.info("PaymentProcessedEvent in Saga for Order Id: {}", event.getOrderId());
        ShipOrderCommand shipOrderCommand;
        try {

            shipOrderCommand = ShipOrderCommand.builder()
                    .orderId(event.getOrderId())
                    .shipmentId(UUID.randomUUID().toString())
                    .paymentId(event.getPaymentId())
                    .build();

            commandGateway.send(shipOrderCommand);
        } catch (Exception e) {
            log.error("Error Creating ShipOrderCommand, Cause: {}", e.getMessage());
            cancelPaymentCommand(event);
        }
    }

    @SagaEventHandler(associationProperty = "orderId")
    private void handle(OrderShippedEvent event) {
        try {

            if(true)
                throw new Exception();

            log.info("OrderShippedEvent in Saga for Order Id: {}", event.getOrderId());
            CompleteOrderCommand completeOrderCommand = CompleteOrderCommand.builder()
                    .orderId(event.getOrderId())
                    .orderStatus("APPROVED")
                    .build();

            commandGateway.send(completeOrderCommand);
        } catch (Exception e) {
            log.error("Error Creating CompleteOrderCommand, Cause: {}", e.getMessage());
            cancelShipmentCommand(event);
        }
    }

    @SagaEventHandler(associationProperty = "orderId")
    @EndSaga
    public void handle(OrderCompletedEvent event) {
        log.info("OrderCompletedEvent in Saga for Order Id: {}", event.getOrderId());
    }

    @SagaEventHandler(associationProperty = "orderId")
    @EndSaga
    public void handle(OrderCancelledEvent event) {
        log.info("OrderCancelledEvent in Saga for Order Id: {}", event.getOrderId());
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(PaymentCancelledEvent event) {
        log.info("PaymentCancelledEvent in Saga for Order Id: {}", event.getOrderId());
        cancelOrderCommand(event.getOrderId());
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ShipmentCancelledEvent event) {
        log.info("ShipmentCancelledEvent in Saga for Order Id: {}", event.getOrderId());
        PaymentProcessedEvent paymentProcessedEvent = new PaymentProcessedEvent(event.getPaymentId(), event.getOrderId());
        cancelPaymentCommand(paymentProcessedEvent);
    }
    private void cancelOrderCommand(String orderId) {
        CancelOrderCommand cancelOrderCommand = new CancelOrderCommand(orderId);
        commandGateway.send(cancelOrderCommand);
    }

    private void cancelPaymentCommand(PaymentProcessedEvent event) {
        CancelPaymentCommand cancelPaymentCommand = new CancelPaymentCommand(event.getPaymentId(), event.getOrderId());
        commandGateway.send(cancelPaymentCommand);
    }

    private void cancelShipmentCommand(OrderShippedEvent event) {
        CancelShipmentCommand cancelShipmentCommand = new CancelShipmentCommand(event.getShipmentId(), event.getOrderId(), event.getPaymentId());
        commandGateway.send(cancelShipmentCommand);
    }
}
