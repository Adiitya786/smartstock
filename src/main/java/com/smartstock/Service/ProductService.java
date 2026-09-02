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
    public Product getProductById(Long id){
        return repo.findById(id).orElseThrow(() ->
                new ProductNotFoundException("Product not found with id: " + id)
        );
    }

    public void deleteProduct(Long id){
         repo.deleteById(id);
    }
}
