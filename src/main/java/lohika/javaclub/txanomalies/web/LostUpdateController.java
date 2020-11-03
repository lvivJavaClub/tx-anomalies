package lohika.javaclub.txanomalies.web;

import lohika.javaclub.txanomalies.cases.LostUpdateCase;
import lohika.javaclub.txanomalies.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/anomalies/lost-update")
public class LostUpdateController {

    private final LostUpdateCase lostUpdateCase;

    @GetMapping("/save")
    public Product lostUpdate(Product product) throws Exception {
        return lostUpdateCase.waitAndUpdate(product);
    }

    @GetMapping("/save-lost")
    public Product update(Product product) throws Exception {
        return lostUpdateCase.updateAndUnblock(product);
    }

}
/**
 localhost:8080/anomalies/write/lost-update/save-lost?id=1&price=5&name=Product_A
 */