package com.smartstock.Service;

import com.smartstock.Repo.ProductRepo;
import com.smartstock.dto.ProductRequest;
import com.smartstock.dto.ProductResponse;
import com.smartstock.exception.ProductNotFoundException;
import com.smartstock.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private  ProductRepo repo;


    @Autowired
    private ProductCacheService cacheService;

    public ProductResponse createPRoduct(ProductRequest request){
        Product product = new Product();
        product.setName(request.getName());
        product.setSku(request.getSku());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());

        Product savePRoduct = repo.save(product);

        return new ProductResponse(savePRoduct.getId(), savePRoduct.getName(),savePRoduct.getSku()
        ,savePRoduct.getPrice(), savePRoduct.getDescription());
    }

    public List<Product> getAllProducts(){
        return repo.findAll();
    }

    public Product getProductById(Long id) {


        // 1. Check Redis
        Product cachedProduct = cacheService.get(id);

        if (cachedProduct != null) {
            return cachedProduct;
        }

        // 2. Cache miss → PostgreSQL
        Product product = repo.findById(id).orElseThrow(() ->
                new ProductNotFoundException(
                        "Product not found with id: " + id
                )
        );

        // 3. Save in Redis
        cacheService.save(product);

        return product;
    }

    public ProductResponse updateProduct(Long id, ProductRequest request) {

        Product product = repo.findById(id).orElseThrow(() ->
                new ProductNotFoundException(
                        "Product not found with id: " + id
                )
        );

        product.setName(request.getName());
        product.setSku(request.getSku());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());

        Product updatedProduct = repo.save(product);

        // Invalidate old cached version
        cacheService.delete(id);

        return new ProductResponse(
                updatedProduct.getId(),
                updatedProduct.getName(),
                updatedProduct.getSku(),
                updatedProduct.getPrice(),
                updatedProduct.getDescription()
        );
    }

    public void deleteProduct(Long id){
         repo.deleteById(id);
         cacheService.delete(id);
    }
}
