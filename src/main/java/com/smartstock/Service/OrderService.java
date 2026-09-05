package com.smartstock.Service;

import com.smartstock.Repo.OrderRepo;
import com.smartstock.Repo.ProductRepo;
import com.smartstock.Repo.UserRepo;
import com.smartstock.dto.OrderItemRequest;
import com.smartstock.dto.OrderItemResponse;
import com.smartstock.dto.OrderRequest;
import com.smartstock.dto.OrderResponse;
import com.smartstock.exception.OrderNotFoundException;
import com.smartstock.exception.ProductNotFoundException;
import com.smartstock.exception.UserNotFoundException;
import com.smartstock.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepo orepo;
    @Autowired
    private ProductRepo prepo;
    @Autowired
    private UserRepo urepo;

    public OrderResponse createOrder(OrderRequest request){
        User user  =  urepo.findById(request.getUserId())
                .orElseThrow(()-> new UserNotFoundException("User Not Found with Id" +request.getUserId()))
                ;

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(LocalDateTime.now());

        List<OrderItem> items = new ArrayList<>();

        BigDecimal total = BigDecimal.ZERO;

        for(OrderItemRequest itemRequest : request.getItems()){

            Product product = prepo.findById(
                    itemRequest.getProductId()
            ).orElseThrow(() ->
                    new ProductNotFoundException(
                            "Product not found with id: "
                                    + itemRequest.getProductId()
                    ));

            BigDecimal price = product.getPrice();

            BigDecimal subtotal = price.multiply(
                    BigDecimal.valueOf(itemRequest.getQuantity())
            );

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setPrice(price);
            item.setProduct(product);
            item.setQuantity(itemRequest.getQuantity());
            items.add(item);
            total = total.add(subtotal);
        }



        order.setItems(items);
        order.setTotalAmount(total);

        Order savedOrder = orepo.save(order);

        return mapToResponse(savedOrder);

    }

    public OrderResponse getOrderById(Long orderId){
        Order order= orepo.findById(orderId).orElseThrow(
                ()->new OrderNotFoundException("Order not found with id: " + orderId)
        );
         return mapToResponse(order);
    }

    public List<OrderResponse> getOrdersByUserId(Long userId) {

        urepo.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with id: " + userId
                        ));

        List<Order> orders = orepo.findByUserId(userId);

        return orders.stream()
                .map(this::mapToResponse)
                .toList();
    }

    public OrderResponse mapToResponse(Order order){
        OrderResponse response = new OrderResponse();
        response.setCreatedAt(order.getCreatedAt());
        response.setStatus(order.getStatus());
        response.setTotalAmount(order.getTotalAmount());
        response.setUserId(order.getUser().getId());
        response.setId(order.getId());

        List<OrderItemResponse> itemResponses = new ArrayList<>();
        for(OrderItem item: order.getItems()){
            OrderItemResponse itemResponse = new OrderItemResponse();
            itemResponse.setPrice(item.getPrice());
            itemResponse.setProductId(item.getProduct().getId());
            itemResponse.setProductName(item.getProduct().getName());
            itemResponse.setQuantity(item.getQuantity());
            itemResponse.setSubtotal(
                    item.getPrice().multiply(
                            BigDecimal.valueOf(item.getQuantity()))
                    );

             itemResponses.add(itemResponse);

        }
        response.setItems(itemResponses);

        return  response;
    }
}
