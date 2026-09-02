package com.smartstock.Controller;

import com.smartstock.Repo.InventoryRepo;
import com.smartstock.Service.InventoryService;
import com.smartstock.model.Inventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryService service;

    @PostMapping("/{productId}")
    public Inventory createInventory(@PathVariable Long productId, @RequestParam int quantity){

        return service.createInventory(productId,quantity);
    }
    @GetMapping("/{productId}")
    public Inventory getInventory(
            @PathVariable Long productId) {

        return service
                .getInventoryByProductId(productId);
    }
}
