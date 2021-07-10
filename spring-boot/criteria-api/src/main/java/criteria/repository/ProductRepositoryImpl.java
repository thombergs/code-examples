package criteria.repository;

import criteria.model.Category;
import criteria.model.Product;
import criteria.model.Product_;
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
public class ProductRepositoryImpl {

    @PersistenceContext
    EntityManager em;

    public List<Product> findProductsByNameAndCategory(String name, String category) {

        CriteriaBuilder cBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Product> cQuery = cBuilder.createQuery(Product.class);

        /* FROM **/
        Root<Product> root = cQuery.from(Product.class);

        /* Conditions using Metamodel **/
        Predicate namePredicate = cBuilder.like(root.get(Product_.NAME), '%' + (name == null ? "" : name) + '%');
        Predicate categoryPredicate = cBuilder.equal(root.get(Product_.CATEGORY), (category == null ? Category.TOOLS : category));

        /* WHERE **/
        cQuery.where(namePredicate, categoryPredicate);

        /* Create Query object **/
        TypedQuery<Product> query = em.createQuery(cQuery);

        return query.getResultList();
    }
}
