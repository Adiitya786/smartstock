package com.smartstock.Controller;

import com.smartstock.Service.PaymentService;
import com.smartstock.dto.PaymentResponse;
import com.smartstock.model.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService service;

    @PostMapping("/{orderId}")
    public PaymentResponse makePayment(@PathVariable Long orderId,@RequestParam boolean success){
        return service.makePayment(orderId,success);
    }
}
