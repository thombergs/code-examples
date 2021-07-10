package criteria.repository;

import criteria.model.Criteria;
import criteria.model.Operation;
import criteria.model.Product;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductSpecificationBuilder {

    private final List<Criteria> params;

    public ProductSpecificationBuilder() {
        params = new ArrayList<>();
    }

    public final ProductSpecificationBuilder with(final String key, final String operation, final Object value) {
        return with(false, key, operation, value);
    }

    public final ProductSpecificationBuilder with(final boolean orPredicate, final String key, final String operation, final Object value) {
        Operation op = Operation.getSimpleOperation(operation.charAt(0));
        if (op != null) {
            params.add(new Criteria(orPredicate, key, op, value));
        }
        return this;
    }

    public Specification<Product> build() {
        if (params.size() == 0)
            return null;

        Specification<Product> result = new ProductSpecification(params.get(0));

        for (int i = 1; i < params.size(); i++) {
            result = params.get(i).isOrPredicate()
                    ? Objects.requireNonNull(Specification.where(result)).or(new ProductSpecification(params.get(i)))
                    : Objects.requireNonNull(Specification.where(result)).and(new ProductSpecification(params.get(i)));
        }
        return result;
    }
}
