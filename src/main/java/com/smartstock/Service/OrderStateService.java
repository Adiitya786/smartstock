package com.smartstock.Service;

import com.smartstock.Repo.OrderRepo;
import com.smartstock.exception.OrderNotFoundException;
import com.smartstock.model.Order;
import com.smartstock.model.OrderStatus;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderStateService {
    @Autowired
    private  OrderRepo orepo;

    public Order updateOrderStatus(Long orderId, OrderStatus newStatus){
        Order order = orepo.findById(orderId).orElseThrow(
                () -> new OrderNotFoundException("Order not found with id: "+orderId)
        );

        OrderStatus currStatus = order.getStatus();

        if (!isValidTransition(currStatus, newStatus)) {
            throw new IllegalStateException(
                    "Invalid order status transition from "
                            + currStatus
                            + " to "
                            + newStatus
            );
        }
        order.setStatus(newStatus);
        return orepo.save(order);

    }

    private boolean isValidTransition(OrderStatus currStatus, OrderStatus next){
        return switch (currStatus) {

            case CREATED ->
                    next == OrderStatus.PAYMENT_PENDING
                            || next == OrderStatus.CANCELLED;

            case PAYMENT_PENDING ->
                    next == OrderStatus.PAID
                            || next == OrderStatus.CANCELLED;

            case PAID ->
                    next == OrderStatus.CONFIRMED
                            || next == OrderStatus.CANCELLED;

            case CONFIRMED ->
                    next == OrderStatus.SHIPPED
                            || next == OrderStatus.CANCELLED;

            case SHIPPED ->
                    next == OrderStatus.DELIVERED;

            case DELIVERED, CANCELLED ->
                    false;
        };
    }
}
