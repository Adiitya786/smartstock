package com.smartstock.Service;

import com.smartstock.model.Product;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.concurrent.TimeUnit;

@Service
public class ProductCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public ProductCacheService(
            RedisTemplate<String, Object> redisTemplate,
            ObjectMapper objectMapper) {

        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    private String getKey(Long productId) {
        return "product:" + productId;
    }

    public void save(Product product) {
        redisTemplate.opsForValue()
                .set(getKey(product.getId()), product,10, TimeUnit.MINUTES);
    }

    public Product get(Long productId) {

        Object value = redisTemplate.opsForValue()
                .get(getKey(productId));

        if (value == null) {
            return null;
        }

        return objectMapper.convertValue(value, Product.class);
    }

    public void delete(Long productId) {
        redisTemplate.delete(getKey(productId));
    }
}