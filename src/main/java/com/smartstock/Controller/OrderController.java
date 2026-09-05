package com.smartstock.Controller;

import com.smartstock.Service.OrderService;
import com.smartstock.Service.OrderStateService;
import com.smartstock.dto.OrderRequest;
import com.smartstock.dto.OrderResponse;
import com.smartstock.model.Order;
import com.smartstock.model.OrderStatus;
import jakarta.persistence.GeneratedValue;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService service;

    @Autowired
    private OrderStateService stateService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse creteOrder(@RequestBody @Valid OrderRequest request){
        return service.createOrder(request);
    }

    @GetMapping("/{orderId}")
    public OrderResponse getOrderById(@PathVariable Long orderId){
        return service.getOrderById(orderId);
    }

    @GetMapping("/user/{userId}")
    public List<OrderResponse> getOrderByUserId(@PathVariable Long userId){
        return service.getOrdersByUserId(userId);
    }

    @PatchMapping("/{orderId}/status")
    public OrderResponse updateOrderStatus(@PathVariable Long orderId,@RequestParam OrderStatus status){
       Order order = stateService.updateOrderStatus(orderId,status);
       return service.mapToResponse(order);
    }

    @PatchMapping("/{orderId}/cancel")
    public OrderResponse cancelOrder(
            @PathVariable Long orderId) {

        return service.cancelOrder(orderId);
    }
}
