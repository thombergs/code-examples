package io.reflectoring.springcloudredis.service;

import io.reflectoring.springcloudredis.entity.Category;
import io.reflectoring.springcloudredis.entity.Product;
import io.reflectoring.springcloudredis.model.ProductInput;
import io.reflectoring.springcloudredis.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
@CacheConfig(cacheNames = "product-cache")
public class ProductService {
    private final ProductRepository repository;

    @Cacheable
    public Product getProduct(String id) {
        return repository.findById(id).orElseThrow(()->
                new RuntimeException("No such product found with id"));
    }

    public Product addProduct(ProductInput productInput){
        var product = new Product();
        product.setName(productInput.getName());
        product.setPrice(productInput.getPrice());
        product.setWeight(product.getWeight());
        product.setCategory(Objects.isNull(productInput.getCategory())? Category.BOOKS: productInput.getCategory());
        return repository.save(product);
    }

    @CacheEvict
    public void deleteProduct(String id) {
        repository.deleteById(id);
    }

    @CachePut(key = "#id")
    public Product updateProduct(String id, ProductInput productInput) {
        var product = new Product();
        product.setId(id);
        product.setName(productInput.getName());
        product.setPrice(productInput.getPrice());
        product.setWeight(product.getWeight());
        product.setCategory(Objects.isNull(productInput.getCategory())? Category.BOOKS: productInput.getCategory());
        return repository.save(product);
    }
}
