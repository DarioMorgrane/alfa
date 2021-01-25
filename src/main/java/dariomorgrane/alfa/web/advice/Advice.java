package dariomorgrane.alfa.web.advice;

import dariomorgrane.alfa.exception.ClientLayerException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class Advice {

    @ResponseBody
    @ExceptionHandler(ClientLayerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    String WebLayerExceptionHandler(ClientLayerException ex) {
        return ex.getMessage();
    }

}
