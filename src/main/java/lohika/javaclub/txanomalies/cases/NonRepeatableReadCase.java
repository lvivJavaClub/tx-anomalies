package lohika.javaclub.txanomalies.cases;

import lohika.javaclub.txanomalies.data.ProductRepository;
import lohika.javaclub.txanomalies.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

@Slf4j
@RequiredArgsConstructor
@Component
public class NonRepeatableReadCase {

    private final ProductRepository productRepository;
    private final EntityManager entityManager;

    private CyclicBarrier cyclicBarrier = new CyclicBarrier(2);

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<Product> listProductWithId(Integer productId) throws Exception {
        return getProducts(productId);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public List<Product> safeListProductWithId(Integer productId) throws Exception {
        return getProducts(productId);
    }

    private List<Product> getProducts(Integer productId) throws InterruptedException, BrokenBarrierException {
        List<Product> list = new ArrayList<>(2);
        list.add(productRepository.findById(productId).orElseThrow());
        entityManager.clear();
        cyclicBarrier.await();
        list.add(productRepository.findById(productId).orElseThrow());
        return list;
    }

    public Product twicePriceOfProduct(Integer productId) throws Exception {
        var product = productRepository.findById(productId).orElseThrow();
        log.info("Update price of product [product={}]", product);
        product.setPrice(product.getPrice() * 2);
        productRepository.saveAndFlush(product);
        cyclicBarrier.await();
        return product;
    }

}
