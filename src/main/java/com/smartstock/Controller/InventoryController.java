package com.smartstock.Controller;

import com.smartstock.Repo.InventoryRepo;
import com.smartstock.Service.InventoryService;
import com.smartstock.dto.InventoryRequest;
import com.smartstock.dto.InventoryResponse;
import com.smartstock.model.Inventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryService service;

    @PostMapping("/{productId}")
    public InventoryResponse createInventory(@PathVariable Long productId, @RequestParam int quantity){

        return service.createInventory(productId,quantity);
    }
    @GetMapping("/{productId}")
    public InventoryResponse getInventory(
            @PathVariable Long productId) {

        return service
                .getInventoryByProductId(productId);
    }

    @PostMapping("/{id}/add")
    public InventoryResponse addQuantity(@PathVariable Long id, @RequestParam int quantity){

        return service.AddQuantity(id,quantity);
    }


    @PostMapping("/{id}/remove")
    public InventoryResponse removeQuantity(@PathVariable Long id, @RequestParam int quantity){

        return service.removeQuantity(id,quantity);
    }

    @PostMapping("/{productId}/reserve")
    public InventoryResponse reserveStock(
            @PathVariable Long productId,
            @RequestParam int quantity) {

        return service.reserveStock(productId, quantity);
    }

    @PostMapping("/{productId}/release")
    public InventoryResponse releaseStock(
            @PathVariable Long productId,
            @RequestParam int quantity) {

        return service.releaseStock(productId, quantity);
    }
}
