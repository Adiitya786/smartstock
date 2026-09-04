package com.smartstock.Controller;

import com.smartstock.Service.OrderService;
import com.smartstock.dto.OrderRequest;
import com.smartstock.dto.OrderResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse creteOrder(@RequestBody @Valid OrderRequest request){
        return service.createOrder(request);
    }
}
