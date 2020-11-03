package lohika.javaclub.txanomalies.data;

import lohika.javaclub.txanomalies.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findAllByPriceLessThanEqual(Double price);

    @Modifying
    @Query(
            value = "UPDATE product set price = price * 2 where id = :productId"
    )
    void twiceProductPrice(@Param("productId") Integer productId);

}
