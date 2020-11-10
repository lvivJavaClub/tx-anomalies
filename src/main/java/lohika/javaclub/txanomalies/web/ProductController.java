package lohika.javaclub.txanomalies.web;

import lohika.javaclub.txanomalies.cases.RollbackCase;
import lohika.javaclub.txanomalies.data.ProductRepository;
import lohika.javaclub.txanomalies.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;
    private final RollbackCase rollbackCase;

    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/save")
    public Product createProduct(Product product) {
        return productRepository.saveAndFlush(product);
    }

    @GetMapping("/discount")
    public List<Product> discount(Integer discount) {
        rollbackCase.discount(getAllProducts(), discount);
        return getAllProducts();
    }

}

/*
http://localhost:8080/products/save?id=1&price=2&name=test
 */
