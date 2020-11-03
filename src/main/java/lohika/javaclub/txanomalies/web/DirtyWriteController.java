package lohika.javaclub.txanomalies.web;

import lohika.javaclub.txanomalies.cases.DirtyWriteCase;
import lohika.javaclub.txanomalies.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/anomalies/dirty-write")
public class DirtyWriteController {

    final DirtyWriteCase dirtyWriteCase;

    @GetMapping("/update")
    public void dirtyWriteUpdate(Product product) throws Exception {
        dirtyWriteCase.updateAndFail(product);
    }

    @GetMapping("/twice")
    public List<Product> dirtyWriteTwicePrices() throws Exception {
        return dirtyWriteCase.twiceAllPrices();
    }

}
