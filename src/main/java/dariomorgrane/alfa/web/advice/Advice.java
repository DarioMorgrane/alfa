package dariomorgrane.alfa.web.advice;

import dariomorgrane.alfa.exception.WebClientLayerException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class Advice {

    @ResponseBody
    @ExceptionHandler(WebClientLayerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    String ClientLayerExceptionHandler(WebClientLayerException ex) {
        return ex.getMessage();
    }

}
