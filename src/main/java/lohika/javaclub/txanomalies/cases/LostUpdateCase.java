package lohika.javaclub.txanomalies.cases;

import lohika.javaclub.txanomalies.data.ProductRepository;
import lohika.javaclub.txanomalies.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.concurrent.CyclicBarrier;

@Slf4j
@RequiredArgsConstructor
@Component
public class LostUpdateCase {

    private final ProductRepository productRepository;
    private final EntityManager entityManager;
    private CyclicBarrier cyclicBarrier = new CyclicBarrier(2);

    @Transactional
    public Product waitAndUpdate(Product product) throws Exception {
        log.info("Do some proccessing with product [product={}]",
                productRepository.findById(product.getId()).orElseThrow());
        log.info("waitAndUpdate product [product={}]", product);
        entityManager.clear();
        cyclicBarrier.await();
        productRepository.saveAndFlush(product);
        return product;
    }

    public Product updateAndUnblock(Product product) throws Exception {
        log.info("updateAndUnblock product [product={}]", product);
        productRepository.saveAndFlush(product);
        cyclicBarrier.await();
        return product;
    }

}
