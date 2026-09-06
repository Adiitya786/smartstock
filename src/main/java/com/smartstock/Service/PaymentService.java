package com.smartstock.Service;

import com.smartstock.Repo.InventoryRepo;
import com.smartstock.Repo.OrderRepo;
import com.smartstock.Repo.PaymentRepo;
import com.smartstock.dto.PaymentResponse;
import com.smartstock.exception.OrderNotFoundException;
import com.smartstock.model.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Autowired
    private OrderRepo orepo;
    @Autowired
    private PaymentRepo prepo;
    @Autowired
    private InventoryRepo irepo;
    @Autowired
    private InventoryService inventoryService;

    @Transactional
    public PaymentResponse makePayment(Long orderId,boolean success,String idempotencyKey){

        Payment existingPayment =
                prepo.findByIdempotencyKey(idempotencyKey)
                        .orElse(null);

        if (existingPayment != null) {
            return mapToResponse(existingPayment);
        }

        Order order = orepo.findById(orderId)
                .orElseThrow(() ->
                        new OrderNotFoundException(
                                "Order not found with id: " + orderId
                        ));
        if(order.getStatus()!= OrderStatus.PAYMENT_PENDING){
            throw new RuntimeException(
                    "Order is not ready for payment"
            );


        }
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());
        payment.setCreatedAt(order.getCreatedAt());
        payment.setIdempotencyKey(idempotencyKey);
        if(success){
            payment.setStatus(PaymentStatus.SUCCESS);
            order.setStatus(OrderStatus.PAID);
        }
        else{
            payment.setStatus(PaymentStatus.FAILED);

            for(OrderItem item:order.getItems()){
                inventoryService.releaseStock(item.getProduct().getId(),item.getQuantity());
            }
            order.setStatus(OrderStatus.CANCELLED);
        }

        prepo.save(payment);
        orepo.save(order);
         return mapToResponse(payment);
    }

    public PaymentResponse mapToResponse(Payment payment){
        PaymentResponse response = new PaymentResponse();
        response.setAmount(payment.getAmount());
        response.setId(payment.getId());
        response.setCreatedAt(payment.getCreatedAt());
        response.setOrderId(payment.getOrder().getId());
        response.setAmount(payment.getAmount());
        response.setStatus(payment.getStatus());

        return response;
    }
}
