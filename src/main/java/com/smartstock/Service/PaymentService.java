package com.smartstock.Service;

import com.smartstock.Repo.InventoryRepo;
import com.smartstock.Repo.OrderRepo;
import com.smartstock.Repo.PaymentRepo;
import com.smartstock.dto.PaymentResponse;
import com.smartstock.exception.OrderNotFoundException;
import com.smartstock.model.Order;
import com.smartstock.model.OrderStatus;
import com.smartstock.model.Payment;
import com.smartstock.model.PaymentStatus;
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

    @Transactional
    public PaymentResponse makePayment(Long orderId){
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
        payment.setStatus(PaymentStatus.PENDING.SUCCESS);
        order.setStatus(OrderStatus.PAID);

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

        return response;
    }
}
