package criteria.controller;

import criteria.model.PredicateEnum;
import criteria.model.Product;
import criteria.repository.ProductRepository;
import criteria.repository.ProductSpecificationBuilder;
import criteria.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Autowired
    private ProductService productService;

    @GetMapping(value = "/product/{search}/{predicate}")
    public List<Product> search(@PathVariable(value = "search") String search, @PathVariable(value = "predicate") PredicateEnum predicate) {
        ProductSpecificationBuilder specificationBuilder = new ProductSpecificationBuilder();
        Pattern pattern = Pattern.compile("(\\w+?)([:<%>])(\\w+?),");
        Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            if (predicate.equals(PredicateEnum.OR)) {
                specificationBuilder.with(true, matcher.group(1), matcher.group(2), matcher.group(3));
            } else {
                specificationBuilder.with(matcher.group(1), matcher.group(2), matcher.group(3));
            }
        }
        return productService.getResultQuery(specificationBuilder);
    }
}
