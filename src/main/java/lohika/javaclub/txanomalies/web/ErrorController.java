package lohika.javaclub.txanomalies.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class ErrorController {

    @ResponseBody
    @ExceptionHandler
    public String onError(Exception e) {
        log.error(e.getMessage(), e);
        return e.getMessage();
    }

}
