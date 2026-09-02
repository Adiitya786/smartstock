package com.smartstock.Service;

import com.smartstock.Repo.ProductRepo;
import com.smartstock.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private  ProductRepo repo;

    public Product createPRoduct(Product product){
        return repo.save(product);
    }

    public List<Product> getAllProducts(){
        return repo.findAll();
    }
    public Product getProductById(Long id){
        return repo.findById(id).orElseThrow(()->new RuntimeException("Product Not Found"));
    }

    public void deleteProduct(Long id){
         repo.deleteById(id);
    }
}
