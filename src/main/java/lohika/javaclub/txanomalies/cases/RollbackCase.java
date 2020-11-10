package lohika.javaclub.txanomalies.cases;

import lohika.javaclub.txanomalies.data.ProductRepository;
import lohika.javaclub.txanomalies.model.InvalidPriceException;
import lohika.javaclub.txanomalies.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public abstract class RollbackCase {

    private final ProductRepository productRepository;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Lookup
    protected abstract RollbackCase self();

    @Transactional(transactionManager = "jdbcTxManager")
    public void discount(List<Product> products, int discountValue) {
        for (Product product : products) {
            try {
                self().discount(product, discountValue);
            } catch (Exception e) {
                log.error("Failed to make discount [productId={},msg={}]", product.getId(),e.getClass().getSimpleName());
            }
        }
    }

    @Transactional(transactionManager = "jdbcTxManager")
    public void discount(Product product, int discountValue) {
        log.info("discount [productId={},currentPrice={},value={}]", product.getId(), product.getPrice(), discountValue);
        if (product.getPrice() - discountValue <= 0) {
            throw new InvalidPriceException();
        }
        jdbcTemplate.update(
                "UPDATE product SET price = price - :discount WHERE id = :id",
                new MapSqlParameterSource(Map.of(
                        "id", product.getId(),
                        "discount", discountValue
                ))
        );
    }


}
