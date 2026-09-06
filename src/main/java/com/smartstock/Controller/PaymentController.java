package com.smartstock.Controller;

import com.smartstock.Service.PaymentService;
import com.smartstock.dto.PaymentResponse;
import com.smartstock.model.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService service;

    @PostMapping("/{orderId}")
    public PaymentResponse makePayment(@PathVariable Long orderId){
        return service.makePayment(orderId);
    }
}
