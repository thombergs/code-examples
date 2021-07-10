package criteria.repository;

import criteria.model.Category;
import criteria.model.Criteria;
import criteria.model.Product;
import criteria.model.Product_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class ProductSpecification implements Specification<Product> {

    private Criteria criteria;

    public ProductSpecification(final Criteria criteria) {
        super();
        this.criteria = criteria;
    }

    public Criteria getCriteria() {
        return criteria;
    }

    @Override
    public Predicate toPredicate(final Root<Product> root, final CriteriaQuery<?> cQuery, final CriteriaBuilder cBuilder) {
        switch (criteria.getOperation()) {
            case EQ:
                return cBuilder.equal(root.get(criteria.getKey()), criteria.getValue());
            case GT:
                return cBuilder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case LT:
                return cBuilder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case LIKE:
                return cBuilder.like(root.get(criteria.getKey()), '%' + criteria.getValue().toString() + '%');
            default:
                return null;
        }
    }

    public static Specification<Product> belongsToCategory(List<Category> categories) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.in(root.get(Product_.CATEGORY)).value(categories);
    }

    public static Specification<Product> hasNameLike(String name) {
        return ((root, cQuery, cBuilder) -> cBuilder.like(root.get(Product_.NAME), '%' + (name == null ? "" : name) + '%'));
    }

    public static Specification<Product> isCheap() {
        return ((root, cQuery, cBuilder) -> cBuilder.lessThan(root.get(Product_.PRICE), 10));
    }

    public static Specification<Product> getCheapProductsWithNameLike(String name) {
        return Specification.where(ProductSpecification.isCheap().and(
                ProductSpecification.hasNameLike(name)
        ));
    }
}
