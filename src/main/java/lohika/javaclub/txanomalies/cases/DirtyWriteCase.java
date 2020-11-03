package lohika.javaclub.txanomalies.cases;

import lohika.javaclub.txanomalies.data.ProductRepository;
import lohika.javaclub.txanomalies.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CyclicBarrier;

@Component
@RequiredArgsConstructor
@Slf4j
public class DirtyWriteCase {

    private final ProductRepository productRepository;

    private CyclicBarrier cyclicBarrier = new CyclicBarrier(2);

    @Lookup
    protected DirtyWriteCase self() {
        return this;
    }

    @Transactional
    public void updateAndFail(Product product) throws Exception {
        productRepository.saveAndFlush(product);
        log.info("Updated product [product={}]", product);
        cyclicBarrier.await();
        throw new RuntimeException("Rollback");
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public List<Product> twiceAllPrices() throws Exception {
        List<Product> products = productRepository.findAll();
        cyclicBarrier.await();

        products.forEach(product -> {
            product.setPrice(product.getPrice() * 2);
            productRepository.saveAndFlush(product);
        });

        return productRepository.findAll();
    }

}
