package criteria.repository;

import criteria.model.Category;
import criteria.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> , JpaSpecificationExecutor<Product> {

    @Query("SELECT pr FROM Product pr WHERE pr.name like :name AND pr.category = :category ")
    List<Product> findProductsByNameAndCategory(@Param("name") String name , @Param("category") Category category);

    List<Product> findAllByNameLikeAndPriceLessThanEqual(
            String name,
            Double price
    );

    List<Product> findByName(String name);
}
