package io.reflectoring.exception.service;

import io.reflectoring.exception.commons.I18Constants;
import io.reflectoring.exception.entity.Category;
import io.reflectoring.exception.entity.Product;
import io.reflectoring.exception.exception.NoSuchElementFoundException;
import io.reflectoring.exception.model.ProductInput;
import io.reflectoring.exception.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository repository;
    private final MessageSource messageSource;

    public Product getProduct(String id) {
        return repository.findById(id).orElseThrow(()->
                new NoSuchElementFoundException(getLocalMessage(I18Constants.NO_ITEM_FOUND.getKey(), id)));
    }

    public Product addProduct(ProductInput productInput){
        Product product = new Product();
        product.setName(productInput.getName());
        product.setPrice(productInput.getPrice());
        product.setWeight(product.getWeight());
        product.setCategory(Objects.isNull(productInput.getCategory())? Category.BOOKS: productInput.getCategory());
        return repository.save(product);
    }

    private String getLocalMessage(String key, String... params){
        return messageSource.getMessage(key,
                params,
                Locale.ENGLISH);
    }
}
