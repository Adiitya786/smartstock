package com.smartstock.Service;

import com.smartstock.Repo.InventoryRepo;
import com.smartstock.Repo.ProductRepo;
import com.smartstock.exception.ProductNotFoundException;
import com.smartstock.model.Inventory;
import com.smartstock.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    @Autowired
    private ProductRepo prepo;
    @Autowired
    private InventoryRepo irepo;

    public Inventory createInventory(Long productId,int quantity){

        if (quantity < 0) {
            throw new IllegalArgumentException(
                    "Quantity cannot be negative"
            );
        }
        Product product = prepo.findById(productId)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with id: " + productId));

        Inventory inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setQuantity(quantity);
        inventory.setReservedQuantity(0);
        return irepo.save(inventory);
    }

    public Inventory getInventoryByProductId(Long productId) {
        return irepo.findByProductId(productId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Inventory not found for product: " + productId));
    }
}
