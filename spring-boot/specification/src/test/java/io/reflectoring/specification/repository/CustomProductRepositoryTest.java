package io.reflectoring.specification.repository;

import io.reflectoring.specification.model.Category;
import io.reflectoring.specification.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomProductRepositoryTest {

    @Autowired
    private CustomProductRepository productRepository;

    @Test
    void getLowRangeProducts() {
        List<Product> products = productRepository.getLowRangeProducts(List.of(Category.MOBILE, Category.TV_APPLIANCES));
        assertEquals(2, products.size());
    }

    @Test
    void getPremiumProducts() {
        List<Product> products = productRepository.getPremiumProducts(List.of(Category.MEN_FASHION, Category.WOMEN_FASHION));
        assertEquals(2, products.size());
    }

    @Test
    void testGetPremiumProducts() {
        List<Product> products = productRepository.getPremiumProducts("jacket", List.of(Category.MEN_FASHION));
        assertEquals(2, products.size());
    }

    @Test
    void testDynamicSpecification() {
        Filter nameLike = Filter.builder()
                .field("name")
                .operator(QueryOperator.LIKE)
                .value("jacket")
                .build();
        Filter categories = Filter.builder()
                .field("category")
                .operator(QueryOperator.IN)
                .values(List.of(Category.MEN_FASHION.name(), Category.WOMEN_FASHION.name()))
                .build();
        List<Filter> filters = new ArrayList<>();
        filters.add(nameLike);
        filters.add(categories);
        List<Product> products = productRepository.getQueryResult(filters);
        assertEquals(2, products.size());

        Filter lowRange = Filter.builder()
                .field("price")
                .operator(QueryOperator.LESS_THAN)
                .value("1000")
                .build();
        categories.setValues(List.of(Category.MOBILE.name(), Category.TV_APPLIANCES.name()));
        filters = new ArrayList<>();
        filters.add(lowRange);
        filters.add(categories);

        products = productRepository.getQueryResult(filters);
        assertEquals(2, products.size());

        Filter priceEquals = Filter.builder()
                .field("price")
                .operator(QueryOperator.EQUALS)
                .value("1100")
                .build();
        filters = new ArrayList<>();
        filters.add(priceEquals);
        products = productRepository.getQueryResult(filters);
        assertEquals(1, products.size());
    }
}