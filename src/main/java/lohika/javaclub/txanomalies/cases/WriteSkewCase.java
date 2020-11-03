package lohika.javaclub.txanomalies.cases;

import lohika.javaclub.txanomalies.data.ProductRepository;
import lohika.javaclub.txanomalies.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.concurrent.CyclicBarrier;

@Slf4j
@RequiredArgsConstructor
@Component
public class WriteSkewCase {

    private final ProductRepository productRepository;

    CyclicBarrier cyclicBarrier = new CyclicBarrier(2);

    @Transactional
    public Product twicePrice(Integer productId) throws Exception {
        checkTotalConstraintOfTwicedPrice(productId);
        log.info("Twice price [productId={}]", productId);
        cyclicBarrier.await();
        productRepository.twiceProductPrice(productId);
        return productRepository.findById(productId).orElseThrow();
    }

    private void checkTotalConstraintOfTwicedPrice(Integer productId) {
        double totalPrice = productRepository.findAll().stream()
                .mapToDouble(product ->
                        Objects.equals(product.getId(), productId) ?
                                product.getPrice() * 2 : product.getPrice())
                .sum();
        if (totalPrice > 15) {
            throw new IllegalStateException("Total price of all products exceeds limits");
        }
    }


}
