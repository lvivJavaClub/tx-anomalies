package lohika.javaclub.txanomalies.cases;

import lohika.javaclub.txanomalies.data.ProductRepository;
import lohika.javaclub.txanomalies.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

@Slf4j
@RequiredArgsConstructor
@Component
public class PhantomReadCase {

    private final ProductRepository productRepository;
    private final EntityManager entityManager;

    private CyclicBarrier cyclicBarrier = new CyclicBarrier(2);

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<Product> listProductWithPriceLessThan(Double lowBoundPrice) throws Exception {
        return getProductsWithPriceLessThen(lowBoundPrice);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public List<Product> safeListProductWithPricesLessThan(Double lowBoundPrice) throws Exception {
        return getProductsWithPriceLessThen(lowBoundPrice);
    }

    private List<Product> getProductsWithPriceLessThen(Double lowBoundPrice)
            throws InterruptedException, BrokenBarrierException {

        DoubleSummaryStatistics stats = productRepository.findAllByPriceLessThanEqual(lowBoundPrice).stream()
                .mapToDouble(Product::getPrice)
                .summaryStatistics();
        log.info("Stats of invalid products [stats={}]", stats);
        entityManager.clear();
        cyclicBarrier.await();

        return productRepository.findAllByPriceLessThanEqual(lowBoundPrice);
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
