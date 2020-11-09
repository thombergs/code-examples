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
    private CustomProductRepository productAdapter;

    @Test
    void getLowRangeProducts() {
        List<Product> products = productAdapter.getLowRangeProducts(List.of(Category.MOBILE, Category.TV_APPLIANCES));
        assertEquals(2, products.size());
    }

    @Test
    void getPremiumProducts() {
        List<Product> products = productAdapter.getPremiumProducts(List.of(Category.MEN_FASHION, Category.WOMEN_FASHION));
        assertEquals(2, products.size());
    }

    @Test
    void testGetPremiumProducts() {
        List<Product> products = productAdapter.getPremiumProducts("jacket", List.of(Category.MEN_FASHION));
        assertEquals(2, products.size());
    }

    @Test
    void testDynamicSpecification() {
        QueryInput nameLike = QueryInput.builder()
                .field("name")
                .operator(QueryOperator.LIKE)
                .value("jacket")
                .isOptional(false)
                .build();
        QueryInput categories = QueryInput.builder()
                .field("category")
                .operator(QueryOperator.IN)
                .values(List.of(Category.MEN_FASHION.name(), Category.WOMEN_FASHION.name()))
                .isOptional(false)
                .build();
        List<QueryInput> queries = new ArrayList<>();
        queries.add(nameLike);
        queries.add(categories);
        List<Product> products = productAdapter.getQueryResult(queries);
        assertEquals(2, products.size());

        QueryInput lowRange = QueryInput.builder()
                .field("price")
                .operator(QueryOperator.LT)
                .value("1000")
                .isOptional(false)
                .build();
        categories.setValues(List.of(Category.MOBILE.name(), Category.TV_APPLIANCES.name()));
        queries = new ArrayList<>();
        queries.add(lowRange);
        queries.add(categories);

        products = productAdapter.getQueryResult(queries);
        assertEquals(2, products.size());

        QueryInput priceEquals = QueryInput.builder()
                .field("price")
                .operator(QueryOperator.EQ)
                .value("1100")
                .build();
        queries = new ArrayList<>();
        queries.add(priceEquals);
        products = productAdapter.getQueryResult(queries);
        assertEquals(1, products.size());
    }
}