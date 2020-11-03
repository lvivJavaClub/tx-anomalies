package lohika.javaclub.txanomalies.cases;

import lohika.javaclub.txanomalies.data.ProductRepository;
import lohika.javaclub.txanomalies.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CyclicBarrier;

@Component
@RequiredArgsConstructor
@Slf4j
/**
 * http://localhost:8080/dirty-read/update?id=1&price=50&name=test
 * http://localhost:8080/dirty-read/get?id=1
 */
public class DirtyReadCase {

    private final ProductRepository productRepository;

    private CyclicBarrier cyclicBarrier = new CyclicBarrier(2);

    @Transactional
    public void updateAndFail(Product product) throws Exception {
        productRepository.saveAndFlush(product);
        log.info("Updated product [product={}]", product);
        cyclicBarrier.await();
        throw new RuntimeException("Rollback");
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public Product getDirtyProduct(Integer id) throws Exception {
        Product product = productRepository.findById(id).orElseThrow();
        log.info("Get product [product={}]", product);
        cyclicBarrier.await();
        return product;
    }

}
