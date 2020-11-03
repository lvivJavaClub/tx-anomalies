package lohika.javaclub.txanomalies.web;

import lohika.javaclub.txanomalies.cases.DirtyReadCase;
import lohika.javaclub.txanomalies.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/anomalies/dirty-read")
public class DirtyReadController {

    final DirtyReadCase dirtyReadCase;

    @GetMapping("/update")
    public void dirtyReadUpdate(Product product) throws Exception {
        dirtyReadCase.updateAndFail(product);
    }

    @GetMapping("/get")
    public Product dirtyReadUpdate(Integer id) throws Exception {
        return dirtyReadCase.getDirtyProduct(id);
    }

}
