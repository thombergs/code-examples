package criteria.model;

import criteria.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@Order(1)
public class DataLineRunner implements CommandLineRunner {
    private final ProductRepository productRepository;

    public DataLineRunner(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        insertWithQuery();
    }

    @Transactional
    public void insertWithQuery() {

        List<Product> products = new ArrayList<>();

        Product sampleProduct1 = new Product();
        sampleProduct1.setId(1L);
        sampleProduct1.setName("Broom");
        sampleProduct1.setPrice(10.0);
        sampleProduct1.setCategory(Category.TOOLS);

        products.add(sampleProduct1);

        Product sampleProduct2 = new Product();
        sampleProduct2.setId(2L);
        sampleProduct2.setName("Kabab");
        sampleProduct2.setPrice(50.0);
        sampleProduct2.setCategory(Category.FOOD);

        products.add(sampleProduct2);

        Product sampleProduct3 = new Product();
        sampleProduct3.setId(3L);
        sampleProduct3.setName("Harry Potter");
        sampleProduct3.setPrice(100.0);
        sampleProduct3.setCategory(Category.BOOKS);

        products.add(sampleProduct3);

        productRepository.saveAll(products);
    }
}
