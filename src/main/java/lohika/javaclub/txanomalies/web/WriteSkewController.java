package lohika.javaclub.txanomalies.web;

import lohika.javaclub.txanomalies.cases.WriteSkewCase;
import lohika.javaclub.txanomalies.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/anomalies/write-skew")
public class WriteSkewController {

    final WriteSkewCase writeSkewCase;

    @GetMapping("/update")
    public Product twiceProductPrice(Integer id) throws Exception {
        return writeSkewCase.twicePrice(id);
    }

}
