package com.smartstock.Service;

import com.smartstock.Repo.InventoryRepo;
import com.smartstock.Repo.ProductRepo;
import com.smartstock.dto.InventoryResponse;
import com.smartstock.exception.InsufficientStockException;
import com.smartstock.exception.InventoryNotFoundException;
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

    public InventoryResponse createInventory(Long productId,int quantity){

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
        Inventory savedInventory = irepo.save(inventory);

        return mapToResponse(savedInventory);
    }

    public InventoryResponse getInventoryByProductId(Long productId) {

        Inventory inventory= irepo.findByProductId(productId)
                .orElseThrow(() ->
                        new InventoryNotFoundException(
                                "Inventory not found for product: " + productId));

        return mapToResponse(inventory);
    }

    public InventoryResponse AddQuantity(Long ProductId, int quantity){

        if(quantity<=0) {
            throw
                    new InsufficientStockException(
                            "Quantity must be greater than zero"
                    );
        }
        Inventory inventory = irepo.findByProductId(ProductId)
                .orElseThrow(() ->
                        new InventoryNotFoundException(
                                "Inventory not found for product: " + ProductId
                        ));

              inventory.setQuantity(inventory.getQuantity()+quantity);

        Inventory savedInventory = irepo.save(inventory);

        return mapToResponse(savedInventory);
    }

    public InventoryResponse removeQuantity(Long ProductId, int quantity){

        if(quantity<=0) {
            throw
                    new InsufficientStockException(
                            "Quantity  must be greater than zero"
                    );
        }
        Inventory inventory = irepo.findByProductId(ProductId)
                .orElseThrow(() ->
                        new InventoryNotFoundException(
                                "Inventory not found for product: " + ProductId
                        ));

        int availstock = inventory.getQuantity()-inventory.getReservedQuantity();
        if(quantity>availstock){
            throw
                    new RuntimeException(
                            "Insufficient  available stock"
                    );
        }
        inventory.setQuantity(inventory.getQuantity()-quantity);

        Inventory savedInventory = irepo.save(inventory);

        return mapToResponse(savedInventory);
    }

    public InventoryResponse mapToResponse(Inventory inventory){
        InventoryResponse response = new InventoryResponse();
        response.setId(inventory.getId());
        response.setProductId(inventory.getProduct().getId());
        response.setProductName(inventory.getProduct().getName());
        response.setQuantity(inventory.getQuantity());
        response.setReservedQuantity(inventory.getReservedQuantity());
        response.setAvailableQuantity(inventory.getQuantity()- inventory.getReservedQuantity());

        return  response;
    }

    public InventoryResponse reserveStock(Long ProductId,int quantity){

        if(quantity<=0){
            throw new RuntimeException("Reservation quantity must be greater than 0");
        }

        Inventory  inventory = irepo.findByProductId(ProductId).orElseThrow(
                ()-> new InventoryNotFoundException("No inventory find with product id: "+ProductId)
        );

        int availStock = inventory.getQuantity()-inventory.getReservedQuantity();
        if(quantity>availStock){
            throw new InsufficientStockException("Insufficient available stock");
        }
        inventory.setReservedQuantity(inventory.getReservedQuantity()+quantity);
        Inventory saved = irepo.save(inventory);
        return mapToResponse(inventory);
    }


    public InventoryResponse releaseStock(Long productId, int quantity) {

        if (quantity <= 0) {
            throw new RuntimeException(
                    "Release quantity must be greater than zero"
            );
        }

        Inventory inventory = irepo.findByProductId(productId)
                .orElseThrow(() ->
                        new InventoryNotFoundException(
                                "Inventory not found for product: " + productId
                        ));

        if (quantity > inventory.getReservedQuantity()) {
            throw new InsufficientStockException(
                    "Cannot release more than reserved stock"
            );
        }

        inventory.setReservedQuantity(
                inventory.getReservedQuantity() - quantity
        );

        Inventory savedInventory = irepo.save(inventory);

        return mapToResponse(savedInventory);
    }

}
