package criteria.service;

import criteria.model.Category;
import criteria.model.Product;
import criteria.model.Product_;
import criteria.repository.ProductRepository;
import criteria.repository.ProductSpecification;
import criteria.repository.ProductSpecificationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class ProductService {

    @PersistenceContext
    EntityManager em;

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getResultQuery(ProductSpecificationBuilder specificationBuilder) {
        Specification<Product> spec = specificationBuilder.build();
        return productRepository.findAll(spec);
    }

    public List<Product> findProductsByNameAndCategory(String name, Category category) {

        CriteriaBuilder cBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Product> cQuery = cBuilder.createQuery(Product.class);

        /* FROM **/
        Root<Product> root = cQuery.from(Product.class);

        /* Conditions WITHOUT Metamodel **/
        Predicate namePredicate = cBuilder.like(root.get("name"), '%' + (name == null ? "" : name) + '%');
        Predicate categoryPredicate = cBuilder.equal(root.get("category"), (category == null ? Category.TOOLS : category));

        /* Conditions using Metamodel **/
//        Predicate namePredicate = cBuilder.like(root.get(Product_.NAME), '%' + (name == null ? "" : name) + '%');
//        Predicate categoryPredicate = cBuilder.equal(root.get(Product_.CATEGORY), (category == null ? Category.TOOLS : category));

        /* WHERE **/
        cQuery.where(namePredicate, categoryPredicate);

        /* Create Query object **/
        TypedQuery<Product> query = em.createQuery(cQuery);

        return query.getResultList();
    }

    public List<Product> getCheapProducts() {
        return productRepository.findAll(ProductSpecification.isCheap());
    }
}
